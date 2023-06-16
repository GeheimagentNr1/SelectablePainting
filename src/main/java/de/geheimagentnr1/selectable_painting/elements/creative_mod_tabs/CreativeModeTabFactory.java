package de.geheimagentnr1.selectable_painting.elements.creative_mod_tabs;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;


public interface CreativeModeTabFactory extends Supplier<CreativeModeTab> {
	
	
	default ResourceLocation getName() {
		
		return new ResourceLocation( getModId(), getRegistryName() );
	}
	
	String getModId();
	
	String getRegistryName();
	
	@Override
	default CreativeModeTab get() {
		
		return CreativeModeTab.builder()
			.title( Component.translatable( "itemGroup." + getRegistryName() ) )
			.icon( this::buildDisplayItemStack )
			.displayItems( this::displayItemsGenerator )
			.build();
	}
	
	default ItemStack buildDisplayItemStack() {
		
		return new ItemStack( getDisplayItem() );
	}
	
	Item getDisplayItem();
	
	default void displayItemsGenerator(
		CreativeModeTab.ItemDisplayParameters itemDisplayParameters,
		CreativeModeTab.Output output ) {
		
		output.acceptAll( getDisplayItems() );
	}
	
	List<ItemStack> getDisplayItems();
}
