package de.geheimagentnr1.selectable_painting.elements.items;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePainting;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;


@SuppressWarnings( { "StaticNonFinalField", "unused", "PublicField", "PublicStaticArrayField" } )
public class ModItems {
	
	//TODO:
	// F - Funktion fertig
	// I - Item Texture fertig
	// N - Name und Registierungsname vorhanden und fertig
	// R - Rezept fertig
	// T - Tags fertig
	
	public final static Item[] ITEMS = {
		//Selectable Painting
		new SelectablePainting(),//FINRT
	};
	
	//Plank
	
	@ObjectHolder( SelectablePaintingMod.MODID + ":" + SelectablePainting.registry_name )
	public static SelectablePainting SELECTABLE_PAINTING;
	
	@ObjectHolder( SelectablePaintingMod.MODID + ":" + SelectablePainting.registry_name )
	public static EntityType<SelectablePaintingEntity> SELECTABLE_PAINTING_ENTITY;
}
