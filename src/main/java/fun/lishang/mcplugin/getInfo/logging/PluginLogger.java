package fun.lishang.mcplugin.getInfo.logging;

import java.util.logging.Logger;

public class PluginLogger {
    private final Logger logger;
    private LogLevel currentLevel;
    
    public PluginLogger(Logger logger, LogLevel level) {
        this.logger = logger;
        this.currentLevel = level;
    }
    
    public void setLevel(LogLevel level) {
        this.currentLevel = level;
    }
    
    public LogLevel getLevel() {
        return this.currentLevel;
    }
    
    public void debug(String message) {
        if (currentLevel.shouldLog(LogLevel.DEBUG)) {
            logger.info("[DEBUG] " + message);
        }
    }
    
    public void info(String message) {
        if (currentLevel.shouldLog(LogLevel.NORMAL)) {
            logger.info(message);
        }
    }
    
    public void startup(String message) {
        if (currentLevel.shouldLog(LogLevel.MINIMAL)) {
            logger.info(message);
        }
    }
    
    public void warning(String message) {
        if (currentLevel.shouldLog(LogLevel.MINIMAL)) {
            logger.warning(message);
        }
    }
    
    public void severe(String message) {
        logger.severe(message);
    }
}
