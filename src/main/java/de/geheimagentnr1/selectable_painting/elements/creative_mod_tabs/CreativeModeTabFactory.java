package de.geheimagentnr1.selectable_painting.elements.creative_mod_tabs;

import de.geheimagentnr1.selectable_painting.registry.RegistryEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public interface CreativeModeTabFactory {
	
	
	@NotNull
	String getRegistryName();
	
	@NotNull
	ItemLike getIconItem();
	
	@NotNull
	List<RegistryEntry<Item>> getDisplayItems();
}
