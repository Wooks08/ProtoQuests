package net.wookscode.protoquests.command.helper;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;
import net.wookscode.protoquests.exception.QuestDoesNotExistException;
import net.wookscode.protoquests.exception.ResultAlreadyExistException;
import net.wookscode.protoquests.result.Result;
import net.wookscode.protoquests.result.XpResult;
import net.wookscode.protoquests.util.StateSaverAndLoader;

import java.util.Objects;

public class NewXpResult {
    public NewXpResult(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
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

        int amount = IntegerArgumentType.getInteger(context, "amount");

        XpResult result = new XpResult(result_name, amount);
        state.quests.get(quest_name).getResults().add(result);

        state.markDirty();

        source.sendMessage(ProtoQuests.PREFIX.copy()
                .append(Text.literal("Result ").setStyle(Style.EMPTY.withColor(ProtoQuests.WHITE)))
                .append(Text.literal(result_name).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
                .append(Text.literal(" has been added to quest ").setStyle(Style.EMPTY.withColor(ProtoQuests.WHITE)))
                .append(Text.literal(quest_name).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
                .append(Text.literal(".").setStyle(Style.EMPTY.withColor(ProtoQuests.WHITE))));
    }
}
