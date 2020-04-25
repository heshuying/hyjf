package com.hyjf.app.find;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.find.contentarticle.AppContentArticleDefine;
import com.hyjf.app.home.HomePageService;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ContentArticle;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class FindController {
    @Autowired
    private HomePageService homePageService;

    @Autowired
    private ContentArticleService contentArticleService;
    private final static String DATE_FORMAT = "yyyy-MM-dd"; 
    /** 图片地址 */
    private static String HOST_URL = PropUtils.getSystem("file.domain.url");

    @ResponseBody
    @RequestMapping(value = FindDefine.FIND_PATH)
    public JSONObject find(HttpServletRequest request, HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        
        String sign = request.getParameter("sign");
        // 检查参数正确性
        if (!Validator.isNull(sign)) {
        	String key = SecretUtil.getKey(sign);
            if (!Validator.isNull(key)) {
	            Integer userId = null;
	            try {
	                userId = SecretUtil.getUserId(sign);
	                ret.put("userid",userId);
	            } catch (Exception e) {
	            }
            } 
        }
        // 取得加密用的Key
        //前端使用占位图片来显示banner了，这里就不处理
//        Map<String, Object> ads = new HashMap<String, Object>();
//        ads.put("limitStart", -1);
//        // ads.put("limitEnd", 1);
//        ads.put("host", HOST_URL);
//        ads.put("code", "startpage");
//        List<AppAdsCustomize> list = homePageService.searchBannerList(ads);
//        ret.put("bannerList", list);
        //从 huiyingdai_content_article 表中查询，其中20 对应字段type，表示公司公告
        List<ContentArticle> newsList =
                homePageService.searchHomeNoticeList("20", 0, 3);
        JSONArray jsonArray = new JSONArray();
        
        for(ContentArticle article:newsList){
        	JSONObject detailsJson = new JSONObject();
        	detailsJson.put("id", article.getId());
        	detailsJson.put("img", article.getImgurl());
        	detailsJson.put("title", article.getTitle());
        	detailsJson.put("date", DateFormatUtils.format(article.getCreateTime(), DATE_FORMAT));
        	detailsJson.put("shareTitle", article.getTitle());
        	detailsJson.put("shareContent", article.getSummary());
        	detailsJson.put("sharePicUrl", "https://www.hyjf.com/data/upfiles/image/20140617/1402991818340.png");
        	detailsJson.put("shareUrl", PropUtils.getSystem("hyjf.web.host")+ AppContentArticleDefine.REQUEST_MAPPING+
                    AppContentArticleDefine.GET_CONTENT_ARTICLE_ID_ACTION.replace("{contentArticleId}", article.getId()+"").replace("{type}", "20"));
        	
//        	前端自己拼写这个link地址了，这里就屏蔽了
//        	detailsJson.put("link", "http://www.hyjf.com");
        	jsonArray.add(detailsJson);
        }
        ret.put("status", "000");
        ret.put("statusDesc", "成功");
        ret.put("newsList", jsonArray);
        return ret;
    }

   /* @ResponseBody
    @RequestMapping(value = FindDefine.FIND_PATH + FindDefine.Banner)
    public JSONObject banner(HttpServletRequest request, HttpServletResponse response) {
        JSONObject ret = new JSONObject();

        Map<String, Object> ads = new HashMap<String, Object>();
        ads.put("limitStart", -1);
        // ads.put("limitEnd", 1);
        ads.put("host", HOST_URL);
        ads.put("code", "startpage");
        List<AppAdsCustomize> list = homePageService.searchBannerList(ads);
        ret.put("bannerList", list);
        return ret;
    }*/

//    @ResponseBody
//    @RequestMapping(value = FindDefine.FIND_PATH + FindDefine.News)
//    public JSONObject news(HttpServletRequest request, HttpServletResponse response) {
//        JSONObject ret = new JSONObject();
//
//        List<ContentArticle> list =
//                homePageService.searchHomeNoticeList("3", 0, 3);
//        ret.put("newsList", list);
//        return ret;
//    }

    @ResponseBody
    @RequestMapping(FindDefine.FIND_PATH + FindDefine.News + "/{newsId}")
    public JSONObject newid(@PathVariable int newsId, HttpServletRequest request, HttpServletResponse response) {
        JSONObject ret = new JSONObject();

        ContentArticle article = contentArticleService.getNoticeInfo(newsId);
        JSONObject detail = new JSONObject();
        detail.put("title", article.getTitle());
        detail.put("date", DateFormatUtils.format(article.getCreateTime(), DATE_FORMAT));
        detail.put("content", article.getSummary());
//        ret.put(key, value)
        ret.put("status", "000");
        ret.put("statusDesc", "成功");
        ret.put("details",detail);
        return ret;
    }

}
