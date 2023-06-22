package de.geheimagentnr1.selectable_painting.elements.creative_mod_tabs;

import de.geheimagentnr1.minecraft_forge_api.elements.creative_mod_tabs.CreativeModeTabFactory;
import de.geheimagentnr1.minecraft_forge_api.elements.creative_mod_tabs.CreativeModeTabRegisterFactory;
import de.geheimagentnr1.selectable_painting.elements.items.ModItemsRegisterFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@RequiredArgsConstructor
public class ModCreativeModeTabsRegisterFactory extends CreativeModeTabRegisterFactory {
	
	
	@NotNull
	private final ModItemsRegisterFactory modItemsRegisterFactory;
	
	@NotNull
	@Override
	protected List<CreativeModeTabFactory> factories() {
		
		return List.of(
			new SelectablePaintingCreativeModeTabFactory( modItemsRegisterFactory )
		);
	}
}
