package io.Sriptirc_wp_1417.eftarkov.config;

import io.Sriptirc_wp_1417.eftarkov.Eftarkov;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

public class ConfigManager {

    private final Eftarkov plugin;
    private FileConfiguration config;

    private Set<String> enabledWorlds;
    private double defaultReward;
    private Map<String, Double> mobRewards;
    private boolean showRewardMessage;
    private String rewardMessage;

    private static final int CONFIG_VERSION = 1;

    public ConfigManager(Eftarkov plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();

        // 检查配置版本
        int version = config.getInt("ScriptIrc-config-version", 0);
        if (version < CONFIG_VERSION) {
            plugin.getLogger().log(Level.WARNING,
                    "配置文件版本过旧 (当前: " + version + ", 期望: " + CONFIG_VERSION +
                    ")，请删除 config.yml 后重启服务器以生成新配置。");
        }

        // 读取生效世界
        List<String> worlds = config.getStringList("enabled-worlds");
        this.enabledWorlds = new HashSet<>(worlds);

        // 默认奖励
        this.defaultReward = config.getDouble("default-reward", 10.0);

        // 怪物类型单独奖励
        this.mobRewards = new HashMap<>();
        if (config.contains("mob-rewards")) {
            for (String key : config.getConfigurationSection("mob-rewards").getKeys(false)) {
                double value = config.getDouble("mob-rewards." + key);
                mobRewards.put(key.toUpperCase(), value);
            }
        }

        // 消息设置
        this.showRewardMessage = config.getBoolean("show-reward-message", true);
        this.rewardMessage = config.getString("reward-message", "&a击杀 {mob} 获得 &6{amount} 金币！");
    }

    /**
     * 判断指定世界是否启用了打怪金币
     */
    public boolean isWorldEnabled(String worldName) {
        return enabledWorlds.contains(worldName);
    }

    /**
     * 获取某个怪物类型的奖励金额
     * 如果没单独配置，返回默认值
     */
    public double getRewardForMob(String entityType) {
        return mobRewards.getOrDefault(entityType.toUpperCase(), defaultReward);
    }

    public Set<String> getEnabledWorlds() {
        return enabledWorlds;
    }

    public double getDefaultReward() {
        return defaultReward;
    }

    public Map<String, Double> getMobRewards() {
        return mobRewards;
    }

    public boolean isShowRewardMessage() {
        return showRewardMessage;
    }

    public String getRewardMessage() {
        return rewardMessage;
    }
}
