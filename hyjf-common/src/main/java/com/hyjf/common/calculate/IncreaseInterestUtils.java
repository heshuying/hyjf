package com.hyjf.common.calculate;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.hyjf.common.util.CommonUtils;

/**
 * 产品加息预期收益
 */
public class IncreaseInterestUtils {

    /**
     * 
     * 计算加息预期收益
     * @author sunss
     * @param borrow
     * @return
     */
    public static String getIncreaseInterest(String money , String borrowStyle,Integer borrowPeriod,BigDecimal borrowApr) {
        BigDecimal earnings = new BigDecimal("0");
        String version = "2.1.0";
        if (!StringUtils.isBlank(money) && Double.parseDouble(money) >= 0) {
            // 计算本金出借预期收益
            switch (borrowStyle) {
                case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“: 预期收益=出借金额*年化收益÷12*月数；
                    earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(money),
                            borrowApr.divide(new BigDecimal("100")), borrowPeriod)
                            .setScale(2, BigDecimal.ROUND_DOWN);
                    break;
                case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“: 预期收益=出借金额*年化收益÷360*天数；
                    earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(money),
                            borrowApr.divide(new BigDecimal("100")), borrowPeriod)
                            .setScale(2, BigDecimal.ROUND_DOWN);
                    break;
                case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“: 预期收益=出借金额*年化收益÷12*月数；
                    earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(money),
                            borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod)
                            .setScale(2, BigDecimal.ROUND_DOWN);
                    break;
                case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“: 预期收益=出借金额*年化收益÷12*月数；
                    earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(money),
                            borrowApr.divide(new BigDecimal("100")), borrowPeriod)
                            .setScale(2, BigDecimal.ROUND_DOWN);
                    break;
                case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“: 预期收益=出借金额*年化收益÷12*月数；
                    earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(money),
                            borrowApr.divide(new BigDecimal("100")), borrowPeriod)
                            .setScale(2, BigDecimal.ROUND_DOWN);
                    break;
                default:
                    break;
            }
        }
        return CommonUtils.formatAmount(version, earnings);
    }
}
