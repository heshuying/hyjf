package com.hyjf.api.web.idfa;

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
 * @author 朱晓东
 * 
 */
@Controller
@RequestMapping(value = IdfaDefine.IDFA_QUEST_MAPPING_CLASS)
public class IdfaServer extends BaseController {

	private static final String THIS_CLASS = IdfaServer.class.getName();

	@Autowired
	IdfaService idfaService;

	/**
	 * 检验IDFA
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = IdfaDefine.CHECK_REQUEST_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public JSONObject userRegist(HttpServletRequest request, IdfaBean paramBean) {
		LogUtil.startLog(THIS_CLASS, IdfaDefine.CHECK_REQUEST_ACTION);
		LogUtil.infoLog(THIS_CLASS, IdfaDefine.CHECK_REQUEST_ACTION, JSONObject.toJSONString(paramBean));
		JSONObject jsonObject = new JSONObject();
		try {
			if(paramBean.getIdfa()!=null && !"".equals(paramBean.getIdfa())){
			    String idfaArry[] = paramBean.getIdfa().split(",");
			    if(idfaArry!=null && idfaArry.length>0){
			        List<Idfa> idfaList = idfaService.selectIdfaByArry(java.util.Arrays.asList(idfaArry));
			        for(String idfaStr : idfaArry){
			            jsonObject.put(idfaStr, 0);
			            for(Idfa idfa: idfaList){
			                if(idfa.getIdfa().equals(idfaStr)){
			                    jsonObject.put(idfaStr, 1);
			                }
			            }
			        }
			    }else{
			        jsonObject.put("error","idfa is null");
			    }
			}else{
			    jsonObject.put("error","idfa is null");
			}
		} catch (Exception e) {
			jsonObject.put("error",e.toString());
		}
		LogUtil.endLog(THIS_CLASS, IdfaDefine.CHECK_REQUEST_ACTION);
		return jsonObject;
	}
}
