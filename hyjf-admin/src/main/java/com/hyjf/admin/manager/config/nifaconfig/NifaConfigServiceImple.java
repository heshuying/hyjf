/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.nifaconfig;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.FddTempletCustomize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;

import java.util.List;

/**
 * @author nixiaoling
 * @version NifaConfigBean, v0.1 2018/7/4 11:46
 */
@Service
public class NifaConfigServiceImple extends BaseServiceImpl implements NifaConfigService {

    private static Logger logger = LoggerFactory.getLogger(NifaConfigServiceImple.class);

    /**
     * 插入字段定义表
     *
     * @param record
     */
    @Override
    public int insertRecordFieldDefinition(NifaFieldDefinition record) {
        int intFlg = nifaFieldDefinitionMapper.insertSelective(record);
        if (intFlg > 0) {
            logger.info("==================插入字段定义表成功!======");
        } else {
            throw new RuntimeException("============插入字段定义表失败!========");
        }
        return intFlg;
    }

    /**
     * 根据分页查找数据
     *
     * @param limtStart
     * @param limtEnd
     * @return
     */
    @Override
    public List<NifaFieldDefinition> getNifaFieldDefinitionList(int limtStart, int limtEnd) {
        NifaFieldDefinitionExample example = new NifaFieldDefinitionExample();
        SimpleDateFormat smp= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        example.setOrderByClause("create_time DESC");
        if (limtStart != -1) {
            example.setLimitStart(limtStart);
            example.setLimitEnd(limtEnd);
        }
        List<NifaFieldDefinition> nifaFieldDefinitionInterfaceList = nifaFieldDefinitionMapper.selectByExample(example);
        return nifaFieldDefinitionInterfaceList;
    }

    /**
     * 根据id查找字段定义数据
     *
     * @param fieldDefinitId
     * @return
     */
    @Override
    public NifaFieldDefinition selectNifaFieldDefinitionInterfaceById(int fieldDefinitId) {
        NifaFieldDefinitionExample example = new NifaFieldDefinitionExample();
        example.createCriteria().andIdEqualTo(fieldDefinitId);
        List<NifaFieldDefinition> nifaFieldDefinitionInterfaceList = nifaFieldDefinitionMapper.selectByExample(example);
        if (null != nifaFieldDefinitionInterfaceList && nifaFieldDefinitionInterfaceList.size() > 0) {
            return nifaFieldDefinitionInterfaceList.get(0);
        }
        return null;
    }

    /**
     * 修改字段定义
     *
     * @param record
     * @return
     */
    @Override
    public int updateRecordFieldDefinition(NifaFieldDefinition record) {
        int intFlg = nifaFieldDefinitionMapper.updateByPrimaryKey(record);
        if (intFlg > 0) {
            logger.info("==================更新字段定义表成功!======");
        } else {
            throw new RuntimeException("============更新字段定义表失败!========");
        }
        return intFlg;
    }

    /**
     * 获取合同模板条款
     *
     * @param limtStart
     * @param limtEnd
     * @return
     */
    @Override
    public List<NifaContractTemplate> selectNifaContractTemplateInterface(int limtStart, int limtEnd) {
        NifaContractTemplateExample example = new NifaContractTemplateExample();
        if (limtStart != -1) {
            example.setLimitStart(limtStart);
            example.setLimitEnd(limtEnd);
        }
        example.setOrderByClause(" create_time DESC ");
        List<NifaContractTemplate> nifaFieldDefinitionInterfaceList = nifaContractTemplateMapper.selectByExample(example);
        return nifaFieldDefinitionInterfaceList;
    }

    /**
     * 插入合同模板条款
     *
     * @param nifaContractTemplateInterface
     * @return
     */
    @Override
    public int insertNifaContractTemplateInterface(NifaContractTemplate nifaContractTemplateInterface) {
        int intFlg = nifaContractTemplateMapper.insertSelective(nifaContractTemplateInterface);
        if (intFlg > 0) {
            logger.info("==================插入合同模版约定条款表成功!======");
        } else {
            throw new RuntimeException("============插入合同模版约定条款表失败!========");
        }
        return intFlg;
    }

    /**
     * 修改合同模板条款
     *
     * @param nifaContractTemplateInterface
     * @return
     */
    @Override
    public int updateNifaContractTemplateInterface(NifaContractTemplate nifaContractTemplateInterface) {
        int intFlg = nifaContractTemplateMapper.updateByPrimaryKey(nifaContractTemplateInterface);
        if (intFlg > 0) {
            logger.info("==================修改合同模版约定条款表成功!======");
        } else {
            throw new RuntimeException("============修改合同模版约定条款表失败!========");
        }
        return intFlg;
    }

    /**
     * 根据id获取合同模板条款
     *
     * @param contractId
     * @return
     */
    @Override
    public NifaContractTemplate selectNifaContractTemplateInterfaceById(int contractId) {
        NifaContractTemplateExample example = new NifaContractTemplateExample();
        example.createCriteria().andIdEqualTo(contractId);
        List<NifaContractTemplate> nifaFieldDefinitionInterfaceList = nifaContractTemplateMapper.selectByExample(example);
        if (null != nifaFieldDefinitionInterfaceList && nifaFieldDefinitionInterfaceList.size() > 0) {
            return nifaFieldDefinitionInterfaceList.get(0);
        }
        return null;
    }

    /**
     * 查找不再合同模版约定条款表里的协议模板号
     *
     * @return
     */
    @Override
    public List<FddTempletCustomize> selectContractTempId() {
        List<FddTempletCustomize> listContractTemp = fddTempletCustomizeMapper.selectContractTempId();
        return listContractTemp;
    }

    /**
     * 合同模版约定条款删除
     *
     * @param recordList
     */
    @Override
    public void deleteRecordContractTemplate(List<Integer> recordList) {
        for (Integer id : recordList) {
            int intDelete = nifaContractTemplateMapper.deleteByPrimaryKey(id);
            if (intDelete <= 0) {
                throw new RuntimeException("合同模版约定条款删除失败：" + id);
            }
        }
    }

}
