package cn.wildfirechat.app.mapper;

import cn.wildfirechat.app.entity.Device;
import cn.wildfirechat.app.entity.Group;
import cn.wildfirechat.app.pojo.MdmResponeDevice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wy
 * 2019-07-11 11:03
 */
public interface DeviceMapper {
    /**
     * 登陆
     * @param imei
     * @param token
     * @return Device
     */
    Device login(@Param("imei") String imei, @Param("token")String token);
    /**
     * 批量插入
     * @param deviceList
     */
    void insertList(List<MdmResponeDevice> deviceList);
    /**
     * 删除公司下设备
     * @param companyId
     */
    void delByCompany(String companyId);
    /**
     * 获取分组列表
     * @param device
     * @return List<Group>
     * */
    List<Group> getGroupList(Device device);

}
