package de.geheimagentnr1.selectable_painting.setup;

import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.SelectablePaintingRenderer;
import net.minecraft.client.Minecraft;


public class ClientProxy implements IProxy {
	
	
	@Override
	public void init() {
		
		Minecraft.getInstance().getRenderManager().register( ModItems.SELECTABLE_PAINTING_ENTITY,
			new SelectablePaintingRenderer( Minecraft.getInstance().getRenderManager() ) );
	}
}
