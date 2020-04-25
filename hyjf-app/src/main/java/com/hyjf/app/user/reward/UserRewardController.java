package com.hyjf.app.user.reward;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseDefine;
import com.hyjf.app.msgpush.MsgPushService;
import com.hyjf.app.user.manage.AppUserDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.admin.UserInfoForLogCustomize;
import com.hyjf.mybatis.model.customize.web.WebRewardRecordCustomize;

/**
 * 用户奖励相关的控制类 此处为类说明
 * 
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年12月11日
 * @see 上午10:16:28
 */
@Controller
// @RequestMapping(value = UserRewardDefine.REWARD_PATH)
public class UserRewardController {
	@Autowired
	private InviteService inviteService;
	@Autowired
	public MsgPushService msgPushService;

	/**
	 * 
	 * 我的奖励初始化数据
	 * 
	 * @author pcc
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRewardDefine.REWARD_PATH)
	public JSONObject reward(HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = new JSONObject();

		// 版本号
		String version = request.getParameter("version");
		// 平台 系统类别 0：PC，1：微官网，2：Android，3：IOS，4：其他
		String platform = request.getParameter("platform");
		// 唯一标识
		String sign = request.getParameter("sign");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(platform)) {
			ret.put("status", "999");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "999");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		// 取得用户iD
		Integer userId = null;
		try {
			userId = SecretUtil.getUserId(sign);
		} catch (Exception e) {
			ret.put("status", "999");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		ret.put("status", "000");
		ret.put("statusDesc", "成功");
		int inviteCount = inviteService.queryInviteCount(userId);
		BigDecimal total = inviteService.queryRewardRecordsSum(userId);
		if (total == null) {
			ret.put("total", 0);
		} else {
			ret.put("total", total);
		}
		ret.put("friendCount", inviteCount);
		// int couponTotal = this.couponService.countUserCouponTotal(userId);
		ret.put("coupon", 0);
		
		
		UsersExample example = new UsersExample();
        example.createCriteria().andUserIdEqualTo(userId);
        Users users = inviteService.getUsers(userId);
        ret.put("userName", users.getUsername());
        ret.put("userId", users.getUserId());
        
		String iconUrl = users.getIconurl();
		String imghost = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
        imghost = imghost.substring(0, imghost.length() - 1);// http://cdn.huiyingdai.com/
		String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.REQUEST_HOME.substring(1, AppUserDefine.REQUEST_HOME.length()) + "/";
        webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
		// 上传文件的CDNURL
        if (StringUtils.isNotEmpty(iconUrl)) {
            // 实际物理路径前缀2
            String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.real.path"));
            iconUrl=imghost + fileUploadTempPath + iconUrl;
        } else {
            UsersInfo usersInfo=inviteService.getUsersInfoByUserId(userId);
            if (usersInfo==null||usersInfo.getSex() == null || usersInfo.getSex().intValue() == 1) {
                iconUrl=webhost + "/img/" + "icon_man.png";
            } else {
                iconUrl=webhost + "/img/" + "icon_woman.png";
            }
        }
		ret.put("iconUrl", iconUrl);
		return ret;
	}

	/**
	 * 
	 * 奖励记录
	 * 
	 * @author pcc
	 * @param page
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRewardDefine.REWARD_PATH + UserRewardDefine.RWEARD_LIST)
	public JSONObject rewardList(@RequestParam(value = "currentPage", defaultValue = "1") int page,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject ret = new JSONObject();
		// 版本号
		String version = request.getParameter("version");
		// 平台 系统类别 0：PC，1：微官网，2：Android，3：IOS，4：其他
		String platform = request.getParameter("platform");
		// 唯一标识
		String sign = request.getParameter("sign");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(platform)) {
			ret.put("status", "999");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "999");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		// 取得用户iD
		Integer userId = null;
		try {
			userId = SecretUtil.getUserId(sign);
		} catch (Exception e) {
			ret.put("status", "999");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		try {
			page = Integer.valueOf(page);
			pageSize = Integer.valueOf(pageSize);
			int limitStart = pageSize * (page - 1);
			int count = inviteService.queryRewardRecordsCount(userId);
			double lastpage = Math.ceil((double) count / (double) pageSize);
			if (lastpage == page) {
				ret.put("isEnd", true);
			} else {
				ret.put("isEnd", false);
			}
			List<WebRewardRecordCustomize> list = inviteService.queryRewardRecords(userId, limitStart, pageSize);

			JSONArray jsonArray = new JSONArray();
			for (WebRewardRecordCustomize user : list) {
				JSONObject detailsJson = new JSONObject();
				detailsJson.put("friendName", user.getUsername());
				detailsJson.put("source", "好友出借");
				detailsJson.put("content", user.getPushMoney() + "元现金");

				jsonArray.add(detailsJson);
			}

			ret.put("list", list);
			ret.put("status", "000");
			ret.put("statusDesc", "成功");
			ret.put("list", jsonArray);
		} catch (Exception e) {
			ret.put("isEnd", true);
			ret.put("list", new ArrayList<WebRewardRecordCustomize>());
			ret.put("status", "000");
			ret.put("statusDesc", "成功");
		}

		return ret;
	}

	/**
	 * 
	 * 邀请记录
	 * 
	 * @author pcc
	 * @param page
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserRewardDefine.REWARD_PATH + UserRewardDefine.INVITE_LIST)
	public JSONObject inviteList(@RequestParam(value = "currentPage", defaultValue = "1") int page,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, HttpServletRequest request) {
		JSONObject ret = new JSONObject();

		String version = request.getParameter("version");
		// 平台 系统类别 0：PC，1：微官网，2：Android，3：IOS，4：其他
		String platform = request.getParameter("platform");
		// 唯一标识
		String sign = request.getParameter("sign");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(platform)) {
			ret.put("status", "999");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "999");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		// 取得用户iD
		Integer userId = null;
		try {
			userId = SecretUtil.getUserId(sign);
		} catch (Exception e) {
			ret.put("status", "999");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		page = Integer.valueOf(page);
		pageSize = Integer.valueOf(pageSize);
		int limitStart = pageSize * (page - 1);
		ret.put("status", "000");
		ret.put("statusDesc", "成功");
		int count = inviteService.queryInviteCount(userId);
		double lastpage = Math.ceil((double) count / (double) pageSize);
		if (lastpage == page) {
			ret.put("isEnd", true);
		}else{
		    ret.put("isEnd", false);
		}
		List<UserInfoForLogCustomize> list = inviteService.queryInviteList(userId, limitStart, pageSize);
		JSONArray jsonArray = new JSONArray();
		for (UserInfoForLogCustomize user : list) {
			JSONObject detailsJson = new JSONObject();
			detailsJson.put("friendName", user.getUserName());
			detailsJson.put("date", GetDate.timestamptoStrYYYYMMDDHHMM(user.getRegTime().toString()));
			
			if (user.getBankOpenAccount() == 0) {
				detailsJson.put("openStatus", "未开户");
			} else {
				detailsJson.put("openStatus", "已开户");
			}
			jsonArray.add(detailsJson);
		}
		ret.put("list", jsonArray);
		return ret;

	}

	@ResponseBody
	@RequestMapping(value = UserRewardDefine.MSG_PATH)
	public JSONObject msg(@PathVariable int msgid, HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = new JSONObject();

		String version = request.getParameter("version");
		// 平台 系统类别 0：PC，1：微官网，2：Android，3：IOS，4：其他
		String platform = request.getParameter("platform");
		// 唯一标识
		String sign = request.getParameter("sign");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(platform)) {
			ret.put("status", "999");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "999");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		// 取得用户iD
		// Integer userId = null;
		// try {
		// userId = SecretUtil.getUserId(sign);
		// } catch (Exception e) {
		// ret.put("status", "999");
		// ret.put("statusDesc", "请求参数非法");
		// return ret;
		// }

		ret.put("status", "000");
		ret.put("statusDesc", "成功");

		MessagePushMsgHistory msg = msgPushService.getMsgPushMsgHistoryById(msgid);

		JSONObject detail = new JSONObject();
		detail.put("title", msg.getMsgTitle());
		detail.put("date", GetDate.timestamptoStrYYYYMMDDHHMM(msg.getCreateTime().toString()));
		detail.put("content", msg.getMsgContent());
		ret.put("details", detail);
		return ret;
	}
	
	  /**
     * 格式化时间
     * @param sendtime
     * @return
     */
//    public String formatTime(Integer sendtime){
//		//00:00:00 时间戳
//		
//    	return GetDate.timestamptoStrYYYYMMDDHHMM(sendtime);
//    }
}
