package com.hyjf.admin.coupon.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.customize.coupon.CouponUserCustomize;

/**
 * service接口定义
 */
public interface CouponUserService extends BaseService {

	/**
	 * 获取用户的优惠券列表
	 * @return
	 */
	public List<CouponUserCustomize> getRecordList(Map paraMap);

	/**
	 * 获得记录数
	 * @param CouponConfigCustomize
	 * @return
	 */
	public Integer countRecord(Map paraMap);
	
	/**
	 * 
	 * 根据id删除一条记录
	 * @author hsy
	 * @param recordId
	 */
	public void deleteRecord(Integer recordId, String remark);

	/**
	 * 
	 * 增加一条优惠券给用户
	 * @author hsy
	 * @param couponUserBean
	 */
    public void insertRecord(CouponUserBean couponUserBean);

    /**
     * 
     * 根据优惠券编码查询优惠券详情
     * @author hsy
     * @param couponCode
     * @return
     */
    public CouponConfig selectConfigByCode(String couponCode);

    /**
     * 
     * 根据用户优惠券id查询用户优惠券详情
     * @author pcc
     * @param couponUserId
     * @return
     */
    public CouponUser selectCouponUserById(String couponUserId);
    
    /**
     * 
     * 用户优惠券审批
     * @author pcc
     * @param couponUserId
     * @return
     */
    public void auditRecord(CouponUserBean form, CouponConfig config, Integer couponUserId);
    
    /**
	 * 手动批量发券上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;
	

}