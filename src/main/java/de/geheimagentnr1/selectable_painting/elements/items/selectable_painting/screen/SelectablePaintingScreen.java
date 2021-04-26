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
	
	
	private static final ResourceLocation SELECTABLE_PAINTING_GUI_TEXTURE =
		new ResourceLocation( SelectablePaintingMod.MODID, "textures/gui/select_painting_gui.png" );
	
	public SelectablePaintingScreen(
		@Nonnull SelectablePaintingContainer screenContainer,
		@Nonnull PlayerInventory inv,
		@Nonnull ITextComponent titleIn ) {
		
		super( screenContainer, inv, titleIn );
	}
	
	
	@Override
	public void init( @Nonnull Minecraft _minecraft, int _width, int _height ) {
		
		super.init( _minecraft, _width, _height );
		addButton( new LeftButton( guiLeft + 6, guiTop + 15, button -> container.previousSize() ) );
		addButton( new RightButton( guiLeft + 160, guiTop + 15, button -> container.nextSize() ) );
		addButton( new LeftButton( guiLeft + 6, guiTop + 33, button -> container.previousPainting() ) );
		addButton( new RightButton( guiLeft + 160, guiTop + 33, button -> container.nextPainting() ) );
		addButton( new RandomCheckBoxButton(
			guiLeft + 6,
			guiTop + 51,
			new TranslationTextComponent( Util.makeTranslationKey(
				"message",
				new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_random_painting" )
			) ).getString(),
			container.getRandom(),
			checkboxButton -> container.toggleRandom()
		) );
	}
	
	@Override
	public void render( @Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks ) {
		
		renderBackground( matrixStack );
		super.render( matrixStack, mouseX, mouseY, partialTicks );
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer( @Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY ) {
		
		Objects.requireNonNull( minecraft );
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		minecraft.getTextureManager().bindTexture( SELECTABLE_PAINTING_GUI_TEXTURE );
		int i = guiLeft;
		int j = ( height - ySize ) / 2;
		blit( matrixStack, i, j, 0, 0, xSize, ySize );
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer( @Nonnull MatrixStack matrixStack, int mouseX, int mouseY ) {
		
		int titleStartX = width / 2
			- guiLeft - font.getStringWidth( title.getString() ) / 2;
		font.drawString( matrixStack, title.getString(), titleStartX, 5, 4210752 );
		drawCenteredString(
			matrixStack,
			font,
			container.getSizeText(),
			width / 2 - guiLeft,
			19,
			16777215
		);
		drawCenteredString(
			matrixStack,
			font,
			container.getPaintingText(),
			width / 2 - guiLeft,
			37,
			16777215
		);
		
		if( !container.getRandom() ) {
			Objects.requireNonNull( minecraft );
			PaintingType paintingType = container.getCurrentPaintingType();
			TextureAtlasSprite paintingTextureAtlasSprite = minecraft.getPaintingSpriteUploader()
				.getSpriteForPainting( paintingType );
			minecraft.getTextureManager()
				.bindTexture( paintingTextureAtlasSprite.getAtlasTexture().getTextureLocation() );
			blit(
				matrixStack,
				width / 2 - guiLeft - paintingType.getWidth() / 2,
				70,
				0,
				paintingType.getWidth(),
				paintingType.getHeight(),
				paintingTextureAtlasSprite
			);
		}
	}
}
