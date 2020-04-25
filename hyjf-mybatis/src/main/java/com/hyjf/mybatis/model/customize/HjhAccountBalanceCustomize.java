/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.hyjf.mybatis.model.customize;

import java.util.Date;

import com.hyjf.common.util.PropUtils;



public class HjhAccountBalanceCustomize  implements java.io.Serializable{
	public static final long serialVersionUID = 5454155825314635342L;
	private String dataFormt;
	public String getDataFormt() {
		return dataFormt;
	}

	public void setDataFormt(String dataFormt) {
		this.dataFormt = dataFormt;
	}

	//alias
	public static final String TABLE_ALIAS = "HjhAccountBalance";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_DATE = "日期";
	public static final String ALIAS_INVEST_ACCOUNT = "原始资产交易额(元)";
	public static final String ALIAS_CREDIT_ACCOUNT = "债转资产交易额(元)";
	public static final String ALIAS_REINVEST_ACCOUNT = "复出借金额(元)";
	public static final String ALIAS_ADD_ACCOUNT = "新加入资金额(元)";
	public static final String ALIAS_CREATE_USER = "创建人id";
	public static final String ALIAS_CREATE_TIME = "创建时间";
	public static final String ALIAS_UPDATE_USER = "更新人id";
	public static final String ALIAS_UPDATE_TIME = "更新时间";
	public static final String ALIAS_DEL_FLG = "删除标识";
	
	/**
	 * 检索条件 添加时间开始
	 */
	private String addTimeStart;

	/**
	 * 检索条件 添加时间结束
	 */
	private String addTimeEnd;
 
	//查询用变量

	private Date yuechu;
	private Date yuemo;

	public Date getYuechu() {
		return yuechu;
	}

	public void setYuechu(Date yuechu) {
		this.yuechu = yuechu;
	}

	public Date getYuemo() {
		return yuemo;
	}

	public void setYuemo(Date yuemo) {
		this.yuemo = yuemo;
	}

	
	public int limitStart = -1;
	public int limitEnd = -1;
	
    /**
     * 主键       db_column: id 
     */	
	public java.lang.Integer id;
    /**
     * 日期       db_column: date 
     */	
	public java.sql.Date date;
    /**
     * 原始资产交易额(元)       db_column: invest_account 
     */	
	public java.math.BigDecimal investAccount;
    /**
     * 债转资产交易额(元)       db_column: credit_account 
     */	
	public java.math.BigDecimal creditAccount;
    /**
     * 复出借金额(元)       db_column: reinvest_account 
     */	
	public java.math.BigDecimal reinvestAccount;
    /**
     * 新加入资金额(元)       db_column: add_account 
     */	
	public java.math.BigDecimal addAccount;
    /**
     * 创建人id       db_column: create_user 
     */	
	public java.lang.Integer createUser;
    /**
     * 创建时间       db_column: create_time 
     */	
	public java.lang.Integer createTime;
    /**
     * 更新人id       db_column: update_user 
     */	
	public java.lang.Integer updateUser;
    /**
     * 更新时间       db_column: update_time 
     */	
	public java.lang.Integer updateTime;
    /**
     * 删除标识       db_column: del_flg 
     */	
	public java.lang.Boolean delFlg;
	
    /**
     * 主键查询条件  
     */ 
	public java.lang.Integer idSer;
    /**
     * 日期查询条件  
     */ 
	public java.sql.Date dateSer;
    /**
     * 原始资产交易额(元)查询条件  
     */ 
	public java.math.BigDecimal investAccountSer;
    /**
     * 债转资产交易额(元)查询条件  
     */ 
	public java.math.BigDecimal creditAccountSer;
    /**
     * 复出借金额(元)查询条件  
     */ 
	public java.math.BigDecimal reinvestAccountSer;
    /**
     * 新加入资金额(元)查询条件  
     */ 
	public java.math.BigDecimal addAccountSer;
    /**
     * 创建人id查询条件  
     */ 
	public java.lang.Integer createUserSer;
    /**
     * 创建时间查询条件  
     */ 
	public java.lang.Integer createTimeSer;
    /**
     * 更新人id查询条件  
     */ 
	public java.lang.Integer updateUserSer;
    /**
     * 更新时间查询条件  
     */ 
	public java.lang.Integer updateTimeSer;
    /**
     * 删除标识查询条件  
     */ 
	public java.lang.Boolean delFlgSer;
	//columns END
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setDate(java.sql.Date value) {
		this.date = value;
	}
	
	public java.sql.Date getDate() {
		return this.date;
	}
	public void setInvestAccount(java.math.BigDecimal value) {
		this.investAccount = value;
	}
	
	public java.math.BigDecimal getInvestAccount() {
		return this.investAccount;
	}
	public void setCreditAccount(java.math.BigDecimal value) {
		this.creditAccount = value;
	}
	
	public java.math.BigDecimal getCreditAccount() {
		return this.creditAccount;
	}
	public void setReinvestAccount(java.math.BigDecimal value) {
		this.reinvestAccount = value;
	}
	
	public java.math.BigDecimal getReinvestAccount() {
		return this.reinvestAccount;
	}
	public void setAddAccount(java.math.BigDecimal value) {
		this.addAccount = value;
	}
	
	public java.math.BigDecimal getAddAccount() {
		return this.addAccount;
	}
	public void setCreateUser(java.lang.Integer value) {
		this.createUser = value;
	}
	
	public java.lang.Integer getCreateUser() {
		return this.createUser;
	}
	public void setCreateTime(java.lang.Integer value) {
		this.createTime = value;
	}
	
	public java.lang.Integer getCreateTime() {
		return this.createTime;
	}
	public void setUpdateUser(java.lang.Integer value) {
		this.updateUser = value;
	}
	
	public java.lang.Integer getUpdateUser() {
		return this.updateUser;
	}
	public void setUpdateTime(java.lang.Integer value) {
		this.updateTime = value;
	}
	
	public java.lang.Integer getUpdateTime() {
		return this.updateTime;
	}
	public void setDelFlg(java.lang.Boolean value) {
		this.delFlg = value;
	}
	
	public java.lang.Boolean getDelFlg() {
		return this.delFlg;
	}
        public void setIdSer(java.lang.Integer value) {
            this.idSer = value;
        }
        
        public java.lang.Integer getIdSer() {
            return this.idSer;
        }
        public void setDateSer(java.sql.Date value) {
            this.dateSer = value;
        }
        
        public java.sql.Date getDateSer() {
            return this.dateSer;
        }
        public void setInvestAccountSer(java.math.BigDecimal value) {
            this.investAccountSer = value;
        }
        
        public java.math.BigDecimal getInvestAccountSer() {
            return this.investAccountSer;
        }
        public void setCreditAccountSer(java.math.BigDecimal value) {
            this.creditAccountSer = value;
        }
        
        public java.math.BigDecimal getCreditAccountSer() {
            return this.creditAccountSer;
        }
        public void setReinvestAccountSer(java.math.BigDecimal value) {
            this.reinvestAccountSer = value;
        }
        
        public java.math.BigDecimal getReinvestAccountSer() {
            return this.reinvestAccountSer;
        }
        public void setAddAccountSer(java.math.BigDecimal value) {
            this.addAccountSer = value;
        }
        
        public java.math.BigDecimal getAddAccountSer() {
            return this.addAccountSer;
        }
        public void setCreateUserSer(java.lang.Integer value) {
            this.createUserSer = value;
        }
        
        public java.lang.Integer getCreateUserSer() {
            return this.createUserSer;
        }
        public void setCreateTimeSer(java.lang.Integer value) {
            this.createTimeSer = value;
        }
        
        public java.lang.Integer getCreateTimeSer() {
            return this.createTimeSer;
        }
        public void setUpdateUserSer(java.lang.Integer value) {
            this.updateUserSer = value;
        }
        
        public java.lang.Integer getUpdateUserSer() {
            return this.updateUserSer;
        }
        public void setUpdateTimeSer(java.lang.Integer value) {
            this.updateTimeSer = value;
        }
        
        public java.lang.Integer getUpdateTimeSer() {
            return this.updateTimeSer;
        }
        public void setDelFlgSer(java.lang.Boolean value) {
            this.delFlgSer = value;
        }
        
        public java.lang.Boolean getDelFlgSer() {
            return this.delFlgSer;
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

		public String getAddTimeStart() {
			return addTimeStart;
		}

		public void setAddTimeStart(String addTimeStart) {
			this.addTimeStart = addTimeStart;
		}

		public String getAddTimeEnd() {
			return addTimeEnd;
		}

		public void setAddTimeEnd(String addTimeEnd) {
			this.addTimeEnd = addTimeEnd;
		}
       
}

