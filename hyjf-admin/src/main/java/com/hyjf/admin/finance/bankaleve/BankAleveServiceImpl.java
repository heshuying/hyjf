package com.hyjf.admin.finance.bankaleve;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.mapper.customize.AleveCustomizeMapper;
import com.hyjf.mybatis.model.customize.AleveLogCustomize;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by cuigq on 2018/1/22.
 */
@Service
public class BankAleveServiceImpl implements BankAleveService {

    @Autowired
    private AleveCustomizeMapper aleveCustomizeMapper;

    @Override
    public void queryList(BankAleveBean form) {

        AleveLogCustomize customize=new AleveLogCustomize();
        BeanUtils.copyProperties(form,customize);

        Integer count = this.aleveCustomizeMapper.queryAleveLogCount(customize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count,BankAleveDefine.DEFAULT_PAGE_LIMIT);
            customize.setLimitStart(paginator.getOffset());
            customize.setLimitEnd(paginator.getLimit());

            List<AleveLogCustomize> aleveLogs = this.aleveCustomizeMapper.queryAleveLogList(customize);
            form.setPaginator(paginator);
            form.setRecordList(aleveLogs);
        }
    }

    @Override
    public List<AleveLogCustomize> queryAleveLogList(BankAleveBean form) {
        AleveLogCustomize customize=new AleveLogCustomize();
        BeanUtils.copyProperties(form,customize);
        return this.aleveCustomizeMapper.queryAleveLogList(customize);
    }

    @Override
    public Integer queryAleveLogCount(BankAleveBean form) {
        AleveLogCustomize customize=new AleveLogCustomize();
        BeanUtils.copyProperties(form,customize);
        return this.aleveCustomizeMapper.queryAleveLogCount(customize);
    }

}
