package com.hyjf.app.activity.inviteseven;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.app.BaseController;
import com.hyjf.app.BaseDefine;
import com.hyjf.app.user.manage.AppUserDefine;
import com.hyjf.app.user.manage.AppUserService;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetJumpCommand;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Users;

@Controller
@RequestMapping(value = InviteSevenDefine.REQUEST_MAPPING)
public class InviteSevenController extends BaseController {
    public static final String API_WEB_URL = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL);
    
    private static final String GET_INVITELIST_API = "inviteseven/getInviteList";
    
    @Autowired
    private AppUserService appUserService;
    
    /**
     * 
     * 六月份活动
     * @author zhangjp
     * @return
     */
    @RequestMapping(value = InviteSevenDefine.INIT_REQUEST_MAPPING)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(InviteSevenController.class.toString(), InviteSevenDefine.INIT_REQUEST_MAPPING);
        ModelAndView modelAndView = new ModelAndView(InviteSevenDefine.ACTIVITY_INVITESEVEN_PATH);
        
        String sign = request.getParameter("sign");
        String version = request.getParameter("version");
        String jumpCommand = GetJumpCommand.getLinkJumpPrefix(request, version);
        modelAndView.addObject("jumpCommand", jumpCommand);
        modelAndView.addObject("sign", sign);
        modelAndView.addObject("version", version);
        if(request.getRequestURL()!=null && request.getServletPath() !=null){
            modelAndView.addObject("serverUrl", request.getRequestURL().toString().replace(request.getServletPath(), ""));
        }
        
        if(StringUtils.isEmpty(sign)){
            modelAndView.addObject("isLogin", 0);
            modelAndView.addObject("loginUrl", jumpCommand + "://jumpLogin/?");
            return modelAndView;
        }
        
        Integer userId = SecretUtil.getUserIdNoException(sign);
        
        if(Validator.isNull(userId)){
            modelAndView.addObject("isLogin", 0);
            modelAndView.addObject("loginUrl", jumpCommand + "://jumpLogin/?");
        }else {
            Users user = appUserService.getUserByUserId(Integer.valueOf(userId));
            String imghost = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
            imghost = imghost.substring(0, imghost.length() - 1);// http://cdn.huiyingdai.com/
            String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.REQUEST_HOME.substring(1, AppUserDefine.REQUEST_HOME.length()) + "/";
            webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
            // 上传文件的CDNURL
            if (StringUtils.isNotEmpty(user.getIconurl())) {
                // 实际物理路径前缀
                String fileUploadRealPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.real.path"));
                modelAndView.addObject("iconUrl",imghost + fileUploadRealPath + user.getIconurl());
            } else {
                 modelAndView.addObject("iconUrl",StringUtils.EMPTY);
            }
            
            modelAndView.addObject("isLogin", 1);
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("loginUrl", jumpCommand + "://jumpLogin/?");
            modelAndView.addObject("webCatLink", PropUtils.getSystem("hyjf.wechat.invite.url")+userId+".html");
        }
        
        return modelAndView;
    }
    
    @ResponseBody
    @RequestMapping(value = InviteSevenDefine.GET_INVITELIST_MAPPING, produces = "application/json; charset=utf-8")
    public String searchInviteList(HttpServletRequest request, HttpServletResponse response) {
        
        LogUtil.startLog(this.getClass().toString(), InviteSevenDefine.GET_INVITELIST_MAPPING);
        
        String userId = request.getParameter("userId");
        String investType = request.getParameter("investType");
        String page = request.getParameter("page");
        String pageSize = request.getParameter("pageSize");

        String result = "";
        if(StringUtils.isEmpty(userId)){
            
        }else {
            Map<String, String> params = new HashMap<String, String>();
            // 用户编号
            params.put("userId", String.valueOf(userId));
            params.put("investType", investType);
            params.put("page", page);
            params.put("pageSize", pageSize);

            String getInviteListUrl = API_WEB_URL + GET_INVITELIST_API;
            
            
            result = HttpClientUtils.post(getInviteListUrl, params);
            
            
        }
        
        LogUtil.endLog(this.getClass().toString(), InviteSevenDefine.GET_INVITELIST_MAPPING);
        return result;
    }
    
    /**
     * 下载二维码
     * @param request
     * @param response
     * @param form
     */
    @RequestMapping(value = InviteSevenDefine.DOWNLOAD_ACTION, method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response){
            LogUtil.startLog(this.getClass().getName(), InviteSevenDefine.DOWNLOAD_ACTION);
            
            String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
            String fileUploadRealPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.real.path"));
            
            String sign = request.getParameter("sign");
            Integer userId = SecretUtil.getUserIdNoException(sign);
            
            try {
                this.getServletFile(request, response, webhost + fileUploadRealPath+String.valueOf(userId)+".jpg", String.valueOf(userId)+".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    /**
   * 通过HTTP方式获取文件,文件类型.jpg
   *
   * @param strUrl
   * @param fileName
   * @return
   * @throws IOException
   */
  public boolean getServletFile(HttpServletRequest request, HttpServletResponse response, String strUrl, String fileName) throws IOException {

      URL url = new URL(strUrl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
      // http正文内，因此需要设为true, 默认情况下是false;
      conn.setDoOutput(true);
      // 设置是否从httpUrlConnection读入，默认情况下是true;
      conn.setDoInput(true);
      // Post 请求不能使用缓存
      conn.setUseCaches(false);
      conn.setRequestProperty("Content-type", "application/x-java-serialized-object");
      conn.setRequestProperty("Accept-Charset", "UTF-8");
      // 设定请求的方法为"POST"，默认是GET
      conn.setRequestMethod("POST");
      DataInputStream input =null; 
      ServletOutputStream output = null;
      byte[] buffer = new byte[1024];
      int count = 0;
      try {
          output = response.getOutputStream();
          response.reset();
          response.setContentType("image/jpeg;charset=utf-8");
          response.setHeader("content-disposition", "attachment;filename=" + new String((fileName).getBytes("UTF-8"), "ISO8859-1"));
          input=new DataInputStream(conn.getInputStream());
          while ((count = input.read(buffer)) != -1) {
              output.write(buffer, 0, count);
          }
      } catch (Exception e) {
          e.printStackTrace();
      } finally {
          output.flush();
          output.close();
          input.close();
      }
      return true;
  }


}
