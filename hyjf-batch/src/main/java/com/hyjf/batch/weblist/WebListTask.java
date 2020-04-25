package com.hyjf.batch.weblist;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;

public class WebListTask {
    /** 类名 */
    private static final String THIS_CLASS = WebListTask.class.getName();

    /** 债转服务费运行状态 */
    private static int isCreditFeeRun = 0;

    /** 线下充值运行状态 */
    private static int isOutLineRun = 0;

    /** 计算服务费运行状态 */
    private static int isIncomeLoanRun = 0;

    /** 计算管理费运行状态 */
    private static int isIncomeFeeRun = 0;

    /** 充值手续费返还运行状态 */
    private static int isRechargeFeeRun = 0;

    /** 出借推广提成运行状态 */
    private static int isPromoteCommissionRun = 0;

    /** 其他运行状态 */
    private static int isOtherRun = 0;

    @Autowired
    WebListService webListService;

    public void run() {
        // 债转服务费
        creditFee();

        // 线下充值
        outLine();

        // 计算服务费
        incomeLoan();

        // 计算管理费
        incomeFee();

        // 充值手续费返还
        rechargeFee();

        // 出借推广提成
        promoteCommission();

        // 其他
        other();
    }

    /**
     * 债转服务费
     *
     * @return
     */
    private boolean creditFee() {
        String methodName = "creditFee";

        if (isCreditFeeRun == 0) {
            isCreditFeeRun = 1;
            LogUtil.startLog(THIS_CLASS, methodName, "债转服务费任务开始。");

            try {
                webListService.creditFeeService();
                return true;
            } catch (Exception e) {
                LogUtil.errorLog(THIS_CLASS, methodName, "债转服务费发生错误。", e);
            } finally {
                isCreditFeeRun = 0;
            }

            LogUtil.endLog(THIS_CLASS, methodName, "债转服务费任务结束。");

        } else {
            LogUtil.endLog(THIS_CLASS, methodName, "债转服务费任务正在运行。");
        }

        return false;
    }

    /**
     * 线下充值
     *
     * @return
     */
    private boolean outLine() {
        String methodName = "outLine";
        if (isOutLineRun == 0) {
            isOutLineRun = 1;
            LogUtil.startLog(THIS_CLASS, methodName, "线下充值任务开始。");

            try {
                webListService.outLineService();
                return true;
            } catch (Exception e) {
                LogUtil.errorLog(THIS_CLASS, methodName, "线下充值发生错误。", e);
            } finally {
                isOutLineRun = 0;
            }

            LogUtil.endLog(THIS_CLASS, methodName, "线下充值任务结束。");

        } else {
            LogUtil.endLog(THIS_CLASS, methodName, "线下充值任务正在运行。");
        }
        return false;
    }

    /**
     * 计算网站收到情况 （服务费）
     *
     * @return
     */
    private boolean incomeLoan() {
        String methodName = "incomeLoan";
        if (isIncomeLoanRun == 0) {
            isIncomeLoanRun = 1;
            LogUtil.startLog(THIS_CLASS, methodName, "计算服务费任务开始。");

            try {
                webListService.incomeLoanService();
                return true;
            } catch (Exception e) {
                LogUtil.errorLog(THIS_CLASS, methodName, "计算服务费发生错误。", e);
            } finally {
                isIncomeLoanRun = 0;
            }

            LogUtil.endLog(THIS_CLASS, methodName, "计算服务费任务结束。");

        } else {
            LogUtil.endLog(THIS_CLASS, methodName, "计算服务费任务正在运行。");
        }
        return false;
    }

    /**
     * 计算网站收到情况 （管理费）
     *
     * @return
     */
    private boolean incomeFee() {
        String methodName = "incomeFee";
        if (isIncomeFeeRun == 0) {
            isIncomeFeeRun = 1;
            LogUtil.startLog(THIS_CLASS, methodName, "计算管理费任务开始。");

            try {
                webListService.incomeFeeService();
                return true;
            } catch (Exception e) {
                LogUtil.errorLog(THIS_CLASS, methodName, "计算管理费发生错误。", e);
            } finally {
                isIncomeFeeRun = 0;
            }

            LogUtil.endLog(THIS_CLASS, methodName, "计算管理费任务结束。");

        } else {
            LogUtil.endLog(THIS_CLASS, methodName, "计算管理费任务正在运行。");
        }
        return false;
    }

    /**
     * 充值手续费返还
     *
     * @return
     */
    private boolean rechargeFee() {
        String methodName = "rechargeFee";
        if (isRechargeFeeRun == 0) {
            isRechargeFeeRun = 1;
            LogUtil.startLog(THIS_CLASS, methodName, "充值手续费返还任务开始。");

            try {
                webListService.rechargeFeeService();
                return true;
            } catch (Exception e) {
                LogUtil.errorLog(THIS_CLASS, methodName, "充值手续费返还发生错误。", e);
            } finally {
                isRechargeFeeRun = 0;
            }

            LogUtil.endLog(THIS_CLASS, methodName, "充值手续费返还任务结束。");

        } else {
            LogUtil.endLog(THIS_CLASS, methodName, "充值手续费返还任务正在运行。");
        }
        return false;
    }

    /**
     * 出借推广提成
     *
     * @return
     */
    private boolean promoteCommission() {
        String methodName = "promoteCommission";
        if (isPromoteCommissionRun == 0) {
            isPromoteCommissionRun = 1;
            LogUtil.startLog(THIS_CLASS, methodName, "出借推广提成任务开始。");

            try {
                webListService.promoteCommissionService();
                return true;
            } catch (Exception e) {
                LogUtil.errorLog(THIS_CLASS, methodName, "出借推广提成发生错误。", e);
            } finally {
                isPromoteCommissionRun = 0;
            }

            LogUtil.endLog(THIS_CLASS, methodName, "出借推广提成任务结束。");

        } else {
            LogUtil.endLog(THIS_CLASS, methodName, "出借推广提成任务正在运行。");
        }
        return false;
    }

    /**
     * 其他
     *
     * @return
     */
    private boolean other() {
        String methodName = "other";
        if (isOtherRun == 0) {
            isOtherRun = 1;
            LogUtil.startLog(THIS_CLASS, methodName, "其他任务开始。");

            try {
                webListService.otherService();
                return true;
            } catch (Exception e) {
                LogUtil.errorLog(THIS_CLASS, methodName, "其他发生错误。", e);
            } finally {
                isOtherRun = 0;
            }

            LogUtil.endLog(THIS_CLASS, methodName, "其他任务结束。");
        } else {
            LogUtil.endLog(THIS_CLASS, methodName, "其他任务正在运行。");
        }
        return false;
    }
}
