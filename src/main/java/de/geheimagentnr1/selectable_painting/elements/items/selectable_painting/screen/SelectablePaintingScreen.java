package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Util;
import net.minecraft.client.gui.GuiGraphics;
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
	public void render( @NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick ) {
		
		super.render( guiGraphics, mouseX, mouseY, partialTick );
	}
	
	@Override
	protected void renderBg( @NotNull GuiGraphics guiGraphics, float partialTick, int x, int y ) {
		
		guiGraphics.blit( RenderPipelines.GUI_TEXTURED, SELECTABLE_PAINTING_GUI_TEXTURE, leftPos, topPos, 0f, 0f, imageWidth, imageHeight, 256, 256 );
	}
	
	@Override
	protected void renderLabels( @NotNull GuiGraphics guiGraphics, int x, int y ) {
		
		int titleStartX = width / 2 - leftPos - font.width( title.getString() ) / 2;
		guiGraphics.drawString( font, title.getString(), titleStartX, 5, 0xFF404040, false );
		guiGraphics.drawString(
			font,
			menu.getSizeText(),
			width / 2 - leftPos - font.width( menu.getSizeText() ) / 2,
			19,
			0xFFFFFFFF,
			false
		);
		guiGraphics.drawString(
			font,
			menu.getPaintingText(),
			width / 2 - leftPos - font.width( menu.getPaintingText() ) / 2,
			37,
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
			guiGraphics.blitSprite(
				RenderPipelines.GUI_TEXTURED,
				paintingTextureAtlasSprite,
				width / 2 - leftPos - paintingWidth / 2,
				70,
				paintingWidth,
				paintingHeight
			);
		}
	}
}
