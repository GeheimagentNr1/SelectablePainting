package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.elements.items.ModItemsRegisterFactory;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.PaintingSelectionHelper;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingItemStackHelper;
import de.geheimagentnr1.selectable_painting.network.UpdateSelectablePaintingItemStackMsg;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class SelectablePaintingMenu extends AbstractContainerMenu {
	
	
	@NotNull
	private final ItemStack stack;
	
	public SelectablePaintingMenu( int windowId, @NotNull Inventory inventory ) {
		
		this( windowId, inventory.player.getItemInHand( InteractionHand.MAIN_HAND ) );
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
	
	private RegistryAccess registryAccess() {
		
		return Objects.requireNonNull( Minecraft.getInstance().getConnection() ).registryAccess();
	}
	
	@NotNull
	public String getSizeText() {
		
		return PaintingSelectionHelper.getSizeName( registryAccess(), stack );
	}
	
	@NotNull
	public String getPaintingText() {
		
		return PaintingSelectionHelper.getPaintingName( registryAccess(), stack ).getString();
	}
	
	@NotNull
	public PaintingVariant getCurrentMotive() {
		
		return PaintingSelectionHelper.getCurrentMotive( registryAccess(), stack );
	}
	
	public void previousSize() {
		
		PaintingSelectionHelper.previousSize( registryAccess(), stack );
		UpdateSelectablePaintingItemStackMsg.sendToServer( stack );
	}
	
	public void nextSize() {
		
		PaintingSelectionHelper.nextSize( registryAccess(), stack );
		UpdateSelectablePaintingItemStackMsg.sendToServer( stack );
	}
	
	public void previousPainting() {
		
		PaintingSelectionHelper.previousPainting( registryAccess(), stack );
		UpdateSelectablePaintingItemStackMsg.sendToServer( stack );
	}
	
	public void nextPainting() {
		
		PaintingSelectionHelper.nextPainting( registryAccess(), stack );
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
