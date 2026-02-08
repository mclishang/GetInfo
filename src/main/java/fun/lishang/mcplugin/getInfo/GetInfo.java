package fun.lishang.mcplugin.getInfo;

import fun.lishang.mcplugin.getInfo.command.GetInfoCommand;
import fun.lishang.mcplugin.getInfo.config.ConfigManager;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import fun.lishang.mcplugin.getInfo.logging.PluginLogger;
import fun.lishang.mcplugin.getInfo.util.YamlExporter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class GetInfo extends JavaPlugin {
    
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private PluginLogger pluginLogger;
    private YamlExporter exporter;

    @Override
    public void onEnable() {
        try {
            configManager = new ConfigManager(this);
            configManager.loadConfig();
            
            languageManager = new LanguageManager(this);
            languageManager.loadLanguage(configManager.getLanguage());
            
            pluginLogger = new PluginLogger(getLogger(), configManager.getLogLevel());
            
            pluginLogger.startup("GetInfo 插件正在启动...");
            pluginLogger.startup("版本: " + getDescription().getVersion());
            pluginLogger.startup("当前服务器版本: " + Bukkit.getVersion());
            
            checkNBTAPI();
            
            exporter = new YamlExporter(this);
            pluginLogger.debug("导出器已初始化");
            
            registerCommands();
            pluginLogger.debug("命令已注册");
            
            pluginLogger.startup("GetInfo 插件启动完成！");
            
        } catch (Exception e) {
            getLogger().severe("插件启动失败: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (pluginLogger != null) {
            pluginLogger.startup("GetInfo 插件正在关闭...");
        }
    }
    
    private void checkNBTAPI() {
        try {
            Class.forName("de.tr7zw.nbtapi.NBTItem");
            pluginLogger.startup("NBT-API 已加载（外部依赖）");
        } catch (ClassNotFoundException e) {
            pluginLogger.warning("NBT-API 类未找到，某些功能可能无法正常工作");
        }
    }
    
    private void registerCommands() {
        PluginCommand command = getCommand("getinfo");
        if (command != null) {
            GetInfoCommand executor = new GetInfoCommand(this);
            command.setExecutor(executor);
            command.setTabCompleter(executor);
            pluginLogger.debug("已注册命令: /getinfo");
        } else {
            pluginLogger.warning("无法注册命令 /getinfo - 请检查 plugin.yml");
        }
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public LanguageManager getLanguageManager() {
        return languageManager;
    }
    
    public PluginLogger getPluginLogger() {
        return pluginLogger;
    }
    
    public YamlExporter getExporter() {
        return exporter;
    }
}
