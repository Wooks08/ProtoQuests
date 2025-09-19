package net.wookscode.protoquests.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.wookscode.protoquests.command.helper.NewCommandResult;
import net.wookscode.protoquests.command.helper.NewGiveResult;
import net.wookscode.protoquests.command.helper.NewXpResult;
import net.wookscode.protoquests.exception.QuestDoesNotExistException;
import net.wookscode.protoquests.util.StateSaverAndLoader;

public class AddResult {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                           CommandRegistryAccess commandRegistryAccess,
                           CommandManager.RegistrationEnvironment registrationEnvironment){

        dispatcher.register(CommandManager.literal("pq").then(CommandManager.literal("result").then(CommandManager.literal("add")
                .then(CommandManager.argument("quest_name", StringArgumentType.string())
                        .then(CommandManager.argument("result_name", StringArgumentType.string())
                                .then(CommandManager.literal("give")
                                        .then(CommandManager.argument("item", ItemStackArgumentType.itemStack(commandRegistryAccess))
                                                .then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                                                        .executes(AddResult::run_give))))
                                .then(CommandManager.literal("xp")
                                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(AddResult::run_xp)))
                                .then(CommandManager.literal("command")
                                        .executes(AddResult::run_command))
                        )))));
    }

    private static int run_give(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        new NewGiveResult(context);

        return 1;
    }

    private static int run_xp(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        new NewXpResult(context);

        return 1;
    }

    private static int run_command(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        new NewCommandResult(context);

        return 1;
    }
}
