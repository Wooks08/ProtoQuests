package net.wookscode.protoquests.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

public class EventsManager {
    public static void registerEvents(){
        PlayerBlockBreakEvents.AFTER.register(new PlayerBlockBreakEventsAfter());
    }
}
