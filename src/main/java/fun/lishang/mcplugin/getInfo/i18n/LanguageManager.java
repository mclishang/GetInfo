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
    
    public void setLogger(PluginLogger logger) {
        this.logger = logger;
    }
    
    public void loadLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            language = DEFAULT_LANGUAGE;
        }
        
        YamlConfiguration config = loadLanguageFromJar(language);
        
        if (config == null && !language.equals(DEFAULT_LANGUAGE)) {
            if (logger != null) {
                logger.warning("无法加载语言文件: " + language + "，回退到默认语言: " + DEFAULT_LANGUAGE);
            }
            config = loadLanguageFromJar(DEFAULT_LANGUAGE);
            language = DEFAULT_LANGUAGE;
        }
        
        if (config == null) {
            if (logger != null) {
                logger.severe("无法加载默认语言文件: " + DEFAULT_LANGUAGE);
            }
            langConfig = new YamlConfiguration();
            currentLanguage = DEFAULT_LANGUAGE;
            return;
        }
        
        langConfig = config;
        currentLanguage = language;
        
        if (logger != null) {
            logger.debug("已加载语言文件: " + currentLanguage);
        }
    }
    
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
    
    public String getMessage(String key, Map<String, String> placeholders) {
        String message = getMessage(key);
        
        if (placeholders == null || placeholders.isEmpty()) {
            return message;
        }
        
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        
        return message;
    }
    
    private String translateColorCodes(String text) {
        return text == null ? null : ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public String getCurrentLanguage() {
        return currentLanguage;
    }
}
