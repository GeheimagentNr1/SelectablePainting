package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.PaintingSelectionHelper;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingItemStackHelper;
import de.geheimagentnr1.selectable_painting.network.UpdateSelectablePaintingItemStackMsg;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;


public class SelectablePaintingMenu extends AbstractContainerMenu {
	
	
	private final ItemStack stack;
	
	public SelectablePaintingMenu( int windowID, FriendlyByteBuf buffer ) {
		
		this( windowID, buffer.readItem() );
	}
	
	//package-private
	SelectablePaintingMenu( int windowID, ItemStack _stack ) {
		
		super( ModItems.SELECTABLE_PAINTING_MENU, windowID );
		stack = _stack;
	}
	
	@Override
	public @NotNull ItemStack quickMoveStack( Player p_38941_, int p_38942_ ) {
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean stillValid( @Nonnull Player playerIn ) {
		
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
	
	public PaintingVariant getCurrentMotive() {
		
		return PaintingSelectionHelper.getCurrentMotive( stack );
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
