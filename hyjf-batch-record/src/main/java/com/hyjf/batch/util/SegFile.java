/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: Michael
 * @version: 1.0
 * Created at: 2017年4月17日 上午9:32:17
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.batch.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.hyjf.common.util.PropUtils;

/**
 * 分割文件
 * @author Michael
 */

public class SegFile {
	
	
	/** 服务器地址  */
	public static String HOST = PropUtils.getSystem("hyjf.file.server.host").trim();
	
	/** 分割文件地址  */
	public static String sourceFilePath = PropUtils.getSystem("hyjf.file.server.upload.url").trim();
	
	/** 目标文件地址  */
	public static String targetDirectoryPath = PropUtils.getSystem("hyjf.file.server.upload.url").trim();
	
//	/** 分割文件地址  */
//	public static String sourceFilePath = "C:/Users/Michael/Desktop/test/";
//	
//	/** 目标文件地址  */
//	public static String targetDirectoryPath = "C:/Users/Michael/Desktop/test/";

	
	
	
	/**
	   *根据需求,直接调用静态方法start来执行操作
	   *参数:
	   *  rows 为多少行一个文件 int 类型
	   *  sourceFilePath 为源文件路径 String 类型
	   *  targetDirectoryPath 为文件分割后存放的目标目录 String 类型
	   *  fileType 0 批量开户 1 债权迁移 2 标的迁移 3 签约关系迁移
	   */
	 public static String start(int rows,String fileName,int fileType){
		  StringBuilder sb = new StringBuilder();
		  
		  File sourceFile = new File(sourceFilePath + fileName);
		  File targetFile = new File(targetDirectoryPath);
		  if(!sourceFile.exists() || rows <= 0 || sourceFile.isDirectory()){
			  return "源文件不存在或者输入了错误的行数";
		  }
		  if(targetFile.exists()){
			  if(!targetFile.isDirectory()){
				  return "目标文件夹错误,不是一个文件夹";
			  }
		  }else{
			  targetFile.mkdirs();
		  }
		  try{
			  BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(sourceFilePath + fileName),"GBK")); 
			  BufferedWriter bw = null;
			  String str = "";
			  //新文件名称
			  String newFileName = "";
			  String tempData = br.readLine();
			  int i=1,s=0;
			  while(tempData != null){
				  str += tempData+"\r\n";
				  if(i%rows == 0){
					  switch (fileType) {
						case 0: //开户文件
							newFileName = TransUtil.createFilesName(0);
							break;
						case 1: //债权迁移
							newFileName = TransUtil.createFilesName(1);
							break;
						case 2: //标的迁移
							newFileName = TransUtil.createFilesName(2);
							break;
						case 3: //签约关系迁移
							newFileName = TransUtil.createFilesName(3);
							break;
						default:
							break;
						}
					  bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(targetFile.getAbsolutePath()+"/"+s+"_"+newFileName)),"GBK"));
					  bw.write(str);
					  bw.close();
					  str = "";
					  s += 1;
					  sb.append(s+"_"+newFileName).append(";");
				  }
				  i++;
				  tempData = br.readLine();
			  }
			  if((i-1) % rows != 0){
				  
				  switch (fileType) {
					case 0: //开户文件
						newFileName = TransUtil.createFilesName(0);
						break;
					case 1: //债权迁移
						newFileName = TransUtil.createFilesName(1);
						break;
					case 2: //标的迁移
						newFileName = TransUtil.createFilesName(2);
						break;
					case 3: //签约关系迁移
						newFileName = TransUtil.createFilesName(3);
						break;
					default:
						break;
					}
				 bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(targetFile.getAbsolutePath()+"/"+s+"_"+newFileName)),"GBK"));
				 bw.write(str);
				 bw.close();
				 br.close();
				 s += 1;
				 sb.append(s+"_"+newFileName).append(";");
			  }
			  sb.append("--文件分割结束,共分割成了"+s+"个文件");
		   }catch(Exception e){
		 	  e.printStackTrace();
		   }
		  return sb.toString();
	 }
	 //测试
	 public static void main(String args[]){
		 SegFile.start(9999,"3005-APPZX0090-100001-20160905",0);
	 }
	 
}

	