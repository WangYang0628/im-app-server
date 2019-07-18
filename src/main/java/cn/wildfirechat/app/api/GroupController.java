package cn.wildfirechat.app.api;

import cn.wildfirechat.app.RestResult;
import cn.wildfirechat.app.Service;
import cn.wildfirechat.app.pojo.AddGroupMember;
import cn.wildfirechat.app.pojo.RequestModel;
import cn.wildfirechat.app.utils.LoggerUtils;
import cn.wildfirechat.pojos.*;
import cn.wildfirechat.sdk.GroupAdmin;
import cn.wildfirechat.sdk.model.IMResult;
import cn.wildfirechat.sdk.utilities.AdminHttpUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wy
 * 2019-07-08 14:17
 */
@RestController
@RequestMapping(value = "group")
public class GroupController {
    public static org.apache.logging.log4j.Logger log = LogManager.getLogger(GroupController.class);

    @Autowired
    private Service mService;

    @RequestMapping(value = "create")
    @ResponseBody
    public RestResult create(@RequestBody RequestModel<PojoGroup> requestModel) {
        if(requestModel.getCompanyId() == null){
            return RestResult.error(RestResult.RestCode.ERROR_PARAM);
        }
        PojoGroup pojoGroup = requestModel.getData();
        if(pojoGroup == null || pojoGroup.getGroup_info() == null){
            return RestResult.error(RestResult.RestCode.ERROR_PARAM);
        }
        //String operator, PojoGroupInfo group_info, List<PojoGroupMember > members, List<Integer> to_lines, MessagePayload notify_message
        PojoGroupInfo pojoGroupInfo = pojoGroup.getGroup_info();
        List<PojoGroupMember> members = pojoGroup.getMembers();
        List<Integer> toLines = new ArrayList<>();
        toLines.add(0);
        try{
            IMResult<OutputCreateGroupResult> outputCreateGroupResult = GroupAdmin.createGroup("app-server",pojoGroupInfo,members,toLines,new MessagePayload());
            //server服务器错误
            if(outputCreateGroupResult.getCode() != 0){
                log.error("Create group failure {}", outputCreateGroupResult.msg);
                return RestResult.error(RestResult.RestCode.ERROR_SERVER_ERROR);
            }
            String groupId = outputCreateGroupResult.getResult().getGroup_id();
            //TODO 插入关联信息

        }catch (Exception e){
            LoggerUtils.catchError(e, "creatGroup");
            return RestResult.error(RestResult.RestCode.ERROR_SERVER_ERROR);
        }
        return RestResult.ok(null);
    }

    @RequestMapping(value = "del")
    @ResponseBody
    public RestResult del(@RequestBody InputGetGroup group) {
        //String operator, String groupId, List<Integer> to_lines, MessagePayload notify_message
        List<Integer> toLines = new ArrayList<>();
        toLines.add(0);
        try{
            IMResult<Void> result = GroupAdmin.dismissGroup("app-server",group.getGroupId(),toLines,new MessagePayload());
            //server服务器错误
            if(result.getCode() != 0){
                log.error("Create group failure {}", result.msg);
                return RestResult.error(RestResult.RestCode.ERROR_SERVER_ERROR);
            }
        }catch (Exception e){
            LoggerUtils.catchError(e, "delGroup");
            return RestResult.error(RestResult.RestCode.ERROR_SERVER_ERROR);
        }
        return RestResult.ok(null);
    }

    @RequestMapping(value = "addMember")
    @ResponseBody
    public RestResult addMember(@RequestBody InputAddGroupMember groupMember) {
        if(groupMember == null || groupMember.getGroup_id() == null || groupMember.getMembers() == null){
            return RestResult.error(RestResult.RestCode.ERROR_PARAM);
        }
        String path = "/admin/group/member/add";
        groupMember.setOperator("app-server");
        groupMember.setTo_lines(new ArrayList<Integer>(){{add(0);}});
        groupMember.setNotify_message(new MessagePayload());
        try{
            IMResult<Void> result = AdminHttpUtils.httpJsonPost(path, groupMember, Void.class);
            //server服务器错误
            if(result.getCode() != 0){
                log.error("Add member failure {}", result.msg);
                return RestResult.error(RestResult.RestCode.ERROR_SERVER_ERROR);
            }
        }catch (Exception e){
            LoggerUtils.catchError(e, "addMember");
            return RestResult.error(RestResult.RestCode.ERROR_SERVER_ERROR);
        }
        return RestResult.ok(null);
    }

    @RequestMapping(value = "delMember")
    @ResponseBody
    public RestResult delMember(@RequestBody AddGroupMember groupMember) {
        //String operator, String groupId, List<String> groupMemberIds, List<Integer> to_lines, MessagePayload notify_message
        if(groupMember == null || groupMember.getGroupId() == null || groupMember.getMembers() == null){
            return RestResult.error(RestResult.RestCode.ERROR_PARAM);
        }
        List<String> groupMemberIds = new ArrayList<>(16);
        for(PojoGroupMember member : groupMember.getMembers()){
            groupMemberIds.add(member.getMember_id());
        }
        try{
            //RestResult loginResult = mService.login("86966102025157", "wefqfw548wefwef");
            IMResult<Void> result = GroupAdmin.kickoffGroupMembers("wZwUwUyy",groupMember.getGroupId(),groupMemberIds,new ArrayList<Integer>(){{add(0);}},new MessagePayload());
            //server服务器错误
            if(result.getCode() != 0){
                log.error("del member failure {}", result.msg);
                return RestResult.error(RestResult.RestCode.ERROR_SERVER_ERROR);
            }
        }catch (Exception e){
            LoggerUtils.catchError(e, "delMember");
            return RestResult.error(RestResult.RestCode.ERROR_SERVER_ERROR);
        }
        return RestResult.ok(null);
    }

}
