package de.geheimagentnr1.selectable_painting.handlers;

import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePainting;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingEntity;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingRenderer;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingContainer;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingScreen;
import de.geheimagentnr1.selectable_painting.network.Network;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModEventHandler {
	
	
	@SubscribeEvent
	public static void handleCommonSetupEvent( FMLCommonSetupEvent event ) {
		
		Network.registerPackets();
	}
	
	@OnlyIn( Dist.CLIENT )
	@SubscribeEvent
	public static void handleClientSetupEvent( FMLClientSetupEvent event ) {
		
		Minecraft.getInstance().getRenderManager().register(
			ModItems.SELECTABLE_PAINTING_ENTITY,
			new SelectablePaintingRenderer( Minecraft.getInstance().getRenderManager() )
		);
		ScreenManager.registerFactory( ModItems.SELECTABLE_PAINTING_CONTAINER, SelectablePaintingScreen::new );
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
	public static void handleContainerTypeRegistryEvent( RegistryEvent.Register<ContainerType<?>> event ) {
		
		event.getRegistry().register(
			IForgeContainerType.create(
				( windowId, inv, data ) -> new SelectablePaintingContainer( windowId, data )
			).setRegistryName( SelectablePainting.registry_name )
		);
	}
}
