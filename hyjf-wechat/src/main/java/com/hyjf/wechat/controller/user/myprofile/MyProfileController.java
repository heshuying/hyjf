package com.hyjf.wechat.controller.user.myprofile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.admin.AdminUserDetailCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponUserForAppCustomize;
import com.hyjf.mybatis.model.customize.web.CouponUserListCustomize;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.base.SimpleResultBean;
import com.hyjf.wechat.service.myprofile.MyProfileService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 账户总览
 * Created by cuigq on 2018/2/1.
 */
@Controller
@RequestMapping(MyProfileDefine.REQUEST_MAPPING)
public class MyProfileController extends BaseController {
    @Autowired
    private MyProfileService myProfileService;

    @SignValidate
    @RequestMapping(value = MyProfileDefine.INDEX_ACTION, method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean myProfile(HttpServletRequest request) {

        SimpleResultBean<MyProfileVO> result = new SimpleResultBean<>();

        MyProfileVO myProfileVO = new MyProfileVO();

        Integer userId = requestUtil.getRequestUserId(request);

        String trueUserName = myProfileService.getUserTrueName(userId);

        MyProfileVO.UserAccountInfo userAccountInfo = myProfileVO.new UserAccountInfo();

        userAccountInfo.setTrueUserName(trueUserName);

        myProfileService.buildUserAccountInfo(userId, userAccountInfo);
        myProfileVO.setUserAccountInfo(userAccountInfo);

        myProfileService.buildOutInfo(userId, myProfileVO);

        UsersInfo userInfo = myProfileService.getUsersInfoByUserId(userId);

        if (userInfo.getRoleId() == null || userInfo == null ){
            result.setStatus("99");
            result.setStatusDesc("用户信息不存在！");
            return result;
        }
        //1、出借人（投资人）2、借款人3、担保机构授权
        myProfileVO.setRoleId(userInfo.getRoleId());

        result.setObject(myProfileVO);

        getIconUrl(userId, myProfileVO);

        return result;
    }

    private void getIconUrl(Integer userId, MyProfileVO myProfileVO) {
        Users user = myProfileService.getUsers(Integer.valueOf(userId));
        String imghost = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.head.url"));
        String imagePath="";
        if (StringUtils.isNotEmpty(user.getIconurl())) {
            // 实际物理路径前缀
            String fileUploadRealPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.head.path"));
            imagePath = imghost + fileUploadRealPath + user.getIconurl();

        }
        myProfileVO.getUserAccountInfo().setIconUrl(imagePath);

        // 通过当前用户ID 查询用户所在一级分部,从而关联用户所属渠道
        // 合规自查添加
        // 20181205 产品需求, 屏蔽渠道,只保留用户ID
//        AdminUserDetailCustomize userUtmInfo = myProfileService.selectUserUtmInfo(userId);
        String userQrCodeUrl = null;
//        if (userUtmInfo != null) {
//            userQrCodeUrl = PropUtils.getSystem("hyjf.wechat.qrcode.url") + "refferUserId=" + userId + "&utmId=" + userUtmInfo.getSourceId().toString() + "&utmSource=" + userUtmInfo.getSourceName();
//        }else {
            // 已确认未关联渠道的用户
            userQrCodeUrl = PropUtils.getSystem("hyjf.wechat.qrcode.url") + "refferUserId=" + userId;
//        }
        myProfileVO.getUserAccountInfo().setQrcodeUrl(userQrCodeUrl);
    }

    @SignValidate
    @RequestMapping(value = MyProfileDefine.COUPON_LIST_ACTION, method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean getCouponList(HttpServletRequest request) {
        SimpleResultBean<List<CouponUserListCustomize>> resultBean = new SimpleResultBean<>();
        Integer userId = requestUtil.getRequestUserId(request);
//         myProfileService.queryUserCouponList(userId);
        String resultStr = myProfileService.getUserCouponsData("0", 1, 100, userId, "");
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        JSONArray data = resultJson.getJSONArray("data");
        List<CouponUserForAppCustomize> configs = JSON.parseArray(data.toJSONString(), CouponUserForAppCustomize.class);
        List<CouponUserListCustomize> lstCoupon =createCouponUserListCustomize(configs);
        resultBean.setObject(lstCoupon);
        return resultBean;
    }

	private List<CouponUserListCustomize> createCouponUserListCustomize(
			List<CouponUserForAppCustomize> configs) {
		List<CouponUserListCustomize> list=new ArrayList<CouponUserListCustomize>();
		DecimalFormat df = new DecimalFormat(",###");
		df.setRoundingMode(RoundingMode.FLOOR);
		for (CouponUserForAppCustomize config : configs) {
			CouponUserListCustomize customize=new CouponUserListCustomize();
			customize.setId(config.getId());
			String[] time=config.getTime().split("-");
			customize.setAddTime(time[0]);
			customize.setEndTime(time[1]);
			customize.setContent("1");
			customize.setCouponName(config.getCouponName());
			customize.setCouponProfitTime("");
			
			customize.setCouponSystem(config.getOperationPlatform());
			customize.setCouponType(config.getCouponType());
			customize.setCouponUserCode("");
			customize.setProjectExpirationType(config.getInvestTime());
			customize.setProjectType(config.getProjectType());
			customize.setRecoverTime("");
			customize.setRecoverStatus("");
			customize.setTenderQuota(config.getInvestQuota());
			// 如果有，就去掉  要不然下面报类型转换异常
			if(config.getCouponQuota().indexOf(",")>0){
			    config.setCouponQuota(config.getCouponQuota().replaceAll(",", ""));
			}
			if(config.getCouponQuota().indexOf("元")!=-1||config.getCouponQuota().indexOf("%")!=-1){
        		String couponQuota=config.getCouponQuota().substring(0, config.getCouponQuota().length()-1);
        		config.setCouponQuota(couponQuota);
			}
    		if(!"加息券".equals(customize.getCouponType())){
    			customize.setCouponQuota(df.format(new BigDecimal(config.getCouponQuota())));
    		}else{
    			customize.setCouponQuota(config.getCouponQuota());
    		}
			list.add(customize);
		}
		return list;
	}
}
