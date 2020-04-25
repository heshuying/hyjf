
package com.hyjf.web.help;

import java.util.List;

import com.hyjf.mybatis.model.customize.HelpCategoryCustomize;
import com.hyjf.mybatis.model.customize.HelpContentCustomize;


public interface HelpService {
/**
 * 
 * @method: selectCategory
 * @description: 		根据help查出大分类	
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
 * @description: 	根据大类id查询子类		
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
 *  @param type   子类id
 *  @param pid   直属于大类的id
 *  @return 
 * @return: List<Map<String,Object>>
* @mender: zhouxiaoshuai
 * @date:   2016年5月6日 上午10:34:41
 */
    List<HelpContentCustomize> selectSunContentCategory(String type, String pid);

	
}
