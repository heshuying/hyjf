package com.hyjf.admin.finance.bankjournal;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.mapper.auto.EveLogMapper;
import com.hyjf.mybatis.mapper.customize.EveLogCustomizeMapper;
import com.hyjf.mybatis.model.auto.EveLog;
import com.hyjf.mybatis.model.auto.EveLogExample;
import com.hyjf.mybatis.model.customize.EveLogCustomize;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by cui on 2018/1/19.
 */
@Service
public class BankJournalServiceImpl implements BankJournalService {

    @Autowired
    private EveLogCustomizeMapper eveLogCustomizeMapper;

    @Override
    public void queryList(BankJournalBean form) {

        EveLogCustomize customize=new EveLogCustomize();
        BeanUtils.copyProperties(form,customize);

        Integer count = this.eveLogCustomizeMapper.queryEveLogCount(customize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count,BankJournalDefine.DEFAULT_PAGE_LIMIT);
            customize.setLimitStart(paginator.getOffset());
            customize.setLimitEnd(paginator.getLimit());

            List<EveLogCustomize> eveLogs = this.eveLogCustomizeMapper.queryEveLogList(customize);
            form.setPaginator(paginator);
            form.setRecordList(eveLogs);
        }
    }
}
