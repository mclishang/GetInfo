package fun.lishang.mcplugin.getInfo.command;

import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import fun.lishang.mcplugin.getInfo.info.BlockInfoGetter;
import fun.lishang.mcplugin.getInfo.util.MessageUtil;
import fun.lishang.mcplugin.getInfo.util.YamlExporter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockCommand implements SubCommand {
    
    private final GetInfo plugin;
    private final LanguageManager lang;
    private final BlockInfoGetter blockInfoGetter;
    private final MessageUtil messageUtil;
    private final YamlExporter exporter;
    
    public BlockCommand(GetInfo plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
        this.blockInfoGetter = new BlockInfoGetter(plugin);
        this.messageUtil = new MessageUtil(lang);
        this.exporter = plugin.getExporter();
    }
    
    @Override
    public String getName() {
        return "block";
    }
    
    @Override
    public String getPermission() {
        return "getinfo.block";
    }
    
    @Override
    public void execute(Player player, String[] args) {
        try {
            Map<String, Object> blockInfo = blockInfoGetter.getBlockInfo(player);
            
            if (blockInfo == null) {
                messageUtil.sendError(player, "block.no-block");
                return;
            }
            
            messageUtil.sendTitle(player, "block.title");
            messageUtil.sendInfoMessage(player, blockInfo);
            
            if (hasExportFlag(args)) {
                handleExport(player, "block", blockInfo);
            }
        } catch (Exception e) {
            plugin.getPluginLogger().severe("执行方块信息命令时发生错误: " + e.getMessage());
            e.printStackTrace();
            messageUtil.sendError(player, "common.unexpected-error");
        }
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
            completions.add("--export");
        }
        
        return completions;
    }
}
