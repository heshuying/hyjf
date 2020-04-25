package com.hyjf.app.newagreement;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyjf.app.agreement.AgreementController;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.web.TenderToCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;

@Service
public class NewAgreementServiceImpl extends BaseServiceImpl implements NewAgreementService {

	/**
	 * 用户中心债转被出借的协议
	 * 
	 * @return
	 */
	@Override
	public JSONObject selectUserCreditContract(NewCreditAssignedBean tenderCreditAssignedBean,Integer userId) {
	    JSONObject resultMap = new JSONObject();
	    
		// 获取债转出借信息
		CreditTenderExample creditTenderExample = new CreditTenderExample();
		CreditTenderExample.Criteria creditTenderCra = creditTenderExample.createCriteria();
		creditTenderCra.andAssignNidEqualTo(tenderCreditAssignedBean.getAssignNid()).andBidNidEqualTo(tenderCreditAssignedBean.getBidNid()).andCreditNidEqualTo(tenderCreditAssignedBean.getCreditNid())
				.andCreditTenderNidEqualTo(tenderCreditAssignedBean.getCreditTenderNid());
		List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(creditTenderExample);
		resultMap.put("orderId", tenderCreditAssignedBean.getAssignNid());
		if (creditTenderList != null && creditTenderList.size() > 0) {
		    DecimalFormat df = CustomConstants.DF_FOR_VIEW;
			CreditTender creditTender = creditTenderList.get(0);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("creditNid", creditTender.getCreditNid());
			params.put("assignNid", creditTender.getAssignNid());
			List<TenderToCreditDetailCustomize> tenderToCreditDetailList = tenderCreditCustomizeMapper.selectWebCreditTenderDetail(params);
			if (tenderToCreditDetailList != null && tenderToCreditDetailList.size() > 0) {
				if (tenderToCreditDetailList.get(0).getCreditRepayEndTime() != null) {
					tenderToCreditDetailList.get(0).setCreditRepayEndTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(tenderToCreditDetailList.get(0).getCreditRepayEndTime())));
				}
				if (tenderToCreditDetailList.get(0).getCreditTime() != null) {
					try {
						tenderToCreditDetailList.get(0).setCreditTime(GetDate.formatDate(GetDate.parseDate(tenderToCreditDetailList.get(0).getCreditTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd"));
					} catch (Exception e) {
					    tenderToCreditDetailList.get(0).setCreditTime(tenderToCreditDetailList.get(0).getCreditTime());
					}
				}
				try {
				    resultMap.put("addTime", GetDate.formatDate(GetDate.parseDate(creditTender.getAssignCreateDate()+"", "yyyyMMdd"), "yyyy-MM-dd"));
				} catch (Exception e) {
				    resultMap.put("addTime", creditTender.getAssignCreateDate());
                }
				resultMap.put("remainderPeriod", tenderToCreditDetailList.get(0).getCreditTerm()+"天， "+
				        tenderToCreditDetailList.get(0).getCreditTime()+" 起，至 "+tenderToCreditDetailList.get(0).getCreditRepayEndTime()+" 止");
				resultMap.put("assignTime", tenderToCreditDetailList.get(0).getCreditTime());
			}
			
			// 获取借款标的信息
			BorrowExample borrowExample = new BorrowExample();
			BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(creditTender.getBidNid());
			List<Borrow> borrow = this.borrowMapper.selectByExample(borrowExample);

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
			resultMap.put("assignCapital", df.format(creditTender.getAssignCapital()));
			resultMap.put("assignPay", df.format(creditTender.getAssignPrice()));
			if (borrow != null && borrow.size() > 0) {
				if (borrow.get(0).getReverifyTime() != null) {
					borrow.get(0).setReverifyTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getReverifyTime())));
				}
				if (borrow.get(0).getRepayLastTime() != null) {
					borrow.get(0).setRepayLastTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getRepayLastTime())));
				}
				resultMap.put("borrowNid", borrow.get(0).getBorrowNid());
				resultMap.put("borrowAccount", df.format(borrow.get(0).getAccount()));
				
				resultMap.put("borrowApr", borrow.get(0).getBorrowApr()+"%");
				resultMap.put("borrowStyle", getBorrowStyle(borrow.get(0).getBorrowStyle()));
				resultMap.put("borrowPeriod", getBorrowPeriod(borrow.get(0).getBorrowStyle(), borrow.get(0).getBorrowPeriod())+
				        "，"+ borrow.get(0).getReverifyTime()+" 起，至  "+
				        borrow.get(0).getRepayLastTime()+" 止");
			}
			
			
		    if (usersInfo != null && usersInfo.size() > 0) {
		        if(userId.equals(usersInfo.get(0).getUserId())){
	                resultMap.put("newCreditTruename", usersInfo.get(0).getTruename());
	                resultMap.put("newCreditIdcard", usersInfo.get(0).getIdcard());
		        } else {
		            String truename=usersInfo.get(0).getTruename();
                    String encryptedTruename=truename.substring(0, 1);
                    for (int i = 0; i < truename.length()-1; i++) {
                        encryptedTruename+="*";
                    }
                    resultMap.put("newCreditTruename", encryptedTruename);
                    String idCard=usersInfo.get(0).getIdcard();
                    String encryptedIdCard=idCard.substring(0, 4);
                    for (int i = 0; i < idCard.length()-4; i++) {
                        encryptedIdCard+="*";
                    }
                    resultMap.put("newCreditIdcard", encryptedIdCard);
		        }
            }
            if (users != null && users.size() > 0) {
                if(userId.equals(users.get(0).getUserId())){
                    resultMap.put("newCreditUsername", users.get(0).getUsername());
                }else{
                    String userName= users.get(0).getUsername();
                    String encryptedUserName=userName.substring(0, 1);
                    for (int i = 0; i < 5; i++) {
                        encryptedUserName+="*";
                    }
                    resultMap.put("newCreditUsername", encryptedUserName);
                }
            }

            if (usersCredit != null && usersCredit.size() > 0) {
                if(userId.equals(usersCredit.get(0).getUserId())){
                    resultMap.put("oldCreditUsername", usersCredit.get(0).getUsername());
                }else{
                    String userName= usersCredit.get(0).getUsername();
                    String encryptedUserName=userName.substring(0, 1);
                    for (int i = 0; i < 5; i++) {
                        encryptedUserName+="*";
                    }
                    resultMap.put("oldCreditUsername", encryptedUserName);
                }
                
            }
            if (usersInfoCredit != null && usersInfoCredit.size() > 0) {
                if(userId.equals(usersCredit.get(0).getUserId())){
                    resultMap.put("oldCreditTruename", usersInfoCredit.get(0).getTruename());
                    resultMap.put("oldCreditIdcard", usersInfoCredit.get(0).getIdcard());
                }else{
                    String truename=usersInfoCredit.get(0).getTruename();
                    String encryptedTruename=truename.substring(0, 1);
                    for (int i = 0; i < truename.length()-1; i++) {
                        encryptedTruename+="*";
                    }
                    resultMap.put("oldCreditTruename", encryptedTruename);
                    String idCard=usersInfoCredit.get(0).getIdcard();
                    String encryptedIdCard=idCard.substring(0, 4);
                    for (int i = 0; i < idCard.length()-4; i++) {
                        encryptedIdCard+="*";
                    }
                    resultMap.put("oldCreditIdcard", encryptedIdCard);
                }
            }
			
			if (usersBorrow != null && usersBorrow.size() > 0) {
			    if(userId.equals(usersBorrow.get(0).getUserId())){
			        resultMap.put("borrowUsername", usersBorrow.get(0).getUsername());
			    } else {
			        String userName= usersBorrow.get(0).getUsername();
                    String encryptedUserName=userName.substring(0, 1);
                    for (int i = 0; i < 5; i++) {
                        encryptedUserName+="*";
                    }
			        resultMap.put("borrowUsername", encryptedUserName);
			    }
			}
			
		}else{
		    resultMap.put("addTime", "");
            resultMap.put("remainderPeriod", "");
            resultMap.put("assignTime", "");
            resultMap.put("assignCapital", "");
            resultMap.put("assignPay", "");
            resultMap.put("orderId", "");
            resultMap.put("borrowNid", "");
            resultMap.put("borrowAccount", "");
            resultMap.put("borrowApr", "");
            resultMap.put("borrowStyle", "");
            resultMap.put("borrowPeriod", "");
            resultMap.put("newCreditTruename", "");
            resultMap.put("newCreditIdcard", "");
            resultMap.put("borrowUsername", "");
            resultMap.put("newCreditUsername", "");
            resultMap.put("oldCreditUsername", "");
            resultMap.put("oldCreditTruename", "");
            resultMap.put("oldCreditIdcard", "");
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
     * 
     * 查询用户汇计划出借明细
     * @author pcc
     * @param params
     * @return
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
	 * @param nid
	 * @return
	 */
	@Override
	public HjhDebtCreditTender getHjhDebtCreditTender(Integer nid) {
		return hjhDebtCreditTenderMapper.selectByPrimaryKey(nid);
	}

	/**
	 * 获取债转信息
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


    @Override
    public JSONObject interServiceLoanAgreement(Integer userId, String tenderNid, String borrowNid) {
        JSONObject jsonObject=new JSONObject();
        //获取标的信息
        BorrowExample borrowExample=new BorrowExample();
        borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
        List<Borrow> borrows=borrowMapper.selectByExample(borrowExample);
        if(borrows==null||borrows.size()==0){
            return jsonObject;  
        }
        Borrow borrow=borrows.get(0);
        //获取用户出借信息
        BorrowTenderExample borrowTenderExample=new BorrowTenderExample();
        borrowTenderExample.createCriteria().andNidEqualTo(tenderNid);
        List<BorrowTender> borrowTenders=borrowTenderMapper.selectByExample(borrowTenderExample);
        if(borrowTenders==null||borrowTenders.size()==0){
            return jsonObject;  
        }
        BorrowTender borrowTender=borrowTenders.get(0);
        //获取借款人信息
        UsersInfo borrowUserInfo=getUsersInfoByUserId(borrow.getUserId());
        if(borrowUserInfo==null){
            return jsonObject; 
        }
        //获取出借人信息
        UsersInfo lendersUserInfo=getUsersInfoByUserId(borrowTender.getUserId());
        if(lendersUserInfo==null){
            return jsonObject; 
        }
        Users lendersUser=getUsers(borrowTender.getUserId());
        if(lendersUser==null){
            return jsonObject; 
        }
        DecimalFormat df = CustomConstants.DF_FOR_VIEW;
        jsonObject.put("tenderNid", tenderNid);
        try {
            jsonObject.put("signingTime", GetDate.formatDate(GetDate.parseDate(borrowTender.getLoanOrderDate(), "yyyyMMdd"), "yyyy-MM-dd"));
        } catch (Exception e) {
            jsonObject.put("signingTime", "待确认");
        }
        
        if(userId.equals(borrowTender.getUserId())){
            jsonObject.put("lendersTrueName", lendersUserInfo.getTruename());
            jsonObject.put("lendersCredentialNo", lendersUserInfo.getIdcard());
            jsonObject.put("lendersUserName", lendersUser.getUsername());  
        }else{
            String truename=lendersUserInfo.getTruename();
            String encryptedTrueName=truename.substring(0, 1);
            for (int i = 0; i < truename.length()-1; i++) {
                encryptedTrueName+="*";
            }
            jsonObject.put("lendersTrueName", encryptedTrueName);
            String idcode=lendersUserInfo.getIdcard();
            String encryptedIdcode=idcode.substring(0, 4);
            for (int i = 0; i < idcode.length()-4; i++) {
                encryptedIdcode+="*";
            }
            jsonObject.put("lendersCredentialNo", encryptedIdcode);
            String userName=lendersUser.getUsername();

            String encryptedUserName=userName.substring(0, 1);
            for (int i = 0; i < 5; i++) {
                encryptedUserName+="*";
            }
            jsonObject.put("lendersUserName", encryptedUserName);
        }
        
        jsonObject.put("borrowTrueName", borrowUserInfo.getTruename().substring(0,1)+(borrowUserInfo.getTruename().length()==2?"*":"**"));
        jsonObject.put("borrowAccount", df.format(borrow.getAccount()));
        jsonObject.put("borrowPeriod", getBorrowPeriod(borrow.getBorrowStyle(),borrow.getBorrowPeriod()));
        jsonObject.put("borrowApr", borrow.getBorrowApr().toString()+"%");
        jsonObject.put("borrowStyle", getBorrowStyle(borrow.getBorrowStyle()));
        jsonObject.put("tenderAccount", df.format(borrowTender.getAccount()));
        jsonObject.put("tenderInterest", df.format(borrowTender.getRecoverAccountInterest()));
        
        return jsonObject;
    }


    private String getBorrowStyle(String borrowStyle) {
        switch (borrowStyle) {
        case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“
            return "按月计息，到期还本还息";
        case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“；
            return "按天计息，到期还本还息";
        case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“；
            return "先息后本";
        case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“；
            return "等额本息";
        case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“；
            return "等额本金";
        default:
            return "按月计息，到期还本还息";
        }
    }


    private String getBorrowPeriod(String borrowStyle, Integer borrowPeriod) {
        if(CalculatesUtil.STYLE_ENDDAY.equals(borrowStyle)){
            return borrowPeriod+"天";
        }else{
            return borrowPeriod+"个月";
        }
    }


    @Override
    public HjhDebtCreditTender getHjhDebtCreditTenderByCreditNid(String assignOrderId) {
        HjhDebtCreditTenderExample example = new HjhDebtCreditTenderExample();
        HjhDebtCreditTenderExample.Criteria criteria = example.createCriteria();
        criteria.andAssignOrderIdEqualTo(assignOrderId);
        List<HjhDebtCreditTender> list = hjhDebtCreditTenderMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }


    @Override
    public BorrowTender getBorrowTenderByNid(String nid) {
        BorrowTenderExample example = new BorrowTenderExample();
        BorrowTenderExample.Criteria criteria = example.createCriteria();
        criteria.andNidEqualTo(nid);
        List<BorrowTender> list = borrowTenderMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }


    @Override
    public CreditTender getCreditTenderByCreditNid(String nid) {
        CreditTenderExample example = new CreditTenderExample();
        CreditTenderExample.Criteria criteria = example.createCriteria();
        criteria.andAssignNidEqualTo(nid);
        List<CreditTender> list = creditTenderMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 获得协议模板图片
     * @param aliasName 别名
     * @return
     */
    @Override
    public NewAgreementResultBean setProtocolImg(String aliasName){
        LogUtil.startLog(NewAgreementServiceImpl.class.getName(), NewAgreementDefine.GOTAGREEMENTPDF_OR_IMG);
        NewAgreementResultBean newAgreementResultBean = new NewAgreementResultBean();
        if (StringUtils.isEmpty(aliasName)) {
            newAgreementResultBean.setStatus("99");
            newAgreementResultBean.setStatusDesc("请求参数非法");
            return newAgreementResultBean;
        }

        //是否在枚举中有定义
        String displayName = ProtocolEnum.getDisplayName(aliasName);
        if (StringUtils.isEmpty(displayName)) {
            newAgreementResultBean.setStatus("99");
            newAgreementResultBean.setStatusDesc("请求参数非法");
            return newAgreementResultBean;
        }

        List<String> url = null;
        String protocolId = null;

        protocolId = RedisUtils.get(RedisConstants.PROTOCOL_TEMPLATE_ALIAS + aliasName);
        if (StringUtils.isEmpty(protocolId)) {

            boolean flag = this.setRedisProtocolTemplate(displayName);
            if (!flag) {
                newAgreementResultBean.setStatus("000");
                newAgreementResultBean.setStatusDesc("成功");
                return newAgreementResultBean;
            }
            protocolId = RedisUtils.get(RedisConstants.PROTOCOL_TEMPLATE_ALIAS + aliasName);
        }

        try {

            url = this.getImgUrlList(protocolId);
            newAgreementResultBean.setStatus("000");
            newAgreementResultBean.setStatusDesc("成功");
            newAgreementResultBean.setRequest(url);
        } catch (Exception e) {
            newAgreementResultBean.setStatus("99");
            newAgreementResultBean.setStatusDesc("数据非法");
        }

        return newAgreementResultBean;
    }



    /**
     * 查询协议图片
     *
     * @param protocolId 协议模版的ID
     * @return
     */
    @Override
    public List<String> getImgUrlList(String protocolId) throws Exception {

        // 拿出来的信息 /hyjfdata/data/pdf/template/1528268728879.pdf&/hyjfdata/data/pdf/template/1528268728879-0, 1, 2, 3, 4
        String templateUrl = RedisUtils.get(RedisConstants.PROTOCOL_TEMPLATE_URL + protocolId);

        if (StringUtils.isEmpty(templateUrl)) {
            throw new Exception("templateUrl is null");
        }

        if (!templateUrl.contains("&")) {
            throw new Exception("templateUrl is null");
        }

        String[] strUrl = templateUrl.split("&");// &之前的是 pdf路径，&之后的是 图片路径

        //图片地址存储的路径是： /hyjfdata/data/pdf/template/1528087341328-0,1,2
        String imgUrl = strUrl[1];
        if (!imgUrl.contains("-")) {
            throw new Exception("templateUrl is null");
        }

        return getJpgJson(imgUrl);
    }

    /**
     * 将图片拆分，配上路径
     *
     * @param imgUrl
     * @return
     */
    public List<String> getJpgJson(String imgUrl) {
        List<String> listImg = new ArrayList<>();
        String[] url = imgUrl.split("-");
        String imgPath = url[0];// /hyjfdata/data/pdf/template/1528087341328
        String[] imgSize = url[1].split(",");// 0,1,2
        for (String str : imgSize) {

            listImg.add(new StringBuilder().append(imgPath).append("/").append(str).append(".jpg").toString());
        }

        return listImg;
    }

    /**
     * 获得对应的协议模板pdf路径
     *
     * @param protocolId 协议模版的ID
     * @return
     */
    @Override
    public String getAgreementPdf(String protocolId) throws Exception {

        // 拿出来的信息 /hyjfdata/data/pdf/template/1528268728879.pdf&/hyjfdata/data/pdf/template/1528268728879-0, 1, 2, 3, 4
        String templateUrl = RedisUtils.get(RedisConstants.PROTOCOL_TEMPLATE_URL + protocolId);

        if (StringUtils.isEmpty(templateUrl)) {
            throw new Exception("templateUrl is null");
        }

        if (!templateUrl.contains("&")) {
            throw new Exception("templateUrl is null");
        }

        String[] strUrl = templateUrl.split("&");// &之前的是 pdf路径，&之后的是 图片路径

        String fileDomainUrl = strUrl[0];

        return fileDomainUrl;
    }

    /**
     * 往Redis中放入协议模板内容
     *
     * @param displayName
     * @return
     */
    @Override
    public boolean setRedisProtocolTemplate(String displayName) {
        ProtocolTemplateExample examplev = new ProtocolTemplateExample();
        ProtocolTemplateExample.Criteria criteria = examplev.createCriteria();
        criteria.andProtocolTypeEqualTo(displayName);
        criteria.andStatusEqualTo(1);
        List<ProtocolTemplate> list = protocolTemplateMapper.selectByExample(examplev);

        if (CollectionUtils.isEmpty(list)) {
            return false;
        }

        ProtocolTemplate protocolTemplate = list.get(0);

        //将协议模板放入redis中
        RedisUtils.set(RedisConstants.PROTOCOL_TEMPLATE_URL + protocolTemplate.getProtocolId(), protocolTemplate.getProtocolUrl() + "&" + protocolTemplate.getImgUrl());
        //获取协议模板前端显示名称对应的别名
        String alias = ProtocolEnum.getAlias(protocolTemplate.getProtocolType());
        if (StringUtils.isNotBlank(alias)) {
            RedisUtils.set(RedisConstants.PROTOCOL_TEMPLATE_ALIAS + alias, protocolTemplate.getProtocolId());//协议 ID放入redis
        }
        return true;
    }

    /**
     * 协议名称 动态获得
     *
     * @return
     */
    //@Override
    public List<ProtocolTemplate> getdisplayNameDynamic() {
        return protocolTemplateMapper.getdisplayNameDynamic();
    }
}
