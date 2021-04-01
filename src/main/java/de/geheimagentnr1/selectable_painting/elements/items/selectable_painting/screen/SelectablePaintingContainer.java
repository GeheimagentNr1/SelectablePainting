package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.PaintingSelectionHelper;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingItemStackHelper;
import de.geheimagentnr1.selectable_painting.network.UpdateSelectablePaintingItemStackMsg;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;


public class SelectablePaintingContainer extends Container {
	
	
	private final ItemStack stack;
	
	public SelectablePaintingContainer( int windowID, PacketBuffer buffer ) {
		
		this( windowID, buffer.readItemStack() );
	}
	
	//package-private
	SelectablePaintingContainer( int windowID, ItemStack _stack ) {
		
		super( ModItems.SELECTABLE_PAINTING_CONTAINER, windowID );
		stack = _stack;
	}
	
	@Override
	public boolean canInteractWith( @Nonnull PlayerEntity playerIn ) {
		
		return true;
	}
	
	public void updateItemStack( ItemStack _stack ) {
		
		SelectablePaintingItemStackHelper.writeDataToStack(
			stack,
			SelectablePaintingItemStackHelper.getSizeIndex( _stack ),
			SelectablePaintingItemStackHelper.getPaintingIndex( _stack ),
			SelectablePaintingItemStackHelper.getRandom( _stack )
		);
	}
	
	public String getSizeText() {
		
		return PaintingSelectionHelper.getSizeName( stack );
	}
	
	public String getPaintingText() {
		
		return PaintingSelectionHelper.getPaintingName( stack ).getString();
	}
	
	public PaintingType getCurrentPaintingType() {
		
		return PaintingSelectionHelper.getCurrentPaintingType( stack );
	}
	
	public void previousSize() {
		
		PaintingSelectionHelper.previousSize( stack );
		UpdateSelectablePaintingItemStackMsg.sendToServer( stack );
	}
	
	public void nextSize() {
		
		PaintingSelectionHelper.nextSize( stack );
		UpdateSelectablePaintingItemStackMsg.sendToServer( stack );
	}
	
	public void previousPainting() {
		
		PaintingSelectionHelper.previousPainting( stack );
		UpdateSelectablePaintingItemStackMsg.sendToServer( stack );
	}
	
	public void nextPainting() {
		
		PaintingSelectionHelper.nextPainting( stack );
		UpdateSelectablePaintingItemStackMsg.sendToServer( stack );
	}
	
	public void toggleRandom() {
		
		SelectablePaintingItemStackHelper.toogleRandom( stack );
		UpdateSelectablePaintingItemStackMsg.sendToServer( stack );
	}
	
	public boolean getRandom() {
		
		return SelectablePaintingItemStackHelper.getRandom( stack );
	}
}
