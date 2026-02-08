# GetInfo - Minecraft 服务器信息查询插件

一个为 Minecraft 服务器管理员设计的信息查询插件，支持获取物品、玩家、方块和实体的详细信息，并提供导出和交互式复制功能。

---

## 目录

- [功能特性](#功能特性)
- [安装要求](#安装要求)
- [快速开始](#快速开始)
- [命令列表](#命令列表)
- [权限节点](#权限节点)
- [配置文件](#配置文件)
- [常见问题](#常见问题)
- [作者](#作者)

---

## 功能特性

### 核心功能

| 功能 | 描述 |
|------|------|
| 物品信息查询 | 获取手持物品的名称、ID、NBT 数据等详细信息 |
| 玩家信息查询 | 查看玩家的 UUID、血量、经验、坐标等状态信息 |
| 方块信息查询 | 通过射线追踪获取指向方块的类型、坐标、NBT 数据 |
| 实体信息查询 | 通过射线追踪获取指向实体的类型、血量、NBT 数据 |
| 点击复制 | 所有信息值支持点击复制到剪贴板 |
| 导出功能 | 将查询结果导出为 YAML 文件 |
| 多语言支持 | 支持简体中文、繁体中文、英文 |
| 可配置日志 | 支持 4 种日志级别（NONE、MINIMAL、NORMAL、DEBUG） |

### 兼容性

| 项目 | 支持 |
|------|------|
| 服务端 | Spigot、Paper、Purpur、Folia |

---

## 安装要求

### 必需依赖

无

### 可选依赖

| 依赖 | 说明 | 功能影响 |
|------|------|----------|
| NBT-API | 用于访问 NBT 数据 | 没有此插件时，物品、方块、实体信息将不包含 NBT 数据，但基础信息仍可正常查询 |

**功能降级说明**：

当 NBT-API 插件未安装时：
- **物品信息**：显示名称、ID、材料、数量、耐久度，但不显示 NBT 数据
- **方块信息**：显示 ID、材料、坐标、世界，但不显示 NBT 数据
- **实体信息**：显示 ID、类型、名称、血量、坐标、UUID，但不显示 NBT 数据
- **玩家信息**：不受影响，完全正常工作

插件会在启动时自动检测 NBT-API 是否可用，并在查询信息时显示相应提示。

### 安装步骤

1. 下载插件 JAR 文件
2. 将 JAR 文件放入服务器的 `plugins` 文件夹
3. （可选）如需 NBT 功能，下载并安装 NBT-API 插件
4. 重启服务器或使用插件管理器加载
5. 插件将自动生成配置文件

---

## 快速开始

```bash
# 查看手持物品信息
/getinfo item

# 查看玩家信息
/getinfo player <玩家名>

# 查看指向的方块信息
/getinfo block

# 查看指向的实体信息
/getinfo entity

# 导出信息到文件
/getinfo item --export

# 查看帮助
/getinfo help

# 重载配置
/getinfo reload
```

---

## 命令列表

| 命令 | 描述 | 权限 |
|------|------|------|
| `/getinfo` 或 `/gi` | 显示帮助信息 | - |
| `/getinfo item` | 获取手持物品信息 | `getinfo.item` |
| `/getinfo player <玩家名>` | 获取玩家信息 | `getinfo.player.self` / `getinfo.player.others` |
| `/getinfo block` | 获取指向的方块信息 | `getinfo.block` |
| `/getinfo entity` | 获取指向的实体信息 | `getinfo.entity` |
| `/getinfo help` | 显示帮助信息 | - |
| `/getinfo reload` | 重载配置文件 | `getinfo.reload` |

**导出参数**: 在任何信息命令后添加 `--export` 参数可将结果导出为 YAML 文件

---

## 权限节点

### 玩家权限

| 权限节点 | 描述 | 默认值 |
|----------|------|--------|
| `getinfo.item` | 允许查询物品信息 | op |
| `getinfo.player.self` | 允许查询自己的信息 | op |
| `getinfo.block` | 允许查询方块信息 | op |
| `getinfo.entity` | 允许查询实体信息 | op |

### 管理员权限

| 权限节点 | 描述 | 默认值 |
|----------|------|--------|
| `getinfo.player.others` | 允许查询其他玩家的信息 | op |
| `getinfo.export` | 允许使用导出功能 | op |
| `getinfo.reload` | 允许重载配置 | op |
| `getinfo.admin` | 拥有所有权限 | op |

---

## 配置文件

### 文件结构

```
plugins/GetInfo/
├── config.yml          # 主配置文件
└── exports/            # 导出文件目录（自动创建）
```

### config.yml 主配置

```yaml
# 日志设置
logging:
  # 日志级别: NONE, MINIMAL, NORMAL, DEBUG
  # NONE - 仅严重错误
  # MINIMAL - 启动/关闭 + 错误 + 警告
  # NORMAL - 重要操作 + 错误 + 警告（推荐）
  # DEBUG - 所有详细信息
  level: NORMAL

# 导出设置
export:
  # 导出文件保存路径
  path: "plugins/GetInfo/exports/"

# 功能设置
features:
  # 射线追踪最大距离（方块数）
  ray-trace-distance: 10
  
  # 界面语言: zh-CN, en-US, zh-TW
  language: "zh-CN"
```

### 配置说明

| 配置项 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `logging.level` | String | NORMAL | 日志输出级别 |
| `export.path` | String | plugins/GetInfo/exports/ | 导出文件保存路径 |
| `features.ray-trace-distance` | Integer | 10 | 射线追踪最大距离 |
| `features.language` | String | zh-CN | 界面语言 |

---

## 常见问题

**Q: 为什么执行命令后没有反应？**

A: 请检查以下几点：
1. 确认你拥有对应的权限
2. 确认你是玩家而非控制台（插件不支持控制台执行）
3. 查看服务器日志是否有错误信息

**Q: 为什么无法获取 NBT 数据？**

A: NBT 数据需要安装 NBT-API 插件才能获取。如果未安装：
1. 插件会在启动时显示"未检测到 NBT-API 插件"的警告
2. 查询信息时会显示提示消息"NBT 功能不可用，请安装 NBT-API 插件"
3. 基础信息仍可正常查询，只是不包含 NBT 数据
4. 如需 NBT 功能，请下载并安装 NBT-API 插件后重启服务器

**Q: 如何修改射线追踪距离？**

A: 编辑 `config.yml` 中的 `features.ray-trace-distance` 值，然后执行 `/getinfo reload` 重载配置。

**Q: 导出的文件保存在哪里？**

A: 默认保存在 `plugins/GetInfo/exports/` 目录下，文件名格式为 `{类型}-{时间戳}.yml`。

**Q: 如何切换语言？**

A: 编辑 `config.yml` 中的 `features.language` 值（支持 zh-CN、en-US、zh-TW），然后执行 `/getinfo reload` 重载配置。

**Q: 点击复制功能不工作？**

A: 点击复制功能需要客户端支持。确保客户端允许服务器复制到剪贴板。

---

## 作者

mclishang
