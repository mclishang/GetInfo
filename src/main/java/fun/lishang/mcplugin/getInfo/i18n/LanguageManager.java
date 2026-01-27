package fun.lishang.mcplugin.getInfo.i18n;

import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.logging.PluginLogger;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 语言管理器
 * 负责加载和管理多语言支持
 */
public class LanguageManager {
    private final GetInfo plugin;
    private YamlConfiguration langConfig;
    private String currentLanguage;
    private PluginLogger logger;
    
    private static final String DEFAULT_LANGUAGE = "zh-CN";
    private static final String LANG_PATH_PREFIX = "lang/";
    private static final String LANG_FILE_SUFFIX = ".yml";
    
    public LanguageManager(GetInfo plugin) {
        this.plugin = plugin;
    }
    
    /**
     * 设置日志记录器
     * 
     * @param logger 插件日志记录器
     */
    public void setLogger(PluginLogger logger) {
        this.logger = logger;
    }
    
    /**
     * 加载语言文件
     * 
     * @param language 语言代码（zh-CN, en-US, zh-TW）
     */
    public void loadLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            language = DEFAULT_LANGUAGE;
        }
        
        YamlConfiguration config = loadLanguageFromJar(language);
        
        if (config == null) {
            if (logger != null) {
                logger.warning("无法加载语言文件: " + language + "，回退到默认语言: " + DEFAULT_LANGUAGE);
            }
            
            if (!language.equals(DEFAULT_LANGUAGE)) {
                config = loadLanguageFromJar(DEFAULT_LANGUAGE);
            }
            
            if (config == null) {
                if (logger != null) {
                    logger.severe("无法加载默认语言文件: " + DEFAULT_LANGUAGE);
                }
                langConfig = new YamlConfiguration();
                currentLanguage = DEFAULT_LANGUAGE;
                return;
            }
            
            currentLanguage = DEFAULT_LANGUAGE;
        } else {
            currentLanguage = language;
        }
        
        langConfig = config;
        
        if (logger != null) {
            logger.debug("已加载语言文件: " + currentLanguage);
        }
    }
    
    /**
     * 从 JAR 内部加载语言文件
     * 
     * @param language 语言代码
     * @return YamlConfiguration 对象，如果加载失败则返回 null
     */
    private YamlConfiguration loadLanguageFromJar(String language) {
        String fileName = LANG_PATH_PREFIX + language + LANG_FILE_SUFFIX;
        
        try (InputStream inputStream = plugin.getResource(fileName)) {
            if (inputStream == null) {
                if (logger != null) {
                    logger.debug("语言文件不存在: " + fileName);
                }
                return null;
            }
            
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            YamlConfiguration config = YamlConfiguration.loadConfiguration(reader);
            
            if (logger != null) {
                logger.debug("成功从 JAR 加载语言文件: " + fileName);
            }
            
            return config;
        } catch (Exception e) {
            if (logger != null) {
                logger.severe("加载语言文件时发生错误: " + fileName + " - " + e.getMessage());
            }
            return null;
        }
    }
    
    /**
     * 获取翻译消息
     * 
     * @param key 消息键（支持点号分隔的路径，如 "common.prefix"）
     * @return 翻译后的消息，如果键不存在则返回键本身
     */
    public String getMessage(String key) {
        if (langConfig == null) {
            return key;
        }
        
        String message = langConfig.getString(key);
        
        if (message == null) {
            if (logger != null) {
                logger.debug("语言键不存在: " + key);
            }
            return key;
        }
        
        return translateColorCodes(message);
    }
    
    /**
     * 获取翻译消息（带占位符替换）
     * 
     * @param key 消息键
     * @param placeholders 占位符映射（键为占位符名称，值为替换内容）
     * @return 翻译并替换占位符后的消息
     */
    public String getMessage(String key, Map<String, String> placeholders) {
        String message = getMessage(key);
        
        if (placeholders == null || placeholders.isEmpty()) {
            return message;
        }
        
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            message = message.replace(placeholder, entry.getValue());
        }
        
        return message;
    }
    
    /**
     * 转换颜色代码
     * 将 & 符号转换为 Minecraft 颜色代码符号 §
     * 
     * @param text 原始文本
     * @return 转换后的文本
     */
    private String translateColorCodes(String text) {
        if (text == null) {
            return null;
        }
        
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    /**
     * 获取当前语言代码
     * 
     * @return 当前使用的语言代码
     */
    public String getCurrentLanguage() {
        return currentLanguage;
    }
}
