package fun.lishang.mcplugin.getInfo.info;

import de.tr7zw.changeme.nbtapi.NBTItem;
import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class ItemInfoGetter {
    
    private final GetInfo plugin;
    private final LanguageManager lang;
    
    public ItemInfoGetter(GetInfo plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
    }
    
    /**
     * 获取物品信息
     * @param item 要查询的物品
     * @return 格式化的信息映射，如果物品为空则返回 null
     */
    public Map<String, Object> getItemInfo(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        
        try {
            Map<String, Object> info = new HashMap<>();
            
            // 基本信息
            info.put("name", getItemDisplayName(item));
            info.put("namespace_id", getNamespacedId(item));
            info.put("material", item.getType().name());
            info.put("amount", item.getAmount());
            
            // 耐久度信息
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable) {
                Damageable damageable = (Damageable) meta;
                Map<String, Object> durability = new HashMap<>();
                durability.put("current", item.getType().getMaxDurability() - damageable.getDamage());
                durability.put("max", item.getType().getMaxDurability());
                info.put("durability", durability);
            }
            
            // 附魔信息
            if (item.getEnchantments() != null && !item.getEnchantments().isEmpty()) {
                info.put("enchantments", item.getEnchantments());
            }
            
            // NBT 数据
            String nbt = getItemNBT(item);
            if (nbt != null && !nbt.isEmpty()) {
                info.put("nbt", nbt);
            }
            
            return info;
        } catch (Exception e) {
            plugin.getPluginLogger().severe("获取物品信息失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取物品的 NBT 数据
     * @param item 要查询的物品
     * @return NBT 字符串，如果获取失败则返回 null
     */
    public String getItemNBT(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        
        try {
            NBTItem nbtItem = new NBTItem(item);
            return nbtItem.toString();
        } catch (Exception e) {
            plugin.getPluginLogger().severe("获取物品 NBT 失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 获取物品的命名空间 ID
     * @param item 要查询的物品
     * @return 命名空间 ID (namespace:id)
     */
    public String getNamespacedId(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        
        return item.getType().getKey().toString();
    }
    
    /**
     * 获取物品的显示名称
     * @param item 要查询的物品
     * @return 显示名称
     */
    private String getItemDisplayName(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        
        // 使用材料类型名称
        return item.getType().name();
    }
}
