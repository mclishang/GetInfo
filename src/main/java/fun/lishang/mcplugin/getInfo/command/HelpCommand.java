package fun.lishang.mcplugin.getInfo.command;

import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帮助信息子命令
 * 遵循 MCPLUGIN 规范的命令界面样式
 */
public class HelpCommand implements SubCommand {
    
    private final GetInfo plugin;
    private final LanguageManager lang;
    
    public HelpCommand(GetInfo plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
    }
    
    @Override
    public String getName() {
        return "help";
    }
    
    @Override
    public String getPermission() {
        return null;
    }
    
    @Override
    public void execute(Player player, String[] args) {
        // 获取插件版本
        String version = plugin.getDescription().getVersion();
        
        // 创建占位符
        Map<String, String> versionPlaceholder = new HashMap<>();
        versionPlaceholder.put("version", version);
        
        // 发送帮助信息（遵循 MCPLUGIN 规范）
        player.sendMessage("");
        player.sendMessage(translateColor(lang.getMessage("help.header", versionPlaceholder)));
        player.sendMessage("");
        player.sendMessage(translateColor(lang.getMessage("help.command")));
        player.sendMessage(translateColor(lang.getMessage("help.parameters")));
        
        // 发送子命令列表
        player.sendMessage(translateColor(lang.getMessage("help.item")));
        player.sendMessage(translateColor(lang.getMessage("help.item-desc")));
        
        player.sendMessage(translateColor(lang.getMessage("help.player")));
        player.sendMessage(translateColor(lang.getMessage("help.player-desc")));
        
        player.sendMessage(translateColor(lang.getMessage("help.block")));
        player.sendMessage(translateColor(lang.getMessage("help.block-desc")));
        
        player.sendMessage(translateColor(lang.getMessage("help.entity")));
        player.sendMessage(translateColor(lang.getMessage("help.entity-desc")));
        
        player.sendMessage(translateColor(lang.getMessage("help.help")));
        player.sendMessage(translateColor(lang.getMessage("help.help-desc")));
        
        player.sendMessage(translateColor(lang.getMessage("help.reload")));
        player.sendMessage(translateColor(lang.getMessage("help.reload-desc")));
        
        player.sendMessage("");
        player.sendMessage(translateColor(lang.getMessage("help.export-tip")));
        player.sendMessage("");
    }
    
    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }
    
    /**
     * 转换颜色代码
     * @param message 消息
     * @return 转换后的消息
     */
    private String translateColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
