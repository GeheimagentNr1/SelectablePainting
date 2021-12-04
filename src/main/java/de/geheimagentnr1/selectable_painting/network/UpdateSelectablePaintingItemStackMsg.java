package de.geheimagentnr1.selectable_painting.network;

import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.Optional;
import java.util.function.Supplier;


public class UpdateSelectablePaintingItemStackMsg {
	
	
	private final ItemStack stack;
	
	private UpdateSelectablePaintingItemStackMsg( ItemStack _stack ) {
		
		stack = _stack;
	}
	
	//package-private
	static UpdateSelectablePaintingItemStackMsg decode( FriendlyByteBuf buffer ) {
		
		return new UpdateSelectablePaintingItemStackMsg( buffer.readItem() );
	}
	
	//package-private
	void encode( FriendlyByteBuf buffer ) {
		
		buffer.writeItem( stack );
	}
	
	public static void sendToServer( ItemStack stack ) {
		
		Network.CHANNEL.send( PacketDistributor.SERVER.noArg(), new UpdateSelectablePaintingItemStackMsg( stack ) );
	}
	
	//package-private
	void handle( Supplier<NetworkEvent.Context> context ) {
		
		Optional.ofNullable( context.get().getSender() ).ifPresent( player -> {
			if( player.containerMenu instanceof SelectablePaintingMenu selectablePaintingMenu ) {
				selectablePaintingMenu.updateItemStack( stack );
			}
		} );
		context.get().setPacketHandled( true );
	}
}
