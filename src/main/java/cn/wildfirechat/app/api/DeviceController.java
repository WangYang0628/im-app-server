package cn.wildfirechat.app.api;

import cn.wildfirechat.app.RestResult;
import cn.wildfirechat.app.entity.Device;
import cn.wildfirechat.app.entity.Group;
import cn.wildfirechat.app.pojo.LoginRequest;
import cn.wildfirechat.app.pojo.LoginResponse;
import cn.wildfirechat.app.service.DeviceService;
import cn.wildfirechat.app.utils.FastJsonUtils;
import cn.wildfirechat.app.utils.LoggerUtils;
import cn.wildfirechat.common.ErrorCode;
import cn.wildfirechat.pojos.OutputGetIMTokenData;
import cn.wildfirechat.sdk.UserAdmin;
import cn.wildfirechat.sdk.model.IMResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author wy
 * 2019-07-11 11:04
 */
@RestController
@RequestMapping(value = "device")
public class DeviceController {
    public static Logger log = LogManager.getLogger(DeviceController.class);

    @Autowired
    DeviceService deviceService;

    @RequestMapping(value = "login")
    @ResponseBody
    public RestResult login(@RequestBody LoginRequest request) {
        if(request == null || request.getImei() == null || request.getClientId() == null){
            return RestResult.error(RestResult.RestCode.ERROR_PARAM);
        }
        if(request.getToken() == null){
            return RestResult.error(RestResult.RestCode.ERROR_TOKEN);
        }
        //使用imei及token查询用户
        Device device = deviceService.login(request.getImei(),request.getToken());
        //用户不存在
        if (device == null) {
            log.info("设备不存在 {}", "imei:"+device.getImei()+",token:"+device.getToken());
            return RestResult.error(RestResult.RestCode.ERROR_UNACTIVATED_SERVICE);
        }
        try {
            boolean isNewUser = false;
            //使用设备id获取token
            IMResult<OutputGetIMTokenData> tokenResult = UserAdmin.getUserToken(device.getDeviceId(), request.getClientId());
            if (tokenResult.getErrorCode() != ErrorCode.ERROR_CODE_SUCCESS) {
                log.error("Get token failure {}", tokenResult.code);
                return RestResult.error(RestResult.RestCode.ERROR_SERVER_ERROR);
            }
            //返回设备id，token和是否新建
            LoginResponse response = new LoginResponse();
            response.setDeviceId(device.getDeviceId());
            response.setToken(tokenResult.getResult().getToken());
            response.setRegister(isNewUser);
            return RestResult.ok(FastJsonUtils.toJSONNoFeatures(response));
        } catch (Exception e) {
            LoggerUtils.catchError(e, "login");
            return RestResult.error(RestResult.RestCode.ERROR_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "groupList")
    @ResponseBody
    public RestResult groupList(@RequestBody Device device) {
        if (device == null || device.getDeviceId() == null) {
            return RestResult.error(RestResult.RestCode.ERROR_PARAM);
        }
        List<Group> groupList = deviceService.getGroupList(device);
        return RestResult.ok(FastJsonUtils.toJSONNoFeatures(groupList));
    }

}
