package com.hyjf.admin.manager.content.landingpage;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonImage;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.LandingPage;
import com.hyjf.mybatis.model.auto.LandingPageExample;

@Service
public class LandingPageServiceImpl extends BaseServiceImpl implements LandingPageService {

    /**
     * 获取列表
     * 
     * @return
     */
    @Override
	public List<LandingPage> getRecordList(LandingPageBean bean, int limitStart, int limitEnd) {
        LandingPageExample example = new LandingPageExample();
        LandingPageExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if (bean.getChannelNameSrch() != null) {
            criteria.andChannelNameLike("%" + bean.getChannelNameSrch() + "%");
        }
        if (StringUtils.isNotEmpty(bean.getPageNameSrch())) {
            criteria.andPageNameLike("%" + bean.getPageNameSrch() + "%");
        }
        if(StringUtils.isNotEmpty(bean.getStartTime())){
        	criteria.andCreateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(bean.getStartTime())));
        }
        if(StringUtils.isNotEmpty(bean.getEndTime())){
        	criteria.andCreateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(bean.getEndTime())));
        }
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.setOrderByClause("create_time Desc");
        return this.landingPageMapper.selectByExample(example);
    }

	/**
	 * 获取列表记录数
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer getRecordCount(LandingPageBean bean) {
        LandingPageExample example = new LandingPageExample();
        LandingPageExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if (bean.getChannelNameSrch() != null) {
            criteria.andChannelNameLike("%" + bean.getChannelNameSrch() + "%");
        }
        if (StringUtils.isNotEmpty(bean.getPageNameSrch())) {
            criteria.andPageNameLike("%" + bean.getPageNameSrch() + "%");
        }
        if(StringUtils.isNotEmpty(bean.getStartTime())){
        	criteria.andCreateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(bean.getStartTime())));
        }
        if(StringUtils.isNotEmpty(bean.getEndTime())){
        	criteria.andCreateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(bean.getEndTime())));
        }
        return this.landingPageMapper.countByExample(example);
	}
    
    
    /**
     * 获取单个
     * 
     * @return
     */
    @Override
	public LandingPage getRecord(Integer record) {
        LandingPage page = landingPageMapper.selectByPrimaryKey(record);
        return page;
    }

    /**
     * 根据主键判断数据是否存在
     * 
     * @return
     */
    @Override
	public boolean isExistsRecord(LandingPage record) {
        if (record.getId() == null) {
            return false;
        }
        LandingPageExample example = new LandingPageExample();
        LandingPageExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<LandingPage> LandingPageList = landingPageMapper.selectByExample(example);
        if (LandingPageList != null && LandingPageList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 插入
     * 
     * @param record
     */
    @Override
	public void insertRecord(LandingPage record) {

        record.setCreateTime(GetDate.getNowTime10());

        landingPageMapper.insertSelective(record);
    }

    /**
     * 更新
     * 
     * @param record
     */
    @Override
	public void updateRecord(LandingPage record) {
        landingPageMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键删除
     * 
     * @param recordList
     */
    @Override
	public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
            landingPageMapper.deleteByPrimaryKey(id);
        }
    }

	/**
	 * 检车着落页名称唯一性
	 * @param id
	 * @param pageName
	 * @return
	 * @author Michael
	 */
		
	@Override
	public int countByPageName(Integer id, String pageName) {
		 LandingPageExample example = new LandingPageExample();
	     LandingPageExample.Criteria cra = example.createCriteria();
	     if (Validator.isNotNull(id)) {
	    	 cra.andIdNotEqualTo(id);
	     }
	     cra.andPageNameEqualTo(pageName);
	     int cnt = landingPageMapper.countByExample(example);
		 return cnt;
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
			// 图片上传
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
