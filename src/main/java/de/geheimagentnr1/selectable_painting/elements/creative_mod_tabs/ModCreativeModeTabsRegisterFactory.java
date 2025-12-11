package de.geheimagentnr1.selectable_painting.elements.creative_mod_tabs;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.ModItemsRegisterFactory;
import de.geheimagentnr1.selectable_painting.registry.RegistryEntry;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@RequiredArgsConstructor
public class ModCreativeModeTabsRegisterFactory {
	
	
	@NotNull
	private final ModItemsRegisterFactory modItemsRegisterFactory;
	
	@SubscribeEvent
	public void handleRegistryEvent( @NotNull RegisterEvent event ) {
		
		event.register( Registries.CREATIVE_MODE_TAB, helper -> {
			factories().forEach( factory -> {
				CreativeModeTab tab = CreativeModeTab.builder()
					.title( Component.translatable( "itemGroup." + factory.getRegistryName() ) )
					.icon( () -> factory.getIconItem().asItem().getDefaultInstance() )
					.displayItems( ( parameters, output ) -> {
						factory.getDisplayItems().forEach( entry -> output.accept( entry.value() ) );
					} )
					.build();
				helper.register( ResourceLocation.fromNamespaceAndPath( SelectablePaintingMod.MODID, factory.getRegistryName() ), tab );
			} );
		} );
	}
	
	@NotNull
	protected List<CreativeModeTabFactory> factories() {
		
		return List.of(
			new SelectablePaintingCreativeModeTabFactory( modItemsRegisterFactory )
		);
	}
}
