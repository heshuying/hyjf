package com.hyjf.admin.finance.poundagedetail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageCustomize;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageLedgerCustomize;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageDetailCustomize;

import javax.servlet.http.HttpServletResponse;

@Service
public class PoundageDetailServiceImpl extends BaseServiceImpl implements PoundageDetailService {
    /**
     * 查询数量
     *
     * @param poundageDetailCustomize
     * @return
     */
    @Override
    public Integer getPoundageDetailCount(PoundageDetailCustomize poundageDetailCustomize) {
        Integer count = this.poundageDetailCustomizeMapper.getPoundageDetailCount(poundageDetailCustomize);
        return count;
    }

    /**
     * 查询信息
     *
     * @param poundageDetailCustomize
     * @return
     */
    @Override
    public List<PoundageDetailCustomize> getPoundageDetailList(PoundageDetailCustomize poundageDetailCustomize) {
        List<PoundageDetailCustomize> list = this.poundageDetailCustomizeMapper.getPoundageDetailList(poundageDetailCustomize);
        return list;
    }

    /**
     * 新增信息
     *
     * @param poundageDetailCustomize
     * @return
     */
    public void insertPoundageDetail(PoundageDetailCustomize poundageDetailCustomize) {
        this.poundageDetailCustomizeMapper.insertPoundageDetail(poundageDetailCustomize);
    }

    /**
     * 修改信息
     *
     * @param poundageDetailCustomize
     * @return
     */
    public void updatePoundageDetail(PoundageDetailCustomize poundageDetailCustomize) {
        this.poundageDetailCustomizeMapper.updatePoundageDetail(poundageDetailCustomize);
    }

    /**
     * 删除信息
     *
     * @param id
     * @return
     */
    public void deletePoundageDetail(int id) {
        this.poundageDetailCustomizeMapper.deletePoundageDetail(id);
    }

    /**
     * 获取单条信息
     *
     * @param id
     * @return
     */
    public PoundageDetailCustomize getPoundageDetailById(int id) {
        return this.poundageDetailCustomizeMapper.getPoundageDetailById(id);
    }

    /**
     * 导出手续费分账明细
     *
     * @param response
     * @param poundageCustomize
     * @author wgx
     */
    @Override
    public void exportPoundageDetail(HttpServletResponse response, PoundageCustomize poundageCustomize) {
        // 表格sheet名称
        String sheetName = "手续费分账明细";
        // 查询明细对应的手续费配置项
        PoundageLedgerCustomize poundageLedgerCustomize = this.poundageLedgerCustomizeMapper.getPoundageLedgerById(poundageCustomize.getLedgerId());
        // 根据手续费配置id和分账时间段查询对应的手续费明细
        PoundageDetailCustomize poundageDetailCustomize = new PoundageDetailCustomize();
        poundageDetailCustomize.setLedgerIdSer(poundageCustomize.getLedgerId());
        poundageDetailCustomize.setLedgerTimeSer(Integer.parseInt(poundageCustomize.getPoundageTime()));
        List<PoundageDetailCustomize> recordList = this.poundageDetailCustomizeMapper.getPoundageDetailList(poundageDetailCustomize);
        // excel详细信息
        String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
        String[] titles = new String[]{"序号", "项目编号", "项目类型", "放款/还款时间", "出借人", "出借人分公司",
                "分账类型", "分账来源", "服务费分账比例", "债转服务费分账比例", "管理费分账比例", "分账金额",
                "收款方用户名", "收款方姓名", "收款方电子帐号", "分账状态", "实际分账时间"};
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");
        if (recordList != null && recordList.size() > 0) {
            int sheetCount = 1;
            int rowNum = 0;
            for (int i = 0; i < recordList.size(); i++) {
                rowNum++;
                if (i != 0 && i % 60000 == 0) {
                    sheetCount++;
                    sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
                    rowNum = 1;
                }
                // 新建一行
                Row row = sheet.createRow(rowNum);
                // 循环数据
                for (int celLength = 0; celLength < titles.length; celLength++) {
                    PoundageDetailCustomize bean = recordList.get(i);

                    // 创建相应的单元格
                    Cell cell = row.createCell(celLength);
                    //序号
                    if (celLength == 0) {
                        cell.setCellValue(i + 1);
                    }
                    //项目编号
                    if (celLength == 1) {
                        cell.setCellValue(bean.getBorrowNid());
                    }
                    //项目类型
                    if (celLength == 2) {
                        cell.setCellValue(bean.getBorrowType());
                    }
                    //放款/还款时间
                    if (celLength == 3) {
                        Integer addTime = bean.getAddtime();
                        cell.setCellValue(addTime == null ? "" : GetDate.timestamptoStrYYYYMMDDHHMMSS(addTime));
                    }
                    //出借人
                    if (celLength == 4) {
                        cell.setCellValue(bean.getUsernname());
                    }
                    //分账金额
                    if (celLength == 11) {
                        BigDecimal amount = bean.getAmount();
                        cell.setCellValue(amount != null ? amount.toString() : "");
                    }
                    /*根据详情的ledger_id关联相应的手续费配置项 start*/
                    //分账类型
                    if (celLength == 6) {
                        cell.setCellValue(PoundageLedgerCustomize.getTypeStr(poundageLedgerCustomize.getType()));
                    }
                    //出借人分公司
                    if (celLength == 5) {
                        cell.setCellValue(poundageLedgerCustomize.getInvestorCompany());
                    }
                    //分账来源
                    if (celLength == 7) {
                        cell.setCellValue(PoundageLedgerCustomize.getSourceStr(poundageLedgerCustomize.getSource()));
                    }
                    //服务费分账比例
                    if (celLength == 8) {
                        cell.setCellValue(getRatio(poundageLedgerCustomize.getServiceRatio()));
                    }
                    //债转服务费分账比例
                    if (celLength == 9) {
                        cell.setCellValue(getRatio(poundageLedgerCustomize.getCreditRatio()));
                    }
                    //管理费分账比例
                    if (celLength == 10) {
                        cell.setCellValue(getRatio(poundageLedgerCustomize.getManageRatio()));
                    }
                    //收款人用户名
                    if (celLength == 12) {
                        cell.setCellValue(poundageLedgerCustomize.getUsername());
                    }
                    //收款人姓名
                    if (celLength == 13) {
                        cell.setCellValue(poundageLedgerCustomize.getTruename());
                    }
                    //收款人电子帐号
                    if (celLength == 14) {
                        cell.setCellValue(poundageLedgerCustomize.getAccount());
                    }
                    /*根据详情的ledger_id关联相应的手续费配置项 end*/
                    /*手续费分账项 start*/
                    //分账状态
                    if (celLength == 15) {
                        cell.setCellValue(PoundageCustomize.getStatusStr(poundageCustomize.getStatus()));
                    }
                    //实际分账时间
                    if (celLength == 16) {
                        Integer ledgerTime = poundageCustomize.getAddTime();
                        cell.setCellValue(ledgerTime == null ? "" : GetDate.timestamptoStrYYYYMMDDHHMMSS(ledgerTime));
                    }
                    /*手续费分账项 end*/
                }
            }
        }
        // 导出
        ExportExcel.writeExcelFile(response, workbook, titles, fileName);
    }

    /**
     * 获取分账比例
     *
     * @param ratio
     * @return
     */
    private String getRatio(BigDecimal ratio) {
        if (ratio == null || ratio.compareTo(new BigDecimal(0.00)) == 0) {
            return "--";
        }
        return ratio.toString();
    }
}
