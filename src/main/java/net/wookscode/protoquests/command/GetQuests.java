package net.wookscode.protoquests.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;
import net.wookscode.protoquests.quest.Quest;
import net.wookscode.protoquests.task.Task;
import net.wookscode.protoquests.util.StateSaverAndLoader;

import java.util.List;
import java.util.Map;

public class GetQuests {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                           CommandRegistryAccess commandRegistryAccess,
                           CommandManager.RegistrationEnvironment registrationEnvironment){

        dispatcher.register(CommandManager.literal("pq").then(CommandManager.literal("quest").then(CommandManager.literal("list")
                        .executes(GetQuests::run))));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        MinecraftServer server = source.getServer();

        StateSaverAndLoader state = StateSaverAndLoader.getServerState(server);

        Text msg = ProtoQuests.PREFIX.copy().append(Text.literal("Quests:"));
        for(Map.Entry<String, Quest> entry : state.quests.entrySet()){
            msg = msg.copy().append(Text.literal("\n- " + entry.getKey() + ":\n"));


            List<Task> tasks = entry.getValue().getTasks();

            msg = msg.copy().append(Text.literal("  - Tasks:\n").setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)));
            if(!tasks.isEmpty()) {
                for (Task task : tasks) {
                    int progress = (int) (Math.floor((double) task.getProps().get("progress") / 10));
                    msg = msg.copy().append(Text.literal("    > ").setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
                            .append(Text.literal(task.getName())).setStyle(Style.EMPTY.withColor(ProtoQuests.WHITE))
                            .append(Text.literal("\n        Progress: ").setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
                            .append(Text.literal("█".repeat(progress)).setStyle(Style.EMPTY.withColor(ProtoQuests.WHITE)))
                            .append(Text.literal(" " + task.getProps().get("progress").toString() + "%").setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
                            .append(Text.literal("\n"));
                }
            }
            else{
                msg = msg.copy().append(Text.literal("      No Tasks\n"));
            }

            List<String> results = entry.getValue().getResults();

            msg = msg.copy().append(Text.literal("  - Results:\n").setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)));
            if(!results.stream().allMatch(String::isEmpty)){
                for (String result : results) {
                    msg = msg.copy().append(Text.literal("    > ").setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
                            .append(Text.literal(result).setStyle(Style.EMPTY.withColor(ProtoQuests.WHITE)))
                            .append(Text.literal("\n"));
                }
            }
            else{
                msg = msg.copy().append(Text.literal("      No Results\n"));
            }
        }

        context.getSource().sendMessage(msg);

        return 1;
    }


}
