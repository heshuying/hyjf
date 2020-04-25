package com.hyjf.admin.manager.debt.debtborrowcommon;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.maintenance.admin.AdminDefine;
import com.hyjf.admin.manager.debt.debtborrow.DebtBorrowBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetSessionOrRequestUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowFinhxfmanCharge;
import com.hyjf.mybatis.model.auto.BorrowFinhxfmanChargeExample;
import com.hyjf.mybatis.model.auto.BorrowFinmanCharge;
import com.hyjf.mybatis.model.auto.BorrowFinmanChargeExample;
import com.hyjf.mybatis.model.auto.BorrowFinserCharge;
import com.hyjf.mybatis.model.auto.BorrowFinserChargeExample;
import com.hyjf.mybatis.model.auto.BorrowProjectRepay;
import com.hyjf.mybatis.model.auto.BorrowProjectRepayExample;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.ConfigApplicant;
import com.hyjf.mybatis.model.auto.ConfigApplicantExample;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.DebtCarInfo;
import com.hyjf.mybatis.model.auto.DebtCarInfoExample;
import com.hyjf.mybatis.model.auto.DebtCompanyAuthen;
import com.hyjf.mybatis.model.auto.DebtCompanyAuthenExample;
import com.hyjf.mybatis.model.auto.DebtCompanyInfo;
import com.hyjf.mybatis.model.auto.DebtCompanyInfoExample;
import com.hyjf.mybatis.model.auto.DebtHouseInfo;
import com.hyjf.mybatis.model.auto.DebtHouseInfoExample;
import com.hyjf.mybatis.model.auto.DebtUsersInfo;
import com.hyjf.mybatis.model.auto.DebtUsersInfoExample;
import com.hyjf.mybatis.model.auto.Links;
import com.hyjf.mybatis.model.auto.LinksExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.AdminSystem;

@Service
public class DebtBorrowCommonServiceImpl extends BaseServiceImpl implements DebtBorrowCommonService {

	/**
	 * 汇消费的项目类型编号
	 */
	public static String PROJECT_TYPE_HXF = "8";

	/**
	 * 根据主键判断借款数据是否存在
	 * 
	 * @return
	 */
	@Override
	public boolean isExistsRecord(String borrowNid, String borrowPreNid) {
		if (StringUtils.isNotEmpty(borrowNid) || StringUtils.isNotEmpty(borrowPreNid)) {
			DebtBorrowExample debtBorrowExample = new DebtBorrowExample();
			DebtBorrowExample.Criteria debtBorrowCra = debtBorrowExample.createCriteria();

			if (StringUtils.isNotEmpty(borrowNid)) {
				debtBorrowCra.andBorrowNidEqualTo(borrowNid);
			}

			if (StringUtils.isNotEmpty(borrowPreNid)) {
				debtBorrowCra.andBorrowPreNidEqualTo(Integer.valueOf(borrowPreNid));
			}

			List<DebtBorrow> borrowList = this.debtBorrowMapper.selectByExample(debtBorrowExample);

			if (borrowList != null && borrowList.size() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 权限维护插入
	 * 
	 * @param record
	 * @throws Exception
	 */
	@Override
	public void insertRecord(DebtBorrowCommonBean borrowBean) throws Exception {

		// 项目类型
		String projectType = borrowBean.getProjectType();
		String beforeFix = this.getBorrowProjectClass(projectType);
		String borrowPreNid = borrowBean.getBorrowPreNid();

		List<DebtBorrowCommonNameAccount> borrowCommonNameAccountList = borrowBean.getBorrowCommonNameAccountList();

		String isChaibiao = borrowBean.getIsChaibiao();
		// 项目名称 & 借款金额
		if (!"yes".equals(isChaibiao)) {
			borrowCommonNameAccountList = new ArrayList<DebtBorrowCommonNameAccount>();
			DebtBorrowCommonNameAccount borrowCommonNameAccount = new DebtBorrowCommonNameAccount();
			borrowCommonNameAccount.setNames(borrowBean.getName());
			borrowCommonNameAccount.setAccounts(borrowBean.getAccount());
			borrowCommonNameAccountList.add(borrowCommonNameAccount);
		}

		if (borrowCommonNameAccountList != null && borrowCommonNameAccountList.size() > 0) {
			int i = 1;
			for (DebtBorrowCommonNameAccount borrowCommonNameAccount : borrowCommonNameAccountList) {
				String name = borrowCommonNameAccount.getNames();
				String account = borrowCommonNameAccount.getAccounts();
				if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(account)) {

					String cNum = String.valueOf(i);
					if (i < 10) {
						cNum = "0" + String.valueOf(i);
					}
					i++;
					String borrowNid = beforeFix + borrowPreNid + cNum;

					// 插入huiyingdai_borrow
					DebtBorrowWithBLOBs debtBorrow = new DebtBorrowWithBLOBs();

					// 借款表插入
					this.insertBorrowCommonData(borrowBean, debtBorrow, borrowPreNid, borrowNid, name, account);

				}
			}
		}
	}

	/**
	 * 借款表插入
	 * 
	 * @param borrowBean
	 * @param borrow
	 * @throws Exception
	 */
	@Override
	public void insertBorrowCommonData(DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs debtBorrow,
			String borrowPreNid, String borrowNid, String name, String account) throws Exception {
		// 插入时间
		Date systemNowDate = GetDate.getDate();
		// 添加时间
		String addtime = String.valueOf(GetDate.getNowTime10());
		// 借款用户
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andUsernameEqualTo(borrowBean.getUsername());
		cra.andOpenAccountEqualTo(1);
		cra.andStatusEqualTo(0);
		List<Users> userList = this.usersMapper.selectByExample(example);
		if (StringUtils.isNotEmpty(borrowBean.getBorrowLevel())) {
			debtBorrow.setBorrowLevel(borrowBean.getBorrowLevel());
		}
		debtBorrow.setBorrowIncreaseMoney(borrowBean.getBorrowIncreaseMoney());
		debtBorrow.setBorrowInterestCoupon(borrowBean.getBorrowInterestCoupon());
		debtBorrow.setBorrowTasteMoney(borrowBean.getBorrowTasteMoney());

		debtBorrow.setUserId(userList.get(0).getUserId());
		// 借款人用户名
		debtBorrow.setBorrowUserName(borrowBean.getUsername());
		// 项目申请人
		debtBorrow.setApplicant(borrowBean.getApplicant());
		// 项目编号
		borrowBean.setBorrowNid(borrowNid);
		// 项目名称
		debtBorrow.setName(name);
		// 状态
		debtBorrow.setStatus(0);
		// 图片信息
		debtBorrow.setBorrowPic("");
		// 点击次数
		debtBorrow.setHits(0);
		debtBorrow.setCommentCount(0);// 插入时不用的字段

		// 借款方式
		// 车辆抵押:2 房产抵押:1
		if (StringUtils.equals("2", borrowBean.getTypeCar())) {
			debtBorrow.setType(borrowBean.getTypeCar());
		}

		if (StringUtils.equals("1", borrowBean.getTypeHouse())) {
			debtBorrow.setType(borrowBean.getTypeHouse());
		}

		if (StringUtils.equals("2", borrowBean.getTypeCar()) && StringUtils.equals("1", borrowBean.getTypeHouse())) {
			debtBorrow.setType("3");
		}

		if (StringUtils.isEmpty(debtBorrow.getType())) {
			debtBorrow.setType("0");
		}

		debtBorrow.setViewType("");// 插入时不用的字段
		// 添加时间
		debtBorrow.setAddtime(addtime);
		// 添加IP
		debtBorrow.setAddip(GetCilentIP.getIpAddr(GetSessionOrRequestUtils.getRequest()));
		// 冻结额度
		debtBorrow.setAmountAccount(new BigDecimal(account));
		debtBorrow.setAmountType("credit");// 插入时不用的字段
		debtBorrow.setCashStatus(0);// 插入时不用的字段
		// 借款总金额
		debtBorrow.setAccount(new BigDecimal(account));
		debtBorrow.setBorrowAccountWait(new BigDecimal(account));
		debtBorrow.setBorrowAccountWaitAppoint(new BigDecimal(account));
		debtBorrow.setOtherWebStatus(0);// 插入时不用的字段
		// 财务状况
		if (StringUtils.isEmpty(borrowBean.getAccountContents())) {
			debtBorrow.setAccountContents(StringUtils.EMPTY);
		} else {
			debtBorrow.setAccountContents(borrowBean.getAccountContents());
		}
		debtBorrow.setBorrowType("credit");// 插入时不用的字段
		debtBorrow.setBorrowPassword("");// 插入时不用的字段
		debtBorrow.setBorrowFlag("");// 插入时不用的字段
		// 是否可以进行借款
		debtBorrow.setBorrowStatus(0);
		// 满表审核状态
		debtBorrow.setBorrowFullStatus(0);
		// 项目编号
		debtBorrow.setBorrowNid(borrowNid);
		// 借款预编码
		debtBorrow.setBorrowPreNid(Integer.valueOf(borrowPreNid));
		// 已经募集的金额
		debtBorrow.setBorrowAccountYes(BigDecimal.ZERO);
		// // 剩余的金额
		// borrow.setBorrowAccountWait(BigDecimal.ZERO);
		// 募集完成率
		debtBorrow.setBorrowAccountScale(BigDecimal.ZERO);
		// 借款用途
		debtBorrow.setBorrowUse(borrowBean.getBorrowUse());
		// 还款方式
		debtBorrow.setBorrowStyle(borrowBean.getBorrowStyle());
		// 借款期限
		debtBorrow.setBorrowPeriod(Integer.valueOf(borrowBean.getBorrowPeriod()));
		debtBorrow.setBorrowPeriodRoam(0);// 插入时不用的字段
		debtBorrow.setBorrowDay(0);// 插入时不用的字段
		// 借款利率
		debtBorrow.setBorrowApr(new BigDecimal(borrowBean.getBorrowApr()));
		// 项目描述
		debtBorrow.setBorrowContents(borrowBean.getBorrowContents());
		debtBorrow.setBorrowFrostAccount(BigDecimal.ZERO);// 插入时不用的字段
		debtBorrow.setBorrowFrostScale("");// 插入时不用的字段
		debtBorrow.setBorrowFrostSecond(BigDecimal.ZERO);// 插入时不用的字段
		// 借款有效时间
		if (StringUtils.isNotEmpty(borrowBean.getBorrowValidTime())) {
			debtBorrow.setBorrowValidTime(Integer.valueOf(borrowBean.getBorrowValidTime()));
		} else {
			debtBorrow.setBorrowValidTime(0);
		}
		// 借款成功时间
		debtBorrow.setBorrowSuccessTime(0);
		// 借款到期时间
		debtBorrow.setBorrowEndTime("");
		debtBorrow.setBorrowPartStatus(0);// 插入时不用的字段
		debtBorrow.setBorrowUpfiles("");// 插入时不用的字段
		debtBorrow.setCancelUserid(0);// 插入时不用的字段
		debtBorrow.setCancelStatus(0);// 插入时不用的字段
		debtBorrow.setCancelTime("");// 插入时不用的字段
		debtBorrow.setCancelRemark("");// 插入时不用的字段
		debtBorrow.setCancelContents("");// 插入时不用的字段
		// 最低投标金额
		if (StringUtils.isNotEmpty(borrowBean.getTenderAccountMin())) {
			debtBorrow.setTenderAccountMin(Integer.valueOf(borrowBean.getTenderAccountMin()));
		} else {
			debtBorrow.setTenderAccountMin(0);
		}

		// 最高投标金额
		if (StringUtils.isNotEmpty(borrowBean.getTenderAccountMax())) {
			debtBorrow.setTenderAccountMax(Integer.valueOf(borrowBean.getTenderAccountMax()));
		} else {
			debtBorrow.setTenderAccountMax(0);
		}
		// 投标次数
		debtBorrow.setTenderTimes(0);
		// 最后出借时间
		debtBorrow.setTenderLastTime("");
		debtBorrow.setRepayAdvanceStatus(0);// 插入时不用的字段
		debtBorrow.setRepayAdvanceTime("");// 插入时不用的字段
		debtBorrow.setRepayAdvanceStep(0);// 插入时不用的字段
		// 应还款总额
		debtBorrow.setRepayAccountAll(BigDecimal.ZERO);
		// 总还款利息
		debtBorrow.setRepayAccountInterest(BigDecimal.ZERO);
		// 总还款本金
		debtBorrow.setRepayAccountCapital(BigDecimal.ZERO);
		// 已还款总额
		debtBorrow.setRepayAccountYes(BigDecimal.ZERO);
		// 已还款利息
		debtBorrow.setRepayAccountInterestYes(BigDecimal.ZERO);
		// 已还款本金
		debtBorrow.setRepayAccountCapitalYes(BigDecimal.ZERO);
		// 未还款总额
		debtBorrow.setRepayAccountWait(BigDecimal.ZERO);
		// 未还款利息
		debtBorrow.setRepayAccountInterestWait(BigDecimal.ZERO);
		// 未还款本金
		debtBorrow.setRepayAccountCapitalWait(BigDecimal.ZERO);
		debtBorrow.setRepayAccountTimes(0);// 插入时不用的字段
		debtBorrow.setRepayMonthAccount(0);// 插入时不用的字段
		// 最后还款时间
		debtBorrow.setRepayLastTime("");// 插入时不用的字段
		debtBorrow.setRepayEachTime("");// 插入时不用的字段
		debtBorrow.setRepayNextTime(0);// 插入时不用的字段
		debtBorrow.setRepayNextAccount(BigDecimal.ZERO);// 插入时不用的字段
		// 还款次数
		debtBorrow.setRepayTimes(0);
		debtBorrow.setRepayFullStatus(0);// 插入时不用的字段
		debtBorrow.setRepayFeeNormal(BigDecimal.ZERO); // 插入时不用的字段
														// 正常还款费用
		debtBorrow.setRepayFeeAdvance(BigDecimal.ZERO); // 插入时不用的字段
		// 提前还款费用
		debtBorrow.setRepayFeeLate(BigDecimal.ZERO); // 插入时不用的字段
														// 逾期还款费用
		debtBorrow.setLateInterest(BigDecimal.ZERO); // 插入时不用的字段
														// 逾期利息
		debtBorrow.setLateForfeit(BigDecimal.ZERO); // 插入时不用的字段
		// 逾期催缴费
		debtBorrow.setVouchStatus(0); // 插入时不用的字段 是否是担保
		debtBorrow.setVouchAdvanceStatus(0); // 插入时不用的字段
		debtBorrow.setVouchUserStatus(0); // 插入时不用的字段 担保人担保状态
		debtBorrow.setVouchUsers(""); // 插入时不用的字段 担保人列表
		debtBorrow.setVouchAccount(BigDecimal.ZERO); // 插入时不用的字段
														// 总担保的金额
		debtBorrow.setVouchAccountYes(BigDecimal.ZERO); // 插入时不用的字段
		// 已担保的金额
		debtBorrow.setVouchAccountWait(BigDecimal.ZERO); // 插入时不用的字段
		debtBorrow.setVouchAccountScale(0L); // 插入时不用的字段 已担保的比例
		debtBorrow.setVouchTimes(0); // 插入时不用的字段 担保次数
		debtBorrow.setVouchAwardStatus(0); // 插入时不用的字段 是否设置担保奖励
		debtBorrow.setVouchAwardScale(BigDecimal.ZERO); // 插入时不用的字段
		// 担保比例
		debtBorrow.setVouchAwardAccount(BigDecimal.ZERO); // 插入时不用的字段
															// 总付出的担保奖励

		debtBorrow.setVoucherName("");// 插入时不用的字段
		debtBorrow.setVoucherLianxi("");// 插入时不用的字段
		debtBorrow.setVoucherAtt("");// 插入时不用的字段
		debtBorrow.setVouchjgName("");// 插入时不用的字段
		debtBorrow.setVouchjgLianxi("");// 插入时不用的字段
		debtBorrow.setVouchjgJs("");// 插入时不用的字段
		debtBorrow.setVouchjgXy("");// 插入时不用的字段
		debtBorrow.setFastStatus(0);// 插入时不用的字段
		debtBorrow.setVouchstatus(0);// 插入时不用的字段
		debtBorrow.setGroupStatus(0);// 插入时不用的字段
		debtBorrow.setGroupId(0);// 插入时不用的字段

		debtBorrow.setAwardStatus(0); // 插入时不用的字段 是否奖励
		debtBorrow.setAwardFalse(0); // 插入时不用的字段 出借失败是否也奖励
		// 插入时不用的字段 奖励金额
		debtBorrow.setAwardAccount(BigDecimal.ZERO);
		// 插入时不用的字段 按比例奖励
		debtBorrow.setAwardScale(BigDecimal.ZERO);
		// 插入时不用的字段 投标奖励总额
		debtBorrow.setAwardAccountAll(BigDecimal.ZERO);

		debtBorrow.setOpenAccount(0); // 插入时不用的字段 公开我的帐户资金情况
		debtBorrow.setOpenBorrow(0); // 插入时不用的字段 公开我的借款资金情况
		debtBorrow.setOpenTender(0); // 插入时不用的字段 公开我的投标资金情况
		debtBorrow.setOpenCredit(0); // 插入时不用的字段 公开我的信用额度情况
		// 是否可以评论
		debtBorrow.setCommentStaus(0);
		debtBorrow.setCommentTimes(0); // 插入时不用的字段 评论次数
		debtBorrow.setCommentUsertype(""); // 插入时不用的字段 可评论的用户
		debtBorrow.setDiyaContents(""); // 插入时不用的字段
		debtBorrow.setBorrowPawnApp(""); // 插入时不用的字段
		debtBorrow.setBorrowPawnAppUrl(""); // 插入时不用的字段
		debtBorrow.setBorrowPawnAuth(""); // 插入时不用的字段
		debtBorrow.setBorrowPawnAuthUrl(""); // 插入时不用的字段
		debtBorrow.setBorrowPawnFormalities(""); // 插入时不用的字段
		debtBorrow.setBorrowPawnFormalitiesUrl(""); // 插入时不用的字段
		debtBorrow.setBorrowPawnType(""); // 插入时不用的字段
		debtBorrow.setBorrowPawnTime(""); // 插入时不用的字段
		debtBorrow.setBorrowPawnDescription(""); // 插入时不用的字段
		debtBorrow.setBorrowPawnValue(""); // 插入时不用的字段
		debtBorrow.setBorrowPawnXin(""); // 插入时不用的字段
		debtBorrow.setOrderTop(""); // 插入时不用的字段 置顶时间
		// 初审核人
		debtBorrow.setVerifyUserid("0");
		// 正式发标时间
		debtBorrow.setVerifyTime("0");
		// 初审通过时间
		debtBorrow.setVerifyOverTime(0);
		// 初审核备注
		debtBorrow.setVerifyRemark("");
		debtBorrow.setVerifyContents(""); // 插入时不用的字段 审核备注
		debtBorrow.setVerifyStatus(0); // 插入时不用的字段
		// 复审核人
		debtBorrow.setReverifyUserid("0");
		// 复审核时间
		debtBorrow.setReverifyTime("0");
		// 复审核备注
		debtBorrow.setReverifyRemark("");
		debtBorrow.setReverifyStatus(0); // 插入时不用的字段
		debtBorrow.setReverifyContents(""); // 插入时不用的字段 审核复审标注
		debtBorrow.setUpfilesId(""); // 插入时不用的字段 发标上传图片
		debtBorrow.setBorrowRunningUse(""); // 插入时不用的字段 资金运转-用途
		debtBorrow.setBorrowRunningSoruce(""); // 插入时不用的字段 资金运转-来源
		// 担保机构 风险控制措施-机构
		debtBorrow.setBorrowMeasuresInstit(borrowBean.getBorrowMeasuresInstit());
		// 机构介绍
		debtBorrow.setBorrowCompanyInstruction(borrowBean.getBorrowCompanyInstruction());
		// 操作流程
		debtBorrow.setBorrowOperatingProcess(borrowBean.getBorrowOperatingProcess());
		// 抵押物信息 风险控制措施-抵押物
		debtBorrow.setBorrowMeasuresMort(borrowBean.getBorrowMeasuresMort());
		// 本息保障 险控制措施-措施
		debtBorrow.setBorrowMeasuresMea(borrowBean.getBorrowMeasuresMea());
		debtBorrow.setBorrowAnalysisPolicy(""); // 插入时不用的字段 政策及市场分析-政策支持
		debtBorrow.setBorrowAnalysisMarket(""); // 插入时不用的字段 政策及市场分析-市场分析
		debtBorrow.setBorrowCompany(""); // 插入时不用的字段 企业背景
		debtBorrow.setBorrowCompanyScope(""); // 插入时不用的字段 企业信息-营业范围
		debtBorrow.setBorrowCompanyBusiness(""); // 插入时不用的字段 企业信息-经营状况
		debtBorrow.setXmupfilesId(""); // 插入时不用的字段
		debtBorrow.setDyupfilesId(""); // 插入时不用的字段
		// 项目资料
		debtBorrow.setFiles(this.getUploadImage(borrowBean, "", borrowBean.getBorrowNid()));
		// 担保方式
		debtBorrow.setGuaranteeType(0);
		// 项目类型
		debtBorrow.setProjectType(Integer.valueOf(borrowBean.getProjectType()));

		if (!PROJECT_TYPE_HXF.equals(borrowBean.getProjectType())) {
			// 放款服务费
			String borrowServiceScale = this.getBorrowServiceScale(borrowBean.getProjectType(),
					borrowBean.getBorrowStyle(), Integer.valueOf(borrowBean.getBorrowPeriod()));
			debtBorrow.setServiceFeeRate(borrowServiceScale);
			// 还款服务费率
			debtBorrow.setManageFeeRate(this.getBorrowManagerScale(borrowBean.getProjectType(),
					borrowBean.getBorrowStyle(), Integer.parseInt(borrowBean.getBorrowPeriod())));
			// 收益差率
			debtBorrow.setDifferentialRate(this.getBorrowReturnScale(borrowBean.getProjectType(),
					borrowBean.getBorrowStyle(), Integer.parseInt(borrowBean.getBorrowPeriod())));
		} else if (PROJECT_TYPE_HXF.equals(borrowBean.getProjectType())) {
			// 放款服务费
			debtBorrow.setServiceFeeRate("0.00");
			JSONObject jsonObject = new JSONObject();
			jsonObject = this.getBorrowManagerScale(borrowBean.getProjectType(), borrowBean.getBorrowStyle(),
					Integer.valueOf(borrowBean.getBorrowPeriod()), jsonObject);
			if (StringUtils.isNotEmpty(jsonObject.getString("borrowManagerScale"))) {
				// 还款服务费率(上限)
				debtBorrow.setManageFeeRate(jsonObject.getString("borrowManagerScale"));
			}
			if (StringUtils.isNotEmpty(jsonObject.getString("borrowManagerScaleEnd"))) {
				// 还款服务费率(下限)
				debtBorrow.setBorrowManagerScaleEnd(jsonObject.getString("borrowManagerScaleEnd"));
			}
		}

		// 可出借平台_PC
		if (StringUtils.isEmpty(borrowBean.getCanTransactionPc())) {
			debtBorrow.setCanTransactionPc("0");
		} else {
			debtBorrow.setCanTransactionPc(borrowBean.getCanTransactionPc());
		}

		// 可出借平台_微网站
		if (StringUtils.isEmpty(borrowBean.getCanTransactionWei())) {
			debtBorrow.setCanTransactionWei("0");
		} else {
			debtBorrow.setCanTransactionWei(borrowBean.getCanTransactionWei());
		}

		// 可出借平台_IOS
		if (StringUtils.isEmpty(borrowBean.getCanTransactionIos())) {
			debtBorrow.setCanTransactionIos("0");
		} else {
			debtBorrow.setCanTransactionIos(borrowBean.getCanTransactionIos());
		}

		// 可出借平台_Android
		if (StringUtils.isEmpty(borrowBean.getCanTransactionAndroid())) {
			debtBorrow.setCanTransactionAndroid("0");
		} else {
			debtBorrow.setCanTransactionAndroid(borrowBean.getCanTransactionAndroid());
		}

		// 运营标签
		if (StringUtils.isEmpty(borrowBean.getOperationLabel())) {
			debtBorrow.setOperationLabel("0");
		} else {
			debtBorrow.setOperationLabel(borrowBean.getOperationLabel());
		}
		// 企业还是个人
		debtBorrow.setCompanyOrPersonal(borrowBean.getCompanyOrPersonal());
		// 定时发标
		debtBorrow.setOntime(0);
		debtBorrow.setBookingBeginTime(0);
		debtBorrow.setBookingEndTime(0);
		debtBorrow.setBookingStatus(0);
		debtBorrow.setBorrowAccountScaleAppoint(BigDecimal.ZERO);
		// 汇资管的内容设置
		this.setHZGInfo(borrowBean, debtBorrow);
		// 更新时间
		debtBorrow.setUpdatetime(systemNowDate);
		// 银行存管标识 0未进行银行存管 1已进行银行存管
		debtBorrow.setBankInputFlag(0);

		this.debtBorrowMapper.insertSelective(debtBorrow);

		// 个人信息
		this.insertBorrowManinfo(borrowNid, borrowBean, debtBorrow);
		// 公司信息
		this.insertBorrowUsers(borrowNid, borrowBean, debtBorrow);
		// 车辆信息
		this.insertBorrowCarinfo(borrowNid, borrowBean, debtBorrow);
		// 房产信息
		this.insertBorrowHouses(borrowNid, borrowBean, debtBorrow);
		// 认证信息
		this.insertBorrowCompanyAuthen(borrowNid, borrowBean, debtBorrow);
	}

	/**
	 * 更新
	 * 
	 * @param record
	 * @throws Exception
	 */
	@Override
	public void updateRecord(DebtBorrowCommonBean borrowBean) throws Exception {
		String borrowNid = borrowBean.getBorrowNid();
		if (StringUtils.isNotEmpty(borrowNid)) {
			DebtBorrowExample debtBorrowExample = new DebtBorrowExample();
			DebtBorrowExample.Criteria borrowCra = debtBorrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);

			List<DebtBorrowWithBLOBs> borrowList = this.debtBorrowMapper.selectByExampleWithBLOBs(debtBorrowExample);

			if (borrowList != null && borrowList.size() == 1) {
				DebtBorrowWithBLOBs record = borrowList.get(0);

				debtBorrowExample = new DebtBorrowExample();
				borrowCra = debtBorrowExample.createCriteria();
				borrowCra.andBorrowPreNidEqualTo(record.getBorrowPreNid());

				List<DebtBorrowWithBLOBs> borrowAllList = this.debtBorrowMapper
						.selectByExampleWithBLOBs(debtBorrowExample);

				if (borrowAllList != null && borrowAllList.size() > 0) {
					for (DebtBorrowWithBLOBs borrow : borrowAllList) {
						// 借款表更新
						this.updateBorrowCommonData(borrowBean, borrow, borrowNid);
					}
				}
			}
		}
	}

	/**
	 * 借款表更新
	 * 
	 * @param debtBorrowBean
	 * @param debtBorrow
	 * @throws Exception
	 */
	@Override
	public void updateBorrowCommonData(DebtBorrowCommonBean debtBorrowBean, DebtBorrowWithBLOBs debtBorrow,
			String borrowMainNid) throws Exception {

		// 插入时间
		long systemNowDateLong = new Date().getTime() / 1000;
		Date systemNowDate = GetDate.getDate(systemNowDateLong);
		// 项目编号
		String borrowNid = debtBorrow.getBorrowNid();

		int status = debtBorrow.getStatus();
		if (borrowMainNid.equals(borrowNid)) {
			debtBorrow.setBorrowLevel(debtBorrowBean.getBorrowLevel());
			debtBorrow.setBorrowIncreaseMoney(debtBorrowBean.getBorrowIncreaseMoney());
			debtBorrow.setBorrowInterestCoupon(debtBorrowBean.getBorrowInterestCoupon() != null ? debtBorrowBean
					.getBorrowInterestCoupon() : 0);
			debtBorrow.setBorrowTasteMoney(debtBorrowBean.getBorrowTasteMoney() != null ? debtBorrowBean
					.getBorrowTasteMoney() : 0);

		}
		// 用户名
		if (status == 0 && borrowMainNid.equals(borrowNid)) {
			// 借款用户
			UsersExample example = new UsersExample();
			UsersExample.Criteria cra = example.createCriteria();
			cra.andUsernameEqualTo(debtBorrowBean.getUsername());
			cra.andOpenAccountEqualTo(1);
			cra.andStatusEqualTo(0);
			List<Users> userList = this.usersMapper.selectByExample(example);
			debtBorrow.setUserId(userList.get(0).getUserId());
			// 借款人用户名
			debtBorrow.setBorrowUserName(debtBorrowBean.getUsername());
			// 项目申请人
			debtBorrow.setApplicant(debtBorrowBean.getApplicant());
		}

		// 项目名称
		if (borrowMainNid.equals(borrowNid)) {
			debtBorrow.setName(debtBorrowBean.getName());
		}
		// 借款方式
		// 车辆抵押:2 房产抵押:1
		if (StringUtils.equals("2", debtBorrowBean.getTypeCar())) {
			debtBorrow.setType(debtBorrowBean.getTypeCar());
		}

		if (StringUtils.equals("1", debtBorrowBean.getTypeHouse())) {
			debtBorrow.setType(debtBorrowBean.getTypeHouse());
		}

		if (StringUtils.equals("2", debtBorrowBean.getTypeCar())
				&& StringUtils.equals("1", debtBorrowBean.getTypeHouse())) {
			debtBorrow.setType("3");
		}

		if (StringUtils.isEmpty(debtBorrow.getType())) {
			debtBorrow.setType("0");
		}

		if (status == 0 && borrowMainNid.equals(borrowNid)) {
			// 冻结额度 初审通过不能修改
			debtBorrow.setAmountAccount(new BigDecimal(debtBorrowBean.getAccount()));
			// 借款总金额 初审通过不能修改
			debtBorrow.setAccount(new BigDecimal(debtBorrowBean.getAccount()));
		}

		// 车辆抵押:2 房产抵押:1
		if (StringUtils.equals("2", debtBorrowBean.getTypeCar())) {
			debtBorrow.setType(debtBorrowBean.getTypeCar());
		}

		if (StringUtils.equals("1", debtBorrowBean.getTypeHouse())) {
			debtBorrow.setType(debtBorrowBean.getTypeHouse());
		}

		if (StringUtils.equals("2", debtBorrowBean.getTypeCar())
				&& StringUtils.equals("1", debtBorrowBean.getTypeHouse())) {
			debtBorrow.setType("3");
		}

		// 财务状况
		if (StringUtils.isEmpty(debtBorrowBean.getAccountContents())) {
			debtBorrow.setAccountContents(StringUtils.EMPTY);
		} else {
			debtBorrow.setAccountContents(debtBorrowBean.getAccountContents());
		}

		// 借款用途
		debtBorrow.setBorrowUse(debtBorrowBean.getBorrowUse());
		if (status == 0 && borrowMainNid.equals(borrowNid)) {
			// 还款方式 初审通过不能修改
			debtBorrow.setBorrowStyle(debtBorrowBean.getBorrowStyle());
			// 借款期限 初审通过不能修改
			debtBorrow.setBorrowPeriod(Integer.valueOf(debtBorrowBean.getBorrowPeriod()));
			// 借款利率 初审通过不能修改
			debtBorrow.setBorrowApr(new BigDecimal(debtBorrowBean.getBorrowApr()));
		}

		// 项目描述
		if (StringUtils.isEmpty(debtBorrowBean.getBorrowContents())) {
			debtBorrow.setBorrowContents(StringUtils.EMPTY);
		} else {
			debtBorrow.setBorrowContents(debtBorrowBean.getBorrowContents());
		}

		if (status == 0 && borrowMainNid.equals(borrowNid)) {
			// 借款有效时间 初审通过不能修改
			if (StringUtils.isNotEmpty(debtBorrowBean.getBorrowValidTime())) {
				debtBorrow.setBorrowValidTime(Integer.valueOf(debtBorrowBean.getBorrowValidTime()));
			} else {
				debtBorrow.setBorrowValidTime(0);
			}
		}

		if (borrowMainNid.equals(borrowNid)) {
			// 最低投标金额
			if (StringUtils.isNotEmpty(debtBorrowBean.getTenderAccountMin())) {
				debtBorrow.setTenderAccountMin(Integer.valueOf(debtBorrowBean.getTenderAccountMin()));
			} else {
				debtBorrow.setTenderAccountMin(0);
			}

			// 最高投标金额
			if (StringUtils.isNotEmpty(debtBorrowBean.getTenderAccountMax())) {
				debtBorrow.setTenderAccountMax(Integer.valueOf(debtBorrowBean.getTenderAccountMax()));
			} else {
				debtBorrow.setTenderAccountMax(0);
			}
		}

		// 借款初审列表迁移 风控初审：
		if ("BORROW_FIRST".equals(debtBorrowBean.getMoveFlag()) && borrowMainNid.equals(borrowNid)) {
			AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
			int time = Integer.valueOf(String.valueOf(systemNowDateLong));

			// 剩余的金额
			debtBorrow.setBorrowAccountWait(debtBorrow.getAccount());
			debtBorrow.setBorrowAccountWaitAppoint(debtBorrow.getAccount());
			// 初审人员ID
			debtBorrow.setVerifyUserid(adminSystem.getId());
			// 初审备注
			debtBorrow.setVerifyRemark(debtBorrowBean.getVerifyRemark());
			// 初审通过时间
			debtBorrow.setVerifyOverTime(time);
			// 当发标状态为立即发标时插入系统时间
			if (debtBorrowBean.getVerifyStatus() != null && StringUtils.isNotEmpty(debtBorrowBean.getVerifyStatus())) {
				// 发标方式为”暂不发标 3“或者”定时发标 1“时，项目状态变为”待发布“
				if (Integer.valueOf(debtBorrowBean.getVerifyStatus()) == 1
						|| Integer.valueOf(debtBorrowBean.getVerifyStatus()) == 3) {
					// 定时发标
					if (Integer.valueOf(debtBorrowBean.getVerifyStatus()) == 1) {
						// 发标时间
						debtBorrow.setOntime(GetDate.strYYYYMMDDHHMMSS2Timestamp(debtBorrowBean.getOntime()));
						debtBorrow.setBookingBeginTime(GetDate.strYYYYMMDDHHMMSS2Timestamp(debtBorrowBean
								.getBookingBeginTime()));
						debtBorrow.setBookingEndTime(GetDate.strYYYYMMDDHHMMSS2Timestamp(debtBorrowBean
								.getBookingEndTime()));
					} else if (Integer.valueOf(debtBorrowBean.getVerifyStatus()) == 3) {
						// 发标时间
						debtBorrow.setOntime(0);
						debtBorrow.setBookingBeginTime(0);
						debtBorrow.setBookingEndTime(0);

					}
					debtBorrow.setBookingStatus(0);
					debtBorrow.setBorrowAccountScaleAppoint(BigDecimal.ZERO);
					// 状态
					debtBorrow.setStatus(0);
					// 初审状态
					debtBorrow.setVerifyStatus(Integer.valueOf(debtBorrowBean.getVerifyStatus()));
				}
				// 发标方式为”立即发标 2“时，项目状态变为”出借中
				else if (Integer.valueOf(debtBorrowBean.getVerifyStatus()) == 2) {
					// 借款到期时间
					debtBorrow.setBorrowEndTime(String.valueOf(time + debtBorrow.getBorrowValidTime() * 86400));
					// 是否可以进行借款
					debtBorrow.setBorrowStatus(1);
					// 初审时间
					debtBorrow.setVerifyTime(String.valueOf(GetDate.getNowTime10()));
					// 发标的状态
					debtBorrow.setVerifyStatus(Integer.valueOf(debtBorrowBean.getVerifyStatus()));
					// 状态
					debtBorrow.setStatus(1);
					// borrowNid，借款的borrowNid,account借款总额
					RedisUtils.set(CustomConstants.DEBT_REDITS + borrowNid, debtBorrow.getAccount().toString());
				}
			}
		}

		// 担保机构 风险控制措施-机构
		if (StringUtils.isEmpty(debtBorrowBean.getBorrowMeasuresInstit())) {
			debtBorrow.setBorrowMeasuresInstit(StringUtils.EMPTY);
		} else {
			debtBorrow.setBorrowMeasuresInstit(debtBorrowBean.getBorrowMeasuresInstit());
		}

		// 抵押物信息 风险控制措施-抵押物
		if (StringUtils.isEmpty(debtBorrowBean.getBorrowMeasuresMort())) {
			debtBorrow.setBorrowMeasuresMort(StringUtils.EMPTY);
		} else {
			debtBorrow.setBorrowMeasuresMort(debtBorrowBean.getBorrowMeasuresMort());
		}

		// 本息保障 险控制措施-措施
		if (StringUtils.isEmpty(debtBorrowBean.getBorrowMeasuresMea())) {
			debtBorrow.setBorrowMeasuresMea(StringUtils.EMPTY);
		} else {
			debtBorrow.setBorrowMeasuresMea(debtBorrowBean.getBorrowMeasuresMea());
		}

		// 机构介绍
		if (StringUtils.isEmpty(debtBorrowBean.getBorrowCompanyInstruction())) {
			debtBorrow.setBorrowCompanyInstruction(StringUtils.EMPTY);
		} else {
			debtBorrow.setBorrowCompanyInstruction(debtBorrowBean.getBorrowCompanyInstruction());
		}

		// 操作流程
		if (StringUtils.isEmpty(debtBorrowBean.getBorrowOperatingProcess())) {
			debtBorrow.setBorrowOperatingProcess(StringUtils.EMPTY);
		} else {
			debtBorrow.setBorrowOperatingProcess(debtBorrowBean.getBorrowOperatingProcess());
		}

		String files = debtBorrow.getFiles();
		// 项目资料
		debtBorrow.setFiles(this.getUploadImage(debtBorrowBean, files, debtBorrow.getBorrowNid()));
		// 担保方式
		debtBorrow.setGuaranteeType(0);
		if (status == 0 && borrowMainNid.equals(borrowNid)) {
			// 项目类型 初审通过不能修改
			debtBorrow.setProjectType(Integer.valueOf(debtBorrowBean.getProjectType()));
		}

		if (borrowMainNid.equals(borrowNid)) {
			// 可出借平台_PC
			if (StringUtils.isEmpty(debtBorrowBean.getCanTransactionPc())) {
				debtBorrow.setCanTransactionPc("0");
			} else {
				debtBorrow.setCanTransactionPc(debtBorrowBean.getCanTransactionPc());
			}

			// 可出借平台_微网站
			if (StringUtils.isEmpty(debtBorrowBean.getCanTransactionWei())) {
				debtBorrow.setCanTransactionWei("0");
			} else {
				debtBorrow.setCanTransactionWei(debtBorrowBean.getCanTransactionWei());
			}

			// 可出借平台_IOS
			if (StringUtils.isEmpty(debtBorrowBean.getCanTransactionIos())) {
				debtBorrow.setCanTransactionIos("0");
			} else {
				debtBorrow.setCanTransactionIos(debtBorrowBean.getCanTransactionIos());
			}

			// 可出借平台_Android
			if (StringUtils.isEmpty(debtBorrowBean.getCanTransactionAndroid())) {
				debtBorrow.setCanTransactionAndroid("0");
			} else {
				debtBorrow.setCanTransactionAndroid(debtBorrowBean.getCanTransactionAndroid());
			}

			// 运营标签
			if (StringUtils.isEmpty(debtBorrowBean.getOperationLabel())) {
				debtBorrow.setOperationLabel("0");
			} else {
				debtBorrow.setOperationLabel(debtBorrowBean.getOperationLabel());
			}
		}
		// 企业还是个人
		debtBorrow.setCompanyOrPersonal(debtBorrowBean.getCompanyOrPersonal());

		if (status == 0 && borrowMainNid.equals(borrowNid)) {
			if (!PROJECT_TYPE_HXF.equals(debtBorrowBean.getProjectType())) {
				// 放款服务费
				String borrowServiceScale = this.getBorrowServiceScale(debtBorrowBean.getProjectType(),
						debtBorrowBean.getBorrowStyle(), Integer.valueOf(debtBorrowBean.getBorrowPeriod()));
				debtBorrow.setServiceFeeRate(borrowServiceScale);
				// 还款服务费率
				debtBorrow.setManageFeeRate(this.getBorrowManagerScale(debtBorrowBean.getProjectType(),
						debtBorrowBean.getBorrowStyle(), Integer.parseInt(debtBorrowBean.getBorrowPeriod())));
				// 收益差率
				debtBorrow.setDifferentialRate(this.getBorrowReturnScale(debtBorrowBean.getProjectType(),
						debtBorrowBean.getBorrowStyle(), Integer.parseInt(debtBorrowBean.getBorrowPeriod())));
			} else if (PROJECT_TYPE_HXF.equals(debtBorrowBean.getProjectType())) {
				// 放款服务费
				debtBorrow.setServiceFeeRate("0.00");
				JSONObject jsonObject = new JSONObject();
				jsonObject = this.getBorrowManagerScale(debtBorrowBean.getProjectType(),
						debtBorrowBean.getBorrowStyle(), Integer.valueOf(debtBorrowBean.getBorrowPeriod()), jsonObject);
				if (StringUtils.isNotEmpty(jsonObject.getString("borrowManagerScale"))) {
					// 还款服务费率(上限)
					debtBorrow.setManageFeeRate(jsonObject.getString("borrowManagerScale"));
				}
				if (StringUtils.isNotEmpty(jsonObject.getString("borrowManagerScaleEnd"))) {
					// 还款服务费率(下限)
					debtBorrow.setBorrowManagerScaleEnd(jsonObject.getString("borrowManagerScaleEnd"));
				}
			}
		}

		// 汇资管的内容设置
		this.setHZGInfo(debtBorrowBean, debtBorrow);

		// 更新时间
		debtBorrow.setUpdatetime(systemNowDate);

		this.debtBorrowMapper.updateByPrimaryKeySelective(debtBorrow);

		// 个人信息
		this.insertBorrowManinfo(borrowNid, debtBorrowBean, debtBorrow);
		// 公司信息
		this.insertBorrowUsers(borrowNid, debtBorrowBean, debtBorrow);
		// 车辆信息
		this.insertBorrowCarinfo(borrowNid, debtBorrowBean, debtBorrow);
		// 房产信息
		this.insertBorrowHouses(borrowNid, debtBorrowBean, debtBorrow);
		// 认证信息
		this.insertBorrowCompanyAuthen(borrowNid, debtBorrowBean, debtBorrow);
	}

	/**
	 * 汇资管的内容设置
	 * 
	 * @param borrowBean
	 * @param debtBorrow
	 */
	private void setHZGInfo(DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs debtBorrow) {

		// 售价预估
		if (StringUtils.isEmpty(borrowBean.getDisposalPriceEstimate())) {
			debtBorrow.setDisposalPriceEstimate(StringUtils.EMPTY);
		} else {
			debtBorrow.setDisposalPriceEstimate(borrowBean.getDisposalPriceEstimate());
		}
		// 处置周期
		if (StringUtils.isEmpty(borrowBean.getDisposalPeriod())) {
			debtBorrow.setDisposalPeriod(StringUtils.EMPTY);
		} else {
			debtBorrow.setDisposalPeriod(borrowBean.getDisposalPeriod());
		}
		// 处置渠道
		if (StringUtils.isEmpty(borrowBean.getDisposalChannel())) {
			debtBorrow.setDisposalChannel(StringUtils.EMPTY);
		} else {
			debtBorrow.setDisposalChannel(borrowBean.getDisposalChannel());
		}
		// 处置结果预案
		if (StringUtils.isEmpty(borrowBean.getDisposalResult())) {
			debtBorrow.setDisposalResult(StringUtils.EMPTY);
		} else {
			debtBorrow.setDisposalResult(borrowBean.getDisposalResult());
		}
		// 备注说明
		if (StringUtils.isEmpty(borrowBean.getDisposalNote())) {
			debtBorrow.setDisposalNote(StringUtils.EMPTY);
		} else {
			debtBorrow.setDisposalNote(borrowBean.getDisposalNote());
		}

		// 项目名称
		if (StringUtils.isEmpty(borrowBean.getDisposalProjectName())) {
			debtBorrow.setDisposalProjectName(StringUtils.EMPTY);
		} else {
			debtBorrow.setDisposalProjectName(borrowBean.getDisposalProjectName());
		}
		// 项目类型
		if (StringUtils.isEmpty(borrowBean.getDisposalProjectType())) {
			debtBorrow.setDisposalProjectType(StringUtils.EMPTY);
		} else {
			debtBorrow.setDisposalProjectType(borrowBean.getDisposalProjectType());
		}
		// 所在地区
		if (StringUtils.isEmpty(borrowBean.getDisposalArea())) {
			debtBorrow.setDisposalArea(StringUtils.EMPTY);
		} else {
			debtBorrow.setDisposalArea(borrowBean.getDisposalArea());
		}
		// 预估价值
		if (StringUtils.isEmpty(borrowBean.getDisposalPredictiveValue())) {
			debtBorrow.setDisposalPredictiveValue(StringUtils.EMPTY);
		} else {
			debtBorrow.setDisposalPredictiveValue(borrowBean.getDisposalPredictiveValue());
		}
		// 权属类别
		if (StringUtils.isEmpty(borrowBean.getDisposalOwnershipCategory())) {
			debtBorrow.setDisposalOwnershipCategory(StringUtils.EMPTY);
		} else {
			debtBorrow.setDisposalOwnershipCategory(borrowBean.getDisposalOwnershipCategory());
		}
		// 资产成因
		if (StringUtils.isEmpty(borrowBean.getDisposalAssetOrigin())) {
			debtBorrow.setDisposalAssetOrigin(StringUtils.EMPTY);
		} else {
			debtBorrow.setDisposalAssetOrigin(borrowBean.getDisposalAssetOrigin());
		}
		// 附件信息
		if (StringUtils.isEmpty(borrowBean.getDisposalAttachmentInfo())) {
			debtBorrow.setDisposalAttachmentInfo(StringUtils.EMPTY);
		} else {
			debtBorrow.setDisposalAttachmentInfo(borrowBean.getDisposalAttachmentInfo());
		}
	}

	/**
	 * 车辆信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param borrow
	 * @return
	 */
	@Override
	public int insertBorrowCarinfo(String borrowNid, DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs borrow) {
		DebtCarInfoExample borrowCarinfoExample = new DebtCarInfoExample();
		DebtCarInfoExample.Criteria borrowCarinfoCra = borrowCarinfoExample.createCriteria();
		borrowCarinfoCra.andBorrowNidEqualTo(borrowNid);
		this.debtCarInfoMapper.deleteByExample(borrowCarinfoExample);

		if (StringUtils.equals("2", borrowBean.getTypeCar())) {
			List<DebtBorrowCommonCar> borrowCommonCarList = borrowBean.getBorrowCarinfoList();
			if (borrowCommonCarList != null && borrowCommonCarList.size() > 0) {
				for (DebtBorrowCommonCar borrowCommonCar : borrowCommonCarList) {
					DebtCarInfo borrowCarinfo = new DebtCarInfo();
					// 品牌
					if (StringUtils.isNotEmpty(borrowCommonCar.getBrand())) {
						borrowCarinfo.setBrand(borrowCommonCar.getBrand());
					} else {
						borrowCarinfo.setBrand(StringUtils.EMPTY);
					}

					// 型号
					if (StringUtils.isNotEmpty(borrowCommonCar.getModel())) {
						borrowCarinfo.setModel(borrowCommonCar.getModel());
					} else {
						borrowCarinfo.setModel(StringUtils.EMPTY);
					}

					// 车系
					if (StringUtils.isNotEmpty(borrowCommonCar.getCarseries())) {
						borrowCarinfo.setCarseries(borrowCommonCar.getCarseries());
					} else {
						borrowCarinfo.setCarseries(StringUtils.EMPTY);
					}

					// 颜色
					if (StringUtils.isNotEmpty(borrowCommonCar.getColor())) {
						borrowCarinfo.setColor(borrowCommonCar.getColor());
					} else {
						borrowCarinfo.setColor(StringUtils.EMPTY);
					}

					// 出厂年份
					if (StringUtils.isNotEmpty(borrowCommonCar.getYear())) {
						borrowCarinfo.setYear(borrowCommonCar.getYear());
					} else {
						borrowCarinfo.setYear(StringUtils.EMPTY);
					}

					// 产地
					if (StringUtils.isNotEmpty(borrowCommonCar.getPlace())) {
						borrowCarinfo.setPlace(borrowCommonCar.getPlace());
					} else {
						borrowCarinfo.setPlace(StringUtils.EMPTY);
					}

					// 购买日期
					if (StringUtils.isNotEmpty(borrowCommonCar.getBuytime())) {
						borrowCarinfo.setBuytime(Integer.valueOf(String.valueOf(GetDate.str2Timestamp(
								borrowCommonCar.getBuytime()).getTime() / 1000)));
					} else {
						borrowCarinfo.setBuytime(0);
					}
					// 1有保险2无保险
					if (StringUtils.isNotEmpty(borrowCommonCar.getIsSafe())) {
						borrowCarinfo.setIsSafe(Integer.valueOf(borrowCommonCar.getIsSafe()));
					} else {
						borrowCarinfo.setIsSafe(0);
					}

					// 购买价
					if (StringUtils.isNotEmpty(borrowCommonCar.getPrice())) {
						borrowCarinfo.setPrice(new BigDecimal(borrowCommonCar.getPrice()));
					} else {
						borrowCarinfo.setPrice(BigDecimal.ZERO);
					}
					// 评估价
					if (StringUtils.isNotEmpty(borrowCommonCar.getToprice())) {
						borrowCarinfo.setToprice(new BigDecimal(borrowCommonCar.getToprice()));
					} else {
						borrowCarinfo.setToprice(BigDecimal.ZERO);
					}

					borrowCarinfo.setBorrowNid(borrowNid);
					borrowCarinfo.setBorrowPreNid(borrow.getBorrowPreNid());

					this.debtCarInfoMapper.insertSelective(borrowCarinfo);
				}
			}
		}

		return 0;
	}

	/**
	 * 个人信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param debtBorrow
	 * @return
	 */
	@Override
	public int insertBorrowManinfo(String borrowNid, DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs debtBorrow) {
		DebtUsersInfoExample debtUsersInfoExample = new DebtUsersInfoExample();
		DebtUsersInfoExample.Criteria debtUsersInfoCra = debtUsersInfoExample.createCriteria();
		debtUsersInfoCra.andBorrowNidEqualTo(borrowNid);
		this.debtUsersInfoMapper.deleteByExample(debtUsersInfoExample);

		if (StringUtils.equals("2", borrowBean.getCompanyOrPersonal())) {

			// 个人信息
			if (StringUtils.isNotEmpty(borrowBean.getManname()) || StringUtils.isNotEmpty(borrowBean.getOld())
					|| StringUtils.isNotEmpty(borrowBean.getIndustry())
					|| StringUtils.isNotEmpty(borrowBean.getLocation_p())
					|| StringUtils.isNotEmpty(borrowBean.getLocation_c())
					|| StringUtils.isNotEmpty(borrowBean.getBusiness())
					|| StringUtils.isNotEmpty(borrowBean.getWtime())
					|| StringUtils.isNotEmpty(borrowBean.getUserCredit())) {

				DebtUsersInfo debtUsersinfo = new DebtUsersInfo();

				debtUsersinfo.setBorrowNid(borrowNid);
				debtUsersinfo.setBorrowPreNid(debtBorrow.getBorrowPreNid());
				// 姓名
				if (StringUtils.isNotEmpty(borrowBean.getManname())) {
					debtUsersinfo.setName(borrowBean.getManname());
				} else {
					debtUsersinfo.setName(StringUtils.EMPTY);
				}
				// 性别
				if (StringUtils.isNotEmpty(borrowBean.getSex())) {
					debtUsersinfo.setSex(Integer.valueOf(borrowBean.getSex()));
				} else {
					debtUsersinfo.setSex(0);
				}
				// 年龄
				if (StringUtils.isNotEmpty(borrowBean.getOld())) {
					debtUsersinfo.setOld(Integer.valueOf(borrowBean.getOld()));
				} else {
					debtUsersinfo.setOld(0);
				}
				// 婚姻
				if (StringUtils.isNotEmpty(borrowBean.getMerry())) {
					debtUsersinfo.setMerry(Integer.valueOf(borrowBean.getMerry()));
				} else {
					debtUsersinfo.setMerry(0);
				}
				// 岗位职业
				if (StringUtils.isNotEmpty(borrowBean.getPosition())) {
					debtUsersinfo.setPosition(borrowBean.getPosition());
					;
				}
				// 省
				if (StringUtils.isNotEmpty(borrowBean.getLocation_p())) {
					debtUsersinfo.setPro(borrowBean.getLocation_p());
				} else {
					debtUsersinfo.setPro(StringUtils.EMPTY);
				}
				// 市
				if (StringUtils.isNotEmpty(borrowBean.getLocation_c())) {
					debtUsersinfo.setCity(borrowBean.getLocation_c());
				} else {
					debtUsersinfo.setCity(StringUtils.EMPTY);
				}

				// 公司规模
				debtUsersinfo.setSize(StringUtils.EMPTY);

				// 公司月营业额
				debtUsersinfo.setBusiness(BigDecimal.ZERO);

				// 行业
				if (StringUtils.isNotEmpty(borrowBean.getIndustry())) {
					debtUsersinfo.setIndustry(borrowBean.getIndustry());
				} else {
					debtUsersinfo.setIndustry(StringUtils.EMPTY);
				}

				// 现单位工作时间
				if (StringUtils.isNotEmpty(borrowBean.getWtime())) {
					debtUsersinfo.setWtime(borrowBean.getWtime());
				} else {
					debtUsersinfo.setWtime(StringUtils.EMPTY);
				}

				// 授信额度
				if (StringUtils.isNotEmpty(borrowBean.getUserCredit())) {
					debtUsersinfo.setCredit(Integer.valueOf((borrowBean.getUserCredit())));
				} else {
					debtUsersinfo.setCredit(0);
				}

				this.debtUsersInfoMapper.insert(debtUsersinfo);
			}
		} else {
			if (StringUtils.isNotEmpty(borrowBean.getSize())) {
				DebtUsersInfo debtUsersInfo = new DebtUsersInfo();

				debtUsersInfo.setBorrowNid(borrowNid);
				debtUsersInfo.setBorrowPreNid(debtBorrow.getBorrowPreNid());

				// 公司规模
				if (StringUtils.isNotEmpty(borrowBean.getSize())) {
					debtUsersInfo.setSize(borrowBean.getSize());
				} else {
					debtUsersInfo.setSize(StringUtils.EMPTY);
				}

				// 公司月营业额
				if (StringUtils.isNotEmpty(borrowBean.getBusiness())) {
					debtUsersInfo.setBusiness(new BigDecimal(borrowBean.getBusiness()));
				} else {
					debtUsersInfo.setBusiness(BigDecimal.ZERO);
				}
				this.debtUsersInfoMapper.insertSelective(debtUsersInfo);
			}
		}

		return 0;
	}

	/**
	 * 公司信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param borrow
	 * @return
	 */
	@Override
	public int insertBorrowUsers(String borrowNid, DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs borrow) {
		DebtCompanyInfoExample debtCompanyInfoExample = new DebtCompanyInfoExample();
		DebtCompanyInfoExample.Criteria debtCompanyInfoCra = debtCompanyInfoExample.createCriteria();
		debtCompanyInfoCra.andBorrowNidEqualTo(borrowNid);
		this.debtCompanyInfoMapper.deleteByExample(debtCompanyInfoExample);

		if (StringUtils.equals("1", borrowBean.getCompanyOrPersonal())) {
			// 公司信息
			if (StringUtils.isNotEmpty(borrowBean.getBuName()) || StringUtils.isNotEmpty(borrowBean.getRegCaptial())
					|| StringUtils.isNotEmpty(borrowBean.getUserIndustry())
					|| StringUtils.isNotEmpty(borrowBean.getLocation_pro())
					|| StringUtils.isNotEmpty(borrowBean.getLocation_cro())
					|| StringUtils.isNotEmpty(borrowBean.getLocation_aro())
					|| StringUtils.isNotEmpty(borrowBean.getLitigation())
					|| StringUtils.isNotEmpty(borrowBean.getCreReport())
					|| StringUtils.isNotEmpty(borrowBean.getCredit()) || StringUtils.isNotEmpty(borrowBean.getStaff())
					|| StringUtils.isNotEmpty(borrowBean.getOtherInfo())
					|| StringUtils.isNotEmpty(borrowBean.getComRegTime())) {
				DebtCompanyInfo debtCompanyInfo = new DebtCompanyInfo();

				debtCompanyInfo.setBorrowNid(borrowNid);
				debtCompanyInfo.setBorrowPreNid(borrow.getBorrowPreNid());

				if (StringUtils.isNotEmpty(borrowBean.getBuName())) {
					debtCompanyInfo.setUsername(borrowBean.getBuName());
				} else {
					debtCompanyInfo.setUsername(StringUtils.EMPTY);
				}

				if (StringUtils.isNotEmpty(borrowBean.getLocation_pro())) {
					debtCompanyInfo.setProvince(borrowBean.getLocation_pro());
				} else {
					debtCompanyInfo.setProvince(StringUtils.EMPTY);
				}

				if (StringUtils.isNotEmpty(borrowBean.getLocation_cro())) {
					debtCompanyInfo.setCity(borrowBean.getLocation_cro());
				} else {
					debtCompanyInfo.setCity(StringUtils.EMPTY);
				}

				if (StringUtils.isNotEmpty(borrowBean.getLocation_aro())) {
					debtCompanyInfo.setArea(borrowBean.getLocation_aro());
				} else {
					debtCompanyInfo.setArea(StringUtils.EMPTY);
				}

				if (StringUtils.isNotEmpty(borrowBean.getRegCaptial())) {
					debtCompanyInfo.setRegCaptial(Integer.valueOf(borrowBean.getRegCaptial()));
				} else {
					debtCompanyInfo.setRegCaptial(0);
				}

				if (StringUtils.isNotEmpty(borrowBean.getUserIndustry())) {
					debtCompanyInfo.setIndustry(borrowBean.getUserIndustry());
				} else {
					debtCompanyInfo.setIndustry(StringUtils.EMPTY);
				}

				if (StringUtils.isNotEmpty(borrowBean.getLitigation())) {
					debtCompanyInfo.setLitigation(borrowBean.getLitigation());
				} else {
					debtCompanyInfo.setLitigation(StringUtils.EMPTY);
				}

				if (StringUtils.isNotEmpty(borrowBean.getCreReport())) {
					debtCompanyInfo.setCreReport(borrowBean.getCreReport());
				} else {
					debtCompanyInfo.setCreReport(StringUtils.EMPTY);
				}

				if (StringUtils.isNotEmpty(borrowBean.getCredit())) {
					debtCompanyInfo.setCredit(Integer.valueOf(borrowBean.getCredit()));
				} else {
					debtCompanyInfo.setCredit(0);
				}

				if (StringUtils.isNotEmpty(borrowBean.getStaff())) {
					debtCompanyInfo.setStaff(Integer.valueOf(borrowBean.getStaff()));
				} else {
					debtCompanyInfo.setStaff(0);
				}

				if (StringUtils.isNotEmpty(borrowBean.getOtherInfo())) {
					debtCompanyInfo.setOtherInfo(borrowBean.getOtherInfo());
				} else {
					debtCompanyInfo.setOtherInfo(StringUtils.EMPTY);
				}

				if (StringUtils.isNotEmpty(borrowBean.getComRegTime())) {
					debtCompanyInfo.setComRegTime(borrowBean.getComRegTime());
				} else {
					debtCompanyInfo.setComRegTime(StringUtils.EMPTY);
				}

				this.debtCompanyInfoMapper.insertSelective(debtCompanyInfo);
			}
		}

		return 0;
	}

	/**
	 * 房产信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param debtBorrow
	 * @return
	 */
	@Override
	public int insertBorrowHouses(String borrowNid, DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs debtBorrow) {
		if (StringUtils.equals("1", borrowBean.getTypeHouse())) {
			DebtHouseInfoExample debtHouseInfoExample = new DebtHouseInfoExample();
			DebtHouseInfoExample.Criteria debtHouseInfoCra = debtHouseInfoExample.createCriteria();
			debtHouseInfoCra.andBorrowNidEqualTo(borrowNid);
			this.debtHouseInfoMapper.deleteByExample(debtHouseInfoExample);
			List<DebtHouseInfo> debtHouseInfoList = borrowBean.getBorrowHousesList();
			if (debtHouseInfoList != null && debtHouseInfoList.size() > 0) {
				for (DebtHouseInfo debtHouseInfo : debtHouseInfoList) {
					debtHouseInfo.setBorrowNid(borrowNid);
					debtHouseInfo.setBorrowPreNid(debtBorrow.getBorrowPreNid());
					this.debtHouseInfoMapper.insertSelective(debtHouseInfo);
				}
			}
		}
		return 0;
	}

	/**
	 * 认证信息
	 * 
	 * @param borrowNid
	 * @param borrowBean
	 * @param borrow
	 * @return
	 */
	@Override
	public int insertBorrowCompanyAuthen(String borrowNid, DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs borrow) {
		DebtCompanyAuthenExample debtCompanyAuthenExample = new DebtCompanyAuthenExample();
		DebtCompanyAuthenExample.Criteria cra = debtCompanyAuthenExample.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		this.debtCompanyAuthenMapper.deleteByExample(debtCompanyAuthenExample);
		List<DebtBorrowCommonCompanyAuthen> borrowCommonCompanyAuthenList = borrowBean
				.getBorrowCommonCompanyAuthenList();
		if (borrowCommonCompanyAuthenList != null && borrowCommonCompanyAuthenList.size() > 0) {
			for (DebtBorrowCommonCompanyAuthen borrowCommonCompanyAuthen : borrowCommonCompanyAuthenList) {
				DebtCompanyAuthen debtCompanyAuthen = new DebtCompanyAuthen();
				debtCompanyAuthen.setAuthenName(borrowCommonCompanyAuthen.getAuthenName());
				debtCompanyAuthen.setAuthenSortKey(Integer.valueOf(borrowCommonCompanyAuthen.getAuthenSortKey()));
				debtCompanyAuthen.setAuthenTime(borrowCommonCompanyAuthen.getAuthenTime());
				debtCompanyAuthen.setBorrowNid(borrowNid);
				debtCompanyAuthen.setBorrowPreNid(borrow.getBorrowPreNid());
				this.debtCompanyAuthenMapper.insertSelective(debtCompanyAuthen);
			}
		}
		return 0;
	}

	/**
	 * 项目类型
	 * 
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<BorrowProjectType> borrowProjectTypeList(String projectTypeCd) {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
		if (StringUtils.isNotEmpty(projectTypeCd)) {
			cra.andBorrowProjectTypeEqualTo(projectTypeCd);
		}
		// 不查询融通宝相关
		cra.andBorrowNameNotEqualTo(CustomConstants.RTB);
		return this.borrowProjectTypeMapper.selectByExample(example);
	}

	/**
	 * 项目类型
	 * 
	 * @return
	 * @author Administrator
	 */
	@Override
	public String getBorrowProjectClass(String borrowCd) {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
		cra.andBorrowCdEqualTo(borrowCd);
		// 不查询融通宝相关
		cra.andBorrowNameNotEqualTo(CustomConstants.RTB);
		List<BorrowProjectType> list = this.borrowProjectTypeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0).getBorrowClass();
		}
		return "";
	}

	/**
	 * 还款方式
	 * 
	 * @param nid
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<BorrowStyle> borrowStyleList(String nid) {
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(Integer.valueOf(CustomConstants.FLAG_NORMAL));
		if (StringUtils.isNotEmpty(nid)) {
			cra.andNidEqualTo(nid);
		}
		return this.borrowStyleMapper.selectByExample(example);
	}

	/**
	 * 还款方式 关联 项目类型
	 * 
	 * @return
	 */
	@Override
	public List<BorrowProjectRepay> borrowProjectRepayList() {

		HashMap<String, BorrowProjectRepay> map = new HashMap<String, BorrowProjectRepay>();
		BorrowProjectRepayExample example = new BorrowProjectRepayExample();
		BorrowProjectRepayExample.Criteria cra = example.createCriteria();
		cra.andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
		List<BorrowProjectRepay> borrowProjectRepayList = this.borrowProjectRepayMapper.selectByExample(example);
		if (borrowProjectRepayList != null && borrowProjectRepayList.size() > 0) {
			for (BorrowProjectRepay borrowProjectRepay : borrowProjectRepayList) {
				if (map.containsKey(borrowProjectRepay.getRepayMethod())) {
					BorrowProjectRepay mapRecord = map.get(borrowProjectRepay.getRepayMethod());
					String borrowClass = borrowProjectRepay.getBorrowClass();
					String optionAttr = mapRecord.getBorrowClass() + "data-" + borrowClass + "='" + borrowClass + "' ";
					mapRecord.setBorrowClass(optionAttr);
					map.put(borrowProjectRepay.getRepayMethod(), mapRecord);
				} else {
					String borrowClass = borrowProjectRepay.getBorrowClass();
					String optionAttr = "data-" + borrowClass + "='" + borrowClass + "' ";
					borrowProjectRepay.setBorrowClass(optionAttr);
					map.put(borrowProjectRepay.getRepayMethod(), borrowProjectRepay);
				}
			}
		}
		borrowProjectRepayList = new ArrayList<BorrowProjectRepay>();
		Iterator<Entry<String, BorrowProjectRepay>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, BorrowProjectRepay> entry = iter.next();
			borrowProjectRepayList.add(entry.getValue());
		}

		return borrowProjectRepayList;
	}

	/**
	 * 获取借款信息
	 * 
	 * @param borrow
	 * @return
	 * @author Administrator
	 */

	@Override
	public DebtBorrowCommonBean getBorrow(DebtBorrowCommonBean borrowBean) {
		// 借款信息数据获取
		DebtBorrowWithBLOBs borrowWithBLOBs = this.getBorrowWithBLOBs(borrowBean.getBorrowNid());
		if (borrowWithBLOBs != null) {
			// 借款信息数据放置
			this.getBorrowCommonFiled(borrowBean, borrowWithBLOBs);
			if (StringUtils.isEmpty(borrowBean.getCompanyOrPersonal())) {
				if (StringUtils.isNotEmpty(borrowBean.getBuName())) {
					borrowBean.setCompanyOrPersonal("1");
				} else if (StringUtils.isNotEmpty(borrowBean.getManname())) {
					borrowBean.setCompanyOrPersonal("2");
				}
			}
		}
		return borrowBean;

	}

	/**
	 * 借款信息数据获取
	 * 
	 * @param borrowBean
	 * @param borrowWithBLOBs
	 * @author Administrator
	 */
	@Override
	public void getBorrowCommonFiled(DebtBorrowCommonBean borrowBean, DebtBorrowWithBLOBs borrowWithBLOBs) {
		NumberFormat numberFormat = new DecimalFormat("####");
		// 借款预编码（1411001意为2014年11月份第1单借款标，后借款标分期编号HDD141100101）
		borrowBean.setBorrowPreNid(this.getValue(String.valueOf(borrowWithBLOBs.getBorrowPreNid())));
		// 借款状态
		borrowBean.setStatus(this.getValue(String.valueOf(borrowWithBLOBs.getStatus())));
		// 借款编码
		borrowBean.setBorrowNid(this.getValue(String.valueOf(borrowWithBLOBs.getBorrowNid())));
		// 项目名称
		borrowBean.setName(this.getValue(borrowWithBLOBs.getName()));
		// 借款金额
		borrowBean.setAccount(this.getValue(numberFormat.format(borrowWithBLOBs.getAccount())));
		// 借款用户
		Users users = this.usersMapper.selectByPrimaryKey(borrowWithBLOBs.getUserId());
		if (users != null) {
			borrowBean.setUsername(this.getValue(users.getUsername()));
		}
		// 项目申请人
		borrowBean.setApplicant(borrowWithBLOBs.getApplicant());
		// 年利率
		borrowBean.setBorrowApr(this.getValue(String.valueOf(borrowWithBLOBs.getBorrowApr())));
		// 借款期限
		borrowBean.setBorrowPeriod(this.getValue(String.valueOf(borrowWithBLOBs.getBorrowPeriod())));
		// 还款方式
		borrowBean.setBorrowStyle(this.getValue(borrowWithBLOBs.getBorrowStyle()));
		// 借款用途
		borrowBean.setBorrowUse(this.getValue(borrowWithBLOBs.getBorrowUse()));
		// 担保方式
		borrowBean.setGuaranteeType(this.getValue(String.valueOf(borrowWithBLOBs.getGuaranteeType())));
		// 项目类型
		borrowBean.setProjectType(this.getValue(String.valueOf(borrowWithBLOBs.getProjectType())));
		// 借款方式
		borrowBean.setType(this.getValue(String.valueOf(borrowWithBLOBs.getType())));

		if ("BORROW_FIRST".equals(borrowBean.getMoveFlag())) {
			// 立即发标
			borrowBean.setVerifyStatus("3");
		}

		// 担保方式
		// 担保机构
		borrowBean.setBorrowMeasuresInstit(this.getValue(borrowWithBLOBs.getBorrowMeasuresInstit()));
		// 抵押物信息
		borrowBean.setBorrowMeasuresMort(this.getValue(borrowWithBLOBs.getBorrowMeasuresMort()));
		// 本息保障
		borrowBean.setBorrowMeasuresMea(this.getValue(borrowWithBLOBs.getBorrowMeasuresMea()));
		// 机构介绍
		borrowBean.setBorrowCompanyInstruction(this.getValue(borrowWithBLOBs.getBorrowCompanyInstruction()));
		// 操作流程
		borrowBean.setBorrowOperatingProcess(this.getValue(borrowWithBLOBs.getBorrowOperatingProcess()));
		// 财务状况
		borrowBean.setAccountContents(this.getValue(borrowWithBLOBs.getAccountContents()));
		// 项目描述
		borrowBean.setBorrowContents(this.getValue(borrowWithBLOBs.getBorrowContents()));

		// 最低投标金额
		if (borrowWithBLOBs.getTenderAccountMin() == null || borrowWithBLOBs.getTenderAccountMin() == 0) {
			borrowBean.setTenderAccountMin(StringUtils.EMPTY);
		} else {
			borrowBean.setTenderAccountMin(this.getValue(numberFormat.format(borrowWithBLOBs.getTenderAccountMin())));
		}

		// 最高投标金额
		if (borrowWithBLOBs.getTenderAccountMax() == null || borrowWithBLOBs.getTenderAccountMax() == 0) {
			borrowBean.setTenderAccountMax(StringUtils.EMPTY);
		} else {
			borrowBean.setTenderAccountMax(this.getValue(numberFormat.format(borrowWithBLOBs.getTenderAccountMax())));
		}

		// 有效时间
		if (borrowWithBLOBs.getBorrowValidTime() == null || borrowWithBLOBs.getBorrowValidTime() == 0) {
			borrowBean.setBorrowValidTime(StringUtils.EMPTY);
		} else {
			borrowBean.setBorrowValidTime(this.getValue(String.valueOf(borrowWithBLOBs.getBorrowValidTime())));
		}

		// 放款服务费
		borrowBean.setBorrowServiceScale(this.getValue(borrowWithBLOBs.getServiceFeeRate()));
		// 还款服务费率
		borrowBean.setBorrowManagerScale(this.getValue(borrowWithBLOBs.getManageFeeRate()));
		// 还款服务费率(上限)
		borrowBean.setBorrowManagerScaleEnd(this.getValue(borrowWithBLOBs.getBorrowManagerScaleEnd()));
		// 可出借平台_PC
		borrowBean.setCanTransactionPc(this.getValue(borrowWithBLOBs.getCanTransactionPc()));
		// 可出借平台_微网站
		borrowBean.setCanTransactionWei(this.getValue(borrowWithBLOBs.getCanTransactionWei()));
		// 可出借平台_IOS
		borrowBean.setCanTransactionIos(this.getValue(borrowWithBLOBs.getCanTransactionIos()));
		// 可出借平台_Android
		borrowBean.setCanTransactionAndroid(this.getValue(borrowWithBLOBs.getCanTransactionAndroid()));
		// 运营标签
		borrowBean.setOperationLabel(this.getValue(borrowWithBLOBs.getOperationLabel()));
		// 公司个人区分
		borrowBean.setCompanyOrPersonal(this.getValue(borrowWithBLOBs.getCompanyOrPersonal()));
		// 项目资料
		if (StringUtils.isNotEmpty(borrowWithBLOBs.getFiles())) {
			List<DebtBorrowCommonFile> borrowCommonFileList = JSONArray.parseArray(borrowWithBLOBs.getFiles(),
					DebtBorrowCommonFile.class);
			if (borrowCommonFileList != null && borrowCommonFileList.size() > 0) {
				// domain URL
				String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));

				List<DebtBorrowCommonImage> borrowCommonImageList = new ArrayList<DebtBorrowCommonImage>();

				for (DebtBorrowCommonFile borrowCommonFile : borrowCommonFileList) {
					List<DebtBorrowCommonFileData> fileDataList = borrowCommonFile.getData();
					if (fileDataList != null && fileDataList.size() > 0) {
						int i = 0;
						for (DebtBorrowCommonFileData borrowCommonFileData : fileDataList) {
							DebtBorrowCommonImage borrowCommonImage = new DebtBorrowCommonImage();
							borrowCommonImage.setImageName(borrowCommonFileData.getName());
							if (StringUtils.isNotEmpty(borrowCommonFileData.getFileRealName())) {
								borrowCommonImage.setImageRealName(borrowCommonFileData.getFileRealName());
							} else {
								borrowCommonImage.setImageRealName(borrowCommonFileData.getFilename());
							}
							if (StringUtils.isEmpty(borrowCommonFileData.getImageSort().trim())) {
								borrowCommonImage.setImageSort(String.valueOf(i));
							} else {
								borrowCommonImage.setImageSort(borrowCommonFileData.getImageSort().trim());
							}

							borrowCommonImage.setImagePath(borrowCommonFileData.getFileurl());
							borrowCommonImage.setImageSrc(fileDomainUrl + borrowCommonFileData.getFileurl());
							borrowCommonImageList.add(borrowCommonImage);
							i++;
						}
					}
				}

				Collections.sort(borrowCommonImageList, new Comparator<DebtBorrowCommonImage>() {
					@Override
					public int compare(DebtBorrowCommonImage o1, DebtBorrowCommonImage o2) {
						if (o1 != null && o2 != null) {
							Integer sort1 = Integer.valueOf(o1.getImageSort().trim());
							Integer sort2 = Integer.valueOf(o2.getImageSort().trim());
							return sort1.compareTo(sort2);
						}
						return 0;
					}

				});
				borrowBean.setBorrowCommonImageList(borrowCommonImageList);
			}
		}

		// 售价预估
		borrowBean.setDisposalPriceEstimate(this.getValue(borrowWithBLOBs.getDisposalPriceEstimate()));
		// 处置周期
		borrowBean.setDisposalPeriod(this.getValue(borrowWithBLOBs.getDisposalPeriod()));
		// 处置渠道
		borrowBean.setDisposalChannel(this.getValue(borrowWithBLOBs.getDisposalChannel()));
		// 处置结果预案
		borrowBean.setDisposalResult(this.getValue(borrowWithBLOBs.getDisposalResult()));
		// 备注说明
		borrowBean.setDisposalNote(this.getValue(borrowWithBLOBs.getDisposalNote()));

		// 项目名称
		borrowBean.setDisposalProjectName(this.getValue(borrowWithBLOBs.getDisposalProjectName()));
		// 项目类型
		borrowBean.setDisposalProjectType(this.getValue(borrowWithBLOBs.getDisposalProjectType()));
		// 所在地区
		borrowBean.setDisposalArea(this.getValue(borrowWithBLOBs.getDisposalArea()));
		// 预估价值
		borrowBean.setDisposalPredictiveValue(this.getValue(borrowWithBLOBs.getDisposalPredictiveValue()));
		// 权属类别
		borrowBean.setDisposalOwnershipCategory(this.getValue(borrowWithBLOBs.getDisposalOwnershipCategory()));
		// 资产成因
		borrowBean.setDisposalAssetOrigin(this.getValue(borrowWithBLOBs.getDisposalAssetOrigin()));
		// 附件信息
		borrowBean.setDisposalAttachmentInfo(this.getValue(borrowWithBLOBs.getDisposalAttachmentInfo()));

		// 借款人信息数据获取
		this.getBorrowManinfo(borrowBean);
		// 车辆 房屋 认证信息 数据获取
		this.getBorrowCarinfo(borrowBean);
		// 用户信息数据获取
		this.getBorrowUsers(borrowBean);
	}

	/**
	 * 借款人信息数据获取
	 * 
	 * @param borrowManinfo
	 * @return
	 * @author Administrator
	 */

	@Override
	public void getBorrowManinfo(DebtBorrowCommonBean borrowBean) {
		DebtUsersInfoExample example = new DebtUsersInfoExample();
		DebtUsersInfoExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowBean.getBorrowNid());
		List<DebtUsersInfo> debtUsersInfoList = this.debtUsersInfoMapper.selectByExample(example);

		if (debtUsersInfoList != null && debtUsersInfoList.size() > 0) {
			for (DebtUsersInfo record : debtUsersInfoList) {
				// 借款人姓名
				if (StringUtils.isNotEmpty(record.getName())) {
					borrowBean.setManname(this.getValue(record.getName()));
				} else {
					borrowBean.setManname(StringUtils.EMPTY);
				}

				// 借款人性别
				if (record.getSex() == null || record.getSex() == 0) {
					borrowBean.setSex(StringUtils.EMPTY);
				} else {
					borrowBean.setSex(this.getValue(String.valueOf(record.getSex())));
				}
				// 借款人年龄
				if (record.getOld() == null || record.getOld() == 0) {
					borrowBean.setOld(StringUtils.EMPTY);
				} else {
					borrowBean.setOld(this.getValue(String.valueOf(record.getOld())));
				}
				// 岗位职业
				if (StringUtils.isNotEmpty(record.getPosition())) {
					borrowBean.setPosition(record.getPosition());
				}

				// 借款人婚姻状况
				if (record.getMerry() == null || record.getMerry() == 0) {
					borrowBean.setMerry(StringUtils.EMPTY);
				} else {
					borrowBean.setMerry(this.getValue(String.valueOf(record.getMerry())));
				}

				// 借款人省
				if (StringUtils.isNotEmpty(record.getPro())) {
					borrowBean.setLocation_p(this.getValue(record.getPro()));
				} else {
					borrowBean.setLocation_p(StringUtils.EMPTY);
				}

				// 借款人市
				if (StringUtils.isNotEmpty(record.getCity())) {
					borrowBean.setLocation_c(this.getValue(record.getCity()));
				} else {
					borrowBean.setLocation_c(StringUtils.EMPTY);
				}

				// 借款人行业
				if (StringUtils.isNotEmpty(record.getIndustry())) {
					borrowBean.setIndustry(this.getValue(record.getIndustry()));
				} else {
					borrowBean.setIndustry(StringUtils.EMPTY);
				}

				// 借款人公司规模
				if (StringUtils.isNotEmpty(record.getSize())) {
					borrowBean.setSize(this.getValue(record.getSize()));
				} else {
					borrowBean.setSize(StringUtils.EMPTY);
				}

				// 借款人公司月营业额
				if (record.getBusiness() == null || BigDecimal.ZERO.equals(record.getBusiness())) {
					borrowBean.setBusiness(StringUtils.EMPTY);
				} else {
					borrowBean.setBusiness(this.getValue(String.valueOf(record.getBusiness())));
				}

				// 现单位工作时间
				if (StringUtils.isNotEmpty(record.getWtime())) {
					borrowBean.setWtime(this.getValue(record.getWtime()));
				} else {
					borrowBean.setWtime(StringUtils.EMPTY);
				}

				// 授信额度
				if (record.getCredit() != null && !Integer.valueOf(0).equals(record.getCredit())) {
					borrowBean.setUserCredit(String.valueOf((record.getCredit())));
				} else {
					borrowBean.setUserCredit(StringUtils.EMPTY);
				}

			}
		}
	}

	/**
	 * 车辆信息数据获取
	 * 
	 * @param borrowManinfo
	 * @return
	 * @author Administrator
	 */

	@Override
	public void getBorrowCarinfo(DebtBorrowCommonBean borrowBean) {
		NumberFormat numberFormat = new DecimalFormat("####");

		DebtCarInfoExample example = new DebtCarInfoExample();
		DebtCarInfoExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowBean.getBorrowNid());
		List<DebtCarInfo> debtCarInfoList = this.debtCarInfoMapper.selectByExample(example);

		List<DebtBorrowCommonCar> borrowCarList = new ArrayList<DebtBorrowCommonCar>();
		if (debtCarInfoList != null && debtCarInfoList.size() > 0) {
			for (DebtCarInfo record : debtCarInfoList) {
				DebtBorrowCommonCar borrowCar = new DebtBorrowCommonCar();
				// 车辆信息 品牌
				borrowCar.setBrand(this.getValue(record.getBrand()));
				// 车辆信息 型号
				borrowCar.setModel(this.getValue(record.getModel()));
				// 车辆信息 车系
				borrowCar.setCarseries(this.getValue(record.getCarseries()));
				// 车辆信息 颜色
				borrowCar.setColor(this.getValue(record.getColor()));
				// 车辆信息 出厂年份
				borrowCar.setYear(this.getValue(record.getYear()));
				// 车辆信息 产地
				borrowCar.setPlace(this.getValue(record.getPlace()));
				// 车辆信息 购买日期
				if (record.getBuytime() != null && record.getBuytime() != 0) {
					borrowCar.setBuytime(GetDate.formatDate(Long.valueOf(record.getBuytime()) * 1000));
				} else {
					borrowCar.setBuytime(StringUtils.EMPTY);
				}
				// 车辆信息 1有保险2无保险
				if (record.getIsSafe() != null && record.getIsSafe() != 0) {
					borrowCar.setIsSafe(this.getValue(String.valueOf(record.getIsSafe())));
				} else {
					borrowCar.setIsSafe(StringUtils.EMPTY);
				}
				// 车辆信息 购买价
				if (record.getPrice() != null) {
					borrowCar.setPrice(this.getValue(numberFormat.format(record.getPrice())));
				} else {
					borrowCar.setPrice(StringUtils.EMPTY);
				}

				// 车辆信息 评估价
				if (record.getToprice() != null) {
					borrowCar.setToprice(this.getValue(numberFormat.format(record.getToprice())));
				} else {
					borrowCar.setToprice(StringUtils.EMPTY);
				}

				borrowCarList.add(borrowCar);
			}
		}

		borrowBean.setBorrowCarinfoList(borrowCarList);

		DebtHouseInfoExample debtHouseInfoExample = new DebtHouseInfoExample();
		DebtHouseInfoExample.Criteria debtHouseInfoCra = debtHouseInfoExample.createCriteria();
		debtHouseInfoCra.andBorrowNidEqualTo(borrowBean.getBorrowNid());
		List<DebtHouseInfo> debtHouseInfoList = this.debtHouseInfoMapper.selectByExample(debtHouseInfoExample);
		if (debtHouseInfoList != null && debtHouseInfoList.size() > 0) {
			borrowBean.setBorrowHousesList(debtHouseInfoList);
		}

		DebtCompanyAuthenExample debtCompanyAuthenExample = new DebtCompanyAuthenExample();
		DebtCompanyAuthenExample.Criteria borrowCompanyAuthenCra = debtCompanyAuthenExample.createCriteria();
		borrowCompanyAuthenCra.andBorrowNidEqualTo(borrowBean.getBorrowNid());
		debtCompanyAuthenExample.setOrderByClause(" authen_sort_key ASC ");
		List<DebtCompanyAuthen> debtCompanyAuthenList = this.debtCompanyAuthenMapper
				.selectByExample(debtCompanyAuthenExample);
		List<DebtBorrowCommonCompanyAuthen> borrowCommonCompanyAuthenList = new ArrayList<DebtBorrowCommonCompanyAuthen>();
		if (debtCompanyAuthenList != null && debtCompanyAuthenList.size() > 0) {
			borrowBean.setBorrowCompanyAuthenList(debtCompanyAuthenList);
			for (DebtCompanyAuthen debtCompanyAuthen : debtCompanyAuthenList) {
				DebtBorrowCommonCompanyAuthen borrowCommonCompanyAuthen = new DebtBorrowCommonCompanyAuthen();
				borrowCommonCompanyAuthen.setAuthenName(this.getValue(debtCompanyAuthen.getAuthenName()));
				borrowCommonCompanyAuthen.setAuthenTime(this.getValue(debtCompanyAuthen.getAuthenTime()));
				borrowCommonCompanyAuthen.setAuthenSortKey(this.getValue(String.valueOf(debtCompanyAuthen
						.getAuthenSortKey())));
				borrowCommonCompanyAuthenList.add(borrowCommonCompanyAuthen);
				borrowBean.setBorrowCommonCompanyAuthenList(borrowCommonCompanyAuthenList);
			}
		}
	}

	/**
	 * 用户信息数据获取
	 * 
	 * @param borrowUsers
	 * @return
	 * @author Administrator
	 */

	@Override
	public void getBorrowUsers(DebtBorrowCommonBean borrowBean) {
		DebtCompanyInfoExample example = new DebtCompanyInfoExample();
		DebtCompanyInfoExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowBean.getBorrowNid());
		List<DebtCompanyInfo> borrowUsersList = this.debtCompanyInfoMapper.selectByExampleWithBLOBs(example);

		if (borrowUsersList != null && borrowUsersList.size() > 0) {
			for (DebtCompanyInfo record : borrowUsersList) {
				// 用户信息 用户名称
				borrowBean.setBuName(this.getValue(record.getUsername()));
				// 用户信息 所在地区 省
				borrowBean.setLocation_pro(this.getValue(record.getProvince()));
				// 用户信息 所在地区 市
				borrowBean.setLocation_cro(this.getValue(record.getCity()));
				// 用户信息 所在地区 区
				borrowBean.setLocation_aro(this.getValue(record.getArea()));
				// 用户信息 注册资本
				if (record.getRegCaptial() != null && !Integer.valueOf(0).equals(record.getRegCaptial())) {
					borrowBean.setRegCaptial(this.getValue(String.valueOf(record.getRegCaptial())));
				} else {
					borrowBean.setRegCaptial(StringUtils.EMPTY);
				}
				// 用户信息 所属行业
				borrowBean.setUserIndustry(this.getValue(record.getIndustry()));
				// 用户信息 涉诉情况
				borrowBean.setLitigation(this.getValue(record.getLitigation()));
				// 用户信息 征信记录
				borrowBean.setCreReport(this.getValue(record.getCreReport()));
				// 用户信息 授信额度
				if (record.getCredit() != null && !Integer.valueOf(0).equals(record.getCredit())) {
					borrowBean.setCredit(this.getValue(String.valueOf(record.getCredit())));
				} else {
					borrowBean.setCredit(StringUtils.EMPTY);
				}
				// 用户信息 员工人数
				if (record.getStaff() != null && !Integer.valueOf(0).equals(record.getStaff())) {
					borrowBean.setStaff(this.getValue(String.valueOf(record.getStaff())));
				} else {
					borrowBean.setStaff(StringUtils.EMPTY);
				}
				// 用户信息 企业注册时间
				borrowBean.setComRegTime(this.getValue(record.getComRegTime()));
				// 用户信息 其他资料
				borrowBean.setOtherInfo(this.getValue(record.getOtherInfo()));
			}
		}
	}

	private String getValue(String value) {
		if (StringUtils.isNotEmpty(value)) {
			return value;
		}
		return "";
	}

	/**
	 * 借款用户必须是已开户的用户名
	 * 
	 * @param userId
	 * @return
	 * @author Administrator
	 */
	@Override
	public int isExistsUser(String userId) {
		if (StringUtils.isNotEmpty(userId)) {
			UsersExample example = new UsersExample();
			UsersExample.Criteria cra = example.createCriteria();
			cra.andUsernameEqualTo(userId);
			List<Users> userList = this.usersMapper.selectByExample(example);
			if (userList == null || userList.size() == 0) {
				// 借款人用户名不存在。
				return 1;
			}

			Users users = userList.get(0);
			if (users.getOpenAccount() != 1) {
				// 借款人用户名必须已在汇付开户
				return 2;
			}

			if (users.getStatus() != 0) {
				// 借款人用户名已经被禁用
				return 3;
			}

			// 1 ：出借人 2：借款人
			UsersInfoExample usersInfoExample = new UsersInfoExample();
			UsersInfoExample.Criteria usersInfoCra = usersInfoExample.createCriteria();
			usersInfoCra.andUserIdEqualTo(users.getUserId());
			usersInfoCra.andRoleIdEqualTo(2);
			List<UsersInfo> userInfoList = this.usersInfoMapper.selectByExample(usersInfoExample);

			if (userInfoList == null || userInfoList.size() == 0) {
				// 借款人用户名必须是借款人账户
				return 4;
			}
			return 0;
		}
		return 1;
	}

	/**
	 * 用户是否存在
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public String isExistsUser(HttpServletRequest request) {
		JSONObject ret = new JSONObject();
		String message = ValidatorFieldCheckUtil.getErrorMessage("required", "");
		message = message.replace("{label}", "借款人用户名");

		String param = request.getParameter("param");
		if (StringUtils.isEmpty(param)) {
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}

		int usersFlag = this.isExistsUser(param);
		if (usersFlag == 1) {
			message = ValidatorFieldCheckUtil.getErrorMessage("username.not.exists", "");
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		} else if (usersFlag == 2) {
			message = ValidatorFieldCheckUtil.getErrorMessage("username.not.account");
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		} else if (usersFlag == 3) {
			message = ValidatorFieldCheckUtil.getErrorMessage("username.not.use");
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		} else if (usersFlag == 4) {
			message = ValidatorFieldCheckUtil.getErrorMessage("username.not.role");
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}

		ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);

		return ret.toJSONString();
	}

	/**
	 * 项目申请人是否存在
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public String isExistsApplicant(HttpServletRequest request) {
		JSONObject ret = new JSONObject();
		String message = ValidatorFieldCheckUtil.getErrorMessage("required", "");
		message = message.replace("{label}", "项目申请人");

		String param = request.getParameter("param");// 弄明白哪里约定的这个字段！
		if (StringUtils.isEmpty(param)) {
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}

		int applicantFlag = this.isExistsApplicant(param);
		if (applicantFlag == 0) {
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, "项目申请人不存在！");
			return ret.toString();
		}

		ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);

		return ret.toJSONString();
	}

	/**
	 * 验证项目申请人是否存在
	 */
	public int isExistsApplicant(String applicant) {
		if (StringUtils.isNotEmpty(applicant)) {
			ConfigApplicantExample example = new ConfigApplicantExample();
			ConfigApplicantExample.Criteria cra = example.createCriteria();
			cra.andApplicantEqualTo(applicant);
			List<ConfigApplicant> applicants = this.configApplicantMapper.selectByExample(example);

			if (applicants == null || applicants.size() == 0) {
				// 项目申请人不存在。
				return 0;
			}
			return 1;
		}
		return 0;
	}

	/**
	 * 借款预编码是否存在
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public String isExistsBorrowPreNidRecord(HttpServletRequest request) {
		JSONObject ret = new JSONObject();
		String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
		message = message.replace("{label}", "项目编号");

		String param = request.getParameter("param");

		boolean borrowPreNidFlag = this.isExistsRecord(StringUtils.EMPTY, param);
		if (borrowPreNidFlag) {
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}

		ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);

		return ret.toJSONString();
	}

	/**
	 * 借款预编码
	 * 
	 * @return
	 */
	@Override
	public String getBorrowPreNid() {
		String yyyymm = GetDate.getServerDateTime(13, new Date());
		String borrowPreNid = this.debtBorrowCustomizeMapper.getBorrowPreNid(yyyymm);
		if (StringUtils.isEmpty(borrowPreNid)) {
			return yyyymm + "001";
		}
		return String.valueOf(Long.valueOf(borrowPreNid) + 1);
	}

	/**
	 * 获取借款预编号是否存在
	 * 
	 * @return
	 */
	@Override
	public int isExistsBorrowPreNid(String borrowPreNid) {
		DebtBorrowExample debtBorrowExample = new DebtBorrowExample();
		DebtBorrowExample.Criteria debtBorrowCra = debtBorrowExample.createCriteria();
		debtBorrowCra.andBorrowPreNidEqualTo(Integer.valueOf(borrowPreNid));

		List<DebtBorrow> borrowList = this.debtBorrowMapper.selectByExample(debtBorrowExample);

		if (borrowList != null && borrowList.size() > 0) {
			return 1;
		}

		return 0;
	}

	/**
	 * 获取借款信息
	 * 
	 * @param borrow
	 * @return
	 * @author Administrator
	 */

	@Override
	public DebtBorrowWithBLOBs getBorrowWithBLOBs(String borrowNid) {
		if (StringUtils.isEmpty(borrowNid)) {
			return null;
		}
		DebtBorrowExample example = new DebtBorrowExample();
		DebtBorrowExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);

		List<DebtBorrowWithBLOBs> debtBorrowList = this.debtBorrowMapper.selectByExampleWithBLOBs(example);
		if (debtBorrowList != null && debtBorrowList.size() > 0) {
			return debtBorrowList.get(0);
		}

		return null;
	}

	/**
	 * 获取放款服务费率
	 * 
	 * @param borrowStyle2
	 * 
	 * @param chargeTimeType
	 * @param chargeTime
	 * @return
	 */
	@Override
	public String getBorrowServiceScale(String projectType, String borrowStyle, Integer borrowPeriod) {
		BorrowFinserChargeExample example = new BorrowFinserChargeExample();
		BorrowFinserChargeExample.Criteria cra = example.createCriteria();
		if ("endday".equals(borrowStyle)) {
			cra.andChargeTimeTypeEqualTo("endday");
		} else {
			cra.andChargeTimeEqualTo(borrowPeriod);
			cra.andChargeTimeTypeNotEqualTo("endday");
		}
		// 服务费配置修改
		if (StringUtils.isNotBlank(projectType)) {
			cra.andProjectTypeEqualTo(Integer.parseInt(projectType));
		}
		cra.andStatusEqualTo(0);

		List<BorrowFinserCharge> borrowFinserChargeList = borrowFinserChargeMapper.selectByExample(example);

		if (borrowFinserChargeList != null && borrowFinserChargeList.size() > 0) {
			return borrowFinserChargeList.get(0).getChargeRate();
		}

		return "";

	}

	/**
	 * 合作机构
	 * 
	 * @param chargeTimeType
	 * @param chargeTime
	 * @return
	 */
	@Override
	public List<Links> getLinks() {
		LinksExample example = new LinksExample();
		LinksExample.Criteria cra = example.createCriteria();
		cra.andTypeEqualTo(2);
		cra.andPartnerTypeEqualTo(7);

		List<Links> links = linksMapper.selectByExample(example);

		if (links != null && links.size() > 0) {
			return links;
		}
		return null;
	}

	/**
	 * 还款服务费率
	 * 
	 * @param chargeTimeType
	 * @param chargeTime
	 * @return
	 */
	@Override
	public String getBorrowManagerScale(String projectType, String borrowStyle, Integer borrowPeriod) {
		BorrowFinmanChargeExample example = new BorrowFinmanChargeExample();
		BorrowFinmanChargeExample.Criteria cra = example.createCriteria();
		if ("endday".equals(borrowStyle)) {
			cra.andChargeTimeTypeEqualTo("endday");
		} else {
			cra.andChargeTimeTypeNotEqualTo("endday");
		}
		cra.andStatusEqualTo(0);

		List<BorrowFinmanCharge> borrowFinserChargeList = borrowFinmanChargeMapper.selectByExample(example);
		// TODO check fee table
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowPeriod", borrowPeriod);
		params.put("borrowStyle", borrowStyle);
		params.put("projectType", projectType);
		Map<String, Object> feemap = borrowFullCustomizeMapper.selectFeeMapByParams(params);
		String man_charge_rate = "";
		if (feemap != null && !feemap.isEmpty()) {
			man_charge_rate = feemap.get("man_charge_rate") + "";
		}

		if (projectType != null && !projectType.equals("8")) {
			return man_charge_rate;
		} else {
			if (borrowFinserChargeList != null && borrowFinserChargeList.size() > 0) {
				return borrowFinserChargeList.get(0).getManChargePer();
			} else {
				return "";
			}
		}
	}

	/**
	 * 收益差率
	 * 
	 * @param chargeTimeType
	 * @param chargeTime
	 * @return
	 */
	@Override
	public String getBorrowReturnScale(String projectType, String borrowStyle, Integer borrowPeriod) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowPeriod", borrowPeriod);
		params.put("borrowStyle", borrowStyle);
		params.put("projectType", projectType);
		Map<String, Object> feemap = borrowFullCustomizeMapper.selectFeeMapByParams(params);
		String differential_rate = "";
		if (feemap != null && !feemap.isEmpty()) {
			differential_rate = feemap.get("return_rate") + "";
		}
		if (projectType != null && !projectType.equals("8")) {
			return differential_rate;
		} else {
			return "";
		}
	}

	/**
	 * 还款服务费率(最低，最高)
	 * 
	 * @param chargeTimeType
	 * @param chargeTime
	 * @return
	 */
	@Override
	public JSONObject getBorrowManagerScale(String projectType, String borrowStyle, Integer borrowPeriod,
			JSONObject jsonObject) {
		BorrowFinhxfmanChargeExample example = new BorrowFinhxfmanChargeExample();
		BorrowFinhxfmanChargeExample.Criteria cra = example.createCriteria();
		if ("endday".equals(borrowStyle)) {
			cra.andChargeTimeTypeEqualTo("endday");
		} else {
			cra.andChargeTimeEqualTo(borrowPeriod);
			cra.andChargeTimeTypeNotEqualTo("endday");
		}
		cra.andStatusEqualTo(0);

		List<BorrowFinhxfmanCharge> borrowFinhxfmanChargeList = borrowFinhxfmanChargeMapper.selectByExample(example);
		if (borrowFinhxfmanChargeList != null && borrowFinhxfmanChargeList.size() > 0) {
			jsonObject.put("borrowManagerScale", borrowFinhxfmanChargeList.get(0).getManChargePer());
			jsonObject.put("borrowManagerScaleEnd", borrowFinhxfmanChargeList.get(0).getManChargePerEnd());
		} else {
			jsonObject.put("borrowManagerScale", "");
			jsonObject.put("borrowManagerScaleEnd", "");
		}

		return jsonObject;
	}

	/**
	 * 上传图片的信息
	 * 
	 * @param borrowBean
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getUploadImage(DebtBorrowCommonBean borrowBean, String files, String borrowNid) throws Exception {

		HashMap<String, String> fileMap = new HashMap<String, String>();

		// 项目资料
		if (StringUtils.isNotEmpty(files)) {
			List<DebtBorrowCommonFile> borrowCommonFileList = JSONArray.parseArray(files, DebtBorrowCommonFile.class);
			if (borrowCommonFileList != null && borrowCommonFileList.size() > 0) {
				for (DebtBorrowCommonFile borrowCommonFile : borrowCommonFileList) {
					List<DebtBorrowCommonFileData> fileDataList = borrowCommonFile.getData();
					if (fileDataList != null && fileDataList.size() > 0) {
						for (DebtBorrowCommonFileData borrowCommonFileData : fileDataList) {
							if (StringUtils.isEmpty(borrowCommonFileData.getFileRealName())) {
								fileMap.put(borrowCommonFileData.getName(), borrowCommonFileData.getFileurl());
							} else {
								fileMap.put(borrowCommonFileData.getFileRealName(), borrowCommonFileData.getFileurl());
							}
						}
					}
				}
			}
		}

		List<DebtBorrowCommonImage> borrowCommonImageList = borrowBean.getBorrowCommonImageList();
		if (borrowCommonImageList != null && borrowCommonImageList.size() > 0) {
			// 保存的物理路径
			String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
			// 正式保存的路径
			String fileUploadRealPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.real.path"))
					+ UploadFileUtils.getDoPath(borrowNid);

			List<DebtBorrowCommonFile> fileList = new ArrayList<DebtBorrowCommonFile>();
			DebtBorrowCommonFile borrowCommonFile = new DebtBorrowCommonFile();
			List<DebtBorrowCommonFileData> fileDataList = new ArrayList<DebtBorrowCommonFileData>();
			for (DebtBorrowCommonImage borrowCommonImage : borrowCommonImageList) {
				DebtBorrowCommonFileData borrowCommonFileData = new DebtBorrowCommonFileData();
				// 图片顺序
				borrowCommonFileData.setImageSort(borrowCommonImage.getImageSort().trim());
				// 图片名称
				borrowCommonFileData.setName(borrowCommonImage.getImageName());
				// 图片真实名称
				borrowCommonFileData.setFileRealName(borrowCommonImage.getImageRealName());
				// 图片真实名称
				borrowCommonFileData.setFilename(borrowCommonImage.getImageRealName());

				if (fileMap == null || fileMap.isEmpty()) {
					// 图片路径
					String fileName = UploadFileUtils.upload4CopyFile(
							filePhysicalPath + borrowCommonImage.getImagePath(), filePhysicalPath + fileUploadRealPath);
					borrowCommonFileData.setFileurl(fileUploadRealPath + fileName);
					// 垃圾文件删除
					UploadFileUtils.removeFile4Dir(borrowCommonImage.getImagePath());
				} else {
					// 该文件是否已经存在-不存在
					if (!(fileMap.containsKey(borrowCommonImage.getImageRealName()) || fileMap
							.containsKey(borrowCommonImage.getImageName()))) {
						// 图片路径
						String fileName = UploadFileUtils.upload4CopyFile(
								filePhysicalPath + borrowCommonImage.getImagePath(), filePhysicalPath
										+ fileUploadRealPath);
						borrowCommonFileData.setFileurl(fileUploadRealPath + fileName);
						// 垃圾文件删除
						UploadFileUtils.removeFile4Dir(borrowCommonImage.getImagePath());
					} else {
						// 图片顺序
						borrowCommonFileData.setImageSort(borrowCommonImage.getImageSort().trim());
						// 图片名称
						borrowCommonFileData.setName(borrowCommonImage.getImageName());
						// 图片真实名称
						borrowCommonFileData.setFileRealName(borrowCommonImage.getImageRealName());
						// 图片真实名称
						borrowCommonFileData.setFilename(borrowCommonImage.getImageRealName());
						// 图片路径
						borrowCommonFileData.setFileurl(borrowCommonImage.getImagePath());
						// 文件已经存在
						fileMap.remove(borrowCommonImage.getImageRealName());
					}
				}
				fileDataList.add(borrowCommonFileData);
			}

			// 垃圾文件删除
			if (fileMap != null && !fileMap.isEmpty()) {
				Iterator<Entry<String, String>> iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, String> entry = iter.next();
					UploadFileUtils.removeFile4Dir(fileUploadRealPath + entry.getValue());
				}
			}

			if (fileDataList != null && fileDataList.size() > 0) {
				borrowCommonFile.setName("");
				borrowCommonFile.setData(fileDataList);
				fileList.add(borrowCommonFile);
				return JSONObject.toJSONString(fileList, false);
			}
		}
		return "";
	}

	/**
	 * 信息验证
	 * 
	 * @param mav
	 * @param request
	 */
	@Override
	public void validatorFieldCheck(ModelAndView mav, DebtBorrowCommonBean borrowBean, boolean isExistsRecord,
			String HztOrHxf) {

		// 项目类型
		ValidatorFieldCheckUtil.validateRequired(mav, "projectType", borrowBean.getProjectType());

		// 借款用户
		int usersFlag = this.isExistsUser(borrowBean.getUsername());
		if (usersFlag == 1) {
			ValidatorFieldCheckUtil.validateSpecialError(mav, "username", "username.not.exists");
		} else if (usersFlag == 2) {
			ValidatorFieldCheckUtil.validateSpecialError(mav, "username", "username.not.account");
		} else if (usersFlag == 3) {
			ValidatorFieldCheckUtil.validateSpecialError(mav, "username", "username.not.use");
		} else if (usersFlag == 4) {
			ValidatorFieldCheckUtil.validateSpecialError(mav, "username", "username.not.role");
		}

		// 借款预编号
		if (!isExistsRecord) {
			String borrowPreNid = borrowBean.getBorrowPreNid();
			boolean borrowPreNidFlag = ValidatorFieldCheckUtil.validateSignlessNum(mav, "borrowPreNid",
					borrowBean.getBorrowPreNid(), 10, true);
			if (borrowPreNidFlag) {
				borrowPreNidFlag = this.isExistsRecord(StringUtils.EMPTY, borrowPreNid);
				if (borrowPreNidFlag) {
					ValidatorFieldCheckUtil.validateSpecialError(mav, "borrowPreNid", "repeat");
				}
			}
		}

		String isChaibiao = borrowBean.getIsChaibiao();
		// 项目名称 & 借款金额
		if (isExistsRecord || !"yes".equals(isChaibiao)) {
			ValidatorFieldCheckUtil.validateMaxLength(mav, "name", borrowBean.getName(), 60, true);
			ValidatorFieldCheckUtil.validateSignlessNum(mav, "account", borrowBean.getAccount(), 10, true);
		} else if ("yes".equals(isChaibiao)) {
			ValidatorFieldCheckUtil.validateMaxLength(mav, "name", borrowBean.getName(), 60, true);
			boolean accountFlag = ValidatorFieldCheckUtil.validateSignlessNum(mav, "account", borrowBean.getAccount(),
					10, true);
			List<DebtBorrowCommonNameAccount> borrowCommonNameAccountList = borrowBean.getBorrowCommonNameAccountList();
			boolean accountFlag2 = true;
			BigDecimal amountAll = BigDecimal.ZERO;
			BigDecimal amount = BigDecimal.ZERO;
			if (accountFlag) {
				amount = new BigDecimal(borrowBean.getAccount());
				if ((BigDecimal.ZERO).equals(amount)) {
					ValidatorFieldCheckUtil.validateSpecialError(mav, "account-error", "account.not.zero");
					accountFlag = false;
				}
			}
			for (int i = 0; i < borrowCommonNameAccountList.size(); i++) {
				DebtBorrowCommonNameAccount borrowCommonNameAccount = borrowCommonNameAccountList.get(i);
				ValidatorFieldCheckUtil.validateMaxLength(mav, "names" + i, borrowCommonNameAccount.getNames(), 60,
						true);
				boolean accountFlag3 = ValidatorFieldCheckUtil.validateSignlessNum(mav, "accounts" + i,
						borrowCommonNameAccount.getAccounts(), 10, true);
				if (accountFlag3) {
					amountAll = amountAll.add(new BigDecimal(borrowCommonNameAccount.getAccounts()));
				} else {
					accountFlag2 = false;
				}
			}

			if (accountFlag && accountFlag2 && !amountAll.equals(amount)) {
				ValidatorFieldCheckUtil.validateSpecialError(mav, "account-error", "not.equals.account");
			}
		}

		// 还款方式
		boolean borrowStyleFlag = ValidatorFieldCheckUtil.validateRequired(mav, "borrowStyle",
				borrowBean.getBorrowStyle());

		// 出借利率
		ValidatorFieldCheckUtil.validateSignlessNumLength(mav, "borrowApr", borrowBean.getBorrowApr(), 2, 2, true);

		// 借款期限
		boolean borrowPeriodFlag = ValidatorFieldCheckUtil.validateSignlessNum(mav, "borrowPeriod",
				borrowBean.getBorrowPeriod(), 3, true);

		// 售价预估
		ValidatorFieldCheckUtil.validateMaxLength(mav, "disposalPriceEstimate", borrowBean.getDisposalPriceEstimate(),
				50, false);
		// 处置周期
		ValidatorFieldCheckUtil.validateMaxLength(mav, "disposalPeriod", borrowBean.getDisposalPeriod(), 50, false);
		// 处置渠道
		ValidatorFieldCheckUtil.validateMaxLength(mav, "disposalChannel", borrowBean.getDisposalChannel(), 50, false);
		// 处置结果预案
		ValidatorFieldCheckUtil.validateMaxLength(mav, "disposalResult", borrowBean.getDisposalResult(), 2000, false);
		// 备注说明
		ValidatorFieldCheckUtil.validateMaxLength(mav, "disposalNote", borrowBean.getDisposalNote(), 2000, false);

		// 项目名称
		ValidatorFieldCheckUtil.validateMaxLength(mav, "disposalProjectName", borrowBean.getDisposalProjectName(), 100,
				false);
		// 项目类型
		ValidatorFieldCheckUtil.validateMaxLength(mav, "disposalProjectType", borrowBean.getDisposalProjectType(), 100,
				false);
		// 所在地区
		ValidatorFieldCheckUtil.validateMaxLength(mav, "disposalArea", borrowBean.getDisposalArea(), 200, false);
		// 预估价值
		ValidatorFieldCheckUtil.validateMaxLength(mav, "disposalPredictiveValue",
				borrowBean.getDisposalPredictiveValue(), 20, false);
		// 权属类别
		ValidatorFieldCheckUtil.validateMaxLength(mav, "disposalOwnershipCategory",
				borrowBean.getDisposalOwnershipCategory(), 20, false);
		// 资产成因
		ValidatorFieldCheckUtil.validateMaxLength(mav, "disposalAssetOrigin", borrowBean.getDisposalAssetOrigin(), 20,
				false);
		// 附件信息
		ValidatorFieldCheckUtil.validateMaxLength(mav, "disposalAttachmentInfo",
				borrowBean.getDisposalAttachmentInfo(), 2000, false);

		// 初审
		if ("BORROW_FIRST".equals(borrowBean.getMoveFlag())) {
			// 初审意见
			ValidatorFieldCheckUtil.validateRequired(mav, "verify", borrowBean.getVerify());
			// 发标方式
			boolean verifyStatusFlag = ValidatorFieldCheckUtil.validateRequired(mav, "verifyStatus",
					borrowBean.getVerifyStatus());
			if (verifyStatusFlag) {
				// 定时发标
				if (StringUtils.equals(borrowBean.getVerifyStatus(), "1")) {
					verifyStatusFlag = ValidatorFieldCheckUtil.validateDateYYYMMDDHH24MI(mav, "ontime",
							borrowBean.getOntime(), true);
					if (verifyStatusFlag) {
						String systeDate = GetDate.getServerDateTime(14, new Date());
						if (systeDate.compareTo(borrowBean.getOntime()) >= 0) {
							ValidatorFieldCheckUtil.validateSpecialError(mav, "ontime", "ontimeltsystemdate");
						}
						if (StringUtils.isNotEmpty(borrowBean.getBookingBeginTime())
								|| StringUtils.isNotEmpty(borrowBean.getBookingEndTime())) {
							// 如果开始预约和截止预约时间中有一个时间有值,则进行以下验证
							// 截止预约时间必填
							verifyStatusFlag = ValidatorFieldCheckUtil.validateDateYYYMMDDHH24MI(mav, "bookingEndTime",
									borrowBean.getBookingEndTime(), true);
							// 截止预约时间必须小于定时发标时间
							if (verifyStatusFlag
									&& borrowBean.getBookingEndTime().compareTo(borrowBean.getOntime()) >= 0) {
								ValidatorFieldCheckUtil.validateSpecialError(mav, "bookingEndTime",
										"bookingEndTimeltontime");
							}
							// 开始预约时间必填
							verifyStatusFlag = ValidatorFieldCheckUtil.validateDateYYYMMDDHH24MI(mav,
									"bookingBeginTime", borrowBean.getBookingBeginTime(), true);
							// 开始预约时间必须小于截止预约时间
							if (verifyStatusFlag
									&& borrowBean.getBookingBeginTime().compareTo(borrowBean.getBookingEndTime()) >= 0) {
								ValidatorFieldCheckUtil.validateSpecialError(mav, "bookingEndTime",
										"bookingBeginTimeltbookingEndTime");
							}
							// 当前时间必须小于开始预约时间
							if (systeDate.compareTo(borrowBean.getBookingBeginTime()) >= 0) {
								ValidatorFieldCheckUtil.validateSpecialError(mav, "bookingBeginTime",
										"bookingBeginTimeltsystemdate");
							}
						}
					}
				}
			}
			// 复审意见
			ValidatorFieldCheckUtil.validateMaxLength(mav, "verifyRemark", borrowBean.getVerifyRemark(), 255, true);
		}

		// 个人信息
		if (StringUtils.equals("2", borrowBean.getCompanyOrPersonal())) {
			// 借款人信息 姓名
			ValidatorFieldCheckUtil.validateMaxLength(mav, "manname", borrowBean.getManname(), 50, false);
			// 借款人信息 年龄
			ValidatorFieldCheckUtil.validateSignlessNum(mav, "old", borrowBean.getOld(), 3, false);
			// 借款人信息 行业
			ValidatorFieldCheckUtil.validateMaxLength(mav, "industry", borrowBean.getIndustry(), 50, false);
			// 借款人信息 授信额度
			ValidatorFieldCheckUtil.validateSignlessNum(mav, "userCredit", borrowBean.getUserCredit(), 10, false);
		}

		// 企业名称
		if (StringUtils.equals("1", borrowBean.getCompanyOrPersonal())) {
			// 企业名称
			ValidatorFieldCheckUtil.validateMaxLength(mav, "buName", borrowBean.getBuName(), 50, false);
			// 注册时间
			ValidatorFieldCheckUtil.validateMaxLength(mav, "comRegTime", borrowBean.getComRegTime(), 30, false);
			// 涉诉情况
			ValidatorFieldCheckUtil.validateMaxLength(mav, "litigation", borrowBean.getLitigation(), 100, false);
			// 注册资本
			ValidatorFieldCheckUtil.validateSignlessNum(mav, "regCaptial", borrowBean.getRegCaptial(), 10, false);
			// 授信额度
			ValidatorFieldCheckUtil.validateSignlessNum(mav, "credit", borrowBean.getCredit(), 10, false);
			// 征信记录
			ValidatorFieldCheckUtil.validateMaxLength(mav, "creReport", borrowBean.getCreReport(), 100, false);
			// 所属行业
			ValidatorFieldCheckUtil.validateMaxLength(mav, "userIndustry", borrowBean.getUserIndustry(), 100, false);
		}

		if (StringUtils.equals("2", borrowBean.getTypeCar())) {
			List<DebtBorrowCommonCar> borrowCarinfoList = borrowBean.getBorrowCarinfoList();
			if (borrowCarinfoList != null && borrowCarinfoList.size() > 0) {
				for (int i = 0; i < borrowCarinfoList.size(); i++) {
					DebtBorrowCommonCar borrowCarinfo = borrowCarinfoList.get(i);
					// 车辆信息 品牌
					ValidatorFieldCheckUtil.validateMaxLength(mav, "brand" + i, borrowCarinfo.getBrand(), 40, true);
					// 车辆信息 型号
					ValidatorFieldCheckUtil.validateMaxLength(mav, "model" + i, borrowCarinfo.getModel(), 50, false);
					// 车辆信息 车系
					ValidatorFieldCheckUtil.validateMaxLength(mav, "carseries" + i, borrowCarinfo.getCarseries(), 50,
							false);
					// 车辆信息 颜色
					ValidatorFieldCheckUtil.validateMaxLength(mav, "color" + i, borrowCarinfo.getColor(), 16, false);
					// 车辆信息 出厂年份
					ValidatorFieldCheckUtil.validateMaxLength(mav, "year" + i, borrowCarinfo.getYear(), 12, false);
					// 车辆信息 产地
					ValidatorFieldCheckUtil.validateMaxLength(mav, "place" + i, borrowCarinfo.getPlace(), 60, false);
					// 车辆信息 购买日期
					ValidatorFieldCheckUtil.validateDate(mav, "buytime" + i, borrowCarinfo.getBuytime(), false);
					// 车辆信息 购买价
					ValidatorFieldCheckUtil.validateSignlessNum(mav, "price" + i, borrowCarinfo.getPrice(), 13, false);
					// 车辆信息 是否有保险
					ValidatorFieldCheckUtil.validateRequired(mav, "isSafe" + i, borrowCarinfo.getIsSafe());
					// 车辆信息 评估价
					ValidatorFieldCheckUtil.validateSignlessNum(mav, "toprice" + i, borrowCarinfo.getToprice(), 13,
							true);
				}
			}
		}

		if (StringUtils.equals("1", borrowBean.getTypeHouse())) {
			List<DebtHouseInfo> borrowHousesList = borrowBean.getBorrowHousesList();
			if (borrowHousesList != null && borrowHousesList.size() > 0) {
				for (int i = 0; i < borrowHousesList.size(); i++) {
					DebtHouseInfo record = borrowHousesList.get(i);
					// 房产类型
					ValidatorFieldCheckUtil.validateRequired(mav, "housesType" + i, record.getHousesType());
					// 房产位置
					ValidatorFieldCheckUtil.validateMaxLength(mav, "housesLocation" + i, record.getHousesLocation(),
							255, false);
					// 建筑面积
					ValidatorFieldCheckUtil.validateSignlessNumLength(mav, "housesArea" + i, record.getHousesArea(), 7,
							2, true);
					// 市值
					ValidatorFieldCheckUtil.validateSignlessNum(mav, "housesPrice" + i, record.getHousesPrice(), 20,
							true);
					// 抵押价值（元）
					ValidatorFieldCheckUtil.validateSignlessNum(mav, "housesToprice" + i, record.getHousesToprice(),
							20, true);
				}
			}
		}

		List<DebtBorrowCommonCompanyAuthen> borrowCommonCompanyAuthenList = borrowBean
				.getBorrowCommonCompanyAuthenList();
		if (borrowCommonCompanyAuthenList != null && borrowCommonCompanyAuthenList.size() > 0) {
			for (int i = 0; i < borrowCommonCompanyAuthenList.size(); i++) {
				DebtBorrowCommonCompanyAuthen record = borrowCommonCompanyAuthenList.get(i);
				// 展示顺序
				ValidatorFieldCheckUtil.validateSignlessNum(mav, "authenSortKey" + i, record.getAuthenSortKey(), 2,
						true);
				// 认证项目名称
				ValidatorFieldCheckUtil.validateMaxLength(mav, "authenName" + i, record.getAuthenName(), 255, true);
				// 认证时间
				ValidatorFieldCheckUtil.validateDate(mav, "authenTime" + i, record.getAuthenTime(), true);
			}
		}

		List<DebtBorrowCommonImage> borrowCommonImageList = borrowBean.getBorrowCommonImageList();
		if (borrowCommonImageList != null && borrowCommonImageList.size() > 0) {
			for (int i = 0; i < borrowCommonImageList.size(); i++) {
				DebtBorrowCommonImage record = borrowCommonImageList.get(i);
				// 展示顺序
				ValidatorFieldCheckUtil.validateMaxLength(mav, "imageSort" + i, record.getImageSort().trim(), 2, true);
				// 资料名称
				boolean imageNameFlag = ValidatorFieldCheckUtil.validateMaxLength(mav, "imageName" + i,
						record.getImageName(), 50, true);
				// 图片路径
				boolean imagePathFlag = ValidatorFieldCheckUtil.validateMaxLength(mav, "imagePath" + i,
						record.getImagePath(), 100, true);

				if (imageNameFlag && imagePathFlag) {
					String imagePath = record.getImagePath();
					String imageRealName = record.getImageRealName();
					if (!imagePath.contains(imageRealName)) {
						ValidatorFieldCheckUtil.validateSpecialError(mav, "imageSrc" + i, "image.not.exists");
					}
				}
			}
		}

		if (CustomConstants.HZT.equals(HztOrHxf)) {

			// 放款服务费(放款时收)
			if (borrowPeriodFlag && borrowStyleFlag) {
				String borrowStyle = borrowBean.getBorrowStyle();
				List<BorrowStyle> borrowStyleList = this.borrowStyleList(borrowStyle);
				if (borrowStyleList != null && borrowStyleList.size() > 0) {
					String borrowServiceScale = this.getBorrowServiceScale(borrowBean.getProjectType(), borrowStyle,
							Integer.valueOf(borrowBean.getBorrowPeriod()));
					if (StringUtils.isEmpty(borrowServiceScale)) {
						ValidatorFieldCheckUtil.validateSpecialError(mav, "borrowServiceScale",
								"notget.borrowservicescale");
					} else {
						borrowBean.setBorrowServiceScale(borrowServiceScale);
					}
				}
			}

			// 还款服务费率
			if (borrowStyleFlag) {
				String borrowStyle = borrowBean.getBorrowStyle();
				List<BorrowStyle> borrowStyleList = this.borrowStyleList(borrowStyle);
				if (borrowStyleList != null && borrowStyleList.size() > 0) {
					String borrowManagerScale = this.getBorrowManagerScale(borrowBean.getProjectType(), borrowStyle,
							Integer.parseInt(borrowBean.getBorrowPeriod()));
					if (StringUtils.isEmpty(borrowManagerScale)) {
						ValidatorFieldCheckUtil.validateSpecialError(mav, "borrowManagerScale",
								"notget.borrowmanagerscale");
					} else {
						borrowBean.setBorrowManagerScale(borrowManagerScale);
					}
				}
			}
		} else if (CustomConstants.HXF.equals(HztOrHxf)) {
			// 还款服务费率
			if (borrowStyleFlag) {
				String borrowStyle = borrowBean.getBorrowStyle();
				List<BorrowStyle> borrowStyleList = this.borrowStyleList(borrowStyle);
				if (borrowStyleList != null && borrowStyleList.size() > 0) {
					JSONObject jsonObject = new JSONObject();
					jsonObject = this.getBorrowManagerScale(borrowBean.getProjectType(), borrowStyle,
							Integer.valueOf(borrowBean.getBorrowPeriod()), jsonObject);
					if (StringUtils.isEmpty(jsonObject.getString("borrowManagerScale"))) {
						ValidatorFieldCheckUtil.validateSpecialError(mav, "borrowManagerScale",
								"notget.borrowmanagerscale.start");
					}
					if (StringUtils.isEmpty(jsonObject.getString("borrowManagerScaleEnd"))) {
						ValidatorFieldCheckUtil.validateSpecialError(mav, "borrowManagerScaleEnd",
								"notget.borrowmanagerscale.end");
					}
				}
			}
		}

		// 最低投标金额
		boolean tenderAccountMinFlag = ValidatorFieldCheckUtil.validateSignlessNum(mav, "tenderAccountMin",
				borrowBean.getTenderAccountMin(), 10, false);
		// 最高投标金额
		boolean tenderAccountMaxFlag = ValidatorFieldCheckUtil.validateSignlessNum(mav, "tenderAccountMax",
				borrowBean.getTenderAccountMax(), 10, false);

		if (tenderAccountMinFlag && tenderAccountMaxFlag && StringUtils.isNotEmpty(borrowBean.getTenderAccountMin())
				&& StringUtils.isNotEmpty(borrowBean.getTenderAccountMax())) {
			BigDecimal tenderAccountMinb = new BigDecimal(borrowBean.getTenderAccountMin());
			BigDecimal tenderAccountMaxb = new BigDecimal(borrowBean.getTenderAccountMax());
			if (tenderAccountMaxb.compareTo(tenderAccountMinb) <= 0) {
				ValidatorFieldCheckUtil.validateSpecialError(mav, "tenderAccount", "tenderaccount");
			}
		}
		// 有效时间
		ValidatorFieldCheckUtil.validateSignlessNum(mav, "borrowValidTime", borrowBean.getBorrowValidTime(), 2, false);
	}

	/**
	 * 画面的值放到Bean中
	 * 
	 * @param modelAndView
	 * @param form
	 */
	@Override
	public void setPageListInfo(ModelAndView modelAndView, DebtBorrowCommonBean form, boolean isExistsRecord) {

		List<DebtBorrowCommonNameAccount> borrowCommonNameAccountList = new ArrayList<DebtBorrowCommonNameAccount>();
		String isChaibiao = form.getIsChaibiao();
		// 项目名称 & 借款金额
		if (!isExistsRecord && "yes".equals(isChaibiao)) {
			borrowCommonNameAccountList = JSONArray.parseArray(form.getBorrowNameJson(),
					DebtBorrowCommonNameAccount.class);
		}
		form.setBorrowCommonNameAccountList(borrowCommonNameAccountList);
		// 车辆信息
		if (StringUtils.equals("2", form.getTypeCar())) {
			List<DebtBorrowCommonCar> borrowCarinfoList = JSONArray.parseArray(form.getBorrowCarJson(),
					DebtBorrowCommonCar.class);
			form.setBorrowCarinfoList(borrowCarinfoList);
		}
		// 房产信息
		if (StringUtils.equals("1", form.getTypeHouse())) {
			List<DebtHouseInfo> borrowHousesList = JSONArray
					.parseArray(form.getBorrowHousesJson(), DebtHouseInfo.class);
			form.setBorrowHousesList(borrowHousesList);
		}
		// 认证信息
		List<DebtBorrowCommonCompanyAuthen> borrowCommonCompanyAuthenList = JSONArray.parseArray(
				form.getBorrowAuthenJson(), DebtBorrowCommonCompanyAuthen.class);
		form.setBorrowCommonCompanyAuthenList(borrowCommonCompanyAuthenList);
		// 项目资料
		List<DebtBorrowCommonImage> borrowCommonImageList = JSONArray.parseArray(form.getBorrowImageJson(),
				DebtBorrowCommonImage.class);
		String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
		for (DebtBorrowCommonImage borrowCommonImage : borrowCommonImageList) {
			borrowCommonImage.setImageSrc(fileDomainUrl + borrowCommonImage.getImagePath());
		}
		form.setBorrowCommonImageList(borrowCommonImageList);
	}

	/**
	 * 获取放款服务费率 & 还款服务费率 & 收益差率
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public String getBorrowServiceScale(HttpServletRequest request) {
		String borrowPeriod = request.getParameter("borrowPeriod");
		String borrowStyle = request.getParameter("borrowStyle");
		String projectType = request.getParameter("projectType");

		JSONObject jsonObject = new JSONObject();

		if (!GenericValidator.isLong(borrowPeriod) || !NumberUtils.isNumber(borrowPeriod)
				|| Integer.valueOf(borrowPeriod) < 0 || StringUtils.isEmpty(borrowStyle)
				|| StringUtils.isEmpty(projectType)) {
			jsonObject.put("borrowServiceScale", "--");
			jsonObject.put("borrowManagerScale", "--");
			jsonObject.put("borrowReturnScale", "--");
			return jsonObject.toJSONString();
		}

		// 获取放款服务费率
		String borrowServiceScale = this.getBorrowServiceScale(projectType, borrowStyle, Integer.valueOf(borrowPeriod));
		jsonObject.put("borrowServiceScale", StringUtils.isEmpty(borrowServiceScale) ? "--" : borrowServiceScale);
		// 还款服务费率
		String borrowManagerScale = this.getBorrowManagerScale(projectType, borrowStyle, Integer.valueOf(borrowPeriod));
		jsonObject.put("borrowManagerScale", StringUtils.isEmpty(borrowManagerScale) ? "--" : borrowManagerScale);

		// 收益差率
		String borrowReturnScale = this.getBorrowReturnScale(projectType, borrowStyle, Integer.valueOf(borrowPeriod));
		jsonObject.put("borrowReturnScale", StringUtils.isEmpty(borrowReturnScale) ? "--" : borrowReturnScale);

		return jsonObject.toJSONString();
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
				.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
		String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
		String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
		String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.temp.path"));

		String logoRealPathDir = filePhysicalPath + fileUploadTempPath;

		File logoSaveFile = new File(logoRealPathDir);
		if (!logoSaveFile.exists()) {
			logoSaveFile.mkdirs();
		}

		DebtBorrowCommonImage fileMeta = null;
		LinkedList<DebtBorrowCommonImage> files = new LinkedList<DebtBorrowCommonImage>();

		Iterator<String> itr = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;

		while (itr.hasNext()) {
			multipartFile = multipartRequest.getFile(itr.next());
			String fileRealName = String.valueOf(new Date().getTime());
			String originalFilename = multipartFile.getOriginalFilename();
			fileRealName = fileRealName + UploadFileUtils.getSuffix(multipartFile.getOriginalFilename());

			// 文件大小
			String errorMessage = UploadFileUtils.upload4Stream(fileRealName, logoRealPathDir,
					multipartFile.getInputStream(), 5000000L);

			fileMeta = new DebtBorrowCommonImage();
			int index = originalFilename.lastIndexOf(".");
			if (index != -1) {
				fileMeta.setImageName(originalFilename.substring(0, index));
			} else {
				fileMeta.setImageName(originalFilename);
			}

			fileMeta.setImageRealName(fileRealName);
			fileMeta.setImageSize(multipartFile.getSize() / 1024 + "");// KB
			fileMeta.setImageType(multipartFile.getContentType());
			fileMeta.setErrorMessage(errorMessage);
			// 获取文件路径
			fileMeta.setImagePath(fileUploadTempPath + fileRealName);
			fileMeta.setImageSrc(fileDomainUrl + fileUploadTempPath + fileRealName);
			files.add(fileMeta);
		}
		return JSONObject.toJSONString(files, true);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@Override
	public void downloadCar(HttpServletRequest request, HttpServletResponse response, DebtBorrowBean form)
			throws Exception {
		// 表格sheet名称
		String sheetName = "抵押车辆模板";

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "车辆品牌", "型号", "车系", "颜色", "出厂年份", "产地", "购买日期(例：2016-01-04)", "购买价格（元）",
				"是否有保险(否|有)", "评估价（元）" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName);

		CellStyle style = workbook.createCellStyle();
		DataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat("@"));
		for (int i = 0; i < 10; i++) {
			sheet.setDefaultColumnStyle(i, style);
		}

		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public String uploadCar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
				.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());

		Iterator<String> itr = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;

		List<DebtBorrowCommonCar> borrowCarinfoList = new ArrayList<DebtBorrowCommonCar>();

		while (itr.hasNext()) {
			multipartFile = multipartRequest.getFile(itr.next());
			String fileRealName = String.valueOf(new Date().getTime());
			fileRealName = fileRealName + UploadFileUtils.getSuffix(multipartFile.getOriginalFilename());

			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(multipartFile.getInputStream());

			if (hssfWorkbook != null) {
				for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
					HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);

					// 循环行Row
					for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
						if (rowNum == 0) {
							continue;
						}
						DebtBorrowCommonCar borrowCarinfo = new DebtBorrowCommonCar();
						HSSFRow hssfRow = hssfSheet.getRow(rowNum);
						if (hssfRow == null
								|| (hssfRow.getCell(0) == null && hssfRow.getCell(1) == null
										&& hssfRow.getCell(2) == null && hssfRow.getCell(3) == null
										&& hssfRow.getCell(4) == null && hssfRow.getCell(5) == null
										&& hssfRow.getCell(6) == null && hssfRow.getCell(7) == null
										&& hssfRow.getCell(8) == null && hssfRow.getCell(9) == null)) {
							continue;
						}

						if (!(StringUtils.isEmpty(this.getValue(hssfRow.getCell(0)))
								&& StringUtils.isEmpty(this.getValue(hssfRow.getCell(1)))
								&& StringUtils.isEmpty(this.getValue(hssfRow.getCell(2)))
								&& StringUtils.isEmpty(this.getValue(hssfRow.getCell(3)))
								&& StringUtils.isEmpty(this.getValue(hssfRow.getCell(4)))
								&& StringUtils.isEmpty(this.getValue(hssfRow.getCell(5)))
								&& StringUtils.isEmpty(this.getValue(hssfRow.getCell(6)))
								&& StringUtils.isEmpty(this.getValue(hssfRow.getCell(7)))
								&& StringUtils.isEmpty(this.getValue(hssfRow.getCell(8))) && StringUtils.isEmpty(this
								.getValue(hssfRow.getCell(9))))) {
							// 车辆品牌
							borrowCarinfo.setBrand(this.getValue(hssfRow.getCell(0)));
							// 型号
							borrowCarinfo.setModel(this.getValue(hssfRow.getCell(1)));
							// 车系
							borrowCarinfo.setCarseries(this.getValue(hssfRow.getCell(2)));
							// 颜色
							borrowCarinfo.setColor(this.getValue(hssfRow.getCell(3)));
							// 出厂年份
							borrowCarinfo.setYear(this.getValue(hssfRow.getCell(4)));
							// 产地
							borrowCarinfo.setPlace(this.getValue(hssfRow.getCell(5)));
							// 购买日期
							borrowCarinfo.setBuytime(this.getValue(hssfRow.getCell(6)));
							// 购买价格（元）
							borrowCarinfo.setPrice(this.getValue(hssfRow.getCell(7)));
							// 是否有保险
							borrowCarinfo.setIsSafe(this.getValue(hssfRow.getCell(8)));
							// 评估价（元）
							borrowCarinfo.setToprice(this.getValue(hssfRow.getCell(9)));

							borrowCarinfoList.add(borrowCarinfo);
						}
					}
				}
			}
		}
		return JSONObject.toJSONString(borrowCarinfoList, true);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@Override
	public void downloadHouse(HttpServletRequest request, HttpServletResponse response, DebtBorrowBean form)
			throws Exception {
		// 表格sheet名称
		String sheetName = "抵押房产模板";

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "房产类型", "房产位置", "建筑面积", "市值", "抵押价值（元）" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName);

		CellStyle style = workbook.createCellStyle();
		DataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat("@"));
		for (int i = 0; i < 5; i++) {
			sheet.setDefaultColumnStyle(i, style);
		}

		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public String uploadHouse(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
				.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());

		Iterator<String> itr = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;

		List<DebtHouseInfo> recordList = new ArrayList<DebtHouseInfo>();

		while (itr.hasNext()) {
			multipartFile = multipartRequest.getFile(itr.next());
			String fileRealName = String.valueOf(new Date().getTime());
			fileRealName = fileRealName + UploadFileUtils.getSuffix(multipartFile.getOriginalFilename());

			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(multipartFile.getInputStream());

			if (hssfWorkbook != null) {
				for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
					HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);

					// 循环行Row
					for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
						if (rowNum == 0) {
							continue;
						}
						DebtHouseInfo record = new DebtHouseInfo();
						HSSFRow hssfRow = hssfSheet.getRow(rowNum);
						if (hssfRow == null
								|| (hssfRow.getCell(0) == null && hssfRow.getCell(1) == null
										&& hssfRow.getCell(2) == null && hssfRow.getCell(3) == null && hssfRow
										.getCell(4) == null)) {
							continue;
						}
						if (!(StringUtils.isEmpty(this.getValue(hssfRow.getCell(0)))
								&& StringUtils.isEmpty(this.getValue(hssfRow.getCell(1)))
								&& StringUtils.isEmpty(this.getValue(hssfRow.getCell(2)))
								&& StringUtils.isEmpty(this.getValue(hssfRow.getCell(3))) && StringUtils.isEmpty(this
								.getValue(hssfRow.getCell(4))))) {
							// 房产类型
							record.setHousesType(this.getValue(hssfRow.getCell(0)));
							// 房产位置
							record.setHousesLocation(this.getValue(hssfRow.getCell(1)));
							// 建筑面积
							record.setHousesArea(this.getValue(hssfRow.getCell(2)));
							// 市值
							record.setHousesPrice(this.getValue(hssfRow.getCell(3)));
							// 抵押价值（元）
							record.setHousesToprice(this.getValue(hssfRow.getCell(4)));

							recordList.add(record);
						}
					}
				}
			}
		}
		return JSONObject.toJSONString(recordList, true);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@Override
	public void downloadAuthen(HttpServletRequest request, HttpServletResponse response, DebtBorrowBean form)
			throws Exception {
		// 表格sheet名称
		String sheetName = "认证信息模板";

		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		String[] titles = new String[] { "展示顺序", "认证项目名称", "认证时间(例：2016-01-04)" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName);

		CellStyle style = workbook.createCellStyle();
		DataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat("@"));
		for (int i = 0; i < 3; i++) {
			sheet.setDefaultColumnStyle(i, style);
		}

		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public String uploadAuthen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
				.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());

		Iterator<String> itr = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;

		List<DebtBorrowCommonCompanyAuthen> recordList = new ArrayList<DebtBorrowCommonCompanyAuthen>();

		while (itr.hasNext()) {
			multipartFile = multipartRequest.getFile(itr.next());
			String fileRealName = String.valueOf(new Date().getTime());
			fileRealName = fileRealName + UploadFileUtils.getSuffix(multipartFile.getOriginalFilename());

			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(multipartFile.getInputStream());

			if (hssfWorkbook != null) {
				for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
					HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);

					// 循环行Row
					for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
						if (rowNum == 0) {
							continue;
						}
						DebtBorrowCommonCompanyAuthen record = new DebtBorrowCommonCompanyAuthen();
						HSSFRow hssfRow = hssfSheet.getRow(rowNum);
						if (hssfRow == null
								|| (hssfRow.getCell(0) == null && hssfRow.getCell(1) == null && hssfRow.getCell(2) == null)) {
							continue;
						}
						if (!(StringUtils.isEmpty(this.getValue(hssfRow.getCell(0)))
								&& StringUtils.isEmpty(this.getValue(hssfRow.getCell(1))) && StringUtils.isEmpty(this
								.getValue(hssfRow.getCell(2))))) {
							// 展示顺序
							record.setAuthenSortKey(this.getValue(hssfRow.getCell(0)));
							// 认证项目名称
							record.setAuthenName(this.getValue(hssfRow.getCell(1)));
							// 认证时间
							record.setAuthenTime(this.getValue(hssfRow.getCell(2)));

							recordList.add(record);
						}

					}
				}
			}
		}
		return JSONObject.toJSONString(recordList, true);
	}

	/**
	 * 得到Excel表中的值
	 * 
	 * @param hssfCell
	 *            Excel中的每一个格子
	 * @return Excel中每一个格子中的值
	 */
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell == null) {
			return "";
		}
		if (hssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	/**
	 * 获取对应借款对象
	 * 
	 * @param record
	 * @throws Exception
	 */
	@Override
	public DebtBorrowWithBLOBs getRecordById(DebtBorrowCommonBean borrowBean) {
		String borrowNid = borrowBean.getBorrowNid();
		if (StringUtils.isNotEmpty(borrowNid)) {
			DebtBorrowExample debtBorrowExample = new DebtBorrowExample();
			DebtBorrowExample.Criteria borrowCra = debtBorrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);

			List<DebtBorrowWithBLOBs> borrowList = this.debtBorrowMapper.selectByExampleWithBLOBs(debtBorrowExample);
			if (borrowList.size() > 0) {
				return borrowList.get(0);
			}
		}
		return new DebtBorrowWithBLOBs();
	}
}