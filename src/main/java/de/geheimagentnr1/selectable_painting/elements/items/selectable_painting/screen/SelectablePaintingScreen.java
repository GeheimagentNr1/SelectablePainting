package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;
import java.util.Objects;


public class SelectablePaintingScreen extends AbstractContainerScreen<SelectablePaintingMenu> {
	
	
	private static final ResourceLocation SELECTABLE_PAINTING_GUI_TEXTURE =
		new ResourceLocation( SelectablePaintingMod.MODID, "textures/gui/select_painting_gui.png" );
	
	public SelectablePaintingScreen(
		@Nonnull SelectablePaintingMenu screenContainer,
		@Nonnull Inventory inventory,
		@Nonnull Component _title ) {
		
		super( screenContainer, inventory, _title );
	}
	
	@Override
	protected void init() {
		
		super.init();
		addRenderableWidget( new LeftButton( leftPos + 6, topPos + 15, button -> menu.previousSize() ) );
		addRenderableWidget( new RightButton( leftPos + 160, topPos + 15, button -> menu.nextSize() ) );
		addRenderableWidget( new LeftButton( leftPos + 6, topPos + 33, button -> menu.previousPainting() ) );
		addRenderableWidget( new RightButton( leftPos + 160, topPos + 33, button -> menu.nextPainting() ) );
		addRenderableWidget( new RandomCheckBoxButton(
			leftPos + 6,
			topPos + 51,
			Component.translatable( Util.makeDescriptionId(
				"message",
				new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_random_painting" )
			) ).getString(),
			menu.getRandom(),
			checkboxButton -> menu.toggleRandom()
		) );
	}
	
	@Override
	public void render( @Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks ) {
		
		renderBackground( poseStack );
		super.render( poseStack, mouseX, mouseY, partialTicks );
	}
	
	@Override
	protected void renderBg( @Nonnull PoseStack poseStack, float partialTicks, int x, int y ) {
		
		Objects.requireNonNull( minecraft );
		RenderSystem.setShader( GameRenderer::getPositionTexShader );
		RenderSystem.setShaderColor( 1.0F, 1.0F, 1.0F, 1.0F );
		RenderSystem.setShaderTexture( 0, SELECTABLE_PAINTING_GUI_TEXTURE );
		blit( poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight );
	}
	
	@Override
	protected void renderLabels( @Nonnull PoseStack poseStack, int x, int y ) {
		
		int titleStartX = width / 2 - leftPos - font.width( title.getString() ) / 2;
		font.draw( poseStack, title.getString(), titleStartX, 5, 4210752 );
		drawCenteredString( poseStack, font, menu.getSizeText(), width / 2 - leftPos, 19, 16777215 );
		drawCenteredString( poseStack, font, menu.getPaintingText(), width / 2 - leftPos, 37, 16777215 );
		
		if( !menu.getRandom() ) {
			Objects.requireNonNull( minecraft );
			PaintingVariant paintingType = menu.getCurrentMotive();
			TextureAtlasSprite paintingTextureAtlasSprite = minecraft.getPaintingTextures().get( paintingType );
			RenderSystem.setShaderTexture( 0, paintingTextureAtlasSprite.atlasLocation() );
			blit(
				poseStack,
				width / 2 - leftPos - paintingType.getWidth() / 2,
				70,
				0,
				paintingType.getWidth(),
				paintingType.getHeight(),
				paintingTextureAtlasSprite
			);
		}
	}
}
