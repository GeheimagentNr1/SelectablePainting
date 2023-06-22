package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.elements.items.ModItemsRegisterFactory;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.PaintingSelectionHelper;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingItemStackHelper;
import de.geheimagentnr1.selectable_painting.network.UpdateSelectablePaintingItemStackMsg;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class SelectablePaintingMenu extends AbstractContainerMenu {
	
	
	@NotNull
	private final ItemStack stack;
	
	public SelectablePaintingMenu( int windowId, @NotNull FriendlyByteBuf buffer ) {
		
		this( windowId, buffer.readItem() );
	}
	
	//package-private
	SelectablePaintingMenu( int windowId, @NotNull ItemStack _stack ) {
		
		super( ModItemsRegisterFactory.SELECTABLE_PAINTING_MENU, windowId );
		stack = _stack;
	}
	
	@NotNull
	@Override
	public ItemStack quickMoveStack( @NotNull Player player, int index ) {
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean stillValid( @NotNull Player player ) {
		
		return true;
	}
	
	public void updateItemStack( @NotNull ItemStack _stack ) {
		
		SelectablePaintingItemStackHelper.writeDataToStack(
			stack,
			SelectablePaintingItemStackHelper.getSizeIndex( _stack ),
			SelectablePaintingItemStackHelper.getPaintingIndex( _stack ),
			SelectablePaintingItemStackHelper.getRandom( _stack )
		);
	}
	
	@NotNull
	public String getSizeText() {
		
		return PaintingSelectionHelper.getSizeName( stack );
	}
	
	@NotNull
	public String getPaintingText() {
		
		return PaintingSelectionHelper.getPaintingName( stack ).getString();
	}
	
	@NotNull
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
