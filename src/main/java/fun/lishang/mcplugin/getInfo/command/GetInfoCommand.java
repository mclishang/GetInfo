package fun.lishang.mcplugin.getInfo.command;

import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import fun.lishang.mcplugin.getInfo.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        
        registerSubCommands();
    }
    
    private void registerSubCommands() {
        registerSubCommand(new ItemCommand(plugin));
        registerSubCommand(new PlayerCommand(plugin));
        registerSubCommand(new BlockCommand(plugin));
        registerSubCommand(new EntityCommand(plugin));
        registerSubCommand(new HelpCommand(plugin));
        registerSubCommand(new ReloadCommand(plugin));
    }
    
    private void registerSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            messageUtil.sendError((Player) sender, "common.console-only");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            executeSubCommand(player, "help", new String[0]);
            return true;
        }
        
        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand == null) {
            executeSubCommand(player, "help", new String[0]);
            return true;
        }
        
        if (!checkPermission(player, subCommand)) {
            messageUtil.sendError(player, "common.no-permission");
            return true;
        }
        
        String[] subArgs = args.length > 1 ? 
            java.util.Arrays.copyOfRange(args, 1, args.length) : new String[0];
        
        try {
            subCommand.execute(player, subArgs);
        } catch (Exception e) {
            messageUtil.sendError(player, "common.unexpected-error");
            plugin.getPluginLogger().severe("执行命令时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
        
        return true;
    }
    
    private void executeSubCommand(Player player, String commandName, String[] args) {
        SubCommand subCommand = subCommands.get(commandName);
        if (subCommand != null) {
            subCommand.execute(player, args);
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }
        
        Player player = (Player) sender;
        
        if (args.length == 1) {
            return getSubCommandCompletions(player, args[0]);
        }
        
        if (args.length > 1) {
            return getDelegatedCompletions(player, args);
        }
        
        return Collections.emptyList();
    }
    
    private List<String> getSubCommandCompletions(Player player, String input) {
        String lowerInput = input.toLowerCase();
        return subCommands.values().stream()
            .filter(cmd -> checkPermission(player, cmd))
            .map(SubCommand::getName)
            .filter(name -> name.toLowerCase().startsWith(lowerInput))
            .collect(java.util.stream.Collectors.toList());
    }
    
    private List<String> getDelegatedCompletions(Player player, String[] args) {
        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        
        if (subCommand == null || !checkPermission(player, subCommand)) {
            return Collections.emptyList();
        }
        
        String[] subArgs = args.length > 1 ? 
            java.util.Arrays.copyOfRange(args, 1, args.length) : new String[0];
        
        return subCommand.tabComplete(player, subArgs);
    }
    
    private boolean checkPermission(Player player, SubCommand subCommand) {
        String permission = subCommand.getPermission();
        return permission == null || permission.isEmpty() || player.hasPermission(permission);
    }
}
