package com.hyjf.admin.coupon.user;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CreateUUID;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponConfigExample;
import com.hyjf.mybatis.model.auto.CouponOperationHistoryWithBLOBs;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.coupon.CouponUserCustomize;
import com.hyjf.soa.apiweb.CommonSoaUtils;

/**
 * 
 * service接口实现类
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月15日
 * @see 下午3:38:28
 */
@Service
public class CouponUserServiceImpl extends BaseServiceImpl implements CouponUserService {
	Logger _log = LoggerFactory.getLogger(CouponUserServiceImpl.class);

    @Autowired
    @Qualifier("appMsProcesser")
    private MessageProcesser appMsProcesser;
    
    /**
     * 获取用户的优惠券列表
     * @return
     */
    @Override
    public List<CouponUserCustomize> getRecordList(Map paraMap) {
        return couponUserCustomizeMapper.selectCouponUserList(paraMap);
    }

    /**
     * 获得记录数
     * @param CouponConfigCustomize
     * @return
     */
    @Override
    public Integer countRecord(Map paraMap) {
        return couponUserCustomizeMapper.countCouponUser(paraMap);
    }

    /**
     * 
     * 增加一条优惠券给用户
     * @author hsy
     * @param couponUserBean
     */
    @Override
    public void insertRecord(CouponUserBean couponUserBean) {
        CouponUser couponUser = new CouponUser();
        BeanUtils.copyProperties(couponUserBean, couponUser);
        //根据用户名查询用户id
        UsersExample example = new UsersExample();
        UsersExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(couponUserBean.getUsername());
        List<Users> userList = usersMapper.selectByExample(example);
        if(userList == null || userList.isEmpty()){
            LogUtil.errorLog(this.getClass().getName(), "insertRecord", "用户名：" + couponUserBean.getUsername() + " 不存在", null);
            return;
        }
        Users user = userList.get(0);
        
        UsersInfo userInfo = this.getUsersInfoByUserId(user.getUserId());
        
        String channelName = this.getChannelNameByUserId(user.getUserId());
        
        //根据优惠券编码查询优惠券
        CouponConfigExample emConfig = new CouponConfigExample();
        CouponConfigExample.Criteria caConfig = emConfig.createCriteria();
        caConfig.andCouponCodeEqualTo(couponUserBean.getCouponCode());
        List<CouponConfig> configList = couponConfigMapper.selectByExample(emConfig);
        if(configList == null || configList.isEmpty()){
            return;
        }
        CouponConfig config = configList.get(0);
        
        if(config.getExpirationType() == 1){ //截止日
            couponUser.setEndTime(config.getExpirationDate());
        }else if(config.getExpirationType() == 2){  //时长（月）
            Date endDate = GetDate.countDate(GetDate.getDate(), 2, config.getExpirationLength());
            couponUser.setEndTime((int)(endDate.getTime()/1000));
        }else if(config.getExpirationType() == 3){  //时长（天）
            Date endDate = GetDate.countDate(GetDate.getDate(), 5, config.getExpirationLengthDay());
            couponUser.setEndTime((int)(endDate.getTime()/1000));
        }
        couponUser.setUserId(user.getUserId());
        couponUser.setCouponUserCode(GetCode.getCouponUserCode(config.getCouponType()));
        couponUser.setAddUser(ShiroUtil.getLoginUserId());
        couponUser.setAddTime(GetDate.getNowTime10());
        couponUser.setUpdateUser(ShiroUtil.getLoginUserId());
        couponUser.setUpdateTime(GetDate.getNowTime10());
        couponUser.setDelFlag(CustomConstants.FALG_NOR);
        couponUser.setUsedFlag(CustomConstants.USER_COUPON_STATUS_WAITING_PUBLISH);
        couponUser.setReadFlag(CustomConstants.USER_COUPON_READ_FLAG_NO);
        if(couponUser.getActivityId() == null){
        	couponUser.setCouponSource(CustomConstants.USER_COUPON_SOURCE_MANUAL);
        }else{
        	couponUser.setCouponSource(CustomConstants.USER_COUPON_SOURCE_ACTIVE);
        }
        couponUser.setAttribute(userInfo.getAttribute());
        couponUser.setChannel(channelName);
        
        couponUserMapper.insertSelective(couponUser);
        this.operationLog(couponUser, CustomConstants.OPERATION_CODE_INSERT);
    }
    
    /**
     * 
     * 根据id删除一条记录
     * @author hsy
     * @param recordId
     */
    @Override
    public void deleteRecord(Integer recordId, String remark) {
        CouponUser couponUser = new CouponUser();
        couponUser.setId(recordId);
        couponUser.setDeleteContent(remark);
        couponUser.setDelFlag(Integer.parseInt(CustomConstants.FLAG_DELETE));
        this.operationLog(couponUser, CustomConstants.OPERATION_CODE_DELETE);
        couponUserMapper.updateByPrimaryKeySelective(couponUser);
    }
    
    /**
     * 
     * 根据优惠券编码查询优惠券详情
     * @author hsy
     * @param couponCode
     * @return
     */
    @Override
    public CouponConfig selectConfigByCode(String couponCode){
        //根据优惠券编码查询优惠券
        CouponConfigExample example = new CouponConfigExample();
        CouponConfigExample.Criteria caConfig = example.createCriteria();
        caConfig.andCouponCodeEqualTo(couponCode);
        List<CouponConfig> configs = couponConfigMapper.selectByExample(example);
        if(configs == null || configs.isEmpty()){
            return null;
        }
        CouponConfig config =  configs.get(0);
        if(config.getExpirationType() ==1 ){ //截止日
            config.setContent(GetDate.formatDate(Long.parseLong((config.getExpirationDate() + "000"))));
        }else if(config.getExpirationType() == 2){  //时长（月）
            Date date = GetDate.countDate(GetDate.getDate(), 2, config.getExpirationLength());
            config.setContent(GetDate.formatDate(date));
        }else if(config.getExpirationType() == 3){  //时长（天）
            Date date = GetDate.countDate(GetDate.getDate(), 5, config.getExpirationLengthDay());
            config.setContent(GetDate.formatDate(date));
        }
        return config;
        
    }
    
    /**
     * 操作履历
     * @param couponTo
     * @param operationCode
     */
    private void operationLog(CouponUser couponTo,int operationCode) {
        CouponOperationHistoryWithBLOBs co = new CouponOperationHistoryWithBLOBs();
        // 编号
        co.setUuid(CreateUUID.getUUID());
        // 优惠券编号
        co.setCouponCode(couponTo.getCouponCode());
        // 操作编号
        co.setOperationCode(operationCode);
        // 取得上传更新前的数据
        CouponUser recordFrom = couponUserMapper.selectByPrimaryKey(couponTo.getId());
        // 更新，删除的场合
        if(operationCode != CustomConstants.OPERATION_CODE_INSERT){
            // 更新前的json数据
            co.setOperationContentFrom(JSONObject.toJSONString(recordFrom, true));
        }
        // 更新后的json数据
        co.setOperationContentTo(JSONObject.toJSONString(couponTo, true));
        
        // 操作者
        co.setAddUser(ShiroUtil.getLoginUserId());
        // 操作时间
        co.setAddTime(GetDate.getNowTime10());
        couponOperationHistoryMapper.insertSelective(co);

    }
    @Override
    public CouponUser selectCouponUserById(String couponUserId) {
        return couponUserMapper.selectByPrimaryKey(Integer.parseInt(couponUserId));
    }

    @Override
    public void auditRecord(CouponUserBean form, CouponConfig config, Integer couponUserId) {
        Integer nowDate = GetDate.getNowTime10();
        String userId = ShiroUtil.getLoginUserId();
        CouponUser record=new CouponUser();
        record.setId(form.getId());
        record.setAuditContent(form.getDescription());
        if("0".equals(form.getAuditStatus())){
            record.setUsedFlag(CustomConstants.USER_COUPON_STATUS_UNUSED); 
            
            //推送通知消息
            Map<String, String> param = new HashMap<String, String>();
            param.put("val_number", String.valueOf(1));
            param.put("val_coupon_type", config.getCouponType() == 1 ? "体验金" : config.getCouponType() == 2 ? "加息券" : config.getCouponType() == 3 ? "代金券" : "");
            AppMsMessage appMsMessage = new AppMsMessage(couponUserId, param, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_COUPON_SUCCESS);
            appMsProcesser.gather(appMsMessage);
        }else{
            record.setUsedFlag(CustomConstants.USER_COUPON_STATUS_NOCHECKED); 
        }
        record.setUpdateUser(userId);
        record.setUpdateTime(nowDate);
        record.setAuditUser(userId);
        record.setAuditTime(nowDate);
        
        couponUserMapper.updateByPrimaryKeySelective(record);
    }

	/**
	 * 手动批量发券上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());

		Iterator<String> itr = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;
		JSONObject retResult = new JSONObject();
		
		List<BatchSubUserCouponBean> subBeans = new ArrayList<BatchSubUserCouponBean>();

		while (itr.hasNext()) {
			multipartFile = multipartRequest.getFile(itr.next());
			InputStream is = multipartFile.getInputStream();
			String encode = getEncoder(is);
			_log.info("优惠券批量导入编码格式：" + encode);
			Reader in = new InputStreamReader(is, encode);
			Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
			
			for (CSVRecord record : records) {
				
				// 格式小于3,跳过
				if(record.size() < 3){
					continue;
				}
				BatchSubUserCouponBean subBean = new BatchSubUserCouponBean();
//				String userId = record.get(0);
				String userName = record.get(0);
				String activeId = record.get(1);
				String couponcode  = record.get(2);
			   _log.info("优惠券批量导入username: " + userName);
			    
                List<String> copuncodes = Arrays.asList(couponcode.split(","));
				
				if(copuncodes.size() <= 0 || StringUtils.isBlank(userName)){
					continue;
				}
				
				subBean.setUserName(userName);
				subBean.setActivityId(activeId);
				subBean.setCouponCode(copuncodes);
				
				subBeans.add(subBean);
			}
			
			//关闭输入流
			in.close();
			is.close();
			
			// 只处理一个文件
			break;
			
		}
		
		if (subBeans.size() == 0) {
			 retResult.put("status", 0);
	         retResult.put("totalcouponCount", 0);
	         retResult.put("couponCount", 0);
			return JSONObject.toJSONString(retResult, true);
		}else{
			// 访问API
			
			Map<String, String> params = new HashMap<String, String>();
	        // 用户id
			String userId = ShiroUtil.getLoginUserId();
	        params.put("usercoupons", JSON.toJSONString(subBeans));
	        
	        // 请求路径
	        JSONObject result = CommonSoaUtils.getBatchCoupons(userId,params);
			return JSONObject.toJSONString(result, true);
		}
	}
	
	public static String getEncoder(InputStream in) throws Exception{
		in.mark(100000);
        BufferedInputStream bin = new BufferedInputStream(in);
        int p = (bin.read() << 8) + bin.read();
        String code = null;
        in.reset();

        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                in.skip(3);
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                in.skip(3);
                break;
            default:
                code = "GBK";
        }
        return code;
    }

}





