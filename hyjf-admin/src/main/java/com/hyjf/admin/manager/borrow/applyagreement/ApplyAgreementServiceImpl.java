package com.hyjf.admin.manager.borrow.applyagreement;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractBean;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ApplyAgreement;
import com.hyjf.mybatis.model.auto.ApplyAgreementExample;
import com.hyjf.mybatis.model.auto.ApplyAgreementInfo;
import com.hyjf.mybatis.model.auto.ApplyAgreementInfoExample;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecordExample;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditRepayExample;
import com.hyjf.mybatis.model.auto.HjhDebtCreditRepay;
import com.hyjf.mybatis.model.auto.HjhDebtCreditRepayExample;
import com.hyjf.mybatis.model.auto.TenderAgreement;
import com.hyjf.mybatis.model.auto.TenderAgreementExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.admin.BorrowRepayAgreementCustomize;

@Service
public class ApplyAgreementServiceImpl extends BaseServiceImpl implements ApplyAgreementService {
    Logger log = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 垫付协议申请  数目
     * 
     * @return
     */
    @Override
    public Integer countApplyAgreement(ApplyAgreementExample applyAgreementExample) {
        Integer count = this.applyAgreementMapper.countByExample(applyAgreementExample);
        return count;
    }

    /**
     * 垫付协议申请 列表
     * 
     * @return
     */
    @Override
    public List<ApplyAgreement> selectApplyAgreement(ApplyAgreementExample applyAgreementExample) {
        applyAgreementExample.setOrderByClause("create_time Desc");
        List<ApplyAgreement> list = this.applyAgreementMapper.selectByExample(applyAgreementExample);
        if(list!=null && list.size()>0){
            for (ApplyAgreement applyAgreement : list) {
                String tenderNid = ApplyAgreementDefine.DF+"-"+applyAgreement.getRepayPeriod()+"-";
                TenderAgreementExample example = new TenderAgreementExample();
                TenderAgreementExample.Criteria criteria= example.createCriteria();
                //criteria.andStatusEqualTo(3);
                criteria.andTenderNidLike(tenderNid+"%");
                criteria.andBorrowNidEqualTo(applyAgreement.getBorrowNid());
                List<TenderAgreement> tenderAgreements= this.tenderAgreementMapper.selectByExample(example);
                //申请状态 0 全部；1申请失败(hyjf_tender_agreement没有记录)：2申请中；3申请成功
                if(tenderAgreements != null){
                    int tenderAgreementCout = 0;//hyjf_tender_agreement状态为3的数量和hyjf_apply_agreement协议份数相同表示都生成成功
                    for (TenderAgreement tenderAgreement : tenderAgreements) {
                        if(tenderAgreement.getStatus()==1 || tenderAgreement.getStatus()==2 ){
                            applyAgreement.setStatus(2);
                            break;
                        }
                        tenderAgreementCout++;
                       
                    }
                    if(tenderAgreementCout==applyAgreement.getAgreementNumber()){
                        applyAgreement.setStatus(3);
                    }
                }else{
                    applyAgreement.setStatus(1);
                }
            } 
        }
        return list;
    }
    /**
     * 根据主键删除环境
     * 
     * @param recordList
     */
    @Override
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
            applyAgreementMapper.deleteByPrimaryKey(id);
        }
    }
    /**
     * 垫付协议申请 列表
     * 
     * @return
     */
    @Override
    public List<TenderAgreement> selectTenderAgreementByTenderNid(String tenderNid) {
        TenderAgreementExample example = new TenderAgreementExample();
        TenderAgreementExample.Criteria criteria= example.createCriteria();
        criteria.andTenderNidLike(tenderNid+"%");
        List<TenderAgreement> tenderAgreements= this.tenderAgreementMapper.selectByExample(example);
        return tenderAgreements;
    }
    
    /**
     * 垫付协议申请  数目
     * 
     * @return
     */
    @Override
    public Long countBorrowRepay(BorrowRepayAgreementCustomize borrowRepayAgreementCustomize) {
        Long count = this.applyAgreementCustomizeMapper.countBorrowRepay(borrowRepayAgreementCustomize);
        return count;
    }

    /**
     * 垫付协议申请 列表
     * 
     * @return
     */
    @Override
    public List<BorrowRepayAgreementCustomize> selectBorrowRepay(BorrowRepayAgreementCustomize borrowRepayAgreementCustomize) {
        List<BorrowRepayAgreementCustomize> list = this.applyAgreementCustomizeMapper.selectBorrowRepay(borrowRepayAgreementCustomize);
        if(list!=null && list.size()>0){
            for (BorrowRepayAgreementCustomize customize : list) {//判断是否申请过垫付协议
                ApplyAgreementExample applyAgreement = new ApplyAgreementExample();
                ApplyAgreementExample.Criteria criteria= applyAgreement.createCriteria();
                criteria.andBorrowNidEqualTo(customize.getBorrowNid());
                criteria.andRepayPeriodEqualTo(customize.getRepayPeriod());
                Integer count = this.countApplyAgreement(applyAgreement);
                if(count>0){
                    customize.setApplyagreements(1);
                }else {
                    customize.setApplyagreements(0);
                }
            } 
        }
        return list;
    }
    
    /**
     * 垫付协议申请  数目
     * 
     * @return
     */
    @Override
    public Long countBorrowRepayPlan(BorrowRepayAgreementCustomize borrowRepayAgreementCustomize) {
        Long count = this.applyAgreementCustomizeMapper.countBorrowRepayPlan(borrowRepayAgreementCustomize);
        return count;
    }

    /**
     * 垫付协议申请 列表
     * 
     * @return
     */
    @Override
    public List<BorrowRepayAgreementCustomize> selectBorrowRepayPlan(BorrowRepayAgreementCustomize borrowRepayAgreementCustomize) {
       List<BorrowRepayAgreementCustomize> list = this.applyAgreementCustomizeMapper.selectBorrowRepayPlan(borrowRepayAgreementCustomize);
       for (BorrowRepayAgreementCustomize customize : list) {//判断是否申请过垫付协议
           ApplyAgreementExample applyAgreement = new ApplyAgreementExample();
           ApplyAgreementExample.Criteria criteria= applyAgreement.createCriteria();
           criteria.andBorrowNidEqualTo(customize.getBorrowNid());
           criteria.andRepayPeriodEqualTo(customize.getRepayPeriod());
           Integer count = this.countApplyAgreement(applyAgreement);
           if(count>0){
               customize.setApplyagreements(1);
           }else {
               customize.setApplyagreements(0);
           }
       } 
        return list;
    }
    
   

    /**全部转让债转参数集合*/
    @Override
    public JSONObject getAllcreditParamter(CreditRepay creditRepay,FddGenerateContractBean bean,Borrow borrow) {
        JSONObject paramter = new JSONObject();
        /** 标的基本数据 */
        String borrowStyle = borrow.getBorrowStyle();// 还款方式
        Integer borrowPeriod = borrow.getBorrowPeriod();
        String borrowDate = "";
        
        // 是否月标(true:月标, false:天标)
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle);
        if(isMonth){//月表
            borrowDate = borrowPeriod + "个月";
        }else{
            borrowDate = borrowPeriod + "天";
        }
        // 是否分期(true:分期, false:不分期)
        boolean isPlan = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        String  recoverTime = "0";//还款时间
        if(isPlan){//分期repay_last_time-huiyingdai_borrow
            recoverTime = borrow.getRepayLastTime();
        }else{//不分期assign_repay_end_time
            recoverTime = creditRepay.getAssignRepayEndTime()+"";
        }
        if(CustomConstants.BORROW_STYLE_END.equals(borrowStyle)
                ||CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)){//到期还本付息的
            /**转让本金为借款本金，转让价款为本息之和。剩余期限为0*/
            // 转让债权本金
            paramter.put("assignCapital", creditRepay.getAssignRepayCapital()+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay",  creditRepay.getAssignRepayCapital().add(creditRepay.getAssignRepayInterest())+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", 0+"天");
            
        }else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {//先息后本时
            /**还某一期（非最后一期）转让本金是0，转让价款是该期利息金额。剩余转让期间是债权的剩余期限（应还时间-还款时间）。
                            还最后一期转让本金是借款本金，转让价款是借款本金+最后一期利息金额。剩余转让期间为0*/
            if(creditRepay.getRecoverPeriod()!=borrow.getBorrowPeriod()){//非最后一期
                // 转让债权本金
                paramter.put("assignCapital", 0+"");//已承接债转本金
                //转让价款
                paramter.put("assignPay",  creditRepay.getAssignRepayInterest()+"");//已承接垫付利息recover_interest
                // 债转期限assign_repay_yes_time
                paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime, creditRepay.getAssignRepayYesTime()+"")+"天");
            }else{//最后一期
                // 转让债权本金
                paramter.put("assignCapital", creditRepay.getAssignRepayCapital()+"");//已承接债转本金
                //转让价款
                paramter.put("assignPay",  creditRepay.getAssignRepayCapital().add(creditRepay.getAssignRepayInterest())+"");//已承接垫付利息recover_interest
                // 债转期限
                paramter.put("creditTerm", 0+"天");
            }
            
        }else if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)) {//等额本息时
            /**转让期限是原债权的剩余期限，转让本金为本期借款本金，转让价款为本期应还本息之和*/
            // 转让债权本金
            paramter.put("assignCapital", creditRepay.getAssignRepayCapital()+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay",  creditRepay.getAssignRepayCapital().add(creditRepay.getAssignRepayInterest())+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime,creditRepay.getAssignRepayYesTime()+"天"));

        }else{
         // 转让债权本金
            paramter.put("assignCapital", creditRepay.getAssignRepayCapital()+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay", creditRepay.getAssignRepayInterest()+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime,creditRepay.getAssignRepayYesTime()+"天"));
        } 
        // 签署时间
        paramter.put("addTime", GetDate.getDateMyTimeInMillisYYYYMMDD(creditRepay.getAssignRepayYesTime()));
        //转让日期
        paramter.put("creditTime", GetDate.getDateMyTimeInMillisYYYYMMDD(creditRepay.getAssignRepayYesTime()));

        // 标的编号
        paramter.put("borrowNid", borrow.getBorrowNid());
        //编号
        paramter.put("NID", creditRepay.getUniqueNid());
        //借款本金总额
        paramter.put("borrowAccount", borrow.getAccount().toString());
        // 借款利率
        paramter.put("borrowApr", borrow.getBorrowApr() + "%");
        // 还款方式
        paramter.put("borrowStyle", this.getBorrowStyle(borrow.getBorrowStyle()));
        // 借款期限
        paramter.put("borrowPeriod", borrowDate);

        
        // 出让人相关 start-repay_org_user_id
        UsersInfo creditUsersInfo = this.getUsersInfoByUserId(creditRepay.getUserId());

        paramter.put("CreditTruename", creditUsersInfo.getTruename());
        // 出让人身份证号
        paramter.put("CreditIdcard", creditUsersInfo.getIdcard());
        int tenderUserId = bean.getTenderUserId();
        // 承接人用户 start
        UsersInfo tenderUserInfo = this.getUsersInfoByUserId(tenderUserId);
        // 获取承接人平台信息
        Users tenderUser = this.getUsersByUserId(tenderUserId);
        String tenderTrueName = null;
        String tenderIdCard = null;
        tenderTrueName = tenderUserInfo.getTruename();
        tenderIdCard = tenderUserInfo.getIdcard();
        if(tenderUser.getUserType() == 1){
            CorpOpenAccountRecord info = getCorpOpenAccountInfoByUserID(bean.getTenderUserId());
            tenderTrueName = info.getBusiName();
            tenderIdCard = info.getBusiCode();
        }

        // 承接人用户 start
        // 承接人真实姓名
        paramter.put("truename", tenderTrueName);
        // 承接人身份证号
        paramter.put("idcard", tenderIdCard);
        // 承接人用户 end

     // 获取融资方平台信息
        Users borrowUsers = this.getUsers(borrow.getUserId());
        // 借款人用户名
        
        paramter.put("BorrowUsername",borrowUsers.getUsername());
        bean.setTenderUserId(tenderUserId);
        bean.setTenderUserName(tenderUser.getUsername());
        bean.setOrdid(creditRepay.getUniqueNid());//承接订单号
        bean.setAssignOrderId(creditRepay.getUniqueNid());
        bean.setCreditNid(creditRepay.getCreditNid());//债转编号
        bean.setCreditTenderNid(creditRepay.getCreditTenderNid());//原始出借订单号
        return paramter;
    }

    /**部分转让债转参数集合*/
    @Override
    public JSONObject getPartcreditParamter(CreditRepay creditRepay,FddGenerateContractBean bean,Borrow borrow,int assignCapital,int assignPay) {
        JSONObject paramter = new JSONObject();
        /** 标的基本数据 */
        String borrowStyle = borrow.getBorrowStyle();// 还款方式
        Integer borrowPeriod = borrow.getBorrowPeriod();
        String borrowDate = "";
     // 是否分期(true:分期, false:不分期)
        boolean isPlan = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        String  recoverTime = "0";//还款时间
        if(isPlan){//分期repay_last_time-huiyingdai_borrow
            recoverTime = borrow.getRepayLastTime();
        }else{//不分期
            recoverTime = creditRepay.getAssignRepayEndTime()+"";
        }
       /* // 转让债权本金
        paramter.put("assignCapital",assignCapital);//recover_capital-credit_amount
        //转让价款
        paramter.put("assignPay",assignPay);//recover_account-assign_account   ？
*/        if(CustomConstants.BORROW_STYLE_END.equals(borrowStyle)
                ||CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)){//到期还本付息的
            /**转让本金为借款本金，转让价款为本息之和。剩余期限为0*/
            // 转让债权本金
            paramter.put("assignCapital", assignCapital+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay",  assignCapital+assignPay+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", 0+"天");
            
        }else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {//先息后本时
            /**还某一期（非最后一期）转让本金是0，转让价款是该期利息金额。剩余转让期间是债权的剩余期限（应还时间-还款时间）。
                            还最后一期转让本金是借款本金，转让价款是借款本金+最后一期利息金额。剩余转让期间为0*/
            if(creditRepay.getRecoverPeriod()!=borrow.getBorrowPeriod()){//非最后一期
                // 转让债权本金
                paramter.put("assignCapital", 0+"");//已承接债转本金
                //转让价款
                paramter.put("assignPay",  assignPay+"");//已承接垫付利息recover_interest
                // 债转期限
                paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime, creditRepay.getAssignRepayYesTime()+"")+"天");
            }else{//最后一期
                // 转让债权本金
                paramter.put("assignCapital", assignCapital+"");//已承接债转本金
                //转让价款
                paramter.put("assignPay",  assignCapital+assignPay+"");//已承接垫付利息recover_interest
                // 债转期限
                paramter.put("creditTerm", 0+"天");
            }
            
        }else if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)) {//等额本息时
            /**转让期限是原债权的剩余期限，转让本金为本期借款本金，转让价款为本期应还本息之和*/
            // 转让债权本金
            paramter.put("assignCapital", assignCapital+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay",  assignCapital+assignPay+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime,creditRepay.getAssignRepayYesTime()+"")+"天");

        }else{
         // 转让债权本金
            paramter.put("assignCapital", assignCapital+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay", assignPay+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime,creditRepay.getAssignRepayYesTime()+"")+"天");
        }       
        // 签署时间
        paramter.put("addTime", GetDate.getDateMyTimeInMillisYYYYMMDD(creditRepay.getAssignRepayYesTime()));
        //转让日期
        paramter.put("creditTime", GetDate.getDateMyTimeInMillisYYYYMMDD(creditRepay.getAssignRepayYesTime()));

        // 是否月标(true:月标, false:天标)
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle);
        if(isMonth){//月表
            borrowDate = borrowPeriod + "个月";
        }else{
            borrowDate = borrowPeriod + "天";
        }

        // 标的编号
        paramter.put("borrowNid", borrow.getBorrowNid());
        //编号
        paramter.put("NID", creditRepay.getCreditTenderNid());
        //借款本金总额
        paramter.put("borrowAccount", borrow.getAccount().toString());
        // 借款利率
        paramter.put("borrowApr", borrow.getBorrowApr() + "%");
        // 还款方式
        paramter.put("borrowStyle", this.getBorrowStyle(borrow.getBorrowStyle()));
        // 借款期限
        paramter.put("borrowPeriod", borrowDate);

        
        // 出让人相关 start-repay_org_user_id
        UsersInfo creditUsersInfo = this.getUsersInfoByUserId(creditRepay.getUserId());

        paramter.put("CreditTruename", creditUsersInfo.getTruename());
        // 出让人身份证号
        paramter.put("CreditIdcard", creditUsersInfo.getIdcard());
        int tenderUserId = bean.getTenderUserId();
        // 承接人用户 start
        UsersInfo tenderUserInfo = this.getUsersInfoByUserId(tenderUserId);
        // 获取承接人平台信息
        Users tenderUser = this.getUsersByUserId(tenderUserId);
        String tenderTrueName = null;
        String tenderIdCard = null;
        tenderTrueName = tenderUserInfo.getTruename();
        tenderIdCard = tenderUserInfo.getIdcard();
        if(tenderUser.getUserType() == 1){
            CorpOpenAccountRecord info = getCorpOpenAccountInfoByUserID(bean.getTenderUserId());
            tenderTrueName = info.getBusiName();
            tenderIdCard = info.getBusiCode();
        }

        // 承接人用户 start
        // 承接人真实姓名
        paramter.put("truename", tenderTrueName);
        // 承接人身份证号
        paramter.put("idcard", tenderIdCard);
        // 承接人用户 end

     // 获取融资方平台信息
        Users borrowUsers = this.getUsers(borrow.getUserId());
        // 借款人用户名
        
        paramter.put("BorrowUsername",borrowUsers.getUsername());
        bean.setTenderUserId(tenderUserId);
        bean.setTenderUserName(tenderUser.getUsername());
        bean.setOrdid(creditRepay.getAssignNid());//承接订单号
        bean.setAssignOrderId(creditRepay.getAssignNid());
        bean.setCreditNid(creditRepay.getCreditNid());//债转编号
        bean.setCreditTenderNid(creditRepay.getCreditTenderNid());//原始出借订单号
        return paramter;
    }
    /**非转让债转参数集合*/
    @Override
    public JSONObject getNocreditParamter(BorrowRecover borrowRecover,FddGenerateContractBean bean,Borrow borrow) {
        JSONObject paramter = new JSONObject();
       /* paramter.put("assignCapital",borrowRecover.getRecoverCapital());
        paramter.put("assignPay",borrowRecover.getCreditAmount());*/

        /** 标的基本数据 */
        String borrowStyle = borrow.getBorrowStyle();// 还款方式
        Integer borrowPeriod = borrow.getBorrowPeriod();
        String borrowDate = "";
     // 是否分期(true:分期, false:不分期)
        boolean isPlan = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        String  recoverTime = "0";//还款时间
        if(isPlan){//分期repay_last_time-huiyingdai_borrow
            recoverTime = borrow.getRepayLastTime();
        }else{//不分期
            recoverTime = borrowRecover.getRecoverTime()+"";
        }
        if(CustomConstants.BORROW_STYLE_END.equals(borrowStyle)
                ||CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)){//到期还本付息的
            /**转让本金为借款本金，转让价款为本息之和。剩余期限为0*/
            // 转让债权本金
            paramter.put("assignCapital", borrowRecover.getRecoverCapital()+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay",  borrowRecover.getRecoverCapital().add(borrowRecover.getRecoverInterestYes())+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", 0+"天");
            
        }else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {//先息后本时
            /**还某一期（非最后一期）转让本金是0，转让价款是该期利息金额。剩余转让期间是债权的剩余期限（应还时间-还款时间）。
                            还最后一期转让本金是借款本金，转让价款是借款本金+最后一期利息金额。剩余转让期间为0*/
            if(borrowRecover.getRecoverPeriod()!=borrow.getBorrowPeriod()){//非最后一期
                // 转让债权本金
                paramter.put("assignCapital", 0+"");//已承接债转本金
                //转让价款
                paramter.put("assignPay",  borrowRecover.getRecoverInterestYes()+"");//已承接垫付利息recover_interest
                // 债转期限
                paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime, borrowRecover.getRecoverYestime())+"天");
            }else{//最后一期
                // 转让债权本金
                paramter.put("assignCapital", borrowRecover.getRecoverCapital()+"");//已承接债转本金
                //转让价款
                paramter.put("assignPay",  borrowRecover.getRecoverCapital().add(borrowRecover.getRecoverInterestYes())+"");//已承接垫付利息recover_interest
                // 债转期限
                paramter.put("creditTerm", 0+"天");
            }
            
        }else if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)) {//等额本息时
            /**转让期限是原债权的剩余期限，转让本金为本期借款本金，转让价款为本期应还本息之和*/
            // 转让债权本金
            paramter.put("assignCapital", borrowRecover.getRecoverCapital()+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay",  borrowRecover.getRecoverCapital().add(borrowRecover.getRecoverInterestYes())+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime,borrowRecover.getRecoverYestime()+"")+"天");

        }else{
         // 转让债权本金
            paramter.put("assignCapital", borrowRecover.getRecoverCapital()+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay", borrowRecover.getRecoverInterestYes()+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime,borrowRecover.getRecoverYestime()+"")+"天");
        }       
        String recoverYestime = borrowRecover.getRecoverYestime();
        if(StringUtils.isEmpty(recoverYestime)){
            recoverYestime = "0";
        }
        // 签署时间
        paramter.put("addTime", GetDate.getDateMyTimeInMillisYYYYMMDD(Integer.valueOf(recoverYestime)));
        //转让日期
        paramter.put("creditTime", GetDate.getDateMyTimeInMillisYYYYMMDD(Integer.valueOf(recoverYestime)));
        
        // 是否月标(true:月标, false:天标)
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle);
        if(isMonth){//月表
            borrowDate = borrowPeriod + "个月";
        }else{
            borrowDate = borrowPeriod + "天";
        }

        // 标的编号
        paramter.put("borrowNid", borrow.getBorrowNid());
        //编号
        paramter.put("NID", borrowRecover.getNid());//原始标的出借订单号
        //借款本金总额
        paramter.put("borrowAccount", borrow.getAccount().toString());
        // 借款利率
        paramter.put("borrowApr", borrow.getBorrowApr() + "%");
        // 还款方式
        paramter.put("borrowStyle", this.getBorrowStyle(borrow.getBorrowStyle()));
        // 借款期限
        paramter.put("borrowPeriod", borrowDate);
       
        // 出让人相关 start-repay_org_user_id
        UsersInfo creditUsersInfo = this.getUsersInfoByUserId(borrowRecover.getUserId());

        paramter.put("CreditTruename", creditUsersInfo.getTruename());
        // 出让人身份证号
        paramter.put("CreditIdcard", creditUsersInfo.getIdcard());
        int tenderUserId = bean.getTenderUserId();
        // 承接人用户 start
        UsersInfo tenderUserInfo = this.getUsersInfoByUserId(tenderUserId);
        // 获取承接人平台信息
        Users tenderUser = this.getUsersByUserId(tenderUserId);
        String tenderTrueName = null;
        String tenderIdCard = null;
        tenderTrueName = tenderUserInfo.getTruename();
        tenderIdCard = tenderUserInfo.getIdcard();
        if(tenderUser.getUserType() == 1){
            CorpOpenAccountRecord info = getCorpOpenAccountInfoByUserID(bean.getTenderUserId());
            if(info!=null){
                tenderTrueName = info.getBusiName();
                tenderIdCard = info.getBusiCode();
            }
            
        }

        // 承接人用户 start
        // 承接人真实姓名
        paramter.put("truename", tenderTrueName);
        // 承接人身份证号
        paramter.put("idcard", tenderIdCard);
        // 承接人用户 end

     // 获取融资方平台信息
        Users borrowUsers = this.getUsers(borrow.getUserId());
        // 借款人用户名
        
        paramter.put("BorrowUsername",borrowUsers.getUsername());
        bean.setTenderUserId(tenderUserId);
        bean.setTenderUserName(tenderUser.getUsername());
        bean.setOrdid(borrowRecover.getNid());//承接订单号
        bean.setAssignOrderId(borrowRecover.getNid());
        bean.setCreditNid(borrowRecover.getNid());//债转编号
        bean.setCreditTenderNid(borrowRecover.getNid());//债转原始订单号
        return paramter;
    }

    /**
     * 汇计划-全部债转
     */
    @Override
    public JSONObject getAllcreditParamterHjh(HjhDebtCreditRepay hjhDebtCreditRepay,FddGenerateContractBean bean,Borrow borrow) {
        JSONObject paramter = new JSONObject();
        /** 标的基本数据 */
        String borrowStyle = borrow.getBorrowStyle();// 还款方式
        Integer borrowPeriod = borrow.getBorrowPeriod();
        String borrowDate = "";
        // 是否分期(true:分期, false:不分期)
        boolean isPlan = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        String  recoverTime = "0";//还款时间
        if(isPlan){//分期repay_last_time-huiyingdai_borrow
            recoverTime = borrow.getRepayLastTime();
        }else{//不分期
            recoverTime =hjhDebtCreditRepay.getAssignRepayEndTime()+"";
        }
        if(CustomConstants.BORROW_STYLE_END.equals(borrowStyle)
                ||CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)){//到期还本付息的
            /**转让本金为借款本金，转让价款为本息之和。剩余期限为0*/
            // 转让债权本金
            paramter.put("assignCapital", hjhDebtCreditRepay.getRepayCapital()+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay",  hjhDebtCreditRepay.getRepayCapital().add(hjhDebtCreditRepay.getRepayInterest()).add(hjhDebtCreditRepay.getRepayAdvanceInterest()).add(hjhDebtCreditRepay.getRepayLateInterest()).add(hjhDebtCreditRepay.getRepayDelayInterest())+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", 0+"天");
            
        }else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {//先息后本时
            /**还某一期（非最后一期）转让本金是0，转让价款是该期利息金额。剩余转让期间是债权的剩余期限（应还时间-还款时间）。
                            还最后一期转让本金是借款本金，转让价款是借款本金+最后一期利息金额。剩余转让期间为0*/
            if(hjhDebtCreditRepay.getRepayPeriod()!=borrow.getBorrowPeriod()){//非最后一期
                // 转让债权本金
                paramter.put("assignCapital", 0+"");//已承接债转本金
                //转让价款
                paramter.put("assignPay",  hjhDebtCreditRepay.getRepayInterest().add(hjhDebtCreditRepay.getRepayAdvanceInterest()).add(hjhDebtCreditRepay.getRepayLateInterest()).add(hjhDebtCreditRepay.getRepayDelayInterest())+"");//已承接垫付利息recover_interest
                // 债转期限
                paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime, hjhDebtCreditRepay.getAssignRepayYesTime()+"")+"天");
            }else{//最后一期
                // 转让债权本金
                paramter.put("assignCapital", hjhDebtCreditRepay.getRepayCapital()+"");//已承接债转本金
                //转让价款
                paramter.put("assignPay",  hjhDebtCreditRepay.getRepayCapital().add(hjhDebtCreditRepay.getRepayInterest()).add(hjhDebtCreditRepay.getRepayAdvanceInterest()).add(hjhDebtCreditRepay.getRepayLateInterest()).add(hjhDebtCreditRepay.getRepayDelayInterest())+"");//已承接垫付利息recover_interest
                // 债转期限
                paramter.put("creditTerm", 0+"天");
            }
            
        }else if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)) {//等额本息时
            /**转让期限是原债权的剩余期限，转让本金为本期借款本金，转让价款为本期应还本息之和*/
            // 转让债权本金
            paramter.put("assignCapital", hjhDebtCreditRepay.getRepayCapital()+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay",  hjhDebtCreditRepay.getRepayCapital().add(hjhDebtCreditRepay.getRepayInterest()).add(hjhDebtCreditRepay.getRepayAdvanceInterest()).add(hjhDebtCreditRepay.getRepayLateInterest()).add(hjhDebtCreditRepay.getRepayDelayInterest())+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime,hjhDebtCreditRepay.getAssignRepayYesTime()+"")+"天");

        }else{
         // 转让债权本金
            paramter.put("assignCapital", hjhDebtCreditRepay.getRepayCapital()+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay", hjhDebtCreditRepay.getRepayInterest().add(hjhDebtCreditRepay.getRepayAdvanceInterest()).add(hjhDebtCreditRepay.getRepayLateInterest()).add(hjhDebtCreditRepay.getRepayDelayInterest())+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime,hjhDebtCreditRepay.getAssignRepayYesTime()+"")+"天");
        }       
        // 签署时间
        paramter.put("addTime", GetDate.getDateMyTimeInMillisYYYYMMDD(hjhDebtCreditRepay.getAssignRepayYesTime()));
        //转让日期
        paramter.put("creditTime", GetDate.getDateMyTimeInMillisYYYYMMDD(hjhDebtCreditRepay.getAssignRepayYesTime()));

        // 是否月标(true:月标, false:天标)
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle);
        if(isMonth){//月表
            borrowDate = borrowPeriod + "个月";
        }else{
            borrowDate = borrowPeriod + "天";
        }

        // 标的编号
        paramter.put("borrowNid", borrow.getBorrowNid());
        //编号
        paramter.put("NID", hjhDebtCreditRepay.getUniqueNid());//原始标的出借订单号
        //借款本金总额
        paramter.put("borrowAccount", borrow.getAccount().toString());
        // 借款利率
        paramter.put("borrowApr", borrow.getBorrowApr() + "%");
        // 还款方式
        paramter.put("borrowStyle", this.getBorrowStyle(borrow.getBorrowStyle()));
        // 借款期限
        paramter.put("borrowPeriod", borrowDate);

        
        // 出让人相关 start-repay_org_user_id
        UsersInfo creditUsersInfo = this.getUsersInfoByUserId(hjhDebtCreditRepay.getUserId());

        paramter.put("CreditTruename", creditUsersInfo.getTruename());
        // 出让人身份证号
        paramter.put("CreditIdcard", creditUsersInfo.getIdcard());
        int tenderUserId = bean.getTenderUserId();
        // 承接人用户 start
        UsersInfo tenderUserInfo = this.getUsersInfoByUserId(tenderUserId);
        // 获取承接人平台信息
        Users tenderUser = this.getUsersByUserId(tenderUserId);
        String tenderTrueName = null;
        String tenderIdCard = null;
        tenderTrueName = tenderUserInfo.getTruename();
        tenderIdCard = tenderUserInfo.getIdcard();
        if(tenderUser.getUserType() == 1){
            CorpOpenAccountRecord info = getCorpOpenAccountInfoByUserID(bean.getTenderUserId());
            tenderTrueName = info.getBusiName();
            tenderIdCard = info.getBusiCode();
        }

        // 承接人用户 start
        // 承接人真实姓名
        paramter.put("truename", tenderTrueName);
        // 承接人身份证号
        paramter.put("idcard", tenderIdCard);
        // 承接人用户 end

     // 获取融资方平台信息
        Users borrowUsers = this.getUsers(borrow.getUserId());
        // 借款人用户名
        
        paramter.put("BorrowUsername",borrowUsers.getUsername());
        bean.setTenderUserId(tenderUserId);
        bean.setTenderUserName(tenderUser.getUsername());
        bean.setOrdid(hjhDebtCreditRepay.getUniqueNid());//承接订单号
        bean.setAssignOrderId(hjhDebtCreditRepay.getUniqueNid());
        bean.setCreditNid(hjhDebtCreditRepay.getCreditNid());//债转编号
        bean.setCreditTenderNid(hjhDebtCreditRepay.getSellOrderId());//债转原始订单号
        return paramter;
    }

    /**
     * 部分债转-汇计划
     */
    @Override
    public JSONObject getPartcreditParamterHjh(HjhDebtCreditRepay hjhDebtCreditRepay,FddGenerateContractBean bean,Borrow borrow,int assignCapital,int assignPay) {
        JSONObject paramter = new JSONObject();
       /* paramter.put("assignCapital",assignCapital);
        paramter.put("assignPay",assignPay);*/

        /** 标的基本数据 */
        String borrowStyle = borrow.getBorrowStyle();// 还款方式
        Integer borrowPeriod = borrow.getBorrowPeriod();
        String borrowDate = "";
        // 是否分期(true:分期, false:不分期)
        boolean isPlan = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        String  recoverTime = "0";//还款时间
        if(isPlan){//分期repay_last_time-huiyingdai_borrow
            recoverTime = borrow.getRepayLastTime();
        }else{//不分期
            recoverTime =hjhDebtCreditRepay.getAssignRepayEndTime()+"";
        }
        if(CustomConstants.BORROW_STYLE_END.equals(borrowStyle)
                ||CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)){//到期还本付息的
            /**转让本金为借款本金，转让价款为本息之和。剩余期限为0*/
            // 转让债权本金
            paramter.put("assignCapital", assignCapital+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay",  assignPay+assignCapital+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", 0+"天");
            
        }else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {//先息后本时
            /**还某一期（非最后一期）转让本金是0，转让价款是该期利息金额。剩余转让期间是债权的剩余期限（应还时间-还款时间）。
                            还最后一期转让本金是借款本金，转让价款是借款本金+最后一期利息金额。剩余转让期间为0*/
            if(hjhDebtCreditRepay.getRepayPeriod()!=borrow.getBorrowPeriod()){//非最后一期
                // 转让债权本金
                paramter.put("assignCapital", 0+"");//已承接债转本金
                //转让价款
                paramter.put("assignPay",  assignPay+"");//已承接垫付利息recover_interest
                // 债转期限
                paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime, hjhDebtCreditRepay.getAssignRepayYesTime()+"")+"天");
            }else{//最后一期
                // 转让债权本金
                paramter.put("assignCapital", assignCapital+"");//已承接债转本金
                //转让价款
                paramter.put("assignPay",  assignPay+assignCapital+"");//已承接垫付利息recover_interest
                // 债转期限
                paramter.put("creditTerm", 0+"天");
            }
            
        }else if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)) {//等额本息时
            /**转让期限是原债权的剩余期限，转让本金为本期借款本金，转让价款为本期应还本息之和*/
            // 转让债权本金
            paramter.put("assignCapital", assignCapital+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay",   assignPay+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime,hjhDebtCreditRepay.getAssignRepayYesTime()+"")+"天");

        }else{
         // 转让债权本金
            paramter.put("assignCapital", assignCapital+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay", assignPay+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime,hjhDebtCreditRepay.getAssignRepayYesTime()+"")+"天");
        }       

        // 签署时间
        paramter.put("addTime", GetDate.getDateMyTimeInMillisYYYYMMDD(hjhDebtCreditRepay.getAssignRepayYesTime()));
        //转让日期
        paramter.put("creditTime", GetDate.getDateMyTimeInMillisYYYYMMDD(hjhDebtCreditRepay.getAssignRepayYesTime()));

        // 是否月标(true:月标, false:天标)
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle);
        if(isMonth){//月表
            borrowDate = borrowPeriod + "个月";
        }else{
            borrowDate = borrowPeriod + "天";
        }

        // 标的编号
        paramter.put("borrowNid", borrow.getBorrowNid());
        //编号
        paramter.put("NID", hjhDebtCreditRepay.getInvestOrderId());//原始标的出借订单号
        //借款本金总额
        paramter.put("borrowAccount", borrow.getAccount().toString());
        // 借款利率
        paramter.put("borrowApr", borrow.getBorrowApr() + "%");
        // 还款方式
        paramter.put("borrowStyle", this.getBorrowStyle(borrow.getBorrowStyle()));
        // 借款期限
        paramter.put("borrowPeriod", borrowDate);

        
        // 出让人相关 start-repay_org_user_id
        UsersInfo creditUsersInfo = this.getUsersInfoByUserId(hjhDebtCreditRepay.getUserId());

        paramter.put("CreditTruename", creditUsersInfo.getTruename());
        // 出让人身份证号
        paramter.put("CreditIdcard", creditUsersInfo.getIdcard());
        int tenderUserId = bean.getTenderUserId();
        // 承接人用户 start
        UsersInfo tenderUserInfo = this.getUsersInfoByUserId(tenderUserId);
        // 获取承接人平台信息
        Users tenderUser = this.getUsersByUserId(tenderUserId);
        String tenderTrueName = null;
        String tenderIdCard = null;
        tenderTrueName = tenderUserInfo.getTruename();
        tenderIdCard = tenderUserInfo.getIdcard();
        if(tenderUser.getUserType() == 1){
            CorpOpenAccountRecord info = getCorpOpenAccountInfoByUserID(bean.getTenderUserId());
            tenderTrueName = info.getBusiName();
            tenderIdCard = info.getBusiCode();
        }

        // 承接人用户 start
        // 承接人真实姓名
        paramter.put("truename", tenderTrueName);
        // 承接人身份证号
        paramter.put("idcard", tenderIdCard);
        // 承接人用户 end

     // 获取融资方平台信息
        Users borrowUsers = this.getUsers(borrow.getUserId());
        // 借款人用户名
        
        paramter.put("BorrowUsername",borrowUsers.getUsername());
        bean.setTenderUserId(tenderUserId);
        bean.setTenderUserName(tenderUser.getUsername());
        bean.setOrdid(hjhDebtCreditRepay.getUniqueNid());//承接订单号
        bean.setAssignOrderId(hjhDebtCreditRepay.getUniqueNid());
        bean.setCreditNid(hjhDebtCreditRepay.getCreditNid());//债转编号
        bean.setCreditTenderNid(hjhDebtCreditRepay.getSellOrderId());//债转原始订单号
        return paramter;
    }

    /**
     * 汇计划-非债转
     */
    @Override
    public JSONObject getNocreditParamterPlan(BorrowRecoverPlan borrowRecover,FddGenerateContractBean bean,Borrow borrow) {
        JSONObject paramter = new JSONObject();

        /** 标的基本数据 */
        String borrowStyle = borrow.getBorrowStyle();// 还款方式
        Integer borrowPeriod = borrow.getBorrowPeriod();
        String borrowDate = "";
        Object creditTerm = paramter.get("creditTerm");
        String recoverYestime = borrowRecover.getRecoverYestime();
        if(StringUtils.isEmpty(recoverYestime)){
            recoverYestime = "0";
        }
        // 是否月标(true:月标, false:天标)
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle);
        if(isMonth){//月表
            borrowDate = borrowPeriod + "个月";
        }else{
            borrowDate = borrowPeriod + "天";
        }
        // 是否分期(true:分期, false:不分期)
        boolean isPlan = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        String  recoverTime = "0";//还款时间
        if(isPlan){//分期repay_last_time-huiyingdai_borrow
            recoverTime = borrow.getRepayLastTime();
        }else{//不分期
            recoverTime = borrowRecover.getRecoverTime();
        }
        creditTerm = creditTerm + "天";
        if(CustomConstants.BORROW_STYLE_END.equals(borrowStyle)
                ||CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)){//到期还本付息的
            /**转让本金为借款本金，转让价款为本息之和。剩余期限为0*/
            // 转让债权本金
            paramter.put("assignCapital", borrowRecover.getRecoverCapital()+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay",  borrowRecover.getRecoverCapital().add(borrowRecover.getRecoverInterestYes())+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", 0+"天");
            
        }else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {//先息后本时
           
            /**还某一期（非最后一期）转让本金是0，转让价款是该期利息金额。剩余转让期间是债权的剩余期限（应还时间-还款时间）。
                            还最后一期转让本金是借款本金，转让价款是借款本金+最后一期利息金额。剩余转让期间为0*/
            if(borrowRecover.getRecoverPeriod()!=borrow.getBorrowPeriod()){//非最后一期
                // 转让债权本金
                paramter.put("assignCapital", 0+"");//已承接债转本金
                //转让价款
                paramter.put("assignPay",  borrowRecover.getRecoverInterestYes()+"");//已承接垫付利息recover_interest
                // 债转期限
                paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime, borrowRecover.getRecoverYestime())+"天");
            }else{//最后一期
                // 转让债权本金
                paramter.put("assignCapital", borrowRecover.getRecoverCapital()+"");//已承接债转本金
                //转让价款
                paramter.put("assignPay",  borrowRecover.getRecoverCapital().add(borrowRecover.getRecoverInterestYes())+"");//已承接垫付利息recover_interest
                // 债转期限
                paramter.put("creditTerm", 0+"天");
            }
            
        }else if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)) {//等额本息时
            /**转让期限是原债权的剩余期限，转让本金为本期借款本金，转让价款为本期应还本息之和*/
            // 转让债权本金
            paramter.put("assignCapital", borrowRecover.getRecoverCapital()+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay",  borrowRecover.getRecoverCapital().add(borrowRecover.getRecoverInterestYes())+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime,borrowRecover.getRecoverYestime()+"")+"天");

        }else{
         // 转让债权本金
            paramter.put("assignCapital", borrowRecover.getRecoverCapital()+"");//已承接债转本金recover_capital
            //转让价款
            paramter.put("assignPay", borrowRecover.getRecoverInterestYes()+"");//已承接垫付利息recover_interest
            // 债转期限
            paramter.put("creditTerm", DateUtils.differentDaysByString(recoverTime,borrowRecover.getRecoverYestime()+"")+"天");
        }       
        // 签署时间
        paramter.put("addTime", borrowRecover.getRecoverYestime());
     // 签署时间
        paramter.put("addTime", GetDate.getDateMyTimeInMillisYYYYMMDD(Integer.valueOf(recoverYestime)));
        //转让日期
        paramter.put("creditTime", GetDate.getDateMyTimeInMillisYYYYMMDD(Integer.valueOf(recoverYestime)));

        // 标的编号
        paramter.put("borrowNid", borrow.getBorrowNid());
        //编号
        paramter.put("NID", borrowRecover.getNid());//原始标的出借订单号
        //借款本金总额
        paramter.put("borrowAccount", borrow.getAccount().toString());
        // 借款利率
        paramter.put("borrowApr", borrow.getBorrowApr() + "%");
        // 还款方式
        paramter.put("borrowStyle", this.getBorrowStyle(borrow.getBorrowStyle()));
        // 借款期限
        paramter.put("borrowPeriod", borrowDate);

        
        // 出让人相关 start-repay_org_user_id
        UsersInfo creditUsersInfo = this.getUsersInfoByUserId(borrowRecover.getUserId());

        paramter.put("CreditTruename", creditUsersInfo.getTruename());
        // 出让人身份证号
        paramter.put("CreditIdcard", creditUsersInfo.getIdcard());
        int tenderUserId = bean.getTenderUserId();
        // 承接人用户 start
        UsersInfo tenderUserInfo = this.getUsersInfoByUserId(tenderUserId);
        // 获取承接人平台信息
        Users tenderUser = this.getUsersByUserId(tenderUserId);
        String tenderTrueName = null;
        String tenderIdCard = null;
        tenderTrueName = tenderUserInfo.getTruename();
        tenderIdCard = tenderUserInfo.getIdcard();
        if(tenderUser.getUserType() == 1){
            CorpOpenAccountRecord info = getCorpOpenAccountInfoByUserID(bean.getTenderUserId());
            if(info!=null){
                tenderTrueName = info.getBusiName();
                tenderIdCard = info.getBusiCode();
            }
            
        }

        // 承接人用户 start
        // 承接人真实姓名
        paramter.put("truename", tenderTrueName);
        // 承接人身份证号
        paramter.put("idcard", tenderIdCard);
        // 承接人用户 end

     // 获取融资方平台信息
        Users borrowUsers = this.getUsers(borrow.getUserId());
        // 借款人用户名
        
        paramter.put("BorrowUsername",borrowUsers.getUsername());
        bean.setTenderUserId(tenderUserId);
        bean.setTenderUserName(tenderUser.getUsername());
        bean.setOrdid(borrowRecover.getNid());//承接订单号
        bean.setAssignOrderId(borrowRecover.getNid());
        bean.setCreditNid(borrowRecover.getNid());//债转编号
        bean.setCreditTenderNid(borrowRecover.getNid());//债转原始订单号
        return paramter;
    }

    @Override
    public List<HjhDebtCreditRepay> selectHjhDebtCreditRepayByExample(String nid,int repay_period) {
        HjhDebtCreditRepayExample example = new HjhDebtCreditRepayExample();
        HjhDebtCreditRepayExample.Criteria crt = example.createCriteria();
        crt.andInvestOrderIdEqualTo(nid);
        crt.andRepayPeriodEqualTo(repay_period);
        crt.andDelFlagEqualTo(0);
        List<HjhDebtCreditRepay> hjhDebtCreditRepays = this.hjhDebtCreditRepayMapper.selectByExample(example);
        return hjhDebtCreditRepays;
    }
    
    @Override
    public List<CreditRepay> selectCreditRepayByExample(String nid,int repay_period) {
        CreditRepayExample creditRepayExample = new CreditRepayExample();
        CreditRepayExample.Criteria creditRepayCra = creditRepayExample.createCriteria();
        creditRepayCra.andCreditTenderNidEqualTo(nid);
        creditRepayCra.andRecoverPeriodEqualTo(repay_period);
        creditRepayCra.andStatusEqualTo(1);
        List<CreditRepay> list = this.creditRepayMapper.selectByExample(creditRepayExample);
        return list;
    }
    
    /**
     * 获取还款方式
     * @param borrowStyle
     * @return
     */
    private  String  getBorrowStyle(String borrowStyle){
        BorrowStyleExample example = new BorrowStyleExample();
        example.createCriteria().andNidEqualTo(borrowStyle);
        List<BorrowStyle> borrowStyles = this.borrowStyleMapper.selectByExample(example);

        if(borrowStyles != null && borrowStyles.size()>0){
            return borrowStyles.get(0).getName();
        }
        return "";
    }
    /**
     * 获取企业用户信息
     * @param tenderUserId
     * @return
     */
    private CorpOpenAccountRecord getCorpOpenAccountInfoByUserID(Integer tenderUserId) {

        CorpOpenAccountRecordExample example = new CorpOpenAccountRecordExample();
        example.createCriteria().andUserIdEqualTo(tenderUserId);
        List<CorpOpenAccountRecord> openAccountRecords = this.corpOpenAccountRecordMapper.selectByExample(example);
        if(openAccountRecords != null && openAccountRecords.size() > 0){
            return openAccountRecords.get(0);
        }
        return null;
    }
    /**
     * 保存垫付协议申请-协议生成详情
     * @param applyAgreementInfo
     * @return
     */
    public int saveApplyAgreementInfo(ApplyAgreementInfo applyAgreementInfo) {

        ApplyAgreementInfoExample example = new ApplyAgreementInfoExample();
        example.createCriteria().andContractIdEqualTo(applyAgreementInfo.getContractId());
        List<ApplyAgreementInfo> openAccountRecords = this.applyAgreementInfoMapper.selectByExample(example);
        if(openAccountRecords != null && openAccountRecords.size() > 0){
            applyAgreementInfo.setId(openAccountRecords.get(0).getId());
            applyAgreementInfo.setUpdateTime(GetDate.getNowTime10());
            applyAgreementInfo.setUpdateTime(openAccountRecords.get(0).getCreateTime());
            return this.applyAgreementInfoMapper.updateByPrimaryKey(applyAgreementInfo);
            
        }else {
            applyAgreementInfo.setCreateTime(GetDate.getNowTime10());
            return this.applyAgreementInfoMapper.insert(applyAgreementInfo);
        }
    }
    private Users getUsers(Integer userId) {
        return usersMapper.selectByPrimaryKey(userId);
    }

    /**
     * 新增
     */
    @Override
    public int insert(ApplyAgreement record) {
        return this.applyAgreementMapper.insert(record);
    }
    
    public int updateByPrimaryKey(ApplyAgreement record){
        return this.applyAgreementMapper.updateByPrimaryKey(record);
    }
}
