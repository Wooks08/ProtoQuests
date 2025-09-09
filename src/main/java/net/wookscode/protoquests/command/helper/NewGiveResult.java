package net.wookscode.protoquests.command.helper;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.wookscode.protoquests.ProtoQuests;
import net.wookscode.protoquests.exception.ItemNotFoundException;
import net.wookscode.protoquests.exception.QuestDoesNotExistException;
import net.wookscode.protoquests.exception.ResultAlreadyExistException;
import net.wookscode.protoquests.result.GiveResult;
import net.wookscode.protoquests.result.Result;
import net.wookscode.protoquests.util.StateSaverAndLoader;

import java.util.Objects;

public class NewGiveResult {
    public NewGiveResult(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
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

        Item item = ItemStackArgumentType.getItemStackArgument(context, "item").getItem();
        int amount = IntegerArgumentType.getInteger(context, "amount");

        if(!Registries.ITEM.containsId(Registries.ITEM.getId(item))){
            throw ItemNotFoundException.create(item.toString());
        }

        GiveResult result = new GiveResult(result_name, item, amount);
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
