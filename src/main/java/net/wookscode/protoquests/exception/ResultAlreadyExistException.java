package net.wookscode.protoquests.exception;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;

public class ResultAlreadyExistException {
    private static final DynamicCommandExceptionType RESULT_ALREADY_EXISTS = new DynamicCommandExceptionType(name -> ProtoQuests.PREFIX.copy()
            .append(Text.literal("Result of name ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal(name.toString()).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
            .append(Text.literal(" already exists in this quest!").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
    );

    public static CommandSyntaxException create(String name){
        return RESULT_ALREADY_EXISTS.create(name);
    }
}
