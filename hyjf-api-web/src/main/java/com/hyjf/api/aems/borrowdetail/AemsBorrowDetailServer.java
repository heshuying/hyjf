package com.hyjf.api.aems.borrowdetail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.aems.common.AemsCommonSvrChkService;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.calculate.IncreaseInterestUtils;
import com.hyjf.common.enums.utils.MsgEnum;
import com.hyjf.common.result.ResultApiBean;
import com.hyjf.common.util.AsteriskProcessUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BorrowCarinfo;
import com.hyjf.mybatis.model.auto.BorrowHouses;
import com.hyjf.mybatis.model.auto.BorrowManinfo;
import com.hyjf.mybatis.model.auto.BorrowUsers;
import com.hyjf.mybatis.model.customize.web.BorrowDetailCustomize;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 标的详情
 * jijun 20180910
 */

@Controller
@RequestMapping(value = AemsBorrowDetailDefine.REQUEST_MAPPING)
public class AemsBorrowDetailServer extends BaseController {

    @Autowired
    private AemsCommonSvrChkService commonSvrChkService;

    @Autowired
    private AemsBorrowDetailService borrowService;

    /**
     * 查看项目详情
     *
     * @param bean
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = AemsBorrowDetailDefine.BORROW_DETAIL_ACTION)
    public ResultApiBean<BorrowDetailCustomize> getProjectDetail(@RequestBody AemsBorrowDetailRequestBean bean) {
        // 验证
        // 共通验证
        commonSvrChkService.checkRequired(bean);

        //获取项目编号
        String borrowNid = bean.getBorrowNid();

        // 验签
        CheckUtil.check(this.AEMSVerifyRequestSign(bean, AemsBorrowDetailDefine.REQUEST_MAPPING + AemsBorrowDetailDefine.BORROW_DETAIL_ACTION),
                MsgEnum.ERR_SIGN);

        CheckUtil.check(Validator.isNotNull(borrowNid), MsgEnum.STATUS_CE000001);

        //根据borrowNid查询项目详情
        BorrowDetailCustomize borrow = borrowService.selectProjectDetail(borrowNid);

        //项目不存在
        CheckUtil.check(Validator.isNotNull(borrow), MsgEnum.STATUS_ZT000009);

        // 设置项目加息收益
        BigDecimal borrowExtraYield = new BigDecimal(borrow.getBorrowExtraYield() == null ? "0" : borrow.getBorrowExtraYield());
        if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrowExtraYield)) {
            String increaseInterest = IncreaseInterestUtils.getIncreaseInterest(borrow.getBorrowAccount(),
                    borrow.getBorrowStyle(), Integer.parseInt(borrow.getBorrowPeriod()), borrowExtraYield);
            borrow.setIncreaseInterest(increaseInterest);
        }

        //授信额度如果为0 返回空
        if ("0".equals(borrow.getUserCredit())) {
            borrow.setUserCredit("");
        }

        //借款人企业信息
        BorrowUsers borrowUsers = borrowService.getBorrowUsersByNid(borrowNid);
        //借款人信息
        BorrowManinfo borrowManinfo = borrowService.getBorrowManinfoByNid(borrowNid);
        //房产抵押信息
        List<BorrowHouses> borrowHousesList = borrowService.getBorrowHousesByNid(borrowNid);
        //车辆抵押信息
        List<BorrowCarinfo> borrowCarinfoList = borrowService.getBorrowCarinfoByNid(borrowNid);

        //资产列表
        JSONArray json = new JSONArray();
        //基础信息
        String baseTableData = "";
        //资产信息
        String assetsTableData = "";
        //项目介绍
        String intrTableData = "";
        //信用状况
        String credTableData = "";
        //审核信息
        String reviewTableData = "";
        //借款类型  1.企业    2.个人
        int borrowType = Integer.parseInt(borrow.getComOrPer());

        if (borrowType == 1 && borrowUsers != null) {
            //基础信息
            baseTableData = JSONObject.toJSONString(packDetail(borrowUsers, 1, borrowType, borrow.getBorrowLevel()));
            borrow.setBaseTableData(baseTableData);
            //信用状况
            credTableData = JSONObject.toJSONString(packDetail(borrowUsers, 4, borrowType, borrow.getBorrowLevel()));
            borrow.setCredTableData(credTableData);
            //审核信息
            reviewTableData = JSONObject.toJSONString(packDetail(borrowUsers, 5, borrowType, borrow.getBorrowLevel()));
            borrow.setReviewTableData(reviewTableData);
        } else {
            if (borrowManinfo != null) {
                //基础信息
                baseTableData = JSONObject.toJSONString(packDetail(borrowManinfo, 1, borrowType, borrow.getBorrowLevel()));
                borrow.setBaseTableData(baseTableData);
                //信用状况
                credTableData = JSONObject.toJSONString(packDetail(borrowManinfo, 4, borrowType, borrow.getBorrowLevel()));
                borrow.setCredTableData(credTableData);
                //审核信息
                reviewTableData = JSONObject.toJSONString(packDetail(borrowManinfo, 5, borrowType, borrow.getBorrowLevel()));
                borrow.setReviewTableData(reviewTableData);
            }
        }
        //资产信息
        if (borrowHousesList != null && borrowHousesList.size() > 0) {
            //房产抵押信息
            for (BorrowHouses borrowHouses : borrowHousesList) {
                json.add(packDetail(borrowHouses, 2, borrowType, borrow.getBorrowLevel()));
            }
        }
        if (borrowCarinfoList != null && borrowCarinfoList.size() > 0) {
            //车辆抵押信息
            for (BorrowCarinfo borrowCarinfo : borrowCarinfoList) {
                json.add(packDetail(borrowCarinfo, 2, borrowType, borrow.getBorrowLevel()));
            }
        }
        assetsTableData = json.toString();
        borrow.setAssetsTableData(assetsTableData);
        //项目介绍
        intrTableData = JSONObject.toJSONString(packDetail(borrow, 3, borrowType, borrow.getBorrowLevel()));
        borrow.setIntrTableData(intrTableData);

        return new ResultApiBean<BorrowDetailCustomize>(borrow);

    }

    private List<AemsBorrowDetailBean> packDetail(Object objBean, int type, int borrowType, String borrowLevel) {
        List<AemsBorrowDetailBean> detailBeanList = new ArrayList<AemsBorrowDetailBean>();
        String currencyName = "元";
        // 得到对象
        Class c = objBean.getClass();
        // 得到类中声明的属性
        Field fieldlist[] = c.getDeclaredFields();
        for (int i = 0; i < fieldlist.length; i++) {
            // 获取遍历出的属性
            Field f = fieldlist[i];
            // 获取遍历出的属性名
            String fName = f.getName();
            try {
                // 根据属性名构造方法名中的属性部分
                String paramName = fName.substring(0, 1).toUpperCase() + fName.substring(1, fName.length());
                // 通过属性名获取get方法
                Method getMethod = c.getMethod(BankCallConstant.GET + paramName);
                if (getMethod != null) {
                    //执行get方法
                    Object result = getMethod.invoke(objBean);
                    // 执行get方法的结果不为空时
                    if (Validator.isNotNull(result)) {
                        //封装bean
                        AemsBorrowDetailBean detailBean = new AemsBorrowDetailBean();
                        detailBean.setId(fName);
                        detailBean.setVal(result.toString());
                        if (type == 1) {
                            if (borrowType == 2) {//个人借款
                                switch (fName) {
                                    case "name":
                                        detailBean.setKey("姓名");
                                        //数据脱敏
                                        detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 1, 2));
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "cardNo":
                                        detailBean.setKey("身份证号");
                                        //数据脱敏
                                        detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 4, 10));
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "sex":
                                        detailBean.setKey("性别");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("男");
                                        } else {
                                            detailBean.setVal("女");
                                        }
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "old":
                                        if (!"0".equals(detailBean.getVal())) {
                                            detailBean.setKey("年龄");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "merry":
                                        if (!("0".equals(result.toString()) || result.toString() == null)) {
                                            detailBean.setKey("婚姻状况");
                                            if ("1".equals(result.toString())) {
                                                detailBean.setVal("已婚");
                                            } else if ("2".equals(result.toString())) {
                                                detailBean.setVal("未婚");
                                            } else if ("3".equals(result.toString())) {
                                                detailBean.setVal("离异");
                                            } else if ("4".equals(result.toString())) {
                                                detailBean.setVal("丧偶");
                                            }
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "city":
                                        detailBean.setKey("工作城市");
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "domicile":
                                        detailBean.setKey("户籍地");
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "position":
                                        detailBean.setKey("岗位职业");
                                        detailBeanList.add(detailBean);
                                        break;
                                    default:
                                        break;
                                }
                            } else {//企业借款

                                switch (fName) {
                                    case "currencyName":
                                        currencyName = detailBean.getVal();
                                        break;
                                    case "username":
                                        detailBean.setKey("借款主体");
                                        detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), detailBean.getVal().length() - 2));
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "city":
                                        detailBean.setKey("注册地区");
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "regCaptial":
                                        detailBean.setKey("注册资本");
                                        if (StringUtils.isNotBlank(detailBean.getVal())) {
                                            detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + currencyName);
                                        }
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "comRegTime":
                                        detailBean.setKey("注册时间");
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "socialCreditCode":
                                        detailBean.setKey("统一社会信用代码");
                                        //数据脱敏
                                        detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 4, 10));
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "registCode":
                                        detailBean.setKey("注册号");
                                        //数据脱敏
                                        detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 4, 10));
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "legalPerson":
                                        detailBean.setKey("法定代表人");
                                        //数据脱敏
                                        detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 1, 2));
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "industry":
                                        detailBean.setKey("所属行业");
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "mainBusiness":
                                        detailBean.setKey("主营业务");
                                        detailBeanList.add(detailBean);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        } else if (type == 2) {
                            switch (fName) {
                                case "housesType":
                                    detailBean.setKey("资产类型");
                                    String houseType = this.borrowService.getParamName("HOUSES_TYPE", detailBean.getVal());
                                    if (houseType != null) {
                                        detailBean.setVal(houseType);
                                    } else {
                                        detailBean.setVal("住宅");
                                    }
                                    detailBeanList.add(detailBean);
                                    break;
                                case "housesArea":
                                    detailBean.setKey("资产面积");
                                    detailBean.setVal(detailBean.getVal() + "m<sup>2</sup>");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "housesCnt":
                                    detailBean.setKey("资产数量");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "housesToprice":
                                    detailBean.setKey("评估价值");
                                    if (StringUtils.isNotBlank(detailBean.getVal())) {
                                        detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + "元");
                                    }
                                    detailBeanList.add(detailBean);
                                    break;
                                case "housesBelong":
                                    detailBean.setKey("资产所属");
                                    detailBeanList.add(detailBean);
                                    break;
                                //车辆
                                case "brand":
                                    AemsBorrowDetailBean carBean = new AemsBorrowDetailBean();
                                    carBean.setId("carType");
                                    carBean.setKey("资产类型");
                                    carBean.setVal("车辆");
                                    detailBeanList.add(carBean);
                                    detailBean.setKey("品牌");
                                    detailBeanList.add(detailBean);
                                    break;

                                case "model":
                                    detailBean.setKey("型号");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "place":
                                    detailBean.setKey("产地");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "price":
                                    detailBean.setKey("购买价格");
                                    if (StringUtils.isNotBlank(detailBean.getVal())) {
                                        detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + "元");
                                    }
                                    detailBeanList.add(detailBean);
                                    break;
                                case "toprice":
                                    detailBean.setKey("评估价值");
                                    if (StringUtils.isNotBlank(detailBean.getVal())) {
                                        detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + "元");
                                    }
                                    detailBeanList.add(detailBean);
                                    break;
                                case "number":
                                    detailBean.setKey("车牌号");
                                    //数据脱敏
                                    detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 2, 4));
                                    detailBeanList.add(detailBean);
                                    break;
                                case "registration":
                                    detailBean.setKey("车辆登记地");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "vin":
                                    detailBean.setKey("车架号");
                                    //数据脱敏
                                    detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 4, 5));
                                    detailBeanList.add(detailBean);
                                    break;
                                default:
                                    break;
                            }

                        } else if (type == 3) {
                            switch (fName) {
                                case "borrowContents":
                                    detailBean.setKey("项目信息");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "fianceCondition":
                                    detailBean.setKey("财务状况 ");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "financePurpose":
                                    detailBean.setKey("借款用途");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "monthlyIncome":
                                    detailBean.setKey("月薪收入");
                                    if (StringUtils.isNotBlank(detailBean.getVal())) {
                                        detailBean.setVal(detailBean.getVal());
                                    }
                                    detailBeanList.add(detailBean);
                                    break;
                                case "payment":
                                    detailBean.setKey("还款来源");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "firstPayment":
                                    detailBean.setKey("第一还款来源");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "secondPayment"://还没有
                                    detailBean.setKey("第二还款来源");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "costIntrodution":
                                    detailBean.setKey("费用说明");
                                    detailBeanList.add(detailBean);
                                    break;
                                default:
                                    break;
                            }
                        } else if (type == 4) {
                            switch (fName) {
                                case "overdueTimes":
                                    detailBean.setKey("在平台逾期次数");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "overdueAmount":
                                    detailBean.setKey("在平台逾期金额");
                                    if (StringUtils.isNotBlank(detailBean.getVal())) {
                                        detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + "元");
                                    }
                                    detailBeanList.add(detailBean);
                                    break;
                                case "litigation":
                                    detailBean.setKey("涉诉情况");
                                    detailBeanList.add(detailBean);
                                    break;
                                default:
                                    break;
                            }
                        } else if (type == 5) {
                            if (borrowType == 2) {
                                switch (fName) {
                                    case "isCard":
                                        detailBean.setKey("身份证");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isIncome":
                                        detailBean.setKey("收入状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isCredit":
                                        detailBean.setKey("信用状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isAsset":
                                        detailBean.setKey("资产状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isVehicle":
                                        detailBean.setKey("车辆状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isDrivingLicense":
                                        detailBean.setKey("行驶证");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isVehicleRegistration":
                                        detailBean.setKey("车辆登记证");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isMerry":
                                        detailBean.setKey("婚姻状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isWork":
                                        detailBean.setKey("工作状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isAccountBook":
                                        detailBean.setKey("户口本");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                switch (fName) {
                                    case "isCertificate":
                                        detailBean.setKey("企业证件");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isOperation":
                                        detailBean.setKey("经营状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isFinance":
                                        detailBean.setKey("财务状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isEnterpriseCreidt":
                                        detailBean.setKey("企业信用");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isLegalPerson":
                                        detailBean.setKey("法人信息");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isAsset":
                                        detailBean.setKey("资产状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isPurchaseContract":
                                        detailBean.setKey("购销合同");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isSupplyContract":
                                        detailBean.setKey("供销合同");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                continue;
            }
        }
        if (type == 1 || type == 4) {
            //信用评级单独封装
            AemsBorrowDetailBean detailBean = new AemsBorrowDetailBean();
            detailBean.setId("borrowLevel");
            detailBean.setKey("信用评级");
            detailBean.setVal(borrowLevel);
            detailBeanList.add(detailBean);
        }
        return detailBeanList;
    }
}
