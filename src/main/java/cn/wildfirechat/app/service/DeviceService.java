package cn.wildfirechat.app.service;

import cn.wildfirechat.app.entity.Device;
import cn.wildfirechat.app.entity.Group;
import cn.wildfirechat.app.mapper.DeviceMapper;
import cn.wildfirechat.app.pojo.MdmResponeDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wy
 * 2019-07-11 11:02
 */
@Service
@Transactional(readOnly = true,rollbackFor = Exception.class)
public class DeviceService {

    @Autowired
    DeviceMapper deviceMapper;

    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public Device login(String imei, String token){
        return deviceMapper.login(imei,token);
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public void insertList(List<MdmResponeDevice> deviceList){
        deviceMapper.insertList(deviceList);
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public void delByCompany(String companyId){
        deviceMapper.delByCompany(companyId);
    }

    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public List<Group> getGroupList(Device device) {
        return deviceMapper.getGroupList(device);
    }

}
