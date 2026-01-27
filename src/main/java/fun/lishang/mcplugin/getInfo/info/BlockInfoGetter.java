package fun.lishang.mcplugin.getInfo.info;

import de.tr7zw.changeme.nbtapi.NBTTileEntity;
import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import fun.lishang.mcplugin.getInfo.util.FormatUtil;
import fun.lishang.mcplugin.getInfo.util.RayTraceUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.util.HashMap;
import java.util.Map;

public class BlockInfoGetter {
    
    private final GetInfo plugin;
    private final LanguageManager lang;
    private final RayTraceUtil rayTraceUtil;
    
    public BlockInfoGetter(GetInfo plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
        this.rayTraceUtil = new RayTraceUtil(plugin.getConfigManager().getRayTraceDistance());
    }
    
    /**
     * 获取玩家指向的方块信息
     * @param player 执行查询的玩家
     * @return 格式化的信息映射，如果未指向方块则返回 null
     */
    public Map<String, Object> getBlockInfo(Player player) {
        if (player == null) {
            return null;
        }
        
        try {
            // 射线追踪获取方块
            RayTraceResult result = rayTraceUtil.traceBlock(player);
            if (result == null || result.getHitBlock() == null) {
                return null;
            }
            
            Block block = result.getHitBlock();
            
            // 检查是否为空气方块
            if (rayTraceUtil.isAir(block)) {
                return null;
            }
            
            Map<String, Object> info = new HashMap<>();
            
            // 基本信息
            info.put("namespace_id", getNamespacedId(block));
            info.put("material", block.getType().name());
            info.put("location", FormatUtil.formatLocation(block.getLocation()));
            info.put("world", block.getWorld().getName());
            
            // NBT 数据
            String nbt = getBlockNBT(block);
            if (nbt != null && !nbt.isEmpty()) {
                info.put("nbt", nbt);
            }
            
            return info;
        } catch (Exception e) {
            plugin.getPluginLogger().severe("获取方块信息失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取方块的 NBT 数据
     * @param block 要查询的方块
     * @return NBT 字符串，如果没有 NBT 则返回 null
     */
    public String getBlockNBT(Block block) {
        if (block == null) {
            return null;
        }
        
        try {
            // 检查方块是否有 TileEntity
            if (block.getState() instanceof org.bukkit.block.TileState) {
                NBTTileEntity nbtTile = new NBTTileEntity(block.getState());
                String nbtString = nbtTile.toString();
                
                // 如果 NBT 为空或只包含基本信息，返回 null
                if (nbtString == null || nbtString.isEmpty() || nbtString.equals("{}")) {
                    return null;
                }
                
                return nbtString;
            }
            
            return null;
        } catch (Exception e) {
            plugin.getPluginLogger().severe("获取方块 NBT 失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 获取方块的命名空间 ID
     * @param block 要查询的方块
     * @return 命名空间 ID (namespace:id)
     */
    public String getNamespacedId(Block block) {
        if (block == null) {
            return null;
        }
        
        return block.getType().getKey().toString();
    }
}
