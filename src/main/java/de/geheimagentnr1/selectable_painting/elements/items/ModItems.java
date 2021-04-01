package de.geheimagentnr1.selectable_painting.elements.items;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePainting;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingEntity;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingContainer;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
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
	public static ContainerType<SelectablePaintingContainer> SELECTABLE_PAINTING_CONTAINER;
	
	@ObjectHolder( SelectablePaintingMod.MODID + ":" + SelectablePainting.registry_name )
	public static EntityType<SelectablePaintingEntity> SELECTABLE_PAINTING_ENTITY;
}
