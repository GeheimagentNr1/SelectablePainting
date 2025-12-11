package de.geheimagentnr1.selectable_painting.network;

import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;


public record UpdateSelectablePaintingItemStackMsg( @NotNull ItemStack stack ) implements CustomPacketPayload {
	
	
	@NotNull
	public static final Type<UpdateSelectablePaintingItemStackMsg> TYPE = 
		new Type<>( Network.createId( "update_selectable_painting_item_stack" ) );
	
	@NotNull
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateSelectablePaintingItemStackMsg> STREAM_CODEC =
		StreamCodec.composite(
			ItemStack.STREAM_CODEC,
			UpdateSelectablePaintingItemStackMsg::stack,
			UpdateSelectablePaintingItemStackMsg::new
		);
	
	@NotNull
	@Override
	public Type<? extends CustomPacketPayload> type() {
		
		return TYPE;
	}
	
	public static void sendToServer( @NotNull ItemStack stack ) {
		
		PacketDistributor.sendToServer( new UpdateSelectablePaintingItemStackMsg( stack ) );
	}
	
	public static void handle( @NotNull UpdateSelectablePaintingItemStackMsg msg, @NotNull IPayloadContext context ) {
		
		context.enqueueWork( () -> {
			if( context.player().containerMenu instanceof SelectablePaintingMenu selectablePaintingMenu ) {
				selectablePaintingMenu.updateItemStack( msg.stack() );
			}
		} );
	}
}
