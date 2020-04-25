package com.hyjf.mybatis.model.customize;

import java.util.Date;

public class EmployeeDimissionCustomize {
	private static final long serialVersionUID = 1L;
	
	private int limitStart = -1;
	private int limitEnd = -1;
	
	
	//oa_user_leave	员工离职工单日志表
	private int id;
	private String oprocess;
	private int userid;
	private int creater;
	private Date leave_time;
	private Date end_time;
	private int remark;
	private int f_creater;
	private String f_remark;
	private String f_time;
	private int s_creater;
	private String s_remark;
	private String s_time;
	private int q_creater;
	private String q_remark;
	private String q_time;
	private String createtime;
	
	//oa_users	用户表 （员工管理-员工信息）
	private String user_login;
	private String user_pass;
	private String user_realname;
	private String user_email;
	private String idcard;
	private String avatar;
	private int sex;
	private String acc_province;
	private String acc_city;
	private String acc_address;
	private int  departmentid;
	private int  positionid;
	private int  level;
	private int  temporary;
	private String ispart;
	private int  payroll_try;
	private int  payroll;
	private Date entrydate;
	private String reference;
	private int  education;
	private String school;
	private String specialty;
	private String mobile;
	private String last_login_ip;
	private String last_login_time;
	private String create_time;
	private String bank_address;
	private String bank_user;
	private String bank_num;
	private String user_status;
	private int  age;
	private String hyd_name;
	private int hyd_id;
	private int user_type;
	private String name;
	private String manager;
	
	//别名
	private String dname;
	private String pname;
	private String dename;
	private String pename;
	private String two;
	private String seconddepart;
	private String thirddepart;
	private String cityManager;
	private Date entrydatetime;
	private Date enddatetime;
	
	
	//查询条件
	private String truenameSrch;
	private String mobileSrch;
	private String levelSrch;
	private String temporarySrch;
	private String oprocessSrch;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOprocess() {
		return oprocess;
	}
	public void setOprocess(String oprocess) {
		this.oprocess = oprocess;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getCreater() {
		return creater;
	}
	public void setCreater(int creater) {
		this.creater = creater;
	}
	public Date getLeave_time() {
		return leave_time;
	}
	public void setLeave_time(Date leave_time) {
		this.leave_time = leave_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	public int getRemark() {
		return remark;
	}
	public void setRemark(int remark) {
		this.remark = remark;
	}
	public int getF_creater() {
		return f_creater;
	}
	public void setF_creater(int f_creater) {
		this.f_creater = f_creater;
	}
	public String getF_remark() {
		return f_remark;
	}
	public void setF_remark(String f_remark) {
		this.f_remark = f_remark;
	}
	public String getF_time() {
		return f_time;
	}
	public void setF_time(String f_time) {
		this.f_time = f_time;
	}
	public int getS_creater() {
		return s_creater;
	}
	public void setS_creater(int s_creater) {
		this.s_creater = s_creater;
	}
	public String getS_remark() {
		return s_remark;
	}
	public void setS_remark(String s_remark) {
		this.s_remark = s_remark;
	}
	public String getS_time() {
		return s_time;
	}
	public void setS_time(String s_time) {
		this.s_time = s_time;
	}
	public int getQ_creater() {
		return q_creater;
	}
	public void setQ_creater(int q_creater) {
		this.q_creater = q_creater;
	}
	public String getQ_remark() {
		return q_remark;
	}
	public void setQ_remark(String q_remark) {
		this.q_remark = q_remark;
	}
	public String getQ_time() {
		return q_time;
	}
	public void setQ_time(String q_time) {
		this.q_time = q_time;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getUser_login() {
		return user_login;
	}
	public void setUser_login(String user_login) {
		this.user_login = user_login;
	}
	public String getUser_pass() {
		return user_pass;
	}
	public void setUser_pass(String user_pass) {
		this.user_pass = user_pass;
	}
	public String getUser_realname() {
		return user_realname;
	}
	public void setUser_realname(String user_realname) {
		this.user_realname = user_realname;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getAcc_province() {
		return acc_province;
	}
	public void setAcc_province(String acc_province) {
		this.acc_province = acc_province;
	}
	public String getAcc_city() {
		return acc_city;
	}
	public void setAcc_city(String acc_city) {
		this.acc_city = acc_city;
	}
	public String getAcc_address() {
		return acc_address;
	}
	public void setAcc_address(String acc_address) {
		this.acc_address = acc_address;
	}
	public int getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(int departmentid) {
		this.departmentid = departmentid;
	}
	public int getPositionid() {
		return positionid;
	}
	public void setPositionid(int positionid) {
		this.positionid = positionid;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getTemporary() {
		return temporary;
	}
	public void setTemporary(int temporary) {
		this.temporary = temporary;
	}
	public String getIspart() {
		return ispart;
	}
	public void setIspart(String ispart) {
		this.ispart = ispart;
	}
	public int getPayroll_try() {
		return payroll_try;
	}
	public void setPayroll_try(int payroll_try) {
		this.payroll_try = payroll_try;
	}
	public int getPayroll() {
		return payroll;
	}
	public void setPayroll(int payroll) {
		this.payroll = payroll;
	}
	public Date getEntrydate() {
		return entrydate;
	}
	public void setEntrydate(Date entrydate) {
		this.entrydate = entrydate;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public int getEducation() {
		return education;
	}
	public void setEducation(int education) {
		this.education = education;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getLast_login_ip() {
		return last_login_ip;
	}
	public void setLast_login_ip(String last_login_ip) {
		this.last_login_ip = last_login_ip;
	}
	public String getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getBank_address() {
		return bank_address;
	}
	public void setBank_address(String bank_address) {
		this.bank_address = bank_address;
	}
	public String getBank_user() {
		return bank_user;
	}
	public void setBank_user(String bank_user) {
		this.bank_user = bank_user;
	}
	public String getBank_num() {
		return bank_num;
	}
	public void setBank_num(String bank_num) {
		this.bank_num = bank_num;
	}
	public String getUser_status() {
		return user_status;
	}
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getHyd_name() {
		return hyd_name;
	}
	public void setHyd_name(String hyd_name) {
		this.hyd_name = hyd_name;
	}
	public int getHyd_id() {
		return hyd_id;
	}
	public void setHyd_id(int hyd_id) {
		this.hyd_id = hyd_id;
	}
	public int getUser_type() {
		return user_type;
	}
	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getDename() {
		return dename;
	}
	public void setDename(String dename) {
		this.dename = dename;
	}
	public String getPename() {
		return pename;
	}
	public void setPename(String pename) {
		this.pename = pename;
	}
	public String getTwo() {
		return two;
	}
	public void setTwo(String two) {
		this.two = two;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getSeconddepart() {
		return seconddepart;
	}
	public void setSeconddepart(String seconddepart) {
		this.seconddepart = seconddepart;
	}
	public String getThirddepart() {
		return thirddepart;
	}
	public void setThirddepart(String thirddepart) {
		this.thirddepart = thirddepart;
	}
	public String getCityManager() {
		return cityManager;
	}
	public void setCityManager(String cityManager) {
		this.cityManager = cityManager;
	}
	
	public Date getEntrydatetime() {
		return entrydatetime;
	}
	public void setEntrydatetime(Date entrydatetime) {
		this.entrydatetime = entrydatetime;
	}
	public Date getEnddatetime() {
		return enddatetime;
	}
	public void setEnddatetime(Date enddatetime) {
		this.enddatetime = enddatetime;
	}
	public int getLimitStart() {
		return limitStart;
	}
	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}
	public int getLimitEnd() {
		return limitEnd;
	}
	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}
	public String getTruenameSrch() {
		return truenameSrch;
	}
	public void setTruenameSrch(String truenameSrch) {
		this.truenameSrch = truenameSrch;
	}
	public String getMobileSrch() {
		return mobileSrch;
	}
	public void setMobileSrch(String mobileSrch) {
		this.mobileSrch = mobileSrch;
	}
	public String getLevelSrch() {
		return levelSrch;
	}
	public void setLevelSrch(String levelSrch) {
		this.levelSrch = levelSrch;
	}
	public String getTemporarySrch() {
		return temporarySrch;
	}
	public void setTemporarySrch(String temporarySrch) {
		this.temporarySrch = temporarySrch;
	}
	public String getOprocessSrch() {
		return oprocessSrch;
	}
	public void setOprocessSrch(String oprocessSrch) {
		this.oprocessSrch = oprocessSrch;
	}
	
	
	
	

}
