package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.item_groups.ModItemGroups;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingNamedContainerProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class SelectablePainting extends Item {
	
	
	public static final String registry_name = "selectable_painting";
	
	public SelectablePainting() {
		
		super( new Item.Properties().tab( ModItemGroups.SELECTABLE_PAINTING ) );
		setRegistryName( registry_name );
		PaintingSelectionHelper.init();
	}
	
	@Override
	public void appendHoverText(
		@Nonnull ItemStack stack,
		@Nullable World worldIn,
		List<ITextComponent> tooltip,
		@Nonnull ITooltipFlag flagIn ) {
		
		tooltip.add( new TranslationTextComponent( Util.makeDescriptionId(
			"message",
			new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_size" )
		) ).append( ": " ).append( PaintingSelectionHelper.getSizeName( stack ) ) );
		tooltip.add( new TranslationTextComponent( Util.makeDescriptionId(
			"message",
			new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_painting" )
		) ).append( ": " )
			.append( SelectablePaintingItemStackHelper.getRandom( stack )
				? new TranslationTextComponent( Util.makeDescriptionId(
				"message",
				new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_random_painting" )
			) )
				: PaintingSelectionHelper.getPaintingName( stack ) ) );
	}
	
	@Nonnull
	@Override
	public ActionResult<ItemStack> use(
		@Nonnull World worldIn,
		PlayerEntity playerIn,
		@Nonnull Hand handIn ) {
		
		ItemStack stack = playerIn.getItemInHand( handIn );
		
		if( !worldIn.isClientSide ) {
			NetworkHooks.openGui(
				(ServerPlayerEntity)playerIn,
				new SelectablePaintingNamedContainerProvider( stack ),
				packetBuffer -> packetBuffer.writeItem( stack )
			);
		}
		return new ActionResult<>( ActionResultType.SUCCESS, stack );
	}
	
	@Nonnull
	@Override
	public ActionResultType useOn( ItemUseContext context ) {
		
		Direction direction = context.getClickedFace();
		BlockPos pos = context.getClickedPos().relative( direction );
		PlayerEntity player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		World world = context.getLevel();
		
		if( direction.getAxis().isVertical() || player != null && !player.mayUseItemAt( pos, direction, stack ) ) {
			return ActionResultType.FAIL;
		} else {
			SelectablePaintingEntity selectablePaintingEntity = new SelectablePaintingEntity(
				world,
				pos,
				direction,
				PaintingSelectionHelper.getPaintingType( stack, world ),
				SelectablePaintingItemStackHelper.getSizeIndex( stack ),
				SelectablePaintingItemStackHelper.getPaintingIndex( stack ),
				SelectablePaintingItemStackHelper.getRandom( stack )
			);
			
			EntityType.updateCustomEntityTag( world, player, selectablePaintingEntity, stack.getTag() );
			if( selectablePaintingEntity.survives() ) {
				if( !world.isClientSide ) {
					selectablePaintingEntity.playPlacementSound();
					world.addFreshEntity( selectablePaintingEntity );
				}
				stack.shrink( 1 );
			} else {
				if( !world.isClientSide && player != null ) {
					player.sendMessage( new TranslationTextComponent( Util.makeDescriptionId(
						"message",
						new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_painting_to_big_error" )
					) ), Util.NIL_UUID );
				}
			}
			return ActionResultType.SUCCESS;
		}
	}
}
