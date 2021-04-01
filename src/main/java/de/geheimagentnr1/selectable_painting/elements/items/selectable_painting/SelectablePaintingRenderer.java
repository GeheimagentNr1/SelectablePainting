package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.PaintingSpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;


@OnlyIn( Dist.CLIENT )
public class SelectablePaintingRenderer extends EntityRenderer<SelectablePaintingEntity> {
	
	
	public SelectablePaintingRenderer( EntityRendererManager renderManager ) {
		
		super( renderManager );
	}
	
	@Override
	public void render(
		SelectablePaintingEntity entityIn, float entityYaw, float partialTicks,
		MatrixStack matrixStackIn,
		IRenderTypeBuffer bufferIn, int packedLightIn ) {
		
		matrixStackIn.pushPose();
		matrixStackIn.mulPose( Vector3f.YP.rotationDegrees( 180.0F - entityYaw ) );
		PaintingType paintingtype = entityIn.getArt();
		matrixStackIn.scale( 0.0625F, 0.0625F, 0.0625F );
		IVertexBuilder ivertexbuilder =
			bufferIn.getBuffer( RenderType.entitySolid( getTextureLocation( entityIn ) ) );
		PaintingSpriteUploader paintingspriteuploader = Minecraft.getInstance().getPaintingTextures();
		render( matrixStackIn, ivertexbuilder, entityIn,
			paintingtype.getWidth(),
			paintingtype.getHeight(),
			paintingspriteuploader.get( paintingtype ),
			paintingspriteuploader.getBackSprite()
		);
		matrixStackIn.popPose();
		super.render( entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn );
	}
	
	/**
	 * Returns the location of an entity's texture.
	 */
	@Nonnull
	@Override
	public ResourceLocation getTextureLocation( @Nonnull SelectablePaintingEntity entity ) {
		
		return Minecraft.getInstance().getPaintingTextures().getBackSprite().atlas().location();
	}
	
	private void render(
		MatrixStack matrixStack, IVertexBuilder vertexBuilder, SelectablePaintingEntity entity,
		int weidth, int height, TextureAtlasSprite paintingAtlas, TextureAtlasSprite backSpriteAtlas ) {
		
		MatrixStack.Entry matrixstack_entry = matrixStack.last();
		Matrix4f matrix1 = matrixstack_entry.pose();
		Matrix3f matrix2 = matrixstack_entry.normal();
		float negativHalfWeidth = -weidth / 2.0F;
		float negativHalfHeight = -height / 2.0F;
		float minU1 = backSpriteAtlas.getU0();
		float maxU1 = backSpriteAtlas.getU1();
		float minV1 = backSpriteAtlas.getV0();
		float maxV1 = backSpriteAtlas.getV1();
		float minU2 = backSpriteAtlas.getU0();
		float maxU2 = backSpriteAtlas.getU1();
		float minV2 = backSpriteAtlas.getV0();
		float interpoladedV = backSpriteAtlas.getV( 1.0D );
		float minU3 = backSpriteAtlas.getU0();
		float interpolatedU = backSpriteAtlas.getU( 1.0D );
		float minV3 = backSpriteAtlas.getV0();
		float maxV2 = backSpriteAtlas.getV1();
		int i = weidth / 16;
		int j = height / 16;
		double d0 = 16.0D / i;
		double d1 = 16.0D / j;
		
		for( int k = 0; k < i; ++k ) {
			for( int l = 0; l < j; ++l ) {
				float f15 = negativHalfWeidth + ( k + 1 << 4 );
				float f16 = negativHalfWeidth + ( k << 4 );
				float f17 = negativHalfHeight + ( l + 1 << 4 );
				float f18 = negativHalfHeight + ( l << 4 );
				int i1 = MathHelper.floor( entity.getX() );
				int j1 = MathHelper.floor( entity.getY() + ( f17 + f18 ) / 2.0F / 16.0F );
				int k1 = MathHelper.floor( entity.getZ() );
				Direction direction = entity.getDirection();
				if( direction == Direction.NORTH ) {
					i1 = MathHelper.floor( entity.getX() + ( f15 + f16 ) / 2.0F / 16.0F );
				}
				if( direction == Direction.WEST ) {
					k1 = MathHelper.floor( entity.getZ() - ( f15 + f16 ) / 2.0F / 16.0F );
				}
				if( direction == Direction.SOUTH ) {
					i1 = MathHelper.floor( entity.getX() - ( f15 + f16 ) / 2.0F / 16.0F );
				}
				if( direction == Direction.EAST ) {
					k1 = MathHelper.floor( entity.getZ() + ( f15 + f16 ) / 2.0F / 16.0F );
				}
				int l1 = WorldRenderer.getLightColor( entity.level, new BlockPos( i1, j1, k1 ) );
				float f19 = paintingAtlas.getU( d0 * ( i - k ) );
				float f20 = paintingAtlas.getU( d0 * ( i - ( k + 1 ) ) );
				float f21 = paintingAtlas.getV( d1 * ( j - l ) );
				float f22 = paintingAtlas.getV( d1 * ( j - ( l + 1 ) ) );
				render( matrix1, matrix2, vertexBuilder, f15, f18, f20, f21, -0.5F, 0, 0, -1, l1 );
				render( matrix1, matrix2, vertexBuilder, f16, f18, f19, f21, -0.5F, 0, 0, -1, l1 );
				render( matrix1, matrix2, vertexBuilder, f16, f17, f19, f22, -0.5F, 0, 0, -1, l1 );
				render( matrix1, matrix2, vertexBuilder, f15, f17, f20, f22, -0.5F, 0, 0, -1, l1 );
				render( matrix1, matrix2, vertexBuilder, f15, f17, minU1, minV1, 0.5F, 0, 0, 1, l1 );
				render( matrix1, matrix2, vertexBuilder, f16, f17, maxU1, minV1, 0.5F, 0, 0, 1, l1 );
				render( matrix1, matrix2, vertexBuilder, f16, f18, maxU1, maxV1, 0.5F, 0, 0, 1, l1 );
				render( matrix1, matrix2, vertexBuilder, f15, f18, minU1, maxV1, 0.5F, 0, 0, 1, l1 );
				render( matrix1, matrix2, vertexBuilder, f15, f17, minU2, minV2, -0.5F, 0, 1, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f16, f17, maxU2, minV2, -0.5F, 0, 1, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f16, f17, maxU2, interpoladedV, 0.5F, 0, 1, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f15, f17, minU2, interpoladedV, 0.5F, 0, 1, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f15, f18, minU2, minV2, 0.5F, 0, -1, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f16, f18, maxU2, minV2, 0.5F, 0, -1, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f16, f18, maxU2, interpoladedV, -0.5F, 0, -1, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f15, f18, minU2, interpoladedV, -0.5F, 0, -1, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f15, f17, interpolatedU, minV3, 0.5F, -1, 0, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f15, f18, interpolatedU, maxV2, 0.5F, -1, 0, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f15, f18, minU3, maxV2, -0.5F, -1, 0, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f15, f17, minU3, minV3, -0.5F, -1, 0, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f16, f17, interpolatedU, minV3, -0.5F, 1, 0, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f16, f18, interpolatedU, maxV2, -0.5F, 1, 0, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f16, f18, minU3, maxV2, 0.5F, 1, 0, 0, l1 );
				render( matrix1, matrix2, vertexBuilder, f16, f17, minU3, minV3, 0.5F, 1, 0, 0, l1 );
			}
		}
		
	}
	
	private void render(
		Matrix4f matrix1, Matrix3f matrix2, IVertexBuilder vertexBuilder, float x1, float y1, float u,
		float v, float z1, int x2, int y2, int z2, int lightmapUV ) {
		
		vertexBuilder.vertex( matrix1, x1, y1, z1 ).color( 255, 255, 255, 255 ).uv( u, v )
			.overlayCoords( OverlayTexture.NO_OVERLAY ).uv2( lightmapUV ).normal( matrix2, x2, y2, z2 ).endVertex();
	}
}