package com.hyjf.web.api.base;

/**
 * <p>
 * BaseBean
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
public class ApiBaseBean {
    
    /**
     * 随机字符串
     */
    private String randomString;
    /**
     * 安全码
     */
    private String secretKey;
    
    /**
     * 验签
     */
    private String chkValue;
    
    /**
     * 分页码
     */
    private Integer page = 1;
    
    /**
     * 当前页码
     */
    private Integer pageSize = 10;
    
    /**
     * 用户编号
     */
    private Integer userId;
    
    /**
     * 当前时间戳（10位）
     */
    private Integer timestamp;
    

    public String getRandomString() {
        return randomString;
    }

    public void setRandomString(String randomString) {
        this.randomString = randomString;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

	public String getChkValue() {
		return chkValue;
	}

	public void setChkValue(String chkValue) {
		this.chkValue = chkValue;
	}

	public Integer getUserId() {
		return userId;
	}

	public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}
    
}
