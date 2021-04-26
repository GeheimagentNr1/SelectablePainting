package de.geheimagentnr1.selectable_painting.network;

import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;


public class SpawnSelectablePaintingMsg {
	
	
	private final int entityID;
	
	private final UUID uniqueId;
	
	private final PaintingType art;
	
	private final BlockPos position;
	
	private final Direction facing;
	
	private final int size_index;
	
	private final int painting_index;
	
	private final boolean random;
	
	private SpawnSelectablePaintingMsg(
		int _entityID,
		UUID _uniqueId,
		PaintingType _art,
		BlockPos _position,
		Direction _facing,
		int _size_index,
		int _painting_index,
		boolean _random ) {
		
		entityID = _entityID;
		uniqueId = _uniqueId;
		position = _position;
		facing = _facing;
		art = _art;
		size_index = _size_index;
		painting_index = _painting_index;
		random = _random;
	}
	
	//package-private
	static SpawnSelectablePaintingMsg decode( PacketBuffer buffer ) {
		
		return new SpawnSelectablePaintingMsg(
			buffer.readVarInt(),
			buffer.readUniqueId(),
			Registry.MOTIVE.getByValue( buffer.readVarInt() ),
			buffer.readBlockPos(),
			Direction.byHorizontalIndex( buffer.readByte() ),
			buffer.readVarInt(),
			buffer.readVarInt(),
			buffer.readBoolean()
		);
	}
	
	//package-private
	void encode( PacketBuffer buffer ) {
		
		buffer.writeVarInt( entityID );
		buffer.writeUniqueId( uniqueId );
		buffer.writeVarInt( Registry.MOTIVE.getId( art ) );
		buffer.writeBlockPos( position );
		buffer.writeByte( facing.getHorizontalIndex() );
		buffer.writeVarInt( size_index );
		buffer.writeVarInt( painting_index );
		buffer.writeBoolean( random );
	}
	
	public static void sendToChunkTrackers( Chunk chunk, SelectablePaintingEntity selectablePaintingEntity ) {
		
		Network.CHANNEL.send(
			PacketDistributor.TRACKING_CHUNK.with( () -> chunk ),
			new SpawnSelectablePaintingMsg(
				selectablePaintingEntity.getEntityId(),
				selectablePaintingEntity.getUniqueID(),
				selectablePaintingEntity.getArt(),
				selectablePaintingEntity.getHangingPosition(),
				selectablePaintingEntity.getHorizontalFacing(),
				selectablePaintingEntity.getSizeIndex(),
				selectablePaintingEntity.getPaintingIndex(),
				selectablePaintingEntity.isRandom()
			)
		);
	}
	
	//package-private
	void handle( Supplier<NetworkEvent.Context> context ) {
		
		Optional.ofNullable( Minecraft.getInstance().world ).ifPresent( world -> {
			SelectablePaintingEntity selectablePaintingEntity = new SelectablePaintingEntity(
				world,
				position,
				facing,
				art,
				size_index,
				painting_index,
				random
			);
			selectablePaintingEntity.setEntityId( entityID );
			selectablePaintingEntity.setUniqueId( uniqueId );
			world.addEntity( entityID, selectablePaintingEntity );
		} );
		context.get().setPacketHandled( true );
	}
}
