package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
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
	protected void renderContents( @NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick ) {
		
		if( isHovered ) {
			guiGraphics.blit( RenderPipelines.GUI_TEXTURED, DIRECTION_BUTTONS_TEXTURE, getX(), getY(), 13f, 21f, 10, 15, 64, 64 );
		} else {
			guiGraphics.blit( RenderPipelines.GUI_TEXTURED, DIRECTION_BUTTONS_TEXTURE, getX(), getY(), 1f, 21f, 10, 15, 64, 64 );
		}
	}
}
