package com.hyjf.admin.manager.config.underlinerecharge;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.UnderLineRecharge;
import com.hyjf.mybatis.model.customize.AdminSystem;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = UnderLineRechargeDefine.UNDERLINERECHARGE_MAPPING)
public class UnderLineRechargeController extends BaseController {

    @Autowired
    private UnderLineRechargeService underLineRechargeService;

    @RequestMapping(UnderLineRechargeDefine.INIT)
    @RequiresPermissions(UnderLineRechargeDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, @ModelAttribute(UnderLineRechargeDefine.UNDERLINERECHARGE_FORM) UnderLineRechargeBean form){
        //开始日志
        LogUtil.startLog(UnderLineRechargeController.class.toString(), UnderLineRechargeDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(UnderLineRechargeDefine.LIST_PATH);

        //创建分页
        this.createPage(request, modelAndView, form);

        //结束日志
        LogUtil.endLog(UnderLineRechargeController.class.toString(), UnderLineRechargeDefine.INIT);
        return modelAndView;
    }


    /**
     * 含有ID时更新
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(UnderLineRechargeDefine.INFO_ACTION)
    public ModelAndView info(HttpServletRequest request, UnderLineRechargeBean form){
        //开始日志
        LogUtil.startLog(UnderLineRechargeController.class.toString(), UnderLineRechargeDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(UnderLineRechargeDefine.INFO_PATH);

        UnderLineRecharge underLineRecharge = new UnderLineRecharge();

        if (StringUtils.isNotEmpty(form.getIds())){
            underLineRecharge = this.underLineRechargeService.getUnderLineRechargeListInfo(form.getIds());
        }

        BeanUtils.copyProperties(underLineRecharge, form);
        modelAndView.addObject(UnderLineRechargeDefine.UNDERLINERECHARGE_FORM, form);

        //日志结束
        LogUtil.endLog(UnderLineRechargeController.class.toString(), UnderLineRechargeDefine.INFO_ACTION);
        return modelAndView;
    }

    private void createPage(HttpServletRequest request, ModelAndView modelAndView, UnderLineRechargeBean form) {
        int count = this.underLineRechargeService.countUnderLineRecharList(form);

        if (count > 0){
            List<UnderLineRecharge> underLineRechargeList = this.underLineRechargeService.searchUnderLineRechargeList(form);
            form.setUnderLineRechargeList(underLineRechargeList);
        }

        modelAndView.addObject(UnderLineRechargeDefine.UNDERLINERECHARGE_FORM, form);
    }


    /**
     * 添加数据
     * @param form
     * @return
     * @Author : huanghui
     */
    @RequestMapping(value = UnderLineRechargeDefine.INSERT_ACTION)
    @RequiresPermissions(UnderLineRechargeDefine.PERMISSIONS_ADD)
    public ModelAndView add(UnderLineRechargeBean form){
        //开始日志
        LogUtil.startLog(UnderLineRechargeController.class.toString(), UnderLineRechargeDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(UnderLineRechargeDefine.INFO_PATH);


        AdminSystem adminSystem  = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        form.setAddUser(Integer.parseInt(adminSystem.getId()));
        form.setAddUserName(ShiroUtil.getLoginUsername());

        //验证表单
        this.validatorFieldCheck(modelAndView, form);
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)){
            return modelAndView;
        }

        Integer result = this.underLineRechargeService.insertRecord(form);

        List<String> codeList = new ArrayList<String>();

        if (result > 0){
            //写入数据库成功后,将code写入Redis
            List<UnderLineRecharge> underLineRechargeList = this.underLineRechargeService.selectUnderLineRechargeList(form);
            if (!underLineRechargeList.isEmpty()){
                for (UnderLineRecharge code : underLineRechargeList){
                    codeList.add(code.getCode());
                }

                String codeListString = JSONObject.toJSONString(codeList);
                RedisUtils.set(RedisConstants.UNDER_LINE_RECHARGE_TYPE, codeListString);
            }else {
                LogUtil.infoLog(this.getClass().getName(), "========================线下充值类型写入Redis失败!=========================");
            }
        }

        modelAndView.addObject(UnderLineRechargeDefine.SUCCESS, UnderLineRechargeDefine.SUCCESS);
        LogUtil.endLog(UnderLineRechargeController.class.toString(), UnderLineRechargeDefine.INSERT_ACTION);

        return modelAndView;
    }


    /**
     * 更新数据
     * @param request
     * @param form
     * @return
     * @Author : huanghui
     */
    @RequestMapping(UnderLineRechargeDefine.UPDATE_ACTION)
    @RequiresPermissions(UnderLineRechargeDefine.PERMISSIONS_MODIFY)
    public ModelAndView update(HttpServletRequest request, UnderLineRechargeBean form){
        LogUtil.startLog(UnderLineRechargeDefine.class.toString(), UnderLineRechargeDefine.UPDATE_ACTION);

        ModelAndView modelAndView = new ModelAndView(UnderLineRechargeDefine.INFO_PATH);

        //表单校验
        this.validatorFieldCheck(modelAndView, form);
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)){
            return modelAndView;
        }

        Integer result = this.underLineRechargeService.updateUnderLineRechargeListInfo(form);

        if (result != null){
            //更新数据库成功后,将code写入Redis
            List<String> codeList = new ArrayList<String>();
            List<UnderLineRecharge> underLineRechargeList = this.underLineRechargeService.selectUnderLineRechargeList(form);

            if (!underLineRechargeList.isEmpty()) {
                for (UnderLineRecharge code : underLineRechargeList) {
                    codeList.add(code.getCode());
                }

                String codeListString = JSONObject.toJSONString(codeList);
                RedisUtils.set(RedisConstants.UNDER_LINE_RECHARGE_TYPE, codeListString);
            }else {
                LogUtil.infoLog(this.getClass().getName(), "========================线下充值类型更新Redis失败!=========================");
            }
        }

        modelAndView.addObject(UnderLineRechargeDefine.SUCCESS, UnderLineRechargeDefine.SUCCESS);
        LogUtil.endLog(UnderLineRechargeDefine.class.toString(), UnderLineRechargeDefine.UPDATE_ACTION);
        return modelAndView;
    }

    /**
     * 删除条目
     * @param request
     * @param ids
     * @return
     * @Author : huanghui
     */
    @RequestMapping(UnderLineRechargeDefine.DELETE_ACTION)
    @RequiresPermissions(UnderLineRechargeDefine.PERMISSIONS_DELETE)
    public ModelAndView delete(HttpServletRequest request, String ids, UnderLineRechargeBean form){
        LogUtil.startLog(UnderLineRechargeDefine.class.toString(), UnderLineRechargeDefine.DELETE_ACTION);

        ModelAndView modelAndView = new ModelAndView(UnderLineRechargeDefine.RE_LIST_PATH);

        List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);

        this.underLineRechargeService.deleteUnderLineRecharge(recordList);

        //删除操作后,更新redis中的内容
        List<String> codeList = new ArrayList<String>();
        List<UnderLineRecharge> underLineRechargeList = this.underLineRechargeService.selectUnderLineRechargeList(form);

        if (!underLineRechargeList.isEmpty()) {
            for (UnderLineRecharge code : underLineRechargeList) {
                codeList.add(code.getCode());
            }

            String codeListString = JSONObject.toJSONString(codeList);
            RedisUtils.set(RedisConstants.UNDER_LINE_RECHARGE_TYPE, codeListString);
        }else {
            LogUtil.infoLog(this.getClass().getName(), "========================线下充值类型删除更新Redis失败!=========================");
        }

        LogUtil.endLog(UnderLineRechargeDefine.class.toString(), UnderLineRechargeDefine.DELETE_ACTION);
        return modelAndView;
    }

    /**
     * 画面校验
     * @param modelAndView
     * @param form
     * @return
     * @Author : huanghui
     */
    private void validatorFieldCheck(ModelAndView modelAndView, UnderLineRechargeBean form) {
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "code", form.getCode());
    }

    /**
     * 检测提交数据的合法性
     * @param request
     * @return
     * @Author : huanghui
     */
    @ResponseBody
    @RequestMapping(value = UnderLineRechargeDefine.CHECK_ACTION, method = RequestMethod.POST)
    @RequiresPermissions(value = {UnderLineRechargeDefine.PERMISSIONS_ADD, UnderLineRechargeDefine.PERMISSIONS_MODIFY}, logical = Logical.OR)
    public String checkAction(HttpServletRequest request){
        LogUtil.startLog(UnderLineRechargeDefine.class.toString(), UnderLineRechargeDefine.CHECK_ACTION);

        String name = request.getParameter("name");
        String param = request.getParameter("param");
        String ids = request.getParameter("ids");

        JSONObject jsonObject = new JSONObject();

        //检测名为code的input的输入内容
        if ("code".equals(name)){
            if (param.length() != 4){
                String message = "线下交易类型长度应为4位!";
                jsonObject.put(UnderLineRechargeDefine.JSON_VALID_INFO_KEY, message);
            }else {
                int account = this.underLineRechargeService.underLineRechargeListInfoByCode(ids, param);
                if (account > 0){
                    String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
                    message = message.replace("{label}", "线下交易类型");
                    jsonObject.put(UnderLineRechargeDefine.JSON_VALID_INFO_KEY, message);
                }
            }
        }

        //没有错误时返回
        if (!jsonObject.containsKey(UnderLineRechargeDefine.JSON_VALID_INFO_KEY)){
            jsonObject.put(UnderLineRechargeDefine.JSON_VALID_STATUS_KEY, UnderLineRechargeDefine.JSON_VALID_STATUS_OK);
        }

        LogUtil.endLog(UnderLineRechargeDefine.class.toString(), UnderLineRechargeDefine.CHECK_ACTION);
        return jsonObject.toString();
    }

}
