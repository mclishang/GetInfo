package fun.lishang.mcplugin.getInfo.util;

import org.bukkit.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {
    
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    
    /**
     * 格式化坐标
     * @param location 位置
     * @return 格式化的坐标字符串 "X:x Y:y Z:z"
     */
    public static String formatLocation(Location location) {
        if (location == null) {
            return "N/A";
        }
        return String.format("X:%.2f Y:%.2f Z:%.2f", 
            location.getX(), 
            location.getY(), 
            location.getZ());
    }
    
    /**
     * 格式化 NBT 数据以提高可读性
     * @param nbt NBT 字符串
     * @return 格式化的 NBT 字符串
     */
    public static String formatNBT(String nbt) {
        if (nbt == null || nbt.isEmpty()) {
            return "";
        }
        
        // 如果 NBT 数据过长，进行截断
        if (nbt.length() > 1000) {
            return truncate(nbt, 1000);
        }
        
        return nbt;
    }
    
    /**
     * 格式化时间戳
     * @return 格式化的时间戳字符串
     */
    public static String formatTimestamp() {
        return TIMESTAMP_FORMAT.format(new Date());
    }
    
    /**
     * 截断过长的字符串
     * @param text 原始文本
     * @param maxLength 最大长度
     * @return 截断后的文本
     */
    public static String truncate(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        
        if (text.length() <= maxLength) {
            return text;
        }
        
        return text.substring(0, maxLength) + "...";
    }
}
