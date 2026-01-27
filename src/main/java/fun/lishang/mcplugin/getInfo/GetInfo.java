package fun.lishang.mcplugin.getInfo;

import fun.lishang.mcplugin.getInfo.command.GetInfoCommand;
import fun.lishang.mcplugin.getInfo.config.ConfigManager;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import fun.lishang.mcplugin.getInfo.logging.PluginLogger;
import fun.lishang.mcplugin.getInfo.util.YamlExporter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class GetInfo extends JavaPlugin {
    
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private PluginLogger pluginLogger;
    private YamlExporter exporter;

    @Override
    public void onEnable() {
        try {
            // 检查服务器版本兼容性
            if (!checkServerVersion()) {
                getLogger().severe("不支持的服务器版本！插件需要 Minecraft 1.20.1 - 1.21.X");
                getLogger().severe("当前版本: " + Bukkit.getVersion());
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            
            // 初始化配置管理器
            configManager = new ConfigManager(this);
            configManager.loadConfig();
            
            // 初始化语言管理器
            languageManager = new LanguageManager(this);
            languageManager.loadLanguage(configManager.getLanguage());
            
            // 初始化日志系统
            pluginLogger = new PluginLogger(getLogger(), configManager.getLogLevel());
            
            // 记录启动信息
            pluginLogger.startup("GetInfo 插件正在启动...");
            pluginLogger.startup("版本: " + getDescription().getVersion());
            pluginLogger.startup("支持的 Minecraft 版本: 1.20.1 - 1.21.X");
            pluginLogger.startup("当前服务器版本: " + Bukkit.getVersion());
            
            // 检查 NBT-API（已通过 Shadow 插件内置）
            checkNBTAPI();
            
            // 初始化导出器
            exporter = new YamlExporter(this);
            pluginLogger.debug("导出器已初始化");
            
            // 注册命令
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
        String version = Bukkit.getBukkitVersion();
        
        // 提取版本号 (例如: 1.20.1-R0.1-SNAPSHOT -> 1.20.1)
        String[] parts = version.split("-");
        if (parts.length == 0) {
            return false;
        }
        
        String versionNumber = parts[0];
        String[] versionParts = versionNumber.split("\\.");
        
        if (versionParts.length < 2) {
            return false;
        }
        
        try {
            int major = Integer.parseInt(versionParts[0]);
            int minor = Integer.parseInt(versionParts[1]);
            int patch = versionParts.length > 2 ? Integer.parseInt(versionParts[2]) : 0;
            
            // 检查是否为 1.20.1 - 1.20.6
            if (major == 1 && minor == 20 && patch >= 1 && patch <= 6) {
                return true;
            }
            
            // 检查是否为 1.21.0 及更高版本
            if (major == 1 && minor >= 21) {
                return true;
            }
            
            return false;
        } catch (NumberFormatException e) {
            getLogger().warning("无法解析版本号: " + versionNumber);
            return false;
        }
    }
    
    /**
     * 检查 NBT-API 是否可用
     * NBT-API 已通过 Shadow 插件内置到 JAR 中
     */
    private void checkNBTAPI() {
        try {
            Class.forName("de.tr7zw.changeme.nbtapi.NBTItem");
            pluginLogger.startup("NBT-API 已加载（内置版本）");
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
