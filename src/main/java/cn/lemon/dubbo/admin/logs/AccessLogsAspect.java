package cn.lemon.dubbo.admin.logs;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.lemon.framework.utils.JsonUtil;

@Aspect
@Component
public class AccessLogsAspect {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    //定义一个切入点
    @Pointcut("execution(public * cn.lemon.dubbo.admin.controller..**.*(..))")
    public void init(){
    
    }
  
    @Before(value="init()")  
    public void before(JoinPoint point){
        Object[] args = point.getArgs();
        List<Object> methodArgs = new ArrayList<Object>();
        for(Object arg: args) {
        	if (!(arg instanceof RequestFacade) && !(arg instanceof ResponseFacade)) {
        		methodArgs.add(JsonUtil.writeValue(arg));
        	}
        }
        logger.info("args: {}, methodName: {}.{}", methodArgs, point.getTarget(), 
        		point.getSignature().getName());
    }
    
    @AfterReturning(pointcut = "init()", returning = "result")  
    public void afterReturn(JoinPoint point, Object result) {
		logger.info("methodName {}.{} result values: {}", point.getTarget(), 
				point.getSignature().getName(), JsonUtil.writeValue(result));
    }
      
    @AfterThrowing(value="init()", throwing = "ex")
    public void throwing(JoinPoint point, Throwable ex){
		logger.error("throwing: {}", ex.getMessage());
    }
    
}
