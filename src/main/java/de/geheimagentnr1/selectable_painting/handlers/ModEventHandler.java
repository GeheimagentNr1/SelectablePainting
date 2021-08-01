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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


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
	public static void handleItemRegistryEvent( RegistryEvent.Register<Item> itemRegistryEvent ) {
		
		itemRegistryEvent.getRegistry().registerAll( ModItems.ITEMS );
	}
	
	@SubscribeEvent
	public static void handleEntityTypeRegistryEvent( RegistryEvent.Register<EntityType<?>> entityTypeRegister ) {
		
		entityTypeRegister.getRegistry().register( SelectablePaintingEntity.buildEntityType() );
	}
	
	@SubscribeEvent
	public static void handleMenuTypeRegistryEvent( RegistryEvent.Register<MenuType<?>> event ) {
		
		event.getRegistry().register(
			IForgeContainerType.create(
				( containerId, inv, data ) -> new SelectablePaintingMenu( containerId, data )
			).setRegistryName( SelectablePainting.registry_name )
		);
	}
}
