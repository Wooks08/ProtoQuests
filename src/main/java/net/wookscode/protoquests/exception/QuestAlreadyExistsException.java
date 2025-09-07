package net.wookscode.protoquests.exception;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;

public class QuestAlreadyExistsException {
    private static final DynamicCommandExceptionType QUEST_ALREADY_EXISTS = new DynamicCommandExceptionType(name -> ProtoQuests.PREFIX.copy()
            .append(Text.literal("Quest ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal(name.toString()).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
            .append(Text.literal(" already exists").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
    );

    public static CommandSyntaxException create(String name){
        return QUEST_ALREADY_EXISTS.create(name);
    }
}
