package net.wookscode.protoquests.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.wookscode.protoquests.exception.QuestDoesNotExistException;
import net.wookscode.protoquests.quest.Quest;
import net.wookscode.protoquests.util.PlayerData;
import net.wookscode.protoquests.util.StateSaverAndLoader;

import java.util.Collection;

public class AssignQuestToPlayer {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess ignoredCommandRegistryAccess,
                                CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {

        dispatcher.register(CommandManager.literal("pq")
                        .then(CommandManager.literal("players")
                            .then(CommandManager.literal("quests")
                                    .then(CommandManager.literal("add")
                                        .then(CommandManager.argument("player", EntityArgumentType.players())
                                            .then(CommandManager.argument("quest", StringArgumentType.string())
                                                    .executes(AssignQuestToPlayer::run)
                                            )
                                        )
                                    )
                            )
                        )
        );

    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();

        StateSaverAndLoader state = StateSaverAndLoader.getServerState(server);

        String quest_name = StringArgumentType.getString(context, "quest");
        Quest quest = state.quests.get(quest_name);

        if(!state.quests.containsKey(quest_name)){
            throw QuestDoesNotExistException.create(quest_name);
        }

        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "player");

        for (ServerPlayerEntity player : players) {

            PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
            playerState.assigned_quests.put(quest.getName(), quest);

            source.sendMessage(Text.literal("Assigned quest " + quest.getName() + " to " + player.getName().getString()));
        }
        state.markDirty();
        return 1;
    }
}
