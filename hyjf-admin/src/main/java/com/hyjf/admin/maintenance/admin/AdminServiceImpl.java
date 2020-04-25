package com.hyjf.admin.maintenance.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.auto.AdminAndRole;
import com.hyjf.mybatis.model.auto.AdminAndRoleExample;
import com.hyjf.mybatis.model.auto.AdminExample;
import com.hyjf.mybatis.model.auto.AdminExample.Criteria;
import com.hyjf.mybatis.model.customize.AdminCustomize;

@Service
public class AdminServiceImpl extends BaseServiceImpl implements AdminService {
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	/**
	 * 获取账户列表
	 *
	 * @return
	 */
	public List<AdminCustomize> getRecordList(AdminCustomize adminCustomize) {
		return adminCustomizeMapper.selectAdminList(adminCustomize);
	}

	/**
	 * 获取单个账户
	 *
	 * @return
	 */
	public AdminCustomize getRecord(Integer id) {
		AdminCustomize adminCustomize = new AdminCustomize();
		adminCustomize.setId(id);
		List<AdminCustomize> adminList = adminCustomizeMapper.selectAdminList(adminCustomize);
		if (adminList != null && adminList.size() > 0) {
			return adminList.get(0);
		}
		return new AdminCustomize();
	}

	/**
	 * 根据主键判断账户中数据是否存在
	 *
	 * @return
	 */
	public boolean isExistsRecord(Integer id) {
		if (Validator.isNull(id)) {
			return false;
		}
		AdminExample example = new AdminExample();
		AdminExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(id);
		int cnt = adminMapper.countByExample(example);
		return cnt > 0;
	}

	/**
	 * 账户插入
	 *
	 * @param record
	 */
	public void insertRecord(AdminBean form) {
		String nowTime = GetDate.getServerDateTime(9, new Date());
		String userId = ShiroUtil.getLoginUserId();
		Admin record = new Admin();
		BeanUtils.copyProperties(form, record);

		// 插入用户表
		String password = GetCode.getRandomPassword(8);
		record.setPassword(MD5.toMD5Code(password));
		record.setDelFlag(CustomConstants.FLAG_NORMAL);
		record.setAddtime(GetDate.getDate());
		record.setCreatetime(nowTime);
		record.setCreateuser(userId);
		record.setUpdatetime(nowTime);
		record.setUpdateuser(userId);
		int cnt = adminMapper.insertSelective(record);

		// 插入用户权限关联表
		if (Validator.isNotNull(form.getRoleId()) && cnt > 0) {
			AdminAndRole adminAndRole = new AdminAndRole();
			adminAndRole.setUserId(record.getId());
			adminAndRole.setRoleId(GetterUtil.getInteger(form.getRoleId()));
			adminAndRoleMapper.insertSelective(adminAndRole);
			if (userId.equals(String.valueOf(record.getId()))) {
				// 更新权限
				ShiroUtil.updateAuth();
			}
		}

		// 发送短信
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("var_yonghuming", record.getUsername());
		replaceStrs.put("var_htmm", password);
		replaceStrs.put("var_htdizhi", PropUtils.getSystem(CustomConstants.HYJF_ADMIN_LOGIN_URL));
		SmsMessage smsMessage = new SmsMessage(null, replaceStrs, record.getMobile(), null, MessageDefine.SMSSENDFORMOBILE, null, CustomConstants.PARAM_TPL_GLYMM, CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);
	}

	/**
	 * 账户更新
	 *
	 * @param record
	 */
	public void updateRecord(AdminBean form) {
		String nowTime = GetDate.getServerDateTime(9, new Date());
		String userId = ShiroUtil.getLoginUserId();
		Admin record = new Admin();
		BeanUtils.copyProperties(form, record);

		record.setUpdatetime(nowTime);
		record.setUpdateuser(userId);
		adminMapper.updateByPrimaryKeySelective(record);

		// 插入用户权限关联表
		if (Validator.isNotNull(form.getRoleId())) {
			AdminAndRoleExample example = new AdminAndRoleExample();
			example.createCriteria().andUserIdEqualTo(record.getId());
			int cnt = adminAndRoleMapper.countByExample(example);
			AdminAndRole adminAndRole = new AdminAndRole();
			adminAndRole.setUserId(record.getId());
			adminAndRole.setRoleId(GetterUtil.getInteger(form.getRoleId()));
			if (cnt == 0) {
				adminAndRoleMapper.insertSelective(adminAndRole);
			} else {
				adminAndRoleMapper.updateByExample(adminAndRole, example);
			}
			if (userId.equals(String.valueOf(record.getId()))) {
				// 更新权限
				ShiroUtil.updateAuth();
			}
		}
	}

	/**
	 * 账户删除
	 *
	 * @param ids
	 */
	public void deleteRecord(List<Integer> ids) {
		String nowTime = GetDate.getServerDateTime(9, new Date());
		String userId = ShiroUtil.getLoginUserId();
		Admin record = new Admin();
		record.setState(AdminDefine.FLG_DISABLE);
		record.setDelFlag(CustomConstants.FLAG_DELETE);
		record.setUpdatetime(nowTime);
		record.setUpdateuser(userId);

		AdminExample example = new AdminExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(ids);
		adminMapper.updateByExampleSelective(record, example);
	}

	/**
	 * 检查手机号码唯一性
	 *
	 * @param id
	 * @param mobile
	 */
	public int countAdminByMobile(Integer id, String mobile) {
		AdminExample example = new AdminExample();
		AdminExample.Criteria criteria = example.createCriteria();
		if (Validator.isNotNull(id)) {
			criteria.andIdNotEqualTo(id);
		}
		criteria.andMobileEqualTo(mobile).andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
		int cnt = adminMapper.countByExample(example);
		return cnt;
	}

	/**
	 * 检查用户名唯一性
	 *
	 * @param id
	 * @param username
	 */
	public int countAdminByUsername(Integer id, String username) {
		AdminExample example = new AdminExample();
		AdminExample.Criteria criteria = example.createCriteria();
		if (Validator.isNotNull(id)) {
			criteria.andIdNotEqualTo(id);
		}
		criteria.andUsernameEqualTo(username);
		criteria.andDelFlagEqualTo("0");// 未删除
		int cnt = adminMapper.countByExample(example);
		return cnt;
	}

	/**
	 * 检查邮箱唯一性
	 *
	 * @param id
	 * @param username
	 */
	public int countAdminByEmail(Integer id, String email) {
		AdminExample example = new AdminExample();
		AdminExample.Criteria criteria = example.createCriteria();
		if (Validator.isNotNull(id)) {
			criteria.andIdNotEqualTo(id);
		}
		criteria.andEmailEqualTo(email).andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
		int cnt = adminMapper.countByExample(example);
		return cnt;
	}

	@Override
	public void resetPwdAction(List<Integer> ids) {

		String nowTime = GetDate.getServerDateTime(9, new Date());
		String userId = ShiroUtil.getLoginUserId();
		String password = GetCode.getRandomPassword(8);

		Admin record = new Admin();

		record.setPassword(MD5.toMD5Code(password));
		record.setUpdatetime(nowTime);
		record.setUpdateuser(userId);

		AdminExample example = new AdminExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(ids);
		adminMapper.updateByExampleSelective(record, example);

		List<Admin> list = adminMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			for (Admin admin : list) {
				// 发送短信
				Map<String, String> replaceStrs = new HashMap<String, String>();
				replaceStrs.put("var_name", admin.getTruename());
				replaceStrs.put("var_yonghuming", admin.getUsername());
				replaceStrs.put("var_htmm", password);
				SmsMessage smsMessage = new SmsMessage(null, replaceStrs, admin.getMobile(), null, MessageDefine.SMSSENDFORMOBILE, null, CustomConstants.PARAM_TPL_RESETPWD, CustomConstants.CHANNEL_TYPE_NORMAL);
				smsProcesser.gather(smsMessage);
			}
		}
	}
}
