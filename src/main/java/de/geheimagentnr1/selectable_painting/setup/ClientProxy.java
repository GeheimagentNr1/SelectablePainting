package de.geheimagentnr1.selectable_painting.setup;

import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingEntity;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingRenderer;
import net.minecraft.client.Minecraft;


public class ClientProxy implements IProxy {
	
	
	@Override
	public void init() {
		
		Minecraft.getInstance().getRenderManager().register( SelectablePaintingEntity.class,
			new SelectablePaintingRenderer( Minecraft.getInstance().getRenderManager() ) );
	}
}
