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
    
    private static final String MIN_VERSION = "1.20.1";
    private static final String MAX_VERSION = "1.21.X";
    private static final int MIN_MAJOR = 1;
    private static final int MIN_MINOR = 20;
    private static final int MIN_PATCH = 1;
    
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private PluginLogger pluginLogger;
    private YamlExporter exporter;

    @Override
    public void onEnable() {
        try {
            if (!checkServerVersion()) {
                getLogger().severe("不支持的服务器版本！插件需要 Minecraft " + MIN_VERSION + " - " + MAX_VERSION);
                getLogger().severe("当前版本: " + Bukkit.getVersion());
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            
            configManager = new ConfigManager(this);
            configManager.loadConfig();
            
            languageManager = new LanguageManager(this);
            languageManager.loadLanguage(configManager.getLanguage());
            
            pluginLogger = new PluginLogger(getLogger(), configManager.getLogLevel());
            
            pluginLogger.startup("GetInfo 插件正在启动...");
            pluginLogger.startup("版本: " + getDescription().getVersion());
            pluginLogger.startup("支持的 Minecraft 版本: " + MIN_VERSION + " - " + MAX_VERSION);
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
    
    private boolean checkServerVersion() {
        String version = Bukkit.getBukkitVersion().split("-")[0];
        String[] parts = version.split("\\.");
        
        if (parts.length < 2) {
            return false;
        }
        
        try {
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
            
            if (major != MIN_MAJOR) {
                return false;
            }
            
            if (minor == MIN_MINOR) {
                return patch >= MIN_PATCH;
            }
            
            return minor >= 21;
        } catch (NumberFormatException e) {
            getLogger().warning("无法解析版本号: " + version);
            return false;
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
