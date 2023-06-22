package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.elements.items.ModItemsRegisterFactory;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;


public class SelectablePaintingNamedContainerProvider implements MenuProvider {
	
	
	@NotNull
	private final ItemStack stack;
	
	public SelectablePaintingNamedContainerProvider( @NotNull ItemStack _stack ) {
		
		stack = _stack;
	}
	
	@NotNull
	@Override
	public Component getDisplayName() {
		
		return Component.translatable( Util.makeDescriptionId(
			"container",
			BuiltInRegistries.ITEM.getKey( ModItemsRegisterFactory.SELECTABLE_PAINTING )
		) );
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu( int containerId, @NotNull Inventory inventory, @NotNull Player player ) {
		
		return new SelectablePaintingMenu( containerId, stack );
	}
}
