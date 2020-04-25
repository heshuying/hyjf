package com.hyjf.admin.manager.content.statisticsway;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.mapper.auto.StatisticsWayConfigureMapper;
import com.hyjf.mybatis.model.auto.StatisticsWayConfigure;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.statisticsway.StatisticsWayConfigureCustomize;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 运营报告---统计方式配置
 * @author by xiehuili on 2018/6/20.
 */
@Service
public class StatisticsWayServiceImpl extends BaseServiceImpl implements StatisticsWayService {
    @Autowired
    private StatisticsWayConfigureMapper statisticsWayConfigureMapper;

    /**
     * 根据条件查询数据
     *
     * @param map
     * @return
     */
    @Override
    public Integer countRecordList(Map<String, Object> map){
        return statisticsWayCustomizeMapper.countRecordList(map);
    }
    /**
     * 根据条件查询数据
     *
     * @param
     * @return
     */
    @Override
    public List<StatisticsWayConfigureCustomize> selectRecordList(Map<String, Object> map){
        return statisticsWayCustomizeMapper.selectRecordList(map);
    }
    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    @Override
    public StatisticsWayConfigureCustomize selectstatisticsWayById(Integer id){

        return statisticsWayCustomizeMapper.selectstatisticsWayById(id);
    }
    /**
     * 校验表单字段
     *
     * @return
     */
    @Override
    public JSONObject validatFieldCheck(StatisticsWayConfigure form)  {
        boolean flag=true;//判断是否查询唯一标识
        JSONObject js=new JSONObject();
        if(form == null){
            js.put("n_error","请填写标题名称和唯一标识");
            flag=false;
        }
        String titleName = form.getTitleName();
        String uniqueIdentifier = form.getUniqueIdentifier();
        if(StringUtils.isBlank(titleName)){
            js.put("n_error","标题名称不能为空，请重新输入！");
            flag=false;
        }
        if(StringUtils.isBlank(uniqueIdentifier)){
            js.put("v_error","唯一标识不能为空，请重新输入！");
            flag=false;
        }
        if(StringUtils.isNotBlank(form.getStatisticalMethod())&&form.getStatisticalMethod().length() >240){
            js.put("s_error","统计方式必须在240个字符以内，请重新输入！");
            flag=false;
        }
        if(form.getId() == null&& flag==true){ //修改页面不用判断唯一标识
            //根据唯一标识查询，判断唯一标识是否存在
            int count = statisticsWayCustomizeMapper.validatorFieldCheck(uniqueIdentifier);
            if(count >0){
                js.put("v_error","唯一标识已经存在，请重新输入！");
            }
        }
        return js;
    }

    /**
     * 新增或修改
     *
     * @param form
     */
    @Override
    public void updateStaticsWay(StatisticsWayConfigure form) {
        //修改人
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        Integer createUserId = Integer.parseInt(adminSystem.getId());
        //参数进行非空判断
        Integer id = form.getId();
        String titleName = form.getTitleName();
        String uniqueIdentifier = form.getUniqueIdentifier();
        if(StringUtils.isNotBlank(titleName)&&StringUtils.isNotBlank(uniqueIdentifier)){
            form.setCreateUserId(createUserId);
            if(id != null&&id.intValue()>0){
                //修改
                statisticsWayConfigureMapper.updateByPrimaryKeySelective(form);
            }else{
                //新增
                statisticsWayConfigureMapper.insertSelective(form);
            }
        }
    }
    /**
     * 删除统计方式
     *
     * @param id
     */
    @Override
    public void deleteStaticsWay(Integer id) {
        //根据id查询
        StatisticsWayConfigure sc = statisticsWayConfigureMapper.selectByPrimaryKey(id);
        //设置删除状态
        sc.setIsDelete(0);
        statisticsWayConfigureMapper.updateByPrimaryKeySelective(sc);
    }
}
