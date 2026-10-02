package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


@OnlyIn( Dist.CLIENT )
public class SelectablePaintingRenderState extends EntityRenderState {


	public PaintingVariant variant;

	public Direction direction = Direction.SOUTH;

	public float yRot;
}
