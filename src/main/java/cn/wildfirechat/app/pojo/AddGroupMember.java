package cn.wildfirechat.app.pojo;

import cn.wildfirechat.pojos.PojoGroupMember;

import java.util.List;

/**
 * @author wy
 * 2019-07-09 17:53
 */
public class AddGroupMember {
    private String groupId;
    private List<PojoGroupMember> members;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<PojoGroupMember> getMembers() {
        return members;
    }

    public void setMembers(List<PojoGroupMember> members) {
        this.members = members;
    }
}
