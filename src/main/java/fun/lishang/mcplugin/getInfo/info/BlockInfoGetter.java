package fun.lishang.mcplugin.getInfo.info;

import de.tr7zw.nbtapi.NBTTileEntity;
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
    
    public Map<String, Object> getBlockInfo(Player player) {
        if (player == null) {
            return null;
        }
        
        try {
            RayTraceResult result = rayTraceUtil.traceBlock(player);
            if (result == null || result.getHitBlock() == null) {
                return null;
            }
            
            Block block = result.getHitBlock();
            
            if (rayTraceUtil.isAir(block)) {
                return null;
            }
            
            Map<String, Object> info = new HashMap<>();
            
            info.put("namespace_id", getNamespacedId(block));
            info.put("material", block.getType().name());
            info.put("location", FormatUtil.formatLocation(block.getLocation()));
            info.put("world", block.getWorld().getName());
            
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
    
    public String getBlockNBT(Block block) {
        if (block == null) {
            return null;
        }
        
        try {
            if (block.getState() instanceof org.bukkit.block.TileState) {
                NBTTileEntity nbtTile = new NBTTileEntity(block.getState());
                String nbtString = nbtTile.toString();
                
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
    
    public String getNamespacedId(Block block) {
        if (block == null) {
            return null;
        }
        
        return block.getType().getKey().toString();
    }
}
