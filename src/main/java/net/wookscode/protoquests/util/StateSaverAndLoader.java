package net.wookscode.protoquests.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.wookscode.protoquests.ProtoQuests;
import net.wookscode.protoquests.quest.Quest;

import java.util.HashMap;
import java.util.Map;

public class StateSaverAndLoader extends PersistentState {
    public static HashMap<String, Quest> quests = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound quests_compound = new NbtCompound();

        for (Map.Entry<String, Quest> entry : quests.entrySet()) {
            quests_compound.put(entry.getKey(), entry.getValue().toNbt());
        }

        nbt.put("quests", quests_compound);

        return nbt;
    }

    //creating new instance of StateSaverAndLoader and setting quests HashMap to data saved in passed nbt
    public static StateSaverAndLoader fromNbt(NbtCompound nbt) {
        StateSaverAndLoader state = new StateSaverAndLoader();
        state.quests = new HashMap<>();

        if (nbt.contains("quests")) {
            NbtCompound quests_compound = nbt.getCompound("quests");

            for (String key : quests_compound.getKeys()) {
                Quest quest = Quest.fromNbt(quests_compound.getCompound(key));
                state.quests.put(key, quest);
            }
        }

        return state;
    }

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        PersistentStateManager persistent_state_manager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        return persistent_state_manager.getOrCreate(
                StateSaverAndLoader::fromNbt,
                StateSaverAndLoader::new,
                ProtoQuests.MOD_ID
        );
    }
}
