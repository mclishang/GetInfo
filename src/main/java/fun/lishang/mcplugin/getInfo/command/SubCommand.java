package fun.lishang.mcplugin.getInfo.command;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * 子命令接口
 * 定义所有子命令必须实现的方法
 */
public interface SubCommand {
    
    /**
     * 获取子命令名称
     * @return 命令名称
     */
    String getName();
    
    /**
     * 获取子命令所需权限
     * @return 权限节点
     */
    String getPermission();
    
    /**
     * 执行子命令
     * @param player 执行命令的玩家
     * @param args 命令参数
     */
    void execute(Player player, String[] args);
    
    /**
     * Tab 补全
     * @param player 执行命令的玩家
     * @param args 当前参数
     * @return 补全建议列表
     */
    List<String> tabComplete(Player player, String[] args);
}
