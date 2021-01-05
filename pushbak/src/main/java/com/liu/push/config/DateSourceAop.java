package com.sinosoft.push.config;

import com.liu.push.config.DynamicDataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class DateSourceAop {

    /**
     * 在访问主controller前做一个切面，根据请求的参数判断切换什么数据源
     */
    @Pointcut("execution(public * com.liu.push.main.controller.MainController.*(..))")//切入点描述 这个是controller包的切入点
    public void controllerLog(){}//签名，可以理解成这个切入点的一个名称
    
    @After("controllerLog()") //在切入点的方法run之前要干的
    public void logAfterController(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();//这个RequestContextHolder是Springmvc提供来获得请求的东西
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        String code = request.getParameter("code");
        //如果代码为shi 那么将push中的数据源切换成binhai
        	 if("shi".equals(code)){
             DynamicDataSourceContextHolder.setDataSourceKey("binhai");
             }else if("binhai".equals(code)){
             	DynamicDataSourceContextHolder.setDataSourceKey("heping");
        }
    }
    
    @Before("controllerLog()") //在切入点的方法run之前要干的
    public void logBeforeController(JoinPoint joinPoint) {
        //方法调用完后将配置的数据源清空
    		DynamicDataSourceContextHolder.clearDataSourceKey();
    }
}
