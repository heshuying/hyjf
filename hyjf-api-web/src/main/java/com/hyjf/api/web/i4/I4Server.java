package com.hyjf.api.web.i4;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.Idfa;

/**
 * IDFA验证接口
 * 
 * @author hbz
 * 
 */
@Controller
@RequestMapping(value = I4Define.I4_QUEST_MAPPING_CLASS)
public class I4Server extends BaseController {

	private static final String THIS_CLASS = I4Server.class.getName();

	@Autowired
	I4Service i4Service;

	/**
	 * 检验IDFA
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = I4Define.CHECK_REQUEST_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public JSONObject checkIdfa(HttpServletRequest request, I4Bean paramBean) {
		LogUtil.startLog(THIS_CLASS, I4Define.CHECK_REQUEST_ACTION);
		JSONObject jsonObject = new JSONObject();
		try {
			if (paramBean.getIdfa() != null && !"".equals(paramBean.getIdfa())) {
				
				List<Idfa> idfaList = i4Service.selectIdfas(paramBean.getIdfa());
				
				if (idfaList != null && idfaList.size() > 0) {
					jsonObject.put(I4Define.SUCCESS, I4Define.TRUE);
					jsonObject.put(I4Define.MESSAGE, "成功！");
				}
			} else {
				jsonObject.put(I4Define.SUCCESS, I4Define.FALSE);
				jsonObject.put(I4Define.MESSAGE, "idfa参数为空！");
			}
		} catch (Exception e) {
			jsonObject.put(I4Define.SUCCESS, I4Define.FALSE);
			jsonObject.put(I4Define.MESSAGE, e.toString());
		}
		LogUtil.endLog(THIS_CLASS, I4Define.CHECK_REQUEST_ACTION);
		return jsonObject;
	}
	
	/**
	 * 安装通知
	 * @param request
	 * @param paramBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = I4Define.INSTALL_NOTICE_ACTION)
	public JSONObject installNotice(HttpServletRequest request, I4Bean paramBean){
		LogUtil.startLog(THIS_CLASS, I4Define.INSTALL_NOTICE_ACTION);
		if(paramBean!=null){
			JSONObject result = i4Service.insertInstallNotice(paramBean);
			LogUtil.endLog(THIS_CLASS, I4Define.INSTALL_NOTICE_ACTION);
			return result;
		}else{
			JSONObject jo = new JSONObject();
			jo.put(I4Define.SUCCESS, I4Define.FALSE);
			jo.put(I4Define.MESSAGE, "参数为空！");
			LogUtil.endLog(THIS_CLASS, I4Define.INSTALL_NOTICE_ACTION);
			return jo;
		}
	}
	
	
	
	
	
	
}





