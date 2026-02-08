package fun.lishang.mcplugin.getInfo.command;

import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import fun.lishang.mcplugin.getInfo.info.PlayerInfoGetter;
import fun.lishang.mcplugin.getInfo.util.MessageUtil;
import fun.lishang.mcplugin.getInfo.util.YamlExporter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerCommand implements SubCommand {
    
    private final GetInfo plugin;
    private final LanguageManager lang;
    private final PlayerInfoGetter playerInfoGetter;
    private final MessageUtil messageUtil;
    private final YamlExporter exporter;
    
    public PlayerCommand(GetInfo plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
        this.playerInfoGetter = new PlayerInfoGetter(plugin);
        this.messageUtil = new MessageUtil(lang);
        this.exporter = plugin.getExporter();
    }
    
    @Override
    public String getName() {
        return "player";
    }
    
    @Override
    public String getPermission() {
        return "getinfo.player.self";
    }
    
    @Override
    public void execute(Player player, String[] args) {
        try {
            Player targetPlayer = getTargetPlayer(player, args);
            
            if (targetPlayer == null) {
                return;
            }
            
            Map<String, Object> playerInfo = playerInfoGetter.getPlayerInfo(targetPlayer);
            
            if (playerInfo == null) {
                messageUtil.sendError(player, "common.unexpected-error");
                return;
            }
            
            messageUtil.sendTitle(player, "player.title");
            messageUtil.sendInfoMessage(player, playerInfo);
            
            if (hasExportFlag(args)) {
                handleExport(player, "player", playerInfo);
            }
        } catch (Exception e) {
            plugin.getPluginLogger().severe("执行玩家信息命令时发生错误: " + e.getMessage());
            e.printStackTrace();
            messageUtil.sendError(player, "common.unexpected-error");
        }
    }
    
    private Player getTargetPlayer(Player player, String[] args) {
        if (args.length < 1) {
            return player;
        }
        
        String playerName = args[0];
        
        if (!player.hasPermission("getinfo.player.others")) {
            messageUtil.sendError(player, "common.no-permission");
            return null;
        }
        
        Player targetPlayer = Bukkit.getPlayer(playerName);
        
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("player", playerName);
            messageUtil.sendError(player, "player.invalid-player", placeholders);
            return null;
        }
        
        return targetPlayer;
    }
    
    private boolean hasExportFlag(String[] args) {
        for (String arg : args) {
            if ("--export".equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }
    
    private void handleExport(Player player, String type, Map<String, Object> data) {
        if (!player.hasPermission("getinfo.export")) {
            messageUtil.sendError(player, "common.no-permission");
            return;
        }
        
        String exportPath = exporter.export(type, data);
        
        if (exportPath != null) {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("path", exportPath);
            messageUtil.sendError(player, type + ".export-success", placeholders);
        } else {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("error", lang.getMessage("export.unknown-error"));
            messageUtil.sendError(player, "export.error", placeholders);
        }
    }
    
    @Override
    public List<String> tabComplete(Player player, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // 补全在线玩家名称
            String input = args[0].toLowerCase();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getName().toLowerCase().startsWith(input)) {
                    completions.add(onlinePlayer.getName());
                }
            }
            
            // 添加 --export 选项
            if ("--export".startsWith(input)) {
                completions.add("--export");
            }
        } else if (args.length == 2) {
            completions.add("--export");
        }
        
        return completions;
    }
}
