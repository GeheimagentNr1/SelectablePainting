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
	public void func_231158_b_( @Nonnull Minecraft _minecraft, int _width, int _height ) {
		
		super.func_231158_b_( _minecraft, _width, _height );
		func_230480_a_( new LeftButton( guiLeft + 6, guiTop + 15, button -> container.previousSize() ) );
		func_230480_a_( new RightButton( guiLeft + 160, guiTop + 15, button -> container.nextSize() ) );
		func_230480_a_( new LeftButton( guiLeft + 6, guiTop + 33, button -> container.previousPainting() ) );
		func_230480_a_( new RightButton( guiLeft + 160, guiTop + 33, button -> container.nextPainting() ) );
		func_230480_a_( new RandomCheckBoxButton(
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
	public void func_230430_a_( @Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks ) {
		
		func_230446_a_( matrixStack );
		super.func_230430_a_( matrixStack, mouseX, mouseY, partialTicks );
	}
	
	@Override
	protected void func_230450_a_( @Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY ) {
		
		Objects.requireNonNull( field_230706_i_ );
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		field_230706_i_.getTextureManager().bindTexture( SELECTABLE_PAINTING_GUI_TEXTURE );
		int i = guiLeft;
		int j = ( field_230709_l_ - ySize ) / 2;
		func_238474_b_( matrixStack, i, j, 0, 0, xSize, ySize );
	}
	
	@Override
	protected void func_230451_b_( @Nonnull MatrixStack matrixStack, int mouseX, int mouseY ) {
		
		int titleStartX = field_230708_k_ / 2
			- guiLeft - field_230712_o_.getStringWidth( field_230704_d_.getString() ) / 2;
		field_230712_o_.func_238421_b_( matrixStack, field_230704_d_.getString(), titleStartX, 5, 4210752 );
		func_238471_a_(
			matrixStack,
			field_230712_o_,
			container.getSizeText(),
			field_230708_k_ / 2 - guiLeft,
			19,
			16777215
		);
		func_238471_a_(
			matrixStack,
			field_230712_o_,
			container.getPaintingText(),
			field_230708_k_ / 2 - guiLeft,
			37,
			16777215
		);
		
		if( !container.getRandom() ) {
			Objects.requireNonNull( field_230706_i_ );
			PaintingType paintingType = container.getCurrentPaintingType();
			TextureAtlasSprite paintingTextureAtlasSprite = field_230706_i_.getPaintingSpriteUploader()
				.getSpriteForPainting( paintingType );
			field_230706_i_.getTextureManager()
				.bindTexture( paintingTextureAtlasSprite.getAtlasTexture().getTextureLocation() );
			func_238470_a_(
				matrixStack,
				field_230708_k_ / 2 - guiLeft - paintingType.getWidth() / 2,
				70,
				0,
				paintingType.getWidth(),
				paintingType.getHeight(),
				paintingTextureAtlasSprite
			);
		}
	}
}
