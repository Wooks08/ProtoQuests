package net.wookscode.protoquests.command.helper;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatInputManager {
    private static final Map<UUID, Consumer<String>> awaitingResponses = new HashMap<>();

    public static void init() {
        // Intercept chat messages
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            UUID id = sender.getUuid();
            if (awaitingResponses.containsKey(id)) {
                Consumer<String> callback = awaitingResponses.remove(id);
                callback.accept(message.getContent().getString());
                return false; // block from going to public chat
            }
            return true; // normal chat
        });
    }

    public static void awaitResponse(ServerPlayerEntity player, Consumer<String> callback) {
        awaitingResponses.put(player.getUuid(), callback);
    }
}
