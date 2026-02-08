package fun.lishang.mcplugin.getInfo.nbt;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

/**
 * NBT-API 辅助类
 * 直接使用 NBT-API 的类，适用于 Mohist 等混合端
 */
public class NBTHelper {
    
    /**
     * 构造函数
     */
    public NBTHelper() {
        // 不需要特殊初始化，直接使用 NBT-API 的静态方法
    }
    
    /**
     * 获取物品的 NBT 数据
     * @param item 物品
     * @return NBT 字符串
     */
    public String getItemNBT(ItemStack item) {
        ReadableNBT nbt = NBT.itemStackToNBT(item);
        return nbt.toString();
    }
    
    /**
     * 获取方块的 NBT 数据
     * @param block 方块
     * @return NBT 字符串
     */
    public String getBlockNBT(Block block) {
        return NBT.get(block.getState(), nbt -> {
            return nbt.toString();
        });
    }
    
    /**
     * 获取实体的 NBT 数据
     * @param entity 实体
     * @return NBT 字符串
     */
    public String getEntityNBT(Entity entity) {
        return NBT.get(entity, nbt -> {
            return nbt.toString();
        });
    }
}
