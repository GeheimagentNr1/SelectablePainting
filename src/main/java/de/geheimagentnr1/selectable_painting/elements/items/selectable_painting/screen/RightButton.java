package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;


//package-private
class RightButton extends Button {
	
	
	private static final ResourceLocation DIRECTION_BUTTONS_TEXTURE = new ResourceLocation(
		SelectablePaintingMod.MODID,
		"textures/gui/direction_buttons.png"
	);
	
	//package-private
	RightButton( int _x, int _y, IPressable _onPress ) {
		
		super( _x, _y, 10, 15, new StringTextComponent( "" ), _onPress );
	}
	
	@Override
	public void renderButton( @Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks ) {
		
		Minecraft.getInstance().getTextureManager().bindTexture( DIRECTION_BUTTONS_TEXTURE );
		if( isHovered() ) {
			blit( matrixStack, x, y, 13, 2, 10, 15, 64, 64 );
		} else {
			blit( matrixStack, x, y, 1, 2, 10, 15, 64, 64 );
		}
	}
}
