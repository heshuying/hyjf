package com.hyjf.admin.msgpush.tag;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
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
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.MessagePushTagExample;
import com.hyjf.mybatis.model.customize.AdminSystem;

@Service
public class MessagePushTagServiceImpl extends BaseServiceImpl implements MessagePushTagService {

    /**
     * 获取列表
     * 
     * @return
     */
    @Override
	public List<MessagePushTag> getRecordList(MessagePushTagBean bean, int limitStart, int limitEnd) {
        MessagePushTagExample example = new MessagePushTagExample();
        MessagePushTagExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if (bean.getTagNameSrch() != null) {
            criteria.andTagNameLike("%" + bean.getTagNameSrch() + "%");
        }
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.setOrderByClause("sort asc");
        return this.messagePushTagMapper.selectByExample(example);
    }

	/**
	 * 获取列表记录数
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer getRecordCount(MessagePushTagBean bean) {
        MessagePushTagExample example = new MessagePushTagExample();
        MessagePushTagExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if (bean.getTagNameSrch() != null) {
            criteria.andTagNameLike("%" + bean.getTagNameSrch() + "%");
        }
        return this.messagePushTagMapper.countByExample(example);
	}
    
    
    /**
     * 获取单个
     * 
     * @return
     */
    @Override
	public MessagePushTag getRecord(Integer record) {
        MessagePushTag page = messagePushTagMapper.selectByPrimaryKey(record);
        return page;
    }

    /**
     * 根据主键判断数据是否存在
     * 
     * @return
     */
    @Override
	public boolean isExistsRecord(MessagePushTag record) {
        if (record.getId() == null) {
            return false;
        }
        MessagePushTagExample example = new MessagePushTagExample();
        MessagePushTagExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<MessagePushTag> MessagePushTagList = messagePushTagMapper.selectByExample(example);
        if (MessagePushTagList != null && MessagePushTagList.size() > 0) {
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
	public void insertRecord(MessagePushTag record) {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		AdminSystem users = (AdminSystem) session.getAttribute(CustomConstants.LOGIN_USER_INFO);
		record.setCreateTime(GetDate.getNowTime10());
//		record.setCreateUserId(0);
		record.setCreateUserName(users.getUsername());
		record.setLastupdateTime(GetDate.getNowTime10());
//		record.setLastupdateUserId(0);
		record.setLastupdateUserName(users.getUsername());
        record.setStatus(0);//新建状态
        messagePushTagMapper.insertSelective(record);
    }

    /**
     * 更新
     * 
     * @param record
     */
    @Override
	public void updateRecord(MessagePushTag record) {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		AdminSystem users = (AdminSystem) session.getAttribute(CustomConstants.LOGIN_USER_INFO);
		record.setLastupdateTime(GetDate.getNowTime10());
//		record.setLastupdateUserId(0);
		record.setLastupdateUserName(users.getUsername());
        messagePushTagMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键删除
     * 
     * @param recordList
     */
    @Override
	public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
            messagePushTagMapper.deleteByPrimaryKey(id);
        }
    }

	/**
	 * 检查标签编码唯一性
	 * @param id
	 * @param tagCode
	 * @return
	 * @author Michael
	 */
		
	@Override
	public int countByTagCode(Integer id, String tagCode) {
		 MessagePushTagExample example = new MessagePushTagExample();
	     MessagePushTagExample.Criteria cra = example.createCriteria();
	     if (Validator.isNotNull(id)) {
	    	 cra.andIdNotEqualTo(id);
	     }
	     cra.andTagCodeEqualTo(tagCode);
	     int cnt = messagePushTagMapper.countByExample(example);
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

	@Override
	public List<MessagePushTag> getAllPushTagList() {
        MessagePushTagExample example = new MessagePushTagExample();
        return this.messagePushTagMapper.selectByExample(example);
	}

}
