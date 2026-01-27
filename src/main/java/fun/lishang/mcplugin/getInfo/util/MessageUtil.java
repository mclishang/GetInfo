package fun.lishang.mcplugin.getInfo.util;

import fun.lishang.mcplugin.getInfo.i18n.LanguageManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import java.util.Map;

public class MessageUtil {
    
    private final LanguageManager lang;
    
    public MessageUtil(LanguageManager lang) {
        this.lang = lang;
    }
    
    /**
     * 创建可点击复制的文本组件
     * @param displayText 显示的文本
     * @param copyText 要复制的文本
     * @return TextComponent
     */
    public TextComponent createClickToCopy(String displayText, String copyText) {
        TextComponent component = new TextComponent(displayText);
        
        // 设置点击事件：复制到剪贴板
        component.setClickEvent(new ClickEvent(
            ClickEvent.Action.COPY_TO_CLIPBOARD, 
            copyText
        ));
        
        // 设置悬停提示
        String hoverText = lang.getMessage("common.click-to-copy");
        component.setHoverEvent(new HoverEvent(
            HoverEvent.Action.SHOW_TEXT,
            new Text(hoverText)
        ));
        
        return component;
    }
    
    /**
     * 发送信息消息给玩家
     * @param player 接收消息的玩家
     * @param infoMap 信息映射
     */
    public void sendInfoMessage(Player player, Map<String, Object> infoMap) {
        if (player == null || infoMap == null || infoMap.isEmpty()) {
            return;
        }
        
        for (Map.Entry<String, Object> entry : infoMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (value == null) {
                continue;
            }
            
            String valueStr = value.toString();
            
            // 创建消息组件
            ComponentBuilder builder = new ComponentBuilder();
            
            // 添加键（标签）
            builder.append(key + ": ")
                   .color(ChatColor.GRAY);
            
            // 添加可点击复制的值
            TextComponent valueComponent = createClickToCopy(valueStr, valueStr);
            valueComponent.setColor(ChatColor.WHITE);
            builder.append(valueComponent);
            
            player.spigot().sendMessage(builder.create());
        }
    }
    
    /**
     * 发送标题消息
     * @param player 接收消息的玩家
     * @param titleKey 标题的语言键
     */
    public void sendTitle(Player player, String titleKey) {
        if (player == null || titleKey == null) {
            return;
        }
        
        String title = lang.getMessage(titleKey);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', title));
    }
    
    /**
     * 发送错误消息
     * @param player 接收消息的玩家
     * @param messageKey 消息的语言键
     * @param placeholders 占位符
     */
    public void sendError(Player player, String messageKey, Map<String, String> placeholders) {
        if (player == null || messageKey == null) {
            return;
        }
        
        String message = lang.getMessage(messageKey, placeholders);
        String prefix = lang.getMessage("common.prefix");
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            prefix + " " + message));
    }
    
    /**
     * 发送错误消息（无占位符）
     * @param player 接收消息的玩家
     * @param messageKey 消息的语言键
     */
    public void sendError(Player player, String messageKey) {
        sendError(player, messageKey, null);
    }
}
