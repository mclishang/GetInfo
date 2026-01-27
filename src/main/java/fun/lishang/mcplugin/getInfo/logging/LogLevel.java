package fun.lishang.mcplugin.getInfo.logging;

/**
 * 日志级别枚举
 * 定义插件的日志输出级别
 */
public enum LogLevel {
    NONE(0),      // 仅严重错误
    MINIMAL(1),   // 启动/关闭 + 错误 + 警告
    NORMAL(2),    // 重要操作 + 错误 + 警告
    DEBUG(3);     // 所有详细信息
    
    private final int priority;
    
    LogLevel(int priority) {
        this.priority = priority;
    }
    
    /**
     * 判断是否应该输出指定级别的日志
     * 
     * @param messageLevel 消息的日志级别
     * @return 如果当前级别允许输出该消息则返回 true
     */
    public boolean shouldLog(LogLevel messageLevel) {
        return this.priority >= messageLevel.priority;
    }
    
    /**
     * 从字符串解析日志级别
     * 
     * @param level 日志级别字符串（不区分大小写）
     * @return 对应的 LogLevel 枚举值
     * @throws IllegalArgumentException 如果字符串无效
     */
    public static LogLevel fromString(String level) {
        if (level == null || level.trim().isEmpty()) {
            throw new IllegalArgumentException("日志级别不能为空");
        }
        
        try {
            return LogLevel.valueOf(level.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的日志级别: " + level + 
                "。有效值为: NONE, MINIMAL, NORMAL, DEBUG");
        }
    }
}
