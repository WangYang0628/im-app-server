package cn.wildfirechat.app.aop;

import cn.wildfirechat.app.pojo.ResponeResultModel;
import cn.wildfirechat.app.utils.FastJsonUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component("myLog")
@Aspect
public class MyLog {
	public static Logger log = LogManager.getLogger(MyLog.class);
	
	//定义切点
	@Pointcut("execution(* cn.wildfirechat.app.api.*.*(..))")
	public void sayings(){}
	 
	/*@AfterThrowing(value="execution(* cn.wildfirechat.app.api.*.*(..))",throwing="e")
	public void afterThrowing(JoinPoint joinPoint, Exception e){
		log.info("异常通知!!请查看error文件");
		String sOut = "";
	    StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement s : trace) {
            sOut += "\tat " + s + "\r\n";
        }
		String methodName = joinPoint.getSignature().getName();
		log.error("The method " + methodName + " occurs excetion:"+ e.toString() + "\r\n" + sOut);
		 
	}*/

	/*@Before("sayings()")
	public void deBefore(JoinPoint joinPoint) throws Throwable {
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		// 记录下请求内容
		System.out.println("URL : " + request.getRequestURL().toString());
		System.out.println("HTTP_METHOD : " + request.getMethod());
		System.out.println("IP : " + request.getRemoteAddr());
		System.out.println("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
		System.out.println("ARGS : " + Arrays.toString(joinPoint.getArgs()));

	}*/
	//后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
	/*@After("sayings()")
	public void after(JoinPoint joinPoint){
		System.out.println("方法最后执行.....");

		System.out.println("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
		System.out.println("ARGS : " + Arrays.toString(joinPoint.getArgs()));
	}*/

	//环绕通知,环绕增强，相当于MethodInterceptor
	@Around("sayings()")
	public Object arround(ProceedingJoinPoint pjp) {
		String methodName = pjp.getSignature().getName();
		String className = pjp.getTarget().getClass().getName();
		Long beginTime = new Date().getTime();
		try {
			Object o =  pjp.proceed();
			Long time = new Date().getTime() - beginTime;
			log.info(className + "." + methodName + "---200--耗时:" + time + "ms");
			return o;
		} catch (Throwable e) {
			//log.info("异常通知!!请查看error文件");
			String sOut = "";
			StackTraceElement[] trace = e.getStackTrace();
			for (StackTraceElement s : trace) {
				sOut += "\tat " + s + "\r\n";
			}
			log.error("The method " + methodName + " occurs excetion:"+ e.toString() + "\r\n" + sOut);
			ResponeResultModel result = new ResponeResultModel();
			result.setMessage("操作异常");

			result.setResultCode(-1);
			Long time = new Date().getTime() - beginTime;
			log.info(className + "." + methodName + "---500--耗时:" + time + "ms");
			return FastJsonUtils.toJSONString(result);
		}
	}
}
