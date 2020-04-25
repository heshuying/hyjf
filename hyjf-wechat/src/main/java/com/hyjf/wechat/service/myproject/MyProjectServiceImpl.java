package com.hyjf.wechat.service.myproject;

import com.google.common.collect.Maps;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.customize.app.AppAlreadyRepayListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordListCustomize;
import com.hyjf.mybatis.model.customize.web.CurrentHoldObligatoryRightListCustomize;
import com.hyjf.mybatis.model.customize.web.CurrentHoldPlanListCustomize;
import com.hyjf.mybatis.model.customize.web.RepayMentPlanListCustomize;
import com.hyjf.wechat.base.BaseServiceImpl;
import com.hyjf.wechat.controller.user.myAsset.QueryMyProjectVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by cuigq on 2018/2/6.
 */
@Service
public class MyProjectServiceImpl extends BaseServiceImpl implements MyProjectService {

    @Override
    public void selectCurrentHoldObligatoryRightList(String userId, int currentPage, int pageSize, QueryMyProjectVO vo) {
        Map<String, Object> mapParameter = Maps.newHashMap();
        mapParameter.put("userId", userId);

        int total = assetManageCustomizeMapper.selectCurrentHoldObligatoryRightListTotal(mapParameter);
        if (total > 0) {
        	//update by jijun 按照出借时间排序
            mapParameter.put("orderByFlag", "2");
            mapParameter.put("sortBy", "DESC");
            buildQueryPageParam(currentPage, pageSize, mapParameter);
            List<CurrentHoldObligatoryRightListCustomize> lst = assetManageCustomizeMapper.selectCurrentHoldObligatoryRightList(mapParameter);
            for (CurrentHoldObligatoryRightListCustomize customize: lst) {
                String capital = customize.getCapital();
                customize.setCapital(CommonUtils.formatAmount(capital));
                String couponType = customize.getCouponType();
                switch (couponType) {
                case "1":
                    customize.setData("体验金");
                    break;
                case "2":
                    customize.setData("加息券");
                    break;
                case "3":
                    customize.setData("代金券");
                    break;
                default:
                    if("2".equals(customize.getType())){
                        customize.setData("承接债转");
                    } else {
                        customize.setData("");
                    }
                }
                //判断type是否为4（4加息），将couponType设置为4
                if("4".equals(customize.getType())){
                    customize.setCouponType("4");
                    //将"borrowExtraYield": 替换为 "data":
                    customize.setData(customize.getBorrowExtraYield());
                }
            }
            vo.getLstProject().addAll(lst);
        }
        boolean isEnd = pageSize * currentPage >= total;
        vo.setEnd(isEnd);
    }


    @Override
    public void selectRepaymentList(String userId, int currentPage, int pageSize, QueryMyProjectVO vo) {
        Map<String, Object> params = Maps.newConcurrentMap();
        params.put("userId", userId);
        int total = assetManageCustomizeMapper.selectRepaymentListTotal(params);
        if (total > 0) {
            buildQueryPageParam(currentPage, pageSize, params);
            List<AppAlreadyRepayListCustomize> lst = appUserInvestCustomizeMapper.selectAlreadyRepayList(params);
            for (AppAlreadyRepayListCustomize customize: lst) {
                String account = customize.getAccount().replaceAll(",", "");
                String interest = customize.getInterest().replaceAll(",", "");
                customize.setAccount(CommonUtils.formatAmount(account));
                customize.setInterest(CommonUtils.formatAmount(interest));
                customize.setRecoverTime( GetDate.times10toStrYYYYMMDD(Integer.valueOf(customize.getRecoverTime())));
                //判断invest_type是否为3（3加息），将couponType设置为4
                if("3".equals(customize.getInvestType())){
                    customize.setCouponType("4");
                    customize.setLabel(customize.getBorrowExtraYield());
                }
            }
            vo.getLstProject().addAll(lst);

        }
        boolean isEnd = pageSize * currentPage >= total;
        vo.setEnd(isEnd);
    }

    @Override
    public void selectCreditRecordList(String userId, int currentPage, int pageSize,QueryMyProjectVO vo) {
        Map<String, Object> params = Maps.newConcurrentMap();
        params.put("userId", userId);
        int total=appTenderCreditCustomizeMapper.countCreditRecordTotal(params);
        if(total>0){
            buildQueryPageParam(currentPage,pageSize,params);
            List<AppTenderCreditRecordListCustomize> lst= appTenderCreditCustomizeMapper.searchCreditRecordList(params);
            for (AppTenderCreditRecordListCustomize customize: lst) {
                String creditCapital = customize.getCreditCapital();
                //update by jijun 20180427
                creditCapital = creditCapital.replaceAll(",", "");  
                String assigned = customize.getCreditCapitalAssigned().replaceAll(",", "");
                customize.setCreditCapital(CommonUtils.formatAmount(creditCapital));
                customize.setCreditCapitalAssigned(CommonUtils.formatAmount(assigned));
            }
            vo.getLstProject().addAll(lst);
        }
        boolean isEnd = pageSize * currentPage >= total;
        vo.setEnd(isEnd);

    }

    @Override
    public void selectCurrentHoldPlanList(String userId, int currentPage, int pageSize, QueryMyProjectVO vo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", userId);
        int total=assetManageCustomizeMapper.countCurrentHoldPlanTotal(params);
        if(total>0){
        	//update by jijun 20180420
            params.put("orderByFlag", "2");
            params.put("sortBy", "DESC");
            buildQueryPageParam(currentPage,pageSize,params);
            List<CurrentHoldPlanListCustomize> lst=assetManageCustomizeMapper.selectCurrentHoldPlanList(params);
            vo.getLstProject().addAll(lst);
        }

        boolean isEnd = pageSize * currentPage >= total;
        vo.setEnd(isEnd);

    }

    @Override
    public void selectRepayMentPlanList(String userId, int currentPage, int pageSize, QueryMyProjectVO vo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", userId);
        int total=assetManageCustomizeMapper.countRepayMentPlanTotal(params);
        if(total>0){
        	//按照汇款时间倒叙排序 update by jijun 2018/05/03
			params.put("orderByFlag", "3");
			params.put("sortBy", "DESC");
            buildQueryPageParam(currentPage,pageSize,params);
            List<RepayMentPlanListCustomize> recordList = assetManageCustomizeMapper.selectRepayMentPlanList(params);
            //计算实际收益
            for (RepayMentPlanListCustomize record : recordList) {
                if (!"2".equals(record.getType()) && record.getRepayAccountYes() != null && record.getAccedeAccount() != null) {
                    BigDecimal receivedTotal = new BigDecimal(record.getRepayAccountYes().replaceAll(",", "").trim());
                    BigDecimal accedeAccount = new BigDecimal(record.getAccedeAccount().replaceAll(",", "").trim());
                    BigDecimal userHjhInvistDetail = receivedTotal.subtract(accedeAccount);
                    int account = userHjhInvistDetail.compareTo(BigDecimal.ZERO);
                    if (account == -1) {
                        record.setRepayInterestYes(BigDecimal.ZERO.toString());
                    } else {
                        record.setRepayInterestYes(userHjhInvistDetail.toString());
                    }
                }
            }

            vo.getLstProject().addAll(recordList);
        }

        boolean isEnd = pageSize * currentPage >= total;
        vo.setEnd(isEnd);
    }

    private void buildQueryPageParam(int currentPage, int pageSize, Map<String, Object> mapParameter) {
        int offSet = (currentPage - 1) * pageSize;
        if (offSet == 0 || offSet > 0) {
            mapParameter.put("limitStart", offSet);
        }
        if (pageSize > 0) {
            mapParameter.put("limitEnd", pageSize);
        }
    }
}
