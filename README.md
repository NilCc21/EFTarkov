# EFTarkov — 打怪金币插件

支持 **Folia** 核心，基于 **Vault** 经济系统。

## 功能

- 玩家击杀怪物后自动获得金币
- 支持按怪物类型单独配置奖励金额
- 支持多世界配置（只有指定世界打怪才给钱）
- 可自定义奖励消息（支持颜色代码 &）
- 完整的权限控制

## 依赖

- **Vault**（必需）
- 任意支持 Vault 的经济插件（EssentialsX、CMI、CmiEconomy 等）

## 配置

插件启动后会在 `plugins/EFTarkov/config.yml` 生成配置文件。

```yaml
# 生效世界列表
enabled-worlds:
  - ziyuan

# 默认奖励（怪物未单独配置时使用）
default-reward: 10.0

# 按怪物类型单独设置奖励
mob-rewards:
  ZOMBIE: 10.0
  SKELETON: 12.0
  CREEPER: 15.0

# 是否显示获得金币的消息
show-reward-message: true

# 消息模板（支持 & 颜色代码）
reward-message: "&a击杀 {mob} 获得 &6{amount} 金币！"
```

## 命令

| 命令 | 别名 | 权限 | 说明 |
|------|------|------|------|
| `/eftarkov reload` | `/et reload` | `eftarkov.reload` | 重载配置文件 |
| `/eftarkov info` | `/et info` | `eftarkov.admin` | 查看当前配置信息 |

## 权限

| 权限节点 | 默认 | 说明 |
|----------|------|------|
| `eftarkov.use` | false | 允许打怪获得金币 |
| `eftarkov.reload` | false | 允许重载配置 |
| `eftarkov.admin` | false | 管理员权限 |

## 安装

1. 将编译后的 `.jar` 放入 `plugins/` 目录
2. 确保已安装 Vault 和对应的经济插件
3. 重启服务器或 `/reload`
4. 编辑 `plugins/EFTarkov/config.yml` 配置生效世界和奖励金额
5. 执行 `/eftarkov reload` 使配置生效
