package de.geheimagentnr1.selectable_painting.elements.item_groups;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;


//package-private
class SelectablePaintingItemGroup extends ItemGroup {
	
	
	//package-private
	SelectablePaintingItemGroup() {
		
		super( SelectablePaintingMod.MODID );
	}
	
	@Nonnull
	@Override
	public ItemStack createIcon() {
		
		return new ItemStack( ModItems.SELECTABLE_PAINTING );
	}
}
