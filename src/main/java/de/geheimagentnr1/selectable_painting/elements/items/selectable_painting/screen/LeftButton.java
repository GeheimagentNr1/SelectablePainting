package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;


//package-private
class LeftButton extends Button {
	
	
	private static final ResourceLocation DIRECTION_BUTTONS_TEXTURE = new ResourceLocation(
		SelectablePaintingMod.MODID,
		"textures/gui/direction_buttons.png"
	);
	
	//package-private
	LeftButton( int _x, int _y, OnPress _onPress ) {
		
		super( _x, _y, 10, 15, Component.literal( "" ), _onPress );
	}
	
	@Override
	public void renderButton( @Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks ) {
		
		RenderSystem.setShader( GameRenderer::getPositionTexShader );
		RenderSystem.setShaderTexture( 0, DIRECTION_BUTTONS_TEXTURE );
		if( isHovered ) {
			blit( poseStack, x, y, 13, 21, 10, 15, 64, 64 );
		} else {
			blit( poseStack, x, y, 1, 21, 10, 15, 64, 64 );
		}
	}
}
