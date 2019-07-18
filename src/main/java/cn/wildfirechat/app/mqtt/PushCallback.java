package cn.wildfirechat.app.mqtt;


import cn.wildfirechat.app.entity.Device;
import cn.wildfirechat.app.pojo.MdmResponeDevice;
import cn.wildfirechat.app.service.DeviceService;
import cn.wildfirechat.app.utils.FastJsonUtils;
import cn.wildfirechat.app.utils.LoggerUtils;
import cn.wildfirechat.app.utils.PostUtil;
import cn.wildfirechat.app.utils.SpringContextHolder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 发布消息的回调类
 * 
 * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。 在回调中，将它用来标识已经启动了该回调的哪个实例。
 * 必须在回调类中实现三个方法：
 * 
 * public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。
 * 
 * public void connectionLost(Throwable cause)在断开连接时调用。
 * 
 * public void deliveryComplete(MqttDeliveryToken token)) 接收到已经发布的 QoS 1 或 QoS 2
 * 消息的传递令牌时调用。 由 MqttClient.connect 激活此回调。
 * 
 */
@Component
public class PushCallback implements MqttCallback {
	public static Logger log = LogManager.getLogger(PushCallback.class);
	private String instanceData;


	public PushCallback(String instance) {
		instanceData = instance;
	}
	public PushCallback() {
	}

	@Override
	public void connectionLost(Throwable cause) {
		//System.out.println("===========");
		// 连接丢失后，一般在这里面进行重连
		//System.out.println("设备连接断开：" + instanceData);
		log.error("设备连接断开：" + instanceData);
		MqttClientService.connect();
		/*if ( !Thread.currentThread().isInterrupted() ) {
			try {
				log.error("开始休眠");
                Thread.interrupted();
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			MqttClientService.connect();
			log.error("设备重连成功");
		}else {
			log.error("已经休眠！等待连接");
		}*/
	}

	/**
	 * 消息发送成功
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {

	}

	@Override
	public void messageArrived(String topic, MqttMessage arg1) throws Exception {
		//获取主题类型
		String type = topic.substring(topic.lastIndexOf("/")+1);
		Method method = PushCallback.class.getMethod(type,MqttMessage.class);
		method.invoke(PushCallback.class.newInstance(), arg1);

	}

	public void install(MqttMessage message){
		DeviceService deviceService = SpringContextHolder.getBean(DeviceService.class);
		Map<String,Object> payload = FastJsonUtils.stringToCollect(message.toString());
		String component = payload.get("component").toString();
		//只处理ptt组件
		if("PTT".equals(component)){
			String companyId = payload.get("companyId").toString();
			System.out.println(companyId);
			//设置查询条件
			Boolean stop = false;
			Integer pageSize = 10;
			Integer pageNo = 1;
			String result ;
			//TODO 请求MDM接口 获取公司下设备
			ExecutorService threadPool = Executors.newSingleThreadExecutor();
			while (!stop){
				Map<String,Object> params = new HashMap<>(16);
				params.put("pageSize",pageSize);
				params.put("pageNo",pageNo);
				params.put("companyId",companyId);
				PostUtil post = new PostUtil("http://127.0.0.1:8080/web-mdm/device/component/list",FastJsonUtils.toJSONString(params));
				Future<String> future = threadPool.submit(post);
				try{
					result = future.get();
					Map resultMap = FastJsonUtils.stringToCollect(result);
					List<MdmResponeDevice> deviceList = FastJsonUtils.toList(FastJsonUtils.toJSONString(resultMap.get("data")),MdmResponeDevice.class);
					//批量插入
					deviceService.insertList(deviceList);
					pageNo++;
					if(deviceList.size() < pageSize){
						stop = true;
					}
				}catch (Exception e){
					LoggerUtils.catchError(e,"component-install");
					threadPool.shutdown();
				}
			}
			threadPool.shutdown();
		}
	}

	public void unload(MqttMessage message){
		Map<String,Object> payload = FastJsonUtils.stringToCollect(message.toString());
		String component = payload.get("component").toString();
		//只处理ptt组件
		if("PTT".equals(component)){
			String companyId = payload.get("companyId").toString();
			System.out.println(companyId);
			//删除公司下所有设备
			DeviceService deviceService = SpringContextHolder.getBean(DeviceService.class);
			deviceService.delByCompany(companyId);
		}
	}


}