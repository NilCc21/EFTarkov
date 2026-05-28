package io.Sriptirc_wp_1417.eftarkov;

import io.Sriptirc_wp_1417.eftarkov.command.CommandHandler;
import io.Sriptirc_wp_1417.eftarkov.config.ConfigManager;
import io.Sriptirc_wp_1417.eftarkov.economy.EconomyManager;
import io.Sriptirc_wp_1417.eftarkov.listener.MobRewardListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Eftarkov extends JavaPlugin {

    private ConfigManager configManager;
    private EconomyManager economyManager;

    @Override
    public void onEnable() {
        // 初始化配置管理器
        this.configManager = new ConfigManager(this);

        // 初始化经济管理器
        this.economyManager = new EconomyManager(this);

        // 注册事件监听
        getServer().getPluginManager().registerEvents(new MobRewardListener(this), this);

        // 注册命令
        CommandHandler commandHandler = new CommandHandler(this);
        var command = getCommand("eftarkov");
        if (command != null) {
            command.setExecutor(commandHandler);
            command.setTabCompleter(commandHandler);
        }

        getLogger().log(Level.INFO, "EFTarkov 已启用！");
        getLogger().log(Level.INFO, "经济系统: " + economyManager.getEconomyName());
        getLogger().log(Level.INFO, "生效世界: " + String.join(", ", configManager.getEnabledWorlds()));
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "EFTarkov 已禁用！");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }
}
