package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class SelectablePaintingNamedContainerProvider implements MenuProvider {
	
	
	private final ItemStack stack;
	
	public SelectablePaintingNamedContainerProvider( ItemStack _stack ) {
		
		stack = _stack;
	}
	
	@Nonnull
	@Override
	public Component getDisplayName() {
		
		return new TranslatableComponent( Util.makeDescriptionId(
			"container",
			ModItems.SELECTABLE_PAINTING.getRegistryName()
		) );
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu( int containerId, @Nonnull Inventory inventory, @Nonnull Player player ) {
		
		return new SelectablePaintingMenu( containerId, stack );
	}
}
