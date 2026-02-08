package fun.lishang.mcplugin.getInfo.command;

import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String version = plugin.getDescription().getVersion();
        
        Map<String, String> versionPlaceholder = new HashMap<>();
        versionPlaceholder.put("version", version);
        
        player.sendMessage("");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.header", versionPlaceholder)));
        player.sendMessage("");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.command")));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.parameters")));
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.item")));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.item-desc")));
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.player")));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.player-desc")));
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.block")));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.block-desc")));
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.entity")));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.entity-desc")));
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.help")));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.help-desc")));
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.reload")));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.reload-desc")));
        
        player.sendMessage("");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getMessage("help.export-tip")));
        player.sendMessage("");
    }
    
    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }
}
