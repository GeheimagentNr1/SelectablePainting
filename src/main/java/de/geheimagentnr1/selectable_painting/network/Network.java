package de.geheimagentnr1.selectable_painting.network;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;


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
			SpawnSelectablePaintingMsg.class,
			SpawnSelectablePaintingMsg::encode,
			SpawnSelectablePaintingMsg::decode,
			SpawnSelectablePaintingMsg::handle
		);
		CHANNEL.registerMessage(
			1,
			UpdateSelectablePaintingItemStackMsg.class,
			UpdateSelectablePaintingItemStackMsg::encode,
			UpdateSelectablePaintingItemStackMsg::decode,
			UpdateSelectablePaintingItemStackMsg::handle
		);
	}
}
