package com.hyjf.admin.manager.content.help.category;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Category;
import com.hyjf.mybatis.model.auto.CategoryExample;
import com.hyjf.mybatis.model.auto.ContentHelp;
import com.hyjf.mybatis.model.auto.ContentHelpExample;
import com.hyjf.mybatis.model.customize.ContentHelpCustomize;

/**
 * @package com.hyjf.admin.finance.returncash
 * @author GOGTZ-T
 * @date 2015/11/29 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = CategoryDefine.HELP)
public class CategoryController extends BaseController {
	/**
	 * 类名
	 */
	private static final String THIS_CLASS = CategoryController.class.getName();

	@Autowired
	private CategoryService cateService;

	/**
	 * 问题分类画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CategoryDefine.INIT)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(CategoryDefine.QUERY_FORM) CategoryBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.CATEGORY_LIST_VIEW);

		// 创建分页
		this.createCategoryPage(request, modelAndView, form);

		LogUtil.endLog(THIS_CLASS, CategoryDefine.INIT);
		return modelAndView;
	}

	/**
	 * 分类分页列表
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	@SuppressWarnings("unchecked")
	private void createCategoryPage(HttpServletRequest request, ModelAndView modeAndView, CategoryBean form) {
		{
			// 第一步:获取主分类和子分类
			searchParentCategorys(request, modeAndView, form, true);
			searchChildCategorys(request, modeAndView, form, true);
		}
		{
			// 第二步:获取列表
			// 拼装查询条件
			CategoryExample exam = new CategoryExample();
			CategoryExample.Criteria crit = exam.createCriteria();
			if (form.getPid() != null) {
				if (form.getId() != null) {
					// 当子ID不为空时,表示是父子联查
					crit.andPidEqualTo(form.getPid());
				} else {
					// 当子ID为空时,表示查父和所有的子
					// 获取子
					List<Category> childCategorys = (List<Category>) modeAndView.getModel().get("childCategorys");
					List<Integer> ids = new ArrayList<Integer>();
					ids.add(form.getPid());
					if (childCategorys != null && childCategorys.size() > 0) {
						for (Category cat : childCategorys) {
							ids.add(cat.getId());
						}
					}
					crit.andIdIn(ids);
				}
			}
			if (form.getId() != null) {
				crit.andIdEqualTo(form.getId());
			}

			if (form.getHide() != null) {
				crit.andHideEqualTo(form.getHide());
			}
			{
				crit.andGroupEqualTo(CategoryDefine.GROUP);
			}
			Integer count = this.cateService.getCategoryCount(exam);
			if (count >= 0) {
				Paginator paginator = new Paginator(form.getPaginatorPage(), count);
				exam.setLimitStart(paginator.getOffset());
				exam.setLimitEnd(paginator.getLimit());
				List<Category> cates = this.cateService.getCategory(exam);
				for (Category ca : cates) {
					if (ca.getLevel() == 0) {
						// 一级菜单,Pid等于ca.id即满足条件
						ContentHelpExample con = new ContentHelpExample();
						ContentHelpExample.Criteria conCriteria = con.createCriteria();
						conCriteria.andPcateIdEqualTo(ca.getId());
						ca.setTip(cateService.getConNum(con) + "");// 废字段用来存储问题数量
					} else if (ca.getLevel() == 1) {
						// 二级菜单
						ContentHelpExample con = new ContentHelpExample();
						ContentHelpExample.Criteria conCriteria = con.createCriteria();
						conCriteria.andPcateIdEqualTo(ca.getPid());
						conCriteria.andCateIdEqualTo(ca.getId());
						ca.setTip(cateService.getConNum(con) + "");// 废字段用来存储问题数量
					}
				}
				form.setPaginator(paginator);
				form.setRecordList(cates);
				modeAndView.addObject(CategoryDefine.QUERY_FORM, form);
			}
		}
	}

	/**
	 * 获取主分类
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void searchParentCategorys(HttpServletRequest request, ModelAndView modeAndView, CategoryBean form,
			Boolean showHide) {
		CategoryExample example = new CategoryExample();
		CategoryExample.Criteria criteria = example.createCriteria();
		criteria.andLevelEqualTo(0);
		criteria.andGroupEqualTo(CategoryDefine.GROUP);
		if (!showHide) {
			criteria.andHideEqualTo(0);
		}
		List<Category> parentCategorys = cateService.getCategory(example);
		modeAndView.addObject("parentCategorys", parentCategorys);
	}

	/**
	 * 根据主分类ID获取子分类
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void searchChildCategorys(HttpServletRequest request, ModelAndView modeAndView, CategoryBean form,
			Boolean showHide) {
		CategoryExample example = new CategoryExample();
		CategoryExample.Criteria criteria = example.createCriteria();
		if (form.getPid() != null) {
			criteria.andPidEqualTo(form.getPid());
			criteria.andLevelEqualTo(1);
			criteria.andGroupEqualTo(CategoryDefine.GROUP);
			if (!showHide) {
				criteria.andHideEqualTo(0);
			}
			List<Category> childCategorys = cateService.getCategory(example);
			modeAndView.addObject("childCategorys", childCategorys);
		}
	}

	/**
	 * 二级菜单联动
	 * 
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = CategoryDefine.CHANGE_SUBTYPE_ACTION)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_VIEW)
	public Map<String, Object> changeSubTypeAction(HttpServletRequest request, ModelAndView modeAndView,
			CategoryBean form) throws ParseException {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.CHANGE_SUBTYPE_ACTION);
		modeAndView.addObject("success", true);
		searchChildCategorys(request, modeAndView, form, true);
		LogUtil.endLog(THIS_CLASS, CategoryDefine.CHANGE_SUBTYPE_ACTION);
		return modeAndView.getModel();
	}

	/**
	 * 二级菜单联动2,只显示可用的二级菜单
	 * 
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = CategoryDefine.CHANGE_SUBTYPE_ACTION2)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_VIEW)
	public Map<String, Object> changeSubTypeAction2(HttpServletRequest request, ModelAndView modeAndView,
			CategoryBean form) throws ParseException {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.CHANGE_SUBTYPE_ACTION2);
		modeAndView.addObject("success", true);
		searchChildCategorys(request, modeAndView, form, false);
		LogUtil.endLog(THIS_CLASS, CategoryDefine.CHANGE_SUBTYPE_ACTION2);
		return modeAndView.getModel();
	}

	/**
	 * 列表页跳转详情页(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CategoryDefine.INFOTYPE_ACTION)

	@RequiresPermissions(value = { CategoryDefine.PERMISSIONS_INFO, CategoryDefine.PERMISSIONS_ADD,
			CategoryDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView infoTypeAction(HttpServletRequest request, CategoryBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.INFOTYPE_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.INFO_PATH);
		modelAndView.addObject("categoryLevel", 1);// 一级菜单
		if (form.getId() != null) {
			Integer id = Integer.valueOf(form.getId());
			Category record = this.cateService.queryById(id);
			modelAndView.addObject(CategoryDefine.INFO_FORM, record);
		}
		LogUtil.endLog(THIS_CLASS, CategoryDefine.INFOTYPE_ACTION);
		return modelAndView;
	}

	/**
	 * 列表页跳转详情页(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CategoryDefine.INFOSUBTYPE_ACTION)

	@RequiresPermissions(value = { CategoryDefine.PERMISSIONS_INFO, CategoryDefine.PERMISSIONS_ADD,
			CategoryDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView infoSubTypeAction(HttpServletRequest request, CategoryBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.INFOSUBTYPE_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.INFO_PATH);
		modelAndView.addObject("categoryLevel", 2);// 二级菜单
		searchParentCategorys(request, modelAndView, form, false);// 二级菜单需要查询一级菜单列表
		if (form.getId() != null) {
			Integer id = Integer.valueOf(form.getId());
			Category record = this.cateService.queryById(id);
			modelAndView.addObject(CategoryDefine.INFO_FORM, record);
		}
		LogUtil.endLog(THIS_CLASS, CategoryDefine.INFOSUBTYPE_ACTION);
		return modelAndView;
	}

	/**
	 * 添加分类
	 */
	@RequestMapping(value = CategoryDefine.ADD_ACTION)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_ADD)
	public ModelAndView addCate(HttpServletRequest request, CategoryBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.ADD_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.INFO_PATH);// 重定向回列表页面
		Category cate = new Category();
		BeanUtils.copyProperties(form, cate);
		cate.setGroup(CategoryDefine.GROUP);
		cateService.addCate(cate);
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		createCategoryPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, CategoryDefine.ADD_ACTION);
		return modelAndView;
	}

	/**
	 * 修改分类
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = CategoryDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, CategoryBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.INFO_PATH);
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		Category cate = new Category();
		BeanUtils.copyProperties(form, cate);
		cate.setGroup(CategoryDefine.GROUP);
		// 更新
		this.cateService.updateCate(cate);
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		createCategoryPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, CategoryDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 关闭模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = CategoryDefine.CLOSE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_MODIFY)
	public ModelAndView closeAction(HttpServletRequest request,CategoryBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.CLOSE_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.CATEGORY_LIST_VIEW);
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getIds().toString())) {
			return modelAndView;
		}
		if( StringUtils.isNotEmpty(form.getIds())){
			//查找数据
			Category cate = this.cateService.queryById(Integer.valueOf(form.getIds()));
			if(cate != null){
				cate.setHide(1);
				// 更新
				this.cateService.updateCate(cate);
			}
		}
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		createCategoryPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, CategoryDefine.CLOSE_ACTION);
		return modelAndView;
	}

	/**
	 * 启用模板
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = CategoryDefine.OPEN_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_MODIFY)
	public ModelAndView openAction(HttpServletRequest request, CategoryBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.OPEN_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.CATEGORY_LIST_VIEW);

		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getIds().toString())) {
			return modelAndView;
		}
		if( StringUtils.isNotEmpty(form.getIds())){
			//查找数据
			Category cate = this.cateService.queryById(Integer.valueOf(form.getIds()));
			if(cate != null){
				cate.setHide(0);
				// 更新
				this.cateService.updateCate(cate);
			}
		}
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		createCategoryPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, CategoryDefine.OPEN_ACTION);
		return modelAndView;
	}

	/**
	 * 删除前的验证页面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(CategoryDefine.BEFORE_DEL_INFO_ACTION)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_VIEW)
	public Map<String, Object> beforeDelInfoAction(HttpServletRequest request, CategoryBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.DEL_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.DEL_INFO_PATH);
		if (form.getId() == null) {
			modelAndView.addObject("success", false);
			modelAndView.addObject("msg", "删除ID不可为空");
			return modelAndView.getModel();
		}
		Integer id = form.getId();
		// 分类信息
		Category record = this.cateService.queryById(id);
		// 问题数量，暂存

		if (record.getLevel() == 0) {
			// 一级菜单,必须在没有子菜单的情况下才可以删除
			CategoryExample example = new CategoryExample();
			CategoryExample.Criteria criteria = example.createCriteria();
			criteria.andPidEqualTo(record.getId());
			criteria.andLevelEqualTo(1);
			int childSize = cateService.getCategoryCount(example);
			if (childSize != 0) {
				modelAndView.addObject("success", false);
				modelAndView.addObject("msg", "该主分类下还有未删除的子分类,请先删除子分类");
				return modelAndView.getModel();
			} else {
				modelAndView.addObject("success", true);
				return modelAndView.getModel();
			}
		} else if (record.getLevel() == 1) {
			// 二级菜单没有验证
			modelAndView.addObject("success", true);
			return modelAndView.getModel();
		}
		LogUtil.endLog(THIS_CLASS, CategoryDefine.DEL_INFO_ACTION);
		return modelAndView.getModel();
	}

	/**
	 * 删除分类页面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CategoryDefine.DEL_INFO_ACTION)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_DELETE)
	public ModelAndView delInfo(HttpServletRequest request, CategoryBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.DEL_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.DEL_INFO_PATH);

		Integer id = Integer.valueOf(form.getId());
		// 分类信息
		Category record = this.cateService.queryById(id);
		// 问题数量，暂存

		if (record.getLevel() == 0) {
			// 一级菜单,Pid等于ca.id即满足条件
			ContentHelpExample con = new ContentHelpExample();
			ContentHelpExample.Criteria conCriteria = con.createCriteria();
			conCriteria.andPcateIdEqualTo(record.getId());
			record.setTip(cateService.getConNum(con) + "");// 废字段用来存储问题数量
		} else if (record.getLevel() == 1) {
			// 二级菜单
			ContentHelpExample con = new ContentHelpExample();
			ContentHelpExample.Criteria conCriteria = con.createCriteria();
			conCriteria.andPcateIdEqualTo(record.getPid());
			conCriteria.andCateIdEqualTo(record.getId());
			record.setTip(cateService.getConNum(con) + "");// 废字段用来存储问题数量
		}
		modelAndView.addObject(CategoryDefine.INFO_FORM, record);
		// 问题分类列表
		{
			// 一级分类
			searchParentCategorys(request, modelAndView, form, false);
		}
		LogUtil.endLog(THIS_CLASS, CategoryDefine.DEL_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 删除分类
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CategoryDefine.DEL_ACTION)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_DELETE)
	public ModelAndView del(HttpServletRequest request, CategoryBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.DEL_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.DEL_INFO_PATH);

		Integer id = Integer.valueOf(form.getId());

		if (id != null) {
			// category
			Category category = cateService.queryById(id);
			ContentHelpExample example = new ContentHelpExample();
			ContentHelpExample.Criteria criteria = example.createCriteria();
//			criteria.andPcateIdEqualTo(form.getPid());
			if (category.getPid() == 0) {
				// 如果PID=0表示是一级菜单
				criteria.andPcateIdEqualTo(category.getId());
			} else {
				criteria.andPcateIdEqualTo(category.getPid());
				criteria.andCateIdEqualTo(category.getId());
			}
			List<ContentHelp> helpList = cateService.getCon(example);

			if (form.getDelType() == 0) {
				// 分类和问题全删除
				for (ContentHelp help : helpList) {
					cateService.delCon(help.getId());
				}
				cateService.delCate(category.getId());
			} else if (form.getDelType() == 1) {
				// 删除分类,问题转移
				for (ContentHelp help : helpList) {
					help.setPcateId(form.getNewpid());
					help.setCateId(form.getNewid());
					cateService.updateCon(help);
				}
				cateService.delCate(category.getId());
			}
		}
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		modelAndView.addObject(CategoryDefine.INFO_FORM, form);
		createCategoryPage(request, modelAndView, new CategoryBean());
		LogUtil.endLog(THIS_CLASS, CategoryDefine.DEL_ACTION);
		return modelAndView;
	}

	// 以下是问题页面数据

	/**
	 * 问题分页
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createHelpPage(HttpServletRequest request, ModelAndView modeAndView, ContentHelpBean form) {
		{
			CategoryBean bean = new CategoryBean();
			bean.setPid(form.getPcateId());
			// 第一步:获取主分类和子分类
			searchParentCategorys(request, modeAndView, bean, true);
			searchChildCategorys(request, modeAndView, bean, true);
		}
		ContentHelpExample con = new ContentHelpExample();
		ContentHelpExample.Criteria conCriteria = con.createCriteria();
		if (form.getPcateId() != null) {
			conCriteria.andPcateIdEqualTo(form.getPcateId());
		}
		if (form.getCateId() != null) {
			conCriteria.andCateIdEqualTo(form.getCateId());
		}
		if (StringUtils.isNotEmpty(form.getTitle())) {
			conCriteria.andTitleLike(form.getTitle());
		}

		if (form.getStatus() != null) {
			conCriteria.andStatusEqualTo(form.getStatus());
		}

		if (StringUtils.isNotEmpty(form.getPost_time_begin())) {
			int begin = Integer.parseInt(GetDate.get10Time(form.getPost_time_begin()));
			conCriteria.andCreateTimeGreaterThanOrEqualTo(begin);
		}

		if (StringUtils.isNotEmpty(form.getPost_time_end())) {
			int end = Integer.parseInt(GetDate.get10Time(form.getPost_time_end()));
			conCriteria.andCreateTimeLessThanOrEqualTo(end);
		}
		Integer count = this.cateService.getConNum(con);
		if (count >= 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			con.setLimitStart(paginator.getOffset());
			con.setLimitEnd(paginator.getLimit());
			List<ContentHelp> conhelp = this.cateService.getCon(con);
			List<Integer> ids = cateService.getConType();
			List<ContentHelpCustomize> resultHelpList = new ArrayList<ContentHelpCustomize>();
			for (ContentHelp help : conhelp) {
				ContentHelpCustomize cc = new ContentHelpCustomize();
				BeanUtils.copyProperties(help, cc);
				if(ids.contains(help.getPcateId())){
					cc.setIsZhiChi("true");
				}
				cc.setAdd_time(GetDate.getDateTimeMyTimeInMillis(help.getCreateTime()));
				resultHelpList.add(cc);
			}
			form.setPaginator(paginator);

			// 查找开启状态的分类
			CategoryExample cate = new CategoryExample();
			// cate.createCriteria().andHideEqualTo(1);
			List<Category> cates = this.cateService.getCategory(cate);

			modeAndView.addObject(CategoryDefine.CATE_LIST, cates);
			modeAndView.addObject(CategoryDefine.CONTENT_HELP_LIST, resultHelpList);
			modeAndView.addObject(CategoryDefine.HELP_FORM, form);
		}
	}

	/**
	 * 问题列表页面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CategoryDefine.HELP_INIT)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_VIEW)
	public ModelAndView helpInit(HttpServletRequest request, ContentHelpBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.HELP_INIT);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.HELP_LIST_VIEW);
		// 创建分页
		this.createHelpPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, CategoryDefine.HELP_INIT);
		return modelAndView;
	}

	/**
	 * 列表页跳转详情页(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CategoryDefine.HELP_INFO_ACTION)

	@RequiresPermissions(value = { CategoryDefine.PERMISSIONS_INFO, CategoryDefine.PERMISSIONS_ADD,
			CategoryDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView helpInfo(HttpServletRequest request, ContentHelpBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.HELP_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.HELP_INFO_PATH);

		{
			CategoryBean bean = new CategoryBean();
			bean.setPid(form.getPcateId());
			// 第一步:获取主分类
			searchParentCategorys(request, modelAndView, new CategoryBean(), false);
		}
		if (form.getId() != null) {
			Integer id = Integer.valueOf(form.getId());
			ContentHelp record = this.cateService.queryContentById(id);
			{
				CategoryBean bean = new CategoryBean();
				bean.setPid(record.getPcateId());
				// 第一步:获取子分类
				searchChildCategorys(request, modelAndView, bean, true);
			}
			modelAndView.addObject(CategoryDefine.HELP_INFO_FORM, record);
		}
		LogUtil.endLog(THIS_CLASS, CategoryDefine.HELP_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加问题
	 */
	@RequestMapping(value = CategoryDefine.HELP_ADD_ACTION)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_ADD)
	public ModelAndView addHelp(HttpServletRequest request, ContentHelpBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.HELP_ADD_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.HELP_INFO_PATH);

		ContentHelpCustomize con = new ContentHelpCustomize();
		BeanUtils.copyProperties(form, con);
		con.setCreateTime(GetDate.getNowTime10());
		cateService.addCon(con);
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		createHelpPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, CategoryDefine.HELP_ADD_ACTION);
		return modelAndView;
	}

	/**
	 * 修改问题
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = CategoryDefine.HELP_UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateHelpAction(HttpServletRequest request, ContentHelpBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.INFO_PATH);

		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}

		ContentHelpCustomize con = new ContentHelpCustomize();
		BeanUtils.copyProperties(form, con);
		// 更新
		this.cateService.updateCon(con);
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		createHelpPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, CategoryDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 关闭问题
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = CategoryDefine.HELP_CLOSE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_MODIFY)
	public ModelAndView closeHelpAction(HttpServletRequest request, ContentHelpBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.HELP_CLOSE_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.HELP_LIST_VIEW);
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		ContentHelp con = this.cateService.queryContentById(form.getId());
		con.setStatus(0);
		// 更新
		this.cateService.updateCon(con);
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		createHelpPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, CategoryDefine.HELP_CLOSE_ACTION);
		return modelAndView;
	}

	/**
	 * 启用问题
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = CategoryDefine.HELP_OPEN_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_MODIFY)
	public ModelAndView openHelpAction(HttpServletRequest request, ContentHelpBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.HELP_OPEN_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.HELP_LIST_VIEW);

		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		ContentHelp con = this.cateService.queryContentById(form.getId());
		con.setStatus(1);
		// 更新
		this.cateService.updateCon(con);
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		createHelpPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, CategoryDefine.HELP_OPEN_ACTION);
		return modelAndView;
	}

	/**
	 * 删除问题
	 */
	@RequestMapping(value = CategoryDefine.HELP_DEL_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_DELETE)
	public ModelAndView delHelpAction(HttpServletRequest request, ContentHelpBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.HELP_DEL_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.HELP_LIST_VIEW);

		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		// 更新
		this.cateService.delCon(form.getId());
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		createHelpPage(request, modelAndView, new ContentHelpBean());
		LogUtil.endLog(THIS_CLASS, CategoryDefine.HELP_DEL_ACTION);
		return modelAndView;
	}

	/**
	 * 启用问题
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = CategoryDefine.MOVE_OFTEN_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_MODIFY)
	public ModelAndView moveOftenAction(HttpServletRequest request, ContentHelpBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.MOVE_OFTEN_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.HELP_LIST_VIEW);

		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		ContentHelp con = this.cateService.queryContentById(form.getId());
		con.setStatus(2);
		// 更新
		this.cateService.updateCon(con);
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		createHelpPage(request, modelAndView, new ContentHelpBean());
		LogUtil.endLog(THIS_CLASS, CategoryDefine.MOVE_OFTEN_ACTION);
		return modelAndView;
	}
	/**
	 * 启用智齿常见问题
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = CategoryDefine.MOVE_ZHICHI_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_MODIFY)
	public ModelAndView moveZhiChiAction(HttpServletRequest request, ContentHelpBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.MOVE_ZHICHI_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.HELP_LIST_VIEW);

		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		ContentHelp con = this.cateService.queryContentById(form.getId());
		con.setStatus(1);
		con.setZhiChiStatus(1);
		// 更新
		this.cateService.updateCon(con);
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		createHelpPage(request, modelAndView, new ContentHelpBean());
		LogUtil.endLog(THIS_CLASS, CategoryDefine.MOVE_ZHICHI_ACTION);
		return modelAndView;
	}
	// 常见问题列表

	/**
	 * 问题列表页面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CategoryDefine.OFTEN_INIT)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_VIEW)
	public ModelAndView oftenInit(HttpServletRequest request, ContentHelpBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.OFTEN_INIT);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.OFTEN_LIST_VIEW);
		// 创建分页
		this.createoftenPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, CategoryDefine.OFTEN_INIT);
		return modelAndView;
	}
	/**
	 * 智齿客服问题列表页面初始化
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CategoryDefine.ZHICHI_INIT)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_VIEW)
	public ModelAndView zhiChiInit(HttpServletRequest request, ContentHelpBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.ZHICHI_INIT);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.ZHICHI_LIST_VIEW);
		// 创建分页
		this.createZhiChiPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, CategoryDefine.OFTEN_INIT);
		return modelAndView;
	}
	/**
	 * 常见问题分页
	 *
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createZhiChiPage(HttpServletRequest request, ModelAndView modeAndView, ContentHelpBean form) {
		{
			CategoryBean bean = new CategoryBean();
			bean.setPid(form.getPcateId());
			// 第一步:获取主分类和子分类
			searchParentCategorys(request, modeAndView, bean, true);
			searchChildCategorys(request, modeAndView, bean, true);
		}

		ContentHelpExample con = new ContentHelpExample();
		ContentHelpExample.Criteria conCriteria = con.createCriteria();
		if (form.getPcateId() != null) {
			conCriteria.andPcateIdEqualTo(form.getPcateId());
		}
		if (form.getCateId() != null) {
			conCriteria.andCateIdEqualTo(form.getCateId());
		}
		if (StringUtils.isNotEmpty(form.getTitle())) {
			conCriteria.andTitleLike(form.getTitle());
		}
		conCriteria.andZhiChiStatusEqualTo(1);
		conCriteria.andStatusEqualTo(1);// 常见问题状态
		Integer count = this.cateService.getConNum(con);
		if (count >= 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			con.setLimitStart(paginator.getOffset());
			con.setLimitEnd(paginator.getLimit());
			List<ContentHelp> conhelp = this.cateService.getCon(con);
			form.setPaginator(paginator);

			// 查找开启状态的分类
			CategoryExample cate = new CategoryExample();
			cate.createCriteria().andHideEqualTo(0);
			List<Category> cates = this.cateService.getCategory(cate);

			modeAndView.addObject(CategoryDefine.CATE_LIST, cates);
			modeAndView.addObject(CategoryDefine.OFTEN_LIST, conhelp);
			modeAndView.addObject(CategoryDefine.OFTEN_FORM, form);
		}
	}
	/**
	 * 常见问题分页
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createoftenPage(HttpServletRequest request, ModelAndView modeAndView, ContentHelpBean form) {
		{
			CategoryBean bean = new CategoryBean();
			bean.setPid(form.getPcateId());
			// 第一步:获取主分类和子分类
			searchParentCategorys(request, modeAndView, bean, true);
			searchChildCategorys(request, modeAndView, bean, true);
		}

		ContentHelpExample con = new ContentHelpExample();
		ContentHelpExample.Criteria conCriteria = con.createCriteria();
		if (form.getPcateId() != null) {
			conCriteria.andPcateIdEqualTo(form.getPcateId());
		}
		if (form.getCateId() != null) {
			conCriteria.andCateIdEqualTo(form.getCateId());
		}
		if (StringUtils.isNotEmpty(form.getTitle())) {
			conCriteria.andTitleLike(form.getTitle());
		}
		conCriteria.andStatusEqualTo(2);// 常见问题状态
		Integer count = this.cateService.getConNum(con);
		if (count >= 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			con.setLimitStart(paginator.getOffset());
			con.setLimitEnd(paginator.getLimit());
			List<ContentHelp> conhelp = this.cateService.getCon(con);
			form.setPaginator(paginator);

			// 查找开启状态的分类
			CategoryExample cate = new CategoryExample();
			cate.createCriteria().andHideEqualTo(0);
			List<Category> cates = this.cateService.getCategory(cate);

			modeAndView.addObject(CategoryDefine.CATE_LIST, cates);
			modeAndView.addObject(CategoryDefine.OFTEN_LIST, conhelp);
			modeAndView.addObject(CategoryDefine.OFTEN_FORM, form);
		}
	}

	/**
	 * 移除常见列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = CategoryDefine.MOVE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_MODIFY)
	public ModelAndView moveAction(HttpServletRequest request, ContentHelpBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.MOVE_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.OFTEN_LIST_VIEW);

		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		ContentHelp con = this.cateService.queryContentById(form.getId());
		con.setStatus(1);
		// 更新
		this.cateService.updateCon(con);
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		createoftenPage(request, modelAndView, new ContentHelpBean());
		LogUtil.endLog(THIS_CLASS, CategoryDefine.MOVE_ACTION);
		return modelAndView;
	}

	/**
	 * 移除常见列表
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = CategoryDefine.ZHICHI_MOVE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CategoryDefine.PERMISSIONS_MODIFY)
	public ModelAndView zhiChiMoveAction(HttpServletRequest request, ContentHelpBean form) {
		LogUtil.startLog(THIS_CLASS, CategoryDefine.ZHICHI_MOVE_ACTION);
		ModelAndView modelAndView = new ModelAndView(CategoryDefine.ZHICHI_LIST_VIEW);

		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		ContentHelp con = this.cateService.queryContentById(form.getId());
		con.setStatus(1);
		con.setZhiChiStatus(0);
		// 更新
		this.cateService.updateCon(con);
		modelAndView.addObject(CategoryDefine.SUCCESS, CategoryDefine.SUCCESS);
		createZhiChiPage(request, modelAndView, new ContentHelpBean());
		LogUtil.endLog(THIS_CLASS, CategoryDefine.ZHICHI_MOVE_ACTION);
		return modelAndView;
	}
}
