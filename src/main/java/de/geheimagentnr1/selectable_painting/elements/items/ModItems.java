package de.geheimagentnr1.selectable_painting.elements.items;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePainting;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingEntity;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingMenu;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ObjectHolder;


@SuppressWarnings( { "PublicStaticArrayField", "StaticNonFinalField" } )
public class ModItems {
	
	//TODO:
	// F - Funktion fertig
	// I - Item Texture fertig
	// N - Name und Registierungsname vorhanden und fertig
	// R - Rezept fertig
	// T - Tags fertig
	
	public static final Item[] ITEMS = {
		//Selectable Painting
		new SelectablePainting(),//FINRT
	};
	
	//Selectable Painting
	
	@ObjectHolder( SelectablePaintingMod.MODID + ":" + SelectablePainting.registry_name )
	public static SelectablePainting SELECTABLE_PAINTING;
	
	@ObjectHolder( SelectablePaintingMod.MODID + ":" + SelectablePainting.registry_name )
	public static MenuType<SelectablePaintingMenu> SELECTABLE_PAINTING_MENU;
	
	@ObjectHolder( SelectablePaintingMod.MODID + ":" + SelectablePainting.registry_name )
	public static EntityType<SelectablePaintingEntity> SELECTABLE_PAINTING_ENTITY;
}
