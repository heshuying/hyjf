package com.hyjf.admin.app.maintenance.banner;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.maintenance.config.ConfigController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.mybatis.model.auto.AdsType;
import com.hyjf.mybatis.model.auto.AdsWithBLOBs;

/**
 * 移动端广告管理
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = AppBannerDefine.REQUEST_MAPPING)
public class AppBannerController extends BaseController {

	@Autowired
	private AppBannerService appBannerService;
	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppBannerDefine.INIT)
	@RequiresPermissions(AppBannerDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(AppBannerDefine.ADS_FORM) AppBannerBean form) {
		LogUtil.startLog(AppBannerController.class.toString(), AppBannerDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AppBannerDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AppBannerController.class.toString(), AppBannerDefine.INIT);
		return modelAndView;
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppBannerDefine.SEARCH_ACTION)
	@RequiresPermissions(AppBannerDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(AppBannerDefine.ADS_FORM) AppBannerBean form) {
		LogUtil.startLog(AppBannerController.class.toString(), AppBannerDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppBannerDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AppBannerController.class.toString(), AppBannerDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, AppBannerBean form) {
		
		int count = this.appBannerService.countRecordList(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			List<Ads> recordList = this.appBannerService.getRecordList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(AppBannerDefine.ADS_FORM, form);
		}
		// 获取广告类型列表
		List<AdsType> adsTypeList = this.appBannerService.getAdsTypeList();
		List<AdsType> returnNewList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(adsTypeList)){
			if(form.getPlatformType().intValue()==1){
				for(AdsType type:adsTypeList){
					if(AdsTypeEnum.startpage.toString().equals(type.getCode())) {
						returnNewList.add(type);
					}/*else if(AdsTypeEnum.popup.toString().equals(type.getCode())){
						returnNewList.add(type);
					}*/else if(AdsTypeEnum.android_banner.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.android_regist_888.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.android_open_888.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.android_module1.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.android_module2.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.android_module3.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.android_module4.toString().equals(type.getCode())){
						returnNewList.add(type);
					}
				}
			}else if(form.getPlatformType().intValue()==2){
				for(AdsType type:adsTypeList){
					if(AdsTypeEnum.startpage.toString().equals(type.getCode())) {
						returnNewList.add(type);
					}/*else if(AdsTypeEnum.popup.toString().equals(type.getCode())){
						returnNewList.add(type);
					}*/else if(AdsTypeEnum.ios_banner.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.ios_regist_888.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.ios_open_888.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.ios_module1.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.ios_module2.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.ios_module3.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.ios_module4.toString().equals(type.getCode())){
						returnNewList.add(type);
					}
				}
			}else if(form.getPlatformType().intValue()==3){
				for(AdsType type:adsTypeList){
					if(AdsTypeEnum.startpage.toString().equals(type.getCode())) {
						returnNewList.add(type);
					}else if(AdsTypeEnum.weixin.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.wechat_module1.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.wechat_module2.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.wechat_module3.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.wechat_module4.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.wechat_regist_888.toString().equals(type.getCode())){
						returnNewList.add(type);
					}else if(AdsTypeEnum.wechat_banner.toString().equals(type.getCode())){
						returnNewList.add(type);
					}
				}
			}
		}
		modelAndView.addObject("adsTypeList", returnNewList);
		// 文件根目录
		String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
		modelAndView.addObject("fileDomainUrl", fileDomainUrl);
		
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppBannerDefine.INFO_ACTION)
	@RequiresPermissions(value = { AppBannerDefine.PERMISSIONS_INFO, AppBannerDefine.PERMISSIONS_ADD,AppBannerDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView infoAction(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(AppBannerDefine.ADS_FORM) AppBannerBean form) {
		LogUtil.startLog(AppBannerController.class.toString(), AppBannerDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AppBannerDefine.INFO_PATH);
		try {
			if (StringUtils.isNotEmpty(form.getIds())) {
				Integer id = Integer.valueOf(form.getIds());
				Ads record = this.appBannerService.getRecord(id);
				BeanUtils.copyProperties(record, form);
				String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
				modelAndView.addObject("fileDomainUrl", fileDomainUrl);
			} 
			List<AdsType> adsTypeList = this.appBannerService.getAdsTypeList();
            List<AdsType> returnNewList = new ArrayList<>();
            if(!CollectionUtils.isEmpty(adsTypeList)){
                if(form.getPlatformType().intValue()==1){
                    for(AdsType type:adsTypeList){
                        if(AdsTypeEnum.startpage.toString().equals(type.getCode())) {
                            returnNewList.add(type);
                        }/*else if(AdsTypeEnum.popup.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }*/else if(AdsTypeEnum.android_banner.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.android_regist_888.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.android_open_888.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.android_module1.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.android_module2.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.android_module3.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.android_module4.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }
                    }
                }else if(form.getPlatformType().intValue()==2){
                    for(AdsType type:adsTypeList){
                        if(AdsTypeEnum.startpage.toString().equals(type.getCode())) {
                            returnNewList.add(type);
                        }/*else if(AdsTypeEnum.popup.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }*/else if(AdsTypeEnum.ios_banner.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.ios_regist_888.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.ios_open_888.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.ios_module1.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.ios_module2.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.ios_module3.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.ios_module4.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }
                    }
                }else if(form.getPlatformType().intValue()==3){
                    for(AdsType type:adsTypeList){
                        if(AdsTypeEnum.startpage.toString().equals(type.getCode())) {
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.weixin.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.wechat_module1.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.wechat_module2.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.wechat_module3.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.wechat_module4.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.wechat_regist_888.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }else if(AdsTypeEnum.wechat_banner.toString().equals(type.getCode())){
                            returnNewList.add(type);
                        }
                    }
                }
            }
			modelAndView.addObject("adsTypeList", returnNewList);
			modelAndView.addObject(AppBannerDefine.ADS_FORM, form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.endLog(AppBannerController.class.toString(), AppBannerDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AppBannerDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(AppBannerDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request, AdsWithBLOBs form) {
		LogUtil.startLog(AppBannerController.class.toString(), AppBannerDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppBannerDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 数据插入
		this.appBannerService.insertRecord(form);
		modelAndView.addObject(AppBannerDefine.SUCCESS, AppBannerDefine.SUCCESS);
		LogUtil.endLog(AppBannerController.class.toString(), AppBannerDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AppBannerDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(AppBannerDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, AdsWithBLOBs form) {
		LogUtil.startLog(AppBannerController.class.toString(), AppBannerDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppBannerDefine.INFO_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		// 更新
		this.appBannerService.updateRecord(form);
		modelAndView.addObject(AppBannerDefine.SUCCESS, AppBannerDefine.SUCCESS);
		LogUtil.endLog(AppBannerController.class.toString(), AppBannerDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 修改状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppBannerDefine.STATUS_ACTION)
	@RequiresPermissions(AppBannerDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateStatus(HttpServletRequest request,RedirectAttributes attr,AppBannerBean form ) {
		LogUtil.startLog(AppBannerController.class.toString(), AppBannerDefine.STATUS_ACTION);

		ModelAndView modelAndView = new ModelAndView(AppBannerDefine.RE_LIST_PATH);
		// 修改状态
		if (StringUtils.isNotEmpty(form.getIds())) {
			Integer id = Integer.valueOf(form.getIds());
			AdsWithBLOBs record = this.appBannerService.getRecord(id);
			if (record.getStatus() == 1) {
				record.setStatus((short) 0);
			} else {
				record.setStatus((short) 1);
			}
			this.appBannerService.updateRecord(record);
		}
		attr.addFlashAttribute(AppBannerDefine.ADS_FORM, form);
		LogUtil.endLog(AppBannerController.class.toString(), AppBannerDefine.STATUS_ACTION);
		return modelAndView;
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppBannerDefine.DELETE_ACTION)
	@RequiresPermissions(AppBannerDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids,String platformType) {
		LogUtil.startLog(ConfigController.class.toString(), AppBannerDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView("redirect:/app/maintenance/banner/init?platformType="+platformType);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.appBannerService.deleteRecord(recordList);
		LogUtil.endLog(AppBannerController.class.toString(), AppBannerDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, AdsWithBLOBs form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "name", form.getName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "code", form.getCode())) {
			form.setCode("");
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getName(), 50, true)) {
			return modelAndView;
		}
		return null;
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = AppBannerDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { AppBannerDefine.PERMISSIONS_ADD,AppBannerDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(AppBannerController.class.toString(), AppBannerDefine.UPLOAD_FILE);
		String files = borrowCommonService.uploadFile(request, response);
		LogUtil.endLog(AppBannerController.class.toString(), AppBannerDefine.UPLOAD_FILE);
		return files;
	}

}
