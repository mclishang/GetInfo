package fun.lishang.mcplugin.getInfo.util;

import fun.lishang.mcplugin.getInfo.GetInfo;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class YamlExporter {
    
    private final GetInfo plugin;
    private final File exportDir;
    
    public YamlExporter(GetInfo plugin) {
        this.plugin = plugin;
        
        // 从配置获取导出路径
        String exportPath = plugin.getConfigManager().getExportPath();
        this.exportDir = new File(exportPath);
    }
    
    /**
     * 导出信息到 YAML 文件
     * @param type 信息类型 (item, player, block, entity)
     * @param data 要导出的数据
     * @return 导出的文件路径，失败返回 null
     */
    public String export(String type, Map<String, Object> data) {
        if (type == null || data == null || data.isEmpty()) {
            return null;
        }
        
        try {
            // 确保导出目录存在
            ensureExportDirectory();
            
            // 生成文件名
            String fileName = generateFileName(type);
            File file = new File(exportDir, fileName);
            
            // 创建 YAML 配置
            YamlConfiguration yaml = new YamlConfiguration();
            
            // 写入数据
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                yaml.set(entry.getKey(), entry.getValue());
            }
            
            // 保存文件
            yaml.save(file);
            
            return file.getAbsolutePath();
            
        } catch (IOException e) {
            plugin.getPluginLogger().severe("导出文件失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 生成导出文件名
     * @param type 信息类型
     * @return 文件名
     */
    private String generateFileName(String type) {
        String timestamp = FormatUtil.formatTimestamp();
        return type + "-" + timestamp + ".yml";
    }
    
    /**
     * 确保导出目录存在
     */
    private void ensureExportDirectory() {
        if (!exportDir.exists()) {
            try {
                Files.createDirectories(exportDir.toPath());
                plugin.getPluginLogger().debug("创建导出目录: " + exportDir.getAbsolutePath());
            } catch (IOException e) {
                plugin.getPluginLogger().severe("创建导出目录失败: " + e.getMessage());
            }
        }
    }
}
