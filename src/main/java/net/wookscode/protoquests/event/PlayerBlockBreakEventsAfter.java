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
                    Identifier brokenBlockId = Registries.BLOCK.getId(state.getBlock());
                    String taskBlockId = (String) task.getProps().get("block");
                    BlockPos target_pos = (BlockPos) task.getProps().get("position");

                    if (Objects.equals(taskBlockId, brokenBlockId.toString())) {
                        if(task.getProps().size() > 8) {
                            int completed_actions = (int) task.getProps().getOrDefault("actions_completed", 0);
                            int target = Integer.parseInt((String) task.getProps().get("actions_to_complete"));

                            completed_actions++;
                            task.getProps().put("actions_completed", completed_actions);

                            double progress = ((double) completed_actions / target) * 100;
                            task.setProgress(progress);

                            if (completed_actions >= target) {
                                task.setProgress(100);
                            }
                        }
                        else if(task.getProps().size() == 8) {
                            //TODO: Fix not properly working position check

                            if(target_pos.getX() == pos.getX() && target_pos.getY() == pos.getY() && target_pos.getZ() == pos.getZ()){
                                int completed_actions = (int) task.getProps().getOrDefault("actions_completed", 0);
                                int target = Integer.parseInt((String) task.getProps().get("actions_to_complete"));

                                completed_actions++;
                                task.getProps().put("actions_completed", completed_actions);

                                double progress = ((double) completed_actions / target) * 100;
                                task.setProgress(progress);

                                if (completed_actions >= target) {
                                    task.setProgress(100);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
