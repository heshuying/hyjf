package com.hyjf.admin.sql.withdraw;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.mybatis.model.customize.SqlWithdrawCustomize;

@Controller
@RequestMapping(SqlWithdrawDefine.REQUEST_MAPPING)
public class SqlWithdrawController {
	@Autowired
	private SqlWithdrawService sqlWithdrawService;
	
	@RequestMapping(SqlWithdrawDefine.LIST)
	public ModelAndView selectWithdraw(HttpServletRequest request, HttpServletResponse response,int date){
		ModelAndView modelAndView = new ModelAndView(SqlWithdrawDefine.LIST_PATH);
		
		List<SqlWithdrawCustomize> list = sqlWithdrawService.selectWithdrawByAddtime(date);
		modelAndView.addObject("lists",list);
		
		return modelAndView;
		
	}
	
	
}
