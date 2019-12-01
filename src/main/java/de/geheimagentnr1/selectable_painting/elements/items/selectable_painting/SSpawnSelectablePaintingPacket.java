package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;
import java.util.UUID;


//package-private
class SSpawnSelectablePaintingPacket implements IPacket<IClientPlayNetHandler> {
	
	
	private int entityID;
	
	private UUID uniqueId;
	
	private BlockPos position;
	
	private Direction facing;
	
	private int title;
	
	private int size_index;
	
	private int painting_index;
	
	//package-private
	@SuppressWarnings( "deprecation" )
	SSpawnSelectablePaintingPacket( SelectablePaintingEntity selectablePaintingEntity ) {
		
		entityID = selectablePaintingEntity.getEntityId();
		uniqueId = selectablePaintingEntity.getUniqueID();
		position = selectablePaintingEntity.getHangingPosition();
		facing = selectablePaintingEntity.getHorizontalFacing();
		title = Registry.MOTIVE.getId( selectablePaintingEntity.art );
		size_index = selectablePaintingEntity.size_index;
		painting_index = selectablePaintingEntity.painting_index;
	}
	
	@Override
	public void readPacketData( PacketBuffer buf ) {
		
		entityID = buf.readVarInt();
		uniqueId = buf.readUniqueId();
		title = buf.readVarInt();
		position = buf.readBlockPos();
		facing = Direction.byHorizontalIndex( buf.readUnsignedByte() );
		size_index = buf.readVarInt();
		painting_index = buf.readVarInt();
	}
	
	@Override
	public void writePacketData( PacketBuffer buf ) {
		
		buf.writeVarInt( entityID );
		buf.writeUniqueId( uniqueId );
		buf.writeVarInt( title );
		buf.writeBlockPos( position );
		buf.writeByte( facing.getHorizontalIndex() );
		buf.writeVarInt( size_index );
		buf.writeVarInt( painting_index );
	}
	
	@Override
	public void processPacket( @Nonnull IClientPlayNetHandler handler ) {
		
		if( handler instanceof ClientPlayNetHandler ) {
			ClientPlayNetHandler clientPlayNetHandler = (ClientPlayNetHandler)handler;
			PacketThreadUtil.checkThreadAndEnqueue( this, handler, Minecraft.getInstance() );
			SelectablePaintingEntity selectablePaintingEntity = new SelectablePaintingEntity(
				clientPlayNetHandler.getWorld(), getPosition(), getFacing(), getType(), getSizeIndex(),
				getPaintingIndex() );
			selectablePaintingEntity.setEntityId( getEntityID() );
			selectablePaintingEntity.setUniqueId( getUniqueId() );
			clientPlayNetHandler.getWorld().addEntity( getEntityID(), selectablePaintingEntity );
		}
	}
	
	private int getEntityID() {
		
		return entityID;
	}
	
	private UUID getUniqueId() {
		
		return uniqueId;
	}
	
	private BlockPos getPosition() {
		
		return position;
	}
	
	private Direction getFacing() {
		
		return facing;
	}
	
	@SuppressWarnings( "deprecation" )
	private PaintingType getType() {
		
		return Registry.MOTIVE.getByValue( title );
	}
	
	private int getSizeIndex() {
		
		return size_index;
	}
	
	private int getPaintingIndex() {
		
		return painting_index;
	}
}
