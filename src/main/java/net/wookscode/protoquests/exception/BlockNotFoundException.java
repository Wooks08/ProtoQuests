package net.wookscode.protoquests.exception;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.block.Block;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.ProtoQuests;

public class BlockNotFoundException {
    private static final DynamicCommandExceptionType BLOCK_NOT_FOUND = new DynamicCommandExceptionType(block -> ProtoQuests.PREFIX.copy()
            .append(Text.literal("Block ").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
            .append(Text.literal(block.toString()).setStyle(Style.EMPTY.withColor(ProtoQuests.PRIMARY)))
            .append(Text.literal(" not found!").setStyle(Style.EMPTY.withColor(ProtoQuests.RED)))
    );

    public static CommandSyntaxException create(String block){
        return BLOCK_NOT_FOUND.create(block);
    }
}
