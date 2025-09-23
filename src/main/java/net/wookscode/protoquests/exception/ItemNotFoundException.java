package net.wookscode.protoquests.exception;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;

public class ItemNotFoundException {
    private static final DynamicCommandExceptionType BLOCK_NOT_FOUND = new DynamicCommandExceptionType(item -> ProtoQuests.PREFIX.copy()
            .append(Text.literal("Item ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal(item.toString()).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
            .append(Text.literal(" not found!").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
    );

    public static CommandSyntaxException create(String item){
        return BLOCK_NOT_FOUND.create(item);
    }
}
