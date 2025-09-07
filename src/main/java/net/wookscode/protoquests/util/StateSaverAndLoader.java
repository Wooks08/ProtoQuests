package net.wookscode.protoquests.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.wookscode.protoquests.ProtoQuests;
import net.wookscode.protoquests.quest.Quest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StateSaverAndLoader extends PersistentState {
    public static HashMap<String, Quest> quests = new HashMap<>();

    public static HashMap<UUID, PlayerData> players = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        //write quests to global nbt data
        NbtCompound quests_compound = new NbtCompound();

        for (Map.Entry<String, Quest> entry : quests.entrySet()) {
            quests_compound.put(entry.getKey(), entry.getValue().toNbt());
        }

        nbt.put("quests", quests_compound);

        NbtCompound players_compound = new NbtCompound();

        players.forEach(((uuid, playerData) -> {
            NbtCompound player_compound = new NbtCompound();

            NbtCompound player_quests_compound = new NbtCompound();
            for(Map.Entry<String, Quest> entry : PlayerData.assigned_quests.entrySet()){
                player_quests_compound.put(entry.getKey(), entry.getValue().toNbt());
            }

            player_compound.put("assinged_quest", player_quests_compound);

            players_compound.put(uuid.toString(), player_compound);
        }));

        nbt.put("players", players_compound);

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


        if (nbt.contains("players")) {
            NbtCompound players_compound = nbt.getCompound("players");

            players_compound.getKeys().forEach(player_uuid -> {
                PlayerData player_data = new PlayerData();

                NbtCompound player_assinged_quests = players_compound.getCompound(player_uuid).getCompound("assinged_quest");

                for (String compound_key : player_assinged_quests.getKeys()){
                    Quest quest = Quest.fromNbt(player_assinged_quests.getCompound(compound_key));

                    player_data.assigned_quests.put(compound_key, quest);

                    UUID uuid = UUID.fromString(player_uuid);
                    state.players.put(uuid, player_data);
                }
            });
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

    public static PlayerData getPlayerState(LivingEntity player){
        StateSaverAndLoader server_state = getServerState(player.getWorld().getServer());

        PlayerData player_state = server_state.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());

        return  player_state;
    }
}
