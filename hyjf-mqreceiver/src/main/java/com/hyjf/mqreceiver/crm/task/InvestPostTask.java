package com.hyjf.mqreceiver.crm.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.SpringContextHolder;
import com.hyjf.mqreceiver.crm.utils.CheckSignUtil;
import com.hyjf.mqreceiver.crm.utils.PropUtils;
import com.hyjf.mybatis.mapper.auto.BorrowMapper;
import com.hyjf.mybatis.mapper.auto.HjhPlanMapper;
import com.hyjf.mybatis.mapper.auto.UsersInfoMapper;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;

public class InvestPostTask<T> extends Thread {
    private static Logger logger = LoggerFactory.getLogger(InvestPostTask.class);

    private T obj = null;

    private final String CRM_INVESTMENTDETAIL_ACTOIN_URL = PropUtils.getSystem("crm.investmentdetails.url");

    public InvestPostTask(T obj) {
        this.obj = obj;
    }

    // 此处有坑，短时间大批量出借或放款操作遇到网络异常，会产生大量异常线程占用资源，线程池解决
    @Override
    public void run() {

        logger.info("crm出借接口调用开始....................");
        CloseableHttpResponse result = null;
        int status = 0;
        int i = 0;
        do {
            if (i > 0) {
                try {
                    // 网络异常重试3次，每次间隔0.3s
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            result = this.postInvestInfo(this.buildData().toJSONString());
            // 获取返回码，200为正常
            status = result.getStatusLine().getStatusCode();
            i++;

            // 业务异常处理
            if (status == HttpStatus.SC_OK /* && 业务异常判断 */)
                break;

            // 网络异常处理
            if (i == 4) {
            }
        } while (status != HttpStatus.SC_OK && i <= 3);
        logger.info("crm出借接口调用结束....................");
    }

    private CloseableHttpResponse postInvestInfo(String jsonStr) {
        logger.info("crmurl...................." + CRM_INVESTMENTDETAIL_ACTOIN_URL);
        logger.info("crmdata...................." + jsonStr);
        logger.debug("crm.investmentdetails.url=【{}】", CRM_INVESTMENTDETAIL_ACTOIN_URL);
        return this.postJson(CRM_INVESTMENTDETAIL_ACTOIN_URL, jsonStr);
    }

    /* post数据构造 所取数据为必要数据 */
    private JSONObject buildData() {
        JSONObject ret = new JSONObject();
        Map<String, Object> map = Maps.newHashMap();
        // 根据数据类型判断出借类型。直投类
        if (this.obj instanceof BorrowTender) {
            BorrowTender bt = (BorrowTender) obj;
            UsersInfo userInfo = this.getUserInfo(bt.getUserId());
            Borrow borrowInfo = getBorrowInfo(bt.getBorrowNid());
            String borrowStyle = borrowInfo.getBorrowStyle();

            map.put("idNum", userInfo.getIdcard());
            map.put("referrerIdCard", "");
            map.put("status", 1);
            map.put("borrowType", borrowInfo.getName());
            map.put("borrowNid", bt.getBorrowNid());
            map.put("investmentNid", bt.getNid());
            map.put("unit",
                    CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
                            || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                            || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)
                            || CustomConstants.BORROW_STYLE_END.equals(borrowStyle) ? 2 : 1);
            map.put("term", borrowInfo.getBorrowPeriod());
            map.put("account", bt.getAccount());
            map.put("addTime", bt.getAddtime());
            map.put("loanTime", getDate(bt.getLoanOrderDate()));
            map.put("productNo", 1001);
        }
        // 计划类出借
        else if (this.obj instanceof HjhAccede) {
            HjhAccede hj = (HjhAccede) obj;
            UsersInfo userInfo = this.getUserInfo(hj.getUserId());
            HjhPlan hjhPlan = getHjhPlanInfo(hj.getPlanNid());

            map.put("idNum", userInfo.getIdcard());
            map.put("referrerIdCard", "");
            map.put("status", 1);
            map.put("borrowType", hjhPlan.getPlanName());
            map.put("borrowNid", hj.getPlanNid());
            map.put("investmentNid", hj.getAccedeOrderId());
            map.put("unit", hjhPlan.getIsMonth() == 0 ? 1 : 2);
            map.put("term", hjhPlan.getLockPeriod());
            map.put("account", hj.getAccedeAccount());
            map.put("addTime", hj.getAddTime());
            map.put("loanTime", 0);
            map.put("productNo", 1002);

        }
        map.put("instCode", "10000001");

        String sign = CheckSignUtil.encryptByRSA(map, "10000001");
        ret.put("instCode", "10000001");
        ret.put("object", map);
        ret.put("sign", sign);
        return ret;
    }

    /**
     * 处理post请求.
     *
     * @param url 参数
     * @return json
     */
    public CloseableHttpResponse postJson(String url, String jsonStr) {

        // 实例化httpClient
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 实例化post方法
        HttpPost httpPost = new HttpPost(url);

        // 结果
        CloseableHttpResponse response = null;
        try {
            // 提交的参数
            StringEntity uefEntity = new StringEntity(jsonStr, "utf-8");
            uefEntity.setContentEncoding("UTF-8");
            uefEntity.setContentType("application/json");
            // 将参数给post方法
            httpPost.setEntity(uefEntity);
            // 执行post方法
            response = httpclient.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    // 格式化时间格式
    private int getDate(String fdate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        int idate = 0;
        try {
            Date date = format.parse(fdate);

            idate = (int) (date.getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idate;
    }

    // 取得用户详细信息
    private UsersInfo getUserInfo(Integer userId) {
        UsersInfoMapper usersInfoMapper = SpringContextHolder.getBean(UsersInfoMapper.class);
        UsersInfoExample userInfoExample = new UsersInfoExample();
        userInfoExample.createCriteria().andUserIdEqualTo(userId);
        return usersInfoMapper.selectByExample(userInfoExample).get(0);
    }

    // 取得标的信息
    private Borrow getBorrowInfo(String borrowNid) {
        BorrowMapper borrowMapper = SpringContextHolder.getBean(BorrowMapper.class);
        BorrowExample borrowExample = new BorrowExample();
        borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
        return borrowMapper.selectByExample(borrowExample).get(0);
    }

    // 取得计划信息
    private HjhPlan getHjhPlanInfo(String planNid) {
        HjhPlanMapper hjhPlanMapper = SpringContextHolder.getBean(HjhPlanMapper.class);
        HjhPlanExample hjhPlanExample = new HjhPlanExample();
        hjhPlanExample.createCriteria().andPlanNidEqualTo(planNid);
        return hjhPlanMapper.selectByExample(hjhPlanExample).get(0);
    }

}
