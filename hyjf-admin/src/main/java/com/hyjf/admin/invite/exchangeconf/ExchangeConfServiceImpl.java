package com.hyjf.admin.invite.exchangeconf;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonImage;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.InvitePrizeConf;
import com.hyjf.mybatis.model.auto.InvitePrizeConfExample;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.admin.PrizeGetCustomize;

@Service
public class ExchangeConfServiceImpl extends BaseServiceImpl implements ExchangeConfService {

    /**
     * 获取奖品配置列表
     * 
     * @return
     */
	@Override
    public List<PrizeGetCustomize> getRecordList(ExchangeConfBean bean, int limitStart, int limitEnd) {
	    String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
	    Map<String,Object> paraMap = new HashMap<String,Object>();
	    // 条件查询
        if (StringUtils.isNotEmpty(bean.getPrizeStatusSrch())) {
            paraMap.put("prizeStatus", bean.getPrizeStatusSrch());
        }
        if (StringUtils.isNotEmpty(bean.getPrizeTypeSrch())) {
            paraMap.put("prizeType", bean.getPrizeTypeSrch());
        }
        paraMap.put("fileurl", fileDomainUrl);
        paraMap.put("prizeKind", CustomConstants.CONF_PRIZE_KIND_CHANGE);
        paraMap.put("limitStart", limitStart);
        paraMap.put("limitEnd", limitEnd);
        
        List<PrizeGetCustomize> result =  prizeGetCustomizeMapper.selectPrizeConfList(paraMap);
        
        return result;
	    
    }
	
	/**
	 * 
	 * 根据groupCode获取奖品配置
	 * @author hsy
	 * @param groupCode
	 * @return
	 */
	@Override
    public InvitePrizeConfCustom getPrizeByGroupCode(String groupCode){
	    if(StringUtils.isEmpty(groupCode)){
	        return null;
	    }
	    
	    InvitePrizeConfCustom prizeCustom = new InvitePrizeConfCustom();
	    
	    InvitePrizeConfExample example = new InvitePrizeConfExample();
	    example.createCriteria().andPrizeGroupCodeEqualTo(groupCode);
	    List<InvitePrizeConf> prizes =  invitePrizeConfMapper.selectByExample(example);
	    BeanUtils.copyProperties(prizes.get(0), prizeCustom);
	    
	    String couponCodes = "";
	    for(InvitePrizeConf prizeConf : prizes){
	        couponCodes = couponCodes + prizeConf.getCouponCode();
	        couponCodes = couponCodes + ",";
	    }
	    
	    couponCodes = couponCodes.substring(0, couponCodes.length() -1);
	    
	    prizeCustom.setCouponCodes(couponCodes);
	    
	    return prizeCustom;
	}
	
	/**
	 * 
	 * 奖品入库
	 * @author hsy
	 * @param prize
	 * @return
	 */
	@Override
    public int insertRecord(InvitePrizeConf prize){
	    AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
	    
	    prize.setAddTime(GetDate.getNowTime10());
	    prize.setUpdateTime(GetDate.getNowTime10());
	    prize.setAddUser(Integer.parseInt(adminSystem.getId()));
	    prize.setUpdateUser(Integer.parseInt(adminSystem.getId()));
	    prize.setPrizeKind(Integer.parseInt(CustomConstants.CONF_PRIZE_KIND_CHANGE));
	    prize.setPrizeReminderQuantity(prize.getPrizeQuantity());
	    prize.setDelFlg(CustomConstants.FALG_NOR);
	    return invitePrizeConfMapper.insertSelective(prize);
	}
	
	/**
	 * 
	 * 奖品更新
	 * @author hsy
	 * @param prize
	 * @return
	 */
	@Override
    public int updatePrizeConfig(ExchangeConfBean prize){
	    AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
	    
	    int result = 0;
	    int deleteResult = prizeGetCustomizeMapper.deletePrizeByGroupCode(prize.getPrizeGroupCode());
	    if(deleteResult <= 0){
	        return 0;
	    }
	    
        prize.setUpdateTime(GetDate.getNowTime10());
        prize.setUpdateUser(Integer.parseInt(adminSystem.getId()));
        
        // 记得同时更新剩余奖品数量
        int prizeRemaind = prize.getPrizeQuantity() - prize.getPrizeUsed();
        prize.setPrizeReminderQuantity(prizeRemaind);
        
        String[] couponCodes;
        if(prize.getPrizeType() == Integer.parseInt(CustomConstants.CONF_PRIZE_TYPE_COUPON) && prize.getCouponCode().contains(",")){
            couponCodes = prize.getCouponCode().split(",");
        }else{
            couponCodes = new String[]{prize.getCouponCode()};
        }
        for(String code : couponCodes){
            prize.setCouponCode(code);
            result = invitePrizeConfMapper.insertSelective(prize);
        }
        
        return result;
	}
	
	/**
	 * 
	 * 更新奖品状态
	 * @author hsy
	 * @param paraMap
	 * @return
	 */
	@Override
    public int updatePrizeStatus(Map<String, Object> paraMap){
	    return prizeGetCustomizeMapper.updatePrizeStatus(paraMap);
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
        MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
        String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
        String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
        String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.temp.path"));

        String logoRealPathDir = filePhysicalPath + fileUploadTempPath;

        File logoSaveFile = new File(logoRealPathDir);
        if (!logoSaveFile.exists()) {
            logoSaveFile.mkdirs();
        }

        BorrowCommonImage fileMeta = null;
        LinkedList<BorrowCommonImage> files = new LinkedList<BorrowCommonImage>();

        Iterator<String> itr = multipartRequest.getFileNames();
        MultipartFile multipartFile = null;

        while (itr.hasNext()) {
            multipartFile = multipartRequest.getFile(itr.next());
            String fileRealName = String.valueOf(new Date().getTime());
            String originalFilename = multipartFile.getOriginalFilename();
            fileRealName = fileRealName + UploadFileUtils.getSuffix(multipartFile.getOriginalFilename());

            // 文件大小
            String errorMessage = UploadFileUtils.upload4Stream(fileRealName, logoRealPathDir, multipartFile.getInputStream(), 5000000L);

            fileMeta = new BorrowCommonImage();
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


}
