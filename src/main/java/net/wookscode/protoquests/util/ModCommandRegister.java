package net.wookscode.protoquests.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.wookscode.protoquests.ProtoQuests;
import net.wookscode.protoquests.command.AssignQuest;
import net.wookscode.protoquests.command.CreateNewQuest;

public class ModCommandRegister {
    public static void registerCommands(){
        ProtoQuests.LOGGER.info("Registering commands...");

        CommandRegistrationCallback.EVENT.register(AssignQuest::register);
        CommandRegistrationCallback.EVENT.register(CreateNewQuest::register);
    }
}
