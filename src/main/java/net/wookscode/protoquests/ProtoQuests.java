package net.wookscode.protoquests;

import net.fabricmc.api.ModInitializer;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.wookscode.protoquests.command.helper.ChatInputManager;
import net.wookscode.protoquests.event.EventsManager;
import net.wookscode.protoquests.util.ModCommandRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoQuests implements ModInitializer {
	public static final String MOD_ID = "proto-quests";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final int WHITE = 0xFFFFFF;
	public static final int RED = 0xFF5757;
	public static final int PRIMARY = 0x00a6fb;
	public static final int PRIMARY_DARK = 0x0273b4;

	public static final Text PREFIX = Text.literal("[").setStyle(Style.EMPTY.withColor(WHITE))
			.append(Text.literal("PQ").setStyle(Style.EMPTY.withColor(PRIMARY_DARK)))
			.append(Text.literal("] ").setStyle(Style.EMPTY.withColor(WHITE)));

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing ProtoQuests...");

		ModCommandRegister.registerCommands();
		EventsManager.registerEvents();
		ChatInputManager.init();
	}
}