package net.wookscode.protoquests.command.helper;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;
import net.wookscode.protoquests.exception.BlockNotFoundException;
import net.wookscode.protoquests.exception.QuestDoesNotExistException;
import net.wookscode.protoquests.exception.TaskAlreadyExistException;
import net.wookscode.protoquests.task.BreakTask;
import net.wookscode.protoquests.task.Task;
import net.wookscode.protoquests.util.StateSaverAndLoader;

import java.util.Objects;

public class NewBreakTask {
    public NewBreakTask(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();

        StateSaverAndLoader state = StateSaverAndLoader.getServerState(server);

        final String quest_name = StringArgumentType.getString(context, "quest_name");
        final String task_name = StringArgumentType.getString(context, "task_name");
        final RegistryKey<Block> blockKey = context.getArgument("block", RegistryKey.class);
        final int number = IntegerArgumentType.getInteger(context, "number");

        if(!state.quests.containsKey(quest_name)){
            throw QuestDoesNotExistException.create(quest_name);
        }

        for(Task task : state.quests.get(quest_name).getTasks()){
            if(Objects.equals(task.getName(), task_name)) {
                throw TaskAlreadyExistException.create(task_name);
            }
        }

        if(!Registries.BLOCK.contains(blockKey)){
            throw BlockNotFoundException.create(blockKey.getValue().toString());
        }

        final Block block = Registries.BLOCK.get(blockKey);

        BreakTask task = new BreakTask(task_name, block, number, null);

        state.quests.get(quest_name).getTasks().add(task);
        state.markDirty();

        source.sendMessage(ProtoQuests.PREFIX.copy()
                .append(Text.literal("Task ").setStyle(Style.EMPTY.withColor(ProtoQuests.WHITE)))
                .append(Text.literal(task_name).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
                .append(Text.literal(" has been added to quest ").setStyle(Style.EMPTY.withColor(ProtoQuests.WHITE)))
                .append(Text.literal(quest_name).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
                .append(Text.literal(".").setStyle(Style.EMPTY.withColor(ProtoQuests.WHITE))));
    }
}
