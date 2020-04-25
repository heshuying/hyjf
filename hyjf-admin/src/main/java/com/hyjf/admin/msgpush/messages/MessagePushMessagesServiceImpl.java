package com.hyjf.admin.msgpush.messages;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.hyjf.mybatis.model.auto.MessagePushMsg;
import com.hyjf.mybatis.model.auto.MessagePushMsgExample;
import com.hyjf.mybatis.model.customize.AdminSystem;

@Service
public class MessagePushMessagesServiceImpl extends BaseServiceImpl implements MessagePushMessagesService {

	@Override
	public Integer getRecordCount(MessagePushMessagesBean form) {
		MessagePushMsgExample example = new MessagePushMsgExample();
		MessagePushMsgExample.Criteria criteria = example.createCriteria();
		if (form.getTagId() != null) {
			criteria.andTagIdEqualTo(form.getTagId());
		}
		if (StringUtils.isNotEmpty(form.getMessagesTitleSrch())) {
			criteria.andMsgTitleLike("%"+form.getMessagesTitleSrch()+ "%");
		}
		if (StringUtils.isNotEmpty(form.getMessagesCodeSrch())) {
			criteria.andMsgCodeLike("%"+form.getMessagesCodeSrch()+ "%");
		}
		if (StringUtils.isNotEmpty(form.getMessagesCreateUserNameSrch())) {
			criteria.andCreateUserNameLike("%"+form.getMessagesCreateUserNameSrch()+ "%");
		}
		if (StringUtils.isNotEmpty(form.getMessagesTerminalSrch())) {
			criteria.andMsgTerminalLike("%"+form.getMessagesTerminalSrch()+ "%");
		}
		if (form.getMessagesSendStatusSrch() != null) {
			criteria.andMsgSendStatusEqualTo(form.getMessagesSendStatusSrch());
		}
		if (StringUtils.isNotEmpty(form.getStartSendTimeSrch())) {
			try {
				Integer time = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getMessagesCreateUserNameSrch());
				criteria.andSendTimeGreaterThanOrEqualTo(time);
			} catch (Exception e) {
			}
		}
		if (StringUtils.isNotEmpty(form.getEndSendTimeSrch())) {
			try {
				Integer time = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getEndSendTimeSrch());
				criteria.andSendTimeLessThanOrEqualTo(time);
			} catch (Exception e) {
			}
		}
		criteria.andMsgDestinationTypeEqualTo(CustomConstants.MSG_PUSH_DESTINATION_TYPE_1);
		return this.messagePushMsgMapper.countByExample(example);
	}

	@Override
	public List<MessagePushMsg> getRecordList(MessagePushMessagesBean form, int limitStart, int limitEnd) {
		MessagePushMsgExample example = new MessagePushMsgExample();
		MessagePushMsgExample.Criteria criteria = example.createCriteria();
		// 条件查询
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		if (form.getTagId() != null) {
			criteria.andTagIdEqualTo(form.getTagId());
		}
		if (StringUtils.isNotEmpty(form.getMessagesTitleSrch())) {
			criteria.andMsgTitleLike("%"+form.getMessagesTitleSrch()+ "%");
		}
		if (StringUtils.isNotEmpty(form.getMessagesCodeSrch())) {
			criteria.andMsgCodeLike("%"+form.getMessagesCodeSrch()+ "%");
		}
		if (StringUtils.isNotEmpty(form.getMessagesCreateUserNameSrch())) {
			criteria.andCreateUserNameLike("%"+form.getMessagesCreateUserNameSrch()+ "%");
		}
		if (StringUtils.isNotEmpty(form.getMessagesTerminalSrch())) {
			criteria.andMsgTerminalLike("%"+form.getMessagesTerminalSrch()+ "%");
		}
		if (form.getMessagesSendStatusSrch() != null) {
			criteria.andMsgSendStatusEqualTo(form.getMessagesSendStatusSrch());
		}
		if (StringUtils.isNotEmpty(form.getStartSendTimeSrch())) {
			try {
				Integer time = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getMessagesCreateUserNameSrch());
				criteria.andSendTimeGreaterThanOrEqualTo(time);
			} catch (Exception e) {
			}
		}
		if (StringUtils.isNotEmpty(form.getEndSendTimeSrch())) {
			try {
				Integer time = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getEndSendTimeSrch());
				criteria.andSendTimeLessThanOrEqualTo(time);
			} catch (Exception e) {
			}
		}
		criteria.andMsgDestinationTypeEqualTo(CustomConstants.MSG_PUSH_DESTINATION_TYPE_1);
		example.setOrderByClause("create_time DESC");
		return this.messagePushMsgMapper.selectByExample(example);
	}

	/**
	 * 获取单个
	 * 
	 * @return
	 */
	@Override
	public MessagePushMsg getRecord(Integer record) {
		MessagePushMsg page = messagePushMsgMapper.selectByPrimaryKey(record);
		return page;
	}

	/**
	 * 根据主键判断数据是否存在
	 * 
	 * @return
	 */
	@Override
	public boolean isExistsRecord(MessagePushMsg record) {
		if (record.getId() == null) {
			return false;
		}
		MessagePushMsgExample example = new MessagePushMsgExample();
		MessagePushMsgExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<MessagePushMsg> MessagePushTemplateList = messagePushMsgMapper.selectByExample(example);
		if (MessagePushTemplateList != null && MessagePushTemplateList.size() > 0) {
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
	public void insertRecord(MessagePushMsg record) {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		AdminSystem users = (AdminSystem) session.getAttribute(CustomConstants.LOGIN_USER_INFO);
		record.setCreateTime(GetDate.getNowTime10());
		// record.setCreateUserId(0);
		record.setCreateUserName(users.getUsername());
		record.setLastupdateTime(GetDate.getNowTime10());
		// record.setLastupdateUserId(0);
		record.setLastupdateUserName(users.getUsername());
		messagePushMsgMapper.insertSelective(record);
	}

	/**
	 * 更新
	 * 
	 * @param record
	 */
	@Override
	public void updateRecord(MessagePushMsg record) {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		AdminSystem users = (AdminSystem) session.getAttribute(CustomConstants.LOGIN_USER_INFO);
		record.setLastupdateTime(GetDate.getNowTime10());
		// record.setLastupdateUserId(0);
		record.setLastupdateUserName(users.getUsername());
		messagePushMsgMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 根据主键删除
	 * 
	 * @param recordList
	 */
	@Override
	public void deleteRecord(List<Integer> recordList) {
		for (Integer id : recordList) {
			messagePushMsgMapper.deleteByPrimaryKey(id);
		}
	}

//	/**
//	 * 检查标签编码是否唯一
//	 * 
//	 * @param id
//	 *            主键
//	 * @param tagId
//	 *            标签类型id
//	 * @param templateCode
//	 *            标签编码code
//	 */
//
//	@Override
//	public int countByTemplateCode(Integer id, Integer tagId, String templateCode) {
//		MessagePushMsgExample example = new MessagePushMsgExample();
//		MessagePushMsgExample.Criteria cra = example.createCriteria();
//		// TODO
//		// if (Validator.isNotNull(id)) {
//		// cra.andIdNotEqualTo(id);
//		// }
//		// if(StringUtils.isNotEmpty(tagCode)){
//		// cra.andTagCodeEqualTo(tagCode);
//		// }
//		// if(StringUtils.isNotEmpty(templateCode)){
//		// cra.andTemplateCodeEqualTo(templateCode);
//		// }
//		int cnt = messagePushMsgMapper.countByExample(example);
//		return cnt;
//	}

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
