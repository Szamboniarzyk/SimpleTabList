package de.sesosas.simpletablist.classes.commands;

import de.sesosas.simpletablist.classes.handlers.internal.IntervalHandler;
import de.sesosas.simpletablist.classes.handlers.tab.NameHandler;
import de.sesosas.simpletablist.classes.handlers.lp.PermissionsHandler;
import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.handlers.internal.MessageHandler;
import de.sesosas.simpletablist.classes.handlers.tab.TabHandler;
import de.sesosas.simpletablist.classes.scheduler.Scheduler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static de.sesosas.simpletablist.classes.handlers.tab.AnimationHandler.loadAnimationsConfig;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            File file = new File(SimpleTabList.getPlugin().getDataFolder().getAbsolutePath() + "/config.yml");
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            SimpleTabList.getPlugin().config = cfg;

            loadAnimationsConfig();

            NameHandler.Update();
            for(Player p : Bukkit.getOnlinePlayers()){
                TabHandler.UpdateTab(p);
            }

            if (SimpleTabList.getPlugin().config.getBoolean("Tab.Refresh.Interval.OtherThreadPool")) {
                Scheduler scheduler = SimpleTabList.getPlugin().getScheduler();
                if (scheduler == null) scheduler = new Scheduler(1);
                scheduler.registerSchedule("tab", new IntervalHandler(), SimpleTabList.getPlugin().config.getLong("Tab.Refresh.Interval.Time"), TimeUnit.SECONDS);
            } else {
                Bukkit.getScheduler().runTaskTimer(SimpleTabList.getPlugin(), new IntervalHandler(), 0L, SimpleTabList.getPlugin().config.getLong("Tab.Refresh.Interval.Time") * 20);
            }

            String text = "Successfully reloaded the Config!";
            MessageHandler.Send(player, ChatColor.AQUA + text);
        }
        return false;
    }
}

