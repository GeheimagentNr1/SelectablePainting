package de.geheimagentnr1.selectable_painting.network;

import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Optional;
import java.util.function.Supplier;


public class UpdateSelectablePaintingItemStackMsg {
	
	
	private final ItemStack stack;
	
	private UpdateSelectablePaintingItemStackMsg( ItemStack _stack ) {
		
		stack = _stack;
	}
	
	//package-private
	static UpdateSelectablePaintingItemStackMsg decode( PacketBuffer buffer ) {
		
		return new UpdateSelectablePaintingItemStackMsg( buffer.readItemStack() );
	}
	
	//package-private
	void encode( PacketBuffer buffer ) {
		
		buffer.writeItemStack( stack );
	}
	
	public static void sendToServer( ItemStack stack ) {
		
		Network.CHANNEL.send( PacketDistributor.SERVER.noArg(), new UpdateSelectablePaintingItemStackMsg( stack ) );
	}
	
	//package-private
	void handle( Supplier<NetworkEvent.Context> context ) {
		
		Optional.ofNullable( context.get().getSender() ).ifPresent( player -> {
			if( player.openContainer instanceof SelectablePaintingContainer ) {
				SelectablePaintingContainer selectablePaintingContainer =
					(SelectablePaintingContainer)player.openContainer;
				selectablePaintingContainer.updateItemStack( stack );
			}
		} );
		context.get().setPacketHandled( true );
	}
}
