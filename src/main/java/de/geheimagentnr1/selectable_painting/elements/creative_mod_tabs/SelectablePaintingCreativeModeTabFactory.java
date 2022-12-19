package de.geheimagentnr1.selectable_painting.elements.creative_mod_tabs;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;


public class SelectablePaintingCreativeModeTabFactory implements CreativeModeTabFactory {
	
	
	@Override
	public String getModId() {
		
		return SelectablePaintingMod.MODID;
	}
	
	@Override
	public String getRegistryName() {
		
		return SelectablePaintingMod.MODID;
	}
	
	@Override
	public Item getDisplayItem() {
		
		return ModItems.SELECTABLE_PAINTING.asItem();
	}
	
	@Override
	public List<ItemStack> getDisplayItems() {
		
		return ModItems.ITEMS.stream()
			.map( registryEntry -> new ItemStack( registryEntry.getValue() ) )
			.toList();
	}
}
