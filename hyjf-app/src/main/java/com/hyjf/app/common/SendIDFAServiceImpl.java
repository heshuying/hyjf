package com.hyjf.app.common;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.I4;
import com.hyjf.mybatis.model.auto.I4Example;
import com.hyjf.mybatis.model.auto.Idfa;
import com.hyjf.mybatis.model.auto.IdfaExample;
import com.hyjf.mybatis.model.auto.MobileCode;
import com.hyjf.mybatis.model.auto.MobileCodeExample;
import com.hyjf.mybatis.model.auto.Version;
import com.hyjf.mybatis.model.auto.VersionExample;

@Service
public class SendIDFAServiceImpl extends BaseServiceImpl implements SendIDFAService {



	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param mobileCodeInfo
	 * @author Michael
	 */
		
	@Override
	public void insertMobileCode(MobileCode mobileCodeInfo) {
		this.mobileCodeMapper.insertSelective(mobileCodeInfo);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param userid
	 * @return
	 * @author Michael
	 */
		
	@Override
	public MobileCode getMobileCodeByUserId(Integer userid) {
		MobileCodeExample mobileCodeExample = new MobileCodeExample();
		MobileCodeExample.Criteria cri = mobileCodeExample.createCriteria();
		if(userid != null){
			cri.andUserIdEqualTo(userid);
		}
		List<MobileCode> list = mobileCodeMapper.selectByExample(mobileCodeExample);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null ;
	}


	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param mobileCode
	 * @author Michael
	 */
		
	@Override
	public void updateMobileCode(MobileCode mobileCode) {
		this.mobileCodeMapper.updateByPrimaryKeySelective(mobileCode);
	}


	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param type
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Version getNewVersionByType(Integer type) {
		VersionExample example = new VersionExample();
		VersionExample.Criteria cri = example.createCriteria();
		cri.andTypeEqualTo(type);
		example.setOrderByClause(" id desc ");
		List<Version> list = versionMapper.selectByExample(example);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null ;
			
	}

    /**
     * 查询IDFA
     * @param mobileCode
     */
    @Override
    public List<Idfa> selectIDFA(String idfa) {
        IdfaExample example = new IdfaExample();
        IdfaExample.Criteria cri = example.createCriteria();
        cri.andIdfaEqualTo(idfa);
        List<Idfa> list = idfaMapper.selectByExample(example);
        return list;
    }

    /**
     * 更新IDFA
     * @param mobileCode
     */
    @Override
    public Integer updateIDFA(Idfa idfa) {
        return idfaMapper.updateByPrimaryKeySelective(idfa);
    }

    /**
     * 插入IDFA
     * @param mobileCode
     */
    @Override
    public Integer insertIDFA(Idfa idfa) {
        return idfaMapper.insertSelective(idfa);
    }

    /**
     * 删除IDFA
     * @param mobileCode
     */
    @Override
    public Integer deleteIDFA(Idfa idfa) {
        return idfaMapper.deleteByPrimaryKey(idfa.getId());
    }
    

    /**
     * 删除一年以上的数据
     * @param mobileCode
     */
    @Override
    public Integer deleteIDFAByOneYear(Integer timeInt) {
        IdfaExample example = new IdfaExample();
        IdfaExample.Criteria cri = example.createCriteria();
        cri.andCreateTimeLessThan(timeInt);
        return idfaMapper.deleteByExample(example);
    }
    

    /**
     * 查询I4
     * @param mobileCode
     */
    @Override
    public List<I4> selectI4List(String idfa){
    	I4Example example= new I4Example();
    	I4Example.Criteria cri= example.createCriteria();
    	cri.andIdfaEqualTo(idfa);
    	List<I4> list= i4Mapper.selectByExample(example);
    	return list;
    }
}
