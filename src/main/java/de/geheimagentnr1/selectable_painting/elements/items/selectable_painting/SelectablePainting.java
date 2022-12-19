package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingNamedContainerProvider;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class SelectablePainting extends Item {
	
	
	public static final String registry_name = "selectable_painting";
	
	public SelectablePainting() {
		
		super( new Item.Properties() );
		PaintingSelectionHelper.init();
	}
	
	@Override
	public void appendHoverText(
		@Nonnull ItemStack stack,
		@Nullable Level level,
		@Nonnull List<Component> tooltip,
		@Nonnull TooltipFlag flag ) {
		
		tooltip.add( Component.translatable( Util.makeDescriptionId(
			"message",
			new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_size" )
		) ).append( ": " ).append( PaintingSelectionHelper.getSizeName( stack ) ) );
		tooltip.add( Component.translatable( Util.makeDescriptionId(
				"message",
				new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_painting" )
			) ).append( ": " )
			.append( SelectablePaintingItemStackHelper.getRandom( stack )
				? Component.translatable( Util.makeDescriptionId(
				"message",
				new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_random_painting" )
			) )
				: PaintingSelectionHelper.getPaintingName( stack ) ) );
	}
	
	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(
		@Nonnull Level level,
		@Nonnull Player player,
		@Nonnull InteractionHand hand ) {
		
		ItemStack stack = player.getItemInHand( hand );
		
		if( !level.isClientSide() ) {
			NetworkHooks.openScreen(
				(ServerPlayer)player,
				new SelectablePaintingNamedContainerProvider( stack ),
				packetBuffer -> packetBuffer.writeItem( stack )
			);
		}
		return new InteractionResultHolder<>( InteractionResult.SUCCESS, stack );
	}
	
	@Nonnull
	@Override
	public InteractionResult useOn( UseOnContext context ) {
		
		Direction direction = context.getClickedFace();
		BlockPos pos = context.getClickedPos().relative( direction );
		Player player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		Level level = context.getLevel();
		
		if( direction.getAxis().isVertical() || player != null && !player.mayUseItemAt( pos, direction, stack ) ) {
			return InteractionResult.FAIL;
		} else {
			SelectablePaintingEntity selectablePaintingEntity = new SelectablePaintingEntity(
				level,
				pos,
				direction,
				PaintingSelectionHelper.getMotive( stack, level ),
				SelectablePaintingItemStackHelper.getSizeIndex( stack ),
				SelectablePaintingItemStackHelper.getPaintingIndex( stack ),
				SelectablePaintingItemStackHelper.getRandom( stack )
			);
			
			CompoundTag tag = stack.getTag();
			if( tag != null ) {
				EntityType.updateCustomEntityTag( level, player, selectablePaintingEntity, tag );
			}
			
			if( selectablePaintingEntity.survives() ) {
				if( !level.isClientSide() ) {
					selectablePaintingEntity.playPlacementSound();
					level.gameEvent( player, GameEvent.ENTITY_PLACE, pos );
					level.addFreshEntity( selectablePaintingEntity );
				}
				
				stack.shrink( 1 );
				return InteractionResult.sidedSuccess( level.isClientSide );
			} else {
				if( !level.isClientSide() && player != null ) {
					player.sendSystemMessage(
						Component.translatable( Util.makeDescriptionId(
							"message",
							new ResourceLocation(
								SelectablePaintingMod.MODID,
								"selectable_painting_painting_to_big_error"
							)
						) )
					);
				}
				return InteractionResult.CONSUME;
			}
		}
	}
}
