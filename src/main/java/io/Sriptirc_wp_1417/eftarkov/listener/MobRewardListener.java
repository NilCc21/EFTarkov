package io.Sriptirc_wp_1417.eftarkov.listener;

import io.Sriptirc_wp_1417.eftarkov.Eftarkov;
import io.Sriptirc_wp_1417.eftarkov.config.ConfigManager;
import io.Sriptirc_wp_1417.eftarkov.economy.EconomyManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.text.DecimalFormat;

public class MobRewardListener implements Listener {

    private final Eftarkov plugin;
    private final ConfigManager configManager;
    private final EconomyManager economyManager;
    private final DecimalFormat df = new DecimalFormat("#.##");

    public MobRewardListener(Eftarkov plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.economyManager = plugin.getEconomyManager();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        // 1. 检查是否是玩家击杀
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        // 2. 检查权限
        if (!killer.hasPermission("eftarkov.use")) return;

        // 3. 检查世界是否启用
        World world = event.getEntity().getWorld();
        if (!configManager.isWorldEnabled(world.getName())) return;

        // 4. 检查经济系统是否就绪
        if (!economyManager.isEconomyReady()) return;

        // 5. 获取怪物类型和奖励金额
        String mobType = event.getEntityType().name();
        double reward = configManager.getRewardForMob(mobType);

        // 奖励必须大于0
        if (reward <= 0) return;

        // 6. 发放金币（Folia 兼容：直接在主线程执行，Vault 操作本身是同步的）
        boolean success = economyManager.depositPlayer(killer, reward);

        // 7. 发送消息
        if (success && configManager.isShowRewardMessage()) {
            String message = configManager.getRewardMessage()
                    .replace("{amount}", df.format(reward))
                    .replace("{mob}", formatMobName(mobType));
            killer.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    /**
     * 把 ENTITY_TYPE 格式转成可读名称
     * 比如 ZOMBIE -> Zombie, SKELETON -> Skeleton
     */
    private String formatMobName(String mobType) {
        String lower = mobType.toLowerCase().replace("_", " ");
        String[] words = lower.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;
            sb.append(Character.toUpperCase(word.charAt(0)))
              .append(word.substring(1))
              .append(" ");
        }
        return sb.toString().trim();
    }
}
