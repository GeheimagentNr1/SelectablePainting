package de.geheimagentnr1.selectable_painting.elements.items;

import com.mojang.serialization.Codec;
import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePainting;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingEntity;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingRenderer;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingMenu;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingScreen;
import de.geheimagentnr1.selectable_painting.registry.RegistryEntry;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings( "StaticNonFinalField" )
public class ModItemsRegisterFactory {
	
	//TODO:
	// F - Funktion fertig
	// I - Item Texture fertig
	// N - Name und Registierungsname vorhanden und fertig
	// R - Rezept fertig
	// T - Tags fertig
	
	//Selectable Painting
	
	public static SelectablePainting SELECTABLE_PAINTING;
	
	public static MenuType<SelectablePaintingMenu> SELECTABLE_PAINTING_MENU;
	
	public static EntityType<SelectablePaintingEntity> SELECTABLE_PAINTING_ENTITY;
	
	
	@NotNull
	public static final DataComponentType<Integer> SIZE_INDEX = DataComponentType.<Integer> builder()
		.persistent( Codec.INT )
		.networkSynchronized( ByteBufCodecs.INT )
		.build();
	
	@NotNull
	public static final DataComponentType<Integer> PAINTING_INDEX = DataComponentType.<Integer> builder()
		.persistent( Codec.INT )
		.networkSynchronized( ByteBufCodecs.INT )
		.build();
	
	@NotNull
	public static final DataComponentType<Boolean> RANDOM = DataComponentType.<Boolean> builder()
		.persistent( Codec.BOOL )
		.networkSynchronized( ByteBufCodecs.BOOL )
		.build();
	
	@NotNull
	private final List<RegistryEntry<Item>> items = new ArrayList<>();
	
	@NotNull
	public List<RegistryEntry<Item>> getItems() {
		
		return items;
	}
	
	@SubscribeEvent
	public void handleRegistryEvent( @NotNull RegisterEvent event ) {
		
		event.register( Registries.ITEM, helper -> {
			SELECTABLE_PAINTING = new SelectablePainting();
			helper.register( ResourceLocation.fromNamespaceAndPath( SelectablePaintingMod.MODID, SelectablePainting.registry_name ), SELECTABLE_PAINTING );
			items.add( RegistryEntry.create( SelectablePainting.registry_name, SELECTABLE_PAINTING ) );
		} );
		
		event.register( Registries.MENU, helper -> {
			SELECTABLE_PAINTING_MENU = new MenuType<>( ( windowId, inv ) -> new SelectablePaintingMenu( windowId, inv ), FeatureFlags.DEFAULT_FLAGS );
			helper.register( ResourceLocation.fromNamespaceAndPath( SelectablePaintingMod.MODID, SelectablePainting.registry_name ), SELECTABLE_PAINTING_MENU );
		} );
		
		event.register( Registries.DATA_COMPONENT_TYPE, helper -> {
			helper.register( ResourceLocation.fromNamespaceAndPath( SelectablePaintingMod.MODID, "size" ), SIZE_INDEX );
			helper.register( ResourceLocation.fromNamespaceAndPath( SelectablePaintingMod.MODID, "painting" ), PAINTING_INDEX );
			helper.register( ResourceLocation.fromNamespaceAndPath( SelectablePaintingMod.MODID, "random" ), RANDOM );
		} );
		
		event.register( Registries.ENTITY_TYPE, helper -> {
			SELECTABLE_PAINTING_ENTITY = SelectablePaintingEntity.buildEntityType();
			helper.register( ResourceLocation.fromNamespaceAndPath( SelectablePaintingMod.MODID, SelectablePainting.registry_name ), SELECTABLE_PAINTING_ENTITY );
		} );
	}
	
	@SubscribeEvent
	public void handleFMLClientSetupEvent( @NotNull FMLClientSetupEvent event ) {
		
		EntityRenderers.register( SELECTABLE_PAINTING_ENTITY, SelectablePaintingRenderer::new );
	}
	
	@SubscribeEvent
	public void handleRegisterMenuScreensEvent( @NotNull RegisterMenuScreensEvent event ) {
		
		event.register( SELECTABLE_PAINTING_MENU, SelectablePaintingScreen::new );
	}
}
