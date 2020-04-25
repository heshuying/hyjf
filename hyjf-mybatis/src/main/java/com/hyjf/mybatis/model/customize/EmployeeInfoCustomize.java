package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.util.Date;

public class EmployeeInfoCustomize implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int limitStart = -1;
	private int limitEnd = -1;
	
	//oa_users用户表中所有数据
	private Integer id; //主键
	private String user_login; //用户名
	private String user_pass; //登录密码；oa_password加密
	private String user_realname; //姓名
	private String user_email; //登录邮箱
	private String idcard; //身份证号
	private String avatar; //用户头像，默认null
	private int sex; //性别；0：保密，1：男；2：女，默认0
	private String acc_province; //户口省份
	private String acc_city; //户口城市
	private String acc_address; //户口所在地址
	private int  departmentid; //部门
	private int  positionid; //岗位名称
	private int  level; //角色等级: 2-员工/1-主管
	private int  temporary; //1,兼职  2,正式员工，默认2
	private String ispart; //Y/N
	private int  payroll_try; //试用期月薪，默认null
	private int  payroll; //转正月薪，默认null
	private Date entrydate; //入职时间
	private String reference; //入职推荐人
	private int  education; //学历
	private String school; //毕业院校
	private String specialty; //专业
	private String mobile; //手机
	private String last_login_ip; //最后登录ip
	private String last_login_time; //最后登录时间
	private String create_time; //注册时间
	private String bank_address; //开户行地址
	private String bank_user; //开户人姓名。默认null
	private String bank_num; //银行卡账号。默认null
	private String user_status; //用户状态 E1:一级未审核;E2二级未审核 1：正常 ；2：未验证 。默认E
	private int  age; //年龄
	private String hyd_name; //平台账号
	private int hyd_id; //汇盈贷账号ID
	private int user_type; //用户类型，1:admin ;2:会员。默认2
	
	//查询用变量
	private String truenameSrch;
	private String mobileSrch;
	private String levelSrch;
	private String temporarySrch;
	
	//别名
	private String seconddepart; //二级分部名称
	private String thirddepart;  //三级分部名称
	private String cityManager;  //城市经理
	private String pname; //岗位名称
	private String enterdate; //入职时间
	private String firstdepart;
	private String enterdatedetail;
	private int cityid;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getEnterdate() {
		return enterdate;
	}
	public void setEnterdate(String enterdate) {
		this.enterdate = enterdate;
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
	public String getFirstdepart() {
		return firstdepart;
	}
	public void setFirstdepart(String firstdepart) {
		this.firstdepart = firstdepart;
	}
	public String getEnterdatedetail() {
		return enterdatedetail;
	}
	public void setEnterdatedetail(String enterdatedetail) {
		this.enterdatedetail = enterdatedetail;
	}
	public int getCityid() {
		return cityid;
	}
	public void setCityid(int cityid) {
		this.cityid = cityid;
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
	
	
	
}
