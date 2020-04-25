/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Michael
 * @version: 1.0
 * Created at: 2015年12月2日 下午4:31:47
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.admin.htl.dafei;

/**
 * @author Michael
 */

public class ConsumeInfo {
	
	private String Person_Name;//	客户姓名
	private String Ident;//		身份证号
	private String Contract_No;//	合同号
	private String Loan_Date;//	贷款日期
	private int Credit_Amount;//贷款金额
	private int price;//	商品价格
	private int Init_Pay;//	首付
	private int Instalment_Num;//	期数
	private String Mobile;//手机号
	private String Ident_Exp;//	身份证过期日
	private String Ident_Auth;//发证机关
	private String Sex;//性别
	private String Bank_Name;//	开户行
	private String Account_No;//	银行帐号
	private int Income;	//	月收入
	private String Address;//地址
	private String Company;//公司
	
	public String getPerson_Name() {
		return Person_Name;
	}
	public void setPerson_Name(String person_Name) {
		Person_Name = person_Name;
	}
	public String getIdent() {
		return Ident;
	}
	public void setIdent(String ident) {
		Ident = ident;
	}
	public String getContract_No() {
		return Contract_No;
	}
	public void setContract_No(String contract_No) {
		Contract_No = contract_No;
	}
	public String getLoan_Date() {
		return Loan_Date;
	}
	public void setLoan_Date(String loan_Date) {
		Loan_Date = loan_Date;
	}
	public int getCredit_Amount() {
		return Credit_Amount;
	}
	public void setCredit_Amount(int credit_Amount) {
		Credit_Amount = credit_Amount;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getInit_Pay() {
		return Init_Pay;
	}
	public void setInit_Pay(int init_Pay) {
		Init_Pay = init_Pay;
	}
	public int getInstalment_Num() {
		return Instalment_Num;
	}
	public void setInstalment_Num(int instalment_Num) {
		Instalment_Num = instalment_Num;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getIdent_Exp() {
		return Ident_Exp;
	}
	public void setIdent_Exp(String ident_Exp) {
		Ident_Exp = ident_Exp;
	}
	public String getIdent_Auth() {
		return Ident_Auth;
	}
	public void setIdent_Auth(String ident_Auth) {
		Ident_Auth = ident_Auth;
	}
	public String getSex() {
		return Sex;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public String getBank_Name() {
		return Bank_Name;
	}
	public void setBank_Name(String bank_Name) {
		Bank_Name = bank_Name;
	}
	public String getAccount_No() {
		return Account_No;
	}
	public void setAccount_No(String account_No) {
		Account_No = account_No;
	}
	public int getIncome() {
		return Income;
	}
	public void setIncome(int income) {
		Income = income;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getCompany() {
		return Company;
	}
	public void setCompany(String company) {
		Company = company;
	}
	
}