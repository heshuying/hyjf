/**
 * Description:管理
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */
package com.hyjf.admin.app.maintenance.borrowimage;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonImage;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.AppBorrowImage;

@Controller
@RequestMapping(value = AppBorrowImageDefine.REQUEST_MAPPING)
public class AppBorrowImageController extends BaseController {

	@Autowired
	private AppBorrowImageService borrowImageService;

	/**
	 * 维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppBorrowImageDefine.INIT)
	@RequiresPermissions(AppBorrowImageDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppBorrowImageDefine.FORM) AppBorrowImageBean form) {
		LogUtil.startLog(AppBorrowImageController.class.toString(), AppBorrowImageDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(AppBorrowImageDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AppBorrowImageController.class.toString(), AppBorrowImageDefine.INIT);
		return modelAndView;
	}

	/**
	 * 维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppBorrowImageDefine.SEARCH_ACTION)
	@RequiresPermissions(AppBorrowImageDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(AppBorrowImageDefine.FORM) AppBorrowImageBean form) {
		LogUtil.startLog(AppBorrowImageController.class.toString(), AppBorrowImageDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppBorrowImageDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(AppBorrowImageController.class.toString(), AppBorrowImageDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, AppBorrowImageBean form) {
		List<AppBorrowImage> recordList = this.borrowImageService.getRecordList(new AppBorrowImage(), -1, -1);
		if (recordList != null) {
			String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size(), 12);
			recordList = this.borrowImageService.getRecordList(new AppBorrowImage(), paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			for (AppBorrowImage appBorrowImage : recordList) {
				appBorrowImage.setBorrowImageUrl(filePhysicalPath + appBorrowImage.getBorrowImageUrl());
			}
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(AppBorrowImageDefine.FORM, form);
	}

	/**
	 * 详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = AppBorrowImageDefine.INFO_ACTION)
	@RequiresPermissions(value = { AppBorrowImageDefine.PERMISSIONS_ADD, AppBorrowImageDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView moveToInfoAction(HttpServletRequest request, AppBorrowImageBean form) {
		LogUtil.startLog(AppBorrowImageController.class.toString(), AppBorrowImageDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppBorrowImageDefine.INFO_PATH);

		AppBorrowImage record = new AppBorrowImage();
		record.setId(form.getId());

		if (Validator.isNotNull(record.getId())) {
			// 根据主键检索数据
			String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			record = this.borrowImageService.getRecord(form.getId());
			if (record != null) {
				record.setBorrowImageUrl(filePhysicalPath + record.getBorrowImageUrl());
				modelAndView.addObject("isEdit", "Y");
			}
		}

		modelAndView.addObject(AppBorrowImageDefine.FORM, record);
		LogUtil.endLog(AppBorrowImageController.class.toString(), AppBorrowImageDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = AppBorrowImageDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { AppBorrowImageDefine.PERMISSIONS_ADD })
	public ModelAndView insertAction(HttpServletRequest request, AppBorrowImageBean form) throws Exception {
		LogUtil.startLog(AppBorrowImageController.class.toString(), AppBorrowImageDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppBorrowImageDefine.INFO_PATH);
		 if (form.getPageType()!=null&&!form.getPageType().equals("")
		         &&form.getPageType().equals("0")) {
		     if (form.getJumpName()!=null&&form.getJumpName().contains(",")) {
		         if (form.getJumpName().split(",")[0]==null||form.getJumpName().split(",")[0].equals("")) {
		             form.setJumpName(form.getJumpName().split(",")[form.getJumpName().split(",").length-1]);
                }else {
                    form.setJumpName(form.getJumpName().split(",")[0]);
                }
	         }
        }else {
            form.setJumpName("");
        } 
		this.validatorFieldCheck(modelAndView, form);

	/*	AppBorrowImage appBorrowImage = this.borrowImageService.getRecords(form.getBorrowImage());
		if (appBorrowImage != null) {
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "borrowImage", "repeat");
		}

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(AppBorrowImageDefine.FORM, form);
			return modelAndView;
		}*/

		AppBorrowImage record = new AppBorrowImage();
		record.setBorrowImageTitle(form.getBorrowImageTitle());
		record.setBorrowImage(form.getBorrowImage());
		record.setBorrowImageName(form.getBorrowImageName());
		record.setBorrowImageUrl(form.getBorrowImageUrl());
		record.setBorrowImageRealname(form.getBorrowImageRealname());
		record.setNotes(form.getNotes());
		record.setSort(form.getSort());
        record.setPageUrl(form.getPageUrl());
        record.setPageType(form.getPageType());
        record.setVersion(form.getVersion());
        record.setStatus(form.getStatus());
        record.setVersionMax(form.getVersionMax());
        record.setJumpName(form.getJumpName());
		if (Validator.isNotNull(form.getBorrowImage())) {
			this.borrowImageService.insertRecord(record);
		}

		modelAndView.addObject(AppBorrowImageDefine.SUCCESS, AppBorrowImageDefine.SUCCESS);
		LogUtil.endLog(AppBorrowImageController.class.toString(), AppBorrowImageDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 修改维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = AppBorrowImageDefine.UPDATE_ACTION)
	@RequiresPermissions(value = { AppBorrowImageDefine.PERMISSIONS_MODIFY })
	public ModelAndView updateAction(HttpServletRequest request, AppBorrowImageBean form) throws Exception {
		LogUtil.startLog(AppBorrowImageController.class.toString(), AppBorrowImageDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(AppBorrowImageDefine.INFO_PATH);
		 if (form.getPageType()!=null&&!form.getPageType().equals("")
                 &&form.getPageType().equals("0")) {
             if (form.getJumpName()!=null&&form.getJumpName().contains(",")) {
                 if (form.getJumpName().split(",")[0]==null||form.getJumpName().split(",")[0].equals("")) {
                     form.setJumpName(form.getJumpName().split(",")[form.getJumpName().split(",").length-1]);
                }else {
                    form.setJumpName(form.getJumpName().split(",")[0]);
                }
             }
        }else {
            form.setJumpName("");
        }  
		this.validatorFieldCheck(modelAndView, form);
   
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)&&form.getStatus()==null) {
			modelAndView.addObject("isEdit", "Y");
			modelAndView.addObject(AppBorrowImageDefine.FORM, form);
			return modelAndView;
		}

		if (Validator.isNotNull(form.getBorrowImage())) {
			AppBorrowImage record = new AppBorrowImage();
            record.setId(form.getId());
			record.setBorrowImageTitle(form.getBorrowImageTitle());
			record.setBorrowImage(form.getBorrowImage());
			record.setBorrowImageName(form.getBorrowImageName());
			record.setBorrowImageUrl(form.getBorrowImageUrl());
			record.setBorrowImageRealname(form.getBorrowImageRealname());
			record.setNotes(form.getNotes());
			record.setSort(form.getSort());
	        record.setPageUrl(form.getPageUrl());
	        record.setPageType(form.getPageType());
	        record.setVersion(form.getVersion());
	        record.setStatus(form.getStatus());
	        record.setVersionMax(form.getVersionMax());
	        record.setJumpName(form.getJumpName());
			this.borrowImageService.updateRecord(record);
		}else {
		    AppBorrowImage record = new AppBorrowImage();
            record.setId(form.getId());
            record.setStatus(form.getStatus());
            this.borrowImageService.updateRecord(record);
        }
		modelAndView.addObject(AppBorrowImageDefine.SUCCESS, AppBorrowImageDefine.SUCCESS);
		LogUtil.endLog(AppBorrowImageController.class.toString(), AppBorrowImageDefine.UPDATE_ACTION);
		if (form.getStatus()!=null) {
		  return  init(request, null, form);
        } else {
            return modelAndView;
        }
		
	}

	/**
	 * 删除信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(AppBorrowImageDefine.DELETE_ACTION)
	@RequiresPermissions(AppBorrowImageDefine.PERMISSIONS_DELETE)
	public String deleteRecordAction(HttpServletRequest request, RedirectAttributes attr, AppBorrowImageBean form) {
		LogUtil.startLog(AppBorrowImageController.class.toString(), AppBorrowImageDefine.DELETE_ACTION);
		this.borrowImageService.deleteRecord(form.getId());
		attr.addFlashAttribute(AppBorrowImageDefine.FORM, form);
		LogUtil.endLog(AppBorrowImageController.class.toString(), AppBorrowImageDefine.DELETE_ACTION);
		return "redirect:" + AppBorrowImageDefine.REQUEST_MAPPING + "/" + AppBorrowImageDefine.INIT;
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = AppBorrowImageDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(BorrowCommonController.class.toString(), AppBorrowImageDefine.UPLOAD_FILE);
		ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
		String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
		String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
		String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.temp.path"));

		String logoRealPathDir = filePhysicalPath + fileUploadTempPath;

		File logoSaveFile = new File(logoRealPathDir);
		if (!logoSaveFile.exists()) {
			logoSaveFile.mkdirs();
		}

		BorrowCommonImage fileMeta = null;
		LinkedList<BorrowCommonImage> files = new LinkedList<BorrowCommonImage>();

		Iterator<String> itr = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;

		while (itr.hasNext()) {
			multipartFile = multipartRequest.getFile(itr.next());
			String fileRealName = String.valueOf(new Date().getTime());
			String originalFilename = multipartFile.getOriginalFilename();
			fileRealName = fileRealName + UploadFileUtils.getSuffix(multipartFile.getOriginalFilename());

			// 文件大小
			String errorMessage = UploadFileUtils.upload4Stream(fileRealName, logoRealPathDir, multipartFile.getInputStream(), 5000000L);

			fileMeta = new BorrowCommonImage();
			int index = originalFilename.lastIndexOf(".");
			if (index != -1) {
				fileMeta.setImageName(originalFilename.substring(0, index));
			} else {
				fileMeta.setImageName(originalFilename);
			}

			fileMeta.setImageRealName(fileRealName);
			fileMeta.setImageSize(multipartFile.getSize() / 1024 + "");// KB
			fileMeta.setImageType(multipartFile.getContentType());
			fileMeta.setErrorMessage(errorMessage);
			// 获取文件路径
			fileMeta.setImagePath(fileUploadTempPath + fileRealName);
			fileMeta.setImageSrc(fileDomainUrl + fileUploadTempPath + fileRealName);
			files.add(fileMeta);
		}

		LogUtil.endLog(BorrowCommonController.class.toString(), AppBorrowImageDefine.UPLOAD_FILE);
		return JSONObject.toJSONString(files, true);
	}

	/**
	 * 检查月数唯一
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppBorrowImageDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = { AppBorrowImageDefine.PERMISSIONS_ADD, AppBorrowImageDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkAction(HttpServletRequest request, AppBorrowImageBean form) {

		LogUtil.startLog(BorrowCommonController.class.toString(), AppBorrowImageDefine.CHECK_ACTION);
		JSONObject ret = new JSONObject();
		// 没有错误时,返回y
		if (!ret.containsKey(AppBorrowImageDefine.JSON_VALID_INFO_KEY)) {
			ret.put(AppBorrowImageDefine.JSON_VALID_STATUS_KEY, AppBorrowImageDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(BorrowCommonController.class.toString(), AppBorrowImageDefine.CHECK_ACTION);
		return ret.toString();
	}

	/**
	 * 信息验证
	 * 
	 * @param mav
	 * @param request
	 */
	public void validatorFieldCheck(ModelAndView mav, AppBorrowImageBean form) {
		// 图片标识
		ValidatorFieldCheckUtil.validateAlphaNumericAndMaxLength(mav, "borrowImage", form.getBorrowImage(), 20, true);
		// 图片名称
		ValidatorFieldCheckUtil.validateMaxLength(mav, "borrowImageTitle", form.getBorrowImageTitle(), 100, true);
		// 图片
		ValidatorFieldCheckUtil.validateRequired(mav, "borrowImageRealname", form.getBorrowImageRealname());
	}
}
