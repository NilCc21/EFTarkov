package io.Sriptirc_wp_1417.eftarkov.command;

import io.Sriptirc_wp_1417.eftarkov.Eftarkov;
import io.Sriptirc_wp_1417.eftarkov.config.ConfigManager;
import io.Sriptirc_wp_1417.eftarkov.economy.EconomyManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandHandler implements CommandExecutor, TabCompleter {

    private final Eftarkov plugin;
    private final ConfigManager configManager;
    private final EconomyManager economyManager;

    public CommandHandler(Eftarkov plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.economyManager = plugin.getEconomyManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                return handleReload(sender);
            case "info":
                return handleInfo(sender);
            default:
                sendHelp(sender);
                return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "===== EFTarkov 帮助 =====");
        sender.sendMessage(ChatColor.YELLOW + "/eftarkov reload" + ChatColor.WHITE + " - 重载配置文件");
        sender.sendMessage(ChatColor.YELLOW + "/eftarkov info" + ChatColor.WHITE + " - 查看插件当前配置信息");
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("eftarkov.reload") && !sender.hasPermission("eftarkov.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此命令！");
            return true;
        }

        try {
            configManager.loadConfig();
            sender.sendMessage(ChatColor.GREEN + "配置文件已重载！");
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "重载配置时出错: " + e.getMessage());
            plugin.getLogger().warning("重载配置失败: " + e.getMessage());
        }
        return true;
    }

    private boolean handleInfo(CommandSender sender) {
        if (!sender.hasPermission("eftarkov.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有权限查看此信息！");
            return true;
        }

        sender.sendMessage(ChatColor.GOLD + "===== EFTarkov 配置信息 =====");
        sender.sendMessage(ChatColor.YELLOW + "经济系统: " + ChatColor.WHITE + economyManager.getEconomyName());
        sender.sendMessage(ChatColor.YELLOW + "经济就绪: " + ChatColor.WHITE + (economyManager.isEconomyReady() ? "是" : "否"));
        sender.sendMessage(ChatColor.YELLOW + "默认奖励: " + ChatColor.WHITE + configManager.getDefaultReward());
        sender.sendMessage(ChatColor.YELLOW + "生效世界: " + ChatColor.WHITE + String.join(", ", configManager.getEnabledWorlds()));

        Map<String, Double> mobRewards = configManager.getMobRewards();
        if (!mobRewards.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "怪物单独奖励:");
            for (Map.Entry<String, Double> entry : mobRewards.entrySet()) {
                sender.sendMessage(ChatColor.GRAY + "  " + entry.getKey() + ": " + entry.getValue());
            }
        }

        sender.sendMessage(ChatColor.YELLOW + "显示消息: " + ChatColor.WHITE + (configManager.isShowRewardMessage() ? "是" : "否"));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("eftarkov.reload") || sender.hasPermission("eftarkov.admin")) {
                completions.add("reload");
            }
            if (sender.hasPermission("eftarkov.admin")) {
                completions.add("info");
            }
        }

        return completions;
    }
}
