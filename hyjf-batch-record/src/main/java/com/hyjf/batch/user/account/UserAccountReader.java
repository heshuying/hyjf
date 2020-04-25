package com.hyjf.batch.user.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * 批量读取数据
 * @author HP
 *
 */
@Component("userAccountReader")
public class UserAccountReader implements RowMapper<UserAccountBean> {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserAccountReader.class);  
	
	public UserAccountReader(){
		LOG.info("批量开户读取数据...");
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param rs
	 * @param rowNum
	 * @return
	 * @throws SQLException
	 * @author Michael
	 */
		
	@Override
	public UserAccountBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		UserAccountBean readBean = new UserAccountBean();
		readBean.setUserId(rs.getString("userId"));
		readBean.setIdCard(rs.getString("idCard"));
		readBean.setName(rs.getString("name"));
		readBean.setSex(rs.getString("sex"));
		readBean.setMobile(rs.getString("mobile"));
		readBean.setEmail(rs.getString("email"));
		return readBean;
			
	}

}
