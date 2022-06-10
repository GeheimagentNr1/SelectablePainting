package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.renderer.GameRenderer;
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
	public void renderButton( @Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks ) {
		
		RenderSystem.setShader( GameRenderer::getPositionTexShader );
		RenderSystem.setShaderTexture( 0, SELECTABLE_PAINTING_GUI_TEXTURE );
		if( selected() ) {
			blit( poseStack, x, y, 0, 10, 10, 10, 16, 32 );
		} else {
			blit( poseStack, x, y, 0, 0, 10, 10, 16, 32 );
		}
		Minecraft.getInstance().font.draw( poseStack, getMessage().getString(), x + 14, y + 2, 4210752 );
	}
}
