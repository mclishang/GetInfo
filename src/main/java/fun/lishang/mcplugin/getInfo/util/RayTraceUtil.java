package fun.lishang.mcplugin.getInfo.util;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class RayTraceUtil {
    
    private final int maxDistance;
    
    public RayTraceUtil(int maxDistance) {
        this.maxDistance = maxDistance;
    }
    
    /**
     * 追踪玩家视线并获取指向的方块
     * @param player 执行追踪的玩家
     * @return RayTraceResult，如果未命中则返回 null
     */
    public RayTraceResult traceBlock(Player player) {
        if (player == null) {
            return null;
        }
        
        return player.rayTraceBlocks(maxDistance, FluidCollisionMode.NEVER);
    }
    
    /**
     * 追踪玩家视线并获取指向的实体
     * @param player 执行追踪的玩家
     * @return RayTraceResult，如果未命中则返回 null
     */
    public RayTraceResult traceEntity(Player player) {
        if (player == null) {
            return null;
        }
        
        return player.getWorld().rayTraceEntities(
            player.getEyeLocation(),
            player.getEyeLocation().getDirection(),
            maxDistance,
            entity -> !entity.equals(player)
        );
    }
    
    /**
     * 检查方块是否为空气
     * @param block 要检查的方块
     * @return 是否为空气方块
     */
    public boolean isAir(Block block) {
        if (block == null) {
            return true;
        }
        
        Material type = block.getType();
        return type == Material.AIR || 
               type == Material.CAVE_AIR || 
               type == Material.VOID_AIR;
    }
}
