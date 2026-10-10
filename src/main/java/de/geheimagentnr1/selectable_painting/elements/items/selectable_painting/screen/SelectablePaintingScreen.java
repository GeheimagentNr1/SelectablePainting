package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.util.Util;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class SelectablePaintingScreen extends AbstractContainerScreen<SelectablePaintingMenu> {
	
	
	@NotNull
	private static final Identifier SELECTABLE_PAINTING_GUI_TEXTURE =
		Identifier.fromNamespaceAndPath( SelectablePaintingMod.MODID, "textures/gui/select_painting_gui.png" );
	
	private static final int PAINTING_TEXT_MIN_X = 19;
	
	private static final int PAINTING_TEXT_MAX_X = 154;
	
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
		addRenderableWidget(
			Checkbox.builder(
					Component.translatable( Util.makeDescriptionId(
						"message",
						Identifier.fromNamespaceAndPath(
							SelectablePaintingMod.MODID,
							"selectable_painting_random_painting"
						)
					) ),
					font
				)
				.pos( leftPos + 6, topPos + 51 )
				.selected( menu.getRandom() )
				.onValueChange( ( checkbox, value ) -> menu.toggleRandom() )
				.build()
		);
	}
	
	@Override
	public void extractBackground( @NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick ) {
		
		super.extractBackground( graphics, mouseX, mouseY, partialTick );
		//Overload without RenderPipeline: the RenderPipeline class moved to com.mojang.renderpearl in 26.3
		graphics.blit(
			SELECTABLE_PAINTING_GUI_TEXTURE,
			leftPos,
			topPos,
			leftPos + imageWidth,
			topPos + imageHeight,
			0.0F,
			imageWidth / 256.0F,
			0.0F,
			imageHeight / 256.0F
		);
	}
	
	@Override
	public void extractContents( @NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick ) {
		
		super.extractContents( graphics, mouseX, mouseY, partialTick );
		extractPaintingText( graphics );
	}
	
	@Override
	protected void extractLabels( @NotNull GuiGraphicsExtractor graphics, int x, int y ) {
		
		int titleStartX = width / 2 - leftPos - font.width( title.getString() ) / 2;
		graphics.text( font, title.getString(), titleStartX, 5, 0xFF404040, false );
		graphics.text(
			font,
			menu.getSizeText(),
			width / 2 - leftPos - font.width( menu.getSizeText() ) / 2,
			19,
			0xFFFFFFFF,
			false
		);
		
		if( !menu.getRandom() ) {
			Objects.requireNonNull( minecraft );
			PaintingVariant paintingType = menu.getCurrentMotive();
			TextureAtlasSprite paintingTextureAtlasSprite = minecraft.getAtlasManager()
				.getAtlasOrThrow( AtlasIds.PAINTINGS )
				.getSprite( paintingType.assetId() );
			int paintingWidth = paintingType.width() << 4;
			int paintingHeight = paintingType.height() << 4;
			int paintingX = width / 2 - leftPos - paintingWidth / 2;
			graphics.blit(
				paintingTextureAtlasSprite.atlasLocation(),
				paintingX,
				70,
				paintingX + paintingWidth,
				70 + paintingHeight,
				paintingTextureAtlasSprite.getU0(),
				paintingTextureAtlasSprite.getU1(),
				paintingTextureAtlasSprite.getV0(),
				paintingTextureAtlasSprite.getV1()
			);
		}
	}
	
	//Long painting names (e.g. "Ziel erfolgreich bombardiert") scroll back and forth between the buttons like the
	//labels of vanilla buttons. Drawn in screen coordinates and not in the labels, because enableScissor ignores the
	//translation of the labels in some versions (1.21.2 - 1.21.3) and uses it in others (1.21.4+).
	private void extractPaintingText( @NotNull GuiGraphicsExtractor graphics ) {
		
		String paintingText = menu.getPaintingText();
		int minX = leftPos + PAINTING_TEXT_MIN_X;
		int maxX = leftPos + PAINTING_TEXT_MAX_X;
		int y = topPos + 37;
		int textWidth = font.width( paintingText );
		int overflow = textWidth - ( maxX - minX );
		if( overflow <= 0 ) {
			graphics.text( font, paintingText, width / 2 - textWidth / 2, y, 0xFFFFFFFF, false );
			return;
		}
		double seconds = System.currentTimeMillis() / 1000.0;
		double period = Math.max( overflow * 0.5, 3.0 );
		double progress = Math.sin( Math.PI / 2 * Math.cos( Math.PI * 2 * seconds / period ) ) / 2.0 + 0.5;
		graphics.enableScissor( minX, y - 1, maxX, y + font.lineHeight );
		graphics.text( font, paintingText, minX - (int)Math.round( progress * overflow ), y, 0xFFFFFFFF, false );
		graphics.disableScissor();
	}
}
