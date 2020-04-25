package com.hyjf.admin.manager.user.msp;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.maintenance.config.ConfigController;
import com.hyjf.admin.maintenance.config.ConfigDefine;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.user.msp.util.ParamUtil;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.MspAbnormalCredit;
import com.hyjf.mybatis.model.auto.MspAbnormalCreditDetail;
import com.hyjf.mybatis.model.auto.MspApply;
import com.hyjf.mybatis.model.auto.MspApplyDetails;
import com.hyjf.mybatis.model.auto.MspConfigure;
import com.hyjf.mybatis.model.auto.MspQueryDetail;

/**
 * 
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = MspApplyDefine.REQUEST_MAPPING)
public class MspApplyController extends BaseController {

    Logger _log = LoggerFactory.getLogger("MspApplyController");
    
	@Autowired
	private MspApplyService mspApplyService;
	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MspApplyDefine.INIT)
	@RequiresPermissions(MspApplyDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(MspApplyDefine.CONFIGBANK_FORM) MspApplyBean form) {
		LogUtil.startLog(MspApplyController.class.toString(), MspApplyDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MspApplyDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(MspApplyController.class.toString(), MspApplyDefine.INIT);
		return modelAndView;
	}

	/**
	 * 文章列表维护分页机能 页面初始化
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 * @throws ParseException 
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, MspApplyBean form){
		MspApply ma=new MspApply();
		int start=0;
		int end=0;
		ma.setName(form.getName());

		ma.setIdentityCard(form.getIdentityCard());

		ma.setCreateUser(form.getCreateUser());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(form.getStartCreate() != null) {
	        Date date;
			try {
				date = simpleDateFormat.parse(form.getStartCreate());
				
				start = (int) (date.getTime()/1000);
			} catch (ParseException e) {
			    _log.info("安融返回日期格式化异常："+e.getMessage());
			}
	        
		}
		if(form.getEndCreate() != null) {
			Date date;
			try {
				date = simpleDateFormat.parse(form.getEndCreate());
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);
				
				end = (int) ((cal.getTime()).getTime()/1000);
			} catch (ParseException e) {
			    _log.info("安融返回日期格式化异常："+e.getMessage());
			}
			
		}
		List<MspApply> recordList = this.mspApplyService.getRecordList(ma, -1, -1,start,end);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.mspApplyService.getRecordList(ma, paginator.getOffset(),
					paginator.getLimit(),start,end);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(MspApplyDefine.CONFIGBANK_FORM, form);
			String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
			modelAndView.addObject("fileDomainUrl",fileDomainUrl);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MspApplyDefine.INFO_ACTION)
	@RequiresPermissions(value = { MspApplyDefine.PERMISSIONS_INFO, MspApplyDefine.PERMISSIONS_ADD,
			MspApplyDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView info(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(MspApplyDefine.CONFIGBANK_FORM) MspApplyBean form) {
		LogUtil.startLog(MspApplyController.class.toString(), MspApplyDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(MspApplyDefine.INFO_PATH);
		
		
		 Date currentTime = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
	    form.setApplyDate(formatter.format(currentTime));
		
		form.setRegionList(this.mspApplyService.getRegionList());
		form.setConfigureList(this.mspApplyService.getConfigureList());
		modelAndView.addObject(MspApplyDefine.CONFIGBANK_FORM, form);
		
		LogUtil.endLog(MspApplyController.class.toString(), MspApplyDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加活动信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = MspApplyDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(MspApplyDefine.PERMISSIONS_ADD)
	@ResponseBody
	public JSONObject insertAction(HttpServletRequest request, MspApply form) {
		LogUtil.startLog(MspApplyController.class.toString(), MspApplyDefine.INSERT_ACTION);
		JSONObject result = new JSONObject();
		form.setCreateUser(ShiroUtil.getLoginUsername());
		// 调用校验
		if (!validatorParm(form)) {
			// 失败返回
		    result.put(MspApplyDefine.SUCCESS, false);
		    result.put(MspApplyDefine.MSG, "参数错误");
			return result;
		}
		// 准备参数
		Map<String, String> params = ParamUtil.getQueryUserParm(form);
		System.out.println("要请求的参数：params"+params);
		// 调用api 请求安融 查询方法
		String postResult = HttpClientUtils.post(PropUtils.getSystem(MspApplyDefine.HYJF_API_WEB_URL)+MspApplyDefine.HYJF_API_QUERY_URL, params);
		postResult = postResult.substring(1, postResult.length()-1);
		postResult = postResult.replaceAll("\\\\", "");
		JSONObject postResultJson = JSONObject.parseObject(postResult);
		
		if(!validatorApiResult(postResultJson)){
		    // 失败返回
            result.put(MspApplyDefine.SUCCESS, false);
            result.put(MspApplyDefine.MSG, postResultJson.get(MspApplyDefine.RESULT_JSON_KEY_MSP_MESS)+"<br/>"+postResultJson.get(MspApplyDefine.RESULT_JSON_KEY_FQZ_MESS));
            return result;
		}
		    
		form.setApplyId(postResultJson.getString(MspApplyDefine.RESULT_JSON_KEY_REQID));
		//form.setApplyId("kkkkkkk");
		form.setCreateTime(GetDate.getNowTime10());
		// 数据插入
		this.mspApplyService.insertRecord(form);
		result.put(MspApplyDefine.SUCCESS, true);
		return result;
	}

	private boolean validatorApiResult(JSONObject postResultJson) {
	    if(postResultJson.containsKey(MspApplyDefine.SUCCESS) && postResultJson.getBooleanValue(MspApplyDefine.SUCCESS)){
	        // 成功
	        return true;
	    }
        return false;
    }

    private boolean validatorParm(MspApply form) {
	    if (Validator.isNull(form.getName())) {
            return false;
        }
	    if (form.getName().length()>50) {
	        return false;
        }
        return true;
    }

    /**
	 * 修改活动维护信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = MspApplyDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(MspApplyDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, MspApply form) {
		LogUtil.startLog(MspApplyController.class.toString(), MspApplyDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(MspApplyDefine.INFO_PATH);

		// 调用校验
		if (validatorFieldCheck(modelAndView, form) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, form);
		}
		// // 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		// form.setLogo("1");
		// 更新
		this.mspApplyService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(MspApplyDefine.SUCCESS, MspApplyDefine.SUCCESS);
		LogUtil.endLog(MspApplyController.class.toString(), MspApplyDefine.UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 删除配置信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ConfigDefine.DELETE_ACTION)
	@RequiresPermissions(MspApplyDefine.PERMISSIONS_DELETE)
	public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
		LogUtil.startLog(ConfigController.class.toString(), ConfigDefine.DELETE_ACTION);

		ModelAndView modelAndView = new ModelAndView(MspApplyDefine.RE_LIST_PATH);
		// 解析json字符串
		List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
		this.mspApplyService.deleteRecord(recordList);
		LogUtil.endLog(ConfigController.class.toString(), ConfigDefine.DELETE_ACTION);
		return modelAndView;
	}

	/**
	 * 调用校验表单方法
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, MspApply form) {
		// 字段校验(非空判断和长度判断)
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "name", form.getName())) {
			return modelAndView;
		}
		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getName(), 50, true)) {
			return modelAndView;
		}
//		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "code", form.getCode())) {
//			return modelAndView;
//		}
//		if (!ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "code", form.getCode(), 10, true)) {
//			return modelAndView;
//		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = MspApplyDefine.VALIDATEBEFORE)
	@RequiresPermissions(MspApplyDefine.PERMISSIONS_VIEW)
	public Map<String, Object> validateBeforeAction(HttpServletRequest request, MspApply form) {
		LogUtil.startLog(MspApplyController.class.toString(), MspApplyDefine.VALIDATEBEFORE);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<MspApply> list = mspApplyService.getRecordList(form, -1, -1,0,0);
		if (list != null && list.size() != 0) {
			if (form.getId() != null) {
				Boolean hasnot = true;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId() == form.getId()) {
						hasnot = false;
						break;
					}
				}
				if (hasnot) {
					resultMap.put("success", false);
					resultMap.put("msg", "银行名称或银行代码不可重复添加");
				} else {
					resultMap.put("success", true);
				}
			} else {
				resultMap.put("success", false);
				resultMap.put("msg", "银行名称或银行代码不可重复添加");
			}
		} else {
			resultMap.put("success", true);
		}
		LogUtil.endLog(MspApplyController.class.toString(), MspApplyDefine.VALIDATEBEFORE);
		return resultMap;
	}
	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(MspApplyDefine.EXPORT_ACTION)
	@RequiresPermissions(MspApplyDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(MspApplyDefine.CONFIGBANK_FORM) MspApplyBean form) throws Exception {
		LogUtil.startLog(MspApplyDefine.class.toString(), MspApplyDefine.EXPORT_ACTION);
		
		
		
		MspApply ma=new MspApply();
		int start=0;
		int end=0;
		ma.setName(form.getName());

		ma.setIdentityCard(form.getIdentityCard());

		ma.setCreateUser(form.getCreateUser());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(form.getStartCreate() != null) {
	        Date date;
			try {
				date = simpleDateFormat.parse(form.getStartCreate());
				start = (int) (date.getTime()/1000);
			} catch (ParseException e) {
			    _log.info("安融返回日期格式化异常："+e.getMessage());
			}
	        
		}
		if(form.getEndCreate() != null) {
			Date date;
			try {
				date = simpleDateFormat.parse(form.getEndCreate());
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);
				
				end = (int) ((cal.getTime()).getTime()/1000);
			} catch (ParseException e) {
			    _log.info("安融返回日期格式化异常："+e.getMessage());
			}
			
		}
		List<MspApply> recordList = this.mspApplyService.getRecordList(ma, -1, -1,start,end);
		
		
		
		// 表格sheet名称
		String sheetName = "安融反欺诈查询";


		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		String[] titles = new String[] {"序号", "姓名", "身份证号","操作人","查询时间" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		if (recordList != null && recordList.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < recordList.size(); i++) {
				rowNum++;
				if (i != 0 && i % 60000 == 0) {
					sheetCount++;
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					MspApply pInfo = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					else if (celLength == 1) {
						cell.setCellValue(pInfo.getName());
					}
					else if (celLength == 2) {
						cell.setCellValue(pInfo.getIdentityCard());
					}
					else if (celLength == 3) {
						cell.setCellValue(pInfo.getCreateUser());
					}
					else if (celLength == 4) {
					    Long time1=new Long(pInfo.getCreateTime());
					    String d = format.format(time1*1000);  
						cell.setCellValue(d);
					}

				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(MspApplyController.class.toString(), MspApplyDefine.EXPORT_ACTION);
	}
	
	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MspApplyDefine.APPLY_ACTION)
	@RequiresPermissions(value = { MspApplyDefine.PERMISSIONS_INFO, MspApplyDefine.PERMISSIONS_ADD,
			MspApplyDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView applyInfo(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(MspApplyDefine.CONFIGBANK_FORM) MspApplyBean form) {
		LogUtil.startLog(MspApplyController.class.toString(), MspApplyDefine.APPLY_ACTION);
		ModelAndView modelAndView = new ModelAndView(MspApplyDefine.INFO_PATH2);
		
		MspApply ma=this.mspApplyService.getRecord( Integer.valueOf(form.getIds()));
		
		
		if(ma.getShareIdentification()==0) {
			 MspConfigure mc=new MspConfigure();
			 ma.setApplyDate( ma.getApplyDate());
			 ma.setContractBegin( ma.getApplyDate());
			 ma.setApprovalDate(ma.getApplyDate());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
			Calendar   calendar   =   new   GregorianCalendar();
			try {
				calendar.setTime(formatter.parse(ma.getApplyDate()));
			} catch (ParseException e) {

			}
			 
			if(ma.getConfigureId() != null) {
				mc=this.mspApplyService.getConfigure(ma.getConfigureId());
				ma.setServiceType(mc.getServiceType());
				ma.setUnredeemedMoney(mc.getUnredeemedMoney());
				ma.setRepaymentStatus(mc.getRepaymentStatus());
				ma.setApprovalResult(mc.getApprovalResult());
				ma.setGuaranteeType(mc.getGuaranteeType());
				ma.setCreditAddress(mc.getCreditAddress());
				calendar.add(Calendar.MONTH, mc.getLoanTimeLimit());
			}else {
				calendar.add(Calendar.MONTH,ma.getLoanTimeLimit());
			}
				
			 ma.setContractEnd( formatter.format(calendar.getTime()));
		}


		modelAndView.addObject(MspApplyDefine.CONFIGBANK_FORM, ma);
		modelAndView.addObject("regionList", this.mspApplyService.getRegionList());
		
		LogUtil.endLog(MspApplyController.class.toString(), MspApplyDefine.INIT);
		return modelAndView;
	}

	// 安融共享
	@RequestMapping(value = MspApplyDefine.SHARE_USER_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(MspApplyDefine.PERMISSIONS_ADD)
    @ResponseBody
    public JSONObject shareUser(HttpServletRequest request, MspApply form) {
        LogUtil.startLog(MspApplyController.class.toString(), MspApplyDefine.SHARE_USER_ACTION);
        JSONObject result = new JSONObject();
        
        // 准备参数
        if(!form.getApprovalResult().equals("01")) {
        	
        	form.setUnredeemedMoney(null);
        	form.setRepaymentStatus(null);
//        	form.setContractBegin(null);
//        	form.setContractEnd(null);

        }
        Map<String, String> params = ParamUtil.getSendParm(form);
        System.out.println("要请求的参数：params"+params);
        // 调用api 请求安融共享方法
        String postResult = HttpClientUtils.post(PropUtils.getSystem(MspApplyDefine.HYJF_API_WEB_URL)+MspApplyDefine.HYJF_API_SEND_URL, params);
        postResult = postResult.substring(1, postResult.length()-1);
        postResult = postResult.replaceAll("\\\\", "");
        JSONObject postResultJson = JSONObject.parseObject(postResult);
        
        if(!validatorApiResult(postResultJson)){
            // 失败返回
            result.put(MspApplyDefine.SUCCESS, false);
            result.put(MspApplyDefine.MSG, postResultJson.get(MspApplyDefine.MSG));
            return result;
        }
            
        form.setUpdateTime(GetDate.getNowTime10());
        form.setUpdateUser(ShiroUtil.getLoginUsername());
        // 数据修改
        this.mspApplyService.updateRecord(form);
        result.put(MspApplyDefine.SUCCESS, true);
        return result;
    }
	
	
	/**
	 * 列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(MspApplyDefine.DOWNLOAD_FILE)
	@RequiresPermissions(MspApplyDefine.PERMISSIONS_VIEW)
	public ModelAndView download(HttpServletRequest request, @RequestParam("applyId") String applyId,@RequestParam("ids") int ids) {
		LogUtil.startLog(MspApplyController.class.toString(), MspApplyDefine.DOWNLOAD_FILE);
		ModelAndView modelAndView = new ModelAndView(MspApplyDefine.DOWNLOAD_PATH);
		if(applyId==null||!applyId.equals("")) {
			MspApply ma=this.mspApplyService.getRecord( Integer.valueOf(ids));
			modelAndView.addObject("mspapply", ma);
			modelAndView.addObject("fqz", this.mspApplyService.getFqz(applyId));
			modelAndView.addObject("shixinList", this.mspApplyService.getShixinInfos(applyId));
			modelAndView.addObject("zhixingList", this.mspApplyService.getZhixingInfos(applyId));
			modelAndView.addObject("anliList",this.mspApplyService.getAnliInfos(applyId));
			
			
			List<MspApplyDetails> applylist = this.mspApplyService.getApplyDetails(applyId);
			//BigDecimal daishenhe = new BigDecimal(0);
			BigDecimal tongguo= new BigDecimal(0);
			BigDecimal jujue= new BigDecimal(0);
			BigDecimal  zongshu= new BigDecimal(0);
			BigDecimal  quxiao= new BigDecimal(0);
			int quxiaoshu=0;
//			if (detail.indexOf("01") >= 0) {
//				$(this).text("审批通过");
//			}
//			if (detail.indexOf("02") >= 0) {
//				$(this).text("审批拒绝");
//			}
//			if (detail.indexOf("04") >= 0) {
//				$(this).text("重新审批");
//			}
//			if (detail.indexOf("05") >= 0) {
//				$(this).text("客户取消");
//			}
			for (MspApplyDetails mspApplyDetails : applylist) {
				if(mspApplyDetails.getApplyresult()!=null&&mspApplyDetails.getApplyresult().equals("01")) {
					tongguo=tongguo.add(new BigDecimal(mspApplyDetails.getLoanmoney()));
					
				}
				if(mspApplyDetails.getApplyresult()!=null&&mspApplyDetails.getApplyresult().equals("02")) {
					jujue=jujue.add(new BigDecimal(mspApplyDetails.getLoanmoney()));
				}
				if(mspApplyDetails.getApplyresult()!=null&&mspApplyDetails.getApplyresult().equals("05")) {
					quxiao=quxiao.add(new BigDecimal(mspApplyDetails.getLoanmoney()));
					quxiaoshu=quxiaoshu+1;
				}
				zongshu=zongshu.add(new BigDecimal(mspApplyDetails.getLoanmoney()));
				
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	          
	        Calendar   calendar   =   new   GregorianCalendar(); 
	        calendar.setTime(new Date()); 
	        calendar.add(calendar.DAY_OF_MONTH, -3);
	        Date y3 = calendar.getTime();
	        calendar.add(calendar.DAY_OF_MONTH, -3);
	        Date y6 = calendar.getTime();
		        
			
	        List<MspQueryDetail> queryList = this.mspApplyService.getQueryDetail(applyId);
	        
	        int sangeyue=0;
	        int liugeyue=0;
	        for (Iterator iterator = queryList.iterator(); iterator.hasNext();) {
				MspQueryDetail mspQueryDetail = (MspQueryDetail) iterator.next();
				try {
					if(y3.before(sdf.parse(mspQueryDetail.getQuerydate()))) {
						sangeyue=sangeyue+1;
					}
					if(y6.before(sdf.parse(mspQueryDetail.getQuerydate()))) {
						liugeyue=liugeyue+1;
					}
				} catch (ParseException e) {
				    _log.info("安融异常："+e.getMessage());
				}
				
			}
	        modelAndView.addObject("sangeyue",sangeyue);
	        modelAndView.addObject("liugeyue",liugeyue);
	        modelAndView.addObject("zongji",queryList.size());
			modelAndView.addObject("tongguo",tongguo);
			modelAndView.addObject("jujue",jujue);
			modelAndView.addObject("zongshu",zongshu);
			modelAndView.addObject("quxiao",quxiao);
			modelAndView.addObject("title",this.mspApplyService.getTitle(applyId));
			modelAndView.addObject("normalCreditList",this.mspApplyService.getNormalCreditDetail(applyId));
			modelAndView.addObject("applyList",applylist);
			modelAndView.addObject("queryList",queryList);
			modelAndView.addObject("blackList",this.mspApplyService.getBlackData(applyId));
			
			List<MspAbnormalCredit> mbl=this.mspApplyService.getAbnormalCredit( applyId);
			List<MspAbnormalBean> mabl=new ArrayList<MspAbnormalBean>();
			for (Iterator<MspAbnormalCredit> iterator = mbl.iterator(); iterator.hasNext();) {
				MspAbnormalCredit mspAbnormalCredit = iterator.next();
				List<MspAbnormalCreditDetail> mcl=this.mspApplyService.getAbnormalCreditDetail( mspAbnormalCredit.getId());
					for (Iterator<MspAbnormalCreditDetail> iterator2 = mcl.iterator(); iterator2.hasNext();) {
						MspAbnormalCreditDetail mspAbnormalCreditDetail = (MspAbnormalCreditDetail) iterator2.next();	
						MspAbnormalBean mb=new MspAbnormalBean();
						mb.setCreditstartdate(mspAbnormalCredit.getCreditstartdate());
						mb.setCreditenddate(mspAbnormalCredit.getCreditenddate());
						mb.setLoantype(mspAbnormalCredit.getLoantype());
						mb.setLoanmoney(mspAbnormalCredit.getLoanmoney());
						mb.setAssuretype(mspAbnormalCredit.getAssuretype());
						mb.setLoanperiods(mspAbnormalCredit.getLoanperiods());
						mb.setNum(mspAbnormalCredit.getNum());
						mb.setCheckoverduedate(mspAbnormalCreditDetail.getCheckoverduedate());
						mb.setOverduedays(mspAbnormalCreditDetail.getOverduedays());
						mb.setOverduereason(mspAbnormalCreditDetail.getOverduereason());
						mb.setOverduestate(mspAbnormalCreditDetail.getOverduestate());
						mb.setOpertime(mspAbnormalCreditDetail.getOpertime());
						mb.setRemark(mspAbnormalCreditDetail.getRemark());
						mabl.add(mb);
					}
				
			}
			modelAndView.addObject("abnormalList",mabl);
		//;	public List<MspAbnormalCreditDetail> getAbnormalCreditDetail(String applyId);
		//	public List<MspAbnormalCredit> getAbnormalCredit(String applyId);
			
		}

		LogUtil.endLog(MspApplyController.class.toString(), MspApplyDefine.DOWNLOAD_FILE);
		return modelAndView;
	}

	
	
}
