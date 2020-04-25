package com.hyjf.admin.coupon.checklist;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.CouponCheck;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @author lisheng
 * @version CouponCheckService, v0.1 2018/6/6 16:30
 */

public interface CouponCheckService {

    public  int countCouponCheck(CouponCheckBean form);

    public List<CouponCheck> searchCouponCheck(CouponCheckBean form, int limitStart, int limitEnd);

    public boolean deleteMessage(String id);

    public String uploadFile(HttpServletRequest request, HttpServletResponse response);

    public void download(String path, HttpServletResponse response);

    public boolean batchCheck(String path, HttpServletResponse response) throws Exception;

    public boolean updateCoupon(CouponCheckBean form);

    /**
     * 根据ID查询 CouponCheck
     * @param id
     * @return
     */
	public CouponCheck getById(String id);
}
