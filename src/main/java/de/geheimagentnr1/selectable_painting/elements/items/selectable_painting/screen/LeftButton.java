package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;


//package-private
class LeftButton extends Button {
	
	
	@NotNull
	private static final ResourceLocation DIRECTION_BUTTONS_TEXTURE = new ResourceLocation(
		SelectablePaintingMod.MODID,
		"textures/gui/direction_buttons.png"
	);
	
	//package-private
	LeftButton( int _x, int _y, @NotNull OnPress _onPress ) {
		
		super( _x, _y, 10, 15, Component.literal( "" ), _onPress, Supplier::get );
	}
	
	@Override
	public void renderWidget( @NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick ) {
		
		if( isHovered ) {
			guiGraphics.blit( DIRECTION_BUTTONS_TEXTURE, getX(), getY(), 13, 21, 10, 15, 64, 64 );
		} else {
			guiGraphics.blit( DIRECTION_BUTTONS_TEXTURE, getX(), getY(), 1, 21, 10, 15, 64, 64 );
		}
	}
}
