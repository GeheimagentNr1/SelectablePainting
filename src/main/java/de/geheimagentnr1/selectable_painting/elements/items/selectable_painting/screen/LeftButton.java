package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;


//package-private
class LeftButton extends Button {
	
	
	private static final ResourceLocation DIRECTION_BUTTONS_TEXTURE = new ResourceLocation(
		SelectablePaintingMod.MODID,
		"textures/gui/direction_buttons.png"
	);
	
	//package-private
	LeftButton( int _x, int _y, IPressable _onPress ) {
		
		super( _x, _y, 10, 15, "", _onPress );
	}
	
	@Override
	public void renderButton( int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_ ) {
		
		Minecraft.getInstance().getTextureManager().bindTexture( DIRECTION_BUTTONS_TEXTURE );
		if( isHovered() ) {
			blit( x, y, 13, 21, 10, 15, 64, 64 );
		} else {
			blit( x, y, 1, 21, 10, 15, 64, 64 );
		}
	}
}
