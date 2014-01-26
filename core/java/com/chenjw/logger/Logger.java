package com.chenjw.logger;


/**
 * 日志包装
 * 
 * @author chenjw
 * 
 */
public class Logger {
    
    public static Logger getLogger(Class<?> clazz) {
        return new Logger();
    }

    public static Logger getLogger(String name) {
        return new Logger();
    }
    
    public Logger() {

    }

    public boolean isDebugEnabled() {
        return true;
    }
    
    public boolean isInfoEnabled() {
        return true;
    }

    // /////////

    public void trace(Object key) {
        System.out.println(key);
    }

    public void trace(Object key, Throwable cause) {
        System.out.println(key);
        cause.printStackTrace();
    }

    public void debug(Object key) {
        System.out.println(key);
    }

    public void debug(Object key, Throwable cause) {
        System.out.println(key);
        cause.printStackTrace();
    }

    public void info(Object key) {
        System.out.println(key);
    }

    public void info(Object key, Throwable cause) {
        System.out.println(key);
        cause.printStackTrace();
    }

    public void warn(Object key) {
        System.out.println(key);
    }

    public void warn(Object key, Throwable cause) {
        System.out.println(key);
        cause.printStackTrace();
    }

    public void error(Object key) {
        System.out.println(key);
    }

    public void error(Object key, Throwable cause) {
        System.out.println(key);
        cause.printStackTrace();
    }

    public void fatal(Object key) {
        System.out.println(key);
    }

    public void fatal(Object key, Throwable cause) {
        System.out.println(key);
        cause.printStackTrace();
    }

}
