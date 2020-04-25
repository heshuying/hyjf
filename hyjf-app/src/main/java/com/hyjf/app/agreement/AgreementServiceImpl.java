package com.hyjf.app.agreement;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.web.TenderToCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgreementServiceImpl extends BaseServiceImpl implements AgreementService {

    /**
     * 用户中心债转被出借的协议
     *
     * @return
     */
    @Override
    public Map<String, Object> selectUserCreditContract(CreditAssignedBean tenderCreditAssignedBean) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 获取债转出借信息
        CreditTenderExample creditTenderExample = new CreditTenderExample();
        CreditTenderExample.Criteria creditTenderCra = creditTenderExample.createCriteria();
        creditTenderCra.andAssignNidEqualTo(tenderCreditAssignedBean.getAssignNid()).andBidNidEqualTo(tenderCreditAssignedBean.getBidNid()).andCreditNidEqualTo(tenderCreditAssignedBean.getCreditNid())
                .andCreditTenderNidEqualTo(tenderCreditAssignedBean.getCreditTenderNid());
        List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(creditTenderExample);
        if (creditTenderList != null && creditTenderList.size() > 0) {
            CreditTender creditTender = creditTenderList.get(0);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("creditNid", creditTender.getCreditNid());
            List<TenderToCreditDetailCustomize> tenderToCreditDetailList = tenderCreditCustomizeMapper.selectWebCreditTenderDetail(params);
            if (tenderToCreditDetailList != null && tenderToCreditDetailList.size() > 0) {
                if (tenderToCreditDetailList.get(0).getCreditRepayEndTime() != null) {
                    tenderToCreditDetailList.get(0).setCreditRepayEndTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(tenderToCreditDetailList.get(0).getCreditRepayEndTime())));
                }
                if (tenderToCreditDetailList.get(0).getCreditTime() != null) {
                    try {
                        tenderToCreditDetailList.get(0).setCreditTime(GetDate.formatDate(GetDate.parseDate(tenderToCreditDetailList.get(0).getCreditTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                resultMap.put("tenderToCreditDetail", tenderToCreditDetailList.get(0));
            }
            // 获取借款标的信息
            BorrowExample borrowExample = new BorrowExample();
            BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
            borrowCra.andBorrowNidEqualTo(creditTender.getBidNid());
            List<Borrow> borrow = this.borrowMapper.selectByExample(borrowExample);
            // 获取债转信息
            BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
            BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
            borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(tenderCreditAssignedBean.getCreditNid())).andBidNidEqualTo(tenderCreditAssignedBean.getBidNid())
                    .andTenderNidEqualTo(tenderCreditAssignedBean.getCreditTenderNid());
            List<BorrowCredit> borrowCredit = this.borrowCreditMapper.selectByExample(borrowCreditExample);
            // 获取承接人身份信息
            UsersInfoExample usersInfoExample = new UsersInfoExample();
            UsersInfoExample.Criteria usersInfoCra = usersInfoExample.createCriteria();
            usersInfoCra.andUserIdEqualTo(creditTender.getUserId());
            List<UsersInfo> usersInfo = this.usersInfoMapper.selectByExample(usersInfoExample);
            // 获取承接人平台信息
            UsersExample usersExample = new UsersExample();
            UsersExample.Criteria usersCra = usersExample.createCriteria();
            usersCra.andUserIdEqualTo(creditTender.getUserId());
            List<Users> users = this.usersMapper.selectByExample(usersExample);
            // 获取融资方平台信息
            UsersExample usersBorrowExample = new UsersExample();
            UsersExample.Criteria usersBorrowCra = usersBorrowExample.createCriteria();
            usersBorrowCra.andUserIdEqualTo(borrow.get(0).getUserId());
            List<Users> usersBorrow = this.usersMapper.selectByExample(usersBorrowExample);
            // 获取债转人身份信息
            UsersInfoExample usersInfoExampleCredit = new UsersInfoExample();
            UsersInfoExample.Criteria usersInfoCraCredit = usersInfoExampleCredit.createCriteria();
            usersInfoCraCredit.andUserIdEqualTo(creditTender.getCreditUserId());
            List<UsersInfo> usersInfoCredit = this.usersInfoMapper.selectByExample(usersInfoExampleCredit);
            // 获取债转人平台信息
            UsersExample usersExampleCredit = new UsersExample();
            UsersExample.Criteria usersCraCredit = usersExampleCredit.createCriteria();
            usersCraCredit.andUserIdEqualTo(creditTender.getCreditUserId());
            List<Users> usersCredit = this.usersMapper.selectByExample(usersExampleCredit);
            // 将int类型时间转成字符串
            creditTender.setAddTime(GetDate.times10toStrYYYYMMDD(Integer.valueOf(creditTender.getAddTime())));
            creditTender.setAddip(GetDate.getDateMyTimeInMillis(creditTender.getAssignRepayEndTime()));// 借用ip字段存储最后还款时间
            resultMap.put("creditTender", creditTender);
            if (borrow != null && borrow.size() > 0) {
                if (borrow.get(0).getReverifyTime() != null) {
                    borrow.get(0).setReverifyTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getReverifyTime())));
                }
                if (borrow.get(0).getRepayLastTime() != null) {
                    borrow.get(0).setRepayLastTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getRepayLastTime())));
                }
                resultMap.put("borrow", borrow.get(0));
                // 获取借款人信息
                UsersInfoExample usersInfoExampleBorrow = new UsersInfoExample();
                UsersInfoExample.Criteria usersInfoCraBorrow = usersInfoExampleBorrow.createCriteria();
                usersInfoCraBorrow.andUserIdEqualTo(borrow.get(0).getUserId());
                List<UsersInfo> usersInfoBorrow = this.usersInfoMapper.selectByExample(usersInfoExampleBorrow);
                if (usersInfoBorrow != null && usersInfoBorrow.size() > 0) {
                    if (usersInfoBorrow.get(0).getTruename().length() > 1) {
                        usersInfoBorrow.get(0).setTruename(usersInfoBorrow.get(0).getTruename().substring(0, 1) + "**");
                    }
                    if (usersInfoBorrow.get(0).getIdcard().length() > 8) {
                        usersInfoBorrow.get(0).setIdcard(usersInfoBorrow.get(0).getIdcard().substring(0, 8) + "*****");
                    }
                    resultMap.put("usersInfoBorrow", usersInfoBorrow.get(0));
                }
            }
            if (borrowCredit != null && borrowCredit.size() > 0) {
                resultMap.put("borrowCredit", borrowCredit.get(0));
            }
            if (usersInfo != null && usersInfo.size() > 0) {
                if (usersInfo.get(0).getTruename().length() > 1) {
                    usersInfo.get(0).setTruename(usersInfo.get(0).getTruename().substring(0, 1) + "**");
                }
                if (usersInfo.get(0).getIdcard().length() > 8) {
                    usersInfo.get(0).setIdcard(usersInfo.get(0).getIdcard().substring(0, 8) + "*****");
                }
                resultMap.put("usersInfo", usersInfo.get(0));
            }
            if (usersBorrow != null && usersBorrow.size() > 0) {
                if (usersBorrow.get(0).getUsername().length() > 1) {
                    usersBorrow.get(0).setUsername(usersBorrow.get(0).getUsername().substring(0, 1) + "**");
                }
                resultMap.put("usersBorrow", usersBorrow.get(0));
            }
            if (users != null && users.size() > 0) {
                if (users.get(0).getUsername().length() > 1) {
                    users.get(0).setUsername(users.get(0).getUsername().substring(0, 1) + "**");
                }
                resultMap.put("users", users.get(0));
            }
            if (usersCredit != null && usersCredit.size() > 0) {
                if (usersCredit.get(0).getUsername().length() > 1) {
                    usersCredit.get(0).setUsername(usersCredit.get(0).getUsername().substring(0, 1) + "**");
                }
                resultMap.put("usersCredit", usersCredit.get(0));
            }
            if (usersInfoCredit != null && usersInfoCredit.size() > 0) {
                if (usersInfoCredit.get(0).getTruename().length() > 1) {
                    usersInfoCredit.get(0).setTruename(usersInfoCredit.get(0).getTruename().substring(0, 1) + "**");
                }
                if (usersInfoCredit.get(0).getIdcard().length() > 8) {
                    usersInfoCredit.get(0).setIdcard(usersInfoCredit.get(0).getIdcard().substring(0, 8) + "*****");
                }
                resultMap.put("usersInfoCredit", usersInfoCredit.get(0));
            }
            String phpWebHost = PropUtils.getSystem("hyjf.web.host.php");
            if (StringUtils.isNotEmpty(phpWebHost)) {
                resultMap.put("phpWebHost", phpWebHost);
            } else {
                resultMap.put("phpWebHost", "http://site.hyjf.com");
            }
        }
        return resultMap;
    }


    /**
     * 根据用户ID取得用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public UsersInfo getUsersInfoByUserId(Integer userId) {
        if (userId != null) {
            UsersInfoExample example = new UsersInfoExample();
            example.createCriteria().andUserIdEqualTo(userId);
            List<UsersInfo> usersInfoList = this.usersInfoMapper.selectByExample(example);
            if (usersInfoList != null && usersInfoList.size() > 0) {
                return usersInfoList.get(0);
            }
        }
        return null;
    }


    /**
     * 查询用户汇计划出借明细
     *
     * @param params
     * @return
     * @author pcc
     */
    @Override
    public UserHjhInvistDetailCustomize selectUserHjhInvistDetail(Map<String, Object> params) {
        return hjhPlanCustomizeMapper.selectUserHjhInvistDetail(params);
    }

    @Override
    public List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize) {
        return this.borrowCustomizeMapper.searchBorrowList(borrowCommonCustomize);
    }

    @Override
    public Integer selectBorrowerByBorrowNid(String borrowNid) {
        BorrowExample example = new BorrowExample();
        example.createCriteria().andBorrowNidEqualTo(borrowNid);
        List<Borrow> borrows = borrowMapper.selectByExample(example);
        //不会有空值，借款编号能找到唯一一条记录
        return borrows.get(0).getUserId();
    }


    @Override
    public BigDecimal getAccedeAccount(String accedeOrderId) {
        return hjhPlanCustomizeMapper.getAccdeAcount(accedeOrderId);
    }

    /**
     * 获取债转承接信息
     *
     * @param nid
     * @return
     */
    @Override
    public HjhDebtCreditTender getHjhDebtCreditTender(Integer nid) {
        return hjhDebtCreditTenderMapper.selectByPrimaryKey(nid);
    }

    /**
     * 获取债转信息
     *
     * @param creditNid
     * @return
     */
    @Override
    public HjhDebtCredit getHjhDebtCreditByCreditNid(String creditNid) {
        HjhDebtCreditExample example = new HjhDebtCreditExample();
        HjhDebtCreditExample.Criteria criteria = example.createCriteria();
        criteria.andCreditNidEqualTo(creditNid);
        List<HjhDebtCredit> list = hjhDebtCreditMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

}
