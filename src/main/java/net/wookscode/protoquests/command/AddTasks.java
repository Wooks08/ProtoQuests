package net.wookscode.protoquests.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.wookscode.protoquests.util.StateSaverAndLoader;

import java.lang.ref.Reference;

public class AddTasks {
    private static final SimpleCommandExceptionType BLOCK_NOT_FOUND = new SimpleCommandExceptionType(Text.literal("Block id not found"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                           CommandRegistryAccess commandRegistryAccess,
                           CommandManager.RegistrationEnvironment registrationEnvironment){

        dispatcher.register(CommandManager.literal("pq").then(CommandManager.literal("quest"))
                        .then(CommandManager.literal("add_task")
                                .then(CommandManager.argument("quest_name", StringArgumentType.string())
                                        .then(CommandManager.literal("break")
                                                .then(CommandManager.argument("block", RegistryKeyArgumentType.registryKey(RegistryKeys.BLOCK))
                                                        .then(CommandManager.argument("position", Vec3ArgumentType.vec3())
                                                                .executes(AddTasks::run_break_with_position))
                                                        .executes(AddTasks::run_break))))));
    }

    //break task, without coordinates
    private static int run_break(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();

        StateSaverAndLoader state = StateSaverAndLoader.getServerState(server);

        final String quest_name = StringArgumentType.getString(context, "quest_name");
        final RegistryKey<Block> blockKey = context.getArgument("block", RegistryKey.class);

        if(!Registries.BLOCK.contains(blockKey)){
            throw BLOCK_NOT_FOUND.create();
        }

        final Block block = Registries.BLOCK.get(blockKey);

        context.getSource().sendMessage(Text.literal(quest_name + ": " + block.toString()));

        return 1;
    }

    //break task, with coordinates
    private static int run_break_with_position(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();

        StateSaverAndLoader state = StateSaverAndLoader.getServerState(server);

        final String quest_name = StringArgumentType.getString(context, "quest_name");
        final RegistryKey<Block> blockKey = context.getArgument("block", RegistryKey.class);
        final Vec3d position = Vec3ArgumentType.getVec3(context, "position");

        if(!Registries.BLOCK.contains(blockKey)){
            throw BLOCK_NOT_FOUND.create();
        }

        final Block block = Registries.BLOCK.get(blockKey);

        context.getSource().sendMessage(Text.literal(quest_name + ": " + block.toString() + " at " + position.toString()));


        return 1;
    }


}
