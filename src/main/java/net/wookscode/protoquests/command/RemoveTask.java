package net.wookscode.protoquests.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;
import net.wookscode.protoquests.exception.QuestDoesNotExistException;
import net.wookscode.protoquests.exception.ResultDoesNotExistException;
import net.wookscode.protoquests.task.Task;
import net.wookscode.protoquests.util.StateSaverAndLoader;

import java.util.List;
import java.util.Objects;

public class RemoveTask {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                           CommandRegistryAccess commandRegistryAccess,
                           CommandManager.RegistrationEnvironment registrationEnvironment){

        dispatcher.register(CommandManager.literal("pq").then(CommandManager.literal("task").then(CommandManager.literal("remove")
                .then(CommandManager.argument("quest_name", StringArgumentType.string())
                        .then(CommandManager.argument("task_name", StringArgumentType.string())
                        .executes(RemoveTask::run))))));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();

        StateSaverAndLoader state = StateSaverAndLoader.getServerState(server);

        final String quest_name = StringArgumentType.getString(context, "quest_name");
        if (!state.quests.containsKey(quest_name)){
            throw QuestDoesNotExistException.create(quest_name);
        }

        List<Task> tasks = state.quests.get(quest_name).getTasks();

        String result_name = StringArgumentType.getString(context, "task_name");
        for(Task task : tasks){
            if(!Objects.equals(task.getName(), result_name)){
                throw ResultDoesNotExistException.create(result_name);
            }
            else {
                tasks.remove(tasks.indexOf(task));
                break;
            }
        }

        state.markDirty();

        source.sendMessage(ProtoQuests.PREFIX.copy().append(Text.literal("Task removed successfully!")));

        return 1;
    }


}
