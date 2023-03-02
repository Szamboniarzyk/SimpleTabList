package de.sesosas.simpletablist.classes.handlers.internal;

import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.handlers.tab.NameHandler;
import de.sesosas.simpletablist.classes.handlers.tab.TabHandler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class IntervalHandler {
    public static void ToggleInterval(boolean use){
        if(use){
            Thread thread = new Thread(){
                public void run(){
                    SimpleTabList.interval = new BukkitRunnable() {

                        @Override
                        public void run() {
                            NameHandler.Update();
                        }
                    }.runTaskTimer(SimpleTabList.getPlugin(), 0, SimpleTabList.getPlugin().config.getLong("Tab.Refresh.Interval.Time") * 10);
                }
            };
            thread.start();
        }
        else{
            if(SimpleTabList.interval != null){
                SimpleTabList.interval.cancel();
            }
        }
    }
}
