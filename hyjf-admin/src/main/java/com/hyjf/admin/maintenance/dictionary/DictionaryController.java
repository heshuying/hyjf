package com.hyjf.admin.maintenance.dictionary;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;

@Controller
@RequestMapping("/dictionary")
public class DictionaryController extends BaseController {

	@RequestMapping("/init")
	public ModelAndView init() {
		ModelAndView modelAndView = new ModelAndView("admin/maintenance/dictionary/dictionary");

		return modelAndView;
	}
}
