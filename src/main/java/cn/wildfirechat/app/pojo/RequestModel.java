package cn.wildfirechat.app.pojo;

public class RequestModel<T> {

    private String clientId;
    private T data;
    private String versionName;
    private String deviceId;
    private String companyId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "clientId='" + clientId + '\'' +
                ", data='" + data + '\'' +
                ", versionName='" + versionName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", companyId='" + companyId + '\'' +
                '}';
    }
}
