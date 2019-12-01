package de.geheimagentnr1.selectable_painting.handlers;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


@SuppressWarnings( "unused" )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class RegistryEventHandler {
	
	
	@SubscribeEvent
	public static void setup( FMLCommonSetupEvent event ) {
		
		SelectablePaintingMod.proxy.init();
	}
	
	@SubscribeEvent
	public static void onItemsRegistry( RegistryEvent.Register<Item> itemRegistryEvent ) {
		
		itemRegistryEvent.getRegistry().registerAll( ModItems.ITEMS );
	}
	
	@SubscribeEvent
	public static void onEntityRegistry( RegistryEvent.Register<EntityType<?>> entityTypeRegister ) {
		
		entityTypeRegister.getRegistry().register( SelectablePaintingEntity.buildEntityType() );
	}
}
