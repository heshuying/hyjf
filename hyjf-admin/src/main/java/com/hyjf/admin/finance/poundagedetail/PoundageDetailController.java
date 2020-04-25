package com.hyjf.admin.finance.poundagedetail;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.admin.finance.poundage.PoundageService;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.config.poundageledger.PoundageLedgerService;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageCustomize;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageLedgerCustomize;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageDetailCustomize;

/**
 * 页面数据：
 * 1、列表数据类型
 * 每周手续费分账的数据，点击详情后，进入此列表，展示一周需要手续费分账的数据，即分账统计时间之前一周的数据
 * 2、列表排序
 * 按放款/还款时间倒序排序，即最新的在前面
 * 3、查询字段
 * （1）订单号：输入框，模糊匹配；
 * （3）项目编号：输入框，模糊匹配；
 * （4）项目类型：下拉列表，单选，选项：全部/所有项目类型，默认：全部；
 * （5）放款/还款时间：时间控件，具体到天；结束值必须大于等于开始值，且时间段最大1个月；默认为空，即表示不限；
 * 4、导出列表
 * 导出字段：序号、订单号、流水号、项目编号、项目类型、出借人、分账类型、出借人分公司、分账来源、分账比例、分账金额、
 * 收款人用户名、收款人姓名、收款人电子帐号、分账状态、实际分账时间、放款/还款时间
 * 注意：分账金额计算时去尾
 * @author Albert
 */
@Controller
@RequestMapping(value = PoundageDetailDefine.REQUEST_MAPPING)
public class PoundageDetailController extends BaseController {

    @Autowired
    private PoundageService poundageService;
    @Autowired
    private PoundageLedgerService poundageLedgerService;
    @Autowired
    private PoundageDetailService poundageDetailService;
    @Autowired
    private BorrowCommonService borrowCommonService;

    /**
     * 查询列表
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PoundageDetailDefine.POUNDAGE_DETAIL_LIST)
    @RequiresPermissions(PoundageDetailDefine.PERMISSIONS_VIEW)
    public ModelAndView search(HttpServletRequest request, PoundageDetailBean form) {
        LogUtil.startLog(PoundageDetailController.class.toString(), PoundageDetailDefine.POUNDAGE_DETAIL_LIST);
        ModelAndView modelAndView = new ModelAndView(PoundageDetailDefine.LIST_PATH);
        // 手续费分账信息
        PoundageCustomize poundageCustomize = poundageService.getPoundageById(form.getPoundageId());
        modelAndView.addObject("poundage", poundageCustomize);
        // 查询明细对应的手续费配置项
        PoundageLedgerCustomize poundageLedgerCustomize = new PoundageLedgerCustomize();
        if(poundageCustomize.getLedgerId()!=null) {
            poundageLedgerCustomize = poundageLedgerService.getPoundageLedgerById(poundageCustomize.getLedgerId());
        }
        modelAndView.addObject("poundageLedger", poundageLedgerCustomize);
        List<BorrowProjectType> projectList = this.borrowCommonService.borrowProjectTypeList(null);
        modelAndView.addObject("projects",projectList);
        // 设置明细查询条件
        form.setLedgerIdSer(poundageCustomize.getLedgerId());
        if(form.getLedgerTimeSer()==null) {
            form.setLedgerTimeSer(Integer.parseInt(poundageCustomize.getPoundageTime()));
        }
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(PoundageDetailController.class.toString(), PoundageDetailDefine.POUNDAGE_DETAIL_LIST);
        return modelAndView;
    }

    /**
     * 分页
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, PoundageDetailBean form) {
        PoundageDetailCustomize poundageDetailCustomize = new PoundageDetailCustomize();
        BeanUtils.copyProperties(form, poundageDetailCustomize);
        Integer count = this.poundageDetailService.getPoundageDetailCount(poundageDetailCustomize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            poundageDetailCustomize.setLimitStart(paginator.getOffset());
            poundageDetailCustomize.setLimitEnd(paginator.getLimit());

            List<PoundageDetailCustomize> customers = this.poundageDetailService.getPoundageDetailList(poundageDetailCustomize);
            form.setPaginator(paginator);
            form.setRecordList(customers);
        }
        modelAndView.addObject(PoundageDetailDefine.POUNDAGE_DETAIL_FORM, form);

    }
    /**
     * 导出手续费明细列表
     *
     * @param request
     * @param response
     * @throws Exception
     * @author wgx
     */
    @RequestMapping(PoundageDetailDefine.POUNDAGE_DETAIL_EXPORT)
    @RequiresPermissions(PoundageDetailDefine.PERMISSIONS_EXPORT)
    public void exportPoundageDetailExcel(HttpServletRequest request, HttpServletResponse response, PoundageDetailBean form)
            throws Exception {
        // 手续费分账信息
        PoundageCustomize poundageCustomize = poundageService.getPoundageById(form.getPoundageId());
        poundageDetailService.exportPoundageDetail(response, poundageCustomize);
    }

}
