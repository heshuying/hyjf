package com.hyjf.web.activity.worldCup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.web.BaseController;


/**
 * @author limeng
 * @version WorldCupController, v0.1 2018/6/19 11:16
 */
@RequestMapping(WorldCupDefine.REQUEST_MAPPING)
@RestController
public class WorldCupController extends BaseController {

	/**
     * 
     * 进入世界杯活动页面
     * @author yyc
     * @return
     */
    @RequestMapping(value = WorldCupDefine.REQUEST_PARTICIPATE, method = RequestMethod.GET)
    public ModelAndView getUserStatus(HttpServletRequest request, HttpServletResponse response) {
        
        ModelAndView modelAndView = new ModelAndView(WorldCupDefine.ACTIVITY_FOOTBALL_PATH);
        
        return modelAndView;
    }
}
