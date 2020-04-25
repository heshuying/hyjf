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
import com.hyjf.mybatis.model.auto.EveLog;
import com.hyjf.mybatis.model.auto.EveLogExample;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 * @author liubin
 */

@Service("EveService")
public class EveFileServiceImpl extends CustomizeMapper implements EveFileService {

    private static final String EVELOG = "evelog";
    //@Autowired
   // private EveLogDao eveLogDao;
	@Override
	public void saveFile(ArrayList<EveLog> list) {
	    for (EveLog eve : list) {
	        EveLogExample example = new EveLogExample();
	        EveLogExample.Criteria eveCrt = example.createCriteria();
	        int nowTime = GetDate.getNowTime10();
	        eve.setCreateTime(nowTime);
	        eve.setCreateUserId(1);
	        eve.setDelFlg(0);
	        eveCrt.andTrannoEqualTo(eve.getTranno());
	        eveCrt.andOrdernoEqualTo(eve.getOrderno());
	        eveLogMapper.insert(eve);
            /*if(eveLogMapper.selectByExample(example).size()<1){
                eveLogMapper.insert(eve);
            }*/
	        //monodb存储
            /*if(selecteveLogByTranno(eve.getTranno())==null){
                EveLogMon logMon = new EveLogMon();
                BeanUtils.copyProperties(eve,logMon);
                eveLogDao.insert(logMon, EVELOG);
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
//    public EveLogMon selecteveLogByTranno(String tranno) {
//        Query query = new Query();
//        Criteria criteria = Criteria.where("tranno").is(tranno);
//        query.addCriteria(criteria);
//        return this.eveLogDao.findOne(query, EVELOG);
//    }
}
	