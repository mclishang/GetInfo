package fun.lishang.mcplugin.getInfo.info;

import fun.lishang.mcplugin.getInfo.GetInfo;
import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import fun.lishang.mcplugin.getInfo.util.FormatUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerInfoGetter {
    
    private final GetInfo plugin;
    private final LanguageManager lang;
    
    public PlayerInfoGetter(GetInfo plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
    }
    
    /**
     * 获取玩家信息
     * @param player 要查询的玩家
     * @return 格式化的信息映射
     */
    public Map<String, Object> getPlayerInfo(Player player) {
        if (player == null || !player.isOnline()) {
            return null;
        }
        
        try {
            Map<String, Object> info = new HashMap<>();
            
            // 基本信息
            info.put("name", player.getName());
            info.put("uuid", player.getUniqueId().toString());
            info.put("level", player.getLevel());
            info.put("exp", String.format("%.1f%%", player.getExp() * 100));
            
            // 血量信息
            Map<String, Object> health = new HashMap<>();
            health.put("current", player.getHealth());
            health.put("max", player.getMaxHealth());
            info.put("health", health);
            
            // 饥饿值和护甲值
            info.put("food", player.getFoodLevel());
            
            // 计算护甲值
            double armorValue = 0;
            if (player.getInventory().getHelmet() != null) {
                armorValue += getArmorPoints(player.getInventory().getHelmet().getType().name());
            }
            if (player.getInventory().getChestplate() != null) {
                armorValue += getArmorPoints(player.getInventory().getChestplate().getType().name());
            }
            if (player.getInventory().getLeggings() != null) {
                armorValue += getArmorPoints(player.getInventory().getLeggings().getType().name());
            }
            if (player.getInventory().getBoots() != null) {
                armorValue += getArmorPoints(player.getInventory().getBoots().getType().name());
            }
            info.put("armor", armorValue);
            
            // 游戏模式
            info.put("gamemode", getGameModeName(player.getGameMode()));
            
            // 位置信息
            info.put("world", player.getWorld().getName());
            info.put("location", FormatUtil.formatLocation(player.getLocation()));
            
            return info;
        } catch (Exception e) {
            plugin.getPluginLogger().severe("获取玩家信息失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取游戏模式名称
     * @param gameMode 游戏模式
     * @return 本地化的游戏模式名称
     */
    public String getGameModeName(GameMode gameMode) {
        switch (gameMode) {
            case SURVIVAL:
                return lang.getMessage("gamemode.survival");
            case CREATIVE:
                return lang.getMessage("gamemode.creative");
            case ADVENTURE:
                return lang.getMessage("gamemode.adventure");
            case SPECTATOR:
                return lang.getMessage("gamemode.spectator");
            default:
                return gameMode.name();
        }
    }
    
    /**
     * 获取护甲点数
     * @param armorType 护甲类型
     * @return 护甲点数
     */
    private double getArmorPoints(String armorType) {
        // 皮革护甲
        if (armorType.startsWith("LEATHER_")) {
            if (armorType.endsWith("_HELMET")) return 1;
            if (armorType.endsWith("_CHESTPLATE")) return 3;
            if (armorType.endsWith("_LEGGINGS")) return 2;
            if (armorType.endsWith("_BOOTS")) return 1;
        }
        // 金护甲
        else if (armorType.startsWith("GOLDEN_")) {
            if (armorType.endsWith("_HELMET")) return 2;
            if (armorType.endsWith("_CHESTPLATE")) return 5;
            if (armorType.endsWith("_LEGGINGS")) return 3;
            if (armorType.endsWith("_BOOTS")) return 1;
        }
        // 锁链护甲
        else if (armorType.startsWith("CHAINMAIL_")) {
            if (armorType.endsWith("_HELMET")) return 2;
            if (armorType.endsWith("_CHESTPLATE")) return 5;
            if (armorType.endsWith("_LEGGINGS")) return 4;
            if (armorType.endsWith("_BOOTS")) return 1;
        }
        // 铁护甲
        else if (armorType.startsWith("IRON_")) {
            if (armorType.endsWith("_HELMET")) return 2;
            if (armorType.endsWith("_CHESTPLATE")) return 6;
            if (armorType.endsWith("_LEGGINGS")) return 5;
            if (armorType.endsWith("_BOOTS")) return 2;
        }
        // 钻石护甲
        else if (armorType.startsWith("DIAMOND_")) {
            if (armorType.endsWith("_HELMET")) return 3;
            if (armorType.endsWith("_CHESTPLATE")) return 8;
            if (armorType.endsWith("_LEGGINGS")) return 6;
            if (armorType.endsWith("_BOOTS")) return 3;
        }
        // 下界合金护甲
        else if (armorType.startsWith("NETHERITE_")) {
            if (armorType.endsWith("_HELMET")) return 3;
            if (armorType.endsWith("_CHESTPLATE")) return 8;
            if (armorType.endsWith("_LEGGINGS")) return 6;
            if (armorType.endsWith("_BOOTS")) return 3;
        }
        // 海龟壳
        else if (armorType.equals("TURTLE_HELMET")) {
            return 2;
        }
        
        return 0;
    }
}
