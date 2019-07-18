package cn.wildfirechat.app.entity;

public class GroupMember {
    private String gid;
    private String mid;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Override
    public String toString() {
        return "GroupMember{" +
                "gid='" + gid + '\'' +
                ", mid='" + mid + '\'' +
                '}';
    }
}
