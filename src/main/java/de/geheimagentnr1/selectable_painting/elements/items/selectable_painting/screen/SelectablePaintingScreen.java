package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.Objects;


public class SelectablePaintingScreen extends ContainerScreen<SelectablePaintingContainer> {
	
	
	private static final ResourceLocation SELECTABLE_PAINTING_GUI_TEXTURE = new ResourceLocation(
		SelectablePaintingMod.MODID,
		"textures/gui/select_painting_gui.png"
	);
	
	public SelectablePaintingScreen(
		@Nonnull SelectablePaintingContainer screenContainer,
		@Nonnull PlayerInventory inv,
		@Nonnull ITextComponent titleIn ) {
		
		super( screenContainer, inv, titleIn );
	}
	
	
	@Override
	public void init( @Nonnull Minecraft _minecraft, int _width, int _height ) {
		
		super.init( _minecraft, _width, _height );
		addButton( new LeftButton( leftPos + 6, topPos + 15, button -> menu.previousSize() ) );
		addButton( new RightButton( leftPos + 160, topPos + 15, button -> menu.nextSize() ) );
		addButton( new LeftButton( leftPos + 6, topPos + 33, button -> menu.previousPainting() ) );
		addButton( new RightButton( leftPos + 160, topPos + 33, button -> menu.nextPainting() ) );
		addButton( new RandomCheckBoxButton(
			leftPos + 6,
			topPos + 51,
			new TranslationTextComponent( Util.makeDescriptionId(
				"message",
				new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_random_painting" )
			) ).getString(),
			menu.getRandom(),
			checkboxButton -> menu.toggleRandom()
		) );
	}
	
	@Override
	public void render( @Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks ) {
		
		renderBackground( matrixStack );
		super.render( matrixStack, mouseX, mouseY, partialTicks );
	}
	
	@Override
	protected void renderBg( @Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY ) {
		
		Objects.requireNonNull( minecraft );
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		minecraft.getTextureManager().bind( SELECTABLE_PAINTING_GUI_TEXTURE );
		int i = leftPos;
		int j = ( height - imageHeight ) / 2;
		blit( matrixStack, i, j, 0, 0, imageWidth, imageHeight );
	}
	
	@Override
	protected void renderLabels( @Nonnull MatrixStack matrixStack, int mouseX, int mouseY ) {
		
		int titleStartX =
			width / 2 - leftPos - font.width( title.getString() ) / 2;
		font.draw(
			matrixStack,
			title.getString(),
			titleStartX,
			5,
			4210752
		);
		drawCenteredString(
			matrixStack,
			font,
			menu.getSizeText(),
			width / 2 - leftPos,
			19,
			16777215
		);
		drawCenteredString(
			matrixStack,
			font,
			menu.getPaintingText(),
			width / 2 - leftPos,
			37,
			16777215
		);
		
		if( !menu.getRandom() ) {
			Objects.requireNonNull( minecraft );
			PaintingType paintingType = menu.getCurrentPaintingType();
			TextureAtlasSprite paintingTextureAtlasSprite = minecraft.getPaintingTextures()
				.get( paintingType );
			minecraft.getTextureManager().bind(
				paintingTextureAtlasSprite.atlas().location()
			);
			blit(
				matrixStack,
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
