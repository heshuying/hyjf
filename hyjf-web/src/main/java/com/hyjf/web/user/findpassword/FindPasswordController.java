/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0
 */
package com.hyjf.web.user.findpassword;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.RSAJSPUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.web.BaseController;
import com.hyjf.web.user.login.LoginBean;
import com.hyjf.web.user.login.LoginController;
import com.hyjf.web.user.regist.UserRegistDefine;
import com.hyjf.web.user.regist.UserRegistService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller(FindPasswordDefine.CONTROLLOR_CLASS_NAME)
@RequestMapping(value = FindPasswordDefine.REQUEST_MAPPING)
public class FindPasswordController extends BaseController {

	@Autowired
	private UserRegistService registService;
	@Autowired
	private FindPasswordService findPasswordService;

	/**
	 * 跳转到找回密码页面
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(FindPasswordDefine.INIT)
	public ModelAndView init(HttpServletRequest request) throws Exception {
		LogUtil.startLog(LoginController.class.getName(), FindPasswordDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(FindPasswordDefine.INIT_PATH);
		LogUtil.endLog(LoginController.class.getName(), FindPasswordDefine.INIT);
		return modeAndView;
	}
	/**
	 * 验证手机号是否已注册
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = FindPasswordDefine.CHECKPHONE, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public boolean form1Submit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(LoginController.class.getName(), FindPasswordDefine.CHECKPHONE);
		String telnum = request.getParameter("telnum");
		if (Validator.isNull(telnum)) {
			return false;
		} else {
			// 验证是否存在账号
			if (!findPasswordService.existPhone(telnum)) {
				return false;
			}
		}
		LogUtil.endLog(LoginController.class.getName(), FindPasswordDefine.CHECKPHONE);
		return true;
	}
	/**
	 * 验证校验码是否正确
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = FindPasswordDefine.CHECKCODE, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public boolean form2Submit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(LoginController.class.getName(), FindPasswordDefine.CHECKCODE);
	
		String code = request.getParameter("code");
		if (StringUtils.isBlank(code)) {
			return false;
		}
		String telnum = request.getParameter("telnum");// 手机号
		int cnt = this.registService.updateCheckMobileCode(telnum, code, UserRegistDefine.PARAM_TPL_ZHAOHUIMIMA, CustomConstants.CLIENT_PC, UserRegistDefine.CKCODE_YIYAN, UserRegistDefine.CKCODE_YIYAN);
		if (cnt > 0) {
			return true;
		} 
		LogUtil.endLog(LoginController.class.getName(), FindPasswordDefine.CHECKCODE);
		return false;
	}

	/**
	 * 跳转到找回密码第二步
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = FindPasswordDefine.TO_SENCOND_PAGE, method = RequestMethod.POST)
	public ModelAndView sencodPage(HttpServletRequest request) throws Exception {
		LogUtil.startLog(LoginController.class.getName(), FindPasswordDefine.TO_SENCOND_PAGE);
		ModelAndView modelAndView = new ModelAndView(FindPasswordDefine.SECOND_PATH);
		String telnum = request.getParameter("telnum");
		String code = request.getParameter("code");
		modelAndView.addObject("telnum", telnum); //手机号
		modelAndView.addObject("code", code);//校验码
		
//		KeyPair kp = RSAJSPUtil.generateKeyPair();
//        RSAPublicKey pubk = (RSAPublicKey) kp.getPublic();// 生成公钥
//        RSAPrivateKey prik = (RSAPrivateKey) kp.getPrivate();// 生成私钥
//
//        String publicKeyExponent = pubk.getPublicExponent().toString(16);// 16进制
//        String publicKeyModulus = pubk.getModulus().toString(16);// 16进制
//        request.getSession().setAttribute("prik", prik);
//        modeAndView.addObject("pubexponent", publicKeyExponent);
//        modeAndView.addObject("pubmodules", publicKeyModulus);
		modelAndView.addObject("pubexponent", "10001");
		modelAndView.addObject("pubmodules", RSAJSPUtil.getPunlicKeys());
		LogUtil.endLog(LoginController.class.getName(), FindPasswordDefine.TO_SENCOND_PAGE);
		return modelAndView;
	}
	
	/**
	 * 第二步表单校验
	 * @param request
	 * @param response
	 * @param loginBean
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = FindPasswordDefine.CHECKPWD, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject checkPwd(HttpServletRequest request, HttpServletResponse response, LoginBean loginBean) throws Exception {

		LogUtil.startLog(LoginController.class.getName(), FindPasswordDefine.CHECKPWD);
		JSONObject resultJson = new JSONObject();
		// 密码1
		String password1 = request.getParameter("password1");
		// 密码2
		String password2 = request.getParameter("password2");
		// 手机号
		String telnum = request.getParameter("telnum");
		// 短信验证码
		String code = request.getParameter("code");
		
		if (StringUtils.isBlank(password1)) {
			resultJson.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			resultJson.put(UserRegistDefine.ERROR, "密码不能为空");
			return resultJson;
		}
		
		//RSAPrivateKey prik = (RSAPrivateKey) request.getSession().getAttribute("prik");

        password1 = RSAJSPUtil.rsaToPassword(password1);
        password2 = RSAJSPUtil.rsaToPassword(password2);
        
		if (StringUtils.isBlank(password2) || !password1.equals(password2)) {
			resultJson.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			resultJson.put(UserRegistDefine.ERROR, "两次密码不一致");
			return resultJson;
		}
		if (password1.length() < 6 || password1.length() > 16) {
			resultJson.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			resultJson.put(UserRegistDefine.ERROR, "密码必须由数字和字母组成，如abc123");
			return resultJson;
		}
		/*if (Validator.isNumber(password1.substring(0, 1))) {
			resultJson.put(UserRegistDefine.STATUS, UserRegistDefine.STATUS_FALSE);
			resultJson.put(UserRegistDefine.ERROR, "密码首位必须为字母");
			return resultJson;
		}*/

		// 改变验证码状态
		int checkStatus = this.registService.updateCheckMobileCode(telnum, code, UserRegistDefine.PARAM_TPL_ZHAOHUIMIMA, CustomConstants.CLIENT_PC, UserRegistDefine.CKCODE_YIYAN, UserRegistDefine.CKCODE_USED);
		// 再次验证验证码
		if(checkStatus != 1) {
			resultJson.put(FindPasswordDefine.STATUS, FindPasswordDefine.STATUS_FALSE);
			resultJson.put(FindPasswordDefine.INFO, "修改密码失败,验证码已经使用或失效");
			return resultJson;
		}
		
		Integer resultStatu = findPasswordService.updatePassword(telnum, password1);
		switch (resultStatu) {
		case 0:
			resultJson.put(FindPasswordDefine.STATUS, FindPasswordDefine.STATUS_FALSE);
			resultJson.put(FindPasswordDefine.INFO, "修改密码失败,未作任何操作");
			break;
		case -1:
			resultJson.put(FindPasswordDefine.STATUS, FindPasswordDefine.STATUS_FALSE);
			resultJson.put(FindPasswordDefine.INFO, "修改密码失败,没有查到手机号");
			break;
		case -2:
			resultJson.put(FindPasswordDefine.STATUS, FindPasswordDefine.STATUS_FALSE);
			resultJson.put(FindPasswordDefine.INFO, "修改密码失败,存在多个相同手机号");
			break;
		default:
			//如果修改密码成功就将登陆密码错误次数的key删除
			RedisUtils.del(RedisConstants.PASSWORD_ERR_COUNT_ALL+telnum);
			resultJson.put(FindPasswordDefine.STATUS, FindPasswordDefine.STATUS_TRUE);
			resultJson.put(FindPasswordDefine.INFO, "修改密码成功");
			break;
		}
		LogUtil.endLog(LoginController.class.getName(), FindPasswordDefine.CHECKPWD);
		return resultJson;
	}

	/**
	 * 跳转成功页
	 * @param request
	 * @param response
	 * @param loginBean
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(FindPasswordDefine.TO_SUCCESS_PAGE)
	public ModelAndView form4Submit(HttpServletRequest request, HttpServletResponse response, LoginBean loginBean) throws Exception {
		LogUtil.startLog(LoginController.class.getName(), FindPasswordDefine.TO_SUCCESS_PAGE);
		ModelAndView modelAndView = new ModelAndView(FindPasswordDefine.SUCCESS_PATH);
		LogUtil.endLog(LoginController.class.getName(), FindPasswordDefine.TO_SUCCESS_PAGE);
		return modelAndView;
	}

}
