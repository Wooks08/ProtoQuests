package net.wookscode.protoquests.command;

import com.mojang.brigadier.CommandDispatcher;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;
import net.wookscode.protoquests.exception.OnlySinglePlayerSpecifierAllowedException;
import net.wookscode.protoquests.util.PlayerData;
import net.wookscode.protoquests.util.StateSaverAndLoader;

import java.util.Collection;


public class GetQuestsFromPlayer {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess ignoredCommandRegistryAccess,
                                CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {

        dispatcher.register(CommandManager.literal("pq")
                        .then(CommandManager.literal("players")
                            .then(CommandManager.literal("quests")
                                    .then(CommandManager.literal("list")
                                        .then(CommandManager.argument("player", EntityArgumentType.players())
                                                .executes(GetQuestsFromPlayer::run)
                                        )
                                    )
                            )
                        )
        );

    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();

        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "player");

        if (players.size() != 1) {
            throw OnlySinglePlayerSpecifierAllowedException.create();
        }

        ServerPlayerEntity player = players.iterator().next();

        source.sendMessage(ProtoQuests.PREFIX.copy().append(Text.literal("Quests assigned to player " + player.getName().getString())));

        PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
        playerState.assigned_quests.forEach((name, quest) -> {
            source.sendMessage(Text.literal("> ").setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)).append(Text.literal(name).setStyle(Style.EMPTY.withColor(ProtoQuests.WHITE))));
        });
        return 1;
    }
}
