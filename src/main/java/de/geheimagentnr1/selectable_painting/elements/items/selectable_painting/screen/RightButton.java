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
	public void func_230431_b_(
		@Nonnull MatrixStack matrixStack,
		int p_renderButton_1_,
		int p_renderButton_2_,
		float p_renderButton_3_ ) {
		
		Minecraft.getInstance().getTextureManager().bindTexture( DIRECTION_BUTTONS_TEXTURE );
		if( func_230449_g_() ) {
			func_238463_a_( matrixStack, field_230690_l_, field_230691_m_, 13, 2, 10, 15, 64, 64 );
		} else {
			func_238463_a_( matrixStack, field_230690_l_, field_230691_m_, 1, 2, 10, 15, 64, 64 );
		}
	}
}
