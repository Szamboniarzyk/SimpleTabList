package de.sesosas.simpletablist.classes.handlers.internal;

import de.sesosas.simpletablist.classes.handlers.tab.AnimationHandler;
import de.sesosas.simpletablist.classes.handlers.tab.NameHandler;
import de.sesosas.simpletablist.classes.handlers.tab.TabHandler;
import org.bukkit.Bukkit;

public class IntervalHandler implements Runnable {

    public void run() {
        NameHandler.Update();
        AnimationHandler.frameIndex++;
        Bukkit.getOnlinePlayers().forEach(TabHandler::UpdateTab);
    }
}

