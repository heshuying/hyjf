package com.hyjf.server.interceptor;

import java.util.LinkedList;
import java.util.Queue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.cache.SerializeUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.DesECBUtil;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.ServerApplication;
import com.hyjf.server.BaseResultBean;
import com.hyjf.server.util.SecretUtil;

/**
 * 
 * 平台对接第三方接口安全验证
 * 
 * @author 朱晓东
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年10月12日
 * @see 上午9:34:23
 */
@Component
public class SecretKeyInterceptor extends HandlerInterceptorAdapter {
     
    //@Autowired
    //private ServerApplicationService serverApplicationService;

	/**
	 * 在DispatcherServlet完全处理完请求后被调用
	 * 
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
	    
	    arg0.removeAttribute("secretKey");
	    arg0.removeAttribute("requestObject");
	}

	/**
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 * 
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	@SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	    LogUtil.infoLog(this.getClass().getName(), "preHandle", "<---------------------接口调用安全验证拦截器验证开始---------------------->");
	    //请求资源路径
        BaseResultBean resultBean = new BaseResultBean();
        String appId = request.getParameter("appId");
        String secretKey = request.getParameter("secretKey");
        String timeStamp = request.getParameter("timeStamp");
        String uniqueSign = request.getParameter("uniqueSign");
        String requestObject = request.getParameter("requestObject");
        if(CustomConstants.POST.equals(request.getMethod())) {// 如果是get请求重定向到异常页面
            if(StringUtils.isNotEmpty(appId) && StringUtils.isNotEmpty(secretKey) && StringUtils.isNotEmpty(timeStamp) && StringUtils.isNotEmpty(uniqueSign)) {
                if(StringUtils.isNotEmpty(appId) && appId.length()<32){
                    //获取到serverApplication
                    //ServerApplication serverApplication = serverApplicationService.getServerApplicationByAppid(appId);//取消查询数据库,在redis中进行查询
                    byte[] applicationByte = RedisUtils.get(("Third-Party-Application:"+appId).getBytes());
                    
                    if(applicationByte!=null && applicationByte.length>0){
                        ServerApplication serverApplication = (ServerApplication)SerializeUtils.unserialize(applicationByte);
                        if(serverApplication!=null){
                            if(serverApplication.getStatus().intValue()==1){
                                //得到appKey
                                String appKey = serverApplication.getAppkey();
                                String secretKeyMingwen = "";
                                String timeStampMingwen = "";
                                try {
                                	secretKeyMingwen = DesECBUtil.decrypt(secretKey, appKey);
                                	timeStampMingwen = DesECBUtil.decrypt(timeStamp, appKey);
								} catch (Exception e) {
									System.out.println("secretKey: " + secretKey);
									System.out.println("appId: " + appId);
									System.out.println("timeStamp: " + timeStamp);
									System.out.println("uniqueSign: " + uniqueSign);
									System.out.println("requestObject: " + requestObject);
									//e.printStackTrace();
									throw e;
								}
                                if(secretKeyMingwen.length()==8){
                                    if(MD5Utils.MD5(SecretUtil.sortByAscii(secretKeyMingwen+timeStampMingwen)).equals(uniqueSign)){
                                        //判断redis队列中是不是已经存在该安全码密钥
                                        byte[] appIdByte = RedisUtils.get(("Third-Party-API:"+appId).getBytes());
                                        Queue<String> queue = (LinkedList<String>)SerializeUtils.unserialize(appIdByte);
                                        if(queue!=null){
                                            //queue.clear();//删除要验证的密钥，正式环境时要删除这句
                                            if(!queue.contains(secretKeyMingwen)){
                                                if(queue.size() >= 200){    
                                                    queue.poll();//如果超出长度,入队时,先出队     
                                                }
                                                queue.offer(secretKeyMingwen);
                                                RedisUtils.set(("Third-Party-API:"+appId).getBytes(), SerializeUtils.serialize(queue));
                                                request.setAttribute("secretKey", secretKeyMingwen);
                                                if(requestObject!=null && !"".equals(requestObject)){
                                                	String requestObjectMingWen = StringUtils.EMPTY;
                                        			try {
                                        				requestObjectMingWen = DesECBUtil.decrypt(requestObject, secretKeyMingwen);
													} catch (Exception e) {
														System.out.println("requestObject解密出错!  requestObject=" + requestObject +"   secretKeyMingwen=" + secretKeyMingwen);
														throw e;
													}
                                                    request.setAttribute("requestObject", requestObjectMingWen);
                                                }
                                                return true;
                                            }else{
                                                resultBean.setStatus("1");
                                                resultBean.setStatusDesc("对接接口的应用存在重发攻击的可能性");
                                                JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
                                                return false;
                                            } 
                                        }else{
                                            Queue<String> queueNew = new LinkedList<String>();
                                            queueNew.offer(secretKeyMingwen);
                                            RedisUtils.set(("Third-Party-API:"+appId).getBytes(), SerializeUtils.serialize(queueNew));
                                            request.setAttribute("secretKey", secretKeyMingwen);
                                            if(requestObject!=null && !"".equals(requestObject)){
                                                request.setAttribute("requestObject", DesECBUtil.decrypt(requestObject, secretKeyMingwen));
                                            }
                                            return true;
                                        }
                                    }else{
                                        resultBean.setStatus("1");
                                        resultBean.setStatusDesc("对接接口的应用安全验证不通过");
                                        JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
                                        return false;
                                    }
                                }else{
                                    resultBean.setStatus("1");
                                    resultBean.setStatusDesc("对接接口的安全码密钥长度不符合要求");
                                    JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
                                    return false;
                                }
                            }else{
                                resultBean.setStatus("1");
                                resultBean.setStatusDesc("对接接口的应用身份没有被许可");
                                JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
                                return false;
                            }
                        }else{
                            resultBean.setStatus("1");
                            resultBean.setStatusDesc("对接接口的应用身份异常");
                            JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
                            return false;
                        } 
                    }else{
                        resultBean.setStatus("1");
                        resultBean.setStatusDesc("对接接口的应用身份不存在");
                        JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
                        return false;
                    }
               }else{
                   resultBean.setStatus("1");
                   resultBean.setStatusDesc("对接接口的应用编号异常");
                   JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
                   return false;
               }
            }else{
                resultBean.setStatus("1");
                resultBean.setStatusDesc("对接接口的应用参数不完整");
                JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
                return false;
            }
        }else{
            resultBean.setStatus("1");
            resultBean.setStatusDesc("接口方式只能POST方式请求");
            JSON.writeJSONStringTo(resultBean, response.getWriter(), SerializerFeature.WriteMapNullValue);
            return false;
        }
	}

}
