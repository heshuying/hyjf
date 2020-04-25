/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.user.userportraitscore;

import com.hyjf.admin.BaseController;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.UserPortraitScoreCustomize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaoyong
 * @version UserPortraitScoreController, v0.1 2018/7/9 17:23
 */
@RestController
@RequestMapping(value = UserPortraitScoreDefine.REQUEST_MAPPING)
public class UserPortraitScoreController extends BaseController {

    @Autowired
    private UserPortraitScoreService userPortraitScoreService;
    private Logger logger = LoggerFactory.getLogger(UserPortraitScoreController.class);

    @RequestMapping(value = UserPortraitScoreDefine.USERPORTRAITSCORE_LIST_ACTION)
    public ModelAndView init(@ModelAttribute(UserPortraitScoreDefine.USERPORTRAITSCORE_LIST_FORM) UserPortraitScoreBean form) {
        logger.info("用户画像评分页面初始化,form : {}" + form);
        ModelAndView modelAndView = new ModelAndView(UserPortraitScoreDefine.USER_PORTRAITSCORE_LIST_PATH);
        //创建分页
        this.createPage(modelAndView, form);
        return modelAndView;
    }

    private void createPage(ModelAndView modelAndView, UserPortraitScoreBean form) {

        // 封装查询条件
        Map<String, Object> userPortrait = this.buildQueryCondition(form);

        int recordTotal = userPortraitScoreService.countRecordTotal(userPortrait);
        logger.info("记录总数 ：{}", recordTotal);
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
            List<UserPortraitScoreCustomize> recordList = userPortraitScoreService.getRecordList(userPortrait,
                    paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject("obj", form);
        }
    }

    /**
     * 构建查询条件
     *
     * @param form
     * @return
     */
    private Map<String, Object> buildQueryCondition(UserPortraitScoreBean form) {
        // 封装查询条件
        Map<String, Object> userPortrait = new HashMap<String, Object>();
        if (form.getUserName() != null) {
            userPortrait.put("userName", form.getUserName().trim());
        }
        return userPortrait;
    }
}
