package cn.wildfirechat.app.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoggerUtils {
	public static Logger log = LogManager.getLogger(LoggerUtils.class);
	
	public static void catchError(Exception e,String method){
		log.info(method + "发生异常，请求中断！");
		String sOut = "";
	    StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement s : trace) {
            sOut += "\tat " + s + "\r\n";
        }
		log.error("The method "+ method + " occurs excetion:"+ e.toString() + "\r\n" + sOut);
	}

}
