package com.hyjf.admin.manager.plan.liquidation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.plan.PlanController;
import com.hyjf.admin.manager.plan.PlanDefine;
import com.hyjf.admin.manager.plan.credit.PlanCreditBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.DebtFreeze;
import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtCreditCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtPlanCustomize;

/**
 * 
 * 后台清算功能控制器
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年11月1日
 * @see 下午2:35:56
 */
@Controller
@RequestMapping(value = LiquidationDefine.REQUEST_MAPPING)
public class LiquidationController extends BaseController {

    @Autowired
    private PlanLiquidationService planLiquidationService;

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(LiquidationDefine.INIT)
    @RequiresPermissions(LiquidationDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, @ModelAttribute("PlanLiquidationBean") PlanLiquidationBean form) {
        LogUtil.startLog(LiquidationController.class.toString(), LiquidationDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(LiquidationDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(LiquidationController.class.toString(), LiquidationDefine.INIT);
        return modelAndView;
    }

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(LiquidationDefine.SEARCH_ACTION)
    @RequiresPermissions(LiquidationDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, PlanLiquidationBean form) {
        LogUtil.startLog(PlanController.class.toString(), LiquidationDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(LiquidationDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(PlanController.class.toString(), LiquidationDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, PlanLiquidationBean form) {

        // 计划类型
        // List<DebtPlanConfig> planTypeList = planService.getPlanTypeList();
        // modelAndView.addObject("planTypeList", planTypeList);

        PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
        // 计划编码
        planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
        // 计划状态 发起中；待审核；审核不通过；待开放；募集中；募集完成；锁定中；清算中；还款中；已还款；已流标
        planCommonCustomize.setPlanStatusSrch(form.getPlanStatusSrch());
        // 计划应清算开始时间
        planCommonCustomize.setLiquidateShouldTime(form.getLiquidateShouldTimeStartSrch());
        // 计划应清算结束时间
        planCommonCustomize.setLiquidateShouldTimeEnd(form.getLiquidateShouldTimeEndSrch());
        // 计划实际清算开始时间
        planCommonCustomize.setLiquidateFactTimeStart(form.getLiquidateFactTimeStartSrch());
        // 计划实际清算结束时间
        planCommonCustomize.setLiquidateFactTimeEnd(form.getLiquidateFactTimeEndSrch());
        planCommonCustomize.setSort(form.getSort());
        planCommonCustomize.setCol(form.getCol());

        int count = this.planLiquidationService.countPlanLiquidation(planCommonCustomize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            planCommonCustomize.setLimitStart(paginator.getOffset());
            planCommonCustomize.setLimitEnd(paginator.getLimit());
            List<DebtPlan> recordList = this.planLiquidationService.selectPlanLiquidationList(planCommonCustomize);
            form.setPaginator(paginator);
            modelAndView.addObject("recordList", recordList);
        }
        modelAndView.addObject(PlanDefine.PLAN_FORM, form);
    }

    /**
     * 导出功能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    @RequestMapping(LiquidationDefine.EXPORT_ACTION)
    @RequiresPermissions(LiquidationDefine.PERMISSIONS_EXPORT)
    public void exportAction(HttpServletRequest request, HttpServletResponse response, PlanLiquidationBean form)
        throws Exception {
        LogUtil.startLog(PlanController.class.toString(), LiquidationDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "清算列表";

        PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
        // 计划编码
        planCommonCustomize.setPlanNidSrch(form.getPlanNidSrch());
        // 计划状态 发起中；待审核；审核不通过；待开放；募集中；募集完成；锁定中；清算中；还款中；已还款；已流标
        planCommonCustomize.setPlanStatusSrch(form.getPlanStatusSrch());
        // 计划应清算开始时间
        planCommonCustomize.setLiquidateShouldTime(form.getLiquidateShouldTimeStartSrch());
        // 计划应清算结束时间
        planCommonCustomize.setLiquidateShouldTimeEnd(form.getLiquidateShouldTimeEndSrch());
        // 计划实际清算开始时间
        planCommonCustomize.setLiquidateFactTimeStart(form.getLiquidateFactTimeStartSrch());
        // 计划实际清算结束时间
        planCommonCustomize.setLiquidateFactTimeEnd(form.getLiquidateFactTimeEndSrch());

        List<DebtPlan> resultList = this.planLiquidationService.exportPlanLiquidationList(planCommonCustomize);

        String fileName =
                sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles =
                new String[] { "序号", "智投编号", "服务回报期限", "参考年回报率", "授权服务金额", "应还利息", "应还总额", "清算前年化", "智投可用余额", "冻结金额", "清算到账",
                        "转让进度", "已收服务费", "应清算日期", "实际清算日期", "智投状态" };
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
                    sheet =
                            ExportExcel
                                    .createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }

                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    DebtPlan debtPlan = resultList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 计划编号
                    else if (celLength == 1) {
                        cell.setCellValue(debtPlan.getDebtPlanNid());
                    }
                    // 锁定期
                    else if (celLength == 2) {
                        cell.setCellValue(debtPlan.getDebtLockPeriod());
                    }
                    // 预期年化
                    else if (celLength == 3) {
                        cell.setCellValue(debtPlan.getExpectApr() == null ? "0" : CustomConstants.DF_FOR_VIEW
                                .format(debtPlan.getExpectApr()) + "%");
                    }
                    // 加入金额
                    else if (celLength == 4) {
                        cell.setCellValue(debtPlan.getDebtPlanMoney() == null ? "0" : CustomConstants.DF_FOR_VIEW
                                .format(debtPlan.getDebtPlanMoney()));
                    }
                    // 应还利息
                    else if (celLength == 5) {
                        cell.setCellValue(debtPlan.getRepayAccountInterest() == null ? "0" : CustomConstants.DF_FOR_VIEW
                                .format(debtPlan.getRepayAccountInterest()) );
                    }
                    // 应还总额
                    else if (celLength == 6) {
                        cell.setCellValue(debtPlan.getRepayAccountAll()== null ? "0" : CustomConstants.DF_FOR_VIEW
                                .format(debtPlan.getRepayAccountAll()));
                    }
                    // 清算前年化
                    else if (celLength == 7) {
                        cell.setCellValue(debtPlan.getActualApr()== null ? "0" : CustomConstants.DF_FOR_VIEW
                                .format(debtPlan.getActualApr()) + "%");
                    }
                    // 计划可用余额
                    else if (celLength == 8) {
                        cell.setCellValue(debtPlan.getDebtPlanBalance() == null ? "0" : CustomConstants.DF_FOR_VIEW
                                .format(debtPlan.getDebtPlanBalance()));
                    }
                    // 冻结金额
                    else if (celLength == 9) {
                        cell.setCellValue(debtPlan.getDebtPlanFrost() == null ? "0" : CustomConstants.DF_FOR_VIEW
                                .format(debtPlan.getDebtPlanFrost()));
                    }
                    // 清算到账
                    else if (celLength == 10) {
                        cell.setCellValue(debtPlan.getLiquidateArrivalAmount() == null ? "0" : CustomConstants.DF_FOR_VIEW
                                .format(debtPlan.getLiquidateArrivalAmount()));
                    }
                    // 转让进度
                    else if (celLength == 11) {
                        cell.setCellValue(debtPlan.getLiquidateApr() == null ? "0" : CustomConstants.DF_FOR_VIEW
                                .format(debtPlan.getLiquidateApr()));
                    }
                    // 已收服务费
                    else if (celLength == 12) {
                        cell.setCellValue(debtPlan.getServiceFee() == null ? "0" : CustomConstants.DF_FOR_VIEW
                                .format(debtPlan.getServiceFee()));
                    }
                    // 应清算日期
                    else if (celLength == 13) {
                        cell.setCellValue(debtPlan.getLiquidateShouldTime() == null ? "0" : GetDate.timestamptoStrYYYYMMDD(debtPlan.getLiquidateShouldTime()));
                    }
                    // 实际清算日期
                    else if (celLength == 14) {
                        cell.setCellValue(debtPlan.getLiquidateFactTime() == null ? "0" : GetDate.timestamptoStrYYYYMMDD(debtPlan.getLiquidateFactTime()));
                    }
                    // 计划状态
                    else if (celLength == 15) {
                        if (debtPlan.getDebtPlanStatus() == 5) {
                            cell.setCellValue("锁定中");
                        } else if (debtPlan.getDebtPlanStatus() == 6) {
                            cell.setCellValue("清算中");
                        } else if (debtPlan.getDebtPlanStatus() == 7) {
                            cell.setCellValue("清算中");
                        }
                    }

                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(PlanController.class.toString(), PlanDefine.EXPORT_ACTION);
    }

    /**
     * 
     * 清算操作
     * @author renxingchen
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(LiquidationDefine.PLAN_LIQUIDATION)
    @RequiresPermissions(LiquidationDefine.PERMISSIONS_LIQUIDATION)
    public ModelAndView planLiquidation(HttpServletRequest request, PlanCreditBean form) {
        ModelAndView modelAndView = new ModelAndView(LiquidationDefine.DETAIL_PATH);
        // 查询需要清算的计划
        PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
        // 计划编码
        planCommonCustomize.setPlanNidSrch(form.getPlanNid());
        planCommonCustomize.setPlanStatusSrch("5");
        List<DebtPlan> debtPlans = this.planLiquidationService.selectPlanLiquidationList(planCommonCustomize);
        if (null != debtPlans && !debtPlans.isEmpty()) {
            DebtPlan debtPlan = debtPlans.get(0);
            try {
                // 判断是否是可清算的状态
                if (GetDate.daysBetween(GetDate.timestamptoStrYYYYMMDD(debtPlan.getLiquidateShouldTime()),
                        GetDate.timestamptoStrYYYYMMDD(GetDate.getNowTime10())) >= 0) {// 如果应该清算的时间日期与当前日期一致
                        // 查询当前计划的出借未放款的订单，所属的专属标是否有已经满标的，如果有则不能清算
                        if (this.planLiquidationService.queryFullBorrow(form.getPlanNid()) > 0) {
                            // 写入页面，说明有专属标未放款
                            modelAndView.addObject("message", "该计划有关联专属标的已满标未放款完毕，请等待放款后再进行清算！");
                            createPage(form, modelAndView);
                            return modelAndView;
                        } else {
                            // 更新计划状态为清算中
                            debtPlan.setDebtPlanStatus(6);
                            boolean updateFlage = this.planLiquidationService.updateDebtPlan(debtPlan);
                            if (updateFlage) {
                            // 查询当前计划是否有出借未放款的数据
                            List<DebtInvest> debtInvests =
                                    this.planLiquidationService.selectPlanInvestList(form.getPlanNid(), 0);
                            // 如果有出借未放款的数据
                            if (null != debtInvests && !debtInvests.isEmpty()) {
                                // 回滚标的金额，解冻用户的资金，重新冻结用户资金
                                for (DebtInvest debtInvest : debtInvests) {
                                    updateFlage = tenderBack(debtInvest);
                                    if (updateFlage) {
                                        continue;
                                    } else {
                                        // 写入页面，说明有出借数据解冻失败
                                        modelAndView.addObject("message", "该计划有关联专属标的已满标未放款完毕，请等待放款后再进行清算！");
                                        break;
                                    }
                                }
                            }
                            if (updateFlage) {// 之前的解冻流程没有返回失败
                                // 清算债权加入债转表
                                updateFlage =
                                        this.planLiquidationService.updateLiquidation(form.getPlanNid(),
                                                debtPlan.getActualApr(), debtPlan.getLiquidateShouldTime());
                                if (updateFlage) {
                                    	  createPage(form, modelAndView);
                                } else {// 解冻流程返回失败

                                }
                            } else {

                            }
                        } else {
                        	 modelAndView.addObject("message", "更新计划状态失败,找不到此计划。或者重复提交,智投编号:"+debtPlan.getDebtPlanNid());
                             createPage(form, modelAndView);
                             return modelAndView;
                        }
                       }
                
                } else {
                	 modelAndView.addObject("message", "智投不在可以清算的时间范围,智投编号:"+debtPlan.getDebtPlanNid());
                     createPage(form, modelAndView);
                     return modelAndView;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
        	 modelAndView.addObject("message", "查询不到该清算计划");
             createPage(form, modelAndView);
             return modelAndView; 
        }
        return modelAndView;
    }

    /**
     * 
     * 回滚汇添金内部出借冻结未放款
     * @author renxingchen
     * @param debtInvest
     */
    private boolean tenderBack(DebtInvest debtInvest) {
        boolean flag = false;
        String trxId = debtInvest.getTrxId();// 出借冻结标识
        if (StringUtils.isNotBlank(trxId)) {
            // 解冻操作
            DebtFreeze debtFreeze = this.planLiquidationService.selectDebtFreeze(trxId, null, 0);// 查询冻结记录
            boolean accountListFlag = false;
            BigDecimal accedeBalance = debtInvest.getAccount();
            // 解冻订单号
            String unfreezeOrderId;
            // 解冻订单日期
            String unfreezeOrderDate;
            try {
                boolean unFreezeFlag = false;
                if (null != debtFreeze) {// 解冻
                    unfreezeOrderId = GetOrderIdUtils.getOrderId0(debtFreeze.getUserId());
                    unfreezeOrderDate = GetOrderIdUtils.getOrderDate();
                    unFreezeFlag =
                            this.planLiquidationService.unFreezeOrder(debtFreeze.getUserId(), debtFreeze.getOrderId(),
                                    trxId, debtFreeze.getOrderDate(), unfreezeOrderId, unfreezeOrderDate);
                    if (unFreezeFlag) {// 解冻成功
                        // 设置相应的解冻订单编号
                        debtFreeze.setUnfreezeOrderId(unfreezeOrderId);
                        // 设置相应的解冻订单日期
                        debtFreeze.setUnfreezeOrderDate(unfreezeOrderDate);
                        // 插入冻结交易明细 回滚用户账户 删除出借记录
                        accountListFlag = this.planLiquidationService.updateDebtAccountList(debtFreeze);
                    } else {
                        // 解冻失败 再次尝试3次解冻操作
                        for (int i = 0; i < 3; i++) {
                            unFreezeFlag =
                                    this.planLiquidationService.unFreezeOrder(debtFreeze.getUserId(),
                                            debtFreeze.getOrderId(), trxId, debtFreeze.getOrderDate(), unfreezeOrderId,
                                            unfreezeOrderDate);
                            if (unFreezeFlag) {// 解冻成功
                                // 设置相应的解冻订单编号
                                debtFreeze.setUnfreezeOrderId(unfreezeOrderId);
                                // 设置相应的解冻订单日期
                                debtFreeze.setUnfreezeOrderDate(unfreezeOrderDate);
                                // 插入冻结交易明细 回滚用户账户 删除出借记录
                                accountListFlag = this.planLiquidationService.updateDebtAccountList(debtFreeze);
                                break;
                            }
                        }

                    }
                } else {
                    // TODO
                }
                if (unFreezeFlag) {// 解冻成功
                    // 整合金额
                    DebtPlanAccede planAccede;
                    if (accountListFlag) {
                        // 查询计划冻结可用金额
                        planAccede = this.planLiquidationService.selectPlanAccede(debtInvest.getPlanOrderId());
                        if (null != planAccede && (planAccede.getAccedeBalance().compareTo(new BigDecimal(0))) > 0) {
                            debtFreeze =
                                    this.planLiquidationService
                                            .selectDebtFreeze(null, planAccede.getAccedeOrderId(), 1);
                            if (null != debtFreeze) {// 有需要整合的金额
                                // 解冻
                                unfreezeOrderId = GetOrderIdUtils.getOrderId0(debtFreeze.getUserId());
                                unfreezeOrderDate = GetOrderIdUtils.getOrderDate();
                                boolean unFreezeFlag2 =
                                        this.planLiquidationService.unFreezeOrder(debtFreeze.getUserId(),
                                                debtFreeze.getOrderId(), trxId, debtFreeze.getOrderDate(),
                                                unfreezeOrderId, unfreezeOrderDate);
                                if (unFreezeFlag2) {// 解冻成功
                                    // 设置相应的解冻订单编号
                                    debtFreeze.setUnfreezeOrderId(unfreezeOrderId);
                                    // 设置相应的解冻订单日期
                                    debtFreeze.setUnfreezeOrderDate(unfreezeOrderDate);
                                    // 插入冻结交易明细 回滚用户账户 删除出借记录
                                    accountListFlag = this.planLiquidationService.updateDebtAccountList(debtFreeze);
                                } else { // 解冻失败
                                    for (int i = 0; i < 3; i++) {
                                        unFreezeFlag2 =
                                                this.planLiquidationService.unFreezeOrder(debtFreeze.getUserId(),
                                                        debtFreeze.getOrderId(), trxId, debtFreeze.getOrderDate(),
                                                        unfreezeOrderId, unfreezeOrderDate);
                                        if (unFreezeFlag2) {
                                            // 设置相应的解冻订单编号
                                            debtFreeze.setUnfreezeOrderId(unfreezeOrderId);
                                            // 设置相应的解冻订单日期
                                            debtFreeze.setUnfreezeOrderDate(unfreezeOrderDate);
                                            // 插入冻结交易明细 回滚用户账户 删除出借记录
                                            accountListFlag =
                                                    this.planLiquidationService.updateDebtAccountList(debtFreeze);
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            // TODO
                            flag = true;
                        }
                        
                          //总整合金额
						  if (accountListFlag) {
						  // 整合金额
                          accedeBalance = accedeBalance.add(planAccede.getAccedeBalance());
                          // 冻结金额
                          // 生成冻结订单号 冻结日期
                          String frzzeOrderId =
                                  GetOrderIdUtils.getOrderId0(Integer.valueOf(debtInvest.getUserId()));
                          String frzzeOrderDate = GetOrderIdUtils.getOrderDate();
                          String tenderUsrcustid =
                                  this.planLiquidationService.getAccountChinapnr(debtInvest.getUserId())
                                          .getChinapnrUsrcustid() + "";
                          // 插入日志
                          Boolean logFlag =
                                  this.planLiquidationService.updateBeforeChinaPnR(debtInvest.getPlanNid(),
                                          frzzeOrderId, frzzeOrderDate, debtInvest.getUserId(),
                                          accedeBalance, tenderUsrcustid);
                          if (logFlag) {
                              // 冻结
                              String trxIdFreeze =
                                      this.planLiquidationService.freeze(debtInvest.getUserId(),
                                              accedeBalance, tenderUsrcustid, frzzeOrderId, frzzeOrderDate);
                              if (StringUtils.isNoneBlank(trxIdFreeze)) {
                            	// 更新debt plan accede和debt_plan的可用余额和冻结金额，插入冻结资金明细
                                  logFlag =
                                          this.planLiquidationService.updatefreezeLog(debtInvest.getPlanNid(),
                                                  frzzeOrderId, frzzeOrderDate, debtInvest.getUserId(),
                                                  accedeBalance, tenderUsrcustid, debtInvest.getClient(),
                                                  trxIdFreeze, debtInvest.getPlanOrderId(),planAccede,debtInvest.getBorrowNid());
                              } else {
                                  // TODO
                              }
                          } else {
                              // TODO
                          }
						  } else {
	
	                  }
                        // 回滚redis
                        this.recoverRedis(CustomConstants.DEBT_REDITS +debtInvest.getBorrowNid(), debtInvest.getUserId(), debtInvest.getAccount()
                                + "");
                        // 修改标的borrow_full_status为0
                        this.planLiquidationService.updateDebtBorrowFullStatus(debtInvest.getBorrowNid(),debtInvest.getAccount(),debtInvest.getLoanFee()
                        		,debtInvest.getPlanNid(),debtInvest.getUserId(),debtInvest.getUpdateUserName());
                        flag = true;
                    } else {
                        // TODO
                    }
                } else {
                    return false;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            // TODO
        }
        return flag;
    }

    /**
     * 
     * 回滚redis
     * 
     * @param borrowNid
     * @param userId
     * @param account
     * @author Administrator
     */
    private void recoverRedis(String borrowNid, Integer userId, String account) {
        JedisPool pool = RedisUtils.getPool();
        Jedis jedis = pool.getResource();
        BigDecimal accountBigDecimal = new BigDecimal(account);
        String balanceLast = RedisUtils.get(borrowNid);
        if (StringUtils.isNotBlank(balanceLast)) {
            while ("OK".equals(jedis.watch(borrowNid))) {
                BigDecimal recoverAccount = accountBigDecimal.add(new BigDecimal(balanceLast));
                Transaction tx = jedis.multi();
                tx.set(borrowNid, recoverAccount + "");
                List<Object> result = tx.exec();
                if (result == null || result.isEmpty()) {
                    jedis.unwatch();
                } else {
                    System.out.println("用户:" + userId + "***********************************from redis恢复redis："
                            + account);
                    break;
                }
            }
        }
    }

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(LiquidationDefine.PLAN_LIQUIDATION_DETAIL)
    @RequiresPermissions(LiquidationDefine.PERMISSIONS_INFO)
    public ModelAndView planLiquidationDetail(HttpServletRequest request, PlanCreditBean form) {
        ModelAndView modelAndView = new ModelAndView(LiquidationDefine.DETAIL_PATH);
        createPage(form, modelAndView);
        return modelAndView;
    }

    private void createPage(PlanCreditBean form, ModelAndView modelAndView) {
        // 查询计划信息
        DebtPlanCustomize debtPlanCustomize =
                this.planLiquidationService.selectPlanLanLiquidationDetail(form.getPlanNid());
        modelAndView.addObject("plan", debtPlanCustomize);
        // 查询清算出来的债权信息
        Integer count = this.planLiquidationService.queryDebtCreditCount(form.getPlanNid());
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            form.setPaginator(paginator);
            DebtCreditCustomize debtCreditCustomize = new DebtCreditCustomize();
            BeanUtils.copyProperties(form, debtCreditCustomize);
            debtCreditCustomize.setLimitStart(paginator.getOffset());
            debtCreditCustomize.setLimitEnd(paginator.getLimit());
            List<DebtCreditCustomize> debtCreditCustomizes =
                    this.planLiquidationService.selectDebtCreditForPages(debtCreditCustomize);
            modelAndView.addObject("recordList", debtCreditCustomizes);
            modelAndView.addObject(LiquidationDefine.CREDIT_FORM, form);
            // 计划总计数据
            DebtCreditCustomize creditCustomizeSum =
                    this.planLiquidationService.selectDebtCreditForPagesSum(debtCreditCustomize);
            modelAndView.addObject("liquidationFairValueSum", creditCustomizeSum.getLiquidationFairValueSum());
            modelAndView.addObject("accedeBalanceSum", creditCustomizeSum.getAccedeBalanceSum());
            modelAndView.addObject("accedeFrostSum", creditCustomizeSum.getAccedeFrostSum());
            modelAndView.addObject("liquidatesRepayFrostSum", creditCustomizeSum.getLiquidatesRepayFrostSum());
            modelAndView.addObject("accountReceiveSum", creditCustomizeSum.getAccountReceiveSum());
        } else {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            form.setPaginator(paginator);
            modelAndView.addObject(LiquidationDefine.CREDIT_FORM, form);
        }
    }
    
    /**
     * 导出功能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    @RequestMapping(LiquidationDefine.INFO_EXPORT_ACTION)
    @RequiresPermissions(LiquidationDefine.PERMISSIONS_EXPORT)
    public void infoexportAction(HttpServletRequest request, HttpServletResponse response, PlanLiquidationBean form)
        throws Exception {
        LogUtil.startLog(PlanController.class.toString(), LiquidationDefine.EXPORT_ACTION);
        // 表格sheet名称
        String sheetName = "清算详情列表";

        DebtCreditCustomize debtCreditCustomize = new DebtCreditCustomize();
        debtCreditCustomize.setPlanNid(form.getPlanNidSrch());
        debtCreditCustomize.setPlanOrderId(form.getPlanOrderIdSrh());
        debtCreditCustomize.setUserName(form.getUserName());
        List<DebtCreditCustomize> debtCreditCustomizes =
                this.planLiquidationService.selectDebtCreditForPages(debtCreditCustomize);

        String fileName =
                sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;

        String[] titles =
                new String[] { "序号", "智投订单号", "用户名","授权服务金额", "预期收益", " 清算时公允价值", "服务费率", "计划订单余额", "冻结金额", "清算-项目还款",
                        "收到转让金额", "累计服务费", "转让进度" };
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 生成一个表格
        HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

        if (debtCreditCustomizes != null && debtCreditCustomizes.size() > 0) {

            int sheetCount = 1;
            int rowNum = 0;

            for (int i = 0; i < debtCreditCustomizes.size(); i++) {
                rowNum++;
                if (i != 0 && i % 60000 == 0) {
                    sheetCount++;
                    sheet =
                            ExportExcel
                                    .createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }

                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                	DebtCreditCustomize debtCreditCustomize1 = debtCreditCustomizes.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);

                    // 序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    // 计划订单号
                    else if (celLength == 1) {
                        cell.setCellValue(debtCreditCustomize1.getPlanOrderId());
                    }
                    // 用户名
                    else if (celLength == 2) {
                        cell.setCellValue(debtCreditCustomize1.getUserName());
                    }
                    // 加入金额
                    else if (celLength == 3) {
                        cell.setCellValue(debtCreditCustomize1.getAccedeAccount());
                    }
                    // 预期收益
                    else if (celLength == 4) {
                        cell.setCellValue(debtCreditCustomize1.getRepayInterest());
                    }
                    // 清算时公允价值
                    else if (celLength == 5) {
                    	 cell.setCellValue(debtCreditCustomize1.getLiquidationFairValue()== null ? "0" :debtCreditCustomize1.getLiquidationFairValue());
                    }
                    // 服务费率
                    else if (celLength == 6) {
                    	   cell.setCellValue(debtCreditCustomize1.getServiceFeeRate()== null ? "0" :debtCreditCustomize1.getServiceFeeRate()+"%");
                    }
                    // 计划订单余额
                    else if (celLength == 7) {
                        cell.setCellValue(debtCreditCustomize1.getAccedeBalance() == null ? "0" :debtCreditCustomize1.getAccedeBalance());
                    }
                    // 冻结金额
                    else if (celLength == 8) {
                        cell.setCellValue(debtCreditCustomize1.getAccedeFrost() == null ? "0" : debtCreditCustomize1.getAccedeFrost() );
                    }
                    // 清算-项目还款
                    else if (celLength == 9) {
                        cell.setCellValue(debtCreditCustomize1.getLiquidatesRepayFrost() == null ? "0" : debtCreditCustomize1.getLiquidatesRepayFrost());
                    }
                    // 收到转让金额
                    else if (celLength == 10) {
                        cell.setCellValue(debtCreditCustomize1.getAssignCapital() == null ? "0" : debtCreditCustomize1.getAssignCapital());
                    }
                    // 累计服务费
                    else if (celLength == 11) {
                        cell.setCellValue(debtCreditCustomize1.getServiceFee() == null ? "0" : debtCreditCustomize1.getServiceFee());
                    }
                    // 转让进度 record.assignCapital/record.creditCapital*100 
                    else if (celLength == 12) {
                        cell.setCellValue(debtCreditCustomize1.getAssignCapital()==null||debtCreditCustomize1.getCreditCapital()==null ? "0" : 
                        	(new BigDecimal(debtCreditCustomize1.getAssignCapital()).divide(new BigDecimal(debtCreditCustomize1.getCreditCapital()), 2, BigDecimal.ROUND_DOWN)).toString());
                    }
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);

        LogUtil.endLog(PlanController.class.toString(), PlanDefine.EXPORT_ACTION);
    }
}
