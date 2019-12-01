package de.geheimagentnr1.selectable_painting.elements.items;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.item.Item;


public abstract class SelectablePaintingBaseItem extends BaseItem {
	
	
	@SuppressWarnings( "SameParameterValue" )
	protected SelectablePaintingBaseItem( Item.Properties item_properties, String registry_name ) {
		
		super( item_properties.group( SelectablePaintingMod.setup.selectablePaintingItemGroup ), registry_name );
	}
}
