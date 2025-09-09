package net.wookscode.protoquests.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.wookscode.protoquests.quest.Quest;
import net.wookscode.protoquests.task.Task;
import net.wookscode.protoquests.util.StateSaverAndLoader;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class PlayerBlockBreakEventsAfter implements PlayerBlockBreakEvents.After {

    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos,
                                BlockState state, @Nullable BlockEntity blockEntity) {

        MinecraftServer server = world.getServer();

        assert server != null;
        StateSaverAndLoader server_state = StateSaverAndLoader.getServerState(server);

        for(Map.Entry<String, Quest> quest : server_state.quests.entrySet()){
            for(Task task : quest.getValue().getTasks()){
                if(Objects.equals(task.getProps().get("type"), "break")){
                    Block block_task = Registries.BLOCK.get(new Identifier((String) task.getProps().get("block")));
                    Block block_broken = state.getBlock();

                    if(Objects.equals(Registries.BLOCK.get(new Identifier((String) task.getProps().get("block"))), state.getBlock())){
                        double current_progress = (double) task.getProps().get("progress");
                        if(current_progress < 100) {
                            double progress_per_block = (double) 100 / Integer.parseInt((String) task.getProps().get("number"));
                            task.setProgress(current_progress + progress_per_block);
                        }
                    }
                }
            }
        }
    }
}
