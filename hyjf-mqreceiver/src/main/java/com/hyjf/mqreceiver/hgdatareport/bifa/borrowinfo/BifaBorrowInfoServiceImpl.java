/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 *
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:43:49
 * Modification History:
 * Modified by :
 */

package com.hyjf.mqreceiver.hgdatareport.bifa.borrowinfo;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.dao.BifaBorrowInfoDao;
import com.hyjf.mongo.hgdatareport.entity.BifaBorrowInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.BifaBorrowStatusEntity;
import com.hyjf.mqreceiver.hgdatareport.BaseHgDateReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.bifa.borrowstatus.BifaBorrowStatusService;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author liubin
 */

@Service
public class BifaBorrowInfoServiceImpl extends BaseHgDateReportServiceImpl implements BifaBorrowInfoService {

    Logger _log = LoggerFactory.getLogger(BifaBorrowInfoServiceImpl.class);

    private String thisMessNameBorrowInfo = "散标信息上报";
    private String logHeaderBorrowInfo = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessNameBorrowInfo + "】";

    private String thisMessNameStatusUpdate = "产品状态更新信息上报";
    private String logHeaderBorrowStatusUpdate = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessNameStatusUpdate + "】";

    @Autowired
    BifaBorrowInfoDao bifaBorrowInfoDao;
    @Autowired
    BifaBorrowStatusService bifaBorrowStatusService;

    @Override
    public BifaBorrowInfoEntity getBifaBorrowInfoFromMongoDB(String borrowNid) {
        return bifaBorrowInfoDao.findOne(new Query(Criteria.where("source_product_code").is(borrowNid)));
    }

    @Override
    public Borrow selectBorrowInfo(String borrowNid) {
        BorrowExample borrowExample = new BorrowExample();
        BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
        //只获取 status等于4 状态为还款中
        borrowCra.andBorrowNidEqualTo(borrowNid);
        List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
        if(CollectionUtils.isNotEmpty(borrowList)){
            return borrowList.get(0);
        }
        return null;
    }

    @Override
    public List<BorrowCarinfo> selectBorrowCarInfo(String borrowNid) {
        BorrowCarinfoExample example=new BorrowCarinfoExample();
        BorrowCarinfoExample.Criteria cri = example.createCriteria();
        cri.andBorrowNidEqualTo(borrowNid);
        return this.borrowCarinfoMapper.selectByExample(example);
    }

    @Override
    public List<BorrowHouses> selectBorrowHouseInfo(String borrowNid) {
        BorrowHousesExample example = new BorrowHousesExample();
        BorrowHousesExample.Criteria criteria = example.createCriteria();
        criteria.andBorrowNidEqualTo(borrowNid);
        return this.borrowHousesMapper.selectByExample(example);
    }

    /**
     * 数据转换
     * @param borrow
     * @param borrowRepay
     * @param borrowCarsinfo
     * @param borrowHouses
     * @param bifaBorrowInfoEntity
     * @return
     */
    @Override
    public boolean convertBifaBorrowInfo(Borrow borrow, Map<String, String> borrowUserInfo,BorrowRepay borrowRepay,List<BorrowCarinfo> borrowCarsinfo,
                                         List<BorrowHouses> borrowHouses, BifaBorrowInfoEntity bifaBorrowInfoEntity){

        try {
            bifaBorrowInfoEntity.setProduct_reg_type("01");
            bifaBorrowInfoEntity.setProduct_name(borrow.getProjectName());
            bifaBorrowInfoEntity.setProduct_mark(this.convertProductMark(borrow.getProjectType()));
            bifaBorrowInfoEntity.setSource_code(SOURCE_CODE);
            bifaBorrowInfoEntity.setSource_product_code(borrow.getBorrowNid());
            bifaBorrowInfoEntity.setBorrow_sex(borrowUserInfo.get("sex"));
            bifaBorrowInfoEntity.setAmount(String.valueOf(borrow.getAccount()));
            bifaBorrowInfoEntity.setRate(this.convertRateBorrow(borrow.getBorrowApr()));
            bifaBorrowInfoEntity.setTerm_type(this.convertTermType(borrow.getBorrowStyle()));
            bifaBorrowInfoEntity.setTerm(String.valueOf(borrow.getBorrowPeriod()));
            bifaBorrowInfoEntity.setPay_type(this.convertPayType(borrow.getBorrowStyle()));
            //服务费=放款服务费+还款服务费
            bifaBorrowInfoEntity.setService_cost(this.getServiceCost(borrow.getBorrowNid()));
            bifaBorrowInfoEntity.setRisk_margin("0");
            bifaBorrowInfoEntity.setLoan_type(this.convertLoanType(borrow.getAssetAttributes()));
            bifaBorrowInfoEntity.setLoan_credit_rating(borrow.getBorrowLevel());
            bifaBorrowInfoEntity.setSecurity_info("");//留空不报送
            bifaBorrowInfoEntity.setCollateral_desc(this.convertCollateralDesc(borrowCarsinfo,borrowHouses));
            bifaBorrowInfoEntity.setCollateral_info(this.convertCollateralInfo(borrowCarsinfo,borrowHouses));
            bifaBorrowInfoEntity.setOverdue_limmit("到期还款日当天24点未提交还款的标的");
            bifaBorrowInfoEntity.setBad_debt_limmit("3月");
            bifaBorrowInfoEntity.setAmount_limmts(String.valueOf(borrow.getTenderAccountMin()));
            bifaBorrowInfoEntity.setAmount_limmtl(String.valueOf(borrow.getTenderAccountMax()));
            bifaBorrowInfoEntity.setAllow_transfer("0");
            bifaBorrowInfoEntity.setClose_limmit("0");
            bifaBorrowInfoEntity.setSecurity_type(this.convertSecurityType(borrow.getAssetAttributes()));
            bifaBorrowInfoEntity.setProject_source("合作机构推荐");
            bifaBorrowInfoEntity.setSource_product_url(MessageFormat.format(SOURCE_PRODUCT_URL_BORROW,borrow.getBorrowNid()));
            bifaBorrowInfoEntity.setBorrow_name_idcard_digest(selectUserIdToSHA256(null, borrowUserInfo.get("trueName"),borrowUserInfo.get("idCard")).getSha256());
            Date currDate =GetDate.getDate();
            bifaBorrowInfoEntity.setCreateTime(currDate);
            bifaBorrowInfoEntity.setUpdateTime(currDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * 抵押标:报送抵押
     * 质押标:报送质押
     * 无抵押内容/质押内容 或者其他值填保证
     * @return
     */
    private String convertSecurityType(Integer assetAttributes) {
        if (assetAttributes ==null || assetAttributes==0 || assetAttributes==1){
            return "抵押";
        } else if (assetAttributes==2){
            return "质押";
        } else {
            return "保证";
        }
	}

	/**
     * 数据转换
     * @param borrow
     * @param usersInfo
     * @param borrowRepay
     * @param borrowCarsinfo
     * @param borrowHouses
     * @param bifaBorrowInfoEntity
     * @return
     */
    @Override
    public boolean convertBifaBorrowInfo(Borrow borrow, UsersInfo usersInfo,BorrowRepay borrowRepay,List<BorrowCarinfo> borrowCarsinfo,
                                         List<BorrowHouses> borrowHouses, BifaBorrowInfoEntity bifaBorrowInfoEntity){

        try {
            bifaBorrowInfoEntity.setProduct_reg_type("01");
            bifaBorrowInfoEntity.setProduct_name(borrow.getProjectName());
            bifaBorrowInfoEntity.setProduct_mark(this.convertProductMark(borrow.getProjectType()));
            bifaBorrowInfoEntity.setSource_code(SOURCE_CODE);
            bifaBorrowInfoEntity.setSource_product_code(borrow.getBorrowNid());
            bifaBorrowInfoEntity.setBorrow_sex(this.convertSex(usersInfo.getSex()));
            bifaBorrowInfoEntity.setAmount(String.valueOf(borrow.getAccount()));
            bifaBorrowInfoEntity.setRate(this.convertRateBorrow(borrow.getBorrowApr()));
            bifaBorrowInfoEntity.setTerm_type(this.convertTermType(borrow.getBorrowStyle()));
            bifaBorrowInfoEntity.setTerm(String.valueOf(borrow.getBorrowPeriod()));
            bifaBorrowInfoEntity.setPay_type(this.convertPayType(borrow.getBorrowStyle()));
            //服务费=放款服务费+还款服务费
            bifaBorrowInfoEntity.setService_cost(this.getServiceCost(borrow.getBorrowNid()));
            bifaBorrowInfoEntity.setRisk_margin("0");
            bifaBorrowInfoEntity.setLoan_type(this.convertLoanType(borrow.getAssetAttributes()));
            bifaBorrowInfoEntity.setLoan_credit_rating(borrow.getBorrowLevel());
            bifaBorrowInfoEntity.setSecurity_info("");//留空不报送
            bifaBorrowInfoEntity.setCollateral_desc(this.convertCollateralDesc(borrowCarsinfo,borrowHouses));
            bifaBorrowInfoEntity.setCollateral_info(this.convertCollateralInfo(borrowCarsinfo,borrowHouses));
            bifaBorrowInfoEntity.setOverdue_limmit("到期还款日当天24点未提交还款的标的");
            bifaBorrowInfoEntity.setBad_debt_limmit("3月");
            bifaBorrowInfoEntity.setAmount_limmts(String.valueOf(borrow.getTenderAccountMin()));
            bifaBorrowInfoEntity.setAmount_limmtl(String.valueOf(borrow.getTenderAccountMax()));
            bifaBorrowInfoEntity.setAllow_transfer("0");
            bifaBorrowInfoEntity.setClose_limmit("0");
            bifaBorrowInfoEntity.setSecurity_type(this.convertSecurityType(borrow.getAssetAttributes()));
            bifaBorrowInfoEntity.setProject_source("合作机构推荐");
            bifaBorrowInfoEntity.setSource_product_url(MessageFormat.format(SOURCE_PRODUCT_URL_BORROW,borrow.getBorrowNid()));
            bifaBorrowInfoEntity.setBorrow_name_idcard_digest(selectUserIdToSHA256(null, usersInfo.getTruename(),usersInfo.getIdcard()).getSha256());
            Date currDate =GetDate.getDate();
            bifaBorrowInfoEntity.setCreateTime(currDate);
            bifaBorrowInfoEntity.setUpdateTime(currDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 担保方式转换
     * @param assetAttributes
     * @return
     */
    public String convertLoanType(Integer assetAttributes) {
        String result = "";
        if (assetAttributes ==null || assetAttributes==0 || assetAttributes==1 || assetAttributes==2){
            result = "抵质押";
        }else if (assetAttributes ==3){
            result = "信用";
        }
        return result;
    }
    
    /**
     * 月利率
     * @param borrowApr
     * @return
     */
    private String convertRateBorrow(BigDecimal borrowApr) {
        //12期 百分号转小数
        BigDecimal bd12 = new BigDecimal("1200");
        BigDecimal divide =borrowApr.divide(bd12,6,RoundingMode.DOWN);
        return divide.toString();
    }


    /**
     * 服务费=放款服务费+还款服务费
     */
    private String getServiceCost(String borrowNid) {
        return this.borrowRecoverCustomizeMapper.selectServiceCostSum(borrowNid).toString();
    }


    /**
     * 散標數據上報北互金
     * @param data
     * @return
     */
    @Override
    public boolean insertReportData(BifaBorrowInfoEntity data) {
        bifaBorrowInfoDao.insert(data);
        return true;
    }

    /**
     * 抵押/质押物、估值、平均处置周期转换
     * @param borrowCarsinfo
     * @param borrowHouses
     * @return
     */
    private String convertCollateralInfo(List<BorrowCarinfo> borrowCarsinfo, List<BorrowHouses> borrowHouses) {
        BigDecimal carsTotalPrice = new BigDecimal("0");
        BigDecimal housesTotalPrice = new BigDecimal("0");
        StringBuffer result = new StringBuffer("");
        if (CollectionUtils.isNotEmpty(borrowCarsinfo)){
            for (BorrowCarinfo carinfo : borrowCarsinfo) {
                carsTotalPrice = carsTotalPrice.add(carinfo.getToprice());
            }
        }

        if (CollectionUtils.isNotEmpty(borrowHouses)){
            for (BorrowHouses house:borrowHouses) {
                housesTotalPrice = housesTotalPrice.add(new BigDecimal(house.getHousesToprice()));
            }
        }

        if (!carsTotalPrice.equals(BigDecimal.ZERO)){
            result.append("车辆评估价(元):"+carsTotalPrice.toString());
        }
        if (!housesTotalPrice.equals(BigDecimal.ZERO)){
            if (result.length()==0){
                result.append("房产抵押价值(元):"+housesTotalPrice.toString());
            }else {
                result.append(",房产抵押价值(元):"+housesTotalPrice.toString());
            }
        }
        return result.toString();
    }

    /**
     * 抵押/质押物描述转换
     * @param borrowCarsinfo
     * @param borrowHouses
     * @return
     */
    private String convertCollateralDesc(List<BorrowCarinfo> borrowCarsinfo, List<BorrowHouses> borrowHouses) {
        StringBuffer sb = new StringBuffer();
        //车辆
        if (CollectionUtils.isNotEmpty(borrowCarsinfo)){
            for (int i = 0; i < borrowCarsinfo.size() ; i++) {
                //最后一个
                if (i==borrowCarsinfo.size()-1){
                    sb.append(borrowCarsinfo.get(i).getBrand()+borrowCarsinfo.get(i).getModel()+"车一辆");
                }else {
                    //非最后一个
                    sb.append(borrowCarsinfo.get(i).getBrand()+borrowCarsinfo.get(i).getModel()+"车一辆, ");
                }

            }
         }

         //房屋
         if (CollectionUtils.isNotEmpty(borrowHouses)){
             if (sb.length()>0){
                 sb.append(", ");
             }
             for (int i = 0; i <borrowHouses.size() ; i++) {
                 //最后一个
                 if (i==borrowHouses.size()-1){
                     sb.append(borrowHouses.get(i).getHousesArea()+"㎡"+this.convertHousesType(borrowHouses.get(i).getHousesType())+"房产一处");
                 }else {
                     //非最后一个
                     sb.append(borrowHouses.get(i).getHousesArea()+"㎡"+this.convertHousesType(borrowHouses.get(i).getHousesType())+"房产一处, ");
                 }
             }

         }

        return sb.toString();

    }

    /**
     * 获取借款人信息
     */
	@Override
	public Map<String, String> getBorrowUserInfo(String borrowNid, String companyOrPersonal) {
		Map<String, String> resultMap = new HashMap<String, String>();
		if ("1".equals(companyOrPersonal)) {
			//公司
			BorrowUsersExample example = new BorrowUsersExample();
			BorrowUsersExample.Criteria criteria = example.createCriteria();
			criteria.andBorrowNidEqualTo(borrowNid);
			List<BorrowUsers> borrowUsers=this.borrowUsersMapper.selectByExample(example);
			resultMap.put("trueName", "");
			resultMap.put("sex", this.convertSex(9));
			String idCard = borrowUsers.get(0).getSocialCreditCode();
			if (StringUtils.isEmpty(idCard)){
                idCard = borrowUsers.get(0).getRegistCode();
            }
			resultMap.put("idCard", idCard);
		}else if ("2".equals(companyOrPersonal)) {
			//个人
			BorrowManinfoExample example = new BorrowManinfoExample();
			BorrowManinfoExample.Criteria criteria = example.createCriteria();
			criteria.andBorrowNidEqualTo(borrowNid);
			List<BorrowManinfo> borrowManinfos=this.borrowManinfoMapper.selectByExample(example);
			resultMap.put("trueName", borrowManinfos.get(0).getName());
			resultMap.put("sex", this.convertSex(borrowManinfos.get(0).getSex()));
			resultMap.put("idCard", borrowManinfos.get(0).getCardNo());
		}
		return resultMap;
	}

    @Override
    public Borrow checkBorrowInfoIsReported(String borrowNid) throws Exception {
	    Borrow borrow = this.selectBorrowInfo(borrowNid);
	    //智投下的标的不上报散标信息
	    if (StringUtils.isEmpty(borrow.getPlanNid())){

            if (null == borrow) {
                throw new Exception(logHeaderBorrowInfo + "未获取到散标信息！！" + "borrowNid:" + borrowNid);
            }

            BifaBorrowInfoEntity bifaBorrowInfoEntity = this.getBifaBorrowInfoFromMongoDB(borrowNid);
            //未上报时执行上报操作
            if (null == bifaBorrowInfoEntity) {
                // 借款人信息
                Map<String, String> borrowUserInfo = this.getBorrowUserInfo(borrow.getBorrowNid(),borrow.getCompanyOrPersonal());
                if(null == borrowUserInfo) {
                    throw new Exception(logHeaderBorrowInfo + "未获取到标的借款人信息！！");
                }

                //获取标的对应的还款信息
                BorrowRepay borrowRepay = this.selectBorrowRepay(borrowNid);

                //抵押車輛信息
                List<BorrowCarinfo> borrowCarsinfo = this.selectBorrowCarInfo(borrowNid);

                //抵押房產信息
                List<BorrowHouses> borrowHouses = this.selectBorrowHouseInfo(borrowNid);


                bifaBorrowInfoEntity = new BifaBorrowInfoEntity();
                // --> 数据变换
                boolean result = this.convertBifaBorrowInfo(
                        borrow, borrowUserInfo, borrowRepay, borrowCarsinfo, borrowHouses, bifaBorrowInfoEntity);
                if (!result) {
                    throw new Exception(logHeaderBorrowInfo + "散标数据变换失败！！" + JSONObject.toJSONString(bifaBorrowInfoEntity));
                }

                // --> 上报数据（实时上报）
                //上报数据失败时 将数据存放到mongoDB
                String methodName = "productRegistration";
                BifaBorrowInfoEntity reportResult = this.reportData(methodName, bifaBorrowInfoEntity);
                if ("9".equals(reportResult.getReportStatus())) {
                    _log.error(logHeaderBorrowInfo + "上报散标数据失败！！" + JSONObject.toJSONString(reportResult));
                } else if ("1".equals(reportResult.getReportStatus())) {
                    _log.info(logHeaderBorrowInfo + "上报散标数据成功！！" + JSONObject.toJSONString(reportResult));
                }

                // --> 保存上报数据
                result = this.insertReportData(reportResult);
                if (!result) {
                    _log.error(logHeaderBorrowInfo + "上报散标数据保存本地失败！！" + JSONObject.toJSONString(reportResult));
                } else {
                    _log.info(logHeaderBorrowInfo + "上报散标数据保存本地成功！！" + JSONObject.toJSONString(reportResult));
                }

            }
        }

        return borrow;

    }
}
