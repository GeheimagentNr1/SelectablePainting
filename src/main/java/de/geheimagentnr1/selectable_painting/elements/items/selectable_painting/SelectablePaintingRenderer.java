package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.PaintingTextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;


@OnlyIn( Dist.CLIENT )
public class SelectablePaintingRenderer extends EntityRenderer<SelectablePaintingEntity> {
	
	
	public SelectablePaintingRenderer( @NotNull EntityRendererProvider.Context pContext ) {
		
		super( pContext );
	}
	
	public void render(
		@NotNull SelectablePaintingEntity pEntity,
		float pEntityYaw,
		float pPartialTick,
		@NotNull PoseStack pPoseStack,
		@NotNull MultiBufferSource pBuffer,
		int pPackedLight ) {
		
		pPoseStack.pushPose();
		pPoseStack.mulPose( Axis.YP.rotationDegrees( 180.0F - pEntityYaw ) );
		PaintingVariant paintingvariant = pEntity.getVariant();
		VertexConsumer vertexconsumer =
			pBuffer.getBuffer( RenderType.entitySolid( this.getTextureLocation( pEntity ) ) );
		PaintingTextureManager paintingtexturemanager = Minecraft.getInstance().getPaintingTextures();
		this.renderPainting(
			pPoseStack,
			vertexconsumer,
			pEntity,
			paintingvariant.width(),
			paintingvariant.height(),
			paintingtexturemanager.get( paintingvariant ),
			paintingtexturemanager.getBackSprite()
		);
		pPoseStack.popPose();
		super.render( pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight );
	}
	
	@NotNull
	public ResourceLocation getTextureLocation( @NotNull SelectablePaintingEntity pEntity ) {
		
		return Minecraft.getInstance().getPaintingTextures().getBackSprite().atlasLocation();
	}
	
	private void renderPainting(
		@NotNull PoseStack pPoseStack,
		@NotNull VertexConsumer pConsumer,
		@NotNull SelectablePaintingEntity pPainting,
		int pWidth,
		int pHeight,
		@NotNull TextureAtlasSprite pPaintingSprite,
		@NotNull TextureAtlasSprite pBackSprite
	) {
		
		PoseStack.Pose pose = pPoseStack.last();
		float f = ( -pWidth ) / 2.0F;
		float f1 = ( -pHeight ) / 2.0F;
		float f3 = pBackSprite.getU0();
		float f4 = pBackSprite.getU1();
		float f5 = pBackSprite.getV0();
		float f6 = pBackSprite.getV1();
		float f7 = pBackSprite.getU0();
		float f8 = pBackSprite.getU1();
		float f9 = pBackSprite.getV0();
		float f10 = pBackSprite.getV( 0.0625F );
		float f11 = pBackSprite.getU0();
		float f12 = pBackSprite.getU( 0.0625F );
		float f13 = pBackSprite.getV0();
		float f14 = pBackSprite.getV1();
		double d0 = 1.0 / pWidth;
		double d1 = 1.0 / pHeight;
		
		for( int i = 0; i < pWidth; i++ ) {
			for( int j = 0; j < pHeight; j++ ) {
				float f15 = f + ( i + 1 );
				float f16 = f + i;
				float f17 = f1 + ( j + 1 );
				float f18 = f1 + j;
				int k = pPainting.getBlockX();
				int l = Mth.floor( pPainting.getY() + ( ( f17 + f18 ) / 2.0F ) );
				int i1 = pPainting.getBlockZ();
				Direction direction = pPainting.getDirection();
				if( direction == Direction.NORTH ) {
					k = Mth.floor( pPainting.getX() + ( ( f15 + f16 ) / 2.0F ) );
				}
				
				if( direction == Direction.WEST ) {
					i1 = Mth.floor( pPainting.getZ() - ( ( f15 + f16 ) / 2.0F ) );
				}
				
				if( direction == Direction.SOUTH ) {
					k = Mth.floor( pPainting.getX() - ( ( f15 + f16 ) / 2.0F ) );
				}
				
				if( direction == Direction.EAST ) {
					i1 = Mth.floor( pPainting.getZ() + ( ( f15 + f16 ) / 2.0F ) );
				}
				
				int j1 = LevelRenderer.getLightColor( pPainting.level(), new BlockPos( k, l, i1 ) );
				float f19 = pPaintingSprite.getU( (float)( d0 * ( pWidth - i ) ) );
				float f20 = pPaintingSprite.getU( (float)( d0 * ( pWidth - ( i + 1 ) ) ) );
				float f21 = pPaintingSprite.getV( (float)( d1 * ( pHeight - j ) ) );
				float f22 = pPaintingSprite.getV( (float)( d1 * ( pHeight - ( j + 1 ) ) ) );
				this.vertex( pose, pConsumer, f15, f18, f20, f21, -0.03125F, 0, 0, -1, j1 );
				this.vertex( pose, pConsumer, f16, f18, f19, f21, -0.03125F, 0, 0, -1, j1 );
				this.vertex( pose, pConsumer, f16, f17, f19, f22, -0.03125F, 0, 0, -1, j1 );
				this.vertex( pose, pConsumer, f15, f17, f20, f22, -0.03125F, 0, 0, -1, j1 );
				this.vertex( pose, pConsumer, f15, f17, f4, f5, 0.03125F, 0, 0, 1, j1 );
				this.vertex( pose, pConsumer, f16, f17, f3, f5, 0.03125F, 0, 0, 1, j1 );
				this.vertex( pose, pConsumer, f16, f18, f3, f6, 0.03125F, 0, 0, 1, j1 );
				this.vertex( pose, pConsumer, f15, f18, f4, f6, 0.03125F, 0, 0, 1, j1 );
				this.vertex( pose, pConsumer, f15, f17, f7, f9, -0.03125F, 0, 1, 0, j1 );
				this.vertex( pose, pConsumer, f16, f17, f8, f9, -0.03125F, 0, 1, 0, j1 );
				this.vertex( pose, pConsumer, f16, f17, f8, f10, 0.03125F, 0, 1, 0, j1 );
				this.vertex( pose, pConsumer, f15, f17, f7, f10, 0.03125F, 0, 1, 0, j1 );
				this.vertex( pose, pConsumer, f15, f18, f7, f9, 0.03125F, 0, -1, 0, j1 );
				this.vertex( pose, pConsumer, f16, f18, f8, f9, 0.03125F, 0, -1, 0, j1 );
				this.vertex( pose, pConsumer, f16, f18, f8, f10, -0.03125F, 0, -1, 0, j1 );
				this.vertex( pose, pConsumer, f15, f18, f7, f10, -0.03125F, 0, -1, 0, j1 );
				this.vertex( pose, pConsumer, f15, f17, f12, f13, 0.03125F, -1, 0, 0, j1 );
				this.vertex( pose, pConsumer, f15, f18, f12, f14, 0.03125F, -1, 0, 0, j1 );
				this.vertex( pose, pConsumer, f15, f18, f11, f14, -0.03125F, -1, 0, 0, j1 );
				this.vertex( pose, pConsumer, f15, f17, f11, f13, -0.03125F, -1, 0, 0, j1 );
				this.vertex( pose, pConsumer, f16, f17, f12, f13, -0.03125F, 1, 0, 0, j1 );
				this.vertex( pose, pConsumer, f16, f18, f12, f14, -0.03125F, 1, 0, 0, j1 );
				this.vertex( pose, pConsumer, f16, f18, f11, f14, 0.03125F, 1, 0, 0, j1 );
				this.vertex( pose, pConsumer, f16, f17, f11, f13, 0.03125F, 1, 0, 0, j1 );
			}
		}
	}
	
	private void vertex(
		@NotNull PoseStack.Pose pPose,
		@NotNull VertexConsumer pConsumer,
		float pX,
		float pY,
		float pU,
		float pV,
		float pZ,
		int pNormalX,
		int pNormalY,
		int pNormalZ,
		int pPackedLight ) {
		
		pConsumer.addVertex( pPose, pX, pY, pZ )
			.setColor( -1 )
			.setUv( pU, pV )
			.setOverlay( OverlayTexture.NO_OVERLAY )
			.setLight( pPackedLight )
			.setNormal( pPose, pNormalX, pNormalY, pNormalZ );
	}
}