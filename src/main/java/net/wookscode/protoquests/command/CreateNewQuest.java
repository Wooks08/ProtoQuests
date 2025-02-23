package net.wookscode.protoquests.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;
import net.wookscode.protoquests.quest.Quest;
import net.wookscode.protoquests.util.StateSaverAndLoader;

import java.util.Map;

public class CreateNewQuest {
    private static final DynamicCommandExceptionType QUEST_ALREADY_EXISTS = new DynamicCommandExceptionType(name -> ProtoQuests.PREFIX.copy()
            .append(Text.literal("Quest ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal(name.toString()).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
            .append(Text.literal(" already exists").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                           CommandRegistryAccess commandRegistryAccess,
                           CommandManager.RegistrationEnvironment registrationEnvironment){

        dispatcher.register(CommandManager.literal("pq").then(CommandManager.literal("quest").then(CommandManager.literal("create")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .executes(CreateNewQuest::run)))));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();

        StateSaverAndLoader state = StateSaverAndLoader.getServerState(server);

        final String name = StringArgumentType.getString(context, "name");
        if (state.quests.containsKey(name)){
            throw QUEST_ALREADY_EXISTS.create(name);
        }

        Quest newQuest = new Quest(name);

        state.quests.put(name, newQuest);
        state.markDirty();

        source.sendMessage(Text.literal(state.quests.toString()));
        for (Map.Entry<String, Quest> entry : state.quests.entrySet()){
            source.sendMessage(Text.literal(entry.getValue().getProps().toString()));
        }

        source.sendMessage(ProtoQuests.PREFIX.copy().append(Text.literal("Quest added successfully!")));

        return 1;
    }


}
