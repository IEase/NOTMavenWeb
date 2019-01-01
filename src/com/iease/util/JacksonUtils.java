package com.iease.util;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

/**
 * Json实用类，扩展封装Jackson。源码来源于ME项目
 * 
 * @author 陆翔 431520
 * @version 1.0, 2015�?8�?11�?
 * @see JacksonUtils
 */
@SuppressWarnings("serial")
public class JacksonUtils implements Serializable {
	private static final Logger logger = Logger.getLogger(JacksonUtils.class);

	private static ObjectMapper mapper;

	private static String PATTERN = "yyyy-MM-dd";
	
	public static final String DATE_FORMART = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	static {
		mapper = new ObjectMapper();
		// 属�?�名称没有用""括起�? ,�?要加上配�?
		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属�?
		mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);

		// mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//		mapper.setDateFormat(new SimpleDateFormat(PATTERN));
	}

	/**
	 * Object对象转Json字符串，Object对象可以是一个实体类、null、集合�?�数组�?�List、Map等�??
	 * 
	 * <ul>
	 * <li>Object为null时，返回null</li>
	 * <li>Object为集合，集合为空时，返回[]</li>
	 * </ul>
	 * 
	 * @param obj
	 * @return
	 * @see JacksonUtils#toJson
	 */
	public static String toJson(Object obj) {
		return toJson(obj, PATTERN);
	}

	/**
	 * Object对象转Json字符串，Object对象可以是一个实体类、null、集合�?�数组�?�List、Map等�??
	 * 
	 * <ul>
	 * <li>Object为null时，返回null</li>
	 * <li>Object为集合，集合为空时，返回[]</li>
	 * </ul>
	 * 
	 * @param obj
	 * @param dateFormat
	 * @return
	 * @see JacksonUtils#toJson
	 */
	public static String toJson(Object obj, String dateFormat) {
		try {
			setDateFormat(dateFormat);
			return mapper.writeValueAsString(obj);
		} catch (IOException e) {
			e.printStackTrace();
			logger.warn("write to json string error:" + obj, e);
			return null;
		}
	}

	/**
	 * Json字符串转Object对象�?
	 * 
	 * <ul>
	 * <li>如果JSON字符串为null�?"null"字符串，返回Null</li>
	 * <li>如果JSON字符串为"[]"，返回空集合</li>
	 * <li>如需读取集合如List/Map，且不是List<String>这种�?单类型时使用如下语句</li>
	 * </ul>
	 * 
	 * @param jsonString
	 * @param clazz
	 * @return
	 * @see JacksonUtils#fromJson
	 */
	public static <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			setDateFormat(PATTERN);
			return mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * Json字符串转Object对象�?
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJsonByDateFormat(String jsonString, Class<T> clazz, String dateFormat) {
	    if (StringUtils.isEmpty(jsonString)) {
	        return null;
	    }

	    try {
	        setDateFormat(dateFormat);
	        return mapper.readValue(jsonString, clazz);
	    } catch (IOException e) {
	        logger.warn("parse json string error:" + jsonString, e);
	        return null;
	    }
	}

	/**
	 * Json字符串转List结合
	 * 
	 * @param jsonString
	 * @param clazz
	 * @return
	 * @see JacksonUtils#json2List
	 */
	public static <T> List<T> json2List(String jsonString, Class<?> clazz) {
		TypeFactory t = TypeFactory.defaultInstance();
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			setDateFormat(PATTERN);
			// 指定容器结构和类型（这里是ArrayList和clazz�?
			List<T> list = mapper.readValue(jsonString, t.constructCollectionType(ArrayList.class, clazz));
			// 如果T确定的情况下可以使用下面的方法：
			// List<T> list = mapper.readValue(jsonString, new
			// TypeReference<List<T>>(){});

			return list;
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * 设置转换日期类型的format pattern，如果不设置默认打印Timestamp毫秒数�??
	 * 
	 * @param pattern
	 * @see JacksonUtils#setDateFormat
	 */
	public static void setDateFormat(String pattern) {
		if (StringUtils.isNotBlank(pattern)) {
			DateFormat df = new SimpleDateFormat(pattern);
			mapper.setDateFormat(df);
		}else{
		    mapper.setDateFormat(null);
		}
	}

}
