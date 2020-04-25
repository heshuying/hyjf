package com.hyjf.web.wisdomtooth;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.ContentHelp;
import com.hyjf.mybatis.model.auto.ContentHelpExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.ContentHelpCustomize;
import com.hyjf.mybatis.model.customize.HelpCategoryCustomize;
import com.hyjf.mybatis.model.customize.HelpContentCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.help.HelpService;
import com.hyjf.web.util.WebUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * WisdomToothController智齿客服
 * </p>
 *
 * @author tyy
 * @version 1.0.0
 */
@Controller
@RequestMapping(WisdomToothDefine.CONTROLLOR_REQUEST_MAPPING)
public class WisdomToothController extends BaseController {

    Logger _log = LoggerFactory.getLogger(WisdomToothController.class);
    @Autowired
    private WisdomToothService wisdomToothService;
    @Autowired
    private HelpService helpService;
    /**
     * 智齿客服修改绑定银行卡
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = WisdomToothDefine.MODIFY_BANK_CIRD, method = RequestMethod.GET)
    public ModelAndView modifyCode(HttpServletRequest request) throws Exception {
        LogUtil.startLog(WisdomToothController.class.getName(), WisdomToothDefine.MODIFY_CODE);
        ModelAndView modeAndView = null;
        Integer userId = WebUtils.getUserId(request);
        if(userId!=null){
            _log.info("转发地址修改密码================="+CustomConstants.HOST +WisdomToothDefine.USER_SAFE_MODIFYCODE);
            Users user =  wisdomToothService.getUsers(userId);
            if(user.getBankOpenAccount()!=null&&user.getBankOpenAccount().intValue()==0){
                //未开户
                modeAndView = new ModelAndView("redirect:" + CustomConstants.HOST +"/bank/web/user/bankopen/init.do");
            }else{
                modeAndView = new ModelAndView("redirect:" + CustomConstants.HOST +"/bank/web/user/recharge/rechargePage.do");
            }
        }else {
            _log.info("转发登录地址================="+CustomConstants.HOST + "/user/login/init.do");

            modeAndView = new ModelAndView("redirect:" + CustomConstants.HOST + "/user/login/init.do");
        }
        LogUtil.endLog(WisdomToothController.class.getName(), WisdomToothDefine.MODIFY_CODE);
        return modeAndView;
    }

    /**
     * 智齿客服修改密码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = WisdomToothDefine.MODIFY_CODE, method = RequestMethod.GET)
    public ModelAndView modifyBankCard(HttpServletRequest request) throws Exception {
        LogUtil.startLog(WisdomToothController.class.getName(), WisdomToothDefine.MODIFY_CODE);
        ModelAndView modeAndView = null;
        Integer userId = WebUtils.getUserId(request);
        if(userId!=null){
            modeAndView = new ModelAndView("redirect:" + CustomConstants.HOST +WisdomToothDefine.USER_SAFE_MODIFYCODE);
        }else {
            _log.info("转发登录地址================="+CustomConstants.HOST + "/user/login/init.do");

            modeAndView = new ModelAndView("redirect:" + CustomConstants.HOST + "/user/login/init.do");
        }
        LogUtil.endLog(WisdomToothController.class.getName(), WisdomToothDefine.MODIFY_CODE);
        return modeAndView;
    }
    /**
     * 智齿客服修改手机号
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = WisdomToothDefine.MODIFY_TEL_PHONE, method = RequestMethod.GET)
    public ModelAndView modifyTelPhone(HttpServletRequest request) throws Exception {
        LogUtil.startLog(WisdomToothController.class.getName(), WisdomToothDefine.MODIFY_TEL_PHONE);
        ModelAndView modeAndView = null;
        Integer userId = WebUtils.getUserId(request);
        if(userId!=null){
            modeAndView = new ModelAndView("redirect:" + CustomConstants.HOST +WisdomToothDefine.BANK_USER_TRANSPASSWORD_INITMOBILE);
        }else {
            modeAndView = new ModelAndView("redirect:" + CustomConstants.HOST + "/user/login/init.do");
        }
        LogUtil.endLog(WisdomToothController.class.getName(), WisdomToothDefine.MODIFY_TEL_PHONE);
        return modeAndView;
    }
    /**
     * 智齿客服找回密码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = WisdomToothDefine.FIND_PASSWORD, method = RequestMethod.GET)
    public ModelAndView findPassword(HttpServletRequest request) throws Exception {
        LogUtil.startLog(WisdomToothController.class.getName(), WisdomToothDefine.FIND_PASSWORD);
        ModelAndView  modeAndView = new ModelAndView("redirect:" + CustomConstants.HOST +WisdomToothDefine.USER_FINDPASSWORD_INIT);;
        LogUtil.endLog(WisdomToothController.class.getName(), WisdomToothDefine.FIND_PASSWORD);
        return modeAndView;
    }
    /**
     * 智齿客服修改交易密码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = WisdomToothDefine.MODIFY_BUSINESS_CODE, method = RequestMethod.GET)
    public ModelAndView modifyBusinessCode(HttpServletRequest request) throws Exception {
        LogUtil.startLog(WisdomToothController.class.getName(), WisdomToothDefine.MODIFY_BUSINESS_CODE);
        ModelAndView modeAndView = null;
        Integer userId = WebUtils.getUserId(request);
        if(userId!=null){
            modeAndView = new ModelAndView("redirect:" + CustomConstants.HOST +WisdomToothDefine.BANK_USER_TRANSPASSWORD_RESETPASSWORD);
        }else {
            modeAndView = new ModelAndView("redirect:" + CustomConstants.HOST + "/user/login/init.do");
        }
        LogUtil.endLog(WisdomToothController.class.getName(), WisdomToothDefine.MODIFY_BUSINESS_CODE);
        return modeAndView;
    }
    /**
     * 智齿客服主页
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = WisdomToothDefine.MIANPAGE, method = RequestMethod.GET)
    public ModelAndView mainPage(HttpServletRequest request,HttpServletResponse response) throws Exception {
        LogUtil.startLog(WisdomToothController.class.getName(), WisdomToothDefine.MIANPAGE);
        ModelAndView modeAndView = new ModelAndView(WisdomToothDefine.SERVICE_SOBOT);
        LogUtil.endLog(WisdomToothController.class.getName(), WisdomToothDefine.MIANPAGE);
        return modeAndView;
    }
    /**
     * 智齿客服线下充值
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = WisdomToothDefine.OFFLINE_RECHARGE, method = RequestMethod.GET)
    public ModelAndView offlineRecharge(HttpServletRequest request,HttpServletResponse response) throws Exception {
        LogUtil.startLog(WisdomToothController.class.getName(), WisdomToothDefine.OFFLINE_RECHARGE);
        ModelAndView modeAndView = new ModelAndView(WisdomToothDefine.OFFLINE_RECHARGE_PAGE);
        LogUtil.endLog(WisdomToothController.class.getName(), WisdomToothDefine.OFFLINE_RECHARGE);
        return modeAndView;
    }
    /**
     * 智齿客服常见问题列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = WisdomToothDefine.CONTENT_HELPS, method = RequestMethod.GET)
    @ResponseBody
    public Object contentHelps(HttpServletRequest request, HttpServletResponse response, String pageName)  throws Exception {
        LogUtil.startLog(WisdomToothController.class.getName(), WisdomToothDefine.CONTENT_HELPS);
        Map<String,Object> map = new HashMap<>();
        ContentHelpCustomize contentHelpCustomize = new ContentHelpCustomize();
        contentHelpCustomize.setLimitStart(0);
        contentHelpCustomize.setLimitEnd(5);//默认五条
        List<ContentHelpCustomize> pageList= wisdomToothService.queryContentCustomize(contentHelpCustomize);
        // 按照帮助中心逻辑生成itemId
        List<HelpCategoryCustomize> list = helpService.selectCategory("help");
        List<Map<String, Object>> AllList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            String typeId = "hp"+i;
            String itemId = "is"+i;
            Map<String, Object> tmpmap = new HashMap<String, Object>();
            HelpCategoryCustomize HelpCategoryCustomize = list.get(i);
            HelpCategoryCustomize.setItemId(itemId);
            tmpmap.put("HelpCategoryCustomize", list.get(i));
            // 查出帮助中心子分类
            List<HelpCategoryCustomize> listsun = helpService.selectSunCategory(list.get(i).getId() + "");
            if (listsun != null) {
                for (int j = 0; j < listsun.size(); j++) {
                    HelpCategoryCustomize HelpCategoryCustomize1 = listsun.get(j);
                    HelpCategoryCustomize1.setItemId(typeId+(j+1));
                    List<HelpContentCustomize> listsunContent = helpService.selectSunContentCategory(
                            String.valueOf(HelpCategoryCustomize1.getId()), String.valueOf(HelpCategoryCustomize1.getPid()));
                    for(int k=0;k<listsunContent.size();k++){
                        HelpContentCustomize HelpContentCustomize2 =  listsunContent.get(k);
                        HelpContentCustomize2.setTypeId(typeId+(j+1));
                        HelpContentCustomize2.setItemId(itemId+j+(k+1));
                    }
                    HelpCategoryCustomize1.setListsunContent(listsunContent);
                }
                tmpmap.put("listsun", listsun);
            }
            AllList.add(tmpmap);
        }
        for(int i =0;i< pageList.size();i++){
            ContentHelpCustomize help = pageList.get(i);
            for(int j = 0;j<AllList.size();j++){
                Map<String, Object> mapList = AllList.get(j);
                if(mapList!=null){
                    List<HelpCategoryCustomize> listsun = (List<HelpCategoryCustomize>)mapList.get("listsun");
                    if(!CollectionUtils.isEmpty(listsun)){
                        for(int k=0;k<listsun.size();k++){
                            List<HelpContentCustomize> listsunContent =  listsun.get(k).getListsunContent();
                            if(!CollectionUtils.isEmpty(listsunContent)){
                                for(int ii=0;ii<listsunContent.size();ii++){
                                    if(listsunContent.get(ii).getId().equals(help.getId().toString())){
                                        help.setTypeId(listsunContent.get(ii).getTypeId());
                                        help.setItemId(listsunContent.get(ii).getItemId());
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        //map.put("list",AllList);
        map.put("pageList",pageList);
        map.put("status", "200");
        return map;
    }


    /**
     * 智齿客服常见问题答案
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = WisdomToothDefine.HELP, method = RequestMethod.GET)
    @ResponseBody
    public Object help(HttpServletRequest request,Integer id) throws Exception {
        LogUtil.startLog(WisdomToothController.class.getName(), WisdomToothDefine.HELP);
        Map<String,Object> map = new HashMap<>();
        if(id==null){
            map.put("status", "99");
            map.put("statusDesc","请求参数非法");
            return map;
        }
        map.put("status", "200");
        ContentHelp contentHelp = wisdomToothService.queryContentById(id);
        map.put("contentHelp",contentHelp);
        return map;
    }
}
