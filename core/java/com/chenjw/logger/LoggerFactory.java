package com.chenjw.logger;

/**
 * 日志包装
 * 
 * @author chenjw
 * 
 */
public class LoggerFactory {

	public static Logger getLogger(Class<?> clazz) {
		return new Logger(clazz);
	}

	public static Logger getLogger(String name) {
		return new Logger(name);
	}

}
