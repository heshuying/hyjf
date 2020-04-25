package com.hyjf.admin.exception.assetexception;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.maintenance.admin.AdminDefine;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.admin.AssetExceptionCustomize;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigCustomize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version AssetExceptionCustomize, v0.1 2018/8/2 9:39
 */
@Service
public class AssetExceptionServiceImpl extends BaseServiceImpl implements AssetExceptionService {

    @Override
    public Integer countBorrowDelete(AssetExceptionCustomize assetExceptionCustomize) {
        return this.assetExceptionCustomizeMapper.countBorrowDelete(assetExceptionCustomize);
    }

    @Override
    public List<AssetExceptionCustomize> selectBorrowDeleteList(AssetExceptionCustomize assetExceptionCustomize) {
        return this.assetExceptionCustomizeMapper.selectBorrowDeleteList(assetExceptionCustomize);
    }

    @Override
    public boolean deleteBorrowDeleteById(Integer id) {
        return this.borrowDeleteMapper.deleteByPrimaryKey(id) > 0 ? true : false;
    }

    @Override
    public BorrowDelete selectBorrowDeleteById(Integer id) {
        return this.borrowDeleteMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean insertBorrowDelete(BorrowDelete form) {
        // 根据项目编号获取借款信息
        Borrow borrow = this.getBorrowByNid(form.getBorrowNid());
        if (null == borrow) {
            return false;
        }
        form.setAccount(borrow.getAccount());
        form.setStatus(borrow.getStatus());
        form.setInstCode(borrow.getInstCode());

        return this.borrowDeleteMapper.insertSelective(form) > 0 ? true : false;
    }

    @Override
    public void updateBorrowDelete(BorrowDelete form) {
        BorrowDelete borrowDelete = new BorrowDelete();
        borrowDelete.setId(form.getId());
        borrowDelete.setExceptionType(form.getExceptionType());
        borrowDelete.setExceptionRemark(form.getExceptionRemark());
        borrowDelete.setExceptionTime(form.getExceptionTime());
        this.borrowDeleteMapper.updateByPrimaryKeySelective(borrowDelete);
    }

    @Override
    public String isExistsBorrow(HttpServletRequest request) {

        JSONObject ret = new JSONObject();
        String message = ValidatorFieldCheckUtil.getErrorMessage("required", "");
        message = message.replace("{label}", "项目编号");

        String param = request.getParameter("param");
        if (StringUtils.isEmpty(param)) {
            return ret.toJSONString();
        }

        Borrow borrow = this.getBorrowByNid(param);
        if (null == borrow) {
            message = ValidatorFieldCheckUtil.getErrorMessage("borrownid.not.exists", "");
            ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
            return ret.toString();
        }
        if (borrow.getStatus() != 0 && borrow.getStatus() != 1) {
            message = ValidatorFieldCheckUtil.getErrorMessage("borrownid.cannot.add", "");
            ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
            return ret.toString();
        }
        // 判断是否已经添加到异常表中
        BorrowDeleteExample example = new BorrowDeleteExample();
        example.createCriteria().andBorrowNidEqualTo(borrow.getBorrowNid());
        List<BorrowDelete> borrowDeleteList = this.borrowDeleteMapper.selectByExample(example);
        if (null != borrowDeleteList && borrowDeleteList.size()>0){
            message = ValidatorFieldCheckUtil.getErrorMessage("borrownid.delete.exists", "");
            ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
            return ret.toString();
        }

        ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);

        return ret.toJSONString();
    }

    private HjhBailConfig selectHjhBailConfigByExample(String instCode) {
        HjhBailConfigExample example = new HjhBailConfigExample();
        example.createCriteria().andInstCodeEqualTo(instCode).andDelFlgEqualTo(0);
        List<HjhBailConfig> hjhBailConfigList = this.hjhBailConfigMapper.selectByExample(example);
        if (null != hjhBailConfigList && hjhBailConfigList.size() > 0) {
            return hjhBailConfigList.get(0);
        }
        return null;
    }

    /**
     * 插入删除标的成功后更新保证金
     *
     * @param instCode
     * @param account
     * @return
     */
    @Override
    public boolean updateAddBail(String instCode, BigDecimal account) {
        // 获取该机构保证金配置
        HjhBailConfig hjhBailConfig = this.selectHjhBailConfigByExample(instCode);
        if (null != hjhBailConfig) {
            // 发标已发额度
            hjhBailConfig.setLoanMarkLine(hjhBailConfig.getLoanMarkLine().subtract(account));
            // loan_balance在贷余额
            hjhBailConfig.setLoanBalance(hjhBailConfig.getLoanBalance().subtract(account));
            // 发标额度余额remain_mark_line
            hjhBailConfig.setRemainMarkLine(hjhBailConfig.getRemainMarkLine().add(account));

            // 周期内发标已发额度
            BigDecimal sendedAccountByCycBD = BigDecimal.ZERO;
            String sendedAccountByCyc = this.selectSendedAccountByCyc(instCode, hjhBailConfig.getTimestart(), hjhBailConfig.getTimeend());
            if (StringUtils.isNotBlank(sendedAccountByCyc)) {
                sendedAccountByCycBD = new BigDecimal(sendedAccountByCyc);
            }
            hjhBailConfig.setCycLoanTotal(sendedAccountByCycBD);

            return this.hjhBailConfigMapper.updateByPrimaryKey(hjhBailConfig) > 0 ? true : false;
        } else {
            return false;
        }
    }

    /**
     * 获取周期内发标已发额度
     *
     * @param instCode
     * @return
     */
    private String selectSendedAccountByCyc(String instCode, String timeStart, String timeEnd) {
        HjhBailConfigCustomize hjhBailConfigCustomize = new HjhBailConfigCustomize();
        hjhBailConfigCustomize.setInstCode(instCode);
        hjhBailConfigCustomize.setTimestart(timeStart);
        hjhBailConfigCustomize.setTimeend(timeEnd);
        return this.hjhBailConfigCustomizeMapper.selectSendedAccountByCyc(hjhBailConfigCustomize);
    }

    /**
     * 去除删除标的成功后更新保证金
     *
     * @param instCode
     * @param account
     * @return
     */
    @Override
    public boolean updateSubtractBail(String instCode, BigDecimal account) {
        // 获取该机构保证金配置
        HjhBailConfig hjhBailConfig = this.selectHjhBailConfigByExample(instCode);
        if (null != hjhBailConfig) {
            // 发标已发额度
            hjhBailConfig.setLoanMarkLine(hjhBailConfig.getLoanMarkLine().add(account));
            // 周期内发标已发额度
            BigDecimal sendedAccountByCycBD = BigDecimal.ZERO;
            String sendedAccountByCyc = this.selectSendedAccountByCyc(instCode,hjhBailConfig.getTimestart(),hjhBailConfig.getTimeend());
            if (StringUtils.isNotBlank(sendedAccountByCyc)) {
                sendedAccountByCycBD = new BigDecimal(sendedAccountByCyc);
            }
            hjhBailConfig.setCycLoanTotal(sendedAccountByCycBD);
            // loan_balance在贷余额
            hjhBailConfig.setLoanBalance(hjhBailConfig.getLoanBalance().add(account));
            // 发标额度余额remain_mark_line
            hjhBailConfig.setRemainMarkLine(hjhBailConfig.getRemainMarkLine().subtract(account));
            return this.hjhBailConfigMapper.updateByPrimaryKey(hjhBailConfig) > 0 ? true : false;
        } else {
            return false;
        }
    }
}
