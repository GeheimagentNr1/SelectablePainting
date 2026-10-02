package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.PaintingRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;


//Based on the vanilla PaintingRenderer of Minecraft 1.21.11.
@OnlyIn( Dist.CLIENT )
public class SelectablePaintingRenderer extends EntityRenderer<SelectablePaintingEntity, PaintingRenderState> {


	@NotNull
	private static final Identifier BACK_SPRITE_LOCATION = Identifier.withDefaultNamespace( "back" );

	@NotNull
	private final TextureAtlas paintingsAtlas;

	public SelectablePaintingRenderer( @NotNull EntityRendererProvider.Context pContext ) {

		super( pContext );
		paintingsAtlas = pContext.getAtlas( AtlasIds.PAINTINGS );
	}

	@Override
	public void submit(
		@NotNull PaintingRenderState pState,
		@NotNull PoseStack pPoseStack,
		@NotNull SubmitNodeCollector pNodeCollector,
		@NotNull CameraRenderState pCameraState ) {

		PaintingVariant paintingVariant = pState.variant;
		if( paintingVariant != null ) {
			pPoseStack.pushPose();
			pPoseStack.mulPose( Axis.YP.rotationDegrees( 180 - pState.direction.get2DDataValue() * 90 ) );
			TextureAtlasSprite paintingSprite = paintingsAtlas.getSprite( paintingVariant.assetId() );
			TextureAtlasSprite backSprite = paintingsAtlas.getSprite( BACK_SPRITE_LOCATION );
			renderPainting(
				pPoseStack,
				pNodeCollector,
				RenderTypes.entitySolidZOffsetForward( backSprite.atlasLocation() ),
				pState.lightCoordsPerBlock,
				paintingVariant.width(),
				paintingVariant.height(),
				paintingSprite,
				backSprite
			);
			pPoseStack.popPose();
			super.submit( pState, pPoseStack, pNodeCollector, pCameraState );
		}
	}

	@NotNull
	@Override
	public PaintingRenderState createRenderState() {

		return new PaintingRenderState();
	}

	@Override
	public void extractRenderState(
		@NotNull SelectablePaintingEntity pEntity,
		@NotNull PaintingRenderState pState,
		float pPartialTick ) {

		super.extractRenderState( pEntity, pState, pPartialTick );
		Direction direction = pEntity.getDirection();
		PaintingVariant paintingVariant = pEntity.getVariant();
		pState.direction = direction;
		pState.variant = paintingVariant;
		int width = paintingVariant.width();
		int height = paintingVariant.height();
		if( pState.lightCoordsPerBlock.length != width * height ) {
			pState.lightCoordsPerBlock = new int[width * height];
		}

		float startX = -width / 2.0F;
		float startY = -height / 2.0F;
		Level level = pEntity.level();

		for( int y = 0; y < height; y++ ) {
			for( int x = 0; x < width; x++ ) {
				float offsetX = x + startX + 0.5F;
				float offsetY = y + startY + 0.5F;
				int blockX = pEntity.getBlockX();
				int blockY = Mth.floor( pEntity.getY() + offsetY );
				int blockZ = pEntity.getBlockZ();
				switch( direction ) {
					case NORTH -> blockX = Mth.floor( pEntity.getX() + offsetX );
					case WEST -> blockZ = Mth.floor( pEntity.getZ() - offsetX );
					case SOUTH -> blockX = Mth.floor( pEntity.getX() - offsetX );
					case EAST -> blockZ = Mth.floor( pEntity.getZ() + offsetX );
					default -> {
					}
				}
				pState.lightCoordsPerBlock[x + y * width] = LevelRenderer.getLightColor(
					level,
					new BlockPos( blockX, blockY, blockZ )
				);
			}
		}
	}

	private void renderPainting(
		@NotNull PoseStack pPoseStack,
		@NotNull SubmitNodeCollector pNodeCollector,
		@NotNull RenderType pRenderType,
		int[] pLightCoords,
		int pWidth,
		int pHeight,
		@NotNull TextureAtlasSprite pPaintingSprite,
		@NotNull TextureAtlasSprite pBackSprite ) {

		pNodeCollector.submitCustomGeometry( pPoseStack, pRenderType, ( pose, consumer ) -> {
			float f = -pWidth / 2.0F;
			float f1 = -pHeight / 2.0F;
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
					int k = pLightCoords[i + j * pWidth];
					float f19 = pPaintingSprite.getU( (float)( d0 * ( pWidth - i ) ) );
					float f20 = pPaintingSprite.getU( (float)( d0 * ( pWidth - ( i + 1 ) ) ) );
					float f21 = pPaintingSprite.getV( (float)( d1 * ( pHeight - j ) ) );
					float f22 = pPaintingSprite.getV( (float)( d1 * ( pHeight - ( j + 1 ) ) ) );
					vertex( pose, consumer, f15, f18, f20, f21, -0.03125F, 0, 0, -1, k );
					vertex( pose, consumer, f16, f18, f19, f21, -0.03125F, 0, 0, -1, k );
					vertex( pose, consumer, f16, f17, f19, f22, -0.03125F, 0, 0, -1, k );
					vertex( pose, consumer, f15, f17, f20, f22, -0.03125F, 0, 0, -1, k );
					vertex( pose, consumer, f15, f17, f4, f5, 0.03125F, 0, 0, 1, k );
					vertex( pose, consumer, f16, f17, f3, f5, 0.03125F, 0, 0, 1, k );
					vertex( pose, consumer, f16, f18, f3, f6, 0.03125F, 0, 0, 1, k );
					vertex( pose, consumer, f15, f18, f4, f6, 0.03125F, 0, 0, 1, k );
					vertex( pose, consumer, f15, f17, f7, f9, -0.03125F, 0, 1, 0, k );
					vertex( pose, consumer, f16, f17, f8, f9, -0.03125F, 0, 1, 0, k );
					vertex( pose, consumer, f16, f17, f8, f10, 0.03125F, 0, 1, 0, k );
					vertex( pose, consumer, f15, f17, f7, f10, 0.03125F, 0, 1, 0, k );
					vertex( pose, consumer, f15, f18, f7, f9, 0.03125F, 0, -1, 0, k );
					vertex( pose, consumer, f16, f18, f8, f9, 0.03125F, 0, -1, 0, k );
					vertex( pose, consumer, f16, f18, f8, f10, -0.03125F, 0, -1, 0, k );
					vertex( pose, consumer, f15, f18, f7, f10, -0.03125F, 0, -1, 0, k );
					vertex( pose, consumer, f15, f17, f12, f13, 0.03125F, -1, 0, 0, k );
					vertex( pose, consumer, f15, f18, f12, f14, 0.03125F, -1, 0, 0, k );
					vertex( pose, consumer, f15, f18, f11, f14, -0.03125F, -1, 0, 0, k );
					vertex( pose, consumer, f15, f17, f11, f13, -0.03125F, -1, 0, 0, k );
					vertex( pose, consumer, f16, f17, f12, f13, -0.03125F, 1, 0, 0, k );
					vertex( pose, consumer, f16, f18, f12, f14, -0.03125F, 1, 0, 0, k );
					vertex( pose, consumer, f16, f18, f11, f14, 0.03125F, 1, 0, 0, k );
					vertex( pose, consumer, f16, f17, f11, f13, 0.03125F, 1, 0, 0, k );
				}
			}
		} );
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
