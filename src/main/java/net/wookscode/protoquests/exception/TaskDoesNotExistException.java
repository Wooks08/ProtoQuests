package net.wookscode.protoquests.exception;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;

public class TaskDoesNotExistException {
    private static final DynamicCommandExceptionType TASK_DOES_NOT_EXIST = new DynamicCommandExceptionType(name -> ProtoQuests.PREFIX.copy()
            .append(Text.literal("Task ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal(name.toString()).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
            .append(Text.literal(" wasn't added to this quest. If you want to add it use ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal("/pq task add <quest_name> <task_name> ...").setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
    );

    public static CommandSyntaxException create(String name){
        return TASK_DOES_NOT_EXIST.create(name);
    }
}
