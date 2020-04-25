package com.hyjf.batch.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hyjf.batch.debtTransfer.DebtTransferBean;
import com.hyjf.batch.result.account.UserAccountResultBean;
import com.hyjf.batch.result.debtTransfer.DebtTransferResultBean;
import com.hyjf.batch.result.sigtranTransfer.SigtranTransferResultBean;
import com.hyjf.batch.result.subjectTransfer.SubjectTransferResultBean;
import com.hyjf.batch.sigtranTransfer.SigtranTransferBean;
import com.hyjf.batch.subject.transfer.SubjectTransferBean;
import com.hyjf.batch.user.account.UserAccountBean;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;

/**
 * <p>
 * 共通方法文件
 * </p>
 *
 * @author Michael
 * @version 1.0.0
 */
public class TransUtil {

	private static final Logger LOG = LoggerFactory.getLogger(TransUtil.class);

	/**
	 * 分割开户结果文件
	 * 
	 * @param message
	 * @return
	 */
	public static UserAccountResultBean splitUserAccountMessage(String message) {

		UserAccountResultBean resultBean = new UserAccountResultBean();
		if (StringUtils.isEmpty(message)) {
			return null;
		}
		// 结果文件数据
		byte[] msgBytes = null;
		try {
			msgBytes = message.getBytes("GBK");
			if (msgBytes.length != TransConstants.ACCRETLENGTH) {
				LOG.info("文件长度不正确，抛出数据：" + message);
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		String accountId = substring(msgBytes, 0, 19);
		String idCard = substring(msgBytes, 19, 37);
		String idType = substring(msgBytes, 37, 39);
		String flag = substring(msgBytes, 39, 40);
		String errCode = substring(msgBytes, 40, 43);
		String name = substring(msgBytes, 43, 103);
		String accType = substring(msgBytes, 103, 104);
		String appId = substring(msgBytes, 104, 164);
		String mobile = substring(msgBytes, 164, 176);
		String info = substring(msgBytes, 176, 276);
		String revers = substring(msgBytes, 276, 364);

		resultBean.setAccountId(accountId.trim());
		resultBean.setIdCard(idCard.trim());
		resultBean.setIdType(idType.trim());
		resultBean.setFlag(flag.trim());
		resultBean.setErrCode(errCode);
		resultBean.setName(name.trim());
		resultBean.setAccType(accType);
		resultBean.setAppId(appId.trim());
		resultBean.setMobile(mobile.trim());
		resultBean.setInfo(info);
		resultBean.setRevers(revers);

		return resultBean;
	}

	/**
	 * 拼接批量开户信息
	 * 
	 * @param userAccountBean
	 * @return
	 */
	public static String jointUserAccountMessage(UserAccountBean userAccountBean) {
		StringBuilder sb = new StringBuilder();
		// 身份证号
		sb.append(fillEmptyRight(userAccountBean.getIdCard(), 18));
		if (userAccountBean.getIdCard().length() < 18) {
			sb.append(TransConstants.ID_TYPE_2);// 15位身份证
		} else {
			sb.append(TransConstants.ID_TYPE_1); // 18位身份证
		}
		// 姓名
		sb.append(fillEmptyRight(userAccountBean.getName(), 60));
		// 性别
		if (userAccountBean.getSex().equals("0")) {
			sb.append(TransConstants.BLANK);
		} else {
			sb.append(userAccountBean.getSex());
		}
		// 手机号
		sb.append(fillEmptyRight(userAccountBean.getMobile(), 12));
		// 账户类型
		sb.append(TransConstants.ACCOUNT_TYPE);// 个人账户
		// 邮箱
		sb.append(fillEmptyRight(TransConstants.BLANK, 40));
		// 用户id
		sb.append(fillEmptyRight(userAccountBean.getUserId(), 60));
		// 营业执照编号
		sb.append(fillEmptyRight(TransConstants.BLANK, 9));
		// 税务登记号
		sb.append(fillEmptyRight(TransConstants.BLANK, 30));
		// 渠道推荐码
		sb.append(fillEmptyRight(TransConstants.BLANK, 20));
		// 账户类型
		sb.append(TransConstants.ACC_TYPE_2);// 活期账户
		// 基金公司代码
		sb.append(fillEmptyRight(TransConstants.BLANK, 2));
		// 请求方保留信息
		sb.append(fillEmptyRight(TransConstants.BLANK, 100));
		// 对公账户号
		sb.append(fillEmptyRight(TransConstants.BLANK, 42));
		// 营业执照编号1
		sb.append(fillEmptyRight(TransConstants.BLANK, 18));
		// 保留域
		sb.append(fillEmptyRight(TransConstants.BLANK, 17));
		return sb.toString();
	}

	/**
	 * 分割标的迁移结果文件
	 * 
	 * @param message
	 * @author Libin
	 * @return
	 */
	public static SubjectTransferResultBean splitSubjectTransferMessage(String message) {
		SubjectTransferResultBean resultBean = new SubjectTransferResultBean();
		if (StringUtils.isEmpty(message)) {
			return null;
		}
		// 结果文件数据
		byte[] msgBytes = null;
		try {
			msgBytes = message.getBytes("GBK");
			if (msgBytes.length != TransConstants.SUBJECTRETLENGTH) {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		
		String borrowNid = substring(msgBytes, 10, 50);//标的号
		String prodDesc = substring(msgBytes, 50, 110);//标的描述
		String borrowerElecAcc = substring(msgBytes, 110, 129);//借款人电子账号
		String amount = formatAmount(substring(msgBytes, 189, 202));// 0000000010010  金额
		String respCode = substring(msgBytes, 271, 273);//返回码
		String reserved = substring(msgBytes, 273, 373);//保留域（borrow id）
		
		resultBean.setBorrowNid(borrowNid.trim());
		resultBean.setProdDesc(prodDesc.trim());
		resultBean.setBorrowerElecAcc(borrowerElecAcc.trim());
		resultBean.setAmount(amount.trim());
		resultBean.setRespCode(respCode.trim());
		resultBean.setBorrowId(reserved.trim());
		return resultBean;
	}

	/**
	 * 拼接标的迁移信息
	 * 
	 * @param subjectTransferBean
	 * @author Libin
	 * @return
	 */
	public static String jointSubjectTransferMessage(SubjectTransferBean subjectTransferBean) {

		StringBuilder sb = new StringBuilder();
		
		// 托管银行编号
		sb.append(fillEmptyRight(TransConstants.BANK_CODE,4));
		// 批次号
		sb.append(fillEmptyRight(GetOrderIdUtils.getCurrentBatchId(),6));	
		// P2P端产品编号(borrow_nid)
		sb.append(fillEmptyRight(subjectTransferBean.getBorrowNid(), 40));
		// 产品描述
		sb.append(fillEmptyRight(subjectTransferBean.getProdDesc(), 60));
		// 借款人电子账号
		sb.append(fillEmptyRight(subjectTransferBean.getBorrowerElecAcc(), 19));
		// 借款金额
		sb.append(amountLeftComplement(subjectTransferBean.getAmount(), 13));
		// 付息方式
		sb.append(integerComplement(subjectTransferBean.getPaymentStyle(), 1));
		// 利息每月支付日,若付息方式为0，利息每月支付日可不填送空
		if(subjectTransferBean.getPaymentStyle() == 0 || subjectTransferBean.getPaymentStyle() == 2){
			sb.append(fillEmptyRight(TransConstants.BLANK, 2));
		} else {
			sb.append(integerComplement(Integer.parseInt(subjectTransferBean.getInterestPayDate()), 2));
		}
		// 项目期限
		sb.append(integerComplement(subjectTransferBean.getPeriod(), 4));
		// 预计年化收益率
		sb.append(rateComplement(subjectTransferBean.getExpectAnnualRate(), 8));
		// 担保人电子账号
		if(StringUtils.isBlank(subjectTransferBean.getGuarantorElecAcc())){
			sb.append(fillEmptyRight(TransConstants.BLANK, 19));
		}else{
			sb.append(fillEmptyRight(subjectTransferBean.getGuarantorElecAcc(), 19));
		}
		// 名义借款人电子账号
		sb.append(fillEmptyRight(TransConstants.BLANK, 19));
		// 多种借款人模式标志
		sb.append(integerComplement(0, 1));
		// 收款人电子账号
		sb.append(fillEmptyRight(TransConstants.BLANK, 19));
		// 受托支付标识
		sb.append(integerComplement(0, 1));
		// 保留域
		sb.append(fillEmptyRight(subjectTransferBean.getP2pProdId(), 100));
		// 第三方平台保留域
		sb.append(fillEmptyRight(TransConstants.BLANK, 100));
		
		// 募集日
//		sb.append(fillEmptyRight(subjectTransferBean.getRaiseDate(), 8));
//		// 募集结束日期
//		sb.append(fillEmptyRight(subjectTransferBean.getRaiseEndDate(), 8));

		return sb.toString();

	}

	/**
	 * 取得标的请求付息方式
	 * 
	 * @author Libin
	 * @return
	 */
	public static int getPaymentStyle(String paymentStyle) {

		// 是否月标(true:月标, false:天标)
		boolean isMonth = TransConstants.BORROW_STYLE_PRINCIPAL.equals(paymentStyle) || TransConstants.BORROW_STYLE_MONTH.equals(paymentStyle)
				|| TransConstants.BORROW_STYLE_ENDMONTH.equals(paymentStyle);
		if (isMonth) {
			return 2;
		} else {
			return 0;
		}
	}

	/**
	 * 取得标的请求项目期限
	 * 
	 * @author Libin
	 * @return
	 */
	public static Integer getPeriod(int period, String paymentStyle) {
		if (!paymentStyle.equals("endday")) {
			return period * 30;
		} else {
			return period;
		}

	}

	/**
	 * 取得标的请求募集结束日期
	 * 
	 * @author Libin
	 * @return
	 */
	public static String getRaiseEndDate(String raiseDate, String raiseEndDate) {
		Integer date = GetDate.countDate(Integer.parseInt(raiseDate), 5, Integer.parseInt(raiseEndDate));
		return GetDate.getDateMyTimeInMillisYYYYMMDD(date);
	}

	/**
	 * 字符类型向右补空格
	 * 
	 * @param message
	 * @param len
	 * @return
	 */
	public static String fillEmptyRight(String message, int len) {
		int byteLen = 0;
		if (!isChineseChar(message)) {
			return String.format("%-" + len + "s", message);
		}
		try {
			byteLen = message.getBytes("GBK").length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(message);
		if (byteLen < len) {
			for (int i = 0; i < len - byteLen; i++) {
				sb.append(TransConstants.BLANK);
			}
		}
		return sb.toString();
	}

	/**
	 * 判断是否包含中文字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChineseChar(String str) {
		boolean temp = false;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			temp = true;
		}
		return temp;
	}

	/**
	 * 按字节截取字符串
	 * 
	 * @param orignal
	 *            原始字符串
	 * @param beginIndex
	 *            开始位
	 * @param endIndex
	 *            结束位
	 * @return
	 */
	public static String substring(String orignal, int beginIndex, int endIndex) {
		// 原始字符不为null，也不是空字符串
		if (StringUtils.isNotEmpty(orignal)) {
			// 将原始字符串转换为GBK编码格式
			try {
				orignal = new String(orignal.getBytes(), "GBK");
				// 原始字节
				byte[] msgBytes = orignal.getBytes("GBK");
				// 新字节
				byte[] newBytes = new byte[endIndex - beginIndex];
				// 要截取的字节数大于0，且小于原始字符串的字节数
				if (endIndex > 0 && endIndex <= msgBytes.length) {
					for (int i = 0; i < endIndex - beginIndex; i++) {
						newBytes[i] = msgBytes[beginIndex + i];
					}
				}
				return new String(newBytes, "GBK");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orignal;
	}

	/**
	 * 按字节截取字符串
	 * 
	 * @param msgBytes
	 *            原始字符数组
	 * @param beginIndex
	 *            开始位
	 * @param endIndex
	 *            结束位
	 * @return
	 */
	public static String substring(byte[] msgBytes, int beginIndex, int endIndex) {
		// 数组不为空
		if (msgBytes.length > 0) {
			// 将原始字符串转换为GBK编码格式
			try {
				// 新字节
				byte[] newBytes = new byte[endIndex - beginIndex];
				// 要截取的字节数大于0，且小于原始字符串的字节数
				if (endIndex > 0 && endIndex <= msgBytes.length) {
					for (int i = 0; i < endIndex - beginIndex; i++) {
						newBytes[i] = msgBytes[beginIndex + i];
					}
				}
				return new String(newBytes, "GBK");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 金额补位 当前持有债权金额
	 * 
	 * @param bd
	 *            需要格式化的数字
	 * @param fixedLength
	 *            固定长度
	 * @return
	 */
	public static String amountLeftComplement(BigDecimal bd, int fixedLength) {

		DecimalFormat df = new DecimalFormat("#.00");

		String oriStr = df.format(bd).toString().replace(".", "");

		String filledNum = "0";
		int strlen = oriStr.length();

		if (strlen < fixedLength) {
			for (int i = 0; i < fixedLength - strlen; i++) { // 补13位
				oriStr = filledNum + oriStr;
			}
		}
		return oriStr;
	}

	/**
	 * 利率补位 预计年化收益率
	 * 
	 * @param bd
	 *            需要格式化的数字
	 * @param fixedLength
	 *            固定长度
	 * @return
	 */
	public static String rateComplement(BigDecimal bd, int fixedLength) {

		double convertFromBigdecimal = bd.doubleValue();
		int lengthAfterComma = fixedLength - 3;// 整数最多3位

		// 补小数缺位
		if (lengthAfterComma < 0) {
			throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
		}
		if (lengthAfterComma == 0) {
			return new DecimalFormat("0").format(convertFromBigdecimal);
		}
		String formatStr = "0.";
		String decimal = "";
		for (int i = 0; i < lengthAfterComma; i++) {
			formatStr = formatStr + "0";
		}
		decimal = new DecimalFormat(formatStr).format(convertFromBigdecimal).replace(".", "");

		// 补整数缺位
		String filledNum = "0";
		if (decimal.length() < fixedLength) {
			for (int i = 0; i <= fixedLength - decimal.length(); i++) {
				decimal = filledNum + decimal;
			}
		}

		return decimal;
	}

	/**
	 * 通过获取用户身份证号，计算用户年龄
	 * 
	 * @param cardNo
	 *            身份证号
	 * @author Chenyanwei
	 * @return
	 */
	public static int cardNoToAge(String cardNo) {
		int leh = cardNo.length();
		String dates = "";
		if (leh == 18) {
			dates = cardNo.substring(6, 10);
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			String year = df.format(new Date());
			int age = Integer.parseInt(year) - Integer.parseInt(dates);
			return age;
		} else {
			dates = cardNo.substring(6, 8);
			return Integer.parseInt(dates);
		}
	}

	/**
	 * 生成产品文件名
	 * 
	 * @param numProduct
	 *            0 批量开户 1 债转迁移 2 标的迁移 3 签约关系迁移
	 * @author Chenyanwei
	 * @return
	 */
	public static String createFilesName(int numProduct) {

		String fileName = "";

		if (numProduct == 0) {
			fileName = TransConstants.BANK_CODE + TransConstants.SYMBOL + TransConstants.PRODUCT_CODE_APPZX + TransConstants.PRODUCT_CODE + TransConstants.SYMBOL + GetOrderIdUtils.getDataBatchNo()
					+ TransConstants.SYMBOL + TransConstants.SIT_TIME;// 批量开户

		} else if (numProduct == 1) {
			fileName = TransConstants.BANK_CODE + TransConstants.SYMBOL + TransConstants.PRODUCT_CODE_BID + TransConstants.SYMBOL + GetOrderIdUtils.getDataBatchNo() + TransConstants.SYMBOL
					+ TransConstants.SIT_TIME;// 债转迁移

		} else if (numProduct == 2) {
			fileName = TransConstants.BANK_CODE + TransConstants.SYMBOL + TransConstants.PRODUCT_CODE_BIDIN + TransConstants.SYMBOL + TransConstants.COINST_CODE + TransConstants.SYMBOL
					+ GetOrderIdUtils.getDataBatchNo() + TransConstants.SYMBOL
					+ TransConstants.SIT_TIME;// 标的迁移

		} else if (numProduct == 3) {
			fileName = TransConstants.BANK_CODE + TransConstants.SYMBOL + TransConstants.PRODUCT_CODE + TransConstants.SYMBOL + TransConstants.PRODUCT_CODE_SIGTRAN + TransConstants.SYMBOL
					+ GetOrderIdUtils.getDataBatchNo() + TransConstants.SYMBOL + TransConstants.SIT_TIME;// 签约关系迁移

		} else {
			return null;
		}
		return fileName;
	}

	/**
	 * 取得BigDecimal的字符串
	 * 
	 * @author Libin
	 * @return
	 */
	public static String formatAmount(String number) {
		Integer value = Integer.valueOf(number);
		BigDecimal bd = new BigDecimal(value);
		bd = bd.multiply(new BigDecimal("0.01"));
		return bd.toString();
	}

	/**
	 * 拼接债权转移信息
	 * 
	 * @param DebtTransferBean
	 * @return
	 */
	public static String jointDebtTransferMessage(DebtTransferBean debtTransferBean) {
		// read data from Reader
		StringBuilder sb = new StringBuilder();
		// 银行代号
		sb.append(fillEmptyRight(debtTransferBean.getBankId(), 4));
		// 批次号
		sb.append(fillEmptyRight(debtTransferBean.getBatchId(), 6));
		// 债权持有人电子账号
		sb.append(fillEmptyRight(debtTransferBean.getDebtHolderAcc(), 19));
		// 产品发行方
		sb.append(fillEmptyRight(debtTransferBean.getProdIssuer(), 4));
		// 产品编号
		sb.append(fillEmptyRight(TransConstants.BLANK, 6));
		// 申请流水号
		sb.append(fillEmptyRight(debtTransferBean.getSerialNum(), 40));
		// 当前持有债权金额
		sb.append(amountLeftComplement(debtTransferBean.getAmount(), 13));
		// 债权获取日期
		sb.append(fillEmptyRight(debtTransferBean.getDebtObtDate(), 8));
		// 起息日
		sb.append(fillEmptyRight(debtTransferBean.getIntStDate(), 8));
		// 付息方式
		sb.append(integerComplement(debtTransferBean.getIntStStyle(), 1));
		// 利息每月支付日,若付息方式为0，利息每月支付日可不填送空
		if(debtTransferBean.getIntStStyle().equals(0)){
			sb.append(fillEmptyRight(TransConstants.BLANK, 2));
		} else {
			sb.append(integerComplement(Integer.parseInt(debtTransferBean.getIntPaydate()), 2));
		}
		// 产品到期日
		sb.append(fillEmptyRight(debtTransferBean.getEndDate(), 8));
		// 预计年化收益率
		sb.append(rateComplement(debtTransferBean.getExpectAnualRate(), 8));
		// 币种
		sb.append(fillEmptyRight(debtTransferBean.getCurrType(), 3));
		// 标的编号（扩位）
		sb.append(fillEmptyRight(debtTransferBean.getBorrowNid(), 40));
		// 保留域
		sb.append(fillEmptyRight(debtTransferBean.getProdNum(), 60));
		// 此处不拼接 order_id 和 type
		// 此处不拼接 借款人用户id 和 出借人用户id和待还利息和已还利息
		return sb.toString();

	}

	/**
	 * 补整数缺位
	 * 
	 * @param bd
	 *            需要格式化的数字
	 * @param fixedLength
	 *            固定长度
	 * @return
	 */
	public static String integerComplement(int youNumber, int fixedLength) {
		// 0 代表前面补充0
		// length 代表长度为
		// d 代表参数为正数型
		String length = String.valueOf(fixedLength);
		String str = String.format("%0" + length + "d", youNumber);
		return str;

	}

	/**
	 * 分割债权迁移结果文件
	 * 
	 * @param message
	 * @return
	 */
	public static DebtTransferResultBean splitDebtTransferResultMessage(String message) {

		DebtTransferResultBean resultBean = new DebtTransferResultBean();
		if (StringUtils.isEmpty(message)) {
			return null;
		}
		// 结果文件数据
		byte[] msgBytes = null;
		try {
			msgBytes = message.getBytes("GBK");
			if (msgBytes.length != TransConstants.DEBTRETLENGTH) {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		String bankId = substring(msgBytes, 0, 4);
		String batchId = substring(msgBytes, 4, 10);
		String debtHolderAcc = substring(msgBytes, 10, 29);
		String prodIssuer = substring(msgBytes, 29, 33);
		String prodNum = substring(msgBytes, 33, 39);
		String serialNum = substring(msgBytes, 39, 79);
		String amount = TransUtil.formatAmount(substring(msgBytes, 79, 92));
		String holderName = substring(msgBytes, 92, 152);
		String dealDate = substring(msgBytes, 152, 160);
		String respCode = substring(msgBytes, 160, 162);
		String authCode = substring(msgBytes, 162, 182);
		String borrowNid = substring(msgBytes, 182, 222);
		String revers = substring(msgBytes, 222, 282);

		resultBean.setBankId(bankId.trim());
		resultBean.setBatchId(batchId.trim());
		resultBean.setDebtHolderAcc(debtHolderAcc.trim());
		resultBean.setProdIssuer(prodIssuer.trim());
		resultBean.setProdNum(prodNum.trim());
		resultBean.setSerialNum(serialNum.trim());
		resultBean.setAmount(new BigDecimal(amount.trim()));
		resultBean.setHolderName(holderName.trim());
		resultBean.setDealDate(dealDate.trim());
		resultBean.setRespCode(respCode.trim());
		resultBean.setAuthCode(authCode.trim());
		resultBean.setRevers(revers.trim());
		resultBean.setBorrowNid(borrowNid);

		return resultBean;
	}

	/**
	 * 分割签约关系迁移结果文件
	 * 
	 * @param message
	 * @author Chenyanwei
	 * @return
	 * @return
	 */
	public static SigtranTransferResultBean splitSigtranTransferResultMessage(String message) {
		SigtranTransferResultBean resultBean = new SigtranTransferResultBean();
		if (StringUtils.isEmpty(message)) {
			return null;
		}
		// 结果文件数据
		byte[] msgBytes = null;
		try {
			msgBytes = message.getBytes("GBK");
			if (msgBytes.length != TransConstants.SIGRETLENGTH) {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		String bankId = substring(msgBytes, 0, 4);
		String batchId = substring(msgBytes, 4, 10);
		String sigCardnnbr = substring(msgBytes, 10, 29);
		String prodFuissuer = substring(msgBytes, 29, 33);
		String sigType = substring(msgBytes, 33, 34);
		String sigNo = substring(msgBytes, 34, 74);
		String sigDate = substring(msgBytes, 74, 82);
		String sigTime = substring(msgBytes, 82, 88);
		String rspCode = substring(msgBytes, 88, 90);
		String reserved = substring(msgBytes, 90, 190);
		String trdresv = substring(msgBytes, 190, 290);

		resultBean.setBankId(bankId.trim());
		resultBean.setBatchId(batchId.trim());
		resultBean.setSigCardnnbr(sigCardnnbr.trim());
		resultBean.setProdFuissuer(prodFuissuer.trim());
		resultBean.setSigType(sigType.trim());
		resultBean.setSigNo(sigNo.trim());
		resultBean.setSigDate(sigDate.trim());
		resultBean.setSigTime(sigTime.trim());
		resultBean.setRspCode(rspCode.trim());
		resultBean.setReserved(reserved.trim());
		resultBean.setTrdResv(trdresv.trim());

		return resultBean;
	}

	/**
	 * 拼接签约关系迁移文件请求
	 * 
	 * @param SigtranTransferBean
	 * @return
	 */
	public static String jointSigtranTransferMessage(SigtranTransferBean sigtranTransferBean) {
		StringBuilder sb = new StringBuilder();
		// 银行代号
		sb.append(integerComplement(Integer.valueOf(TransConstants.BANK_CODE), 4));
		// 批次号
		sb.append(integerComplement(Integer.valueOf(sigtranTransferBean.getBatchId()), 6));
		// 签约电子账号
		sb.append(fillEmptyRight(sigtranTransferBean.getSigCardnnbr(), 19));
		// 产品发行方
		sb.append(fillEmptyRight(TransConstants.PRODUCT_USER, 4));
		// 签约类型
		sb.append(fillEmptyRight(sigtranTransferBean.getSigType(), 1));
		// 签约流水号
		sb.append(fillEmptyRight(sigtranTransferBean.getSigNo(), 40));
		// 签约日期
		sb.append(integerComplement(Integer.valueOf(sigtranTransferBean.getSigDate()), 8));
		// 签约时间
		sb.append(integerComplement(Integer.valueOf(sigtranTransferBean.getSigTime()), 6));
		// 保留域
		sb.append(fillEmptyRight(sigtranTransferBean.getSigReserved(), 100));
		// 第三方保留域
		sb.append(fillEmptyRight(sigtranTransferBean.getSigTrdresv(), 100));

		return sb.toString();

	}

	public static String transferDate(String dateStr) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		try {
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf1.format(date);
	}

	public static String transferTime(String dateStr) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
		try {
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf2.format(date);

	}

	public static void main(String[] args) {
		// System.out.println(fillEmptyRight("HDD测试标的第一期", 60));
		 System.out.println(rateComplement(new BigDecimal("11"), 8));
	}

}
