package cn.wildfirechat.app.api;

import cn.wildfirechat.app.RestResult;
import cn.wildfirechat.app.ServiceImpl;
import cn.wildfirechat.app.entity.Device;
import cn.wildfirechat.app.entity.Group;
import cn.wildfirechat.app.entity.GroupMember;
import cn.wildfirechat.app.pojo.RequestModel;
import cn.wildfirechat.app.pojo.ResponeResultModel;
import cn.wildfirechat.app.service.GroupMemberService;
import cn.wildfirechat.app.utils.FastJsonUtils;
import cn.wildfirechat.pojos.InputGetUserInfo;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "member")
public class GroupMemberController {

    //public static org.apache.logging.log4j.Logger log = LogManager.getLogger(GroupMemberController.class);

    @Autowired
    private GroupMemberService groupMemberService;

}
