package com.hyjf.web.user.invite;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.mybatis.model.customize.admin.AdminUserDetailCustomize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.QRCodeUtil;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.web.CouponUserListCustomize;
import com.hyjf.mybatis.model.customize.web.WebInviteRecordCustomize;
import com.hyjf.mybatis.model.customize.web.WebRewardRecordCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.coupon.CouponListBean;
import com.hyjf.web.coupon.CouponService;
import com.hyjf.web.user.login.LoginDefine;
import com.hyjf.web.user.regist.UserRegistDefine;
import com.hyjf.web.util.WebUtils;
/**
 * 邀请好友
 * @author Albert
 *
 */
@Controller
@RequestMapping(value = InviteDefine.REQUEST_MAPPING)
public class InviteController extends BaseController {

	@Autowired
	private InviteService inviteService;
	@Autowired
    private CouponService couponService;
	
	@RequestMapping(InviteDefine.TO_INVITE_ACTION)
	public ModelAndView toInvitePage(HttpServletRequest request, HttpServletResponse response, InviteBean form){
		LogUtil.startLog(InviteController.class.getName(), InviteDefine.TO_INVITE_ACTION);
		ModelAndView modeAndView = new ModelAndView(InviteDefine.TO_REWARD_PATH);
		String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
		String fileUploadRealPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.real.path"));
		
		Integer userId = WebUtils.getUserId(request);
		if(userId==null || userId==0){
			//session失效，跳转到登陆页
			return new ModelAndView(LoginDefine.INIT_PATH);
		}

        modeAndView.addObject("userId", userId);

        // 通过当前用户ID 查询用户所在一级分部,从而关联用户所属渠道
        // 合规自查添加
        // 20181205 产品需求, 屏蔽渠道,只保留用户ID
//        AdminUserDetailCustomize userUtmInfo = inviteService.selectUserUtmInfo(userId);
//		String utmId = null;
//		String utmSource = null;
//		if (userUtmInfo != null) {
//            utmId = userUtmInfo.getSourceId().toString();
//            utmSource = userUtmInfo.getSourceName();
//            modeAndView.addObject("webCatLink", PropUtils.getSystem("hyjf.wechat.invite.url") + "refferUserId=" +userId + "&utmId=" + utmId + "&utmSource=" + utmSource);
//            modeAndView.addObject("inviteLink", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+"/landingpage/init.do?refferUserId="+userId + "&utmId=" + utmId + "&utmSource=" + utmSource);
//        }else {
            // 已确认未关联渠道的用户
            modeAndView.addObject("webCatLink", PropUtils.getSystem("hyjf.wechat.invite.url") + "refferUserId=" +userId);
            modeAndView.addObject("inviteLink", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+"/landingpage/init.do?refferUserId="+userId);
//        }

        modeAndView.addObject("download2wm", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL));
		try {
//		    if (userUtmInfo != null){
//                QRCodeUtil.encode(PropUtils.getSystem("hyjf.wechat.invite.url") + "refferUserId=" +userId + "&utmId=" + utmId + "&utmSource=" + utmSource, String.valueOf(userId),filePhysicalPath + fileUploadRealPath, false);
//            }else {
                QRCodeUtil.encode(PropUtils.getSystem("hyjf.wechat.invite.url") + "refferUserId=" +userId, String.valueOf(userId),filePhysicalPath + fileUploadRealPath, false);
//            }

		} catch (Exception e) {
			e.printStackTrace();
		}
		//邀请记录
		int inviteCount= inviteService.queryInviteCount(userId);
		modeAndView.addObject("inviteCount", inviteCount);
		
		BigDecimal rewardRecordsSum = inviteService.queryRewardRecordsSum(userId);
		if(rewardRecordsSum == null){
			rewardRecordsSum = BigDecimal.ZERO;
		}
		modeAndView.addObject("rewardRecordsSum", rewardRecordsSum);
		
		// 用户id
        UsersInfo usersInfo = inviteService.getUsersInfoByUserId(userId);
        modeAndView.addObject("usersInfo", usersInfo);
		/*
		 //		if(form.getRecordType()==null || "1".equals(form.getRecordType())){
			List<WebInviteRecordCustomize> invites= inviteService.queryInviteRecords(userId);
			modeAndView.addObject("invites", invites);
//		}else{
			//奖励记录
			List<WebRewardRecordCustomize> rewards= inviteService.queryRewardRecords(userId);
			modeAndView.addObject("rewards", rewards);
//		}
*/		createCouponListPage(modeAndView,userId+"");
		return modeAndView;
	}
	
	/**
	 * 下载二维码
	 * @param request
	 * @param response
	 * @param form
	 */
	@RequestMapping(value = InviteDefine.DOWNLOAD_ACTION, method = RequestMethod.GET)
	public void download(HttpServletRequest request, HttpServletResponse response, @ModelAttribute InviteBean form){
		  LogUtil.startLog(InviteController.class.getName(), InviteDefine.DOWNLOAD_ACTION);
			
			String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			String fileUploadRealPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.real.path"));
			Integer userId =  WebUtils.getUserId(request);
			try {
			    System.out.println("download url: " + webhost + fileUploadRealPath+String.valueOf(userId)+".jpg");
				this.getServletFile(request, response, webhost + fileUploadRealPath+String.valueOf(userId)+".jpg", String.valueOf(userId)+".jpg");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * 查询邀请记录/奖励记录
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */ 
	@ResponseBody
	@RequestMapping(value = InviteDefine.PROJECT_LIST_ACTION) //, method = RequestMethod.POST, produces = "application/json; charset=utf-8"
	public InviteBean searchUserProjectList(@ModelAttribute InviteBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(InviteDefine.THIS_CLASS, InviteDefine.PROJECT_LIST_ACTION);
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		form.setUserId(userId.toString());
		//邀请记录
		if(form.getRecordType()==null || "".equals(form.getRecordType()) || "yqfs".equals(form.getRecordType())){
			// 统计相应的用户邀请记录总数
			int inviteCount= inviteService.queryInviteCount(userId);
			if (inviteCount > 0) {
				Paginator paginator = new Paginator(form.getPaginatorPage(), inviteCount);
				// 查询相应的邀请记录列表
				List<WebInviteRecordCustomize> invites= inviteService.queryInviteRecords(userId,paginator.getOffset(), paginator.getLimit());
				form.setPaginator(paginator);
				form.setInviteList(invites);
			} else {
				form.setPaginator(new Paginator(form.getPaginatorPage(), 0));
				form.setInviteList(new ArrayList<WebInviteRecordCustomize>());
			}
		}
		//奖励记录
		else if ("jljl".equals(form.getRecordType())) {
			// 统计相应的奖励记录总数
			int rewardCount = inviteService.queryRewardRecordsCount(userId);
			if (rewardCount > 0) {
				Paginator paginator = new Paginator(form.getPaginatorPage(), rewardCount);
				// 查询相应的奖励记录列表
				List<WebRewardRecordCustomize> rewards= inviteService.queryRewardRecords(userId,paginator.getOffset(), paginator.getLimit());
				form.setPaginator(paginator);
				form.setRewardList(rewards);
			} else {
				form.setPaginator(new Paginator(form.getPaginatorPage(), 0));
				form.setRewardList(new ArrayList<WebRewardRecordCustomize>());
			}
		}

		form.success();
		
		LogUtil.endLog(InviteDefine.THIS_CLASS, InviteDefine.PROJECT_LIST_ACTION);
		return form;
	}
	
	
	private void createCouponListPage(ModelAndView modeAndView,String userId) {
	    CouponListBean form=new CouponListBean();
	    form.setUserId(userId);
	    List<CouponUserListCustomize> wsyList=new ArrayList<CouponUserListCustomize>();
        List<CouponUserListCustomize> ysyList=new ArrayList<CouponUserListCustomize>();
        List<CouponUserListCustomize> ysxList=new ArrayList<CouponUserListCustomize>();
        // 统计相应的用户出借项目总数
        int recordTotal = this.couponService.countUserCouponTotal(form);
        if (recordTotal > 0) {
            // 查询相应的用户出借项目列表
            List<CouponUserListCustomize> recordList = couponService.selectUserCouponList(form,
                    -1, -1);
            for(CouponUserListCustomize coupon : recordList){
                //操作平台
                List<ParamName> clients=this.couponService.getParamNameList("CLIENT");
                //被选中操作平台
                String clientString = "";
                String clientSed[] = StringUtils.split(coupon.getCouponSystem(), ",");
                for(int i=0 ; i< clientSed.length;i++){
                    if("-1".equals(clientSed[i])){
                        clientString=clientString+"不限";
                        break;
                    }else{
                        for (ParamName paramName : clients) {
                            if(clientSed[i].equals(paramName.getNameCd())){
                                if(i!=0&&clientString.length()!=0){
                                    clientString=clientString+"/";
                                }
                                clientString=clientString+paramName.getName();
                                
                            }
                        }
                    }
                }
                coupon.setCouponSystem(clientString.replace("Android/iOS", "APP"));
                
                

              String projectType=coupon.getProjectType();
              String projectString = "";
              if (projectType.indexOf("-1") != -1) {
                  projectString = "不限";
              } else {
                //勾选汇直投，尊享汇，融通宝
                  if (projectType.indexOf("1")!=-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("7")!=-1) {
                      projectString = projectString + "散标,";
                  }
                  //勾选汇直投  未勾选尊享汇，融通宝
                  if (projectType.indexOf("1")!=-1&&projectType.indexOf("4")==-1&&projectType.indexOf("7")==-1) {
                      projectString = projectString + "散标,";
                  }
                  //勾选汇直投，融通宝  未勾选尊享汇
                  if(projectType.indexOf("1")!=-1&&projectType.indexOf("4")==-1&&projectType.indexOf("7")!=-1){
                      projectString = projectString + "散标,";
                  }
                  //勾选汇直投，选尊享汇 未勾选融通宝
                  if(projectType.indexOf("1")!=-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("7")==-1){
                      projectString = projectString + "散标,";
                  }
                  //勾选尊享汇，融通宝  未勾选直投
                  if(projectType.indexOf("1")==-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("7")!=-1){
                      projectString = projectString + "散标,";
                  }
                  //勾选尊享汇  未勾选直投，融通宝
                  if(projectType.indexOf("1")==-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("7")==-1){
                      projectString = projectString + "散标,";
                  }
                  //勾选尊享汇  未勾选直投，融通宝
                  if(projectType.indexOf("1")==-1&&projectType.indexOf("4")==-1&&projectType.indexOf("7")!=-1){
                      projectString = projectString + "散标,";
                  }
                  
                  if (projectType.indexOf("3")!=-1) {
                      projectString = projectString + "新手,";
                  }
                  /*if (projectType.indexOf("5")!=-1) {
                      projectString = projectString + "汇添金,";
                  }*/
                  // mod by nxl 智投服务：修改汇计划->智投服务 start
                    /*if (projectType.indexOf("6")!=-1) {
                        projectString = projectString + "汇计划,";
                    }*/
                  if (projectType.indexOf("6")!=-1) {
                      projectString = projectString + "智投,";
                  }
                  // mod by nxl 智投服务：修改汇计划->智投服务 end
                  projectString = StringUtils.removeEnd(projectString, ",");
              }
                coupon.setProjectType(projectString);
                
                switch (coupon.getUsedFlag()) {
                case 0:
                    wsyList.add(coupon);
                    break;
                case 1:
                    ysyList.add(coupon);
                    break;
                case 4:
                    ysxList.add(coupon);
                    break;
                }
            }
        }
        modeAndView.addObject("wsyList", wsyList);
        modeAndView.addObject("ysyList", ysyList);
        modeAndView.addObject("ysxList", ysxList);
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
    public static void main(String[] args) {
        System.out.println(8%7);
        System.out.println(0%8);
    }
}



