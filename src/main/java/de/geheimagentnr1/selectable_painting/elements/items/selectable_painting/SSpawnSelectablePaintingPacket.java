package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import de.geheimagentnr1.selectable_painting.helpers.RegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;


public class SSpawnSelectablePaintingPacket {
	
	
	private final int entityID;
	
	private final UUID uniqueId;
	
	private final BlockPos position;
	
	private final Direction facing;
	
	private final int title;
	
	private final int size_index;
	
	private final int painting_index;
	
	public SSpawnSelectablePaintingPacket( PacketBuffer buffer ) {
		
		entityID = buffer.readVarInt();
		uniqueId = buffer.readUniqueId();
		title = buffer.readVarInt();
		position = buffer.readBlockPos();
		facing = Direction.byHorizontalIndex( buffer.readUnsignedByte() );
		size_index = buffer.readVarInt();
		painting_index = buffer.readVarInt();
	}
	
	//package-private
	SSpawnSelectablePaintingPacket( SelectablePaintingEntity selectablePaintingEntity ) {
		
		entityID = selectablePaintingEntity.getEntityId();
		uniqueId = selectablePaintingEntity.getUniqueID();
		position = selectablePaintingEntity.getHangingPosition();
		facing = selectablePaintingEntity.getHorizontalFacing();
		title = RegistryHelper.getMotiveRegistry().getId( selectablePaintingEntity.art );
		size_index = selectablePaintingEntity.size_index;
		painting_index = selectablePaintingEntity.painting_index;
	}
	
	private PaintingType getPaintingType() {
		
		return RegistryHelper.getMotiveRegistry().getByValue( title );
	}
	
	public void encode( PacketBuffer buffer ) {
		
		buffer.writeVarInt( entityID );
		buffer.writeUniqueId( uniqueId );
		buffer.writeVarInt( title );
		buffer.writeBlockPos( position );
		buffer.writeByte( facing.getHorizontalIndex() );
		buffer.writeVarInt( size_index );
		buffer.writeVarInt( painting_index );
	}
	
	public void handle( Supplier<NetworkEvent.Context> context ) {
		
		Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get(
			context.get().getDirection().getReceptionSide() );
		if( !world.isPresent() || !( world.get() instanceof ClientWorld ) ) {
			return;
		}
		SelectablePaintingEntity selectablePaintingEntity = new SelectablePaintingEntity( world.get(), position,
			facing, getPaintingType(), size_index, painting_index );
		selectablePaintingEntity.setEntityId( entityID );
		selectablePaintingEntity.setUniqueId( uniqueId );
		Minecraft.getInstance().world.addEntity( entityID, selectablePaintingEntity );
	}
}
