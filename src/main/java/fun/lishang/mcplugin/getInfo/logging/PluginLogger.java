package fun.lishang.mcplugin.getInfo.logging;

import java.util.logging.Logger;

/**
 * 插件日志管理器
 * 提供基于日志级别的过滤功能
 */
public class PluginLogger {
    private final Logger logger;
    private LogLevel currentLevel;
    
    /**
     * 创建插件日志管理器
     * 
     * @param logger Bukkit 日志记录器
     * @param level 初始日志级别
     */
    public PluginLogger(Logger logger, LogLevel level) {
        this.logger = logger;
        this.currentLevel = level;
    }
    
    /**
     * 设置日志级别
     * 
     * @param level 新的日志级别
     */
    public void setLevel(LogLevel level) {
        this.currentLevel = level;
    }
    
    /**
     * 获取当前日志级别
     * 
     * @return 当前日志级别
     */
    public LogLevel getLevel() {
        return this.currentLevel;
    }
    
    /**
     * 输出调试信息
     * 仅在 DEBUG 级别时输出
     * 
     * @param message 日志消息
     */
    public void debug(String message) {
        if (currentLevel.shouldLog(LogLevel.DEBUG)) {
            logger.info("[DEBUG] " + message);
        }
    }
    
    /**
     * 输出普通信息
     * 在 NORMAL 及以上级别时输出
     * 
     * @param message 日志消息
     */
    public void info(String message) {
        if (currentLevel.shouldLog(LogLevel.NORMAL)) {
            logger.info(message);
        }
    }
    
    /**
     * 输出启动/关闭信息
     * 在 MINIMAL 及以上级别时输出
     * 
     * @param message 日志消息
     */
    public void startup(String message) {
        if (currentLevel.shouldLog(LogLevel.MINIMAL)) {
            logger.info(message);
        }
    }
    
    /**
     * 输出警告信息
     * 在 MINIMAL 及以上级别时输出
     * 
     * @param message 日志消息
     */
    public void warning(String message) {
        if (currentLevel.shouldLog(LogLevel.MINIMAL)) {
            logger.warning(message);
        }
    }
    
    /**
     * 输出严重错误信息
     * 始终输出，不受日志级别限制
     * 
     * @param message 日志消息
     */
    public void severe(String message) {
        logger.severe(message);
    }
}
