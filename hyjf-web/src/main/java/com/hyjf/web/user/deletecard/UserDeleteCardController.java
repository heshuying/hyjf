/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:32:36
 * Modification History:
 * Modified by :
 */
package com.hyjf.web.user.deletecard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseController;
import com.hyjf.web.util.WebUtils;

/**
 * 用户绑卡
 *
 * @author HBZ
 */
@Controller
@RequestMapping(value = UserDeleteCardDefine.REQUEST_MAPPING)
public class UserDeleteCardController extends BaseController {

	/** THIS_CLASS */
	private static final String THIS_CLASS = UserDeleteCardController.class.getName();

	@Autowired
	private UserDeleteCardService userDeleteCardService;

	/**
	 * 用户删除银行卡
	 *
	 * @param request
	 * @param ret
	 * @return
	 */
    @ResponseBody
	@RequestMapping(UserDeleteCardDefine.REQUEST_MAPPING)
	public JSONObject deleteCard(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserDeleteCardController.class.toString(), UserDeleteCardDefine.REQUEST_MAPPING);
		String cardId = request.getParameter("cardId");
		JSONObject ret = new JSONObject();
		Integer userId = WebUtils.getUserId(request);
		// 检查参数
		if (Validator.isNull(WebUtils.getUserId(request))) {
		    ret.put("status","false");
            return ret;
		}
		if (Validator.isNull(cardId)) {
		    ret.put("status","false");
            return ret;
		}

		// 取得用户在汇付天下的客户号
		AccountChinapnr accountChinapnrTender = userDeleteCardService.getAccountChinapnr(userId);
		Long chinapnrUsrcustidTender = accountChinapnrTender.getChinapnrUsrcustid();
		//获取用户的银行卡
		AccountBank bankCard = this.userDeleteCardService.getUserBankCard(Integer.parseInt(cardId));
		if(Validator.isNull(bankCard)){
			ret.put("status","false");
            return ret;
		}
		String cardNo = bankCard.getAccount();
		// 调用汇付接口(4.2.6 删除银行卡接口)
        ChinapnrBean retBean = null;
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
		bean.setCmdId(ChinaPnrConstant.CMDID_DEL_CARD); // 消息类型
        bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID)); // 商户客户号
		bean.setUsrCustId(String.valueOf(chinapnrUsrcustidTender));// 用户客户号
        bean.setCardId(cardNo);// 银行卡号
		bean.setChkValue(ChinaPnrConstant.PARAM_CHKVALUE); // 签名
		// 调用汇付接口
		try {
		    retBean = ChinapnrUtil.callApiBg(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//回调数据处理  
		//RespCode:000 成功     544 此银行卡已被删除
		if(retBean == null || !(retBean.getRespCode().equals("000")||retBean.getRespCode().equals("544"))){
		    if(retBean != null ){
		          LogUtil.debugLog(THIS_CLASS, "RespCode:"+retBean.getRespCode()+"&&&&&&&&&&& RespDesc:"+retBean.getRespDesc());
		    }
		    ret.put("status","false");
            return ret;
		}else if(retBean != null && (retBean.getRespCode().equals("000")||retBean.getRespCode().equals("544"))){
		 // 执行删除卡后处理,判断银行卡状态，删除平台本地银行卡信息
            this.userDeleteCardService.updateAfterDeleteCard(bean, userId);
            ret.put("status","success");
		}
		LogUtil.endLog(UserDeleteCardController.class.toString(), UserDeleteCardDefine.REQUEST_MAPPING);
		return ret;
	}
}
