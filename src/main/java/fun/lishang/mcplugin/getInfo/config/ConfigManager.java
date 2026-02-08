package fun.lishang.mcplugin.getInfo.config;

import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.logging.LogLevel;
import fun.lishang.mcplugin.getInfo.logging.PluginLogger;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * 配置管理器
 * 负责加载、管理和提供插件配置
 */
public class ConfigManager {
    private final GetInfo plugin;
    private FileConfiguration config;
    private PluginLogger logger;
    
    private static final LogLevel DEFAULT_LOG_LEVEL = LogLevel.NORMAL;
    private static final String DEFAULT_EXPORT_PATH = "plugins/GetInfo/exports/";
    private static final int DEFAULT_RAY_TRACE_DISTANCE = 10;
    private static final String DEFAULT_LANGUAGE = "zh-CN";
    private static final java.util.Set<String> SUPPORTED_LANGUAGES = 
        new java.util.HashSet<>(java.util.Arrays.asList("zh-CN", "en-US", "zh-TW"));
    
    public ConfigManager(GetInfo plugin) {
        this.plugin = plugin;
    }
    
    public void setLogger(PluginLogger logger) {
        this.logger = logger;
    }
    
    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        
        if (logger != null) {
            logger.debug("配置文件已加载");
        }
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        
        if (logger != null) {
            logger.startup("配置文件已重载");
        }
    }
    
    public LogLevel getLogLevel() {
        String levelStr = config.getString("logging.level", DEFAULT_LOG_LEVEL.name());
        
        try {
            return LogLevel.fromString(levelStr);
        } catch (IllegalArgumentException e) {
            if (logger != null) {
                logger.warning("配置文件中的日志级别无效: " + levelStr + 
                    "，使用默认值: " + DEFAULT_LOG_LEVEL.name());
            }
            return DEFAULT_LOG_LEVEL;
        }
    }
    
    public String getExportPath() {
        String path = config.getString("export.path", DEFAULT_EXPORT_PATH);
        
        if (path == null || path.trim().isEmpty()) {
            if (logger != null) {
                logger.warning("配置文件中的导出路径无效，使用默认值: " + DEFAULT_EXPORT_PATH);
            }
            return DEFAULT_EXPORT_PATH;
        }
        
        return path;
    }
    
    public int getRayTraceDistance() {
        int distance = config.getInt("features.ray-trace-distance", DEFAULT_RAY_TRACE_DISTANCE);
        
        if (distance <= 0 || distance > 100) {
            if (logger != null) {
                logger.warning("配置文件中的射线追踪距离无效: " + distance + 
                    "，使用默认值: " + DEFAULT_RAY_TRACE_DISTANCE);
            }
            return DEFAULT_RAY_TRACE_DISTANCE;
        }
        
        return distance;
    }
    
    public String getLanguage() {
        String language = config.getString("features.language", DEFAULT_LANGUAGE);
        
        if (language == null || language.trim().isEmpty() || !SUPPORTED_LANGUAGES.contains(language)) {
            if (logger != null) {
                logger.warning("不支持的语言: " + language + "，使用默认值: " + DEFAULT_LANGUAGE);
            }
            return DEFAULT_LANGUAGE;
        }
        
        return language;
    }
}
