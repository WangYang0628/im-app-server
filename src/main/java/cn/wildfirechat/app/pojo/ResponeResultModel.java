package cn.wildfirechat.app.pojo;


/**
 * 请求数据返回状态
 * 
 * @author liuchunhe
 * 
 */
public class ResponeResultModel {

	/**
	 * 接口是否调用成功
	 */
	private int resultCode;

	/**
	 * 接口返回数据
	 */
	private String data;

	/**
	 * 接口调用失败错误说明
	 */
	private String message;// 内部人自己看的


	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ResponeResultModel{" +
				"resultCode=" + resultCode +
				", data=" + data +
				", message='" + message + '\'' +
				'}';
	}
}
