package com.hyjf.web.bank.web.user.withdraw;

public class WithdrawBean {
    private String idcard;
    
    private String bankFee;
    
    private String bankAccount;

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getBankFee() {
        return bankFee;
    }

    public void setBankFee(String bankFee) {
        this.bankFee = bankFee;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
    
}