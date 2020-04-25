package com.hyjf.admin.coupon.tender.htj;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.coupon.config.CouponConfigController;
import com.hyjf.admin.coupon.config.CouponConfigService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderDetailCustomize;

/**
 * 优惠券发行
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = CouponTenderHtjDefine.REQUEST_MAPPING)
public class CouponTenderHtjController extends BaseController {

	@Autowired
	private CouponTenderHtjService couponTenderHtjService;

	@Autowired
    private CouponConfigService couponConfigService;
	/**
	 * 画面初始化
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CouponTenderHtjDefine.INIT)
	@RequiresPermissions(CouponTenderHtjDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(CouponTenderHtjDefine.FORM) CouponTenderHtjBean form) {
		LogUtil.startLog(CouponTenderHtjController.class.toString(), CouponTenderHtjDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(CouponTenderHtjDefine.LIST_PATH);
        form.setTimeStartSrch(GetDate.date2Str(GetDate.getTodayBeforeOrAfter(-30), new SimpleDateFormat("yyyy-MM-dd")));
        form.setTimeEndSrch(GetDate.date2Str(new Date(), new SimpleDateFormat("yyyy-MM-dd")));
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(CouponTenderHtjController.class.toString(), CouponTenderHtjDefine.INIT);
		return modelAndView;
	}

	/**
	 * 查询
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CouponTenderHtjDefine.SEARCH_ACTION)
	@RequiresPermissions(CouponTenderHtjDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(CouponTenderHtjDefine.FORM) CouponTenderHtjBean form) {
		LogUtil.startLog(CouponTenderHtjController.class.toString(), CouponTenderHtjDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(CouponTenderHtjDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(CouponTenderHtjController.class.toString(), CouponTenderHtjDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页机能
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, CouponTenderHtjBean form) {
		CouponTenderCustomize couponTenderCustomize=createCouponTenderCustomize(form);
		
		// 项目状态
        List<ParamName> list = this.couponTenderHtjService.getParamNameList(CustomConstants.COUPON_RECIVE_STATUS);
        modelAndView.addObject("couponReciveStatusList", list);
		
        Integer count = this.couponTenderHtjService.countRecord(couponTenderCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			couponTenderCustomize.setLimitStart(paginator.getOffset());
			couponTenderCustomize.setLimitEnd(paginator.getLimit());
			List<CouponTenderCustomize>  recordList = this.couponTenderHtjService.getRecordList(couponTenderCustomize);
			form.setPaginator(paginator);
			String recoverAccountTotal=this.couponTenderHtjService.queryRecoverAccountTotalHtj(couponTenderCustomize);
            modelAndView.addObject("recoverAccountTotal",recoverAccountTotal);
			modelAndView.addObject("recordList",recordList);
		}
		modelAndView.addObject(CouponTenderHtjDefine.FORM, form);
	}

	
    /**
	 * 画面迁移(含有id更新，不含有id添加)
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CouponTenderHtjDefine.INFO_ACTION)

	public ModelAndView infoAction(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(CouponTenderHtjDefine.FORM) CouponTenderHtjBean form) {
		LogUtil.startLog(CouponTenderHtjController.class.toString(), CouponTenderHtjDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(CouponTenderHtjDefine.INFO_PATH);
		String[] ids=form.getId().split("_");
		/*String userid=ids[0];
		String borrowNid=ids[1];
		String orderId=ids[2];
		String tenderId=ids[3];*/
		String couponUserId = ids[3];
		/*CouponTenderDetailCustomize couponTenderDetailCustomize=new CouponTenderDetailCustomize();
		couponTenderDetailCustomize.setUserId(userid);
		couponTenderDetailCustomize.setOrderId(orderId);
		couponTenderDetailCustomize.setbNid(borrowNid);
		couponTenderDetailCustomize.setTenderId(tenderId);*/
		Map<String,Object> paramMap = new HashMap<String,Object>();
		// 优惠券发放编号
		// couponTenderDetailCustomize.setTenderId(couponId);
		paramMap.put("couponUserId", couponUserId);
		paramMap.put("userFlag", 1);
		
		//详情
		CouponTenderDetailCustomize detail=new CouponTenderDetailCustomize();
		detail=couponTenderHtjService.getCouponTenderHtjDetailCustomize(paramMap);
		//回款列表
		List<CouponRecoverCustomize> list=
		        couponTenderHtjService.getCouponRecoverHtjCustomize(paramMap);
        //操作平台
        List<ParamName> clients=this.couponConfigService.getParamNameList("CLIENT");
        //modelAndView.addObject("clients", clients);
        //modelAndView.addObject("projectTypes",projectTypes);
        //被选中操作平台
        String clientString = "";

      //被选中操作平台
        String clientSed[] = StringUtils.split(detail.getCouponSystem(), ",");
        for(int i=0 ; i< clientSed.length;i++){
            if("-1".equals(clientSed[i])){
                clientString=clientString+"不限";
                break;
            }else{
                for (ParamName paramName : clients) {
                    if(clientSed[i].equals(paramName.getNameCd())){
                        if(i!=0&&clientString.length()!=0){
                            clientString=clientString+"/";
                        }
                        clientString=clientString+paramName.getName();
                        
                    }
                }
            }
        }
        detail.setCouponSystem(clientString);
        
        
        //modelAndView.addObject("selectedClientList", selectedClientList);
        //被选中项目类型  新逻辑 pcc20160715
        String projectString = "";
        //被选中项目类型
        String projectSed[] = StringUtils.split(detail.getProjectType(), ",");
        if(detail.getProjectType().indexOf("-1")!=-1){
            projectString="所有散标/新手/智投项目";
          }else{
              projectString="所有";
              for (String project : projectSed) {
                   if("1".equals(project)){
                       projectString=projectString+"散标/";
                   }  
                   if("2".equals(project)){
                       projectString=projectString+"";
                   } 
                   if("3".equals(project)){
                       projectString=projectString+"新手/";
                   } 
                   if("4".equals(project)){
                       projectString=projectString+"";
                   } 
                   if("5".equals(project)){
                       projectString=projectString+"";
                   }
                   if("6".equals(project)){
                       projectString=projectString+"智投/";
                   }
                   
              }
              projectString = StringUtils.removeEnd(
                      projectString, "/");
              projectString=projectString+"项目";
       }
        detail.setProjectType("适用"+projectString);
        
//        modelAndView.addObject("selectedProjectList", selectedProjectList);
        modelAndView.addObject("detail",detail);
        
        modelAndView.addObject("couponRecoverlist",list);
        
		LogUtil.endLog(CouponTenderHtjController.class.toString(), CouponTenderHtjDefine.INFO_ACTION);
		return modelAndView;
	}

	
	/**
     * 导出功能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    @RequestMapping(CouponTenderHtjDefine.EXPORT_ACTION)
    @RequiresPermissions(CouponTenderHtjDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(CouponTenderHtjDefine.FORM) CouponTenderHtjBean form) throws Exception {
        LogUtil.startLog(CouponTenderHtjController.class.toString(), CouponTenderHtjDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "优惠券使用-汇添金列表";
		//设置默认查询时间
		if(StringUtils.isEmpty(form.getTimeStartSrch())){
			form.setTimeStartSrch(GetDate.getDate("yyyy-MM-dd"));
		}
		if(StringUtils.isEmpty(form.getTimeEndSrch())){
			form.setTimeEndSrch(GetDate.getDate("yyyy-MM-dd"));
		}
        CouponTenderCustomize couponTenderCustomize=createCouponTenderCustomize(form);
        List<CouponTenderCustomize> resultList  = this.couponTenderHtjService.exoportRecordList(couponTenderCustomize);
        String recoverAccountTotal=this.couponTenderHtjService.queryRecoverAccountTotalHtj(couponTenderCustomize);
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[] {"序号", "智投编号", "计划订单号","用户名","用户优惠券编号",
                "优惠券类型编号","优惠券类型","面值","项目期限","出借利率","预期收益","还款状态","操作平台"  ,"使用时间" };
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        // 生成一个表格
        HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

        if (resultList != null && resultList.size() > 0) {

            int sheetCount = 1;
            int rowNum = 0;

            for (int i = 0; i < resultList.size(); i++) {
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
                    CouponTenderCustomize pInfo = resultList.get(i);
                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    else if (celLength == 1) {
                        cell.setCellValue(pInfo.getBorrowNid());
                    }
                    else if (celLength == 2) {
                        cell.setCellValue(pInfo.getOrderId());
                    }
                    else if (celLength == 3) {
                        cell.setCellValue(pInfo.getUsername());
                    }
                    else if (celLength == 4) {
                        cell.setCellValue(pInfo.getCouponUserCode());
                    }
                    else if (celLength == 5) {
                        cell.setCellValue(pInfo.getCouponCode());
                    }
                    else if (celLength == 6) {
                        cell.setCellValue(pInfo.getCouponTypeStr());
                    }
                    else if (celLength == 7) {
                        if("1".equals(pInfo.getCouponType())){
                            cell.setCellValue("￥"+pInfo.getCouponQuota());
                        }
                        if("2".equals(pInfo.getCouponType())){
                            cell.setCellValue(pInfo.getCouponQuota()+"%");
                        }
                        if("3".equals(pInfo.getCouponType())){
                            cell.setCellValue("￥"+pInfo.getCouponQuota());
                        }
                        
                    }
                    else if (celLength == 8) {
                        cell.setCellValue(pInfo.getBorrowPeriod());
                    }
                    else if (celLength == 9) {
                        cell.setCellValue(pInfo.getBorrowApr());
                    }
                    else if (celLength == 10) {
                        cell.setCellValue("￥"+pInfo.getRecoverAccountAll());
                    }
                    else if (celLength == 11) {
                        cell.setCellValue(pInfo.getReceivedFlg());
                    }
                    else if (celLength == 12) {
                        cell.setCellValue(pInfo.getOperatingDeck());
                    }
                    else if (celLength == 13) {
                        cell.setCellValue(pInfo.getOrderDate());
                    }
                }
            }
            rowNum++;
            Row row = sheet.createRow(rowNum);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue("合计");
            Cell cell2 = row.createCell(10);
            cell2.setCellValue("￥"+recoverAccountTotal);
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(CouponConfigController.class.toString(), CouponTenderHtjDefine.EXPORT_ACTION);
    }

    
    private CouponTenderCustomize createCouponTenderCustomize(CouponTenderHtjBean form) {
        CouponTenderCustomize couponTenderCustomize = new CouponTenderCustomize();

        if(StringUtils.isNotEmpty(form.getBorrowNid())){
            couponTenderCustomize.setBorrowNid(form.getBorrowNid());;
        }
        if(StringUtils.isNotEmpty(form.getOrderId())){
            couponTenderCustomize.setOrderId(form.getOrderId());
        }
        if(StringUtils.isNotEmpty(form.getUsername())){
            couponTenderCustomize.setUsername(form.getUsername());
        }
        if(StringUtils.isNotEmpty(form.getCouponCode())){
            couponTenderCustomize.setCouponCode(form.getCouponCode());
        }
        if(StringUtils.isNotEmpty(form.getCouponUserCode())){
            couponTenderCustomize.setCouponUserCode(form.getCouponUserCode());
        }
        if(StringUtils.isNotEmpty(form.getCouponType())){
            couponTenderCustomize.setCouponType(form.getCouponType());
        }
        if(StringUtils.isNotEmpty(form.getOperatingDeck())){
            couponTenderCustomize.setOperatingDeck(form.getOperatingDeck());
        }
        if(StringUtils.isNotEmpty(form.getCouponReciveStatus())){
            couponTenderCustomize.setReceivedFlg(form.getCouponReciveStatus());
        }
        if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
            couponTenderCustomize.setTimeStartSrch(form.getTimeStartSrch());
        }
        if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
            couponTenderCustomize.setTimeEndSrch(form.getTimeEndSrch());
        }
        
        return couponTenderCustomize;
    }

}
