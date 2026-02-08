package fun.lishang.mcplugin.getInfo.info;

import de.tr7zw.nbtapi.NBTItem;
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
    
    public Map<String, Object> getItemInfo(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        
        try {
            Map<String, Object> info = new HashMap<>();
            
            info.put("name", getItemDisplayName(item));
            info.put("namespace_id", getNamespacedId(item));
            info.put("material", item.getType().name());
            info.put("amount", item.getAmount());
            
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable) {
                Damageable damageable = (Damageable) meta;
                Map<String, Object> durability = new HashMap<>();
                durability.put("current", item.getType().getMaxDurability() - damageable.getDamage());
                durability.put("max", item.getType().getMaxDurability());
                info.put("durability", durability);
            }
            
            if (item.getEnchantments() != null && !item.getEnchantments().isEmpty()) {
                info.put("enchantments", item.getEnchantments());
            }
            
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
    
    public String getNamespacedId(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        
        return item.getType().getKey().toString();
    }
    
    private String getItemDisplayName(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        
        return item.getType().name();
    }
}
