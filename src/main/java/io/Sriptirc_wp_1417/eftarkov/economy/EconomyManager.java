package io.Sriptirc_wp_1417.eftarkov.economy;

import io.Sriptirc_wp_1417.eftarkov.Eftarkov;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Level;

public class EconomyManager {

    private final Eftarkov plugin;
    private Economy economy;

    public EconomyManager(Eftarkov plugin) {
        this.plugin = plugin;
        setupEconomy();
    }

    private void setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().log(Level.SEVERE, "未找到 Vault 插件！经济功能将不可用。");
            return;
        }

        RegisteredServiceProvider<Economy> rsp =
                plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            plugin.getLogger().log(Level.SEVERE, "未找到 Vault 经济服务提供者！请确保安装了支持 Vault 的经济插件（如 EssentialsX、CmiEconomy 等）。");
            return;
        }

        economy = rsp.getProvider();
        plugin.getLogger().log(Level.INFO, "已连接到经济系统: " + economy.getName());
    }

    /**
     * 给玩家增加金币
     * @return true 如果成功
     */
    public boolean depositPlayer(OfflinePlayer player, double amount) {
        if (economy == null) {
            plugin.getLogger().log(Level.WARNING, "经济系统未就绪，无法给 " + player.getName() + " 发放 " + amount + " 金币");
            return false;
        }

        EconomyResponse response = economy.depositPlayer(player, amount);
        if (!response.transactionSuccess()) {
            plugin.getLogger().log(Level.WARNING,
                    "给 " + player.getName() + " 发放金币失败: " + response.errorMessage);
            return false;
        }
        return true;
    }

    /**
     * 检查经济系统是否可用
     */
    public boolean isEconomyReady() {
        return economy != null;
    }

    /**
     * 获取当前使用的经济系统名称
     */
    public String getEconomyName() {
        return economy != null ? economy.getName() : "未连接";
    }
}
