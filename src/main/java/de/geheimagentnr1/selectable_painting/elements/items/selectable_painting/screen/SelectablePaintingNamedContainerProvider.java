package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class SelectablePaintingNamedContainerProvider implements INamedContainerProvider {
	
	
	private final ItemStack stack;
	
	public SelectablePaintingNamedContainerProvider( ItemStack _stack ) {
		
		stack = _stack;
	}
	
	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		
		return new TranslationTextComponent( Util.makeDescriptionId(
			"container",
			ModItems.SELECTABLE_PAINTING.getRegistryName()
		) );
	}
	
	@Nullable
	@Override
	public Container createMenu(
		int window_id,
		@Nonnull PlayerInventory playerInventory,
		@Nonnull PlayerEntity playerEntity ) {
		
		return new SelectablePaintingContainer( window_id, stack );
	}
}
