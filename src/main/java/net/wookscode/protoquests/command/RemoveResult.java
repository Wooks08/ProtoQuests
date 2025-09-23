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
import net.wookscode.protoquests.quest.Quest;
import net.wookscode.protoquests.result.Result;
import net.wookscode.protoquests.util.StateSaverAndLoader;

import java.util.List;
import java.util.Objects;

public class RemoveResult {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                           CommandRegistryAccess commandRegistryAccess,
                           CommandManager.RegistrationEnvironment registrationEnvironment){

        dispatcher.register(CommandManager.literal("pq").then(CommandManager.literal("result")
                .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("quest_name", StringArgumentType.string())
                                .then(CommandManager.argument("result_name", StringArgumentType.string())
                                        .executes(RemoveResult::run))))));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();

        StateSaverAndLoader state = StateSaverAndLoader.getServerState(server);

        final String quest_name = StringArgumentType.getString(context, "quest_name");
        if (!state.quests.containsKey(quest_name)){
            throw QuestDoesNotExistException.create(quest_name);
        }

        List<Result> results = state.quests.get(quest_name).getResults();

        String result_name = StringArgumentType.getString(context, "result_name");
        for(Result result: results){
            if(!Objects.equals(result.getName(), result_name)){
                throw ResultDoesNotExistException.create(result_name);
            }
            else {
                results.remove(results.indexOf(result));
                break;
            }
        }

        state.markDirty();

        source.sendMessage(ProtoQuests.PREFIX.copy().append(Text.literal("Result removed successfully!")));

        return 1;
    }


}
