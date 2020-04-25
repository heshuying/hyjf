package com.hyjf.admin.coupon.checklist;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.coupon.user.BatchSubUserCouponBean;
import com.hyjf.admin.excel.ReadExcel;
import com.hyjf.bank.service.coupon.UserCouponServiceImpl;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.mapper.auto.CouponCheckMapper;
import com.hyjf.mybatis.mapper.auto.CouponConfigMapper;
import com.hyjf.mybatis.mapper.auto.CouponUserMapper;
import com.hyjf.mybatis.mapper.auto.UsersInfoMapper;
import com.hyjf.mybatis.mapper.auto.UsersMapper;
import com.hyjf.mybatis.mapper.customize.ChannelCustomizeMapper;
import com.hyjf.mybatis.mapper.customize.coupon.CouponConfigCustomizeMapper;
import com.hyjf.mybatis.model.auto.CouponCheck;
import com.hyjf.mybatis.model.auto.CouponCheckExample;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponConfigExample;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;

/**
 * @author lisheng
 * @version CouponCheckServiceImpl, v0.1 2018/6/6 16:30
 */
@Service
public class CouponCheckServiceImpl implements CouponCheckService {
	private static final String THIS_CLASS = UserCouponServiceImpl.class.getName();
	Logger _log = LoggerFactory.getLogger(CouponCheckServiceImpl.class);
    @Autowired
    CouponCheckMapper couponCheckMapper;
    @Autowired
    protected CouponUserMapper couponUserMapper;
    @Autowired
    protected CouponConfigMapper couponConfigMapper;
    @Autowired
    protected UsersMapper usersMapper;
    @Autowired
    protected UsersInfoMapper usersInfoMapper;
    @Autowired
	protected ChannelCustomizeMapper channelCustomizeMapper;
    @Autowired
    protected CouponConfigCustomizeMapper couponConfigCustomizeMapper;


    /**
     * 根据 条件查询表中记录数
     * @param form
     * @return
     */
    public int countCouponCheck(CouponCheckBean form) {
        Integer start = 0;
        Integer end = 0;
        CouponCheckExample couponCheckExample = new CouponCheckExample();
        CouponCheckExample.Criteria criteria = couponCheckExample.createCriteria();
        criteria.andDeFlagEqualTo(0);//未删除

        String timeStartAddSrch = form.getTimeStartAddSrch();
        String timeEndAddSrch = form.getTimeEndAddSrch();
        String status = form.getStatus();
        if (StringUtils.isNotBlank(timeStartAddSrch) && StringUtils.isNotBlank(timeEndAddSrch)) {
            criteria.andCreateTimeBetween( GetDate.getDayStart10(timeStartAddSrch), GetDate.getDayEnd10(timeEndAddSrch));
        }

        if (StringUtils.isNotBlank(status)) {
            criteria.andStatusEqualTo(Integer.valueOf(status));
        }
        return couponCheckMapper.countByExample(couponCheckExample);
    }

    /**
     * 列表查询
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     */
    @Override
    public List<CouponCheck> searchCouponCheck(CouponCheckBean form, int limitStart, int limitEnd) {
        CouponCheckExample couponCheckExample = new CouponCheckExample();
        couponCheckExample.setOrderByClause("create_time desc");
        CouponCheckExample.Criteria criteria = couponCheckExample.createCriteria();
        criteria.andDeFlagEqualTo(0);//未删除
        String timeStartAddSrch = form.getTimeStartAddSrch();
        String timeEndAddSrch = form.getTimeEndAddSrch();

        String status = form.getStatus();
        if (StringUtils.isNotBlank(timeStartAddSrch) && StringUtils.isNotBlank(timeEndAddSrch)) {
            criteria.andCreateTimeBetween( GetDate.getDayStart10(timeStartAddSrch), GetDate.getDayEnd10(timeEndAddSrch));
        }

        if (StringUtils.isNotBlank(status)) {
            criteria.andStatusEqualTo(Integer.valueOf(status));
        }
        if (limitStart != -1) {
            couponCheckExample.setLimitStart(limitStart);
            couponCheckExample.setLimitEnd(limitEnd);
        }

        List<CouponCheck> couponChecks = couponCheckMapper.selectByExample(couponCheckExample);
        return couponCheckMapper.selectByExample(couponCheckExample);
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteMessage(String id) {
        CouponCheck couponCheck = new CouponCheck();
        couponCheck.setDeFlag(1);
        CouponCheckExample couponCheckExample = new CouponCheckExample();
        CouponCheckExample.Criteria criteria = couponCheckExample.createCriteria();
        criteria.andIdEqualTo(Integer.valueOf(id));
        return couponCheckMapper.updateByExampleSelective(couponCheck, couponCheckExample) > 0 ? true : false;
    }

    /**
     * 文件上传
     *
     * @param request
     * @return
     */
    @Override
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) {
        JSONObject retResult = new JSONObject();
        String errorMessage="";
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart(request);
        String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.path"));
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String today = format.format(date);

        String logoRealPathDir = filePhysicalPath+today;
        File logoSaveFile = new File(logoRealPathDir);
        if (!logoSaveFile.exists()) {
            logoSaveFile.mkdirs();
        }
        Iterator<String> itr = multipartRequest.getFileNames();
        MultipartFile multipartFile = null;
        while (itr.hasNext()) {
            multipartFile = multipartRequest.getFile(itr.next());
            String fileRealName = String.valueOf(new Date().getTime() / 1000);
            Integer createTime = Integer.valueOf(fileRealName);
            String originalFilename = multipartFile.getOriginalFilename();
           String suffix = UploadFileUtils.getSuffix(multipartFile.getOriginalFilename());
           if(StringUtils.equals(suffix,".xls") || StringUtils.equals(suffix,".xlsx")){
               fileRealName = fileRealName + suffix;
               try {
                   errorMessage = UploadFileUtils.upload4Stream(fileRealName, logoRealPathDir, multipartFile.getInputStream(), 5000000L);
               } catch (Exception e) {
                   e.printStackTrace();
               }
               if (StringUtils.equals("上传文件成功！", errorMessage)) {
                   CouponCheck couponCheck = new CouponCheck();
                   couponCheck.setFileName(originalFilename);
                   couponCheck.setCreateTime(createTime);
                   couponCheck.setFilePath(logoRealPathDir +"/"+ fileRealName);
                   couponCheck.setDeFlag(0);
                   couponCheck.setStatus(1);
                   boolean insert = couponCheckMapper.insert(couponCheck)>0?true:false;
                   if(insert){
                       retResult.put("flag", errorMessage);
                   }else{
                       retResult.put("flag", "插入数据异常失败");
                   }
               }
           }else{
               retResult.put("flag", "上传失败，请上传Excel文件");
           }


        }

        return JSONObject.toJSONString(retResult, true);
    }

    /**
     * 下载
     * @param id
     * @param response
     */
    public void download(String id, HttpServletResponse response) {
        CouponCheck couponCheck = couponCheckMapper.selectByPrimaryKey(Integer.valueOf(id));
        String fileP="";
        String fileN="";
        if (couponCheck!=null){
            fileP = couponCheck.getFilePath();
            fileN = couponCheck.getFileName();
        }
        try {
            response.setHeader("content-disposition",
                    "attachment;filename=" + URLEncoder.encode(fileN, "utf-8"));

            FileInputStream in = new FileInputStream(fileP);
            // 创建输出流
            OutputStream out = response.getOutputStream();
            // 创建缓冲区
            byte buffer[] = new byte[1024];
            int len = 0;
            // 循环将输入流中的内容读取到缓冲区中
            while ((len = in.read(buffer)) > 0) {
                // 输出缓冲区内容到浏览器，实现文件下载
                out.write(buffer, 0, len);
            }
            // 关闭文件流
            in.close();
            // 关闭输出流
            out.close();
        } catch (Exception e) {

        }
    }


    /**
     * 批量审核优惠券
     *
     * @param
     * @return
     * @throws Exception
     */
    public boolean batchCheck(String path, HttpServletResponse response) throws Exception {
        try {
        	String[] split = path.split(",");
        	String filePath = split[1];
        	
        	Map<String, String> nameMaps = new HashMap<>();
        	nameMaps.put("couponCode", "couponCode");
        	nameMaps.put("activityId", "activityId");
        	nameMaps.put("userName", "userName");
        	ReadExcel readExcel = new ReadExcel();
        	List<JSONObject> list = new ArrayList<>();
        	try {
        		list = readExcel.readExcel(filePath,nameMaps);
        	} catch (IOException e) {
        		_log.error("批量发送优惠券，解析Excel："+filePath+"失败！",e);
        		return false;
        	}
        	final List<JSONObject> lists = list;
        	Thread t = new Thread(new Runnable(){  
                public void run(){  
               // run方法具体重写
            		int totalcouponCount = 0;
                    int succouponCount = 0;
                    
                    // 优惠券来源1：手动发放，2：活动发放，3：vip礼包
                    int couponSource = 2;
            		_log.info("批量发放优惠券开始： 预计" + lists.size() + " 张");
            		for (JSONObject jsonObject : lists) {
            			List<String> copuncodes;
                        String userName = jsonObject.getString("userName");
                        String activeId = jsonObject.getString("activityId");
                        String couponcode = jsonObject.getString("couponCode");
                        copuncodes = Arrays.asList(couponcode.split(","));
                        if (copuncodes.size() <= 0 || org.apache.commons.lang.StringUtils.isBlank(userName)) {
                            continue;
                        }
               		 Integer activityId = null;
               		 if(StringUtils.isNotBlank(activeId)){
               			 activityId = Integer.parseInt(activeId.trim());
               		 }
//                        String userId = userCouponBean.getUserId();
                        _log.info("批量发放优惠券当前用户名：" + userName);
                        if(StringUtils.isBlank(userName)){
                       	 continue;
                        }
                        batchInsertUserCoupon(userName, copuncodes, totalcouponCount, succouponCount, activityId, couponSource);
                        
            		}
                }});  
            t.start();  
            
        	
		} catch (Exception e) {
			_log.error("优惠券发放异常",e);
			return false;
		}
		return true;
		
    }
    
    public boolean batchInsertUserCoupon(String userName,List<String> copuncodes,int totalcouponCount,int succouponCount,Integer activityId,int couponSource){
    	Users user = this.getUserByUserName(userName);
        _log.info("批量发放优惠券User：" + user);
        if(user == null){
       	 return false;
        }
        if(copuncodes ==null || copuncodes.isEmpty()){
       	 return false;
        }
        
        totalcouponCount = totalcouponCount+copuncodes.size();
       // 发放优惠券
       int couponCount = 0;
		try {
			couponCount = this.sendConponAction(copuncodes, String.valueOf(user.getUserId()), activityId, couponSource);
		} catch (Exception e) {
			_log.error("用户："+userName + "发送优惠券失败！",e);
			e.printStackTrace();
		}
       succouponCount = succouponCount + couponCount;
       _log.info(user.getUserId()+ " 发放优惠券：" + couponCount + " 张");
       return true;
    }
    
    
    private synchronized int sendConponAction(List<String> couponCodeList, String userId, Integer activityId,
            int couponSource) throws Exception {
    	
    	// sendflg设置1跳过活动id不设置的逻辑
    	return sendUserConponAction(couponCodeList, userId, 1, activityId, couponSource,"上传csv文件，批量发券");
    }
    
    public int sendUserConponAction(List<String> couponCodeList, String userId, Integer sendFlg, Integer activityId,
            Integer couponSource, String content) throws Exception {
            _log.info("用户："+userId+",执行发券逻辑开始  " + GetDate.dateToString(new Date()));
            String methodName = "sendConponAction";
            int nowTime = GetDate.getNowTime10();
            // String couponGroupCode = CreateUUID.createUUID();
            
            UsersInfo userInfo = this.getUsersInfoByUserId(Integer.parseInt(userId));
            if(userInfo == null){
            	return 0;
            }
            
            String channelName = this.getChannelNameByUserId(Integer.parseInt(userId));
            
            int couponCount = 0;
            if (couponCodeList != null && couponCodeList.size() > 0) {
                for (String couponCode : couponCodeList) {
                    // 如果优惠券的发行数量已大于等于配置的发行数量，则不在发放该类别优惠券
                    if (!this.checkSendNum(couponCode)) {
                        LogUtil.infoLog(THIS_CLASS, methodName, "优惠券发行数量超出上限，不再发放！");
                        continue;
                    }
                    CouponUser couponUser = new CouponUser();
                    couponUser.setCouponCode(couponCode);
                    if (StringUtils.contains(couponCode, "PT")) {
                        // 体验金编号
                        couponUser.setCouponUserCode(GetCode.getCouponUserCode(1));
                    } else if (StringUtils.contains(couponCode, "PJ")) {
                        // 加息券编号
                        couponUser.setCouponUserCode(GetCode.getCouponUserCode(2));
                    } else if (StringUtils.contains(couponCode, "PD")) {
                        // 代金券编号
                        couponUser.setCouponUserCode(GetCode.getCouponUserCode(3));
                    }
                    // 优惠券组编号
                    // couponUser.setCouponGroupCode(couponGroupCode);
                    couponUser.setUserId(Integer.parseInt(userId));
                    if (Validator.isNotNull(sendFlg) && sendFlg != CouponCheckDefine.NUM_TWO && Validator.isNotNull(activityId)) {
                        // 购买vip与活动无关
                        couponUser.setActivityId(activityId);
                    }
                    couponUser.setUsedFlag(CustomConstants.USER_COUPON_STATUS_UNUSED);

                    // 根据优惠券编码查询优惠券
                    CouponConfigExample emConfig = new CouponConfigExample();
                    CouponConfigExample.Criteria caConfig = emConfig.createCriteria();
                    caConfig.andCouponCodeEqualTo(couponCode);
                    List<CouponConfig> configList = couponConfigMapper.selectByExample(emConfig);
                    if (configList == null || configList.isEmpty()) {
                        continue;
                    }
                    CouponConfig config = configList.get(0);

                    Integer status = config.getStatus();
                    if(status==null||status==1||status==3){
                        LogUtil.infoLog(THIS_CLASS, methodName, "优惠券审核未通过，无法发放！（coupon）"+couponCode);
                        continue;
                    }
                    // 加息券编号
                    couponUser.setCouponUserCode(GetCode.getCouponUserCode(config.getCouponType()));

                    if (config.getExpirationType() == 1) { // 截止日
                        couponUser.setEndTime(config.getExpirationDate());
                    } else if(config.getExpirationType() == 2) { // 时长
                        couponUser.setEndTime((int) (GetDate.countDate(2, config.getExpirationLength()).getTime() / 1000));
                    } else if(config.getExpirationType() == 3){
                    	couponUser.setEndTime((int) (GetDate.countDate(5, config.getExpirationLengthDay()).getTime() / 1000));
                    }
                    couponUser.setCouponSource(couponSource);
                    couponUser.setAddTime(nowTime);
                    couponUser.setAddUser(CustomConstants.OPERATOR_AUTO_REPAY);
                    couponUser.setUpdateTime(nowTime);
                    couponUser.setUpdateUser(CustomConstants.OPERATOR_AUTO_REPAY);
                    couponUser.setDelFlag(CustomConstants.FALG_NOR);
                    couponUser.setChannel(channelName);
                    couponUser.setAttribute(userInfo.getAttribute());
                    couponUser.setContent(StringUtils.isEmpty(content)?"":content);
                    couponUserMapper.insertSelective(couponUser);
                    couponCount++;
                }
                _log.info("发放优惠券成功，发放张数：" + couponCount);
            }
            _log.info("用户："+userId+",执行发券逻辑结束  " + GetDate.dateToString(new Date()));
            return couponCount;
        }
    
    /**
     * 校验优惠券的已发行数量
     * 
     * @return
     */
    private boolean checkSendNum(String couponCode) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("couponCode", couponCode);
        int remain = couponConfigCustomizeMapper.checkCouponSendExcess(param);

        return remain > 0 ? true : false;
    }
    
    /**
     * 
     * 获取用户注册时的渠道名称
     * @author hsy
     * @param userId
     * @return
     */
    public String getChannelNameByUserId(Integer userId){
        
        String channelName = channelCustomizeMapper.selectChannelNameByUserId(userId);
        
        return channelName;
    }
    
    /**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	public UsersInfo getUsersInfoByUserId(Integer userId) {
		if (userId != null) {
			UsersInfoExample example = new UsersInfoExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<UsersInfo> usersInfoList = this.usersInfoMapper.selectByExample(example);
			if (usersInfoList != null && usersInfoList.size() > 0) {
				return usersInfoList.get(0);
			}
		}
		return null;
	}
    
    /**
     * 根据用户名获取用户
     * @param userName
     * @return
     */
	public Users getUserByUserName(String userName){
    	if(StringUtils.isEmpty(userName)){
    		return null;
    	}
    	
    	UsersExample example = new UsersExample();
    	example.createCriteria().andUsernameEqualTo(userName);
    	List<Users> userList = usersMapper.selectByExample(example);
    	
    	if(userList != null && userList.size() == 1){
    		return userList.get(0);
    	}
    	
    	return null;
    }

    /**
     * 修改审核状态
     *
     * @param
     * @return
     */
    @Override
    public boolean updateCoupon(CouponCheckBean form) {
        String id = form.getId();
        Integer status = Integer.valueOf(form.getStatus());
        String mark = form.getMark();
        String[] split = id.split(",");
        String ids = split[0];
        CouponCheck couponCheck = new CouponCheck();
        couponCheck.setStatus(status);
        couponCheck.setMark(mark);
        CouponCheckExample couponCheckExample = new CouponCheckExample();
        CouponCheckExample.Criteria criteria = couponCheckExample.createCriteria();
        criteria.andIdEqualTo(Integer.valueOf(ids));
        return this.couponCheckMapper.updateByExampleSelective(couponCheck, couponCheckExample) > 0 ? true : false;

    }


    public static String getEncoder(InputStream in) throws Exception {
        in.mark(100000);
        BufferedInputStream bin = new BufferedInputStream(in);
        int p = (bin.read() << 8) + bin.read();
        String code = null;
        //in.reset();

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

	@Override
	public CouponCheck getById(String id) {
		return couponCheckMapper.selectByPrimaryKey(Integer.parseInt(id));
	}


}
