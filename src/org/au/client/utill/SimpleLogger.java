package org.au.client.utill;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLogger {
    // 日志级别枚举
    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }

    private String name;
    private Level level;

    public SimpleLogger(String name, Level level) {
        this.name = name;
        this.level = level;
    }

    // 设置日志级别
    public void setLevel(Level level) {
        this.level = level;
    }

    // 获取当前时间的字符串表示
    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(new Date());
    }

    // 输出日志的方法
    private void log(Level level, String message) {
        if (this.level.ordinal() <= level.ordinal()) {
            String formattedLog = String.format("[%s] [%-5s] [%-20s] %s",
                    getCurrentTime(), level, name, message);
            System.out.println(formattedLog);
        }
    }

    // 不同级别的日志方法
    public void debug(String message) {
        log(Level.DEBUG, message);
    }

    public void info(String message) {
        log(Level.INFO, message);
    }

    public void warn(String message) {
        log(Level.WARN, message);
    }

    public void error(String message) {
        log(Level.ERROR, message);
    }
}
