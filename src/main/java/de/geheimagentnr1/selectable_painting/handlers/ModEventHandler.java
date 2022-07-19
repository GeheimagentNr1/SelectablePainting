package de.geheimagentnr1.selectable_painting.handlers;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePainting;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingEntity;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingRenderer;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingMenu;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingScreen;
import de.geheimagentnr1.selectable_painting.network.Network;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;


@Mod.EventBusSubscriber( modid = SelectablePaintingMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModEventHandler {
	
	
	@SubscribeEvent
	public static void handleCommonSetupEvent( FMLCommonSetupEvent event ) {
		
		Network.registerPackets();
	}
	
	@OnlyIn( Dist.CLIENT )
	@SubscribeEvent
	public static void handleClientSetupEvent( FMLClientSetupEvent event ) {
		
		EntityRenderers.register( ModItems.SELECTABLE_PAINTING_ENTITY, SelectablePaintingRenderer::new );
		MenuScreens.register( ModItems.SELECTABLE_PAINTING_MENU, SelectablePaintingScreen::new );
	}
	
	@SubscribeEvent
	public static void handleItemRegistryEvent( RegisterEvent event ) {
		
		if( event.getRegistryKey().equals( ForgeRegistries.Keys.ITEMS ) ) {
			event.register(
				ForgeRegistries.Keys.ITEMS,
				registerHelper -> ModItems.ITEMS.forEach( registryEntry -> registerHelper.register(
					registryEntry.getRegistryName(),
					registryEntry.getValue()
				) )
			);
		}
	}
	
	@SubscribeEvent
	public static void handleEntityTypeRegistryEvent( RegisterEvent event ) {
		
		if( event.getRegistryKey().equals( ForgeRegistries.Keys.ENTITY_TYPES ) ) {
			event.register(
				ForgeRegistries.Keys.ENTITY_TYPES,
				registerHelper -> registerHelper.register(
					SelectablePainting.registry_name,
					SelectablePaintingEntity.buildEntityType()
				)
			);
		}
	}
	
	@SubscribeEvent
	public static void handleMenuTypeRegistryEvent( RegisterEvent event ) {
		
		if( event.getRegistryKey().equals( ForgeRegistries.Keys.MENU_TYPES ) ) {
			event.register(
				ForgeRegistries.Keys.MENU_TYPES,
				registerHelper -> registerHelper.register(
					SelectablePainting.registry_name,
					IForgeMenuType.create(
						( containerId, inv, data ) -> new SelectablePaintingMenu( containerId, data )
					)
				)
			);
		}
	}
}
