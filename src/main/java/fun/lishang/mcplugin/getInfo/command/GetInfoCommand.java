package fun.lishang.mcplugin.getInfo.command;

import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import fun.lishang.mcplugin.getInfo.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GetInfo 主命令处理器
 * 负责命令路由、权限检查和 Tab 补全
 */
public class GetInfoCommand implements CommandExecutor, TabCompleter {
    
    private final GetInfo plugin;
    private final LanguageManager lang;
    private final MessageUtil messageUtil;
    private final Map<String, SubCommand> subCommands;
    
    public GetInfoCommand(GetInfo plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
        this.messageUtil = new MessageUtil(lang);
        this.subCommands = new HashMap<>();
        
        // 注册所有子命令
        registerSubCommands();
    }
    
    /**
     * 注册所有子命令
     */
    private void registerSubCommands() {
        registerSubCommand(new ItemCommand(plugin));
        registerSubCommand(new PlayerCommand(plugin));
        registerSubCommand(new BlockCommand(plugin));
        registerSubCommand(new EntityCommand(plugin));
        registerSubCommand(new HelpCommand(plugin));
        registerSubCommand(new ReloadCommand(plugin));
    }
    
    /**
     * 注册单个子命令
     * @param subCommand 子命令实例
     */
    private void registerSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查是否为玩家
        if (!(sender instanceof Player)) {
            messageUtil.sendError((Player) sender, "common.console-only");
            return true;
        }
        
        Player player = (Player) sender;
        
        // 如果没有参数，显示帮助信息
        if (args.length == 0) {
            SubCommand helpCommand = subCommands.get("help");
            if (helpCommand != null) {
                helpCommand.execute(player, new String[0]);
            }
            return true;
        }
        
        // 获取子命令
        String subCommandName = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subCommandName);
        
        // 如果子命令不存在，显示帮助信息
        if (subCommand == null) {
            SubCommand helpCommand = subCommands.get("help");
            if (helpCommand != null) {
                helpCommand.execute(player, new String[0]);
            }
            return true;
        }
        
        // 检查权限
        if (!checkPermission(player, subCommand)) {
            messageUtil.sendError(player, "common.no-permission");
            return true;
        }
        
        // 提取子命令参数
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, args.length - 1);
        
        // 执行子命令
        try {
            subCommand.execute(player, subArgs);
        } catch (Exception e) {
            messageUtil.sendError(player, "common.unexpected-error");
            plugin.getPluginLogger().severe("执行命令时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        // 只为玩家提供补全
        if (!(sender instanceof Player)) {
            return completions;
        }
        
        Player player = (Player) sender;
        
        // 第一个参数：补全子命令名称
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            
            for (SubCommand subCommand : subCommands.values()) {
                // 检查权限
                if (checkPermission(player, subCommand)) {
                    String name = subCommand.getName();
                    if (name.toLowerCase().startsWith(input)) {
                        completions.add(name);
                    }
                }
            }
        }
        // 后续参数：委托给子命令处理
        else if (args.length > 1) {
            String subCommandName = args[0].toLowerCase();
            SubCommand subCommand = subCommands.get(subCommandName);
            
            if (subCommand != null && checkPermission(player, subCommand)) {
                // 提取子命令参数
                String[] subArgs = new String[args.length - 1];
                System.arraycopy(args, 1, subArgs, 0, args.length - 1);
                
                completions = subCommand.tabComplete(player, subArgs);
            }
        }
        
        return completions;
    }
    
    /**
     * 检查玩家是否有权限执行子命令
     * @param player 玩家
     * @param subCommand 子命令
     * @return 是否有权限
     */
    private boolean checkPermission(Player player, SubCommand subCommand) {
        String permission = subCommand.getPermission();
        
        // 如果子命令不需要权限，直接返回 true
        if (permission == null || permission.isEmpty()) {
            return true;
        }
        
        // 检查玩家是否有权限
        return player.hasPermission(permission);
    }
}
