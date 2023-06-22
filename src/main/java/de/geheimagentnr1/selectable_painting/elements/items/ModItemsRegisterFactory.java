package de.geheimagentnr1.selectable_painting.elements.items;

import de.geheimagentnr1.minecraft_forge_api.elements.items.ItemsRegisterFactory;
import de.geheimagentnr1.minecraft_forge_api.registry.RegistryEntry;
import de.geheimagentnr1.minecraft_forge_api.registry.RegistryHelper;
import de.geheimagentnr1.minecraft_forge_api.registry.RegistryKeys;
import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePainting;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingEntity;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingRenderer;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingMenu;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@SuppressWarnings( "StaticNonFinalField" )
public class ModItemsRegisterFactory extends ItemsRegisterFactory {
	
	//TODO:
	// F - Funktion fertig
	// I - Item Texture fertig
	// N - Name und Registierungsname vorhanden und fertig
	// R - Rezept fertig
	// T - Tags fertig
	
	//Selectable Painting
	
	@ObjectHolder( registryName = RegistryKeys.ITEMS,
		value = SelectablePaintingMod.MODID + ":" + SelectablePainting.registry_name )
	public static SelectablePainting SELECTABLE_PAINTING;
	
	@ObjectHolder( registryName = RegistryKeys.MENU_TYPES,
		value = SelectablePaintingMod.MODID + ":" + SelectablePainting.registry_name )
	public static MenuType<SelectablePaintingMenu> SELECTABLE_PAINTING_MENU;
	
	@ObjectHolder( registryName = RegistryKeys.ENTITY_TYPES,
		value = SelectablePaintingMod.MODID + ":" + SelectablePainting.registry_name )
	public static EntityType<SelectablePaintingEntity> SELECTABLE_PAINTING_ENTITY;
	
	@SubscribeEvent
	public void handleRegistryEvent( @NotNull RegisterEvent event ) {
		
		super.handleRegistryEvent( event );
		RegistryHelper.registerElements( event, ForgeRegistries.Keys.ENTITY_TYPES, this::entityTypes );
	}
	
	@NotNull
	@Override
	protected List<RegistryEntry<Item>> items() {
		
		return List.of(//FINRT
			//Selectable Painting
			RegistryEntry.create( SelectablePainting.registry_name, new SelectablePainting() )//FINRT
		);
	}
	
	@NotNull
	@Override
	protected List<RegistryEntry<MenuType<?>>> menuTypes() {
		
		return List.of(
			RegistryEntry.create(
				SelectablePainting.registry_name,
				IForgeMenuType.create(
					( windowId, inv, data ) -> new SelectablePaintingMenu( windowId, data )
				)
			)
		);
	}
	
	@NotNull
	private List<RegistryEntry<EntityType<?>>> entityTypes() {
		
		return List.of(
			RegistryEntry.create(
				SelectablePainting.registry_name,
				SelectablePaintingEntity.buildEntityType()
			)
		);
	}
	
	@SubscribeEvent
	@Override
	public void handleFMLClientSetupEvent( @NotNull FMLClientSetupEvent event ) {
		
		EntityRenderers.register( SELECTABLE_PAINTING_ENTITY, SelectablePaintingRenderer::new );
		MenuScreens.register( SELECTABLE_PAINTING_MENU, SelectablePaintingScreen::new );
	}
}
