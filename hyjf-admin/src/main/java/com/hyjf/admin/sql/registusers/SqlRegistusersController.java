package com.hyjf.admin.sql.registusers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.mybatis.model.customize.SqlRegistUsersCustomize;

@Controller
@RequestMapping(SqlRegistusersDefine.REQUEST_MAPPING)
public class SqlRegistusersController {
	@Autowired
	private SqlRegistusersService sqlRegistusersService;
	
	@RequestMapping(SqlRegistusersDefine.LIST)
	public ModelAndView selectBorrowTender(HttpServletRequest request, HttpServletResponse response,int date){
		ModelAndView modelAndView = new ModelAndView(SqlRegistusersDefine.LIST_PATH);
		
		List<SqlRegistUsersCustomize> list = sqlRegistusersService.selectRegistUsersByRegtime(date);
		modelAndView.addObject("lists", list);
		
		return modelAndView;
		
	}

	
	
}
