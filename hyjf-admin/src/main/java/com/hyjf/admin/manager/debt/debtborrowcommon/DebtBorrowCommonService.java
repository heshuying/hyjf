package com.hyjf.admin.manager.debt.debtborrowcommon;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseService;
import com.hyjf.admin.manager.debt.debtborrow.DebtBorrowBean;
import com.hyjf.mybatis.model.auto.BorrowProjectRepay;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.Links;

public interface DebtBorrowCommonService extends BaseService {

	/**
	 * 根据主键判断权限维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(String borrowNid, String borrowPreNid);

	/**
	 * 用户是否存在
	 * 
	 * @param userId
	 * @return
	 */
	public int isExistsUser(String userId);

	/**
	 * 借款预编码是否存在
	 * 
	 * @param username
	 * @return
	 */
	public String isExistsBorrowPreNidRecord(HttpServletRequest request);

	/**
	 * 项目类型
	 * 
	 * @return
	 */
	public List<BorrowProjectType> borrowProjectTypeList(String borrowTypeCd);

	/**
	 * 还款方式
	 * 
	 * @return
	 */
	public List<BorrowStyle> borrowStyleList(String nid);

	/**
	 * 还款方式 关联 项目类型
	 * 
	 * @return
	 */
	public List<BorrowProjectRepay> borrowProjectRepayList();

	/**
	 * 借款数据获取
	 * 
	 * @param borrow
	 * @return
	 */
	public DebtBorrowCommonBean getBorrow(DebtBorrowCommonBean borrowCommonBean);

	/**
	 * 借款数据获取
	 * 
	 * @param borrow
	 * @return
	 */
	public void getBorrowCommonFiled(DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs borrowWithBLOBs);

	/**
	 * 借款人信息数据获取
	 * 
	 * @param borrow
	 * @return
	 */
	public void getBorrowManinfo(DebtBorrowCommonBean borrowBean);

	/**
	 * 车辆信息数据获取
	 * 
	 * @param borrow
	 * @return
	 */
	public void getBorrowCarinfo(DebtBorrowCommonBean borrowBean);

	/**
	 * 用户信息数据获取
	 * 
	 * @param borrow
	 * @return
	 */
	public void getBorrowUsers(DebtBorrowCommonBean borrowBean);

	/**
	 * 插入操作
	 * 
	 * @param record
	 */
	public void insertRecord(DebtBorrowCommonBean borrowBean) throws Exception;

	/**
	 * 借款表插入
	 * 
	 * @param borrowBean
	 * @param borrow
	 */
	public void insertBorrowCommonData(DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs borrow, String borrowPreNid, String borrowNid, String name, String account) throws Exception;

	/**
	 * 更新操作
	 * 
	 * @param record
	 */
	public void updateRecord(DebtBorrowCommonBean borrowBean) throws Exception;

	/**
	 * 借款表更新
	 * 
	 * @param borrowBean
	 * @param borrow
	 */
	public void updateBorrowCommonData(DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs borrow, String borrowNid) throws Exception;

	/**
	 * 获取借款预编号
	 * 
	 * @return
	 */
	public String getBorrowPreNid();

	/**
	 * 获取借款预编号
	 * 
	 * @return
	 */
	public int isExistsBorrowPreNid(String borrowPreNid);

	/**
	 * 获取借款信息
	 * 
	 * @param borrow
	 * @return
	 * @author Administrator
	 */
	public DebtBorrowWithBLOBs getBorrowWithBLOBs(String borrowNid);

	/**
	 * 获取放款服务费率
	 * 
	 * @param chargeTimeType
	 * @param chargeTime
	 * @return
	 */
	public String getBorrowServiceScale(String projectType, String chargeTimeType, Integer chargeTime);

	/**
	 * 还款服务费率
	 * 
	 * @param chargeTimeType
	 * @param chargeTime
	 * @return
	 */
	public String getBorrowManagerScale(String projectType, String borrowStyle, Integer borrowPeriod);

	/**
	 * 收益差率
	 * 
	 * @param chargeTimeType
	 * @param chargeTime
	 * @return
	 */
	public String getBorrowReturnScale(String projectType, String borrowStyle, Integer borrowPeriod);

	/**
	 * 上传图片的信息
	 * 
	 * @param borrowBean
	 * @return
	 * @throws Exception
	 */
	public String getUploadImage(DebtBorrowCommonBean borrowBean, String files, String borrowNid) throws Exception;

	/**
	 * 车辆信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param debtBorrow
	 * @return
	 */
	public int insertBorrowCarinfo(String borrowNid, DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs debtBorrow);

	/**
	 * 个人信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param debtBorrow
	 * @return
	 */
	public int insertBorrowManinfo(String borrowNid, DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs debtBorrow);

	/**
	 * 公司信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param debtBorrow
	 * @return
	 */
	public int insertBorrowUsers(String borrowNid, DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs debtBorrow);

	/**
	 * 房产信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param borrow
	 * @return
	 */
	public int insertBorrowHouses(String borrowNid, DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs borrow);

	/**
	 * 认证信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param borrow
	 * @return
	 */
	public int insertBorrowCompanyAuthen(String borrowNid, DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs borrow);

	/**
	 * 合作机构
	 * 
	 * @param chargeTimeType
	 * @param chargeTime
	 * @return
	 */
	public List<Links> getLinks();

	/**
	 * 信息验证
	 * 
	 * @param mav
	 * @param request
	 */
	public void validatorFieldCheck(ModelAndView mav, DebtBorrowCommonBean borrowBean, boolean isExistsRecord, String HztOrHxf);

	/**
	 * 画面的值放到Bean中
	 * 
	 * @param modelAndView
	 * @param form
	 */
	public void setPageListInfo(ModelAndView modelAndView, DebtBorrowCommonBean form, boolean isExistsRecord);

	/**
	 * 用户是否存在
	 * 
	 * @param username
	 * @return
	 */
	public String isExistsUser(HttpServletRequest request);

	/**
	 * 项目申请人是否存在
	 * 
	 * @param username
	 * @return
	 */
	public String isExistsApplicant(HttpServletRequest request);

	/**
	 * 获取放款服务费率 & 还款服务费率 & 收益差率
	 * 
	 * @param request
	 * @return
	 */
	public String getBorrowServiceScale(HttpServletRequest request);

	/**
	 * 还款服务费率(最低，最高)
	 * 
	 * @param chargeTimeType
	 * @param chargeTime
	 * @return
	 */
	public JSONObject getBorrowManagerScale(String projectType, String borrowStyle, Integer borrowPeriod, JSONObject jsonObject);

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * 项目类型
	 * 
	 * @return
	 * @author Administrator
	 */
	public String getBorrowProjectClass(String borrowCd);

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	public void downloadCar(HttpServletRequest request, HttpServletResponse response, DebtBorrowBean form) throws Exception;

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String uploadCar(HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	public void downloadHouse(HttpServletRequest request, HttpServletResponse response, DebtBorrowBean form) throws Exception;

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String uploadHouse(HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	public void downloadAuthen(HttpServletRequest request, HttpServletResponse response, DebtBorrowBean form) throws Exception;

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String uploadAuthen(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public DebtBorrowWithBLOBs getRecordById(DebtBorrowCommonBean borrowBean);
}
