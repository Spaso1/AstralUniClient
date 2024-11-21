package org.au.client.utill;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLogger {
    // 日志级别枚举
    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }

    private String name;
    private Level level;
    private String logDirectory;

    public SimpleLogger(String name, Level level) {
        this.name = name;
        this.level = level;
        this.logDirectory = ".//log//";
        // 确保日志目录存在
        File dir = new File(logDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
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

    // 获取当前日期的字符串表示，用于生成日志文件名
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    // 输出日志的方法
    private void log(Level level, String message) {
        if (this.level.ordinal() <= level.ordinal()) {
            String formattedLog = String.format("[%s] [%-5s] [%-20s] %s",
                    getCurrentTime(), level, name, message);
            System.out.println(formattedLog);
            writeToFile(formattedLog);
        }
    }

    // 将日志写入文件
    private void writeToFile(String logMessage) {
        String logFileName = logDirectory + "log-" + getCurrentDate() + ".txt";
        File logFile = new File(logFileName);
        // 确保日志文件存在
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                System.err.println("Failed to create log file: " + e.getMessage());
                return;
            }
        }
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(logMessage + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Failed to write log to file: " + e.getMessage());
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
