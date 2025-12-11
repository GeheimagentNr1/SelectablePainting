package de.geheimagentnr1.selectable_painting.elements.creative_mod_tabs;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.ModItemsRegisterFactory;
import de.geheimagentnr1.selectable_painting.registry.RegistryEntry;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@RequiredArgsConstructor
public class SelectablePaintingCreativeModeTabFactory implements CreativeModeTabFactory {
	
	
	@NotNull
	private final ModItemsRegisterFactory modItemsRegisterFactory;
	
	@NotNull
	@Override
	public String getRegistryName() {
		
		return SelectablePaintingMod.MODID;
	}
	
	@NotNull
	@Override
	public ItemLike getIconItem() {
		
		return ModItemsRegisterFactory.SELECTABLE_PAINTING;
	}
	
	@NotNull
	@Override
	public List<RegistryEntry<Item>> getDisplayItems() {
		
		return modItemsRegisterFactory.getItems();
	}
}
