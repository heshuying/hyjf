package com.hyjf.admin.sql.borrowtender;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.mybatis.model.customize.SqlBorrowTenderCustomize;

@Controller
@RequestMapping(BorrowTenderDefine.REQUEST_MAPPING)
public class BorrowTenderController {
	@Autowired
	private BorrowTenderService borrowTenderService;
	
	@RequestMapping(BorrowTenderDefine.LIST)
	public ModelAndView selectBorrowTender(HttpServletRequest request, HttpServletResponse response,int date){
		ModelAndView modelAndView = new ModelAndView(BorrowTenderDefine.LIST_PATH);
		
		List<SqlBorrowTenderCustomize> list = borrowTenderService.selectBorrowTenderByAddtime(date);
		modelAndView.addObject("lists", list);
		
		return modelAndView;
		
	}

	
	
}
