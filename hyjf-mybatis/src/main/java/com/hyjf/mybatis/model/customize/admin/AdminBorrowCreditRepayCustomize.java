package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.CreditRepay;

@SuppressWarnings("serial")
public class AdminBorrowCreditRepayCustomize  implements Serializable {
    private CreditRepay creditRepay;
    private Borrow borrow;
    /*--------add by START---------*/
    private String sumAssignCapital;
    private String sumAssignInterest;
    private String sumAssignAccount;
    private String sumManageFee;
    /*--------add by END---------*/
    
    public CreditRepay getCreditRepay() {
        return creditRepay;
    }
    public void setCreditRepay(CreditRepay creditRepay) {
        this.creditRepay = creditRepay;
    }
    public Borrow getBorrow() {
        return borrow;
    }
    public void setBorrow(Borrow borrow) {
        this.borrow = borrow;
    }
	/**
	 * sumAssignCapital
	 * @return the sumAssignCapital
	 */
		
	public String getSumAssignCapital() {
		return sumAssignCapital;
			
	}
	/**
	 * @param sumAssignCapital the sumAssignCapital to set
	 */
		
	public void setSumAssignCapital(String sumAssignCapital) {
		this.sumAssignCapital = sumAssignCapital;
			
	}
	/**
	 * sumAssignInterest
	 * @return the sumAssignInterest
	 */
		
	public String getSumAssignInterest() {
		return sumAssignInterest;
			
	}
	/**
	 * @param sumAssignInterest the sumAssignInterest to set
	 */
		
	public void setSumAssignInterest(String sumAssignInterest) {
		this.sumAssignInterest = sumAssignInterest;
			
	}
	/**
	 * sumAssignAccount
	 * @return the sumAssignAccount
	 */
		
	public String getSumAssignAccount() {
		return sumAssignAccount;
			
	}
	/**
	 * @param sumAssignAccount the sumAssignAccount to set
	 */
		
	public void setSumAssignAccount(String sumAssignAccount) {
		this.sumAssignAccount = sumAssignAccount;
			
	}
	/**
	 * sumManageFee
	 * @return the sumManageFee
	 */
		
	public String getSumManageFee() {
		return sumManageFee;
			
	}
	/**
	 * @param sumManageFee the sumManageFee to set
	 */
		
	public void setSumManageFee(String sumManageFee) {
		this.sumManageFee = sumManageFee;
			
	} 
}




	