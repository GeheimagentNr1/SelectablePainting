package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.function.Consumer;


//package-private
class RandomCheckBoxButton extends CheckboxButton {
	
	
	private static final ResourceLocation SELECTABLE_PAINTING_GUI_TEXTURE =
		new ResourceLocation( SelectablePaintingMod.MODID, "textures/gui/checkbox.png" );
	
	private final Consumer<CheckboxButton> onPress;
	
	//package-private
	@SuppressWarnings( "SameParameterValue" )
	RandomCheckBoxButton( int _x, int _y, String _message, boolean _checked, Consumer<CheckboxButton> _onPress ) {
		
		super( _x, _y, 10, 10, new StringTextComponent( _message ), _checked );
		onPress = _onPress;
	}
	
	@Override
	public void func_230930_b_() {
		
		super.func_230930_b_();
		onPress.accept( this );
		
	}
	
	@Override
	public void func_230431_b_(
		@Nonnull MatrixStack matrixStack,
		int p_renderButton_1_,
		int p_renderButton_2_,
		float p_renderButton_3_ ) {
		
		Minecraft.getInstance().getTextureManager().bindTexture( SELECTABLE_PAINTING_GUI_TEXTURE );
		if( isChecked() ) {
			func_238463_a_( matrixStack, field_230690_l_, field_230691_m_, 0, 10, 10, 10, 16, 32 );
		} else {
			func_238463_a_( matrixStack, field_230690_l_, field_230691_m_, 0, 0, 10, 10, 16, 32 );
		}
		Minecraft.getInstance().fontRenderer.func_238421_b_(
			matrixStack,
			func_230458_i_().getString(),
			field_230690_l_ + 14,
			field_230691_m_ + 2,
			4210752
		);
	}
}
