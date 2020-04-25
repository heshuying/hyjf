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
package com.hyjf.web.bank.web.user.unbindcard;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hyjf.bank.service.user.unbindcard.UnBindCardService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.web.BaseController;
import com.hyjf.web.chinapnr.ChinapnrService;

/**
 * 用户绑卡
 *
 * @author HBZ
 */
@Controller
@RequestMapping(value = UnBindCardDefine.REQUEST_MAPPING)
public class UnBindCardController extends BaseController {

	@Autowired
	private UnBindCardService userUnBindCardService;
	@Autowired
	private ChinapnrService chinapnrService;

	/**
	 * 用户解绑快捷卡
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(UnBindCardDefine.RETURN_MAPPING)
	public void unBindCardReturn(HttpServletRequest request, @ModelAttribute ChinapnrBean bean) {
		System.out.println("测试用户解绑快捷卡,用户客户号:" + bean.getCustId());
		LogUtil.startLog(UnBindCardController.class.toString(), UnBindCardDefine.REQUEST_MAPPING);
		// 返回Code
		boolean updateFlag = false;
		// 取得更新用UUID
		String uuid = request.getParameter("uuid");
		System.out.println("uuid:" + uuid);
		if (Validator.isNotNull(uuid)) {
			// 取得检证数据
			ChinapnrExclusiveLogWithBLOBs record = chinapnrService.selectChinapnrExclusiveLog(Long.parseLong(uuid));
			// 如果检证数据状态为未发送
			if (record != null) {
				// 检证通过时
				if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(record.getStatus())) {
					// 将状态更新成[2:处理中]
					record.setId(Long.parseLong(uuid));
					record.setResult(bean.getAllParams().toString());
					record.setStatus(ChinaPnrConstant.STATUS_RUNNING);
					int cnt = this.chinapnrService.updateChinapnrExclusiveLog(record);
					// 防止重复提交
					if (cnt > 0) {
						updateFlag = true;
					}
				}
			}

		} else {
			updateFlag = true;
		}
		System.out.println("验签结果:" + updateFlag);

		// 其他程序正在处理中,或者返回值错误
		if (!updateFlag) {
			return;
		}
		System.out.println("测试用户解绑快捷卡,用户客户号:" + bean.getCustId());
		if (bean.getCustId() != null) {
			try {
				// 查询用户userid
				Integer userid = userUnBindCardService.getUserIdByCustId(Long.parseLong(bean.getCustId()));
				System.out.println("用户userid" + userid);
				// 执行解绑操作(执行更新银行卡接口)
				if (userid != null) {
					userUnBindCardService.updateAccountBankByUserId(userid);
				}
				System.out.println("执行成功:" + updateFlag);
				chinapnrService.updateChinapnrExclusiveLogStatus(Long.parseLong(uuid), ChinaPnrConstant.STATUS_SUCCESS);
			} catch (NumberFormatException e) {
				System.out.println("用户解绑快捷卡失败");
				e.printStackTrace();
			}
		}
		LogUtil.endLog(UnBindCardController.class.toString(), UnBindCardDefine.REQUEST_MAPPING);
	}
}