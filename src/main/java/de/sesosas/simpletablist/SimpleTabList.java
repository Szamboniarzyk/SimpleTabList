package de.sesosas.simpletablist;

import de.sesosas.simpletablist.classes.handlers.commands.CommandHandler;
import de.sesosas.simpletablist.classes.handlers.events.IEventHandler;
import de.sesosas.simpletablist.classes.handlers.spigot.UpdateHandler;
import de.sesosas.simpletablist.classes.handlers.tab.NameHandler;
import de.sesosas.simpletablist.classes.handlers.tab.TabHandler;
import de.sesosas.simpletablist.classes.handlers.worldbased.TabWBHandler;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.LuckPermsEvent;
import net.luckperms.api.event.node.NodeAddEvent;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class SimpleTabList extends JavaPlugin implements Listener {

    public FileConfiguration config = getConfig();

    private static SimpleTabList plugin;

    public static SimpleTabList getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        java.lang.String[] headerString = new java.lang.String[]{"This is a Header!", "Welcome %player_name%!"};
        java.lang.String[] footerString = new java.lang.String[] {"This is a Footer!", "This is Footer line 2!"};

        //java.lang.String[] bannedWords = new java.lang.String[] {"bastard", "ass"};
        //java.lang.String[] whitelistedLinks = new java.lang.String[] { "http://discord.gg/invite", "https://your-website" };

        config.addDefault("Names.Use", true);
        config.addDefault("Worlds.Names.Use", true);
        config.addDefault("Worlds.HeaderFooter.Use", true);
        config.addDefault("Header.Use", true);
        config.addDefault("Header.Content", headerString);
        config.addDefault("Footer.Use", true);
        config.addDefault("Footer.Content", footerString);
        config.addDefault("Chat.Prefix", "§f[§cSTL§f]");
        /*
        config.addDefault("Event.Use", true);
        config.addDefault("Event.JoinMessage", "The Player {player_name} joined the Server!");
        config.addDefault("Event.QuitMessage", "The Player {player_name} left the Server!");
        config.addDefault("Chat.Use", true);
        config.addDefault("Chat.Prefix", "§f[§cSTL§f]");
        config.addDefault("Chat.Separator", " >> ");
        config.addDefault("Chat.Moderation.WordBlacklist", bannedWords);
        config.addDefault("Chat.Moderation.LinkWhitelist", whitelistedLinks);
        config.addDefault("Chat.Colors", true);
         */
        config.addDefault("Plugin.ActionbarMessage", false);
        config.addDefault("Plugin.NoticeMe", "You need LuckPerms to get this Plugin to work!");
        config.addDefault("bstats.Use", true);
        config.options().copyDefaults(true);
        saveConfig();
        TabWBHandler.GenerateWorldConfig();

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms luckPerms = provider.getProvider();
            EventBus eventBus = luckPerms.getEventBus();

            eventBus.subscribe(this.plugin, NodeAddEvent.class, this::onNodeAddEvent);
        }

        if(config.getBoolean("bstats.Use")){
            int id = 15221;
            Metrics metrics = new Metrics(this, id);
            metrics.addCustomChart(new SingleLineChart("banned", () -> Bukkit.getBannedPlayers().size()));
        }

        new UpdateHandler(this, 101989).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is no new update available.");
            } else {
                getLogger().info("There is a new update available.");
            }
        });

        Thread thread = new Thread(){
            public void run(){
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        TabHandler.UpdateTab();
                        NameHandler.Update();
                    }
                }.runTaskTimer(SimpleTabList.getPlugin(), 0, 20L);
            }
        };
        thread.start();



        getServer().getPluginManager().registerEvents(new IEventHandler(), this);
        getCommand("stl").setExecutor(new CommandHandler());
        System.out.println("Simple TabList has started!");
    }

    private <T extends LuckPermsEvent> void onNodeAddEvent(T t) {
        NameHandler.Update();
    }
}