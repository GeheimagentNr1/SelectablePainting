package de.geheimagentnr1.selectable_painting.elements.items;

import net.minecraft.item.Item;


//package-private
@SuppressWarnings( "AbstractClassExtendsConcreteClass" )
abstract class BaseItem extends Item {
	
	
	//package-private
	BaseItem( Item.Properties item_properties, String registry_name ) {
		
		super( item_properties );
		setRegistryName( registry_name );
	}
}
