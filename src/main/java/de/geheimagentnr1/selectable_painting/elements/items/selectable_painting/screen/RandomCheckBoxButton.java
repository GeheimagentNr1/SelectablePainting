package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.Consumer;


//package-private
class RandomCheckBoxButton extends Checkbox {
	
	
	private static final ResourceLocation SELECTABLE_PAINTING_GUI_TEXTURE =
		new ResourceLocation( SelectablePaintingMod.MODID, "textures/gui/checkbox.png" );
	
	private final Consumer<Checkbox> onPress;
	
	//package-private
	@SuppressWarnings( "SameParameterValue" )
	RandomCheckBoxButton( int _x, int _y, String _message, boolean _checked, Consumer<Checkbox> _onPress ) {
		
		super( _x, _y, 10, 10, Component.literal( _message ), _checked );
		onPress = _onPress;
	}
	
	@Override
	public void onPress() {
		
		super.onPress();
		onPress.accept( this );
		
	}
	
	@Override
	public void renderWidget( @Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partial ) {
		
		if( selected() ) {
			guiGraphics.blit( SELECTABLE_PAINTING_GUI_TEXTURE, getX(), getY(), 0, 10, 10, 10, 16, 32 );
		} else {
			guiGraphics.blit( SELECTABLE_PAINTING_GUI_TEXTURE, getX(), getY(), 0, 0, 10, 10, 16, 32 );
		}
		guiGraphics.drawString(
			Minecraft.getInstance().font,
			getMessage().getString(),
			getX() + 14,
			getY() + 2,
			4210752,
			false
		);
	}
}
