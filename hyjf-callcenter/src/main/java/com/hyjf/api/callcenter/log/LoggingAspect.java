/**
 * Description: 呼叫中心调用Log切面文件
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 * Created at: 2017年7月16日 下午10:09:47
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.api.callcenter.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hyjf.callcenter.base.BaseResultBean;
import com.hyjf.callcenter.base.BaseDefine;
import com.hyjf.common.log.LogUtil;

/**
 * @author 刘彬
 */
@Component
@Aspect
public class LoggingAspect {
	private static Logger _log = LoggerFactory.getLogger(LogUtil.class);
	
    /**
     * 定义一个切点，此处定义切面对象为针对service
     */
    @Pointcut("execution(* com.hyjf..*Server.*(..))")
    public void pointcut() {
    }
	
	
    /**
     * 在方法执行前进行，输出类名，方法名
     * 
     * @param jp
     */
    @Before("pointcut()")
    public void beforeMethod(JoinPoint jp) {
        // 获取类名
        String className = jp.getTarget().getClass().toString();
        // 获取方法名
        String methodName = jp.getSignature().getName();
        // 获取方法参数
        Object[] arguments = jp.getArgs();
        // 遍历获取参数类型，以及参数值
        StringBuffer sb = new StringBuffer();
        if (arguments != null && arguments.length > 0) {
            for (Object argument : arguments) {
                if (argument != null
                	&& !argument.getClass().getSimpleName().equals("RequestFacade") 
            		&& !argument.getClass().getSimpleName().equals("ResponseFacade") ) {
                    sb.append("参数类型(" + argument.getClass().getSimpleName() + ")");
                    sb.append("参数值(" + argument.toString() + ")");
                }
            }
        }
        _log.info(BaseDefine.MSG_CALLCENTER_COLON+"["+className+"."+methodName+"]开始");
        _log.info(BaseDefine.MSG_CALLCENTER_COLON+sb.toString());
        
    }
    
    /**
     * 方法执行完后执行，输出类名，方法名，执行结果
     * 
     * @param jp
     */
    @AfterReturning(pointcut="pointcut()",returning="rvt")
    public void afterReturningMethod(JoinPoint jp,BaseResultBean rvt) {
        // 获取类名
        String className = jp.getTarget().getClass().toString();
        // 获取方法名
        String methodName = jp.getSignature().getName();
        // 获取被通知方法的执行结果
        String statusDesc = rvt.getStatusDesc();
        _log.info(BaseDefine.MSG_CALLCENTER_COLON+"["+className+"."+methodName+"]"+statusDesc);
    }
    
    /**
     * 系统出现异常后执行 输出类名，方法名，异常信息
     * 
     * @param jp
     * @param ex
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint jp, Exception ex) {
        // 获取类名
        String className = jp.getTarget().getClass().toString();
        // 获取方法名
        String methodName = jp.getSignature().getName();
        // 获取方法参数
        Object[] arguments = jp.getArgs();
        // 遍历获取参数类型，以及参数值
        StringBuffer sb = new StringBuffer();
        if (arguments != null && arguments.length > 0) {
            for (Object argument : arguments) {
                if (argument != null
                	&& !argument.getClass().getSimpleName().equals("RequestFacade") 
            		&& !argument.getClass().getSimpleName().equals("ResponseFacade") ) {
                    sb.append("参数类型(" + argument.getClass().getSimpleName() + ")");
                    sb.append("参数值(" + argument.toString() + ")");
                }
            }
        }
        // 获取被通知方法的执行结果
        _log.error(BaseDefine.MSG_CALLCENTER_COLON+"["+className+"."+methodName+"] 执行异常。");
        _log.error(BaseDefine.MSG_CALLCENTER_COLON+sb.toString());
        _log.error(BaseDefine.MSG_CALLCENTER_COLON, ex);
    }
}

	