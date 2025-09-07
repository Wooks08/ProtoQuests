package net.wookscode.protoquests.exception;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;

public class QuestDoesNotExistException {
    private static final DynamicCommandExceptionType QUEST_DOES_NOT_EXIST = new DynamicCommandExceptionType(name -> ProtoQuests.PREFIX.copy()
            .append(Text.literal("Quest ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal(name.toString()).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
            .append(Text.literal(" wasn't created. If you want to create it use ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal("/pq quest create <name>.").setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
    );

    public static CommandSyntaxException create(String name){
        return QUEST_DOES_NOT_EXIST.create(name);
    }
}
