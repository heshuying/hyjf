package com.hyjf.admin;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.auto.AdminRole;
import com.hyjf.mybatis.model.auto.AdminUtmReadPermissions;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.mybatis.model.auto.Department;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhAssetType;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.TenderAgreement;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderDetailCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * <p>
 * BaseService
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
public interface BaseService {

	/**
	 * 获取部门列表
	 *
	 * @return
	 */
	public List<Department> getDepartmentList();

	/**
	 * 获取部门列表
	 *
	 * @return
	 */
	public JSONArray getCrmDepartmentList(String[] selectedNode);

	/**
	 * 获取银行列表
	 *
	 * @return
	 */
	public List<BankConfig> getBankConfig(BankConfig bankConfig);

	/**
	 * 获取角色列表
	 *
	 * @return
	 */
	public List<AdminRole> getAdminRoleList();

	/**
	 * 获取数据字典表的下拉列表
	 *
	 * @return
	 */
	public List<ParamName> getParamNameList(String nameClass);

	/**
	 * 获取数据字典名称
	 *
	 * @return
	 */
	public String getParamName(String nameClass, String nameCd);

	/**
	 * 获取系统配置
	 *
	 * @return
	 */
	public String getBorrowConfig(String configCd);

	/**
	 * 获取用户在汇付天下的账号信息
	 *
	 * @return
	 */
	public AccountChinapnr getChinapnrUserInfo(Integer userId);

	/**
	 * 写入日志
	 *
	 * @return
	 */
	public int insertChinapnrLog(ChinapnrLog log);

	/**
	 * 发送短信添加数据
	 *
	 * @param content
	 * @param mobile
	 * @param checkCode
	 * @param remark
	 * @param status
	 * @return
	 */
	public boolean AddMessage(String content, String mobile, String checkCode, String remark, int status);

	/**
	 * 发送邮箱
	 *
	 * @param title
	 *            邮件标题
	 * @param content
	 *            邮件内容
	 * @param filenames
	 *            附件路径s
	 * @param emails
	 *            用户邮箱
	 * @return
	 */
	public boolean SendMail(String title, String content, String[] filenames, String[] emails);

	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	public Users getUsersByUserId(Integer userId);

	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	public UsersInfo getUsersInfoByUserId(Integer userId);

	/**
	 * 根据用户编号 取得后台admin用户
	 * 
	 * @param userId
	 * @return
	 */
	Admin getAdminInfoByUserId(Integer userId);

	/**
	 * 
	 * 根据条件查询优惠券使用详情
	 * 
	 * @author pcc
	 * @param detail
	 * @return
	 */
	CouponTenderDetailCustomize getCouponTenderDetailCustomize(Map<String, Object> paramMap);

	/**
	 * 
	 * 返回回款列表
	 * 
	 * @author pcc
	 * @param detail
	 * @return
	 */
	List<CouponRecoverCustomize> getCouponRecoverCustomize(Map<String, Object> paramMap);

	/**
	 * 
	 * 根据条件查询优惠券使用详情
	 * 
	 * @author pcc
	 * @param detail
	 * @return
	 */
	CouponTenderDetailCustomize getCouponTenderHtjDetailCustomize(Map<String, Object> paramMap);

	/**
	 * 
	 * 返回回款列表
	 * 
	 * @author pcc
	 * @param detail
	 * @return
	 */
	List<CouponRecoverCustomize> getCouponRecoverHtjCustomize(Map<String, Object> paramMap);

	/**
	 * 根据用户Id查询渠道账号权限信息
	 * 
	 * @param userId
	 * @return
	 */
	public AdminUtmReadPermissions selectAdminUtmReadPermissions(Integer userId);

	/**
	 * 获取银行开户信息
	 * 
	 * @param userId
	 * @return
	 */
	public BankOpenAccount getBankOpenAccount(Integer userId);

	/**
	 * 根据电子账号查询用户在江西银行的可用余额
	 * 
	 * @param userId
	 * @param accountId
	 * @return
	 */
	public BigDecimal getBankBalance(Integer userId, String accountId);
	
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
	
	
	/**返回码错误信息*/
	public String getBankRetMsg(String retCode); 
	
	/**
	 * 根据项目编号获取项目标的
	 * 
	 * @param borrowNid
	 * @return
	 */
	public Borrow getBorrowByNid(String borrowNid);
	
	
	/**
	 * 根据银行卡号查询银行ID
	 * 
	 * @param cardNo
	 * @return
	 */
	public String getBankIdByCardNo(String cardNo);
	
	/**
	 * 根据银行Id查询所属银行名称
	 * @param bankId
	 * @return
	 */
	public String getBankNameById(String bankId);
	
	/**
	 * 调用江西银行查询联行号
	 * @param cardNo
	 * @return
	 */
	public BankCallBean payAllianceCodeQuery(String cardNo,Integer userId);
	
	/**
	 * 根据银行Id查询本地存的银联行号
	 * @param bankId
	 * @return
	 */
	public String getPayAllianceCodeByBankId(String bankId);
	

	/**
	 * 资金来源
	 * @param string
	 * @return
	 */
	public List<HjhInstConfig> hjhInstConfigList(String instCode);
	
//	/**
//	 * 根据资金来源查询产品类型
//	 * @param string
//	 * @return
//	 */
//	public List<HjhAssetBorrowType> hjhAssetBorrowTypeList(String instCode);
	
	/**
	 * 根据资金来源查询产品类型
	 * @param string
	 * @return
	 */
	public List<HjhAssetType> hjhAssetTypeList(String instCode);

	/**
	 * 调用手续费分账接口
	 * @param userId
	 * @param accountId
	 * @param forAccountId
	 * @param txAmount
	 * @return
	 */
	public BankCallBean feeShare(Integer userId, String accountId, String forAccountId, String txAmount);

	/**
	 * 获取用户信息
	 * 
	 * @param userName
	 * @return 用户信息
	 */
	Users getUserByUserName(String userName);

	/**
	 * 获取用户的账户信息
	 * 
	 * @param userId
	 * @return 用户账户信息
	 */
	public Account getAccount(Integer userId);

	/**
	 * 根据出借订单号检索用户出借协议记录表
	 * @param nid
	 * @return
	 */
	TenderAgreement selectTenderAgreement(String nid);


	/**
	 * PDF下载加脱敏
	 * @param tenderAgreement
	 * @param borrowNid
	 * @param transType
	 * @param instCode
	 */
	void updateSaveSignInfo(TenderAgreement tenderAgreement,String borrowNid, Integer transType, String instCode);
	
	/**
	 * 获取流程配置
	 * 
	 * @param borrowNid
	 * @return
	 */
	HjhAssetBorrowType selectAssetBorrowType(String borrowNid);
	
    /**
     * 根据项目编号查询资产信息
     * 
     * @param borrowNid
     * @return
     */
    HjhPlanAsset getHjhPlanAsset(String borrowNid);
}
