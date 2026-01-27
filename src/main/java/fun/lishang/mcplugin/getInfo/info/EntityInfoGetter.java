package fun.lishang.mcplugin.getInfo.info;

import de.tr7zw.changeme.nbtapi.NBTEntity;
import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import fun.lishang.mcplugin.getInfo.util.FormatUtil;
import fun.lishang.mcplugin.getInfo.util.RayTraceUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.util.HashMap;
import java.util.Map;

public class EntityInfoGetter {
    
    private final GetInfo plugin;
    private final LanguageManager lang;
    private final RayTraceUtil rayTraceUtil;
    
    public EntityInfoGetter(GetInfo plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
        this.rayTraceUtil = new RayTraceUtil(plugin.getConfigManager().getRayTraceDistance());
    }
    
    /**
     * 获取玩家指向的实体信息
     * @param player 执行查询的玩家
     * @return 格式化的信息映射，如果未指向实体则返回 null
     */
    public Map<String, Object> getEntityInfo(Player player) {
        if (player == null) {
            return null;
        }
        
        try {
            // 射线追踪获取实体
            RayTraceResult result = rayTraceUtil.traceEntity(player);
            if (result == null || result.getHitEntity() == null) {
                return null;
            }
            
            Entity entity = result.getHitEntity();
            
            Map<String, Object> info = new HashMap<>();
            
            // 基本信息
            info.put("namespace_id", getNamespacedId(entity));
            info.put("type", entity.getType().name());
            
            // 名称信息
            String name = entity.getName();
            info.put("name", name);
            
            // 自定义名称
            if (entity.getCustomName() != null) {
                info.put("custom_name", entity.getCustomName());
            }
            
            // 血量信息（如果是生物实体）
            if (entity instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) entity;
                Map<String, Object> health = new HashMap<>();
                health.put("current", living.getHealth());
                health.put("max", living.getMaxHealth());
                info.put("health", health);
            }
            
            // 位置信息
            info.put("location", FormatUtil.formatLocation(entity.getLocation()));
            info.put("world", entity.getWorld().getName());
            info.put("uuid", entity.getUniqueId().toString());
            
            // NBT 数据
            String nbt = getEntityNBT(entity);
            if (nbt != null && !nbt.isEmpty()) {
                info.put("nbt", nbt);
            }
            
            return info;
        } catch (Exception e) {
            plugin.getPluginLogger().severe("获取实体信息失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取实体的 NBT 数据
     * @param entity 要查询的实体
     * @return NBT 字符串
     */
    public String getEntityNBT(Entity entity) {
        if (entity == null) {
            return null;
        }
        
        try {
            NBTEntity nbtEntity = new NBTEntity(entity);
            return nbtEntity.toString();
        } catch (Exception e) {
            plugin.getPluginLogger().severe("获取实体 NBT 失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 获取实体的命名空间 ID
     * @param entity 要查询的实体
     * @return 命名空间 ID (namespace:id)
     */
    public String getNamespacedId(Entity entity) {
        if (entity == null) {
            return null;
        }
        
        return entity.getType().getKey().toString();
    }
}
