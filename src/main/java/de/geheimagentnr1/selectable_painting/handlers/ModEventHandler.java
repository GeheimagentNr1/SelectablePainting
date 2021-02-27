package de.geheimagentnr1.selectable_painting.handlers;

import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingEntity;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingRenderer;
import de.geheimagentnr1.selectable_painting.network.ModNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModEventHandler {
	
	
	@SubscribeEvent
	public static void handleCommonSetupEvent( FMLCommonSetupEvent event ) {
		
		ModNetworkManager.registerPackets();
	}
	
	@OnlyIn( Dist.CLIENT )
	@SubscribeEvent
	public static void handleClientSetupEvent( FMLClientSetupEvent event ) {
		
		Minecraft.getInstance().getRenderManager().register(
			SelectablePaintingEntity.class,
			new SelectablePaintingRenderer( Minecraft.getInstance().getRenderManager() )
		);
	}
	
	@SubscribeEvent
	public static void handleRegisterItem( RegistryEvent.Register<Item> itemRegistryEvent ) {
		
		itemRegistryEvent.getRegistry().registerAll( ModItems.ITEMS );
	}
	
	@SubscribeEvent
	public static void handleRegisterEntity( RegistryEvent.Register<EntityType<?>> entityTypeRegister ) {
		
		entityTypeRegister.getRegistry().register( SelectablePaintingEntity.buildEntityType() );
	}
}
