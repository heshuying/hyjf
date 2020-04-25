package com.hyjf.admin.exception.assetexception;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowDelete;
import com.hyjf.mybatis.model.customize.admin.AssetExceptionCustomize;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version AssetExceptionCustomize, v0.1 2018/8/2 9:39
 */
public interface AssetExceptionService extends BaseService {

    /**
     * 查询总件数
     *
     * @param assetExceptionCustomize
     * @return
     */
    Integer countBorrowDelete(AssetExceptionCustomize assetExceptionCustomize);

    /**
     * 查询列表
     *
     * @param assetExceptionCustomize
     * @return
     */
    List<AssetExceptionCustomize> selectBorrowDeleteList(AssetExceptionCustomize assetExceptionCustomize);

    /**
     * 删除
     *
     * @param id
     */
    boolean deleteBorrowDeleteById(Integer id);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    BorrowDelete selectBorrowDeleteById(Integer id);

    /**
     * 插入
     *
     * @param form
     */
    boolean insertBorrowDelete(BorrowDelete form);

    /**
     * 更新
     *
     * @param form
     */
    void updateBorrowDelete(BorrowDelete form);

    /**
     * 判断项目编号是否存在
     *
     * @param request
     * @return
     */
    String isExistsBorrow(HttpServletRequest request);

    /**
     * 插入删除标的成功后更新保证金
     *
     * @param instCode
     * @param account
     * @return
     */
    boolean updateAddBail(String instCode, BigDecimal account);

    /**
     * 去除删除标的成功后更新保证金
     *
     * @param instCode
     * @param account
     * @return
     */
    boolean updateSubtractBail(String instCode, BigDecimal account);
}
