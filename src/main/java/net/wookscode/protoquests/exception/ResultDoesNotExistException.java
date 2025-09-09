package net.wookscode.protoquests.exception;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;

public class ResultDoesNotExistException {
    private static final DynamicCommandExceptionType RESULT_DOES_NOT_EXIST = new DynamicCommandExceptionType(name -> ProtoQuests.PREFIX.copy()
            .append(Text.literal("Result ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal(name.toString()).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
            .append(Text.literal(" wasn't added to this quest. If you want to add it use ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal("/pq result add <quest_name> <result_name> ...").setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
    );

    public static CommandSyntaxException create(String name){
        return RESULT_DOES_NOT_EXIST.create(name);
    }
}
