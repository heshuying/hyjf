package com.hyjf.batch.util;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.AleveLog;
import com.hyjf.mybatis.model.auto.EveLog;

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
	public static ArrayList<AleveLog> readFileAleve(File fin) throws IOException{

		if (fin==null) {
			return null;
		}

		 FileInputStream fis = new FileInputStream(fin);    
	     BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));  
	       
	        String line = null;  
	        int i = 1;
	        ArrayList<AleveLog> list = new ArrayList<>();
	        while ((line = br.readLine()) != null) { 
	         // 结果文件数据
	            byte[] msgBytes = null;
	                msgBytes = line.getBytes("GBK");
	                if (msgBytes.length != TransConstants.ALEVELENGTH) {
	                    LOG.info("文件长度不正确，抛出数据：" + line+"------msgBytes.length:"+msgBytes.length);
	                    //return null;
	                }else{
	                    String bank = substring(line,0, 4);
	                    String CARDNBR = substring(line,4, 23);
	                    String AMOUNT = substring(line,23, 40);
	                    String CUR_NUM = substring(line,40, 43);
	                    String CRFLAG = substring(line,43, 44);
	                    String VALDATE = substring(line,44, 52);
	                    String INPDATE = substring(line,52, 60);
	                    String RELDATE = substring(line,60, 68);
	                    String INPTIME = substring(line,68, 76);
	                    String TRANNO = substring(line,76, 82);
	                    String ORI_TRANNO = substring(line,82, 88);
	                    
	                    String TRANSTYPE = substring(line,88, 92);//交易类型
	                    String DESLINE = substring(line,92, 134);//交易描述
	                    String CURR_BAL = substring(line,134, 151);//交易后余额
	                    String FORCARDNBR = substring(line,151, 170);//对手交易帐号
	                    String REVIND = substring(line,170, 171);//冲正、撤销标志
	                    String ACCCHG = substring(line,171, 172);//交易标识
	                    String SEQNO = substring(line,172, 178);//系统跟踪号
	                    String ORI_NUM = substring(line,178, 184);//原交易流水号
	                    
	                    AleveLog aleve = new AleveLog();
	                    aleve.setBank(StringUtils.isNotEmpty(bank)?Integer.valueOf(bank):0);
	                    aleve.setCardnbr(CARDNBR);
	                    aleve.setAmount(StringUtils.isNotEmpty(AMOUNT)?BigDecimal.valueOf(Double.valueOf(AMOUNT)/100):BigDecimal.valueOf(0.00));
	                    aleve.setCurNum(StringUtils.isNotEmpty(CUR_NUM)?Integer.valueOf(CUR_NUM):0);
	                    aleve.setCrflag(CRFLAG);
	                    aleve.setValdate(StringUtils.isNotEmpty(VALDATE)?VALDATE:"");
	                    aleve.setInpdate(StringUtils.isNotEmpty(INPDATE)?INPDATE:"");
	                    aleve.setReldate(StringUtils.isNotEmpty(RELDATE)?RELDATE:"");
	                    aleve.setInptime(StringUtils.isNotEmpty(INPTIME)?Integer.valueOf(INPTIME):0);
	                    aleve.setTranno(TRANNO);
	                    aleve.setOriTranno(StringUtils.isNotEmpty(ORI_TRANNO)?Integer.valueOf(ORI_TRANNO):0);
	                    //String RESV = line.substring(184, 371);//保留域
	                    
	                    aleve.setTranstype(StringUtils.isNotEmpty(TRANSTYPE)?Integer.valueOf(TRANSTYPE):0);
	                    aleve.setDesline(DESLINE);
	                    aleve.setCurrBal(StringUtils.isNotEmpty(CURR_BAL)?BigDecimal.valueOf(Double.valueOf(CURR_BAL)/100):BigDecimal.valueOf(0.00));
	                    aleve.setForcardnbr(FORCARDNBR);
	                    aleve.setRevind(StringUtils.isNotEmpty(REVIND)?Integer.valueOf(REVIND):0);
	                    aleve.setAccchg(ACCCHG);
	                    aleve.setSeqno(StringUtils.isNotEmpty(SEQNO)?Integer.valueOf(SEQNO):0);
	                    aleve.setOriNum(StringUtils.isNotEmpty(ORI_NUM)?Integer.valueOf(ORI_NUM.trim()):0);
	                    list.add(aleve);
	                    
	                   
	                    i++;
	                    System.out.println(line.length()+"**********************************************************************");
	                }
	        }  
	       
	        br.close();
	        return list;  
	}
	
	
	public static ArrayList<EveLog> readFileEve(File fin) throws IOException{

        if (fin==null) {
            return null;
        }

         FileInputStream fis = new FileInputStream(fin);    
         BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));  
           
            String line = null;  
            int i = 1;
            ArrayList<EveLog> list = new ArrayList<>();
            while ((line = br.readLine()) != null) { 
             // 结果文件数据
                byte[] msgBytes = null;
                    msgBytes = line.getBytes("GBK");
                    if (msgBytes.length != 139) {
                        LOG.info("文件长度不正确，抛出数据：" + line);
                        return null;
                    }
                String forcode = substring(line,0, 11).trim();//发送方标识码
                String seqno = substring(line,11, 17).trim();//系统跟踪号
                String cendt = substring(line,17, 27).trim();//交易传输时间
                String cardnbr = substring(line,27, 46).trim();//主账号
                String amount = substring(line,46, 58).trim();//交易金额
                String crflag = substring(line,58, 59).trim();//交易金额符号--小于零等于C；大于零等于D；
                String msgtype = substring(line,59, 63).trim();//消息类型--提现冲正交易是0420
                String proccode = substring(line,63, 69).trim();//交易类型码
                String orderno = substring(line,69, 109).trim();//订单号
                String tranno = substring(line,109, 115).trim();//内部交易流水号
                String reserved = substring(line,115, 134).trim();//内部保留域
                String revind = substring(line,134, 135).trim();//冲正、撤销标志 --1-已撤销/冲正空或0-正常交易
                String transtype = substring(line,135, 139).trim();//交易类型
                String beforeDate = DateUtils.getBeforeDateOfDay();//获取前一天时间返回时间类型 yyyyMMdd
                EveLog eve = new EveLog();
                eve.setForcode(forcode);
                eve.setSeqno(StringUtils.isNotEmpty(seqno)?Integer.valueOf(seqno):0);
                eve.setCendt(StringUtils.isNotEmpty(cendt)?Integer.valueOf(cendt):0);
                eve.setCardnbr(cardnbr);
                eve.setAmount(StringUtils.isNotEmpty(amount)?BigDecimal.valueOf(Double.valueOf(amount)/100):BigDecimal.valueOf(0.00));
                eve.setCrflag(crflag);
                eve.setMsgtype(StringUtils.isNotEmpty(msgtype)?Integer.valueOf(msgtype):0);
                eve.setProccode(StringUtils.isNotEmpty(proccode)?Integer.valueOf(proccode):0);
                eve.setOrderno(orderno);
                eve.setTranno(tranno);
                eve.setReserved(reserved);
                eve.setRevind(StringUtils.isNotEmpty(revind)?Integer.valueOf(revind):0);
                eve.setTranstype(StringUtils.isNotEmpty(transtype)?Integer.valueOf(transtype):0);
                eve.setCreateDay(beforeDate);
                list.add(eve);
                i++;
                System.out.println(line.length()+"**********************************************************************");
                //if( i==20) break;
                
                
            }  
           
            br.close();
            return list;  
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
				orignal = new String(orignal.getBytes(), "utf-8");
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


}
