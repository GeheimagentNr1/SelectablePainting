package de.geheimagentnr1.selectable_painting.elements.creative_mod_tabs;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;


public interface CreativeModeTabFactory extends Consumer<CreativeModeTab.Builder> {
	
	
	default ResourceLocation getName() {
		
		return new ResourceLocation( getModId(), getRegistryName() );
	}
	
	String getModId();
	
	String getRegistryName();
	
	default void accept( CreativeModeTab.Builder builder ) {
		
		builder.title( Component.translatable( "itemGroup." + getRegistryName() ) );
		builder.icon( this::buildDisplayItemStack );
		builder.displayItems( this::displayItemsGenerator );
	}
	
	default ItemStack buildDisplayItemStack() {
		
		return new ItemStack( getDisplayItem() );
	}
	
	Item getDisplayItem();
	
	default void displayItemsGenerator( FeatureFlagSet flags, CreativeModeTab.Output output, boolean hasPermisions ) {
		
		output.acceptAll( getDisplayItems() );
	}
	
	List<ItemStack> getDisplayItems();
}
