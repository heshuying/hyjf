/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:43:49
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.mqreceiver.borrow.eve;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.AleveLog;
import com.hyjf.mybatis.model.auto.AleveLogExample;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 * @author liubin
 */

@Service("AleveService")
public class AleveFileServiceImpl extends CustomizeMapper implements AleveFileService {

    private static final String ALEVELOG = "alevelog";
   // @Autowired
   // private AleveLogDao aleveLogDao;
    
    /**
     * mongo保存
     */
	/*@Override
	public void saveFile(ArrayList<AleveLog> list) {
	    for (AleveLog aleve : list) {
	        AleveLogExample example = new AleveLogExample();
	        AleveLogExample.Criteria eveCrt = example.createCriteria();
	        int nowTime = GetDate.getNowTime10();
	        aleve.setCreateTime(nowTime);
	        aleve.setCreateUserId(1028);
	        aleve.setDelFlg(0);
	        eveCrt.andTrannoEqualTo(aleve.getTranno());
            if(selectaleveLogByTranno(aleve.getTranno())==null){
                AleveLogMon aLogMon = new AleveLogMon();
                BeanUtils.copyProperties(aleve,aLogMon);
                aleveLogDao.insert(aLogMon, ALEVELOG);
            }
        }
	}*/
    /**
     * mysql保存
     */
    @Override
    public void saveFile(ArrayList<AleveLog> list) {
        for (AleveLog aleve : list) {
            AleveLogExample example = new AleveLogExample();
            AleveLogExample.Criteria eveCrt = example.createCriteria();
            int nowTime = GetDate.getNowTime10();
            aleve.setCreateTime(nowTime);
            aleve.setCreateUserId(1);
            aleve.setDelFlg(0);
            aleve.setUpdFlg(0);
            eveCrt.andTrannoEqualTo(aleve.getTranno());
            aleveLogMapper.insert(aleve);
            /*if(aleveLogMapper.selectByExample(example).size()<1){
                aleveLogMapper.insert(aleve);
            }*/
        }
    }
	
	/**
     * 
     * 查询检证订单号
     * 
     * @param tranno
     * @return
     */
//    @Override
//    public AleveLogMon selectaleveLogByTranno(String tranno) {
//        Query query = new Query();
//        Criteria criteria = Criteria.where("tranno").is(tranno);
//        query.addCriteria(criteria);
//        AleveLogMon aleveLog = this.aleveLogDao.findOne(query, ALEVELOG);
//        return aleveLog;
//    }
}
	