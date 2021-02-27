package de.geheimagentnr1.selectable_painting.helpers;

import net.minecraft.entity.item.PaintingType;
import net.minecraft.util.registry.Registry;


public class RegistryHelper {
	
	
	@SuppressWarnings( { "deprecation", "SameReturnValue" } )
	public static Registry<PaintingType> getMotiveRegistry() {
		
		return Registry.MOTIVE;
	}
}
