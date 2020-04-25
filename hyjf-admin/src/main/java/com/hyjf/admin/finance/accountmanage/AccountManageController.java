package com.hyjf.admin.finance.accountmanage;


import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.finance.accountdetail.AccountDetailBean;
import com.hyjf.admin.finance.accountdetail.AccountDetailController;
import com.hyjf.admin.finance.accountdetail.AccountDetailDefine;
import com.hyjf.admin.finance.accountdetail.AccountDetailService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.mybatis.model.customize.AccountManageCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * 资金管理
 *
 * @author HBZ
 */
@Controller
@RequestMapping(value = AccountManageDefine.REQUEST_MAPPING)
public class AccountManageController extends BaseController {
    /**
     * 类名
     */
    private static final String THIS_CLASS = AccountManageController.class.getName();

    @Autowired
    private AccountManageService accountManageService;
    @Autowired
    private AccountDetailService accountDetailService;

    /**
     * 分页
     *
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modeAndView, AccountManageBean form) {
        AccountManageCustomize accountInfoCustomize = new AccountManageCustomize();
        BeanUtils.copyProperties(form, accountInfoCustomize);

        Integer count = this.accountManageService.queryAccountCount(accountInfoCustomize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            accountInfoCustomize.setLimitStart(paginator.getOffset());
            accountInfoCustomize.setLimitEnd(paginator.getLimit());

            List<AccountManageCustomize> accountInfos = this.accountManageService.queryAccountInfos(accountInfoCustomize);
            form.setPaginator(paginator);
            form.setRecordList(accountInfos);
        }
        modeAndView.addObject(AccountManageDefine.ACCOUNTMANAGE_FORM, form);

    }

    /**
     * 账户管理 列表
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountManageDefine.ACCOUNTMANAGE_LIST)
    @RequiresPermissions(AccountManageDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, AccountManageBean form) {
        LogUtil.startLog(AccountManageController.class.toString(), AccountManageDefine.ACCOUNTMANAGE_LIST);
        ModelAndView modeAndView = new ModelAndView(AccountManageDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modeAndView, form);
        LogUtil.endLog(AccountManageController.class.toString(), AccountManageDefine.ACCOUNTMANAGE_LIST);
        return modeAndView;
    }

    /**
     * 根据业务需求导出相应的表格 此处暂时为可用情况 缺陷： 1.无法指定相应的列的顺序， 2.无法配置，excel文件名，excel sheet名称
     * 3.目前只能导出一个sheet 4.列的宽度的自适应，中文存在一定问题
     * 5.根据导出的业务需求最好可以在导出的时候输入起止页码，因为在大数据量的情况下容易造成卡顿
     *
     * 导出账户列表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(AccountManageDefine.ENHANCE_EXPORT_ACTION)
    @RequiresPermissions(value = {AccountManageDefine.PERMISSIONS_ACCOUNTMANAGE_EXPORT, AccountManageDefine.ENHANCE_PERMISSIONS_EXPORT}, logical = Logical.AND)
    public void enhanceExportAccountsExcel(HttpServletRequest request, HttpServletResponse response, AccountManageBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "账户数据";
        
        // 取得数据
        form.setLimitStart(-1);
        form.setLimitEnd(-1);
        // 部门
        if (Validator.isNotNull(form.getCombotreeSrch())) {
            if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
                String[] list = form.getCombotreeSrch().split(StringPool.COMMA);
                form.setCombotreeListSrch(list);
            } else {
                form.setCombotreeListSrch(new String[]{form.getCombotreeSrch()});
            }
        }
        List<AccountManageCustomize> recordList = this.accountManageService.queryAccountInfos(form);
        
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        
        String[] titles = new String[] { "用户ID", "用户名", "分公司", "分部", "团队", "资产总额", "可用金额", "冻结金额", "待收金额", "待还金额", "待返充值金额", "待返充手续费", "待返累计出借金额", "汇付账户" 
        		,"用户手机号","用户属性（当前）","用户角色","一级分部（当前）","二级分部（当前）","三级分部（当前）","推荐人用户名（当前）","推荐人姓名（当前）","推荐人所属一级分部（当前）","推荐人所属二级分部（当前）","推荐人所属三级分部（当前）"};
        
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 生成一个表格
        HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");
        
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
                    AccountManageCustomize bean = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 用户ID
                    if (celLength == 0) {
                        cell.setCellValue(bean.getUserId());
                    }
                    // 用户名
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getUsername());
                    }
                    // 分公司
                    else if (celLength == 2) {
                        cell.setCellValue(bean.getRegionName());
                    }
                    // 分部
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getBranchName());
                    }
                    // 团队
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getDepartmentName());
                    }
                    // 资产总额
                    else if (celLength == 5) {
                        if(bean.getTotal() != null){
                            cell.setCellValue(String.valueOf(bean.getTotal()));
                        }else{
                            cell.setCellValue("0.00");
                        }
                    }
                    // 可用金额
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getBalance().toString());
                    }
                    // 冻结金额
                    else if (celLength == 7) {
                        cell.setCellValue(bean.getFrost().toString());
                    }
                    // 待收金额
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getAwait().toString());
                    }
                    //待还金额
                    else if (celLength == 9) {
                        cell.setCellValue(bean.getRepay().toString());
                    }
                    // 待返充值金额
                    else if (celLength == 10) {
                        if(bean.getRecMoney() != null){
                            cell.setCellValue(String.valueOf(bean.getRecMoney()));
                        }else{
                            cell.setCellValue("0.00");
                        }
                    }
                    // 待返充手续费
                    else if (celLength == 11) {
                        cell.setCellValue(bean.getFee().toString());
                    }
                    // 待返累计出借金额
                    else if (celLength == 12) {
                        cell.setCellValue(bean.getInMoney().toString());
                    }
                    // 汇付账户
                    else if (celLength == 13) {
                        cell.setCellValue(bean.getBalanceCash().toString()+"/"+bean.getBalanceFrost());
                    }
                    // 手机号
                    else if (celLength == 14) {
                    	cell.setCellValue(bean.getMobile());
                    }
                    //用户属性（当前）
                    else if (celLength == 15) {
                    	 if ("0".equals(bean.getUserAttribute())) {
                             cell.setCellValue("无主单");
                         } else if ("1".equals(bean.getUserAttribute())) {
                             cell.setCellValue("有主单");
                         } else if ("2".equals(bean.getUserAttribute())) {
                             cell.setCellValue("线下员工");
                         } else if ("3".equals(bean.getUserAttribute())) {
                             cell.setCellValue("线上员工");
                         }
                    }
                    // 角色
                    else if (celLength == 16) {
                    	cell.setCellValue(bean.getRoleid());
                    }
                    // 用户所属一级分部（当前）
                    else if (celLength == 17) {
                    	cell.setCellValue(bean.getUserRegionName());
                    }
                    // 用户所属二级分部（当前）
                    else if (celLength == 18) {
                    	cell.setCellValue(bean.getUserBranchName());
                    }
                    // 用户所属三级分部（当前）
                    else if (celLength == 19) {
                    	cell.setCellValue(bean.getUserDepartmentName());
                    }
                    // 推荐人用户名（当前）
                    else if (celLength == 20) {
                    	cell.setCellValue(bean.getReferrerName());
                    }
                    // 推荐人姓名（当前）
                    else if (celLength == 21) {
                    	cell.setCellValue(bean.getReferrerTrueName());
                    }
                    // 推荐人所属一级分部（当前）
                    else if (celLength == 22) {
                    	cell.setCellValue(bean.getReferrerRegionName());
                    }
                    // 推荐人所属二级分部（当前）
                    else if (celLength == 23) {
                    	cell.setCellValue(bean.getReferrerBranchName());
                    }
                    // 推荐人所属三级分部（当前）
                    else if (celLength == 24) {
                    	cell.setCellValue(bean.getReferrerDepartmentName());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    @RequestMapping(AccountManageDefine.EXPORT_ACCOUNTMANAGE_ACTION)
    @RequiresPermissions(AccountManageDefine.PERMISSIONS_ACCOUNTMANAGE_EXPORT)
    public void exportAccountsExcel(HttpServletRequest request, HttpServletResponse response, AccountManageBean form) throws Exception {
        // 表格sheet名称
        String sheetName = "账户数据";

        // 取得数据
        form.setLimitStart(-1);
        form.setLimitEnd(-1);
        // 部门
        if (Validator.isNotNull(form.getCombotreeSrch())) {
            if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
                String[] list = form.getCombotreeSrch().split(StringPool.COMMA);
                form.setCombotreeListSrch(list);
            } else {
                form.setCombotreeListSrch(new String[]{form.getCombotreeSrch()});
            }
        }
        List<AccountManageCustomize> recordList = this.accountManageService.queryAccountInfos(form);

        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles = new String[] { "用户ID", "用户名", "资产总额", "可用金额", "冻结金额", "待收金额", "待还金额", "待返充值金额", "待返充手续费", "待返累计出借金额", "汇付账户"
                ,"用户手机号","用户属性（当前）","用户角色", "推荐人用户名（当前）","推荐人姓名（当前）"};

        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 生成一个表格
        HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

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
                    AccountManageCustomize bean = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 用户ID
                    if (celLength == 0) {
                        cell.setCellValue(bean.getUserId());
                    }
                    // 用户名
                    else if (celLength == 1) {
                        cell.setCellValue(bean.getUsername());
                    }
                    // 资产总额
                    else if (celLength == 2) {
                        if(bean.getTotal() != null){
                            cell.setCellValue(String.valueOf(bean.getTotal()));
                        }else{
                            cell.setCellValue("0.00");
                        }
                    }
                    // 可用金额
                    else if (celLength == 3) {
                        cell.setCellValue(bean.getBalance().toString());
                    }
                    // 冻结金额
                    else if (celLength == 4) {
                        cell.setCellValue(bean.getFrost().toString());
                    }
                    // 待收金额
                    else if (celLength == 5) {
                        cell.setCellValue(bean.getAwait().toString());
                    }
                    //待还金额
                    else if (celLength == 6) {
                        cell.setCellValue(bean.getRepay().toString());
                    }
                    // 待返充值金额
                    else if (celLength == 7) {
                        if(bean.getRecMoney() != null){
                            cell.setCellValue(String.valueOf(bean.getRecMoney()));
                        }else{
                            cell.setCellValue("0.00");
                        }
                    }
                    // 待返充手续费
                    else if (celLength == 8) {
                        cell.setCellValue(bean.getFee().toString());
                    }
                    // 待返累计出借金额
                    else if (celLength == 9) {
                        cell.setCellValue(bean.getInMoney().toString());
                    }
                    // 汇付账户
                    else if (celLength == 10) {
                        cell.setCellValue(bean.getBalanceCash().toString()+"/"+bean.getBalanceFrost());
                    }
                    // 手机号
                    else if (celLength == 11) {
                        cell.setCellValue(bean.getMobile());
                    }
                    //用户属性（当前）
                    else if (celLength == 12) {
                        if ("0".equals(bean.getUserAttribute())) {
                            cell.setCellValue("无主单");
                        } else if ("1".equals(bean.getUserAttribute())) {
                            cell.setCellValue("有主单");
                        } else if ("2".equals(bean.getUserAttribute())) {
                            cell.setCellValue("线下员工");
                        } else if ("3".equals(bean.getUserAttribute())) {
                            cell.setCellValue("线上员工");
                        }
                    }
                    // 角色
                    else if (celLength == 13) {
                        cell.setCellValue(bean.getRoleid());
                    }
                    // 推荐人用户名（当前）
                    else if (celLength == 14) {
                        cell.setCellValue(bean.getReferrerName());
                    }
                    // 推荐人姓名（当前）
                    else if (celLength == 15) {
                        cell.setCellValue(bean.getReferrerTrueName());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    /**
     * 资金明细 列表 分页
     *
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modeAndView, AccountDetailBean form) {
        //交易类型列表
        List<AccountTrade> trades= this.accountDetailService.selectTradeTypes();
        form.setTradeList(trades);
        
        AccountDetailCustomize accountDetailCustomize = new AccountDetailCustomize();
        accountDetailCustomize.setType(form.getType());
        accountDetailCustomize.setUserId(form.getUserId());
        accountDetailCustomize.setUsername(form.getUsername());
        accountDetailCustomize.setStartDate(form.getStartDate());
        accountDetailCustomize.setEndDate(form.getEndDate());
        accountDetailCustomize.setReferrerName(form.getReferrerName());
        accountDetailCustomize.setNid(form.getNid());
        accountDetailCustomize.setTradeTypeSearch(form.getTradeTypeSearch());

        Integer count = this.accountDetailService.queryAccountDetailCount(accountDetailCustomize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            accountDetailCustomize.setLimitStart(paginator.getOffset());
            accountDetailCustomize.setLimitEnd(paginator.getLimit());

            List<AccountDetailCustomize> accountDetails = this.accountDetailService.queryAccountDetails(accountDetailCustomize);
            form.setPaginator(paginator);
            form.setRecordList(accountDetails);
            modeAndView.addObject(AccountDetailDefine.ACCOUNTDETAIL_FORM, form);
        } else {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            accountDetailCustomize.setLimitStart(paginator.getOffset());
            accountDetailCustomize.setLimitEnd(paginator.getLimit());
            form.setPaginator(paginator);
            modeAndView.addObject(AccountDetailDefine.ACCOUNTDETAIL_FORM, form);
        }

    }

    /**
     * 资金明细 列表
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AccountDetailDefine.ACCOUNTDETAIL_LIST)
    @RequiresPermissions(AccountDetailDefine.PERMISSIONS_VIEW)
    public ModelAndView DetailInit(HttpServletRequest request, AccountDetailBean form) {
        LogUtil.startLog(AccountDetailController.class.toString(), AccountDetailDefine.ACCOUNTDETAIL_LIST);
        ModelAndView modeAndView = new ModelAndView(AccountDetailDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modeAndView, form);
        LogUtil.endLog(AccountDetailController.class.toString(), AccountDetailDefine.ACCOUNTDETAIL_LIST);
        return modeAndView;
    }
    


    /**
     * 导出资金明细列表
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(AccountDetailDefine.EXPORT_ACCOUNTDETAIL_ACTION)
    @RequiresPermissions(AccountDetailDefine.PERMISSIONS_ACCOUNTDETAIL_EXPORT)
	public void exportAccountDetailExcel(HttpServletRequest request, HttpServletResponse response, AccountDetailBean form)
			throws Exception {
		// 表格sheet名称
		String sheetName = "资金明细";

		// 取得数据
		form.setLimitStart(-1);
		form.setLimitEnd(-1);
		List<AccountDetailCustomize> recordList = this.accountDetailService.queryAccountDetails(form);

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + ".xls";

		String[] titles = new String[] { "序号", "明细ID", "用户名", "推荐人", "推荐组", "订单号", "操作类型", "交易类型", "操作金额", "可用余额", "冻结金额", "备注说明", "时间"};
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (recordList != null && recordList.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < recordList.size(); i++) {
				rowNum++;
				if (i != 0 && i % 60000 == 0) {
					sheetCount++;
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles,
							(sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					AccountDetailCustomize accountDetailCustomize = recordList.get(i);

					// 创建相应的单元格
					Cell cell = row.createCell(celLength);

					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					}
					
					// 明细ID
					else if (celLength == 1) {
						cell.setCellValue(accountDetailCustomize.getId());
					}
					// 用户名
					else if (celLength == 2) {
						cell.setCellValue(accountDetailCustomize.getUsername());
					}
					// 推荐人
					else if (celLength == 3) {
						cell.setCellValue(accountDetailCustomize.getReferrerName());
					}
					// 推荐组
					else if (celLength == 4) {
						cell.setCellValue(accountDetailCustomize.getReferrerGroup());
					}
					// 订单号
					else if (celLength == 5) {
						cell.setCellValue(accountDetailCustomize.getNid());
					}
					// 操作类型
					else if (celLength == 6) {
						cell.setCellValue(accountDetailCustomize.getType());
					}
					// 交易类型
					else if (celLength == 7) {
						cell.setCellValue(accountDetailCustomize.getTradeType());
					}
					// 操作金额
					else if (celLength == 8) {
						cell.setCellValue(accountDetailCustomize.getAmount()+"");
					}
					// 可用余额
					else if (celLength == 9) {
						cell.setCellValue(accountDetailCustomize.getBalance()+"");
					}
					// 冻结金额
					else if (celLength == 10) {
						cell.setCellValue(accountDetailCustomize.getFrost()+"");
					}
					//备注说明
					else if (celLength == 11) {
						cell.setCellValue(accountDetailCustomize.getRemark());
					}
					// 时间
					else if (celLength == 12) {
						cell.setCellValue(accountDetailCustomize.getCreateTime());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

	}


    /**
     * 更新
     *
     * @param request
     * @param form
     * @return
     */
    @ResponseBody
    @RequestMapping(AccountManageDefine.UPDATE_BALANCE_ACTION)
    @RequiresPermissions(AccountManageDefine.PERMISSIONS_UPDATE_BALANCE)
    public String updateBalanceAction(HttpServletRequest request, @RequestBody AccountManageBean form) {
        LogUtil.startLog(THIS_CLASS, AccountManageDefine.UPDATE_BALANCE_ACTION);

        JSONObject ret = new JSONObject();

        // 用户ID
        Integer userId = GetterUtil.getInteger(form.getUserId());
        if (Validator.isNull(userId)) {
            ret.put(AccountManageDefine.JSON_STATUS_KEY, AccountManageDefine.JSON_STATUS_NG);
            ret.put(AccountManageDefine.JSON_RESULT_KEY, "更新发生错误,请重新操作!");
            LogUtil.errorLog(THIS_CLASS, AccountManageDefine.UPDATE_BALANCE_ACTION, new Exception("参数不正确[userId=" + userId + "]"));
            return ret.toString();
        }

        // 取得用户在汇付天下的账户信息
        AccountChinapnr accountChinapnr = accountManageService.getChinapnrUserInfo(userId);
        // 用户未开户时,返回错误信息
        if (accountChinapnr == null) {
            ret.put(AccountManageDefine.JSON_STATUS_KEY, AccountManageDefine.JSON_STATUS_NG);
            ret.put(AccountManageDefine.JSON_RESULT_KEY, "用户未开户!");
            LogUtil.errorLog(THIS_CLASS, AccountManageDefine.UPDATE_BALANCE_ACTION, new Exception("参数不正确[userId=" + userId + "]"));
            return ret.toString();
        }

        // IP地址
        String ip = CustomUtil.getIpAddr(request);

        // 调用汇付接口(4.4.2. 余额查询（后台）)
        ChinapnrBean bean = new ChinapnrBean();
        bean.setVersion(ChinaPnrConstant.VERSION_10);
        bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_BALANCE_BG); // 消息类型(必须)
        bean.setUsrCustId(String.valueOf(accountChinapnr.getChinapnrUsrcustid())); // 用户客户号(必须)

        // 写log用参数
        bean.setLogUserId(userId); // 操作者ID
        bean.setLogRemark("余额查询(后台)"); // 备注
        bean.setLogClient("0"); // PC
        bean.setLogIp(ip); // IP地址

        // 调用汇付接口
        ChinapnrBean chinaPnrBean = ChinapnrUtil.callApiBg(bean);

        if (chinaPnrBean == null) {
            LogUtil.errorLog(THIS_CLASS, AccountManageDefine.UPDATE_BALANCE_ACTION, new Exception("调用汇付接口发生错误"));
            ret.put(AccountManageDefine.JSON_STATUS_KEY, AccountManageDefine.JSON_STATUS_NG);
            ret.put(AccountManageDefine.JSON_RESULT_KEY, "更新发生错误,请重新操作!");
            return ret.toString();
        }

        int cnt = 0;
        // 接口返回正常时,执行更新操作
        if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinaPnrBean.getRespCode())) {
            try {
                // 更新处理
                cnt = this.accountManageService.updateAccount(userId, chinaPnrBean.getAvlBal(), chinaPnrBean.getFrzBal());
            } catch (Exception e) {
                LogUtil.errorLog(THIS_CLASS, AccountManageDefine.UPDATE_BALANCE_ACTION, e);
            }
        }

        // 返现成功
        if (cnt > 0) {
            ret.put(AccountManageDefine.JSON_STATUS_KEY, AccountManageDefine.JSON_STATUS_OK);
            ret.put(AccountManageDefine.JSON_RESULT_KEY, "更新操作成功!");
        } else {
            ret.put(AccountManageDefine.JSON_STATUS_KEY, AccountManageDefine.JSON_STATUS_NG);
            ret.put(AccountManageDefine.JSON_RESULT_KEY, "更新发生错误,请重新操作!");
        }

        LogUtil.endLog(THIS_CLASS, AccountManageDefine.UPDATE_BALANCE_ACTION);
        return ret.toString();
    }
    
    /**
     * 取得部门信息
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping("getCrmDepartmentList")
    @ResponseBody
    public String getCrmDepartmentListAction(@RequestBody AccountManageBean form) {
        // 部门
        String[] list = new String[]{};
        if (Validator.isNotNull(form.getUserId())) {
            if (form.getUserId().contains(StringPool.COMMA)) {
                list = form.getUserId().split(StringPool.COMMA);
            } else {
                list = new String[]{form.getUserId()};
            }
        }

        JSONArray ja = this.accountManageService.getCrmDepartmentList(list);
        if (ja != null) {
            return ja.toString();
        }

        return StringUtils.EMPTY;
    }

}
