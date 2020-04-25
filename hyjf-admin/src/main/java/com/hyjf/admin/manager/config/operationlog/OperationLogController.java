package com.hyjf.admin.manager.config.operationlog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.asset.assetlist.AssetListBean;
import com.hyjf.admin.manager.borrow.borrowinvest.BorrowInvestBean;
import com.hyjf.admin.manager.borrow.borrowinvest.BorrowInvestController;
import com.hyjf.admin.manager.borrow.borrowinvest.BorrowInvestDefine;
import com.hyjf.admin.manager.config.hzrconfig.HzrConfigDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.customize.FeerateModifyLog;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhAssetType;
import com.hyjf.mybatis.model.auto.HjhAssetTypeExample;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.HjhInstConfigExample;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.AssetListCustomize;
import com.hyjf.mybatis.model.customize.BorrowInvestCustomize;

/**
 * 配置中心操作日志
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = OperationLogDefine.REQUEST_MAPPING)
public class OperationLogController extends BaseController {

	@Autowired
	private OperationLogService operationlogService;

	/**
	 * 操作日志费率画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(OperationLogDefine.INIT)
	@RequiresPermissions(OperationLogDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request,
			@ModelAttribute(OperationLogDefine.HTLCONFIG_FORM) OperationLogBean form) {
		// 日志开始
		LogUtil.startLog(OperationLogController.class.toString(), OperationLogDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(OperationLogDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		// 日志结束
		LogUtil.endLog(OperationLogController.class.toString(), OperationLogDefine.INIT);
		return modelAndView;
	}
	/**
	 * 操作日志费率画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(OperationLogDefine.INFO_ACTION)
	@RequiresPermissions(OperationLogDefine.PERMISSIONS_VIEW)
	public ModelAndView infoAction(HttpServletRequest request,
			@ModelAttribute(OperationLogDefine.HTLCONFIG_FORM) OperationLogBean form) {
		// 日志开始
		LogUtil.startLog(OperationLogController.class.toString(), OperationLogDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(OperationLogDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		// 日志结束
		LogUtil.endLog(OperationLogController.class.toString(), OperationLogDefine.INIT);
		return modelAndView;
	}
	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, OperationLogBean form) {

		FeerateModifyLog feerateModifyLog=new FeerateModifyLog();
		HjhAssetTypeExample hjhAssetTypeExample=new HjhAssetTypeExample();
		HjhInstConfigExample hjhInstConfig=new HjhInstConfigExample();
		
		// 资产来源  inst_code机构编号 inst_name机构名称
		List<HjhInstConfig> hjhInstConfigs=this.operationlogService.getHjhInstConfig(hjhInstConfig);
		modelAndView.addObject("hjhInstConfigs", hjhInstConfigs);
		
		//产品类型   asset_type  asset_type_name资产类型名称
		List<HjhAssetType> hjhAssetTypes = this.operationlogService.getHjhAssetType(hjhAssetTypeExample);
		modelAndView.addObject("hjhAssetTypes", hjhAssetTypes);
		
		//修改类型
		modelAndView.addObject("updateTypes", updateTypeList());
		// 封装查询条件
		Map<String, Object> conditionMap = setCondition(form);
		
		Integer count = this.operationlogService.getRecordCount(conditionMap);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			List<FeerateModifyLog> recordList = this.operationlogService.getRecordList(conditionMap,
					paginator.getOffset(), paginator.getLimit());
			for (int i = 0; i < recordList.size(); i++) {
				//
				recordList.get(i).setModifyTypeSrch(accountEsbStates(recordList.get(i).getModifyType()));
				recordList.get(i).setStatusName(nameStates(recordList.get(i).getStatus()));
			}
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		} 

		modelAndView.addObject(OperationLogDefine.HTLCONFIG_FORM, form);
//		for (HjhAssetType hjhAssetType : hjhAssetTypes) {
//			System.out.println("+"+hjhAssetType.getAssetType()+"+"+hjhAssetType.getAssetTypeName());
//			System.out.println(hjhAssetType.getAssetType().equals(form.getAssetTypeSrch()));
//		}
//		System.out.println("~~~~~~~~~~~~");
//		System.out.println(form.getAssetTypeSrch());
	}

	/**
	 * 数据导出
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(OperationLogDefine.EXPORT_ACTION)
	@RequiresPermissions(OperationLogDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request, HttpServletResponse response, OperationLogBean form) throws Exception {
		LogUtil.startLog(OperationLogController.class.toString(), OperationLogDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "费率操作日志";
		
		// 封装查询条件
		Map<String, Object> conditionMap = setCondition(form);

		List<FeerateModifyLog> resultList = null;
		
		resultList = this.operationlogService.getRecordList(conditionMap,-1,-1);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "序号", "资产来源", "产品类型", "期限", "自动发标利率", "服务费", "管理费", "收益差率", "逾期利率", "逾期免息天数", "状态", "修改类型", "操作人", "操作时间" };
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
					FeerateModifyLog record = resultList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					// 资产来源
					else if (celLength == 1) {
						cell.setCellValue(record.getInstName());
					}
					// 产品类型
					else if (celLength == 2) {
						cell.setCellValue(record.getAssetTypeName());
					}
					// 期限
					else if (celLength == 3) {
						cell.setCellValue(record.getBorrowPeriod());
					}
					// 自动发标利率
					else if (celLength == 4) {
						cell.setCellValue(record.getBorrowApr().toString());
					}
					// 服务费
					else if (celLength == 5) {
						cell.setCellValue(record.getServiceFee());
					}
					// 管理费
					else if (celLength == 6) {
						cell.setCellValue(record.getManageFee());
					}
					// 收益差率
					else if (celLength == 7) {
						cell.setCellValue(record.getRevenueDiffRate());
					}
					// 逾期利率
					else if (celLength == 8) {
						cell.setCellValue(record.getLateInterestRate());
					}

					// 逾期免息天数
					else if (celLength == 9) {
						cell.setCellValue(record.getLateFreeDays());
					}
					// 状态
					else if (celLength == 10) {
						cell.setCellValue(nameStates(record.getStatus()));
					}
					// 修改类型
					else if (celLength == 11) {
						cell.setCellValue(accountEsbStates(record.getModifyType()));
					}
					// 操作人
					else if (celLength == 12) {
						cell.setCellValue(record.getName());
					}
					// 操作时间
					else if (celLength == 13) {
						cell.setCellValue(record.getCreateTimeString());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(BorrowInvestController.class.toString(), BorrowInvestDefine.EXPORT_ACTION);
	}
	/**
	 * 封装查询条件
	 * 
	 * @param form
	 * @return
	 */
	private Map<String, Object> setCondition(OperationLogBean form) {
		String instCodeSrch = StringUtils.isNotEmpty(form.getInstCodeSrch()) ? form.getInstCodeSrch() : null;
		String assetTypeSrch = StringUtils.isNotEmpty(form.getAssetTypeSrch()) ? form.getAssetTypeSrch() : null;
		String borrowPeriodSrch = StringUtils.isNotEmpty(form.getBorrowPeriodSrch()) ? form.getBorrowPeriodSrch() : null;
		String modifyTypeSrch = StringUtils.isNotEmpty(form.getModifyTypeSrch()) ? form.getModifyTypeSrch() : null;
		String userNameSrch = StringUtils.isNotEmpty(form.getUserNameSrch()) ? form.getUserNameSrch() : null;
		String recieveTimeStartSrch = StringUtils.isNotEmpty(form.getRecieveTimeStartSrch()) ? form.getRecieveTimeStartSrch() : null;
		String recieveTimeEndSrch = StringUtils.isNotEmpty(form.getRecieveTimeEndSrch()) ? form.getRecieveTimeEndSrch() : null;

		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("instCodeSrch", instCodeSrch);
		conditionMap.put("assetTypeSrch", assetTypeSrch);
		conditionMap.put("borrowPeriodSrch", borrowPeriodSrch);
		conditionMap.put("modifyTypeSrch", modifyTypeSrch);
		conditionMap.put("userNameSrch", userNameSrch);
		conditionMap.put("recieveTimeStartSrch", recieveTimeStartSrch);
		conditionMap.put("recieveTimeEndSrch", recieveTimeEndSrch);
		return conditionMap;
	}
	/**
	 * 修改类型
	 * @return list
	 */
	public List<Map<String,String>> updateTypeList(){
		
		List list=new ArrayList();
		Integer i = 0;
		for (; i < 4; i++) {
			Map<String,String> map=new HashMap();
			switch (i) {
			case 0:
//				map.put("typeId", i.toString());
//				map.put("name", "全部");
				break;
			case 1:
				map.put("typeId", i.toString());
				map.put("name", "增加");
				break;
			case 2:
				map.put("typeId", i.toString());
				map.put("name", "修改");
				break;
			case 3:
				map.put("typeId", i.toString());
				map.put("name", "删除");
				break;
			default:
				break;
			}
			list.add(map);
		}
		return list;
		
		
		
	}
	//判断修改类型表
    public String accountEsbStates(Integer string) {
//        if (string==0) {
//            return "全部";                                                                                                                                                                                                                                                                                                                                                                                                                                                        
//        }
        if (string==1) {
            return "增加";
        }
        if (string==2) {
            return "修改";
        }
        if (string==3) {
            return "删除";
        }
        return null;
        
    }
  //判断状态
    public String nameStates(Integer string) {
        if (string==0) {
            return "启用";                                                                                                                                                                                                                                                                                                                                                                                                                                                        
        }
        if (string==1) {
            return "禁用";
        }
        return null;
        
    }
	
}
