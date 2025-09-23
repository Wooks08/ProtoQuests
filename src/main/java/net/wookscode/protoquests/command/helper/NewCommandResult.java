package net.wookscode.protoquests.command.helper;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;
import net.wookscode.protoquests.exception.QuestDoesNotExistException;
import net.wookscode.protoquests.exception.ResultAlreadyExistException;
import net.wookscode.protoquests.result.CommandResult;
import net.wookscode.protoquests.result.Result;
import net.wookscode.protoquests.util.StateSaverAndLoader;

import java.util.Objects;

public class NewCommandResult {
    public NewCommandResult(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();

        StateSaverAndLoader state = StateSaverAndLoader.getServerState(server);

        final String quest_name = StringArgumentType.getString(context, "quest_name");
        if (!state.quests.containsKey(quest_name)){
            throw QuestDoesNotExistException.create(quest_name);
        }

        String result_name = StringArgumentType.getString(context, "result_name");
        for (Result result: state.quests.get(quest_name).getResults()) {
            if(Objects.equals(result.getName(), result_name)){
                throw ResultAlreadyExistException.create(result_name);
            }
        }


        ServerPlayerEntity player = source.getPlayer();
        player.sendMessage(ProtoQuests.PREFIX.copy().append(Text.literal("Type command you want to use. Do not use '/' in the message.")));

        ChatInputManager.awaitResponse(player, response -> {
            CommandResult result = new CommandResult(result_name, response);
            state.quests.get(quest_name).getResults().add(result);

            player.sendMessage(Text.literal("Result ")
                    .append(Text.literal(result_name).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
                    .append(Text.literal(" was added to quest ").setStyle(Style.EMPTY.withColor(ProtoQuests.WHITE)))
                    .append(Text.literal(quest_name).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
                    .append(Text.literal(".").setStyle(Style.EMPTY.withColor(ProtoQuests.WHITE))));
        });

    }
}
