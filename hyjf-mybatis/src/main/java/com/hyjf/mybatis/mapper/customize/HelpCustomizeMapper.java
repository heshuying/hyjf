package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.auto.ContentArticleExample;
import com.hyjf.mybatis.model.customize.HelpCategoryCustomize;
import com.hyjf.mybatis.model.customize.HelpContentCustomize;

public interface HelpCustomizeMapper {
    /**
     * 
     * @method: selectCategory
     * @description:        根据help查出大分类 
     *  @param group 'help'
     *  @return 
     * @return: List<Map<String,Object>>
    * @mender: zhouxiaoshuai
     * @date:   2016年5月6日 上午10:31:36
     */
    List<HelpCategoryCustomize> selectCategory(String group);
    /**
     * 
     * @method: selectSunCategory
     * @description:    根据大类id查询子类      
     *  @param pageName 大类id
     *  @return 
     * @return: List<Map<String,Object>>
    * @mender: zhouxiaoshuai
     * @date:   2016年5月6日 上午10:32:13
     */
    List<HelpCategoryCustomize> selectSunCategory(String pageName);
    /**
     * 
     * @method: selectSunContentCategory
     * @description: 根据子类id和直属于大类的id查询出所属帮助内容       
     *  @param map   放如下两个数据
     *   type   子类id
     *   pid   直属于大类的id
     *  @return 
     * @return: List<Map<String,Object>>
    * @mender: zhouxiaoshuai
     * @date:   2016年5月6日 上午10:34:41
     */
    List<HelpContentCustomize> selectSunContentCategory(Map<String, Object> map);

    /**
     * 查询首页文章数据 单独
     * @param example
     * @return
     */
    List<ContentArticle> selectByExampleWithBLOBsAlone(ContentArticleExample example);
}
