package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;


//package-private
class LeftButton extends Button {
	
	
	@NotNull
	private static final Identifier DIRECTION_BUTTONS_TEXTURE = Identifier.fromNamespaceAndPath(
		SelectablePaintingMod.MODID,
		"textures/gui/direction_buttons.png"
	);
	
	//package-private
	LeftButton( int _x, int _y, @NotNull OnPress _onPress ) {
		
		super( _x, _y, 10, 15, Component.literal( "" ), _onPress, Supplier::get );
	}
	
	@Override
	protected void extractContents( @NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick ) {
		
		if( isHovered ) {
			graphics.blit( DIRECTION_BUTTONS_TEXTURE, getX(), getY(), getX() + 10, getY() + 15, 13f / 64f, 23f / 64f, 21f / 64f, 36f / 64f );
		} else {
			graphics.blit( DIRECTION_BUTTONS_TEXTURE, getX(), getY(), getX() + 10, getY() + 15, 1f / 64f, 11f / 64f, 21f / 64f, 36f / 64f );
		}
	}
}
