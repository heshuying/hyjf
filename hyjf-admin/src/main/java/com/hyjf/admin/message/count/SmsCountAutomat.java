/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.message.count;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.message.log.SmsLogService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.customize.SmsCountCustomize;
import com.hyjf.mybatis.model.customize.SmsLogCustomize;
import com.hyjf.mybatis.model.customize.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;

/**
 * 短信统计  临时类
 * @author yinhui
 */
@Controller
@RequestMapping(value = "/smsauto")
public class SmsCountAutomat extends BaseController {

    @Autowired
    private SmsLogService logService;

    @Autowired
    private SmsCountService smsCountServiceImpl;


    @RequestMapping(value = "/auto", method = RequestMethod.POST)
    @ResponseBody
    public void smsAuto(@RequestParam(value = "smsStats") int smsStats) {
        LogUtil.startLog(SmsCountAutomat.class.toString(), "smsAuto");
         String smsCountSize = RedisUtils.get("addSmsCountRedisKey");
        long startTime = System.currentTimeMillis();   //获取开始时间
//        if(StringUtils.isNotEmpty(smsCountSize)){
//            RedisUtils.del("addSmsCountRedisKey");
//        }

        if (smsStats <= 0) {
            smsStats = 0;
        }

        //先将CRM中的企业用户对应的部门都查询出来
        List<SmsCountCustomize> listIdAndDepartmentName = smsCountServiceImpl.getuserIdAnddepartmentName();
        HashSet<Integer> setUserId = new HashSet<>();
        List<Integer> listInt = new ArrayList<>();
        for (SmsCountCustomize sms : listIdAndDepartmentName) {
            setUserId.add(sms.getId());//存放的是用户ID ---  这是用来查询是否包含用户ID的
            listInt.add(sms.getId());//存放的是用户ID   ---  这是用来查询用户ID在数组中对的位置
        }

        SmsLogCustomize smsc = new SmsLogCustomize();
        smsc.setStatus(0);
        //查询当前短信日志的总数
        Integer count = logService.queryLogCount(smsc);
        count = count - smsStats;

        Integer loopCount = (count + 5000 - 1) / 5000;

        for (int i = 0; i < loopCount; i++) {
            try {
                //每次查询以前条记录
                SmsLogCustomize smsLogCustomize = new SmsLogCustomize();
                smsLogCustomize.setStatus(0);
                smsLogCustomize.setInitStar(String.valueOf(i * 5000 + smsStats));//ID开始
                smsLogCustomize.setInitEnd(String.valueOf((i + 1) * 5000 + smsStats));//ID结束
                smsLogCustomize.setRowrank("asc");
                //根据ID查询，  1000<id<2000
                List<SmsLogCustomize> listSmsLog = logService.queryLog(smsLogCustomize);

                if (CollectionUtils.isEmpty(listSmsLog)) {
                    continue;
                }

                //批次通过手机号码查询用户ID和推荐人ID，将 key:手机号码,value:UserVO
                Map<String, UserVO> mapUserVo = getUserByMobile(listSmsLog);

                Map<String, Map<String, String>> departmentMap = new LinkedHashMap<>();
                for (SmsLogCustomize dto : listSmsLog) {

                    try {

                        if (StringUtils.isEmpty(dto.getType()) || StringUtils.isEmpty(dto.getMobile())) {
                            continue;
                        }

                        String[] strSms = null;
                        if (dto.getMobile().contains(",")) {
                            strSms = dto.getMobile().split(",");
                        } else {
                            strSms = new String[]{dto.getMobile()};
                        }

                        String postString = GetDate.dataformat(dto.getPostString(), "yyyy-MM-dd");

                        //拆分每个手机号码
                        for (String mobile : strSms) {
                            String departmentId = null;
                            String departmentName = null;
                            Map<String, String> mobileMap = new HashMap<String, String>();

                            UserVO users = mapUserVo.get(mobile);
                            if (users == null) {
                                continue;
                            }

                            boolean flag = setUserId.contains(users.getUserId());
                            if (!flag) {
                                //再查询推荐人是否是分公司的
                                if (users.getReferrer() != null) {
                                    flag = setUserId.contains(users.getReferrer());
                                }

                            }

                            if (flag) {
                                Integer idIndex = listInt.indexOf(users.getUserId());
                                if(idIndex > 0){
//                                    LogUtil.debugLog(SmsCountAutomat.class.getName(),"size="+listIdAndDepartmentName.size()+",idIndex="+idIndex);
                                    departmentId = String.valueOf(listIdAndDepartmentName.get(idIndex).getDepartmentId());
                                    departmentName = listIdAndDepartmentName.get(idIndex).getDepartmentName();
                                }
                            }

//                            Map<String,String> mobileMap = smsCountServiceImpl.getDeptByMobile(mobile);

                            //短信数量
                            int smscounts = this.computeSms(dto.getContent());

                            //如果当前手机号码不归属某个分公司，就放入到其他里面
                            if (StringUtils.isEmpty(departmentId) && StringUtils.isEmpty(departmentName)) {

                                departmentId = "0";//0 -代表 其他
                                departmentName = "其他";//
                            }

                            mobileMap.put("smsCount", String.valueOf(smscounts));
                            mobileMap.put("deptId", departmentId);
                            mobileMap.put("deptName", departmentName);
                            mobileMap.put("posttime", postString);

                            if (departmentMap.containsKey(postString + departmentId)) {
                                Map<String, String> summobileMap = departmentMap.get(postString + departmentId);
                                int counts = Integer.valueOf(summobileMap.get("smsCount")) + smscounts;
                                summobileMap.put("smsCount", String.valueOf(counts));
                                departmentMap.put(postString + departmentId, summobileMap);
                            } else {
                                departmentMap.put(postString + departmentId, mobileMap);
                            }
                        }

                    } catch (Exception e) {
                        LogUtil.errorLog(SmsCountController.class.toString(), "初始化数据失败", e);
                    }

                }

                //保存短信统计记录
                List<SmsLogCustomize> insertListsms = new ArrayList<>();

                Iterator iter = departmentMap.entrySet().iterator();
                while (iter.hasNext()) {
                    SmsLogCustomize smsLogCustomize1 = new SmsLogCustomize();
                    Map.Entry entry = (Map.Entry) iter.next();
                    Map<String, String> mapEn = (Map<String, String>) entry.getValue();
                    smsLogCustomize1.setDeptId(String.valueOf(mapEn.get("deptId")));
                    smsLogCustomize1.setDeptName(mapEn.get("deptName"));
                    smsLogCustomize1.setContentSize(mapEn.get("smsCount"));
                    smsLogCustomize1.setPostString(GetDate.dataformat(mapEn.get("posttime"), "yyyy-MM-dd"));
                    insertListsms.add(smsLogCustomize1);
                }

                if (CollectionUtils.isEmpty(insertListsms)) {
                    continue;
                }

                //部分修改，部分添加
                List<SmsCountCustomize> updateSmsCount = new ArrayList<>();
                List<SmsLogCustomize> insertListsmsTwo = new ArrayList<SmsLogCustomize>
                        (Arrays.asList(new SmsLogCustomize[insertListsms.size()]));
                Collections.copy(insertListsmsTwo, insertListsms);

                if (smsStats > 0) {
                    //List倒序查询
                    Collections.reverse(insertListsms);

                    for (SmsLogCustomize sms : insertListsms) {
                        SmsCountCustomize smsCount = new SmsCountCustomize();
                        smsCount.setDepartmentId(Integer.valueOf(sms.getDeptId()));
                        smsCount.setPosttime(sms.getPostString());
                        List<SmsCountCustomize> lms = smsCountServiceImpl.querySms(smsCount);
                        if (!CollectionUtils.isEmpty(lms)) {
                            smsCount = lms.get(0);
                            smsCount.setSmsNumber(smsCount.getSmsNumber() + Integer.valueOf(sms.getContentSize()));
                            updateSmsCount.add(smsCount);
                            //从添加中的List移除需要修改的数据
                            insertListsmsTwo.remove(sms);
                        } else {
                            break;
                        }

                    }
                    //将数据归0
                    smsStats = 0;
                }

                smsCountServiceImpl.insertBatchSms(insertListsmsTwo);
                if (smsStats > 0) {
                    smsCountServiceImpl.updateByPrimaryKeySelective(updateSmsCount);
                }

                //将目前的最大ID保存到Redis中
                String smsCountSizeAdd = RedisUtils.get("addSmsCountRedisKey");
                Integer listAdd = 5000;
                if (StringUtils.isNotEmpty(smsCountSizeAdd)) {
                    listAdd = listAdd + Integer.valueOf(smsCountSizeAdd);
                }
                RedisUtils.set("addSmsCountRedisKey", String.valueOf(listAdd), 259200);

            } catch (Exception e) {
                LogUtil.errorLog(SmsCountController.class.toString(), "初始化大循环数据失败", e);
            }
        }

        long endTime = System.currentTimeMillis(); //获取结束时间
        LogUtil.endLog(SmsCountAutomat.class.toString(), "smsAuto" + "程序运行时间： " + (endTime - startTime) + "ms");
    }

    /**
     * 拆分短信计数，（70个字符以内计一条，之后每增加67字符增加一条）
     * @param content
     * @return
     */
    public int computeSms(String content) {
        int  contentSize  = 0;//按照字数计算短信条数
        int size = content.length();
        if(size>70){
            //算法计算字数规则  小于等于70算一条 ，大于70后每67字加一条
            if((size-70)%67!=0){
                contentSize=contentSize+1;
            }
            contentSize =contentSize+ 1+(size-70)/67;
        }else {
            contentSize = 1;
        }

        return contentSize;
    }

    /**
     * 批次通过手机号码查询用户ID和推荐人ID，将 key:手机号码,value:UserVO
     * @param listSmsLog
     * @return
     */
    public Map<String,UserVO> getUserByMobile(List<SmsLogCustomize> listSmsLog){
        List<String> listMobile = new ArrayList<>();
        Map<String, UserVO> mapUserVo = new HashMap<>();
        //批量通过手机号码查询用户
        for (SmsLogCustomize dtomobile : listSmsLog) {
            if (StringUtils.isEmpty(dtomobile.getType()) || StringUtils.isEmpty(dtomobile.getMobile())) {
                continue;
            }

            String[] strSmsMobile = null;
            if (dtomobile.getMobile().contains(",")) {
                strSmsMobile = dtomobile.getMobile().split(",");
            } else {
                strSmsMobile = new String[]{dtomobile.getMobile()};
            }
            for (String mobile : strSmsMobile) {
                listMobile.add(mobile);
            }

        }
        List<UserVO> listUserVo = smsCountServiceImpl.getUsersVo(listMobile);
        for (UserVO vo : listUserVo) {
            mapUserVo.put(vo.getMobile(), vo);
        }

        return mapUserVo;
    }

    @RequestMapping(value = "/getRedisSms", method = RequestMethod.POST)
    @ResponseBody
    public String getRedisSms(@RequestParam(value = "smsStats") int smsStats) {

        String smsCountSize = RedisUtils.get("addSmsCountRedisKey");
        if (smsStats == 1) {
            RedisUtils.del("addSmsCountRedisKey");
        }
        if (StringUtils.isEmpty(smsCountSize)) {
            smsCountSize = "这是空的";
        }
        return smsCountSize;
    }

}
