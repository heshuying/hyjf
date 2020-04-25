package com.hyjf.batch.util;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;

import cn.jpush.api.utils.StringUtils;

import com.hyjf.mybatis.model.auto.AleveLog;
import com.hyjf.mybatis.model.auto.EveLog;

public class TXTParseUtils {


    public static ArrayList<AleveLog> readFileAleve(File fin) throws IOException {  
        FileInputStream fis = new FileInputStream(fin);  
        
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));  
       
        String line = null;  
        int i = 1;
        ArrayList<AleveLog> list = new ArrayList<>();
        while ((line = br.readLine()) != null) { 
            byte[] msgBytes = null;
            /*String bank = line.substring(0, 4);
            String CARDNBR = line.substring(4, 23);
            String AMOUNT = line.substring(23, 40);
            String CUR_NUM = line.substring(40, 43);
            String CRFLAG = line.substring(43, 44);
            String VALDATE = line.substring(44, 52);
            String INPDATE = line.substring(52, 60);
            String RELDATE = line.substring(60, 68);
            String INPTIME = line.substring(68, 76);
            String TRANNO = line.substring(76, 82);
            String ORI_TRANNO = line.substring(82, 88);
            
            String TRANSTYPE = line.substring(88, 92);//交易类型
            String DESLINE = line.substring(92, 134);//交易描述
            String CURR_BAL = line.substring(134, 151);//交易后余额
            String FORCARDNBR = line.substring(151, 170);//对手交易帐号
            String REVIND = line.substring(170, 171);//冲正、撤销标志
            String ACCCHG = line.substring(171, 172);//交易标识
            String SEQNO = line.substring(172, 178);//系统跟踪号
            String ORI_NUM = line.substring(178, 184);//原交易流水号
            */           
            msgBytes = line.getBytes("GBK");
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
           /* aleve.setBank(StringUtils.isNotEmpty(bank)?Integer.valueOf(bank):0);
            aleve.setCardnbr(CARDNBR);
            aleve.setAmount(StringUtils.isNotEmpty(AMOUNT)?BigDecimal.valueOf(Double.valueOf(AMOUNT)/100):BigDecimal.valueOf(0.00));
            aleve.setCurNum(StringUtils.isNotEmpty(CUR_NUM)?Integer.valueOf(CUR_NUM):0);
            aleve.setCrflag(CRFLAG);
            aleve.setValdate(StringUtils.isNotEmpty(VALDATE)?new Date(Long.valueOf(VALDATE)):new Date());
            aleve.setInpdate(StringUtils.isNotEmpty(INPDATE)?new Date(Long.valueOf(INPDATE)):new Date());
            aleve.setReldate(StringUtils.isNotEmpty(RELDATE)?new Date(Long.valueOf(RELDATE)):new Date());
            aleve.setInptime(StringUtils.isNotEmpty(INPTIME)?Integer.valueOf(INPTIME):0);
            aleve.setTranno(StringUtils.isNotEmpty(TRANNO)?Integer.valueOf(TRANNO):0);
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
            list.add(aleve);*/
            
            System.out.println("bank："+bank); 
            System.out.println("CARDNBR："+CARDNBR); 
            System.out.println("AMOUNT："+AMOUNT); 
            System.out.println("CUR_NUM："+CUR_NUM); 
            System.out.println("CRFLAG："+CRFLAG); 
            System.out.println("VALDATE："+VALDATE); 
            System.out.println("INPDATE："+INPDATE); 
            System.out.println("RELDATE："+RELDATE); 
            System.out.println("INPTIME："+INPTIME); 
            System.out.println("TRANNO："+TRANNO); 
            System.out.println("ORI_TRANNO："+ORI_TRANNO); 
            
            System.out.println("TRANSTYPE："+TRANSTYPE);
            System.out.println("DESLINE："+DESLINE);
            System.out.println("CURR_BAL："+CURR_BAL);
            System.out.println("FORCARDNBR："+FORCARDNBR);
            System.out.println("REVIND："+REVIND);
            System.out.println("ACCCHG："+ACCCHG);
            System.out.println("SEQNO："+SEQNO);
            System.out.println("ORI_NUM："+ORI_NUM);
            i++;
            System.out.println(line.length()+"**********************************************************************");
            if( i==20) break;
        }  
       
        br.close();
        return list;  
        
    }  
    public static ArrayList<EveLog> readFileEve(File fin) throws IOException {  
        FileInputStream fis = new FileInputStream(fin);  
        
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "utf-8"));  
       
        String line = null;  
        int i = 1;
        ArrayList<EveLog> list = new ArrayList<>();
        while ((line = br.readLine()) != null) { 
            
            String forcode = line.substring(0, 11).trim();//发送方标识码
            String seqno = line.substring(11, 17).trim();//系统跟踪号
            String cendt = line.substring(17, 27).trim();//交易传输时间
            String cardnbr = line.substring(27, 46).trim();//主账号
            String amount = line.substring(46, 58).trim();//交易金额
            String crflag = line.substring(58, 59).trim();//交易金额符号--小于零等于C；大于零等于D；
            String msgtype = line.substring(59, 63).trim();//消息类型--提现冲正交易是0420
            String proccode = line.substring(63, 69).trim();//交易类型码
            String orderno = line.substring(69, 109).trim();//订单号
            String tranno = line.substring(109, 115).trim();//内部交易流水号
            String reserved = line.substring(115, 134).trim();//内部保留域
            String revind = line.substring(134, 135).trim();//冲正、撤销标志 --1-已撤销/冲正空或0-正常交易
            String transtype = line.substring(135, 139).trim();//交易类型
            EveLog eve = new EveLog();
            eve.setForcode(forcode);
            eve.setSeqno(StringUtils.isNotEmpty(forcode)?Integer.valueOf(forcode):0);
            eve.setCendt(StringUtils.isNotEmpty(cendt)?Integer.valueOf(cendt):0);
            eve.setCardnbr(cardnbr);
            eve.setAmount(StringUtils.isNotEmpty(amount)?BigDecimal.valueOf(Double.valueOf(amount)/100):BigDecimal.valueOf(0.00));
            eve.setCrflag(crflag);
            eve.setMsgtype(StringUtils.isNotEmpty(amount)?Integer.valueOf(amount):0);
            eve.setProccode(StringUtils.isNotEmpty(proccode)?Integer.valueOf(proccode):0);
            eve.setOrderno(orderno);
            eve.setTranno(proccode);
            eve.setReserved(proccode);
            eve.setRevind(StringUtils.isNotEmpty(revind)?Integer.valueOf(revind):0);
            eve.setTranstype(StringUtils.isNotEmpty(transtype)?Integer.valueOf(transtype):0);
            System.out.println("forcode："+forcode); 
            System.out.println("seqno："+seqno); 
            System.out.println("cendt："+cendt); 
            System.out.println("cardnbr:"+cardnbr); 
            System.out.println("amount："+amount); 
            System.out.println("crflag："+crflag); 
            System.out.println("msgtype："+msgtype); 
            System.out.println("proccode："+proccode); 
            System.out.println("orderno："+orderno); 
            System.out.println("tranno："+tranno); 
            System.out.println("reserved："+reserved); 
            
            System.out.println("revind："+revind);
            System.out.println("transtype："+transtype);
            list.add(eve);
            i++;
            System.out.println(line.length()+"**********************************************************************");
            if( i==2) break;
        }  
       
        br.close();
        return list;  
        
    }  
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
    public static void main(String[] args) {
        File dir = new File("D:/test");  
        File fin;
        try {
            fin = new File(dir.getCanonicalPath() + File.separator + "0_3005-ALEVE0082-20171015-NEW");
            readFileAleve(fin); 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  

           
        
    }
}
