package com.chenjw.logger;

/**
 * 日志包装
 * 
 * @author chenjw
 * 
 */
public class Logger {
    private org.apache.log4j.Logger logger;

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz);
    }

    public static Logger getLogger(String name) {
        return new Logger(name);
    }

    public Logger(Class<?> clazz) {
        this.logger = org.apache.log4j.Logger.getLogger(clazz);
    }

    public Logger(String name) {
        this.logger = org.apache.log4j.Logger.getLogger(name);
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    // /////////

    public void trace(Object key) {
        logger.trace(key);
    }

    public void trace(Object key, Throwable cause) {
        logger.trace(key, cause);
    }

    public void debug(Object key) {
        logger.debug(key);
    }

    public void debug(Object key, Throwable cause) {
        logger.debug(key, cause);
    }

    public void info(Object key) {
        logger.info(key);
    }

    public void info(Object key, Throwable cause) {
        logger.info(key, cause);
    }

    public void warn(Object key) {
        logger.warn(key);
    }

    public void warn(Object key, Throwable cause) {
        logger.warn(key, cause);
    }

    public void error(Object key) {
        logger.error(key);
    }

    public void error(Object key, Throwable cause) {
        logger.error(key, cause);
    }

    public void fatal(Object key) {
        logger.fatal(key);
    }

    public void fatal(Object key, Throwable cause) {
        logger.fatal(key, cause);
    }

}
