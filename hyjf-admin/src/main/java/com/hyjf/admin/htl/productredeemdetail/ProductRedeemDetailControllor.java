package com.hyjf.admin.htl.productredeemdetail;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.htl.productredeem.ProductRedeemDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.customize.ProductRedeemDetailCustomize;
/**
 * @package com.hyjf.admin.htl.productredeemdetail
 * @author Michael
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ProductRedeemDetailDefine.REQUEST_MAPPING)
public class ProductRedeemDetailControllor extends BaseController {

	private static final String THIS_CLASS = ProductRedeemDetailControllor.class.getName();
	@Autowired
	private ProductRedeemDetailService  productRedeemDetailService;

	/**
	 * 汇天利转出画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ProductRedeemDetailDefine.INIT)
    @RequiresPermissions(ProductRedeemDefine.PRODUCTREDEEMCUSTOMIZE_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(ProductRedeemDetailDefine.PRODUCTREDEEMDETAIL_FORM) ProductRedeemDetailBean form) {
		LogUtil.startLog(THIS_CLASS, ProductRedeemDetailDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ProductRedeemDetailDefine.LIST_PATH);
		String listId = request.getParameter("listId");
		if(StringUtils.isNotEmpty(listId)){
			form.setListIdSrh(listId);
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, ProductRedeemDetailDefine.INIT);
		return modelAndView;
	}

	/**
	 * 汇天利转出详细画面查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ProductRedeemDetailDefine.SEARCH_ACTION)
    @RequiresPermissions(ProductRedeemDefine.PRODUCTREDEEMCUSTOMIZE_SEARCH)
	public ModelAndView search(HttpServletRequest request, ProductRedeemDetailBean form) {
		LogUtil.startLog(THIS_CLASS, ProductRedeemDetailDefine.PRODUCTREDEEMDETAIL_SEARCH);
		ModelAndView modelAndView = new ModelAndView(ProductRedeemDetailDefine.LIST_PATH);
		String listId = request.getParameter("listId");
		if(StringUtils.isNotEmpty(listId)){
			form.setListIdSrh(listId);
		}
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, ProductRedeemDetailDefine.PRODUCTREDEEMDETAIL_SEARCH);
		return modelAndView;
	}

	
	/**
	 * 汇天利转出列表分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ProductRedeemDetailBean form) {
		ProductRedeemDetailCustomize productRedeemDetailCustomize = new ProductRedeemDetailCustomize();
		String listId = request.getParameter("listId");
		if(StringUtils.isNotEmpty(form.getListIdSrh())){
			productRedeemDetailCustomize.setListId(listId);
		}
		if(StringUtils.isNotEmpty(form.getUsernameSrh())){
			productRedeemDetailCustomize.setUsername(form.getUsernameSrh());
		}
		if(StringUtils.isNotEmpty(form.getStatusSrh())){
			productRedeemDetailCustomize.setStatus(Integer.parseInt(form.getStatusSrh()));
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productRedeemDetailCustomize.setTimeStartSrch(GetDate.get10Time(form.getTimeStartSrch()));
		}
		if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
			productRedeemDetailCustomize.setTimeEndSrch(GetDate.get10Time(form.getTimeEndSrch()));
		}
		Integer count = this.productRedeemDetailService.countRedeemDetailRecord(productRedeemDetailCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			productRedeemDetailCustomize.setLimitStart(paginator.getOffset());
			productRedeemDetailCustomize.setLimitEnd(paginator.getLimit());
			List<ProductRedeemDetailCustomize> recordList  = this.productRedeemDetailService.getRecordList(productRedeemDetailCustomize);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(ProductRedeemDetailDefine.PRODUCTREDEEMDETAIL_FORM, form);
		
	}
	
}
