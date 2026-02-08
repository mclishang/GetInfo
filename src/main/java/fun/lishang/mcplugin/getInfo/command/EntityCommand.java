package fun.lishang.mcplugin.getInfo.command;

import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import fun.lishang.mcplugin.getInfo.info.EntityInfoGetter;
import fun.lishang.mcplugin.getInfo.util.MessageUtil;
import fun.lishang.mcplugin.getInfo.util.YamlExporter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityCommand implements SubCommand {
    
    private final GetInfo plugin;
    private final LanguageManager lang;
    private final EntityInfoGetter entityInfoGetter;
    private final MessageUtil messageUtil;
    private final YamlExporter exporter;
    
    public EntityCommand(GetInfo plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
        this.entityInfoGetter = new EntityInfoGetter(plugin);
        this.messageUtil = new MessageUtil(lang);
        this.exporter = plugin.getExporter();
    }
    
    @Override
    public String getName() {
        return "entity";
    }
    
    @Override
    public String getPermission() {
        return "getinfo.entity";
    }
    
    @Override
    public void execute(Player player, String[] args) {
        try {
            Map<String, Object> entityInfo = entityInfoGetter.getEntityInfo(player);
            
            if (entityInfo == null) {
                messageUtil.sendError(player, "entity.no-entity");
                return;
            }
            
            messageUtil.sendTitle(player, "entity.title");
            messageUtil.sendInfoMessage(player, entityInfo);
            
            if (hasExportFlag(args)) {
                handleExport(player, "entity", entityInfo);
            }
        } catch (Exception e) {
            plugin.getPluginLogger().severe("执行实体信息命令时发生错误: " + e.getMessage());
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
