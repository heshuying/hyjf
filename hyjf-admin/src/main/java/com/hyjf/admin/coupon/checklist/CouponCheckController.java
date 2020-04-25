package com.hyjf.admin.coupon.checklist;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.CouponCheck;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @author lisheng
 * @version CouponCheckController, v0.1 2018/6/6 15:50
 */
@Controller
@RequestMapping(value = CouponCheckDefine.REQUEST_MAPPING)
public class CouponCheckController {
    @Autowired
    CouponCheckService couponCheckService;

    private static final String THIS_CLASS = CouponCheckController.class.getName();

    Logger _log = LoggerFactory.getLogger(CouponCheckController.class);

    /**
     * 转跳初始化界面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = CouponCheckDefine.INIT)
    public ModelAndView init(HttpServletRequest request, CouponCheckBean form) {
        ModelAndView modelAndView = new ModelAndView(CouponCheckDefine.LIST_PATH);
        createPage(request, modelAndView, form);
        return modelAndView;
    }


    /**
     * 分页技能维护
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, CouponCheckBean form) {
        // 数量
        int count = couponCheckService.countCouponCheck(form);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            List<CouponCheck> couponChecks = couponCheckService.searchCouponCheck(form, paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(couponChecks);
        }
        modelAndView.addObject(CouponCheckDefine.FORM, form);
    }

    /**
     * 删除信息
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CouponCheckDefine.DELETE_ACTION)
    public ModelAndView deleteMessage(HttpServletRequest request, CouponCheckBean form) {
        ModelAndView modelAndView = new ModelAndView("redirect:" + CouponCheckDefine.REQUEST_MAPPING + "/" + CouponCheckDefine.INIT);
        this.createPage(request, modelAndView, form);
        String ids = form.getId();
        String[] split = ids.split(",");
        for (String id : split) {
            try {
                this.couponCheckService.deleteMessage(id);
            } catch (Exception e) {
                _log.info(THIS_CLASS + "==>" + "删除失败（id）：" + id);
            }
        }
        return modelAndView;
    }

    /**
     * 画面迁移到上传界面
     */
    @RequestMapping(CouponCheckDefine.IMP_DISTRIBUTEVIEW_ACTION)
    public ModelAndView impDistributeViewAction(HttpServletRequest request, RedirectAttributes attr, CouponCheckBean form) {

        ModelAndView modelAndView = new ModelAndView(CouponCheckDefine.IMP_DISTRIBUTE_PATH);
        return modelAndView;
    }

    /**
     * 上传
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = CouponCheckDefine.UPLOAD_ACTION, method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(CouponCheckController.class.toString(), CouponCheckDefine.UPLOAD_ACTION);
        String files = couponCheckService.uploadFile(request, response);
        LogUtil.endLog(CouponCheckController.class.toString(), CouponCheckDefine.UPLOAD_ACTION);
        return files;
    }

    /**
     * 下载文件
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = CouponCheckDefine.DOWNLOAD_ACTION)
    @ResponseBody
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, CouponCheckBean form) throws Exception {
        LogUtil.startLog(CouponCheckController.class.toString(), CouponCheckDefine.UPLOAD_ACTION);
        String id = form.getId();
        couponCheckService.download(id, response);
        LogUtil.endLog(CouponCheckController.class.toString(), CouponCheckDefine.UPLOAD_ACTION);
    }

    /**
     * 转跳审核优惠券页面
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = CouponCheckDefine.AUDIT_INIT_ACTION)
    @ResponseBody
    public ModelAndView checkFile(HttpServletRequest request, HttpServletResponse response, CouponCheckBean form) throws Exception {
        LogUtil.startLog(CouponCheckController.class.toString(), CouponCheckDefine.UPLOAD_ACTION);
        ModelAndView modelAndView = new ModelAndView(CouponCheckDefine.COUPON_PATH);
        modelAndView.addObject(CouponCheckDefine.FORM, form);
        LogUtil.endLog(CouponCheckController.class.toString(), CouponCheckDefine.UPLOAD_ACTION);
        return modelAndView;
    }

    /**
     * 审核优惠券
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = CouponCheckDefine.AUDIT_ACTION)
    @ResponseBody
    public JSONObject checkCoupon(HttpServletRequest request, HttpServletResponse response, CouponCheckBean form) throws Exception {
        LogUtil.startLog(CouponCheckController.class.toString(), CouponCheckDefine.AUDIT_ACTION);
        JSONObject result = new JSONObject();
        boolean results = false;
        String mark = form.getMark();
        if (StringUtils.equals(form.getStatus(), "2")) {//审核通过
        	String path = form.getId();
        	String[] split = path.split(",");
            String id = split[0];
            CouponCheck couponCheck = couponCheckService.getById(id);
            if(1 == couponCheck.getStatus()){
            	
            	//审核状态设置为4，为临时状态-审核中，优惠券发送完成之后更改为审核成功，状态为2.
            	form.setStatus("4");
            	results = couponCheckService.updateCoupon(form);
            	boolean flag = couponCheckService.batchCheck(form.getId(), response);
            	if (flag) {
            		//优惠券发送完成，更改状态为 2  审核完成
            		form.setStatus("2");
            		results = couponCheckService.updateCoupon(form);
            	}else{
            		result.put("success", false);
                    result.put("msg", "审核异常，请检查上传的Excel文件！");
                    return result;
            	}
            }else{
            	result.put("success", false);
                result.put("msg", "请勿重复审核！");
                return result;
            }
        } else if (StringUtils.isNotBlank(mark) && mark.length() <= 20) {//审核不通过需要填写备注,备注20字以内
            results = couponCheckService.updateCoupon(form);
        } else {
            result.put("success", false);
            result.put("msg", "审核不通过需要填写备注,备注20字以内");
            return result;
        }
        if (results) {
            result.put("success", true);
            result.put("msg", "审核成功,正在发放优惠券");
        } else {
            result.put("success", false);
            result.put("msg", "审核失败");
        }
        return result;
    }


}
