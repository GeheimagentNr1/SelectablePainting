package de.geheimagentnr1.selectable_painting.elements.item_groups;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;


//package-private
class SelectablePaintingItemGroup extends CreativeModeTab {
	
	
	//package-private
	SelectablePaintingItemGroup() {
		
		super( SelectablePaintingMod.MODID );
	}
	
	@Nonnull
	@Override
	public ItemStack makeIcon() {
		
		return new ItemStack( ModItems.SELECTABLE_PAINTING );
	}
}
