package net.wookscode.protoquests.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.wookscode.protoquests.command.helper.NewBreakTask;
import net.wookscode.protoquests.command.helper.NewBreakTaskWithPos;

public class AddTask {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                           CommandRegistryAccess commandRegistryAccess,
                           CommandManager.RegistrationEnvironment registrationEnvironment){

        dispatcher.register(CommandManager.literal("pq").then(CommandManager.literal("quest"))
                        .then(CommandManager.literal("task").then(CommandManager.literal("add")
                                .then(CommandManager.argument("quest_name", StringArgumentType.string())
                                        .then(CommandManager.argument("task_name", StringArgumentType.string())
                                                .then(CommandManager.literal("break")
                                                        .then(CommandManager.argument("block", RegistryKeyArgumentType.registryKey(RegistryKeys.BLOCK))
                                                                .then(CommandManager.argument("number", IntegerArgumentType.integer())
                                                                        .then(CommandManager.argument("position", Vec3ArgumentType.vec3())
                                                                                .executes(AddTask::run_break_with_position))
                                                                        .executes(AddTask::run_break)))))))));
    }

    //break task, without coordinates
    private static int run_break(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        new NewBreakTask(context);

        return 1;
    }

    //break task, with coordinates
    private static int run_break_with_position(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        new NewBreakTaskWithPos(context);

        return 1;
    }


}
