package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class SelectablePaintingScreen extends AbstractContainerScreen<SelectablePaintingMenu> {
	
	
	@NotNull
	private static final ResourceLocation SELECTABLE_PAINTING_GUI_TEXTURE =
		new ResourceLocation( SelectablePaintingMod.MODID, "textures/gui/select_painting_gui.png" );
	
	public SelectablePaintingScreen(
		@NotNull SelectablePaintingMenu screenContainer,
		@NotNull Inventory inventory,
		@NotNull Component _title ) {
		
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
	public void render( @NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick ) {
		
		renderBackground( guiGraphics, mouseX, mouseY, partialTick );
		super.render( guiGraphics, mouseX, mouseY, partialTick );
	}
	
	@Override
	protected void renderBg( @NotNull GuiGraphics guiGraphics, float partialTick, int x, int y ) {
		
		guiGraphics.blit( SELECTABLE_PAINTING_GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight );
	}
	
	@Override
	protected void renderLabels( @NotNull GuiGraphics guiGraphics, int x, int y ) {
		
		int titleStartX = width / 2 - leftPos - font.width( title.getString() ) / 2;
		guiGraphics.drawString( font, title.getString(), titleStartX, 5, 4210752, false );
		guiGraphics.drawString(
			font,
			menu.getSizeText(),
			width / 2 - leftPos - font.width( menu.getSizeText() ) / 2,
			19,
			16777215,
			false
		);
		guiGraphics.drawString(
			font,
			menu.getPaintingText(),
			width / 2 - leftPos - font.width( menu.getPaintingText() ) / 2,
			37,
			16777215,
			false
		);
		
		if( !menu.getRandom() ) {
			Objects.requireNonNull( minecraft );
			PaintingVariant paintingType = menu.getCurrentMotive();
			TextureAtlasSprite paintingTextureAtlasSprite = minecraft.getPaintingTextures().get( paintingType );
			RenderSystem.setShaderTexture( 0, paintingTextureAtlasSprite.atlasLocation() );
			guiGraphics.blit(
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
