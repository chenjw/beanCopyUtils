package com.chenjw.tools.beancopy.converter;

import java.sql.Timestamp;
import java.util.Date;

import com.chenjw.tools.beancopy.ClassifiedConverter;
import com.chenjw.tools.beancopy.util.DateUtils;

/**
 * Date类型和Timestamp类型到String类型的转换
 * 
 * @author chenjw 2012-1-10 下午3:23:43
 */
public class DateToStringConverter implements ClassifiedConverter {

	/**
	 * 时间格式
	 */
	private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz) {
		return DateUtils.toLocaleString((Date) origin, TIME_FORMAT);
	}

	public Class<?>[] getOriginClazzes() {
		return new Class<?>[] { Date.class, Timestamp.class };
	}

	public Class<?>[] getDestClazzes() {
		return new Class<?>[] { String.class };
	}

}
