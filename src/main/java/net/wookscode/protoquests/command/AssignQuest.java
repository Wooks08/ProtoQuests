package net.wookscode.protoquests.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class AssignQuest {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {

        dispatcher.register(CommandManager.literal("pq").then(CommandManager.literal("player")
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("quest", StringArgumentType.string())
                                        .executes(context -> {
                                                    final String quest = StringArgumentType.getString(context, "quest");

                                                    return 1;
                                                }
                                        )))));

    }
}
