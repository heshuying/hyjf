package com.hyjf.api.web.i4;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.I4;
import com.hyjf.mybatis.model.auto.I4Example;
import com.hyjf.mybatis.model.auto.Idfa;
import com.hyjf.mybatis.model.auto.IdfaExample;

@Service
public class I4ServiceImpl extends BaseServiceImpl implements I4Service {

    @Override
    public List<Idfa> selectIdfas(String idfa) {
        IdfaExample example = new IdfaExample();
        IdfaExample.Criteria cri = example.createCriteria();
        cri.andIdfaEqualTo(idfa);
        return idfaMapper.selectByExample(example);
    }
    
    /**
     * 添加安装设备信息
     * @param paramBean
     * @return
     */
    public JSONObject insertInstallNotice(I4Bean bean){
    	JSONObject jo =new JSONObject();
    	
    	I4Example example= new I4Example();
    	I4Example.Criteria cri= example.createCriteria();
    	cri.andIdfaEqualTo(bean.getIdfa());
    	cri.andAppidEqualTo(Integer.valueOf(bean.getAppid()));
    	List<I4> list= i4Mapper.selectByExample(example);
    	
    	if(list!=null && list.size()>0){
			jo.put(I4Define.SUCCESS, I4Define.FALSE);
			jo.put(I4Define.MESSAGE, "该设备已存在！");
			return jo;
    	}
    	
    	I4 i4 = new I4();
    	i4.setAppid(Integer.valueOf(bean.getAppid()));
    	i4.setIdfa(bean.getIdfa());
    	i4.setCreateTime(new Date());
    	if(bean.getMac()!=null){
    		i4.setMac(bean.getMac());
    	}
    	if(bean.getOpenudid()!=null){
    		i4.setOpenudid(bean.getOpenudid());
    	}
    	if(bean.getOs()!=null){
    		i4.setOs(bean.getOs());
    	}
    	i4Mapper.insertSelective(i4);

		jo.put(I4Define.SUCCESS, I4Define.TRUE);
		jo.put(I4Define.MESSAGE, "入库成功！");
		return jo;
    }
    
    
    
}



