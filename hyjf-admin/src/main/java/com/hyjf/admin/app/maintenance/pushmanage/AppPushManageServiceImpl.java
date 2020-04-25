package com.hyjf.admin.app.maintenance.pushmanage;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonImage;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.AppPushManage;
import com.hyjf.mybatis.model.auto.AppPushManageExample;
import com.hyjf.mybatis.model.customize.AdminSystem;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author : huanghui
 */
@Service
public class AppPushManageServiceImpl extends BaseServiceImpl implements AppPushManageService {



    @Override
    public int countAppPushManageList(AppPushManageBean form) {
        AppPushManageExample appPushManageExample = new AppPushManageExample();
        AppPushManageExample.Criteria criteria = appPushManageExample.createCriteria();


        return appPushManageMapper.countByExample(appPushManageExample);
    }

    @Override
    public List<AppPushManage> searchAppPushManageList(AppPushManageBean form) {
        AppPushManageExample appPushManageExample = new AppPushManageExample();
        AppPushManageExample.Criteria criteria = appPushManageExample.createCriteria();

        appPushManageExample.setOrderByClause("`order` ASC");
        return appPushManageMapper.selectByExample(appPushManageExample);
    }

    @Override
    public int insertRecord(AppPushManage record) {
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);

        record.setAddAdmin(Integer.parseInt(adminSystem.getId()));
        record.setCreateTime(GetDate.getNowTime10());
        record.setUpdateTime(GetDate.getNowTime10());
        return appPushManageMapper.insertSelective(record);
    }

    @Override
    public AppPushManage getAppPushManageInfo(Integer id) {
        return appPushManageMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateRecord(AppPushManageBean record) {
        record.setUpdateTime(GetDate.getNowTime10());
        return appPushManageMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public AppPushManage getRecord(Integer record) {
        AppPushManage appPushManage = appPushManageMapper.selectByPrimaryKey(record);
        return appPushManage;
    }

    @Override
    public void updateRecordNoVoid(AppPushManage record) {
        record.setUpdateTime(GetDate.getNowTime10());
        appPushManageMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList){
            appPushManageMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<AppPushManage> selectRecordList(AppPushManageBean record, int limitStart, int limitEnd) {
        AppPushManageExample example = new AppPushManageExample();
        AppPushManageExample.Criteria cra = example.createCriteria();

        //标题检索
        if (StringUtils.isNotEmpty(record.getTitle())) {
            cra.andTitleLike("%" + record.getTitle() + "%");
        }
        //状态检索
        if (StringUtils.isNotEmpty(record.getStatusSch())){
            int statusSch = Integer.parseInt(record.getStatusSch());
            cra.andStatusEqualTo(statusSch);
        }
        //添加时间检索
        if (StringUtils.isNotEmpty(record.getTimeStartDiy())){
            int timeStartSch = GetDate.getDayStart10(record.getTimeStartDiy());
            int timeendSch = GetDate.getDayEnd10(record.getTimeEndDiy());
            cra.andCreateTimeBetween(timeStartSch, timeendSch);
        }
        if (limitStart != -1){
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.setOrderByClause("`order` asc");
        return appPushManageMapper.selectByExample(example);
    }

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
