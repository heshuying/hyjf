package com.hyjf.admin.promotion.channel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.Utm;
import com.hyjf.mybatis.model.auto.UtmExample;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;
import com.hyjf.mybatis.model.customize.ChannelCustomize;

@Service
public class ChannelServiceImpl extends BaseServiceImpl implements ChannelService {
	/**
	 * 获取列表
	 * 
	 * @return
	 */
	@Override
	public Integer countList(ChannelCustomize channelCustomize) {
		return channelCustomizeMapper.countList(channelCustomize);
	}

	/**
	 * 获取列表列表
	 * 
	 * @return
	 */
	@Override
	public List<ChannelCustomize> getRecordList(ChannelCustomize channelCustomize) {
		return channelCustomizeMapper.selectList(channelCustomize);
	}

	/**
	 * 获取单个维护
	 * 
	 * @return
	 */
	@Override
	public Utm getRecord(String record) {
		Utm utm = utmMapper.selectByPrimaryKey(Integer.valueOf(record));
		return utm;
	}

	/**
	 * 维护插入
	 * 
	 * @param record
	 */
	@Override
	public void insertRecord(ChannelCustomize channelCustomize) {
		int nowDate = GetDate.getNowTime10();
		Utm record = new Utm();
		record.setSourceId(Integer.valueOf(channelCustomize.getSourceId()));
		if (StringUtils.isNotEmpty(channelCustomize.getSourceId())) {
			List<UtmPlat> utmPlatList = this.getUtmPlat(channelCustomize.getSourceId());
			if (utmPlatList != null && utmPlatList.size() > 0) {
				UtmPlat utmPlat = utmPlatList.get(0);
				record.setUtmSource(utmPlat.getSourceName());
			}
		}
		record.setUtmMedium(channelCustomize.getUtmMedium());
		record.setUtmTerm(channelCustomize.getUtmTerm());
		record.setUtmContent(channelCustomize.getUtmContent());
		record.setUtmCampaign(channelCustomize.getUtmCampaign());

		if (StringUtils.isNotEmpty(channelCustomize.getUtmReferrer())) {
			Users user = this.getUser(channelCustomize.getUtmReferrer(), StringUtils.EMPTY);
			record.setUtmReferrer(user.getUserId());
		}
		if(StringUtils.isNotEmpty(channelCustomize.getLinkAddress())){
            record.setLinkAddress(channelCustomize.getLinkAddress());
        }else {
            record.setLinkAddress("www.hyjf.com");
        }
		record.setStatus(Integer.valueOf(channelCustomize.getStatus()));

		if (StringUtils.isNotEmpty(channelCustomize.getRemark())) {
			record.setRemark(channelCustomize.getRemark());
		} else {
			record.setRemark(StringUtils.EMPTY);
		}

		record.setCreateTime(nowDate);
		utmMapper.insertSelective(record);
	}

	/**
	 * 获取用户
	 * 
	 * @param utmReferrer
	 * @return
	 */
	public Users getUser(String utmReferrer, String userId) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(utmReferrer)) {
			cra.andUsernameEqualTo(utmReferrer);
		}
		if (StringUtils.isNotEmpty(userId)) {
			cra.andUserIdEqualTo(Integer.valueOf(userId));
		}
		List<Users> userList = this.usersMapper.selectByExample(example);
		if (userList != null && userList.size() > 0) {
			return userList.get(0);
		}

		return new Users();
	}

	/**
	 * 维护更新
	 * 
	 * @param record
	 */
	@Override
	public void updateRecord(ChannelCustomize channelCustomize) {
		Utm record = new Utm();
		record.setUtmId(Integer.valueOf(channelCustomize.getUtmId()));
		record.setSourceId(Integer.valueOf(channelCustomize.getSourceId()));

		if (StringUtils.isNotEmpty(channelCustomize.getSourceId())) {
			List<UtmPlat> utmPlatList = this.getUtmPlat(channelCustomize.getSourceId());
			if (utmPlatList != null && utmPlatList.size() > 0) {
				UtmPlat utmPlat = utmPlatList.get(0);
				record.setUtmSource(utmPlat.getSourceName());
			}
		}
		record.setUtmMedium(channelCustomize.getUtmMedium());
		record.setUtmTerm(channelCustomize.getUtmTerm());
		record.setUtmContent(channelCustomize.getUtmContent());
		record.setUtmCampaign(channelCustomize.getUtmCampaign());

		if (StringUtils.isNotEmpty(channelCustomize.getUtmReferrer())) {
			Users user = this.getUser(channelCustomize.getUtmReferrer(), StringUtils.EMPTY);
			record.setUtmReferrer(user.getUserId());
		} else {
			record.setUtmReferrer(0);
		}
		if(StringUtils.isNotEmpty(channelCustomize.getLinkAddress())){
		    record.setLinkAddress(channelCustomize.getLinkAddress());
		}else {
		    record.setLinkAddress("www.hyjf.com");
		}
		record.setStatus(Integer.valueOf(channelCustomize.getStatus()));

		if (StringUtils.isNotEmpty(channelCustomize.getRemark())) {
			record.setRemark(channelCustomize.getRemark());
		} else {
			record.setRemark(StringUtils.EMPTY);
		}
		utmMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 根据主键删除
	 * 
	 * @param recordList
	 */
	@Override
	public void deleteRecord(String sourceId) {
		utmMapper.deleteByPrimaryKey(Integer.valueOf(sourceId));
	}

	/**
	 * 渠道
	 * 
	 * @return
	 */
	@Override
	public List<UtmPlat> getUtmPlat(String sourceId) {
		UtmPlatExample example = new UtmPlatExample();
		UtmPlatExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(sourceId)) {
			cra.andSourceIdEqualTo(Integer.valueOf(sourceId));
		}
		cra.andSourceTypeEqualTo(0);//pc渠道
		cra.andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
		return this.utmPlatMapper.selectByExample(example);
	}

	/**
	 * 获取单表
	 * 
	 * @return
	 */
	@Override
	public Utm getRecord(String sourceId, String utmTerm) {
		if (StringUtils.isNotEmpty(sourceId)) {
			List<UtmPlat> utmPlatList = this.getUtmPlat(sourceId);
			if (utmPlatList != null && utmPlatList.size() > 0) {
				UtmPlat utmPlat = utmPlatList.get(0);
				String utmSource = utmPlat.getSourceName();
				UtmExample example = new UtmExample();
				UtmExample.Criteria cra = example.createCriteria();
				cra.andUtmSourceEqualTo(utmSource);
				cra.andUtmTermEqualTo(utmTerm);

				List<Utm> utmList = this.utmMapper.selectByExample(example);
				if (utmList != null && utmList.size() > 0) {
					return utmList.get(0);
				}
			}
		}

		return null;
	}

	/**
	 * 推荐人用户名
	 * 
	 * @param userId
	 * @return
	 * @author Administrator
	 */
	@Override
	public int checkUtmReferrer(String username) {
		if (StringUtils.isNotEmpty(username)) {
			UsersExample example = new UsersExample();
			UsersExample.Criteria cra = example.createCriteria();
			cra.andUsernameEqualTo(username);
			List<Users> userList = this.usersMapper.selectByExample(example);
			if (userList == null || userList.size() == 0) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONArray uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());

		Iterator<String> itr = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;

		JSONArray jsonArray = new JSONArray();

		List<Utm> utmList = new ArrayList<Utm>();

		while (itr.hasNext()) {
			multipartFile = multipartRequest.getFile(itr.next());
			String fileRealName = String.valueOf(new Date().getTime());
			fileRealName = fileRealName + UploadFileUtils.getSuffix(multipartFile.getOriginalFilename());

			HashMap<String, String> repeatHashMap = new HashMap<String, String>();

			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(multipartFile.getInputStream());

			if (hssfWorkbook != null) {
				for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
					HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
					// 循环行Row
					for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
						if (rowNum == 0) {
							continue;
						}
						Utm utm = new Utm();
						HSSFRow hssfRow = hssfSheet.getRow(rowNum);
						if (hssfRow == null || (hssfRow.getCell(0) == null && hssfRow.getCell(1) == null && hssfRow.getCell(2) == null && hssfRow.getCell(3) == null && hssfRow.getCell(4) == null
								&& hssfRow.getCell(5) == null && hssfRow.getCell(6) == null)) {
							continue;
						}

						// 渠道(utm_source)
						HSSFCell xh = hssfRow.getCell(0);
						boolean sourceIdFlag = false;
						// 必须
						if (xh == null || StringUtils.isEmpty(this.getValue(xh))) {
							String message = ValidatorFieldCheckUtil.getErrorMessage("required").replace("{label}", "第" + (rowNum + 1) + "行的渠道(utm_source)");
							jsonArray.add(message);
						} else {
							UtmPlatExample example = new UtmPlatExample();
							UtmPlatExample.Criteria cra = example.createCriteria();

							cra.andSourceNameEqualTo(this.getValue(xh));
							cra.andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);

							List<UtmPlat> platList = this.utmPlatMapper.selectByExample(example);
							if (platList != null && platList.size() > 0) {
								sourceIdFlag = true;
								utm.setSourceId(platList.get(0).getSourceId());
								utm.setUtmSource(platList.get(0).getSourceName());
							} else {
								String message = "第" + (rowNum + 1) + "行的渠道(utm_source)不存在，请确认渠道管理中存在该推广渠道！";
								jsonArray.add(message);
							}
						}

						// 推广方式(utm_medium)
						xh = hssfRow.getCell(1);

						if (xh != null && !GenericValidator.maxLength(this.getValue(xh), 50)) {
							String message = ValidatorFieldCheckUtil.getErrorMessage("maxlength", 50).replace("{label}", "第" + (rowNum + 1) + "行的推广方式(utm_medium)");
							jsonArray.add(message);
						} else if (xh != null) {
							utm.setUtmMedium(this.getValue(xh));
						}

						// 推广单元(utm_content)
						xh = hssfRow.getCell(2);

						if (xh != null && !GenericValidator.maxLength(this.getValue(xh), 50)) {
							String message = ValidatorFieldCheckUtil.getErrorMessage("maxlength", 50).replace("{label}", "第" + (rowNum + 1) + "行的推广单元(utm_content)");
							jsonArray.add(message);
						} else if (xh != null) {
							utm.setUtmContent(this.getValue(xh));
						}

						// 推广计划(utm_campaign)
						xh = hssfRow.getCell(3);

						if (xh != null && !GenericValidator.maxLength(this.getValue(xh), 50)) {
							String message = ValidatorFieldCheckUtil.getErrorMessage("maxlength", 50).replace("{label}", "第" + (rowNum + 1) + "行的推广计划(utm_campaign)");
							jsonArray.add(message);
						} else if (xh != null) {
							utm.setUtmCampaign(this.getValue(xh));
						}

						// 关键字(utm_term)
						xh = hssfRow.getCell(4);

						if (xh != null && !GenericValidator.maxLength(this.getValue(xh), 50)) {
							String message = ValidatorFieldCheckUtil.getErrorMessage("maxlength", 50).replace("{label}", "第" + (rowNum + 1) + "行的关键字(utm_term)");
							jsonArray.add(message);
						} else if (xh != null) {
							utm.setUtmTerm(this.getValue(xh));
						}

						// 推荐人(utm_referrer)
						xh = hssfRow.getCell(5);
						boolean utmReferrerFlag = false;
						if (xh == null || StringUtils.isEmpty(this.getValue(xh))) {
							String message = ValidatorFieldCheckUtil.getErrorMessage("required").replace("{label}", "第" + (rowNum + 1) + "行的推荐人(utm_referrer)");
							jsonArray.add(message);
						} else if (xh != null) {
							int usersFlag = this.checkUtmReferrer(this.getValue(xh));
							if (usersFlag == 1) {
								String message = "第" + (rowNum + 1) + "行的" + ValidatorFieldCheckUtil.getErrorMessage("referrer_username.not.exists");
								jsonArray.add(message);
							} else {
								if (StringUtils.isNotEmpty(this.getValue(xh))) {
									Users user = this.getUser(this.getValue(xh), StringUtils.EMPTY);
									utmReferrerFlag = true;
									utm.setUtmReferrer(user.getUserId());
								}
							}
						}

						if (utmReferrerFlag && sourceIdFlag) {
							Utm utmexists = this.getRecord(String.valueOf(utm.getSourceId()), utm.getUtmTerm());
							if (utmexists != null) {
								String message = "第" + (rowNum + 1) + "行的" + ValidatorFieldCheckUtil.getErrorMessage("exists.sourceid.utmterm");
								jsonArray.add(message);
							} else {
								String key = String.valueOf(utm.getSourceId()) + utm.getUtmTerm();
								if (repeatHashMap.containsKey(key)) {
									String line = repeatHashMap.get(key);
									String message = "第" + (rowNum + 1) + "行的渠道(utm_source)与关键字(utm_term)和第" + line + "行的渠道(utm_source)与关键字(utm_term)重复";
									jsonArray.add(message);
								} else {
									repeatHashMap.put(key, String.valueOf(rowNum + 1));
								}
							}
						}

						// 备注
						xh = hssfRow.getCell(6);

						if (xh != null && !GenericValidator.maxLength(this.getValue(xh), 100)) {
							String message = ValidatorFieldCheckUtil.getErrorMessage("maxlength", 100).replace("{label}", "第" + (rowNum + 1) + "行的备注(utm_term)");
							jsonArray.add(message);
						} else if (xh != null) {
							utm.setRemark(this.getValue(xh));
						}

						utmList.add(utm);

					}
				}
			}
		}

		if (jsonArray.size() == 0) {
			this.insertRecord(utmList);
		}
		return jsonArray;
	}

	/**
	 * 维护插入
	 * 
	 * @param record
	 */
	@Override
	public void insertRecord(List<Utm> utmList) {
		int nowDate = GetDate.getNowTime10();
		if (utmList != null && utmList.size() > 0) {
			for (Utm record : utmList) {
				record.setStatus(0);
				record.setCreateTime(nowDate);
				this.utmMapper.insertSelective(record);
			}
		}
	}

	/**
	 * 得到Excel表中的值
	 * 
	 * @param hssfCell
	 *            Excel中的每一个格子
	 * @return Excel中每一个格子中的值
	 */
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
}
