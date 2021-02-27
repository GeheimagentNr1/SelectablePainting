package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.PaintingSpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;


public class SelectablePaintingRenderer extends EntityRenderer<SelectablePaintingEntity> {
	
	
	public SelectablePaintingRenderer( EntityRendererManager renderManager ) {
		
		super( renderManager );
	}
	
	@Override
	public void doRender(
		@Nonnull SelectablePaintingEntity entity,
		double x,
		double y,
		double z,
		float entityYaw,
		float partialTicks ) {
		
		GlStateManager.pushMatrix();
		GlStateManager.translated( x, y, z );
		GlStateManager.rotatef( 180.0F - entityYaw, 0.0F, 1.0F, 0.0F );
		GlStateManager.enableRescaleNormal();
		bindEntityTexture( entity );
		PaintingType paintingtype = entity.art;
		GlStateManager.scalef( 0.0625F, 0.0625F, 0.0625F );
		if( renderOutlines ) {
			GlStateManager.enableColorMaterial();
			GlStateManager.setupSolidRenderingTextureCombine( getTeamColor( entity ) );
		}
		PaintingSpriteUploader paintingspriteuploader = Minecraft.getInstance().getPaintingSpriteUploader();
		renderPainting(
			entity,
			paintingtype.getWidth(),
			paintingtype.getHeight(),
			paintingspriteuploader.getSpriteForPainting( paintingtype ),
			paintingspriteuploader.func_215286_b()
		);
		if( renderOutlines ) {
			GlStateManager.tearDownSolidRenderingTextureCombine();
			GlStateManager.disableColorMaterial();
		}
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender( entity, x, y, z, entityYaw, partialTicks );
	}
	
	@Override
	protected ResourceLocation getEntityTexture( @Nonnull SelectablePaintingEntity entity ) {
		
		return AtlasTexture.LOCATION_PAINTINGS_TEXTURE;
	}
	
	private void renderPainting(
		SelectablePaintingEntity entity,
		int width,
		int height,
		TextureAtlasSprite paintingSprite,
		TextureAtlasSprite backSprite ) {
		
		float width_start = -width / 2.0F;
		float height_start = -height / 2.0F;
		float minU = backSprite.getMinU();
		float maxU = backSprite.getMaxU();
		float minV = backSprite.getMinV();
		float maxV = backSprite.getMaxV();
		float minU1 = backSprite.getMinU();
		float maxU1 = backSprite.getMaxU();
		float minV1 = backSprite.getMinV();
		float interpolatedV = backSprite.getInterpolatedV( 1.0D );
		float minU2 = backSprite.getMinU();
		float interpolatedU = backSprite.getInterpolatedU( 1.0D );
		float minV2 = backSprite.getMinV();
		float maxV1 = backSprite.getMaxV();
		int block_width = width / 16;
		int block_height = height / 16;
		double width_double = 16.0D / block_width;
		double height_double = 16.0D / block_height;
		
		for( int i = 0; i < block_width; ++i ) {
			for( int j = 0; j < block_height; ++j ) {
				float width_from = width_start + ( i + 1 << 4 );
				float width_to = width_start + ( i << 4 );
				float height_from = height_start + ( j + 1 << 4 );
				float height_to = height_start + ( j << 4 );
				setLightmap( entity, ( width_from + width_to ) / 2.0F, ( height_from + height_to ) / 2.0F );
				float interpolatedU1 = paintingSprite.getInterpolatedU( width_double * ( block_width - i ) );
				float interpolatedU2 = paintingSprite.getInterpolatedU( width_double * ( block_width - ( i + 1 ) ) );
				float interpolatedV1 = paintingSprite.getInterpolatedV( height_double * ( block_height - j ) );
				float interpolatedV2 = paintingSprite.getInterpolatedV( height_double * ( block_height - ( j + 1 ) ) );
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferbuilder = tessellator.getBuffer();
				bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_TEX_NORMAL );
				bufferbuilder.pos( width_from, height_to, -0.5D )
					.tex( interpolatedU2, interpolatedV1 )
					.normal( 0.0F, 0.0F, -1.0F )
					.endVertex();
				bufferbuilder.pos( width_to, height_to, -0.5D )
					.tex( interpolatedU1, interpolatedV1 )
					.normal( 0.0F, 0.0F, -1.0F )
					.endVertex();
				bufferbuilder.pos( width_to, height_from, -0.5D )
					.tex( interpolatedU1, interpolatedV2 )
					.normal( 0.0F, 0.0F, -1.0F )
					.endVertex();
				bufferbuilder.pos( width_from, height_from, -0.5D )
					.tex( interpolatedU2, interpolatedV2 )
					.normal( 0.0F, 0.0F, -1.0F )
					.endVertex();
				bufferbuilder.pos( width_from, height_from, 0.5D )
					.tex( minU, minV )
					.normal( 0.0F, 0.0F, 1.0F )
					.endVertex();
				bufferbuilder.pos( width_to, height_from, 0.5D )
					.tex( maxU, minV )
					.normal( 0.0F, 0.0F, 1.0F )
					.endVertex();
				bufferbuilder.pos( width_to, height_to, 0.5D )
					.tex( maxU, maxV )
					.normal( 0.0F, 0.0F, 1.0F )
					.endVertex();
				bufferbuilder.pos( width_from, height_to, 0.5D )
					.tex( minU, maxV )
					.normal( 0.0F, 0.0F, 1.0F )
					.endVertex();
				bufferbuilder.pos( width_from, height_from, -0.5D )
					.tex( minU1, minV1 )
					.normal( 0.0F, 1.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_to, height_from, -0.5D )
					.tex( maxU1, minV1 )
					.normal( 0.0F, 1.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_to, height_from, 0.5D )
					.tex( maxU1, interpolatedV )
					.normal( 0.0F, 1.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_from, height_from, 0.5D )
					.tex( minU1, interpolatedV )
					.normal( 0.0F, 1.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_from, height_to, 0.5D )
					.tex( minU1, minV1 )
					.normal( 0.0F, -1.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_to, height_to, 0.5D )
					.tex( maxU1, minV1 )
					.normal( 0.0F, -1.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_to, height_to, -0.5D )
					.tex( maxU1, interpolatedV )
					.normal( 0.0F, -1.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_from, height_to, -0.5D )
					.tex( minU1, interpolatedV )
					.normal( 0.0F, -1.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_from, height_from, 0.5D )
					.tex( interpolatedU, minV2 )
					.normal( -1.0F, 0.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_from, height_to, 0.5D )
					.tex( interpolatedU, maxV1 )
					.normal( -1.0F, 0.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_from, height_to, -0.5D )
					.tex( minU2, maxV1 )
					.normal( -1.0F, 0.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_from, height_from, -0.5D )
					.tex( minU2, minV2 )
					.normal( -1.0F, 0.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_to, height_from, -0.5D )
					.tex( interpolatedU, minV2 )
					.normal( 1.0F, 0.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_to, height_to, -0.5D )
					.tex( interpolatedU, maxV1 )
					.normal( 1.0F, 0.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_to, height_to, 0.5D )
					.tex( minU2, maxV1 )
					.normal( 1.0F, 0.0F, 0.0F )
					.endVertex();
				bufferbuilder.pos( width_to, height_from, 0.5D )
					.tex( minU2, minV2 )
					.normal( 1.0F, 0.0F, 0.0F )
					.endVertex();
				tessellator.draw();
			}
		}
		
	}
	
	private void setLightmap( SelectablePaintingEntity painting, float width, float height ) {
		
		int x = MathHelper.floor( painting.posX );
		int y = MathHelper.floor( painting.posY + height / 16.0F );
		int z = MathHelper.floor( painting.posZ );
		Direction direction = painting.getHorizontalFacing();
		if( direction == Direction.NORTH ) {
			x = MathHelper.floor( painting.posX + width / 16.0F );
		}
		
		if( direction == Direction.WEST ) {
			z = MathHelper.floor( painting.posZ - width / 16.0F );
		}
		
		if( direction == Direction.SOUTH ) {
			x = MathHelper.floor( painting.posX - width / 16.0F );
		}
		
		if( direction == Direction.EAST ) {
			z = MathHelper.floor( painting.posZ + width / 16.0F );
		}
		
		int light_level = renderManager.world.getCombinedLight( new BlockPos( x, y, z ), 0 );
		int level_rest = light_level % 65536;
		int level = light_level / 65536;
		GLX.glMultiTexCoord2f( GLX.GL_TEXTURE1, level_rest, level );
		GlStateManager.color3f( 1.0F, 1.0F, 1.0F );
	}
}
