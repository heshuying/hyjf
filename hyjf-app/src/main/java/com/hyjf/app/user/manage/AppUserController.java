package com.hyjf.app.user.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseDefine;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.extension.ExtensionService;
import com.hyjf.app.user.coupon.util.CouponCheckUtil;
import com.hyjf.app.util.DES;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.app.AppAdsCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller("appUserController")
@RequestMapping(value = AppUserDefine.REQUEST_MAPPING)
public class AppUserController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(AppUserController.class);

	public static final String SOA_COUPON_KEY = PropUtils.getSystem("release.coupon.accesskey");

	@Autowired
	private AppUserService appUserService;

	@Autowired
	private ExtensionService extensionService;
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	@Autowired
	private CouponCheckUtil couponCheckUtil;

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	/** 图片地址 */
	private static String HOST_URL = PropUtils.getSystem("file.domain.url");

	/** 从系统配置中获取最新版本号 */
	private static final String NEW_VERSION = PropUtils.getSystem("hyjf.app.version.new");

	/**
	 * 登录
	 *
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = AppUserDefine.LOGIN_IN_ACTION)
	public JSONObject loginInAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.LOGIN_IN_ACTION);

		JSONObject ret = new JSONObject();
		ret.put("request", AppUserDefine.LOGIN_IN_REQUEST);

		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// 用户名
		String username = request.getParameter("username");
		// 密码
		String password = request.getParameter("password");
		// 唯一标识
		String sign = request.getParameter("sign");

		// 神策数据统计 add by liuyang 20180726 start
		String presetProps = request.getParameter("presetProps");
		// 神策数据统计 add by liuyang 20180726 end

		boolean isNeedUPdate = UpdateNoticeUtils.checkForAppUpdate(version, "此版本暂不可用，请更新至最新版本。", AppUserDefine.LOGIN_IN_REQUEST, ret);
		if(isNeedUPdate){
			return ret;
		}


		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(sign)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "709");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 业务逻辑
		try {
			// 解密
			username = DES.decodeValue(key, username);
			password = DES.decodeValue(key, password);
			if (Validator.isNull(username)) {
				ret.put("status", "1");
				ret.put("statusDesc", "用户名不能为空");
				return ret;
			}
			if (Validator.isNull(password)) {
				ret.put("status", "1");
				ret.put("statusDesc", "密码不能为空");
				return ret;
			}

			// 执行登录(登录时间，登录ip)
			int result = appUserService.updateLoginInAction(username, password, CustomUtil.getIpAddr(request));
			switch (result) {
				case -1:
					ret.put("status", "1");
					ret.put("statusDesc", "登录失败,帐号或密码错误");
					break;
				case -2:
					ret.put("status", "1");
					ret.put("statusDesc", "登录失败,存在多个相同用户");
					break;
				case -3:
					ret.put("status", "1");
					ret.put("statusDesc", "登录失败,帐号或密码错误");
					break;
				case -4:
					ret.put("status", "1");
					ret.put("statusDesc", "抱歉，您的账户已被禁用，如有疑问请联系客服");
					break;
				case -5:
					ret.put("status", "1");
					ret.put("statusDesc", "密码错误次数已达上限，请明日再试");
					break;
				case -6:
					ret.put("status", "1");
					ret.put("statusDesc", "存在恶意登录嫌疑，该IP已封");
					break;
				default:
					Users users = appUserService.getUsersByUserId(result);
					UsersInfo usersInfo = appUserService.getUsersInfoByUserId(result);
					//登录成功用戶操作日志记录
					UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
					userOperationLogEntity.setOperationType(1);
					userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
					userOperationLogEntity.setPlatform(request.getParameter("realPlatform")==null?Integer.valueOf(platform):Integer.valueOf(request.getParameter("realPlatform")));
					userOperationLogEntity.setRemark("");
					userOperationLogEntity.setOperationTime(new Date());
					userOperationLogEntity.setUserName(users.getUsername());
					userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
					appUserService.sendUserLogMQ(userOperationLogEntity);
					String truename = usersInfo.getTruename();
					if (truename == null) {
						truename = "";
					}
					int attribute = usersInfo.getAttribute();
					String token = SecretUtil.createToken(sign, result, users.getUsername());
					logger.info("-------------登录颁发token，sign:" + sign + ",token:" + token);
					//重新给用户生成sign----renxingchen
//				sign = SecretUtil.createSignSec(sign); // 不生成了
					/***********登录时自动同步线下充值记录  pcc  start***********/
					if(users.getBankOpenAccount()==1){
						CommonSoaUtils.synBalance(users.getUserId());
					}
					/***********登录时自动同步线下充值记录  pcc  end***********/
					if (extensionService.queryExtensionInfo(result).size() == 0) {
						// 解析渠道号
						String[] versionTemp = version.split("\\.");
						String sourceId = "";
						if (versionTemp.length > 3) {
							sourceId = versionTemp[3];
						} else {
							sourceId = "0";
						}
						ExtensionTemp extemp = new ExtensionTemp();
						extemp.setSign(sign);
						extemp.setUserId(result);
						extemp.setAccessTime(new Date());
						extemp.setTruename(truename);
						extemp.setSourceId(Integer.parseInt(sourceId));
						extemp.setPlatform(Integer.parseInt(platform));
						extemp.setAttribute(attribute);
						extensionService.insertExtensionInfo(extemp);
					}
					//如果登录成功就将登陆密码错误次数的key删除
					RedisUtils.del(RedisConstants.PASSWORD_ERR_COUNT_ALL+result);
					ret.put("status", "0");
					ret.put("statusDesc", "登录成功");
					ret.put("token", token);
					ret.put("sign", sign);
					// add by zhangjp 领取优惠券收益 start
				/*try{
					CouponParamBean couponParamBean = new CouponParamBean();
					couponParamBean.setUserId(usersInfo.getUserId().toString());
					CouponUtils.receiveCouponRecover(couponParamBean);
				}catch(Exception e){
					System.out.println("领取收益失败！！");
					e.printStackTrace();
				}*/

					// add by zhangjp 领取优惠券收益 end

					// 神策数据统计 add by liuyang 20180726 start
					if (org.apache.commons.lang.StringUtils.isNotBlank(presetProps)){
						SensorsDataBean sensorsDataBean = new SensorsDataBean();
						// 将json串转换成Bean
						try {
							Map<String, Object> sensorsDataMap = JSONObject.parseObject(presetProps, new TypeReference<Map<String, Object>>() {
							});
							sensorsDataBean.setPresetProps(sensorsDataMap);
							sensorsDataBean.setUserId(users.getUserId());
							sensorsDataBean.setEventCode("login");
							// 发送神策数据统计MQ
							this.appUserService.sendSensorsDataMQ(sensorsDataBean);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					// 神策数据统计 add by liuyang 20180726 end

					break;
			}
		} catch (Exception e) {
			ret.put("status", "1");
			ret.put("statusDesc", e.getMessage());
		}
		LogUtil.endLog(AppUserDefine.THIS_CLASS, AppUserDefine.LOGIN_IN_ACTION);
		return ret;
	}

	/**
	 * 二维码url
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = AppUserDefine.GET_QRCODE_ACTION)
	public ModelAndView getQrCodeAction(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(AppUserDefine.QRCODE_URL);
		String userId = request.getParameter("userId");
		// add by zhangjp app1.3.5改版 用户二维码图片上增加用户头像 start
		if(StringUtils.isNotEmpty(userId)){
			Users user = this.appUserService.getUserByUserId(Integer.valueOf(userId));
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
		}
		// add by zhangjp app1.3.5改版 用户二维码图片上增加用户头像 end
		//从配置文件中拿到微信二维码Url
		String qrcode = PropUtils.getSystem("hyjf.wechat.qrcode.url").replace("{userId}",userId);
		logger.info("qrcode:{}", qrcode);
		modelAndView.addObject("qrcode",qrcode);

		modelAndView.addObject("userId", userId);
		return modelAndView;
	}

	/**
	 * 获取用户相关数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppUserDefine.GET_USERINFO_ACTION, method = RequestMethod.POST)
	public JSONObject getUserinfoAction(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.GET_USERINFO_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", AppUserDefine.GET_USERINFO_REQUEST);

		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// token
		String token = request.getParameter("token");
		// 唯一标识
		String sign = request.getParameter("sign");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString) || Validator.isNull(order)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 业务逻辑
		try {
			// 取得用户ID
			Integer userId = SecretUtil.getUserId(sign);
			UsersInfo userInfo = appUserService.getUsersInfoByUserId(userId);
			if (userId != null && userInfo != null && userInfo.getRoleId() != null) {
				UserParameters userParameters = appUserService.getUserParameters(userId,platform, request);
				if (StringUtils.isBlank(userParameters.getIdcard()) || userParameters.getIdcard().length() < 15) {
					userParameters.setIdcard("000000000000000000");
				}
				//用户角色 1、出借人 2、借款人 3、担保机构
				String HOST = PropUtils.getSystem("hyjf.web.host");
				//1、出借人 -->自动投标、自动债转、服务费授权；
				if(userInfo.getRoleId().equals(1)){
					//跳转授权须知页面的路径
					userParameters.setPaymentAuthUrl(HOST + "/needs/authorization"+packageStr(request)+"&usertype="+0);
					//用来兼容之前的版本
					userParameters.setMergeAuthUrl(HOST + "/needs/authorization"+packageStr(request)+"&usertype="+0);
					//跳转授权须知页面之前的弹框信息
					userParameters.setPaymentAuthDesc("应合规要求，出借、提现等交易前需进行以下授权：\n自动投标，自动债转，服务费授权。");
				}else if (userInfo.getRoleId().equals(2)) {
					//2、借款人 -->服务费授权、还款授权
					userParameters.setRepayAuthUrl(HOST + "/needs/authorization"+packageStr(request)+"&usertype="+1);
					userParameters.setPaymentAuthUrl(HOST + "/needs/authorization"+packageStr(request)+"&usertype="+1);
					userParameters.setPaymentAuthDesc("应合规要求，借款、提现等交易前需进行以下授权：\n服务费授权，还款授权。");
				}else {
					//3、担保机构 -->服务费授权
					userParameters.setPaymentAuthUrl(HOST + "/needs/authorization"+packageStr(request)+"&usertype="+2);
					userParameters.setPaymentAuthDesc("应合规要求，借款、提现等交易前需进行以下授权：\n服务费授权");
				}
				ret.put("status", "0");
				ret.put("params", userParameters);
				ret.put("statusDesc", "获取用户相关数据成功");

			} else {
				ret.put("status", "1");
				ret.put("statusDesc", "用户信息不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("status", "1");
			ret.put("statusDesc", "获取用户相关数据发生错误");
		}

		LogUtil.endLog(AppUserDefine.THIS_CLASS, AppUserDefine.GET_USERINFO_ACTION);
		return ret;
	}

	/**
	 * 上传头像
	 *
	 * @param request
	 * @param response
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppUserDefine.UPLOAD_AVATAR_ACTION, method = RequestMethod.POST)
	public JSONObject uploadAvatarAction(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.UPLOAD_AVATAR_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", AppUserDefine.UPLOAD_AVATAR_REQUEST);

		// 转型为MultipartHttpRequest(重点的所在)
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart(request);

		// 版本号
		String version = multipartRequest.getParameter("version");
		// 网络状态
		String netStatus = multipartRequest.getParameter("netStatus");
		// 平台
		String platform = multipartRequest.getParameter("platform");
		// token
		String token = multipartRequest.getParameter("token");
		// 唯一标识
		String sign = multipartRequest.getParameter("sign");
		// 随机字符串
		String randomString = multipartRequest.getParameter("randomString");
		// Order
		String order = multipartRequest.getParameter("order");
		// 获得第1张图片（根据前台的name名称得到上传的文件）
		MultipartFile iconImg = multipartRequest.getFile("iconImg");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString) || Validator.isNull(order)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		// 验证sign
		if (RedisUtils.get(sign) == null) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 验证key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 验证Order
		if (!SecretUtil.checkOrder(key, token, randomString, order)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 业务逻辑
		try {
			if (Validator.isNull(iconImg)) {
				ret.put("status", "1");
				ret.put("statusDesc", "上传图片不能为空");
				return ret;
			}

//			String[] allowTypes = new String[] { "jpg", "png" };
			Long allowFileLength = 5000000L;// 单位字节
//			Long size = iconImg.getSize() / 1024;
			// 待修改,加上图片类型和尺寸验证 TODO
			// String fileName=iconImg.getOriginalFilename();
			// if(UploadFileUtils.validTypeByName(fileName, new
			// String[]{"","",""}))

			// 取得用户ID
			Integer userId = SecretUtil.getUserId(sign);

			if (userId != null) {
				// 从配置文件获取上传文件的各个URL
				// 上传文件的CDNURL
				// String fileDomainUrl =
				// UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
				// 实际物理路径前缀1
				String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
				// 实际物理路径前缀2
				String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.real.path"));

				// 如果文件夹(前缀+后缀)不存在,则新建文件夹
				String logoRealPathDir = filePhysicalPath + fileUploadTempPath;
				File logoSaveFile = new File(logoRealPathDir);
				if (!logoSaveFile.exists()) {
					logoSaveFile.mkdirs();
				}

				// 生成图片文件名
				String fileRealName = String.valueOf(GetDate.getMillis());
				fileRealName = "appIconImg_" + userId + fileRealName + UploadFileUtils.getSuffix(iconImg.getOriginalFilename());

				// 上传至服务器
				String returnMessage = UploadFileUtils.upload4Stream(fileRealName, logoRealPathDir, iconImg.getInputStream(), allowFileLength);
				if (!returnMessage.equals("上传文件成功！")) {
					ret.put("status", "1");
					ret.put("statusDesc", returnMessage);
					return ret;
				}

				// 保存到数据库的路径=上传文件的CDNURL+图片的文件名
				String iconUrl = fileRealName;
				// 保存到数据库
				appUserService.updateUserIconImg(userId, iconUrl);
				// 测试环境要加 + request.getContextPath().replace("//", "");
				String imghost = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
				imghost = imghost.substring(0, imghost.length() - 1);// +request.getContextPath().replace("//",
				// "");
				// System.out.println(host+fileUploadTempPath+fileRealName);
				ret.put("iconUrl", imghost + fileUploadTempPath + fileRealName);
				ret.put("status", "0");
				ret.put("statusDesc", "头像上传成功");
			} else {
				ret.put("status", "1");
				ret.put("statusDesc", "用户信息不存在");
			}
		} catch (Exception e) {
			ret.put("status", "1");
			ret.put("statusDesc", "上传头像发生错误");
		}

		LogUtil.endLog(AppUserDefine.THIS_CLASS, AppUserDefine.UPLOAD_AVATAR_ACTION);
		return ret;
	}

	/**
	 * 获取联系人类型
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppUserDefine.GET_RELATIONTYPE_ACTION, method = RequestMethod.POST)
	public JSONObject getRelationTypes(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.GET_RELATIONTYPE_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", AppUserDefine.GET_RELATIONTYPE_REQUEST);

		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// token
		String token = request.getParameter("token");
		// 唯一标识
		String sign = request.getParameter("sign");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString) || Validator.isNull(order)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 业务逻辑
		try {
			List<ParamName> paramList = appUserService.getParamNameList("USER_RELATION");
			JSONArray result = new JSONArray();
			for (int i = 0; i < paramList.size(); i++) {
				JSONObject json = new JSONObject();
				json.put("name", paramList.get(i).getName());
				json.put("value", paramList.get(i).getNameCd());
				result.add(json);
			}
			ret.put("status", "0");
			ret.put("statusDesc", "获取联系人类型成功");
			ret.put("relationTypes", result);
		} catch (Exception e) {
			ret.put("status", "1");
			ret.put("statusDesc", "获取联系人类型发生错误");
		}

		LogUtil.endLog(AppUserDefine.THIS_CLASS, AppUserDefine.GET_RELATIONTYPE_ACTION);
		return ret;
	}

	/**
	 * 修改联系人
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppUserDefine.UPDATE_URGENT_ACTION, method = RequestMethod.POST)
	public JSONObject updateUrgentAction(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.UPDATE_URGENT_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", AppUserDefine.UPDATE_URGENT_REQUEST);

		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// token
		String token = request.getParameter("token");
		// 唯一标识
		String sign = request.getParameter("sign");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");

		// 紧急联系人关系
		String urgentRelation = request.getParameter("urgentRelation");
		// 紧急联系人姓名
		String urgentName = request.getParameter("urgentName");
		// 紧急联系人电话
		String urgentMobile = request.getParameter("urgentMobile");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString) || Validator.isNull(order)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 业务逻辑
		try {
			// 取得用户ID
			Integer userId = SecretUtil.getUserId(sign);

			if (userId != null) {
				// 解密
				urgentRelation = DES.decodeValue(key, urgentRelation);
				urgentName = DES.decodeValue(key, urgentName);
				urgentMobile = DES.decodeValue(key, urgentMobile);
				if (Validator.isNull(urgentRelation)) {
					ret.put("status", "1");
					ret.put("statusDesc", "联系人关系不能为空");
					return ret;
				}
				if (Validator.isNull(urgentName)) {
					ret.put("status", "1");
					ret.put("statusDesc", "联系人姓名不能为空");
					return ret;
				}
				if (Validator.isNull(urgentMobile)) {
					ret.put("status", "1");
					ret.put("statusDesc", "联系人电话不能为空");
					return ret;
				}
				List<ParamName> paramList = appUserService.getParamNameList("USER_RELATION");
				Boolean validUrgentRelation = false;
				for (ParamName paramName : paramList) {
					if (paramName.getNameCd().equals(urgentRelation)) {
						validUrgentRelation = true;
						break;
					}
				}
				if (!validUrgentRelation) {
					ret.put("status", "1");
					ret.put("statusDesc", "无效的紧急联系人关系");
					return ret;
				}
				if (urgentName.length() > 10) {
					ret.put("status", "1");
					ret.put("statusDesc", "紧急联系人姓名不能大于10位");
					return ret;
				}
				if (!Validator.isMobile(urgentMobile)) {
					ret.put("status", "1");
					ret.put("statusDesc", "无效的紧急联系人电话");
					return ret;
				}

				// 更新联系人信息
				boolean success = appUserService.updateUrgentAction(userId, Integer.parseInt(urgentRelation), urgentName, urgentMobile);

				if (success) {
					ret.put("status", "0");
					ret.put("statusDesc", "修改联系人成功");
				} else {
					ret.put("status", "1");
					ret.put("statusDesc", "修改联系人失败");
				}
			} else {
				ret.put("status", "1");
				ret.put("statusDesc", "用户信息不存在");
			}
		} catch (Exception e) {
			ret.put("status", "1");
			ret.put("statusDesc", "修改联系人发生错误");
		}

		LogUtil.endLog(AppUserDefine.THIS_CLASS, AppUserDefine.UPDATE_URGENT_ACTION);
		return ret;
	}

	/**
	 * 修改昵称
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppUserDefine.UPDATE_NICKNAME_ACTION, method = RequestMethod.POST)
	public JSONObject updateNicknameAction(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.UPDATE_NICKNAME_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", AppUserDefine.UPDATE_NICKNAME_REQUEST);

		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// token
		String token = request.getParameter("token");
		// 唯一标识
		String sign = request.getParameter("sign");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");

		// 昵称
		String nickname = request.getParameter("nickname");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(token) || Validator.isNull(sign) || Validator.isNull(randomString) || Validator.isNull(order)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 业务逻辑
		try {
			// 取得用户ID
			Integer userId = SecretUtil.getUserId(sign);

			if (userId != null) {
				// 解密
				nickname = DES.decodeValue(key, nickname);
				if (Validator.isNull(nickname)) {
					ret.put("status", "1");
					ret.put("statusDesc", "昵称不能为空");
					return ret;
				}

				if (nickname.length() > 16) {
					ret.put("status", "1");
					ret.put("statusDesc", "昵称不能大于16位");
					return ret;
				}

				// 更新昵称
				boolean success = appUserService.updateNicknameAction(userId, nickname);

				if (success) {
					ret.put("status", "0");
					ret.put("statusDesc", "修改昵称成功");
				} else {
					ret.put("status", "1");
					ret.put("statusDesc", "修改昵称失败");
				}
			} else {
				ret.put("status", "1");
				ret.put("statusDesc", "用户信息不存在");
			}
		} catch (Exception e) {
			ret.put("status", "1");
			ret.put("statusDesc", "修改昵称发生错误");
		}

		LogUtil.endLog(AppUserDefine.THIS_CLASS, AppUserDefine.UPDATE_NICKNAME_ACTION);
		return ret;
	}
	/**
	 * 注册
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppUserDefine.REGIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject registAction(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.REGIST_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", AppUserDefine.REGIST_REQUEST);

		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// 唯一标识
		String sign = request.getParameter("sign");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");

		// 手机号
		String mobile = request.getParameter("mobile");
		// 验证码
		String verificationCode = request.getParameter("verificationCode");
		// 登录密码
		String password = request.getParameter("password");
		// 推荐人
		String reffer = request.getParameter("reffer");

		// 合规改造 add by huanghui 20181120 start
		/**
		 * 当前注册用的类型
		 * 1:普通用户;
		 * 2:企业用户;
		 * 根据前端传值来判定, 如果不传或者传值其他值 默认为普通用户
		 */
		// 前端拼接的 用户类型
		String urlUserType = request.getParameter("userType");

		Integer userType = 0;
		if ("2".equals(urlUserType)){
			userType = 1;
		}else {
			userType = 0;
		}
		// 合规改造 add by huanghui 20181120 end

		// 神策数据统计 add by liuyang 20180726 start
		// 神策数据统计事件预置属性
		String presetProps = request.getParameter("presetProps");
		// 神策数据统计 add by liuyang 20180726 end

		String jumpCommand = GetJumpCommand.getLinkJumpPrefix(request, version);

		logger.info("当前注册手机号: {}", mobile);

		boolean isNeedUPdate = UpdateNoticeUtils.checkForAppUpdate(version, "此版本暂不可用，请更新至最新版本。", AppUserDefine.REGIST_REQUEST, ret);
		if(isNeedUPdate){
			ret.put(CustomConstants.APP_STATUS, 1);
			ret.put(CustomConstants.APP_STATUS_DESC, "此版本暂不可用，请更新至最新版本。");
			ret.put("successUrl", "");
			return ret;
		}

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {

			ret.put(CustomConstants.APP_STATUS, 1);
			ret.put(CustomConstants.APP_STATUS_DESC, AppUserDefine.STATUS_FAIL_MSG);
			ret.put("successUrl", "");
			return ret;
		}
		// 先验证推荐人
		reffer = DES.decodeValue(key, reffer);
		{
			if (Validator.isNotNull(reffer)) {
				int count = appUserService.countUserByRecommendName(reffer);
				if (count == 0) {
					ret.put(CustomConstants.APP_STATUS, 1);
					ret.put(CustomConstants.APP_STATUS_DESC, "无效的推荐人");
					ret.put("successUrl", "");
					return ret;
				}
			}
		}
		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(sign) || Validator.isNull(randomString) || Validator.isNull(order) || Validator.isNull(mobile) || Validator.isNull(verificationCode)) {
			ret.put(CustomConstants.APP_STATUS, 1);
			ret.put(CustomConstants.APP_STATUS_DESC, "请求参数非法");
			ret.put("successUrl", "");
			return ret;
		}

		// 业务逻辑
		try {// 取得验证码
			mobile = DES.decodeValue(key, mobile);
			verificationCode = DES.decodeValue(key, verificationCode);
			password = DES.decodeValue(key, password);

			logger.info("当前注册手机号: {}", mobile);
			if (Validator.isNull(mobile)) {
				ret.put(CustomConstants.APP_STATUS, 1);
				ret.put(CustomConstants.APP_STATUS_DESC, "手机号不能为空");
				ret.put("successUrl", "");
				return ret;
			}
			if (Validator.isNull(verificationCode)) {
				ret.put(CustomConstants.APP_STATUS, 1);
				ret.put(CustomConstants.APP_STATUS_DESC, "验证码不能为空");
				ret.put("successUrl", "");
				return ret;
			}
			if (Validator.isNull(password)) {
				ret.put(CustomConstants.APP_STATUS, 1);
				ret.put(CustomConstants.APP_STATUS_DESC, "密码不能为空");
				ret.put("successUrl", "");
				return ret;
			}

			if (password.length() < 6 || password.length() > 16) {
				ret.put(CustomConstants.APP_STATUS, 1);
				ret.put(CustomConstants.APP_STATUS_DESC, "密码长度6-16位");
				ret.put("successUrl", "");
				return ret;
			}
//			if (Validator.isNumber(password.substring(0, 1))) {
//				ret.put("status", "1");
//				ret.put("statusDesc", "密码首位必须为字母");
//				return ret;
//			}
			boolean hasNumber = false;

			for (int i = 0; i < password.length(); i++) {
				if (Validator.isNumber(password.substring(i, i + 1))) {
					hasNumber = true;
					break;
				}
			}
			if (!hasNumber) {
				ret.put(CustomConstants.APP_STATUS, 1);
				ret.put(CustomConstants.APP_STATUS_DESC, "密码必须包含数字");
				ret.put("successUrl", "");
				return ret;
			}

			String regEx = "^[a-zA-Z0-9]+$";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(password);
			if (!m.matches()) {
				ret.put(CustomConstants.APP_STATUS, 1);
				ret.put(CustomConstants.APP_STATUS_DESC, "密码必须由数字和字母组成，如abc123");
				ret.put("successUrl", "");
				return ret;
			}
			if (!Validator.isMobile(mobile)) {
				ret.put(CustomConstants.APP_STATUS, 1);
				ret.put(CustomConstants.APP_STATUS_DESC, "请输入您的真实手机号码");
				ret.put("successUrl", "");
				return ret;
			}
			{
				int cnt = appUserService.countUserByMobile(mobile);
				if (cnt > 0) {
					ret.put(CustomConstants.APP_STATUS, 1);
					ret.put(CustomConstants.APP_STATUS_DESC, "该手机号已经注册");
					ret.put("successUrl", "");
					return ret;
				}
			}

			{
				String verificationType = AppUserDefine.PARAM_TPL_ZHUCE;
				int cnt = appUserService.updateCheckMobileCode(mobile, verificationCode, verificationType, platform, AppUserDefine.CKCODE_YIYAN, AppUserDefine.CKCODE_USED);
				if (cnt == 0) {
					ret.put(CustomConstants.APP_STATUS, 1);
					ret.put(CustomConstants.APP_STATUS_DESC, "验证码无效");
					ret.put("successUrl", "");
					return ret;
				}
			}
			// 注册
			Users user = new Users();
			String loginIp = CustomUtil.getIpAddr(request);
			int userId = appUserService.insertUserAction(mobile, password, verificationCode, reffer, loginIp, request,user, userType);

			if (userId != 0) {
				String statusDesc = "注册成功";

				// 发送Ip同步信息
				sendIpInfo(userId, loginIp);


				// 投之家用户注册送券活动
				String activityIdTzj = CustomConstants.REGIST_TZJ_ACTIVITY_ID;
				System.out.println("APP注册送加息券，活动id：" + activityIdTzj);
				// 活动有效期校验
				String resultActivityTzj = couponCheckUtil.checkActivityIfAvailable(activityIdTzj);
				if (StringUtils.isEmpty(resultActivityTzj)) {
					// 投之家用户额外发两张加息券
					if(StringUtils.isNotEmpty(user.getReferrerUserName()) && user.getReferrerUserName().equals("touzhijia")){
						CommonParamBean paramBean = new CommonParamBean();
						paramBean.setUserId(String.valueOf(userId));
						paramBean.setCouponSource(2);
						paramBean.setCouponCode("PJ2958703");
						paramBean.setSendCount(2);
						paramBean.setActivityId(Integer.parseInt(activityIdTzj));
						paramBean.setRemark("投之家用户注册送加息券");
						paramBean.setSendFlg(0);
						// 发放两张加息券
						CommonSoaUtils.sendUserCouponNoRet(paramBean);

					}

				}

				// add by zhangjinpeng 注册送888元新手红包 start
				String activityId = CustomConstants.REGIST_888_ACTIVITY_ID;
				// 活动有效期校验
				String resultActivity = couponCheckUtil.checkActivityIfAvailable(activityId);
				if(StringUtils.isEmpty(resultActivity)){
//                    CommonParamBean paramBean = new CommonParamBean();
//                    paramBean.setUserId(user.getUserId().toString());
//                    paramBean.setSendFlg(11);
//                    // 发放888元新手红包
//                    CommonSoaUtils.sendUserCouponNoRet(paramBean);

					// 发放注册888红包
					try {
						sendCoupon(user);
					} catch (Exception e) {
						LogUtil.errorLog(this.getClass().getName(), "regist", "注册发放888红包失败", e);
					}

					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("limitStart", 0);
					paraMap.put("limitEnd", 1);
					paraMap.put("host", HOST_URL);
					paraMap.put("code", "registpop");
					List<AppAdsCustomize> recordList = appUserService.searchBannerList(paraMap);
					if(recordList != null && !recordList.isEmpty()){
						// 注册成功发券提示
						AppAdsCustomize record = recordList.get(0);
						String operationUrl = jumpCommand + "://jumpCouponsList/?"; //record.getUrl() + "?couponStatus=0&sign=" + sign + "&platform" + platform;
//                        ret.put("imageUrl", record.getImage());
//                        ret.put("imageUrlOperation", operationUrl);
						BaseMapBean baseMapBean=new BaseMapBean();
						baseMapBean.set("imageUrl", record.getImage());
						baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
						baseMapBean.set(CustomConstants.APP_STATUS_DESC, URLEncoder.encode(statusDesc, "UTF-8"));
						baseMapBean.set("imageUrlOperation", operationUrl);
						baseMapBean.setCallBackAction(CustomConstants.HOST+AppUserDefine.REGIST_RESULT_SUCCESS);
						// add by huanghui  合规审查 20181120 start
						baseMapBean.set("userType", String.valueOf(userType));
						// add by huanghui  合规审查 20181120 end

						ret.put(CustomConstants.APP_STATUS, 0);
						ret.put(CustomConstants.APP_STATUS_DESC, statusDesc);
						// 返回给前台 判断注册用户类型
						ret.put("userType", userType);
						ret.put("successUrl", baseMapBean.getUrl());
						return ret;
					}

					// 发券成功
					// 发送短信通知
					user.setMobile(mobile);
					sendSmsCoupon(user);
				}else {
//                    ret.put("imageUrl", "");
//                    ret.put("imageUrlOperation", "");
					BaseMapBean baseMapBean=new BaseMapBean();
					baseMapBean.set("imageUrl", "");
					baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
					baseMapBean.set(CustomConstants.APP_STATUS_DESC, URLEncoder.encode(statusDesc, "UTF-8"));
					baseMapBean.set("imageUrlOperation", "");
					baseMapBean.setCallBackAction(CustomConstants.HOST+AppUserDefine.REGIST_RESULT_SUCCESS);
					// add by huanghui  合规审查 20181120 start
					baseMapBean.set("userType", String.valueOf(userType));
					// add by huanghui  合规审查 20181120 end

					ret.put(CustomConstants.APP_STATUS, 0);
					ret.put(CustomConstants.APP_STATUS_DESC, statusDesc);
					// 返回给前台 判断注册用户类型
					ret.put("userType", userType);
					ret.put("successUrl", baseMapBean.getUrl());
					return ret;
				}
				// add by zhangjinpeng 注册送888元新手红包 end
				BaseMapBean baseMapBean=new BaseMapBean();
				baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
				baseMapBean.set(CustomConstants.APP_STATUS_DESC, URLEncoder.encode(statusDesc, "UTF-8"));
				baseMapBean.setCallBackAction(CustomConstants.HOST+AppUserDefine.REGIST_RESULT_SUCCESS);
				// add by huanghui  合规审查 20181120 start
				baseMapBean.set("userType", String.valueOf(userType));
				// add by huanghui  合规审查 20181120 end

				ret.put(CustomConstants.APP_STATUS, 0);
				ret.put(CustomConstants.APP_STATUS_DESC, statusDesc);
				// 返回给前台 判断注册用户类型
				ret.put("userType", userType);
				ret.put("successUrl", baseMapBean.getUrl());
				// add by liuyang 注册成功后,发送神策数据统计MQ 20180716 start
				try {
					SensorsDataBean sensorsDataBean = new SensorsDataBean();
					// 将json串转换成Bean
					Map<String, Object> sensorsDataMap = JSONObject.parseObject(presetProps, new TypeReference<Map<String, Object>>() {
					});
					sensorsDataBean.setPresetProps(sensorsDataMap);
					sensorsDataBean.setUserId(userId);
					sensorsDataBean.setEventCode("sign_up");
					// 发送神策数据统计MQ
					this.appUserService.sendSensorsDataMQ(sensorsDataBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// add by liuyang 注册成功后,发送神策数据统计MQ 20180716 end
				return ret;
			} else {
				ret.put(CustomConstants.APP_STATUS, 1);
				ret.put(CustomConstants.APP_STATUS_DESC, "注册失败,参数异常");
				ret.put("successUrl", "");
				return ret;
			}
		} catch (Exception e) {
			ret.put(CustomConstants.APP_STATUS, 1);
			ret.put(CustomConstants.APP_STATUS_DESC, "注册发生错误,参数异常");
			ret.put("successUrl", "");

		}

		LogUtil.endLog(AppUserDefine.THIS_CLASS, AppUserDefine.REGIST_ACTION);
		return ret;
	}

	/**
	 * 注册888红包发放
	 * @param user
	 */
	private void sendCoupon(Users user) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("userId", user.getUserId().toString());
		params.put("sendFlg", "11");

		String signValue = StringUtils.lowerCase(MD5.toMD5Code(SOA_COUPON_KEY + user.getUserId().toString() + 11 + SOA_COUPON_KEY));
		params.put("sign", signValue);

		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_COUPON_SEND, JSONObject.toJSONString(params));
	}

	/**
	 * 修改密码
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppUserDefine.UPDATEPASSWORD_ACTION, method = RequestMethod.POST)
	public JSONObject updatePasswordAction(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.GETBACK_PASSWORD_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", AppUserDefine.UPDATEPASSWORD_REQUEST);

		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// 唯一标识
		String sign = request.getParameter("sign");
		// token
		String token = request.getParameter("token");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");

		// 新密码
		String newPassword = request.getParameter("newPassword");
		// 旧密码
		String oldPassword = request.getParameter("oldPassword");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(sign) || Validator.isNull(token) || Validator.isNull(randomString) || Validator.isNull(order)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		String username = SecretUtil.getUserName(sign);
		if (username != null) {
			try {
				// 解密
				oldPassword = DES.decodeValue(key, oldPassword);
				if (Validator.isNull(oldPassword)) {
					ret.put("status", "1");
					ret.put("statusDesc", "旧密码不能为空");
					return ret;
				}
				int result = appUserService.queryOldPassword(username, oldPassword);
				switch (result) {
					case 0:
						// 验证成功
						break;
					case -1:
						ret.put("status", "1");
						ret.put("statusDesc", "旧密码不正确");
						return ret;
					case -2:
						ret.put("status", "1");
						ret.put("statusDesc", "用户不存在");
						return ret;
					case -3:
						ret.put("status", "1");
						ret.put("statusDesc", "存在多个相同用户");
						return ret;
					default:
						break;
				}
			} catch (Exception e) {
				ret.put("status", "1");
				ret.put("statusDesc", e.getMessage());
				return ret;
			}
		} else {
			ret.put("status", "1");
			ret.put("statusDesc", "用户信息不存在");
			return ret;
		}

		// 业务逻辑
		try {
			// 取得用户ID
			Integer userId = SecretUtil.getUserId(sign);

			if (userId != null) {
				// 解密
				newPassword = DES.decodeValue(key, newPassword);
				if (Validator.isNull(newPassword)) {
					ret.put("status", "1");
					ret.put("statusDesc", "新密码不能为空");
					return ret;
				}
				if (newPassword.length() < 6 || newPassword.length() > 16) {
					ret.put("status", "1");
					ret.put("statusDesc", "密码长度6-16位");
					return ret;
				}
//				if (Validator.isNumber(newPassword.substring(0, 1))) {
//					ret.put("status", "1");
//					ret.put("statusDesc", "密码首位必须为字母");
//					return ret;
//				}
				boolean hasNumber = false;

				for (int i = 0; i < newPassword.length(); i++) {
					if (Validator.isNumber(newPassword.substring(i, i + 1))) {
						hasNumber = true;
						break;
					}
				}
				if (!hasNumber) {
					ret.put("status", "1");
					ret.put("statusDesc", "密码必须包含数字");
					return ret;
				}
				String regEx = "^[a-zA-Z0-9]+$";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(newPassword);
				if (!m.matches()) {
					ret.put("status", "1");
					ret.put("statusDesc", "密码必须由数字和字母组成，如abc123");
					return ret;
				}
				boolean success = appUserService.updatePasswordAction(userId, newPassword);

				if (success) {
					Users users = appUserService.getUsersByUserId(userId);
					UsersInfo usersInfo = appUserService.getUsersInfoByUserId(userId);
					UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
					userOperationLogEntity.setOperationType(7);
					userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
					userOperationLogEntity.setPlatform(request.getParameter("realPlatform")==null?Integer.valueOf(platform):Integer.valueOf(request.getParameter("realPlatform")));
					userOperationLogEntity.setRemark("");
					userOperationLogEntity.setOperationTime(new Date());
					userOperationLogEntity.setUserName(users.getUsername());
					userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
					appUserService.sendUserLogMQ(userOperationLogEntity);
					//如果修改密码成功或者重置密码就将登陆密码错误次数的key删除
					RedisUtils.del(RedisConstants.PASSWORD_ERR_COUNT_ALL+userId);
					ret.put("status", "0");
					ret.put("statusDesc", "修改密码成功");
				} else {
					ret.put("status", "1");
					ret.put("statusDesc", "修改密码失败");
				}

			} else {
				ret.put("status", "1");
				ret.put("statusDesc", "用户信息不存在");
			}

		} catch (Exception e) {
			ret.put("status", "1");
			ret.put("statusDesc", "修改密码发生错误");
		}

		LogUtil.endLog(AppUserDefine.THIS_CLASS, AppUserDefine.UPDATEPASSWORD_REQUEST);
		return ret;
	}

	/**
	 * 发送验证码
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppUserDefine.SEND_VERIFICATIONCODE_ACTION)
	public JSONObject sendVerificationCodeAction(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.SEND_VERIFICATIONCODE_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("bankCode", "");//业务授权码
		ret.put("request", AppUserDefine.SEND_VERIFICATIONCODE_REQUEST);

		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// 唯一标识
		String sign = request.getParameter("sign");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");

		// 验证码类型
		String verificationType = request.getParameter("verificationType");
		// 手机号
		String mobile = request.getParameter("mobile");

		boolean isNeedUPdate = UpdateNoticeUtils.checkForAppUpdate(version, "此版本暂不可用，请更新至最新版本。", AppUserDefine.SEND_VERIFICATIONCODE_REQUEST, ret);
		if(isNeedUPdate){
			return ret;
		}


		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(sign) || Validator.isNull(randomString) || Validator.isNull(order)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}


		// 业务逻辑
		try {
			// 解密
			mobile = DES.decodeValue(key, mobile);
			if (Validator.isNull(verificationType)) {
				ret.put("status", "1");
				ret.put("statusDesc", "验证码类型不能为空");
				return ret;
			}
			if (Validator.isNull(mobile)) {
				ret.put("status", "1");
				ret.put("statusDesc", "手机号不能为空");
				return ret;
			}
			if (!Validator.isMobile(mobile)) {
				ret.put("status", "1");
				ret.put("statusDesc", "请输入您的真实手机号码");
				return ret;
			}
			if (verificationType.equals(AppUserDefine.PARAM_TPL_YZYSJH)) {
				ret.put("status", "1");
				ret.put("statusDesc", "功能升级，请联系客服");
				return ret;
			}

			if (!(verificationType.equals(AppUserDefine.PARAM_TPL_ZHUCE) || verificationType.equals(AppUserDefine.PARAM_TPL_ZHAOHUIMIMA) || verificationType.equals(AppUserDefine.PARAM_TPL_BDYSJH)
					|| verificationType.equals(AppUserDefine.PARAM_TPL_YZYSJH) || verificationType.equals(AppUserDefine.PARAM_TPL_SMS_WITHDRAW))) {
				ret.put("status", "1");
				ret.put("statusDesc", "无效的验证码类型");
				return ret;
			}
			{
				int cnt = appUserService.countUserByMobile(mobile);
				if (verificationType.equals(AppUserDefine.PARAM_TPL_ZHUCE)) {
					if (cnt > 0) {
						ret.put("status", "1");
						ret.put("statusDesc", "该手机号已经注册");
						return ret;
					}
				} else if (verificationType.equals(AppUserDefine.PARAM_TPL_ZHAOHUIMIMA) || verificationType.equals(AppUserDefine.PARAM_TPL_YZYSJH)) {
					if (cnt == 0) {
						ret.put("status", "1");
						ret.put("statusDesc", "该手机号尚未注册");
						return ret;
					}

				} else if (verificationType.equals(AppUserDefine.PARAM_TPL_BDYSJH)) {
					Integer userId = appUserService.getUserIdByMobile(mobile);
					Integer userSign = SecretUtil.getUserId(sign);
					if (userId != null && userId != 0 && userSign.intValue() != userId.intValue() ) {
						ret.put("status", "1");
						ret.put("statusDesc", "手机号已存在，请重新填写");
						return ret;
					}
				}
			}
			if(!verificationType.equals(AppUserDefine.PARAM_TPL_BDYSJH)){
				SmsConfig smsConfig = appUserService.getSmsConfig();
				// 短信加固
				validateSmsTemplate(mobile, smsConfig, request);

				//判断用户是否登录
				Integer userId = SecretUtil.getUserIdNoException(sign);

				// 发送短信
				boolean success = sendVerificationCodeAction(mobile, smsConfig, verificationType, platform, request, userId);

				if (success) {
					ret.put("status", "0");
					ret.put("statusDesc", "发送验证码成功");
				} else {
					ret.put("status", "1");
					ret.put("statusDesc", "发送验证码失败");
				}
			}else{
				//判断用户是否登录
				Integer userId = SecretUtil.getUserId(sign);
				// 判断是否开户  假如未开户  发送平台的验证码  假如已开户  发送江西银行的验证码
				BankOpenAccount bankOpenAccount = appUserService.getBankOpenAccount(userId);
				if (bankOpenAccount == null) {
					// 未开户  发送平台验证码
					SmsConfig smsConfig = appUserService.getSmsConfig();
					// 短信加固
					validateSmsTemplate(mobile, smsConfig, request);

					// 发送短信
					boolean success = sendVerificationCodeAction(mobile, smsConfig, verificationType, platform, request, userId);
					ret.put("bankCode",  "");//业务授权码为空
					if (success) {
						ret.put("status", "0");
						ret.put("statusDesc", "发送验证码成功");
					} else {
						ret.put("status", "1");
						ret.put("statusDesc", "发送验证码失败");
					}
					return ret;
				}

				// 请求发送短信验证码
				BankCallBean bean = this.appUserService.sendSms(userId, BankCallMethodConstant.TXCODE_MOBILE_MODIFY_PLUS, mobile, BankCallConstant.CHANNEL_APP);
				if(bean == null){
					ret.put("status", "1");
					ret.put("statusDesc","发送短信验证码异常");
					return ret;
				}
				//返回失败
				if(!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())){
					if(!Validator.isNull(bean.getSrvAuthCode())){
						ret.put("status", "0");
						ret.put("statusDesc", "发送验证码成功");
						ret.put("bankCode", bean.getSrvAuthCode());//业务授权码
						return ret;
					}
					ret.put("status", "1");
					ret.put("statusDesc","发送短信验证码失败，失败原因：" + bean.getRetMsg());
					return ret;
				}
				//成功返回业务授权码
				ret.put("status", "0");
				ret.put("statusDesc", "发送验证码成功");
				ret.put("bankCode", bean.getSrvAuthCode());//业务授权码
			}

		} catch (Exception e) {
			ret.put("status", "1");
			ret.put("statusDesc", e.getMessage());
		}

		LogUtil.endLog(AppUserDefine.THIS_CLASS, AppUserDefine.SEND_VERIFICATIONCODE_ACTION);
		return ret;

	}

	/**
	 * 验证验证码
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppUserDefine.VALIDATE_VERIFICATIONCODE_ACTION, method = RequestMethod.POST)
	public JSONObject validateVerificationCodeAction(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.VALIDATE_VERIFICATIONCODE_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", AppUserDefine.VALIDATE_VERIFICATIONCODE_REQUEST);

		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// 唯一标识
		String sign = request.getParameter("sign");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");

		// 验证方式
		String verificationType = request.getParameter("verificationType");
		// 验证码
		String verificationCode = request.getParameter("verificationCode");
		// 手机号
		String mobile = request.getParameter("mobile");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(sign) || Validator.isNull(randomString) || Validator.isNull(order)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		if (Validator.isNull(verificationType)) {
			ret.put("status", "1");
			ret.put("statusDesc", "验证码类型不能为空");
			return ret;
		}
		if (Validator.isNull(verificationCode)) {
			ret.put("status", "1");
			ret.put("statusDesc", "验证码不能为空");
			return ret;
		}
		if (verificationType.equals(AppUserDefine.PARAM_TPL_YZYSJH)) {
			ret.put("status", "1");
			ret.put("statusDesc", "功能升级，请联系客服");
			return ret;
		}

		if (!(verificationType.equals(AppUserDefine.PARAM_TPL_ZHUCE) || verificationType.equals(AppUserDefine.PARAM_TPL_ZHAOHUIMIMA) || verificationType.equals(AppUserDefine.PARAM_TPL_BDYSJH) || verificationType.equals(AppUserDefine.PARAM_TPL_YZYSJH))) {
			ret.put("status", "1");
			ret.put("statusDesc", "无效的验证码类型");
			return ret;
		}

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 业务逻辑
		try {
			// 解密
			mobile = DES.decodeValue(key, mobile);
			if (Validator.isNull(mobile)) {
				ret.put("status", "1");
				ret.put("statusDesc", "手机号不能为空");
				return ret;
			}
			if (!Validator.isMobile(mobile)) {
				ret.put("status", "1");
				ret.put("statusDesc", "请输入您的真实手机号码");
				return ret;
			}

			int cnt = appUserService.updateCheckMobileCode(mobile, verificationCode, verificationType, platform, AppUserDefine.CKCODE_NEW, AppUserDefine.CKCODE_YIYAN);

			if (cnt > 0) {
				ret.put("status", "0");
				ret.put("statusDesc", "验证验证码成功");
			} else {
				ret.put("status", "1");
				ret.put("statusDesc", "验证码无效");
			}

		} catch (Exception e) {
			ret.put("status", "1");
			ret.put("statusDesc", "验证验证码发生错误");
		}

		LogUtil.endLog(AppUserDefine.THIS_CLASS, AppUserDefine.VALIDATE_VERIFICATIONCODE_ACTION);
		return ret;
	}

	/**
	 * 绑定新手机
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppUserDefine.BIND_NEWPHONE_ACTION, method = RequestMethod.POST)
	public JSONObject bindNewPhoneAction(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.BIND_NEWPHONE_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", AppUserDefine.BIND_NEWPHONE_REQUEST);

		// 唯一标识
		String sign = request.getParameter("sign");

		// 江西银行业务码
		String bankCode = request.getParameter("bankCode");
		String platform = request.getParameter("platform");
		logger.info("江西银行业务码bankCode :{}", bankCode);

		String failReturnUrl = CustomConstants.HOST + AppUserDefine.SET_MOBILE_FAIL;
		String successReturnUrl = CustomConstants.HOST + AppUserDefine.SET_MOBILE_SUCCESS;

		// 验证码
		String verificationCode = request.getParameter("newVerificationCode");
		// 手机号
		String mobile = request.getParameter("newMobile");
		logger.info("绑定新手机获取mobile :{}", mobile);
		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		logger.info("取得加密用的key :{}", key);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 业务逻辑
		try {
			// 取得用户ID
			Integer userId = SecretUtil.getUserId(sign);

			if (userId != null) {
				// 取得验证码
				mobile = DES.decodeValue(key, mobile);
				logger.info("des解密后得到的mobile :{}", mobile);
				if (Validator.isNull(mobile)) {
					ret.put("status", "1");
					ret.put("statusDesc", "手机号不能为空");
					return ret;
				}
				if (Validator.isNull(verificationCode)) {
					ret.put("status", "1");
					ret.put("statusDesc", "验证码不能为空");
					return ret;
				}
				if (!Validator.isMobile(mobile)) {
					ret.put("status", "1");
					ret.put("statusDesc", "请输入您的真实手机号码");
					return ret;
				}
				{
					int cnt = appUserService.countUserByMobile(mobile);
					Users users = appUserService.getUserByUserId(userId);
					if (cnt > 0 && !users.getMobile().equals(mobile)) {
						ret.put("status", "1");
						ret.put("statusDesc", "该手机号已经注册");
						return ret;
					}
				}
				// 判断是否开户  假如未开户  修改平台手机号   已开户 修改江西银行和平台
				BankOpenAccount bankOpenAccount = appUserService.getBankOpenAccount(userId);
				if (bankOpenAccount == null) {
					int cnt = appUserService.updateCheckMobileCode(mobile, verificationCode, AppUserDefine.PARAM_TPL_BDYSJH, platform, AppUserDefine.CKCODE_NEW, AppUserDefine.CKCODE_YIYAN);
					if (cnt > 0) {
						// 未开户 修改平台手机号
						int result = this.appUserService.updateNewPhoneAction(userId, mobile);
						switch (result) {
							case 0:
								ret.put("status", "0");
								ret.put("statusDesc", "修改手机号成功");
								ret.put("mobile", mobile);
								ret.put("successUrl", successReturnUrl + "?status=000&statusDesc=" + "您已绑定手机号" + mobile.substring(0, 3).concat("****").concat(mobile.substring(7)));
								break;
							default:
								break;
						}
					} else {
						ret.put("status", "1");
						ret.put("statusDesc", "验证码无效");
					}
					return ret;
				}

				if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(bankCode) || StringUtils.isEmpty(verificationCode)) {
					ret.put("status", "1");
					ret.put("statusDesc", "请求参数非法");
					return ret;
				}

				// 调用电子账号手机号修改增强
				BankCallBean bean = new BankCallBean();
				bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
				bean.setTxCode(BankCallConstant.TXCODE_MOBILE_MODIFY_PLUS);// 消息类型 修改手机号增强 mobileModifyPlus
				bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
				bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
				bean.setTxDate(GetOrderIdUtils.getTxDate());
				bean.setTxTime(GetOrderIdUtils.getTxTime());
				bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
				bean.setChannel(BankCallConstant.CHANNEL_APP);
				bean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));//电子账号
				bean.setOption(BankCallConstant.OPTION_1);//修改
				bean.setMobile(mobile);// 新手机号
				bean.setLastSrvAuthCode(bankCode);//业务授权码
				bean.setSmsCode(verificationCode);//短信验证码
				// 商户私有域，存放开户平台,用户userId
				LogAcqResBean acqRes = new LogAcqResBean();
				acqRes.setUserId(userId);
				bean.setLogAcqResBean(acqRes);
				// 操作者ID
				bean.setLogUserId(String.valueOf(userId));
				bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
				//返回参数
				BankCallBean retBean = null;
				try {
					//调用接口
					retBean = BankCallUtils.callApiBg(bean);
					LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.BIND_NEWPHONE_ACTION);
				} catch (Exception e) {
					LogUtil.errorLog(AppUserDefine.THIS_CLASS, AppUserDefine.BIND_NEWPHONE_ACTION, e);
					ret.put("status", "1");
					ret.put("statusDesc", "调用银行接口异常！");
					ret.put("successUrl", failReturnUrl + "?status=99&statusDesc=调用银行接口异常！");
					return ret;
				}
				if (retBean == null) {
					ret.put("status", "1");
					ret.put("statusDesc", "修改手机号失败，系统异常");
					ret.put("successUrl", failReturnUrl + "?status=99&statusDesc=修改手机号失败，系统异常！");
					return ret;
				}
				//返回失败
				if (!BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
					String errorMsg = retBean.getRestMsg();
					if(StringUtils.isBlank(errorMsg)){
						errorMsg = this.appUserService.getBankRetMsg(retBean.getRetCode());
					}
					if(StringUtils.isBlank(errorMsg)){
						errorMsg = "修改手机号失败...";
					}
					ret.put("status", "1");
					ret.put("statusDesc", errorMsg);
					ret.put("successUrl", failReturnUrl + "?status=99&statusDesc=" + errorMsg);
					return ret;
				}
				//修改手机号
				int result = this.appUserService.updateNewPhoneAction(userId, mobile);
				switch (result) {
					case 0:
						// add by liuyang 修改手机号之后 发送同步CA认证信息修改MQ start
						this.appUserService.sendCAMQ(userId);
						// add by liuyang 修改手机号之后 发送同步CA认证信息修改MQ end
						ret.put("status", "0");
						ret.put("statusDesc", "修改手机号成功");
						ret.put("mobile", mobile);
						ret.put("successUrl", successReturnUrl + "?status=000&statusDesc=" + "您已绑定手机号" + mobile.substring(0, 3).concat("****").concat(mobile.substring(7)));
						break;
					default:
						break;
				}
			} else {
				ret.put("status", "1");
				ret.put("statusDesc", "用户信息不存在");
				ret.put("successUrl", failReturnUrl + "?status=99&statusDesc=用户信息不存在!");
			}
		} catch (Exception e) {
			logger.error("绑定新手机发生错误...", e);
			ret.put("status", "1");
			ret.put("statusDesc", "绑定新手机发生错误");
			ret.put("successUrl", failReturnUrl + "?status=99&statusDesc=绑定新手机发生错误");
		}
//		}
		LogUtil.endLog(AppUserDefine.THIS_CLASS, AppUserDefine.BIND_NEWPHONE_ACTION);
		return ret;
	}

	/**
	 * 用户退出登录
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppUserDefine.LOGIN_OUT_ACTION, method = RequestMethod.POST)
	public JSONObject loginOutAction(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.LOGIN_OUT_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", AppUserDefine.LOGIN_OUT_REQUEST);

		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// 唯一标识
		String sign = request.getParameter("sign");
		// token
		String token = request.getParameter("token");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(sign) || Validator.isNull(token) || Validator.isNull(randomString) || Validator.isNull(order)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 业务逻辑
		try {
			// 取得用户ID
			Integer userId = SecretUtil.getUserId(sign);
			Users users = appUserService.getUsersByUserId(userId);
			UsersInfo usersInfo = appUserService.getUsersInfoByUserId(userId);
			UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
			userOperationLogEntity.setOperationType(2);
			userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
			userOperationLogEntity.setPlatform(request.getParameter("realPlatform")==null?Integer.valueOf(platform):Integer.valueOf(request.getParameter("realPlatform")));
			userOperationLogEntity.setRemark("");
			userOperationLogEntity.setOperationTime(new Date());
			userOperationLogEntity.setUserName(users.getUsername());
			userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
			appUserService.sendUserLogMQ(userOperationLogEntity);
			if (userId != null) {
				clearMobileCode(userId,sign);

				// 移除sign
//	            SecretUtil.clearToken(sign);
				if(version.substring(0,5).equals(NEW_VERSION) && "6bcbd50a-27c4-4aac-b448-ea6b1b9228f43GYE604".equals(sign)){
					logger.info(AppUserDefine.THIS_CLASS, "sign不做移除");
				}else{
					SecretUtil.clearToken(sign);
				}


				ret.put("status", "0");
				ret.put("statusDesc", "退出登录成功");
			} else {
				ret.put("status", "1");
				ret.put("statusDesc", "用户信息不存在");
			}

		} catch (Exception e) {
			ret.put("status", "1");
			ret.put("statusDesc", "退出登录发生错误");
		}

		LogUtil.endLog(AppUserDefine.THIS_CLASS, AppUserDefine.LOGIN_OUT_ACTION);
		return ret;
	}

	private void clearMobileCode(Integer userId, String sign) {
		appUserService.clearMobileCode(userId,sign);

	}

	/**
	 * 找回密码(重置密码)
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppUserDefine.GETBACK_PASSWORD_ACTION, method = RequestMethod.POST)
	public JSONObject getBackPasswordAction(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(AppUserDefine.THIS_CLASS, AppUserDefine.GETBACK_PASSWORD_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", AppUserDefine.GETBACK_PASSWORD_REQUEST);

		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// 唯一标识
		String sign = request.getParameter("sign");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");

		// 新密码
		String newPassword = request.getParameter("newPassword");
		// 验证方式
		String verificationCode = request.getParameter("verificationCode");
		// 手机号
		String mobile = request.getParameter("mobile");

		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(sign) || Validator.isNull(randomString) || Validator.isNull(order) || Validator.isNull(mobile) || Validator.isNull(verificationCode) || Validator.isNull(newPassword)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}

		// 业务逻辑
		try {

			// 根据手机号取得用户ID
			Integer userId = appUserService.getUserIdByMobile(mobile);

			if (userId != null) {
				if (Validator.isNull(mobile)) {
					ret.put("status", "1");
					ret.put("statusDesc", "手机号不能为空");
					return ret;
				}
				if (!Validator.isMobile(mobile)) {
					ret.put("status", "1");
					ret.put("statusDesc", "请输入您的真实手机号码");
					return ret;
				}
				if (Validator.isNull(verificationCode)) {
					ret.put("status", "1");
					ret.put("statusDesc", "验证码不能为空");
					return ret;
				}
				if (Validator.isNull(newPassword)) {
					ret.put("status", "1");
					ret.put("statusDesc", "密码不能为空");
					return ret;
				}
				if (newPassword.length() < 6 || newPassword.length() > 16) {
					ret.put("status", "1");
					ret.put("statusDesc", "密码长度6-16位");
					return ret;
				}
//				if (Validator.isNumber(newPassword.substring(0, 1))) {
//					ret.put("status", "1");
//					ret.put("statusDesc", "密码首位必须为字母");
//					return ret;
//				}
				boolean hasNumber = false;

				for (int i = 0; i < newPassword.length(); i++) {
					if (Validator.isNumber(newPassword.substring(i, i + 1))) {
						hasNumber = true;
						break;
					}
				}
				if (!hasNumber) {
					ret.put("status", "1");
					ret.put("statusDesc", "密码必须包含数字");
					return ret;
				}

				String regEx = "^[a-zA-Z0-9]+$";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(newPassword);
				if (!m.matches()) {
					ret.put("status", "1");
					ret.put("statusDesc", "密码必须由数字和字母组成，如abc123");
					return ret;
				}
				String verificationType = AppUserDefine.PARAM_TPL_ZHAOHUIMIMA;
				int cnt = appUserService.updateCheckMobileCode(mobile, verificationCode, verificationType, platform, AppUserDefine.CKCODE_YIYAN, AppUserDefine.CKCODE_USED);
				if (cnt == 0) {
					ret.put("status", "1");
					ret.put("statusDesc", "验证码无效");
					return ret;
				}

				boolean success = appUserService.updatePasswordAction(userId, newPassword);

				if (success) {
					//如果修改密码成功或者重置密码就将登陆密码错误次数的key删除
//					RedisUtils.del(RedisConstants.PASSWORD_ERR_COUNT_ALL+userId);
					ret.put("status", "0");
					ret.put("statusDesc", "找回密码成功");
				} else {
					ret.put("status", "1");
					ret.put("statusDesc", "找回密码失败");
				}
			} else {
				ret.put("status", "1");
				ret.put("statusDesc", "用户信息不存在");
			}
		} catch (Exception e) {
			ret.put("status", "1");
			ret.put("statusDesc", "找回密码发生错误");
		}

		LogUtil.endLog(AppUserDefine.THIS_CLASS, AppUserDefine.GETBACK_PASSWORD_ACTION);
		return ret;
	}

	// =======================验证方法===========================

	/**
	 * 短信发送时的加固验证
	 *
	 * @param request
	 * @param mobile
	 */
	private void validateSmsTemplate(String mobile, SmsConfig smsConfig, HttpServletRequest request) throws Exception {
		String ip = CustomUtil.getIpAddr(request);
		String ipCount = RedisUtils.get(ip + ":MaxIpCount");
		if (StringUtils.isBlank(ipCount)) {
			ipCount = "0";
			RedisUtils.set(ip + ":MaxIpCount", "0");
		}
		System.out.println(mobile + "------ip---" + ip + "----------MaxIpCount-----------" + ipCount);
		if (Integer.valueOf(ipCount) >= smsConfig.getMaxIpCount()) {
			if (Integer.valueOf(ipCount) == smsConfig.getMaxIpCount()) {
				try {
					appUserService.sendSms(mobile, "IP访问次数超限:" + ip);
				} catch (Exception e) {
				}
				RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(ipCount) + 1) + "", 24 * 60 * 60);
			}
			try {
				appUserService.sendEmail(mobile, "IP访问次数超限" + ip);
			} catch (Exception e) {
			}
			throw new Exception("IP访问次数超限");
		}

		// 判断最大发送数max_phone_count
		String count = RedisUtils.get(mobile + ":MaxPhoneCount");
		if (StringUtils.isBlank(count)) {
			count = "0";
			RedisUtils.set(mobile + ":MaxPhoneCount", "0");
		}
		int maxPhoneCount = smsConfig.getMaxPhoneCount();
		;// 测试时临时是30,正式上线改为smsConfig.getMaxPhoneCount();
		if (Integer.valueOf(count) >= maxPhoneCount) {
			if (Integer.valueOf(count) == maxPhoneCount) {
				try {
					appUserService.sendSms(mobile, "手机发送次数超限");
				} catch (Exception e) {
				}
				RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
			}
			try {
				appUserService.sendEmail(mobile, "手机发送次数超限");
			} catch (Exception e) {
			}
			throw new Exception("手机发送次数超限");
		}
		// 判断发送间隔时间
		String intervalTime = RedisUtils.get(mobile + ":IntervalTime");
		if (StringUtils.isNotBlank(intervalTime)) {
			throw new Exception("验证码发送过于频繁");
		}
	}

	private Boolean sendVerificationCodeAction(String mobile, SmsConfig smsConfig, String verificationType, String platform, HttpServletRequest request, Integer userId) {
		// 生成验证码
		String checkCode = GetCode.getRandomSMSCode(6);
		Map<String, String> param = new HashMap<String, String>();
		param.put("val_code", checkCode);

		if(verificationType.equals(AppUserDefine.PARAM_TPL_SMS_WITHDRAW) && userId != null){
			UsersInfo user = appUserService.getUsersInfoByUserId(userId);
			param.put("val_name", user.getTruename().substring(0, 1));
			param.put("val_sex", user.getSex() == 2 ? "女士" : "先生");
			param.put("val_amount", request.getParameter("withdrawmoney"));
		}
		// 发送短信验证码
		SmsMessage smsMessage =
				new SmsMessage(null, param, mobile, null, MessageDefine.SMSSENDFORMOBILE, null,
						verificationType, CustomConstants.CHANNEL_TYPE_NORMAL);
		Integer result = (smsProcesser.gather(smsMessage) >= 1) ? 0 : 1;


		// checkCode过期时间，默认120秒
		RedisUtils.set(mobile + ":MaxValidTime", checkCode, smsConfig.getMaxValidTime() == null ? 120 : smsConfig.getMaxValidTime() * 60);

		// 发送checkCode最大时间间隔，默认60秒
		RedisUtils.set(mobile + ":IntervalTime", mobile, smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime());

		// 短信发送成功后处理 临时注释掉
		if (result != null && result == 0) {
			// 累加IP次数
			String ip = CustomUtil.getIpAddr(request);
			String currentMaxIpCount = RedisUtils.get(ip + ":MaxIpCount");
			if (StringUtils.isBlank(currentMaxIpCount)) {
				currentMaxIpCount = "0";
			}
			// 累加手机次数
			String currentMaxPhoneCount = RedisUtils.get(mobile + ":MaxPhoneCount");
			if (StringUtils.isBlank(currentMaxPhoneCount)) {
				currentMaxPhoneCount = "0";
			}
			RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(currentMaxIpCount) + 1) + "", 24 * 60 * 60);
			RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(currentMaxPhoneCount) + 1) + "", 24 * 60 * 60);
		}

		// 保存短信验证码
		appUserService.saveSmsCode(mobile, checkCode, verificationType, result, platform);
		return true;
	}

	/**
	 * 发送短信(注册送188元新手红包)
	 *
	 * @param users
	 */
	private void sendSmsCoupon(Users users) {
		if (users == null || Validator.isNull(users.getUserId())) {
			return;
		}
		SmsMessage smsMessage = new SmsMessage(users.getUserId(), null, users.getMobile(), null, MessageDefine.SMSSENDFORMOBILE, null,
				CustomConstants.PARAM_TPL_TZJ_188HB, CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);

	}

	/**
	 * 发送成功消息队列
	 * @param userid
	 * @param ip
	 */
	private void sendIpInfo(int userid, String ip) {

		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("mqMsgId", GetCode.getRandomCode(10));
			params.put("userId", String.valueOf(userid));
			params.put("regIp", ip);

			rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME,
					RabbitMQConstants.ROUTINGKEY_SYNC_USER_IP_USER, JSONObject.toJSONString(params));

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public static void main(String[] args) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher("17090000000");
		b = m.matches();
		System.out.println(b);
		m = p.matcher("13990000000");
		b = m.matches();
		System.out.println(b);
	}

	// 组装url
	private String packageStr(HttpServletRequest request) {
		StringBuffer sbUrl = new StringBuffer();
		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// token
		String token = request.getParameter("token");
		// 唯一标识
		String sign = request.getParameter("sign");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");
		sbUrl.append("?").append("version").append("=").append(version);
		sbUrl.append("&").append("netStatus").append("=").append(netStatus);
		sbUrl.append("&").append("platform").append("=").append(platform);
		sbUrl.append("&").append("randomString").append("=").append(randomString);
		sbUrl.append("&").append("sign").append("=").append(sign);
		sbUrl.append("&").append("token").append("=").append(strEncode(token));
		sbUrl.append("&").append("order").append("=").append(strEncode(order));
		return sbUrl.toString();
	}
}
