package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;


//package-private
class RandomCheckBoxButton extends CheckboxButton {
	
	
	private static final ResourceLocation SELECTABLE_PAINTING_GUI_TEXTURE = new ResourceLocation(
		SelectablePaintingMod.MODID,
		"textures/gui/checkbox.png"
	);
	
	private final Consumer<CheckboxButton> onPress;
	
	//package-private
	@SuppressWarnings( "SameParameterValue" )
	RandomCheckBoxButton( int _x, int _y, String _message, boolean _checked, Consumer<CheckboxButton> _onPress ) {
		
		super( _x, _y, 10, 10, _message, _checked );
		onPress = _onPress;
	}
	
	@Override
	public void onPress() {
		
		super.onPress();
		onPress.accept( this );
		
	}
	
	@Override
	public void renderButton( int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_ ) {
		
		Minecraft.getInstance().getTextureManager().bindTexture( SELECTABLE_PAINTING_GUI_TEXTURE );
		if( isChecked() ) {
			blit( x, y, 0, 10, 10, 10, 16, 32 );
		} else {
			blit( x, y, 0, 0, 10, 10, 16, 32 );
		}
		Minecraft.getInstance().fontRenderer.drawString( getMessage(), x + 14, y + 1, 4210752 );
	}
}
