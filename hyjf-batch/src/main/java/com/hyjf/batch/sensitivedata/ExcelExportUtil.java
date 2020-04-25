package com.hyjf.batch.sensitivedata;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/** 
 * @author fengp
 * @version v1.0   
 * @param <T> 
 *   应用泛型，代表任意一个符合javabean风格的类 
 *   byte[]表jpg格式的图片数据 
 */  
public class ExcelExportUtil<T>{  
	
	/**
	 *  防止中文名称 乱码 并指定 数据输出的文件名
	 * @param request
	 * @param response
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public OutputStream downLoadResourcesAttention(HttpServletRequest request,HttpServletResponse response,String fileName) throws IOException{
		
			 /** 获取到 浏览器代理  指定编码 防止乱码**/
		     String agent = request.getHeader("USER-AGENT").toLowerCase();
		     OutputStream out = null;
		     out = response.getOutputStream();// 取得输出流
	         response.reset();// 清空输出流
			 response.setContentType("application/vnd.ms-excel");
	         String codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			 if (agent.contains("firefox")) {
				response.setCharacterEncoding("utf-8");
				response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1") + ".xls");
			 } else {
				response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
			 }
			
	         response.setHeader("Content-disposition",
	                "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + ".xls");// 设定输出文件头
	         response.setContentType("application/msexcel");// 定义输出类型
	        
	         return out;
	}
	/**
	 * 
	* @Title: exportExcelByCondition 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param map 文件名和execl名称
	* @param @param headersName 表头汉字名称
	* @param @param objectFields 实体对应字段
	* @param @param dataToList 数据list
	* @param @param out
	* @param @throws IOException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	 
	public void exportExcelByCondition(Map<String,String> map,String[] headersName, String[] objectFields, 
			List<T> dataToList, OutputStream out) throws IOException{  
		        String title = "";
			    if(map.containsKey("title") && !"".equals(map.get("title"))){
				   title = map.get("title");
			    }
			    try {
			    	exportExcelByCondition(title,headersName,objectFields,dataToList, out);  
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
    /**
     * 说明 : 自定义导出 你想要的Excel数据模板
     * @param title
     *            表格标题名
     * @param headersName
     *            表格属性列名数组
     * @param headersId
     *            表格属性列名对应的字段
     *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public void exportExcelByCondition(String title, String[] headersName,String[] headersId,
    		List<T> dataToList,OutputStream out) {
		// DecimalFormat nf = new DecimalFormat("################################0.00");//数据格式
		try{  
            HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象  
           
            HSSFSheet sheet = workbook.createSheet(title);                  // 创建工作表  
            // 产生表格标题行  
            HSSFRow rowm = sheet.createRow(0);  
            HSSFCell cellTiltle = rowm.createCell(0);  
              
            //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】  
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象  
            //HSSFCellStyle wdStyle = this.getStyle(workbook); 
            HSSFCellStyle valueStyle = this.getStyle(workbook); //单元格样式对象  
              
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (headersName.length-1)));    
            cellTiltle.setCellStyle(columnTopStyle);  
            cellTiltle.setCellValue(title);  
            // 定义所需列数  
            int columnNum = headersName.length;  
            HSSFRow rowRowName = sheet.createRow(1);                // 在索引2的位置创建行(最顶端的行开始的第二行)  
              
            // 将列头设置到sheet的单元格中  
            for(int n=0;n<columnNum;n++){  
                HSSFCell  cellRowName = rowRowName.createCell(n);               //创建列头对应个数的单元格  
                cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);             //设置列头单元格的数据类型  
                HSSFRichTextString text = new HSSFRichTextString(headersName[n].toString());  
                cellRowName.setCellValue(text);                                 //设置列头单元格的值  
                cellRowName.setCellStyle(columnTopStyle);                       //设置列头单元格样式  
            }  
            //将查询出的数据设置到sheet对应的单元格中  
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int count =0;
            if(dataToList!=null){
            	for(int i=0;i<dataToList.size();i++){  
                    HSSFRow row = sheet.createRow(i+2);//创建所需的行数  
                    for(int j=0; j<headersId.length; j++){  
                    	 HSSFCell  cell = null;   
                    	 cell = row.createCell(j);
                    	  //设置单元格的数据类型  
                    	 T t = dataToList.get(i);
                    	 Field field  = t.getClass().getDeclaredField(headersId[j]);
                    	 field.setAccessible(true);
                    	 if(field.getType()==Number.class){
                    		 cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    	 }else{
                    		 cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    	 }
                    	 Object ob = field.get((Object)dataToList.get(i));
                    	 if("Date".equals(field.getType().getSimpleName())){
                    		 cell.setCellValue( sdf.format(ob));
                    	 }else{
                    		 cell.setCellValue(ob==null?"":ob.toString());
                    	 }
                    	 cell.setCellStyle(valueStyle); 
                		 count++;
                		  
                                                    //设置单元格样式  
                    }  
                }  
            }
            //让列宽随着导出的列长自动适应  
            for (int colNum = 0; colNum < columnNum; colNum++) {  
                int columnWidth = sheet.getColumnWidth(colNum) / 100;  
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {  
                    HSSFRow currentRow;  
                    //当前行未被使用过  
                    if (sheet.getRow(rowNum) == null) {  
                        currentRow = sheet.createRow(rowNum);  
                    } else {  
                        currentRow = sheet.getRow(rowNum);  
                    }  
                    if (currentRow.getCell(colNum) != null) {  
                        HSSFCell currentCell = currentRow.getCell(colNum);  
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {  
                            int length = currentCell.getStringCellValue().getBytes().length;  
                            if (columnWidth < length) {  
                                columnWidth = length;  
                            }  
                        }  
                    }  
                }  
                if(colNum == 0){  
                    sheet.setColumnWidth(colNum, (columnWidth-2) * 256);  
                }else{  
                    sheet.setColumnWidth(colNum, (columnWidth+4) * 256);  
                }  
            }  
            try  
            {  
                /** 给前端页面的 提示信息 */
                //JOptionPane.showMessageDialog(null, "导出成功!");  
                workbook.write(out);  
                out.close();  
            }catch (IOException e)  
            {   
                e.printStackTrace();  
            }    
  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
      
    }
    private HSSFCellStyle getStyle(HSSFWorkbook workbook) {
		// 设置字体  
        HSSFFont font = workbook.createFont();  
        //设置字体大小  
        //font.setFontHeightInPoints((short)10);  
        //字体加粗  
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        //设置字体名字   
        font.setFontName("Courier New");  
        //设置样式;   
        HSSFCellStyle style = workbook.createCellStyle();  
        //设置底边框;   
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        //设置底边框颜色;    
        style.setBottomBorderColor(HSSFColor.BLACK.index);  
        //设置左边框;     
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        //设置左边框颜色;   
        style.setLeftBorderColor(HSSFColor.BLACK.index);  
        //设置右边框;   
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        //设置右边框颜色;   
        style.setRightBorderColor(HSSFColor.BLACK.index);  
        //设置顶边框;   
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        //设置顶边框颜色;    
        style.setTopBorderColor(HSSFColor.BLACK.index);  
        //在样式用应用设置的字体;    
        style.setFont(font);  
        //设置自动换行;   
        style.setWrapText(false);  
        //设置水平对齐的样式为居中对齐;    
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);  
        //设置垂直对齐的样式为居中对齐;   
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
         
      
        HSSFDataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("@"));
     
        
        return style;  
	}
    private HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
		// 设置字体  
        HSSFFont font = workbook.createFont();  
        //设置字体大小  
        font.setFontHeightInPoints((short)11);  
        //字体加粗  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        //设置字体名字   
        font.setFontName("Courier New");  
        //设置样式;   
        HSSFCellStyle style = workbook.createCellStyle();  
        //设置底边框;   
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        //设置底边框颜色;    
        style.setBottomBorderColor(HSSFColor.BLACK.index);  
        //设置左边框;     
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        //设置左边框颜色;   
        style.setLeftBorderColor(HSSFColor.BLACK.index);  
        //设置右边框;   
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        //设置右边框颜色;   
        style.setRightBorderColor(HSSFColor.BLACK.index);  
        //设置顶边框;   
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        //设置顶边框颜色;    
        style.setTopBorderColor(HSSFColor.BLACK.index);  
        //在样式用应用设置的字体;    
        style.setFont(font);  
        //设置自动换行;   
        style.setWrapText(false);  
        //设置水平对齐的样式为居中对齐;    
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        //设置垂直对齐的样式为居中对齐;   
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
          
        return style; 
	}
    
    //将模板生成包存到本地
    public void writeModel(String path,String title, String[] headersName){
    	OutputStream out;
		try {
			out = new FileOutputStream(path+"/"+title+".xls");
			this.exportExcelByCondition(title,headersName,null,null,out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    }
	/**
	 * 说明 : 自定义导出 你想要的Excel数据模板，ByteArrayOutputStream
	 * @param title
	 *            表格标题名
	 * @param headersName
	 *            表格属性列名数组
	 * @param headersId
	 *            表格属性列名对应的字段
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public InputStreamSource exportExcelByCondition(String title, String[] headersName,String[] headersId, List<T> dataToList) {
		InputStreamSource is  = null;
		// DecimalFormat nf = new DecimalFormat("################################0.00");//数据格式
		try{
			HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象

			HSSFSheet sheet = workbook.createSheet(title);                  // 创建工作表
			// 产生表格标题行
			HSSFRow rowm = sheet.createRow(0);
			HSSFCell cellTiltle = rowm.createCell(0);

			//sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】
			HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象
			//HSSFCellStyle wdStyle = this.getStyle(workbook);
			HSSFCellStyle valueStyle = this.getStyle(workbook); //单元格样式对象

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (headersName.length-1)));
			cellTiltle.setCellStyle(columnTopStyle);
			cellTiltle.setCellValue(title);
			// 定义所需列数
			int columnNum = headersName.length;
			HSSFRow rowRowName = sheet.createRow(1);                // 在索引2的位置创建行(最顶端的行开始的第二行)

			// 将列头设置到sheet的单元格中
			for(int n=0;n<columnNum;n++){
				HSSFCell  cellRowName = rowRowName.createCell(n);               //创建列头对应个数的单元格
				cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);             //设置列头单元格的数据类型
				HSSFRichTextString text = new HSSFRichTextString(headersName[n].toString());
				cellRowName.setCellValue(text);                                 //设置列头单元格的值
				cellRowName.setCellStyle(columnTopStyle);                       //设置列头单元格样式
			}
			//将查询出的数据设置到sheet对应的单元格中
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int count =0;
			if(dataToList!=null){
				for(int i=0;i<dataToList.size();i++){
					HSSFRow row = sheet.createRow(i+2);//创建所需的行数
					for(int j=0; j<headersId.length; j++){
						HSSFCell  cell = null;
						cell = row.createCell(j);
						//设置单元格的数据类型
						T t = dataToList.get(i);
						Field field  = t.getClass().getDeclaredField(headersId[j]);
						field.setAccessible(true);
						if(field.getType()==Number.class){
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						}else{
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						}
						Object ob = field.get((Object)dataToList.get(i));
						if("Date".equals(field.getType().getSimpleName())){
							cell.setCellValue( sdf.format(ob));
						}else{
							cell.setCellValue(ob==null?"":ob.toString());
						}
						cell.setCellStyle(valueStyle);
						count++;

						//设置单元格样式
					}
				}
			}
			//让列宽随着导出的列长自动适应
			for (int colNum = 0; colNum < columnNum; colNum++) {
				int columnWidth = sheet.getColumnWidth(colNum) / 100;
				for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
					HSSFRow currentRow;
					//当前行未被使用过
					if (sheet.getRow(rowNum) == null) {
						currentRow = sheet.createRow(rowNum);
					} else {
						currentRow = sheet.getRow(rowNum);
					}
					if (currentRow.getCell(colNum) != null) {
						HSSFCell currentCell = currentRow.getCell(colNum);
						if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							int length = currentCell.getStringCellValue().getBytes().length;
							if (columnWidth < length) {
								columnWidth = length;
							}
						}
					}
				}
				if(colNum == 0){
					sheet.setColumnWidth(colNum, (columnWidth-2) * 256);
				}else{
					sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
				}
			}
			try
			{
				/** 给前端页面的 提示信息 */
				//JOptionPane.showMessageDialog(null, "导出成功!");
				ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
				workbook.write(os);
				is = new ByteArrayResource(os.toByteArray());
				os.close();
			}catch (IOException e)
			{
				e.printStackTrace();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return is;
	}


}
