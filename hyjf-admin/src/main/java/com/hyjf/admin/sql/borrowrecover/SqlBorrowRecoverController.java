package com.hyjf.admin.sql.borrowrecover;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.mybatis.model.customize.SqlBorrowRecoverCustomize;

@Controller
@RequestMapping(SqlBorrowRecoverDefine.REQUEST_MAPPING)
public class SqlBorrowRecoverController {
	@Autowired
	private SqlBorrowRecoverService sqlBorrowRecoverService;
	
	@RequestMapping(SqlBorrowRecoverDefine.LIST)
	public ModelAndView selectBorrowTender(HttpServletRequest request, HttpServletResponse response,int date){
		ModelAndView modelAndView = new ModelAndView(SqlBorrowRecoverDefine.LIST_PATH);
		
		List<SqlBorrowRecoverCustomize> list = sqlBorrowRecoverService.selectBorrowRecoverByRecovertime(date);
		modelAndView.addObject("lists", list);
		
		return modelAndView;
		
	}

	
	
}
