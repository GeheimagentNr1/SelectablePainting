package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.selectable_painting.screen.SelectablePaintingNamedContainerProvider;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class SelectablePainting extends Item {
	
	
	@NotNull
	public static final String registry_name = "selectable_painting";
	
	public SelectablePainting() {
		
		super( new Item.Properties() );
		PaintingSelectionHelper.init();
	}
	
	@Override
	public void appendHoverText(
		@NotNull ItemStack pStack,
		@Nullable TooltipContext pContext,
		@NotNull List<Component> pTooltipComponents,
		@NotNull TooltipFlag pTooltipFlag ) {
		
		pTooltipComponents.add( Component.translatable( Util.makeDescriptionId(
			"message",
			new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_size" )
		) ).append( ": " ).append( PaintingSelectionHelper.getSizeName( pStack ) ) );
		pTooltipComponents.add( Component.translatable( Util.makeDescriptionId(
				"message",
				new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_painting" )
			) ).append( ": " )
			.append( SelectablePaintingItemStackHelper.getRandom( pStack )
				? Component.translatable( Util.makeDescriptionId(
				"message",
				new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_random_painting" )
			) )
				: PaintingSelectionHelper.getPaintingName( pStack ) ) );
	}
	
	@NotNull
	@Override
	public InteractionResultHolder<ItemStack> use(
		@NotNull Level pLevel,
		@NotNull Player pPlayer,
		@NotNull InteractionHand pUsedHand ) {
		
		ItemStack stack = pPlayer.getItemInHand( pUsedHand );
		
		if( !pLevel.isClientSide() ) {
			if( pPlayer instanceof ServerPlayer serverPlayer ) {
				serverPlayer.openMenu(
					new SelectablePaintingNamedContainerProvider( stack ),
					packetBuffer -> {
						packetBuffer.writeJsonWithCodec( ItemStack.CODEC, stack );
					}
				);
			}
		}
		return new InteractionResultHolder<>( InteractionResult.SUCCESS, stack );
	}
	
	@NotNull
	@Override
	public InteractionResult useOn( @NotNull UseOnContext pContext ) {
		
		Direction direction = pContext.getClickedFace();
		BlockPos pos = pContext.getClickedPos().relative( direction );
		Player player = pContext.getPlayer();
		ItemStack stack = pContext.getItemInHand();
		Level level = pContext.getLevel();
		
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
			
			CustomData customdata = stack.getOrDefault( DataComponents.ENTITY_DATA, CustomData.EMPTY );
			EntityType.updateCustomEntityTag( level, player, selectablePaintingEntity, customdata );
			
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
