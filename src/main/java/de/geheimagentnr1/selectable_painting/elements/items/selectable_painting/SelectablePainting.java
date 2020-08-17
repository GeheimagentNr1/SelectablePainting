package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import de.geheimagentnr1.selectable_painting.elements.items.SelectablePaintingBaseItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class SelectablePainting extends SelectablePaintingBaseItem {
	
	
	public static final String registry_name = "selectable_painting";
	
	public SelectablePainting() {
		
		super( new Item.Properties(), registry_name );
		PaintingSelectionHelper.init();
	}
	
	@Override
	public void addInformation( @Nonnull ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
		@Nonnull ITooltipFlag flagIn ) {
		
		tooltip.add( new TranslationTextComponent( Util.makeTranslationKey( "message",
			new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_size" ) ) ).appendText( ": " )
			.appendText( PaintingSelectionHelper.getSizeName( stack ) ) );
		tooltip.add( new TranslationTextComponent( Util.makeTranslationKey( "message",
			new ResourceLocation( SelectablePaintingMod.MODID, "selectable_painting_painting" ) ) ).appendText( ": " )
			.appendSibling( PaintingSelectionHelper.getPaintingName( stack ) ) );
	}
	
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick( @Nonnull World worldIn, PlayerEntity playerIn,
		@Nonnull Hand handIn ) {
		
		ItemStack stack = playerIn.getHeldItem( handIn );
		TextComponent message;
		
		if( playerIn.isSneaking() ) {
			message = new StringTextComponent( PaintingSelectionHelper.nextSize( stack ) );
		} else {
			message = PaintingSelectionHelper.nextPainting( stack );
		}
		if( worldIn.isRemote ) {
			playerIn.sendMessage( message );
		}
		return new ActionResult<>( ActionResultType.SUCCESS, stack );
	}
	
	@Nonnull
	@Override
	public ActionResultType onItemUse( ItemUseContext context ) {
		
		Direction direction = context.getFace();
		BlockPos pos = context.getPos().offset( direction );
		PlayerEntity player = context.getPlayer();
		ItemStack stack = context.getItem();
		World world = context.getWorld();
		
		if( direction.getAxis().isVertical() || player != null && !player.canPlayerEdit( pos, direction, stack ) ) {
			return ActionResultType.FAIL;
		} else {
			SelectablePaintingEntity selectablePaintingEntity = new SelectablePaintingEntity( world, pos,
				direction, PaintingSelectionHelper.getPaintingType( stack ),
				SelectablePaintingItemStackHelper.getSizeIndex( stack ),
				SelectablePaintingItemStackHelper.getPaintingIndex( stack ) );
			
			EntityType.applyItemNBT( world, player, selectablePaintingEntity, stack.getTag() );
			if( selectablePaintingEntity.onValidSurface() ) {
				if( !world.isRemote ) {
					selectablePaintingEntity.playPlaceSound();
					world.addEntity( selectablePaintingEntity );
				}
				stack.shrink( 1 );
			} else {
				if( !world.isRemote && player != null ) {
					player.sendMessage( new TranslationTextComponent( Util.makeTranslationKey( "message",
						new ResourceLocation( SelectablePaintingMod.MODID,
							"selectable_painting_painting_to_big_error" ) ) ) );
				}
			}
			return ActionResultType.SUCCESS;
		}
	}
}
