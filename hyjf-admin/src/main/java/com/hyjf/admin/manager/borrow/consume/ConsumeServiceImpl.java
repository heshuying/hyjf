package com.hyjf.admin.manager.borrow.consume;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonBean;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonFile;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonFileData;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonImage;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonNameAccount;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowConsumeExample;
import com.hyjf.mybatis.model.auto.BorrowConsumeWithBLOBs;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.Consume;
import com.hyjf.mybatis.model.auto.ConsumeExample;
import com.hyjf.mybatis.model.auto.ConsumeList;
import com.hyjf.mybatis.model.auto.ConsumeListExample;

@Service
public class ConsumeServiceImpl extends BaseServiceImpl implements ConsumeService {

	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 权限维护插入
	 * 
	 * @param record
	 * @throws Exception
	 */
	@Override
	public void insertRecord(BorrowCommonBean borrowBean) throws Exception {

		// 项目类型
		String projectType = borrowBean.getProjectType();
		String beforeFix = this.borrowCommonService.getBorrowProjectClass(projectType);
		String borrowPreNid = borrowBean.getBorrowPreNid();
		String newBorrowPreNid=borrowPreNid;
		List<BorrowCommonNameAccount> borrowCommonNameAccountList = borrowBean.getBorrowCommonNameAccountList();

		String isChaibiao = borrowBean.getIsChaibiao();
		// 项目名称 & 借款金额
		if (!"yes".equals(isChaibiao)) {
			borrowCommonNameAccountList = new ArrayList<BorrowCommonNameAccount>();
			BorrowCommonNameAccount borrowCommonNameAccount = new BorrowCommonNameAccount();
			borrowCommonNameAccount.setNames(borrowBean.getName());
			borrowCommonNameAccount.setAccounts(borrowBean.getAccount());
			borrowCommonNameAccountList.add(borrowCommonNameAccount);
		}

		if (borrowCommonNameAccountList != null && borrowCommonNameAccountList.size() > 0) {
			int i = 1;
			for (BorrowCommonNameAccount borrowCommonNameAccount : borrowCommonNameAccountList) {
				String account = borrowCommonNameAccount.getAccounts();
				if (StringUtils.isNotEmpty(account)) {

					String cNum = String.valueOf(i);
					if (i < 10) {
						cNum = "0" + String.valueOf(i);
					}
					i++;
					String borrowNid = beforeFix + borrowPreNid + cNum;

					// 插入huiyingdai_borrow
					BorrowWithBLOBs borrow = new BorrowWithBLOBs();

					// 借款表插入
					borrow.setConsumeId(borrowBean.getConsumeId());
					this.borrowCommonService.insertBorrowCommonData(borrowBean, borrow, borrowPreNid,newBorrowPreNid, borrowNid,account);

				}
			}

			// 打包完成
			ConsumeExample consumeExample = new ConsumeExample();
			ConsumeExample.Criteria consumeCra = consumeExample.createCriteria();
			consumeCra.andConsumeIdEqualTo(borrowBean.getConsumeId());
			Consume consume = new Consume();
			consume.setRelease(1);
			this.consumeMapper.updateByExampleSelective(consume, consumeExample);

			ConsumeListExample consumeListExample = new ConsumeListExample();
			ConsumeListExample.Criteria consumeListCra = consumeListExample.createCriteria();
			consumeListCra.andConsumeIdEqualTo(borrowBean.getConsumeId());
			ConsumeList consumeList = new ConsumeList();
			consumeList.setRelease(1);
			this.consumeListMapper.updateByExampleSelective(consumeList, consumeListExample);

		}
	}

	/**
	 * 更新
	 * 
	 * @param record
	 * @throws Exception
	 */
	@Override
	public void updateRecord(BorrowCommonBean borrowBean) throws Exception {
		String borrowNid = borrowBean.getBorrowNid();
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowExample borrowExample = new BorrowExample();
			BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);

			List<BorrowWithBLOBs> borrowList = this.borrowMapper.selectByExampleWithBLOBs(borrowExample);

			if (borrowList != null && borrowList.size() == 1) {
				BorrowWithBLOBs record = borrowList.get(0);

				borrowExample = new BorrowExample();
				borrowCra = borrowExample.createCriteria();
				borrowCra.andBorrowPreNidEqualTo(record.getBorrowPreNid());

				List<BorrowWithBLOBs> borrowAllList = this.borrowMapper.selectByExampleWithBLOBs(borrowExample);

				if (borrowAllList != null && borrowAllList.size() > 0) {
					for (BorrowWithBLOBs borrow : borrowAllList) {
						// 借款表更新
						this.borrowCommonService.updateBorrowCommonData(borrowBean, borrow, borrowNid);
					}
				}
			}
		}
	}

	/**
	 * 获取借款信息
	 * 
	 * @param borrow
	 * @return
	 * @author Administrator
	 */
	@Override
	public BorrowCommonBean getBorrow(BorrowCommonBean borrowBean) {

		if (StringUtils.isNotEmpty(borrowBean.getBorrowNid())) {
			// 借款信息数据获取
			return this.borrowCommonService.getBorrow(borrowBean);
		} else {

			if ("BORROW_HXF".equals(borrowBean.getMoveFlag())) {

				BorrowConsumeExample exam = new BorrowConsumeExample();
				BorrowConsumeExample.Criteria borrowConsumeCra = exam.createCriteria();
				borrowConsumeCra.andConsumeClassEqualTo("DAFEI");

				List<BorrowConsumeWithBLOBs> borrowConsumeList = this.borrowConsumeMapper.selectByExampleWithBLOBs(exam);
				if (borrowConsumeList != null && borrowConsumeList.size() > 0) {
					BorrowConsumeWithBLOBs borrowWithBLOBs = borrowConsumeList.get(0);

					JSONObject obj = countUserAccount(borrowWithBLOBs.getName(), borrowBean.getConsumeId(), 2);
					borrowBean.setCunsumeCount(String.valueOf(obj.get("cunsumeCount")));
					String account = String.valueOf(obj.get("amountAll"));
					// 项目名称 借款金额
					// borrowBean.setBorrowCommonNameAccountList((List<BorrowCommonNameAccount>) obj.get("borrowCommonNameAccountList"));
					// 借款预编码
					borrowBean.setBorrowPreNid(this.borrowCommonService.getBorrowPreNid());
					// 项目类型
					borrowBean.setProjectType(borrowWithBLOBs.getProjectType());
					// 项目名称
					int cunsumeCountPlus = this.getConsumeMaxNumber();
					borrowBean.setName(this.getValue(borrowWithBLOBs.getName()) + "-" + cunsumeCountPlus);
					// 借款金额
					borrowBean.setAccount(this.getValue(account));
					// 借款用户
					borrowBean.setUsername(this.getValue(borrowWithBLOBs.getUserName()));
					// 担保机构
					borrowBean.setBorrowMeasuresInstit(this.getValue(borrowWithBLOBs.getBorrowMeasuresInstit()));
					// 机构介绍
					borrowBean.setBorrowCompanyInstruction(this.getValue(borrowWithBLOBs.getBorrowCompanyInstruction()));
					// 风控措施
					borrowBean.setBorrowMeasuresMea(this.getValue(borrowWithBLOBs.getBorrowMeasuresMea()));
					// 财务状况
					borrowBean.setAccountContents(this.getValue(borrowWithBLOBs.getAccountContents()));
					// 项目描述
					borrowBean.setBorrowContents(this.getValue(borrowWithBLOBs.getBorrowContents()));
					// 公司个人区分
					borrowBean.setCompanyOrPersonal(this.getValue(borrowWithBLOBs.getCompanyOrPersonal()));
					// 项目资料
					if (StringUtils.isNotEmpty(borrowWithBLOBs.getFiles())) {
						List<BorrowCommonFile> borrowCommonFileList = JSONArray.parseArray(borrowWithBLOBs.getFiles(), BorrowCommonFile.class);
						if (borrowCommonFileList != null && borrowCommonFileList.size() > 0) {
							// domain URL
							String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));

							for (BorrowCommonFile borrowCommonFile : borrowCommonFileList) {
								List<BorrowCommonFileData> fileDataList = borrowCommonFile.getData();
								if (fileDataList != null && fileDataList.size() > 0) {
									List<BorrowCommonImage> borrowCommonImageList = new ArrayList<BorrowCommonImage>();
									for (BorrowCommonFileData borrowCommonFileData : fileDataList) {
										BorrowCommonImage borrowCommonImage = new BorrowCommonImage();
										borrowCommonImage.setImageName(borrowCommonFileData.getName());
										borrowCommonImage.setImageRealName(borrowCommonFileData.getFileRealName());
										borrowCommonImage.setImageSort(borrowCommonFileData.getImageSort());
										borrowCommonImage.setImagePath(borrowCommonFileData.getFileurl());
										borrowCommonImage.setImageSrc(fileDomainUrl + borrowCommonFileData.getFileurl());
										borrowCommonImageList.add(borrowCommonImage);
									}
									borrowBean.setBorrowCommonImageList(borrowCommonImageList);
								}
							}
						}
					}
					// 还款方式
					borrowBean.setBorrowStyle("month");
					// 用户信息 用户名称
					borrowBean.setComName(this.getValue(borrowWithBLOBs.getUsername()));
					// 用户信息 所在地区 省
					borrowBean.setComLocationProvince(this.getValue(borrowWithBLOBs.getProvince()));
					// 用户信息 所在地区 市
					borrowBean.setComLocationCity(this.getValue(borrowWithBLOBs.getCity()));
					// 用户信息 所在地区 区
					borrowBean.setComLocationArea(this.getValue(borrowWithBLOBs.getArea()));
					// 用户信息 注册资本
					borrowBean.setComRegCaptial(this.getValue(String.valueOf(borrowWithBLOBs.getRegCaptial())));
					// 用户信息 企业注册时间
					borrowBean.setComRegTime(this.getValue(borrowWithBLOBs.getComRegTime()));
					// 用户信息 所属行业
					borrowBean.setComUserIndustry("服务业");

					//List<BorrowProjectType> borrowProjectTypeList = this.borrowCommonService.borrowProjectTypeList(borrowWithBLOBs.getProjectType());    //modified by zhuxiaodong at 2016.5.3 fix projectTypeCd=borrow_cd
					List<BorrowProjectType> borrowProjectTypeList = this.borrowCommonService.borrowProjectTypeList(this.borrowCommonService.getBorrowProjectClass(borrowWithBLOBs.getProjectType()));
					if (borrowProjectTypeList != null && borrowProjectTypeList.size() > 0) {
						BorrowProjectType borrowProjectType = borrowProjectTypeList.get(0);
						// 最低投标金额
						borrowBean.setTenderAccountMin(borrowProjectType.getInvestStart());
						// 最高投标金额
						borrowBean.setTenderAccountMax(borrowProjectType.getInvestEnd());
					}
					// 设置默认值
					// 有效时间
					borrowBean.setBorrowValidTime(this.borrowCommonService.getBorrowConfig("BORROW_VALID_TIME"));

				}
			}
		}

		return borrowBean;

	}

	/**
	 * 获取汇消费的数据数量
	 * 
	 * @return
	 */
	public int getConsumeMaxNumber() {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria cra = example.createCriteria();
		cra.andProjectTypeEqualTo(Integer.valueOf(CustomConstants.HXF_KEY));
		List<Borrow> borrowList = this.borrowMapper.selectByExample(example);
		if (borrowList != null && borrowList.size() > 0) {
			return borrowList.size() + 1;
		}
		return 1;
	}

	/**
	 * 
	 * 获取标题和金额
	 * 
	 * @param consumeId
	 * @param nameCount
	 * @return
	 */
	public JSONObject countUserAccount(String name, String consumeId, Integer nameCount) {
		ConsumeListExample example = new ConsumeListExample();
		ConsumeListExample.Criteria cra = example.createCriteria();
		cra.andConsumeIdEqualTo(consumeId);
		cra.andStatusEqualTo(0);

		List<ConsumeList> consumeListList = this.consumeListMapper.selectByExample(example);

		BigDecimal amount = new BigDecimal("0");
		BigDecimal amountAll = new BigDecimal("0");

		// 通过的人数
		int memberSize = 0;
		// 汇消费的个数
		int cunsumeCountPlus = this.getConsumeMaxNumber();
		int cunsumeCount = cunsumeCountPlus;
		List<BorrowCommonNameAccount> borrowCommonNameAccountList = new ArrayList<BorrowCommonNameAccount>();

		// 循环标题数量
		memberSize = consumeListList.size();

		if (memberSize != 0 && nameCount <= memberSize) {
			// 余数
			int model = memberSize % nameCount;
			// 平均数
			int average = (memberSize - model) / nameCount;
			// 第一次分割的位置
			int splitSize = average + model;
			// 每个标题的开始位置
			int consumeBeginIndex = splitSize;

			NumberFormat numberFormat = new DecimalFormat("####");

			for (int c = 0; c < nameCount; c++) {
				BorrowCommonNameAccount borrowCommonNameAccount = new BorrowCommonNameAccount();
				amount = new BigDecimal("0");
				if (c == 0) {
					for (int i = 0; i < consumeBeginIndex; i++) {
						ConsumeList consumeList = consumeListList.get(i);
						amount = amount.add(consumeList.getCreditAmount());
						amountAll = amountAll.add(consumeList.getCreditAmount());
					}
					borrowCommonNameAccount.setNames(name + "-" + String.valueOf(cunsumeCountPlus));
					borrowCommonNameAccount.setAccounts(numberFormat.format(amount));
					borrowCommonNameAccountList.add(borrowCommonNameAccount);
				} else {
					for (int i = consumeBeginIndex; i < consumeBeginIndex + average; i++) {
						ConsumeList consumeList = consumeListList.get(i);
						amount = amount.add(consumeList.getCreditAmount());
						amountAll = amountAll.add(consumeList.getCreditAmount());

					}
					borrowCommonNameAccount.setNames(name + "-" + String.valueOf(cunsumeCountPlus));
					borrowCommonNameAccount.setAccounts(numberFormat.format(amount));
					borrowCommonNameAccountList.add(borrowCommonNameAccount);
					consumeBeginIndex = consumeBeginIndex + average;
				}
				cunsumeCountPlus++;
			}

			JSONObject obj = new JSONObject();
			obj.put("cunsumeCount", cunsumeCount);
			if (amountAll != null) {
				obj.put("amountAll", numberFormat.format(amountAll));
			} else {
				obj.put("amountAll", "");
			}
			obj.put("errorMsg", "");
			obj.put("borrowCommonNameAccountList", borrowCommonNameAccountList);
			return obj;
		}
		JSONObject obj = new JSONObject();
		obj.put("cunsumeCount", 1);
		obj.put("amountAll", "");
		if (nameCount > memberSize) {
			obj.put("errorMsg", "拆标行数不能大于借款人数！");
		} else {
			obj.put("errorMsg", "");
		}
		obj.put("borrowCommonNameAccountList", new ArrayList<BorrowCommonNameAccount>());
		return obj;
	}

	/**
	 * 获取放款服务费率 & 还款服务费率
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

		if (!GenericValidator.isLong(borrowPeriod) || !NumberUtils.isNumber(borrowPeriod) || Integer.valueOf(borrowPeriod) < 0 || StringUtils.isEmpty(borrowStyle)) {
			jsonObject.put("borrowManagerScale", "--");
			jsonObject.put("borrowManagerScaleEnd", "--");
			return jsonObject.toJSONString();
		}

		// 还款服务费率
		jsonObject = this.borrowCommonService.getBorrowManagerScale(projectType,borrowStyle, Integer.valueOf(borrowPeriod), jsonObject);

		return jsonObject.toJSONString();
	}

	private String getValue(String value) {
		if (StringUtils.isNotEmpty(value)) {
			return value;
		}
		return "";
	}
}