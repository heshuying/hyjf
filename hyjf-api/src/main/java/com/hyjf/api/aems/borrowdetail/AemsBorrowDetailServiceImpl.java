package com.hyjf.api.aems.borrowdetail;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.web.BorrowDetailCustomize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标的详情service实现类
 * jijun 20180910
 */
@Service
public class AemsBorrowDetailServiceImpl extends BaseServiceImpl implements AemsBorrowDetailService {
    @Override
    public BorrowDetailCustomize selectProjectDetail(String borrowNid) {
      return borrowDetailMapper.getProjectDetail(borrowNid);
    }

    //借款人企业信息
    @Override
    public BorrowUsers getBorrowUsersByNid(String borrowNid) {
        if(StringUtils.isBlank(borrowNid)){
            return null;
        }
        BorrowUsersExample example = new BorrowUsersExample();
        BorrowUsersExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        List<BorrowUsers> list = this.borrowUsersMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    //借款人信息
    @Override
    public BorrowManinfo getBorrowManinfoByNid(String borrowNid) {
        if(StringUtils.isBlank(borrowNid)){
            return null;
        }
        BorrowManinfoExample example = new BorrowManinfoExample();
        BorrowManinfoExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        List<BorrowManinfo> list = this.borrowManinfoMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    //房产抵押信息
    @Override
    public List<BorrowHouses> getBorrowHousesByNid(String borrowNid) {
        if(StringUtils.isBlank(borrowNid)){
            return null;
        }
        BorrowHousesExample example = new BorrowHousesExample();
        BorrowHousesExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        List<BorrowHouses> list = this.borrowHousesMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            return list;
        }
        return null;
    }

    //车辆抵押信息
    @Override
    public List<BorrowCarinfo> getBorrowCarinfoByNid(String borrowNid) {
        if(StringUtils.isBlank(borrowNid)){
            return null;
        }
        BorrowCarinfoExample example = new BorrowCarinfoExample();
        BorrowCarinfoExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        List<BorrowCarinfo> list = this.borrowCarinfoMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            return list;
        }
        return null;
    }
}
