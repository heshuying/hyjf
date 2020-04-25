package com.hyjf.web.activity.recommend;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.http.URLCodec;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.QRCodeUtil;
import com.hyjf.web.BaseController;
import com.hyjf.web.user.invite.InviteController;
import com.hyjf.web.util.WebUtils;

@Controller
@RequestMapping(value = RecommendDefine.REQUEST_MAPPING)
public class RecommendController extends BaseController {
    @Autowired
    private RecommendService recommendService;
    
    /**
     * 
     * 推荐星活动数据加载
     * @author yyc
     * @return
     */
    @RequestMapping(value = RecommendDefine.INIT_ACTION, method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(RecommendDefine.THIS_CLASS, RecommendDefine.INIT_ACTION);
        ModelAndView modelAndView = new ModelAndView(RecommendDefine.INIT_PATH);
        String localhost = UploadFileUtils.getDoPath(PropUtils.getSystem("localhost.url"));
        Integer userId = WebUtils.getUserId(request);
//        Integer userId = 22400008;
        // URL base64编码
        String retUrl = URLCodec.encodeURL(RecommendDefine.RET_URL);
        modelAndView.addObject("loginUrl", RecommendDefine.LOGIN_REQUEST_MAPPING + "?retUrl=" + retUrl);
        if(userId==null||"".equals(userId)){
            modelAndView.addObject("status", 0);
            
        }else{
            modelAndView.addObject("inviteLink", localhost+"landingpage/init.do?from_id="+userId);
            modelAndView.addObject("shareUrl", PropUtils.getSystem("hyjf.wechat.mgm10.invite.url").replace("****", userId.toString()));
            //获取用户推荐星信息
            recommendService.getRecommendInfo(modelAndView,userId);
            //获取用户推荐星获得列表
            recommendService.getUserRecommendStarPrizeList(modelAndView,userId);
            //获取用户推荐星使用列表
            recommendService.getUserRecommendStarUsedPrizeList(modelAndView,userId);
            modelAndView.addObject("status", 1);
            modelAndView.addObject("userId", userId);
        }
        //获取兑奖的奖品列表
        recommendService.getPrizeChangeList(modelAndView, userId == null?"":String.valueOf(userId));
        //获取抽奖的奖品列表
        recommendService.getPrizeDrawList(modelAndView, userId == null?"":String.valueOf(userId));
        return modelAndView;
    }
    
    
    /**
     * 下载二维码
     * @param request
     * @param response
     * @param form
     */
    @RequestMapping(value = RecommendDefine.DOWNLOAD_ACTION, method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response){
          LogUtil.startLog(InviteController.class.getName(), RecommendDefine.DOWNLOAD_ACTION);
            
            String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
            webhost=webhost.substring(0,webhost.length()-1);
            String fileUploadRealPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.real.path"));
            String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
            Integer userId =  WebUtils.getUserId(request);
//            Integer userId = 22400008;
            
            try {
                QRCodeUtil.encode(PropUtils.getSystem("hyjf.wechat.mgm10.invite.url").replace("****", userId.toString()),String.valueOf(userId),filePhysicalPath + fileUploadRealPath, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try {
                this.getServletFile(request, response,webhost+ fileUploadRealPath+String.valueOf(userId)+".jpg", String.valueOf(userId)+".jpg");
                
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    /**
     * 
     * 兑奖校验
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = RecommendDefine.PRIZE_CHANGE_CHECK)
    public JSONObject prizeChangeCheck(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(RecommendDefine.THIS_CLASS, RecommendDefine.PRIZE_CHANGE_CHECK);
        
        Integer userId = WebUtils.getUserId(request);
//        Integer userId = 22400008;
      
        String groupCode = request.getParameter("groupCode");
        String changeCount = request.getParameter("changeCount");
        
        JSONObject resultJson = recommendService.prizeChangeCheck(String.valueOf(userId), groupCode, changeCount);
        
        LogUtil.endLog(RecommendDefine.THIS_CLASS, RecommendDefine.PRIZE_CHANGE_CHECK);
        
        return resultJson;
    }

    /**
     * 
     * 兑奖
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = RecommendDefine.DO_PRIZE_CHANGE)
    public JSONObject doPrizeChange(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(RecommendDefine.THIS_CLASS, RecommendDefine.DO_PRIZE_CHANGE);
        
        Integer userId = WebUtils.getUserId(request);
//        Integer userId = 22400008;
      
        String groupCode = request.getParameter("groupCode");
        String changeCount = request.getParameter("changeCount");
        
        JSONObject resultJson = recommendService.doPrizeChange(String.valueOf(userId), groupCode, changeCount);
        
        LogUtil.endLog(RecommendDefine.THIS_CLASS, RecommendDefine.DO_PRIZE_CHANGE);
        
        return resultJson;
    }

    /**
     * 
     * 抽奖
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = RecommendDefine.DO_PRIZE_DRAW)
    public JSONObject doPrizeDraw(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(RecommendDefine.THIS_CLASS, RecommendDefine.DO_PRIZE_DRAW);
        
        Integer userId = WebUtils.getUserId(request);
//        Integer userId = 22400008;
      
        
        JSONObject resultJson = recommendService.doPrizeDraw(String.valueOf(userId));
        
        LogUtil.endLog(RecommendDefine.THIS_CLASS, RecommendDefine.DO_PRIZE_DRAW);
        
        return resultJson;
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
