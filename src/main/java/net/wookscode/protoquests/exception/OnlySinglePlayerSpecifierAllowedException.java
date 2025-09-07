package net.wookscode.protoquests.exception;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;

public class OnlySinglePlayerSpecifierAllowedException {
    private static final SimpleCommandExceptionType ONLY_SINGLE_PLAYER_SPECIFIER_ALLOWED_EXCEPTION = new SimpleCommandExceptionType(ProtoQuests.PREFIX.copy()
            .append(Text.literal("For safety measures you can only specify a single player in this command. Don't use @a, @r etc.").setStyle(Style.EMPTY.withColor(ProtoQuests.WHITE)))
    );

    public static CommandSyntaxException create(){
        return ONLY_SINGLE_PLAYER_SPECIFIER_ALLOWED_EXCEPTION.create();
    }
}
