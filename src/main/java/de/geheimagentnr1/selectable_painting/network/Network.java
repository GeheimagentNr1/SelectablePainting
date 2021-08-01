package de.geheimagentnr1.selectable_painting.network;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;


public class Network {
	
	
	private static final String PROTOCOL_VERSION = "1";
	
	//package-private
	static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
		new ResourceLocation( SelectablePaintingMod.MODID, "main" ),
		() -> PROTOCOL_VERSION,
		PROTOCOL_VERSION::equals,
		PROTOCOL_VERSION::equals
	);
	
	public static void registerPackets() {
		
		CHANNEL.registerMessage(
			0,
			UpdateSelectablePaintingItemStackMsg.class,
			UpdateSelectablePaintingItemStackMsg::encode,
			UpdateSelectablePaintingItemStackMsg::decode,
			UpdateSelectablePaintingItemStackMsg::handle
		);
	}
}
