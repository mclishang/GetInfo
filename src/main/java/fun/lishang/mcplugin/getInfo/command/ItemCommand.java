package fun.lishang.mcplugin.getInfo.command;

import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import fun.lishang.mcplugin.getInfo.info.ItemInfoGetter;
import fun.lishang.mcplugin.getInfo.util.MessageUtil;
import fun.lishang.mcplugin.getInfo.util.YamlExporter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物品信息查询子命令
 */
public class ItemCommand implements SubCommand {
    
    private final GetInfo plugin;
    private final LanguageManager lang;
    private final ItemInfoGetter itemInfoGetter;
    private final MessageUtil messageUtil;
    private final YamlExporter exporter;
    
    public ItemCommand(GetInfo plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
        this.itemInfoGetter = new ItemInfoGetter(plugin);
        this.messageUtil = new MessageUtil(lang);
        this.exporter = plugin.getExporter();
    }
    
    @Override
    public String getName() {
        return "item";
    }
    
    @Override
    public String getPermission() {
        return "getinfo.item";
    }
    
    @Override
    public void execute(Player player, String[] args) {
        try {
            // 获取手持物品
            ItemStack item = player.getInventory().getItemInMainHand();
            
            // 检查是否为空
            if (item == null || item.getType() == Material.AIR) {
                messageUtil.sendError(player, "item.no-item");
                return;
            }
            
            // 获取物品信息
            Map<String, Object> itemInfo = itemInfoGetter.getItemInfo(item);
            
            if (itemInfo == null) {
                messageUtil.sendError(player, "item.no-item");
                return;
            }
            
            // 发送标题
            messageUtil.sendTitle(player, "item.title");
            
            // 发送信息
            messageUtil.sendInfoMessage(player, itemInfo);
            
            // 检查是否需要导出
            boolean shouldExport = false;
            for (String arg : args) {
                if ("--export".equalsIgnoreCase(arg)) {
                    shouldExport = true;
                    break;
                }
            }
            
            if (shouldExport) {
                // 检查导出权限
                if (!player.hasPermission("getinfo.export")) {
                    messageUtil.sendError(player, "common.no-permission");
                    return;
                }
                
                String exportPath = exporter.export("item", itemInfo);
                
                if (exportPath != null) {
                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("path", exportPath);
                    messageUtil.sendError(player, "item.export-success", placeholders);
                } else {
                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("error", lang.getMessage("export.unknown-error"));
                    messageUtil.sendError(player, "export.error", placeholders);
                }
            }
        } catch (Exception e) {
            plugin.getPluginLogger().severe("执行物品信息命令时发生错误: " + e.getMessage());
            e.printStackTrace();
            messageUtil.sendError(player, "common.unexpected-error");
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
