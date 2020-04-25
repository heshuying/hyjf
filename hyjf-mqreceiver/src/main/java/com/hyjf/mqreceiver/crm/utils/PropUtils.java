package com.hyjf.mqreceiver.crm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>
 * 属性文件
 * </p>
 *
 * @version 1.0.0
 */
public class PropUtils {


	protected static final String SYSTEM_RESOURCES_PROPERTIES_FILE_NAME = "properties/crm.properties";

	/**
	 * 取得所有属性值
	 * 
	 * @param propertiesFileName
	 * @return
	 * @throws IOException
	 */
	protected static Properties getProperties(String propertiesFileName) throws IOException {
		Properties prop = new Properties();
		InputStream is = PropUtils.class.getClassLoader().getResourceAsStream(propertiesFileName);

		try {
			prop.load(is);
		} catch (IOException e) {
			throw e;
		}

		return prop;
	}


	/**
	 * 取得System文件
	 *
	 * @return
	 * @throws IOException
	 */
	public static Properties getCrmResourcesProperties() {
		try {
			return getProperties(SYSTEM_RESOURCES_PROPERTIES_FILE_NAME);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}


	/**
	 * 取得System文件的属性值
	 *
	 * @return
	 * @throws IOException
	 */
	public static String getSystem(String key) {
		Properties properties = getCrmResourcesProperties();
		return properties.getProperty(key);
	}

}
