package net.wookscode.protoquests.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.wookscode.protoquests.ProtoQuests;

import java.util.HashMap;
import java.util.Map;

public class StateSaverAndLoader extends PersistentState {
    public static HashMap<String, Quest> quests = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound questsCompound = new NbtCompound();

        for (Map.Entry<String, Quest> entry : quests.entrySet()) {
            questsCompound.put(entry.getKey(), entry.getValue().toNbt());
        }

        nbt.put("quests", questsCompound);

        return nbt;
    }

    //creating new instance of StateSaverAndLoader and setting quests HashMap to data saved in passed nbt
    public static StateSaverAndLoader fromNbt(NbtCompound nbt) {
        StateSaverAndLoader state = new StateSaverAndLoader();
        state.quests = new HashMap<>();

        if (nbt.contains("quests")) {
            NbtCompound questsCompound = nbt.getCompound("quests");

            for (String key : questsCompound.getKeys()) {
                Quest quest = Quest.fromNbt(questsCompound.getCompound(key));
                state.quests.put(key, quest);
            }
        }

        return state;
    }

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        return persistentStateManager.getOrCreate(
                StateSaverAndLoader::fromNbt,
                StateSaverAndLoader::new,
                ProtoQuests.MOD_ID
        );
    }

    public static HashMap<String, Quest> getQuests(){
        return quests;
    }
}
