package com.hyjf.admin.whereaboutspage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonImage;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.Utm;
import com.hyjf.mybatis.model.auto.WhereaboutsPageConfig;
import com.hyjf.mybatis.model.auto.WhereaboutsPagePicture;
import com.hyjf.mybatis.model.auto.WhereaboutsPagePictureExample;
import com.hyjf.mybatis.model.customize.whereaboutspage.WhereaboutsPageConfigCustomize;

@Service
public class WhereaboutsPageServiceImpl extends BaseServiceImpl implements WhereaboutsPageService {
    @Override
    public Integer countRecord(WhereaboutsPageConfigCustomize form) {
        return whereaboutsPageCustomizeMapper.countWhereaboutsPage(form);
    }

    @Override
    public List<WhereaboutsPageConfigCustomize> getRecordList(WhereaboutsPageConfigCustomize form) {
        return whereaboutsPageCustomizeMapper.selectWhereaboutsPageList(form);

    }

    @Override
    public void deleteRecord(String recordId) {
        if (StringUtils.isNotEmpty(recordId)) {
            WhereaboutsPageConfig record = whereaboutsPageConfigMapper.selectByPrimaryKey(Integer
                    .parseInt(recordId));
            record.setDeleteStatus(CustomConstants.FALG_DEL);
            // 操作者
            record.setUpdateUser(ShiroUtil.getLoginUserId());
            // 操作时间
            record.setUpdateTime(GetDate.getNowTime10());
            whereaboutsPageConfigMapper.updateByPrimaryKeySelective(record);
        }
    }

    @Override
    public String checkUtmId(String utmId) {
        JSONObject ret = new JSONObject();
        if(utmId==null || "".equals(utmId)){
            ret.put(WhereaboutsPageDefine.JSON_VALID_INFO_KEY, "渠道不能为空");
            return ret.toString();
        }
        Pattern pattern = Pattern.compile("[0-9]+");  
        Matcher matcher = pattern.matcher((CharSequence) utmId);  
        if(!matcher.matches()){
            ret.put(WhereaboutsPageDefine.JSON_VALID_INFO_KEY, "请输入正确的渠道格式");
            return ret.toString();
        }
        Utm utm =utmMapper.selectByPrimaryKey(Integer.parseInt(utmId));
        if(utm!=null&&utm.getUtmId()!=null){
            ret.put(WhereaboutsPageDefine.JSON_VALID_STATUS_KEY, WhereaboutsPageDefine.JSON_VALID_STATUS_OK);
            return ret.toString();
        }
        ret.put(WhereaboutsPageDefine.JSON_VALID_INFO_KEY, "渠道不存在");
        return ret.toString();
    }

    @Override
    public String checkReferrer(String referrer) {
        JSONObject ret = new JSONObject();
        if(referrer==null || "".equals(referrer)){
            ret.put(WhereaboutsPageDefine.JSON_VALID_INFO_KEY, "推荐人不能为空");
            return ret.toString();
        }
        UsersExample example=new UsersExample();
        example.createCriteria().andUsernameEqualTo(referrer);
        List<Users> list=usersMapper.selectByExample(example);
        if(list!=null&&list.size()>0){
            ret.put(WhereaboutsPageDefine.JSON_VALID_STATUS_KEY, WhereaboutsPageDefine.JSON_VALID_STATUS_OK);
            return ret.toString();
         }
        ret.put(WhereaboutsPageDefine.JSON_VALID_INFO_KEY, "推荐人不存在");
        return ret.toString();
    }

    @Override
    public void insertAction(WhereaboutsPageBean form) {
        WhereaboutsPageConfig whereaboutsPageConfig=createWhereaboutsPageConfig(form);
        whereaboutsPageConfigMapper.insertSelective(whereaboutsPageConfig);
        
        List<WhereaboutsPageImage> imageJsonList1 = JSONArray.parseArray(form.getImageJson1(), WhereaboutsPageImage.class);
        
        for (WhereaboutsPageImage whereaboutsPageImage : imageJsonList1) {
            WhereaboutsPagePicture whereaboutsPagePicture = createWhereaboutsPagePicture(whereaboutsPageImage,1,whereaboutsPageConfig.getId());
            whereaboutsPagePictureMapper.insertSelective(whereaboutsPagePicture);
        }
        
        List<WhereaboutsPageImage> imageJsonList2 = JSONArray.parseArray(form.getImageJson2(), WhereaboutsPageImage.class);
        
        for (WhereaboutsPageImage whereaboutsPageImage : imageJsonList2) {
            WhereaboutsPagePicture whereaboutsPagePicture = createWhereaboutsPagePicture(whereaboutsPageImage,2,whereaboutsPageConfig.getId());
            whereaboutsPagePictureMapper.insertSelective(whereaboutsPagePicture);
        }
        
        List<WhereaboutsPageImage> imageJsonList3 = JSONArray.parseArray(form.getImageJson3(), WhereaboutsPageImage.class);
        
        for (WhereaboutsPageImage whereaboutsPageImage : imageJsonList3) {
            WhereaboutsPagePicture whereaboutsPagePicture = createWhereaboutsPagePicture(whereaboutsPageImage,3,whereaboutsPageConfig.getId());
            whereaboutsPagePictureMapper.insertSelective(whereaboutsPagePicture);
        }
        
        
    }

    private WhereaboutsPagePicture createWhereaboutsPagePicture(WhereaboutsPageImage whereaboutsPageImage,int pictureType,int whereaboutsId) {
        //[{"imageSort":"0","imageName":"1","file":"","imageRealName":"1474868885982.png","imagePath":"/data/upfiles/filetemp/image/1474868885982.png"}]
        WhereaboutsPagePicture whereaboutsPagePicture = new WhereaboutsPagePicture();
        whereaboutsPagePicture.setPictureName(whereaboutsPageImage.getImageName());
        whereaboutsPagePicture.setPictureType(pictureType);
        whereaboutsPagePicture.setWhereaboutsId(whereaboutsId);
        whereaboutsPagePicture.setPictureUrl(
                whereaboutsPageImage.getImagePath());
        whereaboutsPagePicture.setSort(Integer.parseInt(whereaboutsPageImage.getImageSort()));
        whereaboutsPagePicture.setDeleteStatus(0);
        // 操作者
        whereaboutsPagePicture.setCreateUser(ShiroUtil.getLoginUserId());
        // 操作时间
        whereaboutsPagePicture.setCreateTime(GetDate.getNowTime10());
        return whereaboutsPagePicture;
    }

    private WhereaboutsPageConfig createWhereaboutsPageConfig(WhereaboutsPageBean form) {
        WhereaboutsPageConfig whereaboutsPageConfig=new WhereaboutsPageConfig();
        if(form.getId()!=null&&form.getId()!=0){
            whereaboutsPageConfig.setId(form.getId());
        }
        whereaboutsPageConfig.setTitle(form.getTitle());
        Utm utm =utmMapper.selectByPrimaryKey(form.getUtmId());
        if(utm!=null&&utm.getUtmId()!=null){
            whereaboutsPageConfig.setUtmId(form.getUtmId());
        }
        if(form.getReferrerName()!=null && !"".equals(form.getReferrerName())){
            UsersExample example=new UsersExample();
            example.createCriteria().andUsernameEqualTo(form.getReferrerName());
            List<Users> list=usersMapper.selectByExample(example);
            if(list!=null&&list.size()!=0){
                whereaboutsPageConfig.setReferrer(list.get(0).getUserId());
            }
            
        }
        whereaboutsPageConfig.setStyle(form.getStyle());
        whereaboutsPageConfig.setTopButton(StringUtils.isNotEmpty(form.getTopButton()) ? form.getTopButton() : "");
        whereaboutsPageConfig.setJumpPath(StringUtils.isNotEmpty(form.getJumpPath()) ? form.getJumpPath() : "");
        whereaboutsPageConfig.setBottomButtonStatus(form.getBottomButtonStatus());
        whereaboutsPageConfig.setBottomButton(StringUtils.isNotEmpty(form.getBottomButton()) ? form.getBottomButton() : "");
        whereaboutsPageConfig.setDownloadPath(form.getDownloadPath());
        whereaboutsPageConfig.setDescribe(form.getDescribe());
        whereaboutsPageConfig.setDeleteStatus(0);
        whereaboutsPageConfig.setStatusOn(form.getStatusOn());
        
        
        // 操作者
        whereaboutsPageConfig.setCreateUser(ShiroUtil.getLoginUserId());
        // 操作时间
        whereaboutsPageConfig.setCreateTime(GetDate.getNowTime10());
        return whereaboutsPageConfig;
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
        String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.whereabouts.page.path"));

        String logoRealPathDir = filePhysicalPath + fileUploadTempPath;

        File logoSaveFile = new File(logoRealPathDir);
        if (!logoSaveFile.exists()) {
            logoSaveFile.mkdirs();
        }

        WhereaboutsPageImage fileMeta = null;
        LinkedList<WhereaboutsPageImage> files = new LinkedList<WhereaboutsPageImage>();

        Iterator<String> itr = multipartRequest.getFileNames();
        MultipartFile multipartFile = null;

        while (itr.hasNext()) {
            multipartFile = multipartRequest.getFile(itr.next());
            String fileRealName = String.valueOf(new Date().getTime());
            String originalFilename = multipartFile.getOriginalFilename();
            fileRealName = fileRealName + UploadFileUtils.getSuffix(multipartFile.getOriginalFilename());

            // 文件大小
            String errorMessage = UploadFileUtils.upload4Stream(fileRealName, logoRealPathDir, multipartFile.getInputStream(), 5000000L);

            fileMeta = new WhereaboutsPageImage();
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

    @Override
    public void getWhereaboutsPageConfigById(WhereaboutsPageBean form) {
        WhereaboutsPageConfig whereaboutsPageConfig=whereaboutsPageConfigMapper.selectByPrimaryKey(form.getId());
        
        if(whereaboutsPageConfig!=null&&whereaboutsPageConfig.getId()!=0){
            WhereaboutsPagePictureExample example=new WhereaboutsPagePictureExample();
            example.createCriteria().andWhereaboutsIdEqualTo(form.getId());
            List<WhereaboutsPagePicture> list=whereaboutsPagePictureMapper.selectByExample(example);
            
            List<BorrowCommonImage> pictureTypeList1=new ArrayList<BorrowCommonImage>();
            List<BorrowCommonImage> pictureTypeList2=new ArrayList<BorrowCommonImage>();
            List<BorrowCommonImage> pictureTypeList3=new ArrayList<BorrowCommonImage>();
            for (WhereaboutsPagePicture whereaboutsPagePicture : list) {
                if(whereaboutsPagePicture.getPictureType()==1){
                    pictureTypeList1.add(createBorrowCommonImage(whereaboutsPagePicture));
                }
                if(whereaboutsPagePicture.getPictureType()==2){
                    pictureTypeList2.add(createBorrowCommonImage(whereaboutsPagePicture));
                }
                if(whereaboutsPagePicture.getPictureType()==3){
                    pictureTypeList3.add(createBorrowCommonImage(whereaboutsPagePicture));
                }
            } 
            
            form.setWhereaboutsPagePictures1(pictureTypeList1);
            form.setWhereaboutsPagePictures2(pictureTypeList2);
            form.setWhereaboutsPagePictures3(pictureTypeList3);
            
            createWhereaboutsPageBean(form,whereaboutsPageConfig);
            
            
        }
       
    }

    private BorrowCommonImage createBorrowCommonImage(WhereaboutsPagePicture whereaboutsPagePicture) {
        String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
        BorrowCommonImage whereaboutsPageImage=new BorrowCommonImage();
        
        whereaboutsPageImage.setImageName(whereaboutsPagePicture.getPictureName());
        whereaboutsPageImage.setImagePath(whereaboutsPagePicture.getPictureUrl());
        whereaboutsPageImage.setImageSort(whereaboutsPagePicture.getSort()+"");
        whereaboutsPageImage.setImageRealName(fileDomainUrl+whereaboutsPagePicture.getPictureUrl());
        whereaboutsPageImage.setImageSrc(fileDomainUrl+whereaboutsPagePicture.getPictureUrl());
        
        return whereaboutsPageImage;
    }

    private void createWhereaboutsPageBean(WhereaboutsPageBean form, WhereaboutsPageConfig whereaboutsPageConfig) {
        form.setTitle(whereaboutsPageConfig.getTitle());
        form.setUtmId(whereaboutsPageConfig.getUtmId());
        
        if(whereaboutsPageConfig.getReferrer()!=null&&!"".equals(whereaboutsPageConfig.getReferrer())){
            UsersExample example=new UsersExample();
            example.createCriteria().andUserIdEqualTo(whereaboutsPageConfig.getReferrer());
            List<Users> list=usersMapper.selectByExample(example);
            if(list!=null&&list.size()!=0){
                form.setReferrerName(list.get(0).getUsername());  
            }
        }
        form.setStyle(whereaboutsPageConfig.getStyle());
        form.setTopButton(StringUtils.isNotEmpty(whereaboutsPageConfig.getTopButton()) ? whereaboutsPageConfig.getTopButton() : "");
        form.setJumpPath(StringUtils.isNotEmpty(whereaboutsPageConfig.getJumpPath()) ? whereaboutsPageConfig.getJumpPath() : "");
        form.setBottomButtonStatus(whereaboutsPageConfig.getBottomButtonStatus());
        form.setBottomButton(StringUtils.isNotEmpty(whereaboutsPageConfig.getBottomButton()) ? whereaboutsPageConfig.getBottomButton() : "");
        form.setDownloadPath(whereaboutsPageConfig.getDownloadPath());
        form.setDescribe(whereaboutsPageConfig.getDescribe());
        form.setStatusOn(whereaboutsPageConfig.getStatusOn());
    }

    @Override
    public void updateAction(WhereaboutsPageBean form) {
        WhereaboutsPageConfig whereaboutsPageConfig=createWhereaboutsPageConfig(form);
        whereaboutsPageConfigMapper.updateByPrimaryKeySelective(whereaboutsPageConfig);
        
        WhereaboutsPagePictureExample example=new WhereaboutsPagePictureExample();
        example.createCriteria().andWhereaboutsIdEqualTo(whereaboutsPageConfig.getId());
        whereaboutsPagePictureMapper.deleteByExample(example);
        
        List<WhereaboutsPageImage> imageJsonList1 = JSONArray.parseArray(form.getImageJson1(), WhereaboutsPageImage.class);
        
        for (WhereaboutsPageImage whereaboutsPageImage : imageJsonList1) {
            WhereaboutsPagePicture whereaboutsPagePicture = createWhereaboutsPagePicture(whereaboutsPageImage,1,whereaboutsPageConfig.getId());
            whereaboutsPagePictureMapper.insertSelective(whereaboutsPagePicture);
        }
        
        List<WhereaboutsPageImage> imageJsonList2 = JSONArray.parseArray(form.getImageJson2(), WhereaboutsPageImage.class);
        
        for (WhereaboutsPageImage whereaboutsPageImage : imageJsonList2) {
            WhereaboutsPagePicture whereaboutsPagePicture = createWhereaboutsPagePicture(whereaboutsPageImage,2,whereaboutsPageConfig.getId());
            whereaboutsPagePictureMapper.insertSelective(whereaboutsPagePicture);
        }
        
        List<WhereaboutsPageImage> imageJsonList3 = JSONArray.parseArray(form.getImageJson3(), WhereaboutsPageImage.class);
        
        for (WhereaboutsPageImage whereaboutsPageImage : imageJsonList3) {
            WhereaboutsPagePicture whereaboutsPagePicture = createWhereaboutsPagePicture(whereaboutsPageImage,3,whereaboutsPageConfig.getId());
            whereaboutsPagePictureMapper.insertSelective(whereaboutsPagePicture);
        }
        
    }
    
    /**
     * 
     * 状态更新
     * @author hsy
     * @param statusOn
     * @param id
     * @return
     */
    @Override
    public int statusAction(Integer statusOn, Integer id){
        if(statusOn == null || id == null){
            return 0;
        }
        
        WhereaboutsPageConfig config = new WhereaboutsPageConfig();
        config.setId(id);
        config.setStatusOn(statusOn == 0? 1 : 0);
        return whereaboutsPageConfigMapper.updateByPrimaryKeySelective(config);
    }
    
    
}











