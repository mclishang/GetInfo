package fun.lishang.mcplugin.getInfo.command;

import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import fun.lishang.mcplugin.getInfo.util.MessageUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand implements SubCommand {
    
    private final GetInfo plugin;
    private final LanguageManager lang;
    private final MessageUtil messageUtil;
    
    public ReloadCommand(GetInfo plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
        this.messageUtil = new MessageUtil(lang);
    }
    
    @Override
    public String getName() {
        return "reload";
    }
    
    @Override
    public String getPermission() {
        return "getinfo.reload";
    }
    
    @Override
    public void execute(Player player, String[] args) {
        try {
            plugin.getConfigManager().reloadConfig();
            
            String language = plugin.getConfigManager().getLanguage();
            plugin.getLanguageManager().loadLanguage(language);
            
            plugin.getPluginLogger().setLevel(plugin.getConfigManager().getLogLevel());
            
            messageUtil.sendError(player, "reload.success");
            
            plugin.getPluginLogger().info("配置已被 " + player.getName() + " 重载");
            
        } catch (Exception e) {
            messageUtil.sendError(player, "common.unexpected-error");
            
            plugin.getPluginLogger().severe("重载配置失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }
}
