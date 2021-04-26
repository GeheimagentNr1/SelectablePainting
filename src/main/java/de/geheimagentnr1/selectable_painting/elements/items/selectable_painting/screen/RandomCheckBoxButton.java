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
	public void onPress() {
		
		super.onPress();
		onPress.accept( this );
		
	}
	
	@Override
	public void renderButton( @Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks ) {
		
		Minecraft.getInstance().getTextureManager().bind( SELECTABLE_PAINTING_GUI_TEXTURE );
		if( selected() ) {
			blit( matrixStack, x, y, 0, 10, 10, 10, 16, 32 );
		} else {
			blit( matrixStack, x, y, 0, 0, 10, 10, 16, 32 );
		}
		Minecraft.getInstance().font.draw(
			matrixStack,
			getMessage().getString(),
			x + 14,
			y + 2,
			4210752
		);
	}
}
