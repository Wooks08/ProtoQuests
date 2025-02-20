package net.wookscode.protoquests;

import net.fabricmc.api.ModInitializer;

import net.wookscode.protoquests.util.ModCommandRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoQuests implements ModInitializer {
	public static final String MOD_ID = "proto-quests";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing ProtoQuests...");

		ModCommandRegister.registerCommands();
	}
}