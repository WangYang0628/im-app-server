package cn.wildfirechat.app.mqtt;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)//如果多个自定义ApplicationRunner，用来标明执行顺序
public class MqttClientService implements ApplicationRunner {


	//思必拓Mqtt
	private static String MQTT_HOST = "tcp://mqtt.speedata.cn:1883";
	private  static String MQTT_CLIENT = "ptt-app-server";
	private static MqttClient client;
	private static MqttConnectOptions options;

	private MqttClientService() {
	}

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		init();
	}
  
	/**
	 * 初始化
    */
	public static void init() {
		try {
			System.out.println("init MQTTClientService");
			// host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
			System.out.println("MQTT_HOST:"+MQTT_HOST);
			client = new MqttClient(MQTT_HOST, MQTT_CLIENT, new MemoryPersistence());
			// MQTT的连接设置
			options = new MqttConnectOptions();
			// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，设置为true表示每次连接到服务器都以新的身份连接
			options.setCleanSession(true);
			// 设置连接的用户名
			options.setUserName("speedata");
			// 设置连接的密码
			options.setPassword("speedata".toCharArray());
			// 设置超时时间 单位为秒
			options.setConnectionTimeout(50);
			// 设置会话心跳时间 单位为秒 服务器会每隔6秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
			options.setKeepAliveInterval(10);
			// TLS连接配置
			// options.setSocketFactory(
			// SslUtil.getSocketFactory(caFilePath, clientCrtFilePath, clientKeyFilePath, "cs123456"));

			// 设置回调
			client.setCallback(new PushCallback(MQTT_CLIENT));
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 连接到MQTT
    */
	public static void connect() {
		System.out.println("Start connect----------");
		try {
			if(client==null){
				init();
			}
			if(client.isConnected()){
				return;
			}
			client.connect(options);
			//掉线重连需要重新订阅
			//组件安装(公司注册)
			client.subscribe("component/install");
			//组件卸载(公司注销)
			client.subscribe("component/unload");
			//组件安装(设备注册)
			client.subscribe("device/register");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
   
	/**
	 * 断开连接到MQTT
    */
	public void disconnect() {
		System.out.println("Start disconnect----------");
		try {
			client.disconnect();
		} catch (MqttSecurityException e) {
			e.printStackTrace();
		} catch (MqttException e) {
           e.printStackTrace();
		}
	}
   
	/** 
	 * 发布消息
	 * @param topic 主题
	 * @param msg 消息
	*/
	public static void publish(String topic, byte[] msg,int qos,boolean retained) throws Exception{
		System.out.println("Start publish----------");
		if(client==null){
			init();
		}
		if(!client.isConnected()){
			client.connect(options);
			//掉线重连需要重新订阅
			//client.subscribe("$SYS/brokers/+/clients/+/connected");
			//client.subscribe("$SYS/brokers/+/clients/+/disconnected");
			client.unsubscribe("$SYS/brokers/+/clients/+/connected");
			client.unsubscribe("$SYS/brokers/+/clients/+/disconnected");
		}
        MqttTopic mqttTopic = client.getTopic(topic);
        MqttDeliveryToken messageToken = mqttTopic.publish(msg, qos, retained);
        System.out.println("publish success==>"+messageToken.getMessage());

	}

		      	     
}
