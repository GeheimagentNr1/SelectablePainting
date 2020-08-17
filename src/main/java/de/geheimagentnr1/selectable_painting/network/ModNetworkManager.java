package de.geheimagentnr1.selectable_painting.network;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SSpawnSelectablePaintingPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;


public class ModNetworkManager {
	
	
	private static final String PROTOCOL_VERSION = "1";
	
	private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
		new ResourceLocation( SelectablePaintingMod.MODID, "main" ),
		() -> PROTOCOL_VERSION,
		PROTOCOL_VERSION::equals,
		PROTOCOL_VERSION::equals
	);
	
	public static void registerPackets() {
		
		CHANNEL.registerMessage( 0, SSpawnSelectablePaintingPacket.class, SSpawnSelectablePaintingPacket::encode,
			ModNetworkManager::decode, ModNetworkManager::handleSpawnSelectablePainting );
	}
	
	private static SSpawnSelectablePaintingPacket decode( PacketBuffer buffer ) {
		
		return new SSpawnSelectablePaintingPacket( buffer );
	}
	
	private static void handleSpawnSelectablePainting( SSpawnSelectablePaintingPacket packet,
		Supplier<NetworkEvent.Context> context ) {
		
		context.get().enqueueWork( () -> packet.handle( context ) );
		context.get().setPacketHandled( true );
	}
	
	public static void sendSSpawnSelectablePaintingPacket( SSpawnSelectablePaintingPacket packet, Chunk chunk ) {
		
		CHANNEL.send( PacketDistributor.TRACKING_CHUNK.with( () -> chunk ), packet );
	}
}
