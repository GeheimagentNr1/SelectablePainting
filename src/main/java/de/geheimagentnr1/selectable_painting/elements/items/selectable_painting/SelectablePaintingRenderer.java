package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
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

import javax.annotation.Nonnull;


@OnlyIn( Dist.CLIENT )
public class SelectablePaintingRenderer extends EntityRenderer<SelectablePaintingEntity> {
	
	
	public SelectablePaintingRenderer( EntityRendererProvider.Context context ) {
		
		super( context );
	}
	
	@Override
	public void render(
		SelectablePaintingEntity entity,
		float yaw,
		float partialTicks,
		PoseStack poseStack,
		MultiBufferSource buffer,
		int packedLight ) {
		
		poseStack.pushPose();
		poseStack.mulPose( Vector3f.YP.rotationDegrees( 180.0F - yaw ) );
		PaintingVariant motive = entity.getMotive();
		poseStack.scale( 0.0625F, 0.0625F, 0.0625F );
		VertexConsumer vertexconsumer = buffer.getBuffer( RenderType.entitySolid( getTextureLocation( entity ) ) );
		PaintingTextureManager paintingtexturemanager = Minecraft.getInstance().getPaintingTextures();
		renderPainting(
			poseStack,
			vertexconsumer,
			entity,
			motive.getWidth(),
			motive.getHeight(),
			paintingtexturemanager.get( motive ),
			paintingtexturemanager.getBackSprite()
		);
		poseStack.popPose();
		super.render( entity, yaw, partialTicks, poseStack, buffer, packedLight );
	}
	
	/**
	 * Returns the location of an entity's texture.
	 */
	@Nonnull
	@Override
	public ResourceLocation getTextureLocation( @Nonnull SelectablePaintingEntity entity ) {
		
		return Minecraft.getInstance().getPaintingTextures().getBackSprite().atlas().location();
	}
	
	private void renderPainting(
		PoseStack poseStack,
		VertexConsumer vertexConsumer,
		SelectablePaintingEntity entity,
		int width,
		int height,
		TextureAtlasSprite paintingAtlas,
		TextureAtlasSprite backSpriteAtlas ) {
		
		PoseStack.Pose pose = poseStack.last();
		Matrix4f matrix4f = pose.pose();
		Matrix3f matrix3f = pose.normal();
		float f = ( -width ) / 2.0F;
		float f1 = ( -height ) / 2.0F;
		float f3 = backSpriteAtlas.getU0();
		float f4 = backSpriteAtlas.getU1();
		float f5 = backSpriteAtlas.getV0();
		float f6 = backSpriteAtlas.getV1();
		float f7 = backSpriteAtlas.getU0();
		float f8 = backSpriteAtlas.getU1();
		float f9 = backSpriteAtlas.getV0();
		float f10 = backSpriteAtlas.getV( 1.0D );
		float f11 = backSpriteAtlas.getU0();
		float f12 = backSpriteAtlas.getU( 1.0D );
		float f13 = backSpriteAtlas.getV0();
		float f14 = backSpriteAtlas.getV1();
		int i = width / 16;
		int j = height / 16;
		double d0 = 16.0D / i;
		double d1 = 16.0D / j;
		
		for( int k = 0; k < i; ++k ) {
			for( int l = 0; l < j; ++l ) {
				float f15 = f + ( ( k + 1 ) << 4 );
				float f16 = f + ( k << 4 );
				float f17 = f1 + ( ( l + 1 ) << 4 );
				float f18 = f1 + ( l << 4 );
				int i1 = entity.getBlockX();
				int j1 = Mth.floor( entity.getY() + ( ( f17 + f18 ) / 2.0F / 16.0F ) );
				int k1 = entity.getBlockZ();
				Direction direction = entity.getDirection();
				if( direction == Direction.NORTH ) {
					i1 = Mth.floor( entity.getX() + ( ( f15 + f16 ) / 2.0F / 16.0F ) );
				}
				
				if( direction == Direction.WEST ) {
					k1 = Mth.floor( entity.getZ() - ( ( f15 + f16 ) / 2.0F / 16.0F ) );
				}
				
				if( direction == Direction.SOUTH ) {
					i1 = Mth.floor( entity.getX() - ( ( f15 + f16 ) / 2.0F / 16.0F ) );
				}
				
				if( direction == Direction.EAST ) {
					k1 = Mth.floor( entity.getZ() + ( ( f15 + f16 ) / 2.0F / 16.0F ) );
				}
				
				int l1 = LevelRenderer.getLightColor( entity.level, new BlockPos( i1, j1, k1 ) );
				float f19 = paintingAtlas.getU( d0 * ( i - k ) );
				float f20 = paintingAtlas.getU( d0 * ( i - ( k + 1 ) ) );
				float f21 = paintingAtlas.getV( d1 * ( j - l ) );
				float f22 = paintingAtlas.getV( d1 * ( j - ( l + 1 ) ) );
				vertex( matrix4f, matrix3f, vertexConsumer, f15, f18, f20, f21, -0.5F, 0, 0, -1, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f16, f18, f19, f21, -0.5F, 0, 0, -1, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f16, f17, f19, f22, -0.5F, 0, 0, -1, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f15, f17, f20, f22, -0.5F, 0, 0, -1, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f15, f17, f3, f5, 0.5F, 0, 0, 1, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f16, f17, f4, f5, 0.5F, 0, 0, 1, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f16, f18, f4, f6, 0.5F, 0, 0, 1, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f15, f18, f3, f6, 0.5F, 0, 0, 1, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f15, f17, f7, f9, -0.5F, 0, 1, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f16, f17, f8, f9, -0.5F, 0, 1, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f16, f17, f8, f10, 0.5F, 0, 1, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f15, f17, f7, f10, 0.5F, 0, 1, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f15, f18, f7, f9, 0.5F, 0, -1, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f16, f18, f8, f9, 0.5F, 0, -1, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f16, f18, f8, f10, -0.5F, 0, -1, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f15, f18, f7, f10, -0.5F, 0, -1, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f15, f17, f12, f13, 0.5F, -1, 0, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f15, f18, f12, f14, 0.5F, -1, 0, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f15, f18, f11, f14, -0.5F, -1, 0, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f15, f17, f11, f13, -0.5F, -1, 0, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f16, f17, f12, f13, -0.5F, 1, 0, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f16, f18, f12, f14, -0.5F, 1, 0, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f16, f18, f11, f14, 0.5F, 1, 0, 0, l1 );
				vertex( matrix4f, matrix3f, vertexConsumer, f16, f17, f11, f13, 0.5F, 1, 0, 0, l1 );
			}
		}
		
	}
	
	private void vertex(
		Matrix4f matrix1,
		Matrix3f matrix2,
		VertexConsumer vertexConsumer,
		float x1,
		float y1,
		float u,
		float v,
		float z1,
		int x2,
		int y2,
		int z2,
		int lightmapUV ) {
		
		vertexConsumer.vertex( matrix1, x1, y1, z1 )
			.color( 255, 255, 255, 255 )
			.uv( u, v )
			.overlayCoords( OverlayTexture.NO_OVERLAY )
			.uv2( lightmapUV )
			.normal( matrix2, x2, y2, z2 )
			.endVertex();
	}
}