package net.wookscode.protoquests.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.Vec3d;
import net.wookscode.protoquests.ProtoQuests;
import net.wookscode.protoquests.task.BreakTask;
import net.wookscode.protoquests.task.Task;
import net.wookscode.protoquests.util.StateSaverAndLoader;

import java.lang.ref.Reference;

public class AddTasks {
    private static final DynamicCommandExceptionType BLOCK_NOT_FOUND = new DynamicCommandExceptionType(block -> ProtoQuests.PREFIX.copy()
            .append(Text.literal("Block ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal(block.toString()).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
            .append(Text.literal(" not found!").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
    );

    private static final DynamicCommandExceptionType QUEST_DOES_NOT_EXIST = new DynamicCommandExceptionType(name -> ProtoQuests.PREFIX.copy()
            .append(Text.literal("Quest ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal(name.toString()).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
            .append(Text.literal(" wasn't created. If you want to create it use ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal("/pq quest create <name>.").setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
    );

    private static final DynamicCommandExceptionType TASK_ALREADY_EXISTS = new DynamicCommandExceptionType(name -> ProtoQuests.PREFIX.copy()
            .append(Text.literal("Task of name ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal(name.toString()).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
            .append(Text.literal(" already exist! Choose other name.").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
    );

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
                                                                                .executes(AddTasks::run_break_with_position))
                                                                        .executes(AddTasks::run_break)))))))));
    }

    //break task, without coordinates
    private static int run_break(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();

        StateSaverAndLoader state = StateSaverAndLoader.getServerState(server);

        final String quest_name = StringArgumentType.getString(context, "quest_name");
        final String task_name = StringArgumentType.getString(context, "task_name");
        final RegistryKey<Block> blockKey = context.getArgument("block", RegistryKey.class);
        final int number = IntegerArgumentType.getInteger(context, "number");

        if(!state.quests.containsKey(quest_name)){
            throw QUEST_DOES_NOT_EXIST.create(quest_name);
        }

        for(Task task : state.quests.get(quest_name).getTasks()){
            if(task.getName().contains(task_name)) {
                throw TASK_ALREADY_EXISTS.create(task_name);
            }
        }

        if(!Registries.BLOCK.contains(blockKey)){
            throw BLOCK_NOT_FOUND.create(blockKey.getValue().toString());
        }

        final Block block = Registries.BLOCK.get(blockKey);

        BreakTask task = new BreakTask(task_name, block, number, null, 0);

        state.quests.get(quest_name).getTasks().add(task);
        state.markDirty();

        context.getSource().sendMessage(Text.literal(quest_name + ": " + block.toString()));

        return 1;
    }

    //break task, with coordinates
    private static int run_break_with_position(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();

        StateSaverAndLoader state = StateSaverAndLoader.getServerState(server);

        final String quest_name = StringArgumentType.getString(context, "quest_name");
        final String task_name = StringArgumentType.getString(context, "task_name");
        final RegistryKey<Block> blockKey = context.getArgument("block", RegistryKey.class);
        final int number = IntegerArgumentType.getInteger(context, "number");
        final Vec3d position = Vec3ArgumentType.getVec3(context, "position");

        if(!state.quests.containsKey(quest_name)){
            throw QUEST_DOES_NOT_EXIST.create(quest_name);
        }

        for(Task task : state.quests.get(quest_name).getTasks()){
            if(task.getName().contains(task_name)) {
                throw TASK_ALREADY_EXISTS.create(task_name);
            }
        }

        if(!Registries.BLOCK.contains(blockKey)){
            throw BLOCK_NOT_FOUND.create(blockKey.getValue().toString());
        }

        final Block block = Registries.BLOCK.get(blockKey);

        BreakTask task = new BreakTask(task_name, block, number, position,0);

        state.quests.get(quest_name).getTasks().add(task);
        state.markDirty();

        context.getSource().sendMessage(Text.literal(quest_name + ": " + block.toString() + " at " + position.toString()));


        return 1;
    }


}
