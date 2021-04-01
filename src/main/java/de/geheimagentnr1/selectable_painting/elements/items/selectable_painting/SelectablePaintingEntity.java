package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import de.geheimagentnr1.selectable_painting.network.SpawnSelectablePaintingMsg;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class SelectablePaintingEntity extends HangingEntity {
	
	
	private PaintingType art;
	
	private int size_index;
	
	private int painting_index;
	
	private boolean random;
	
	@SuppressWarnings( "unused" )
	public SelectablePaintingEntity( World _world ) {
		
		this( ModItems.SELECTABLE_PAINTING_ENTITY, _world );
	}
	
	private SelectablePaintingEntity( EntityType<SelectablePaintingEntity> entityType, World _world ) {
		
		super( entityType, _world );
		size_index = 0;
		painting_index = 0;
	}
	
	public SelectablePaintingEntity(
		World _world,
		BlockPos pos,
		Direction direction,
		PaintingType paintingType,
		int _size_index,
		int _painting_index,
		boolean _random ) {
		
		super( ModItems.SELECTABLE_PAINTING_ENTITY, _world, pos );
		art = paintingType;
		init( direction );
		size_index = _size_index;
		painting_index = _painting_index;
		random = _random;
	}
	
	private void init( Direction direction ) {
		
		updateFacingWithBoundingBox( direction );
	}
	
	/**
	 * Called when a user uses the creative pick block button on this entity.
	 *
	 * @param target The full target the player is looking at
	 * @return A ItemStack to add to the player's inventory, empty ItemStack if nothing should be added.
	 */
	@Override
	public ItemStack getPickedResult( RayTraceResult target ) {
		
		return getItemStackOfEntity();
	}
	
	private ItemStack getItemStackOfEntity() {
		
		return SelectablePaintingItemStackHelper.writeDataToStack(
			new ItemStack( ModItems.SELECTABLE_PAINTING ),
			size_index,
			painting_index,
			random
		);
	}
	
	@SuppressWarnings( "deprecation" )
	@Override
	public void writeAdditional( CompoundNBT compound ) {
		
		compound.putString( "Motive", Registry.MOTIVE.getKey( art ).toString() );
		compound.putInt( "size_index", size_index );
		compound.putInt( "painting_index", painting_index );
		compound.putBoolean( "random", random );
		super.writeAdditional( compound );
	}
	
	@SuppressWarnings( "deprecation" )
	@Override
	public void readAdditional( CompoundNBT compound ) {
		
		art = Registry.MOTIVE.getOrDefault( ResourceLocation.tryCreate( compound.getString( "Motive" ) ) );
		size_index = compound.getInt( "size_index" );
		painting_index = compound.getInt( "painting_index" );
		random = compound.getBoolean( "random" );
		super.readAdditional( compound );
	}
	
	@Override
	public int getWidthPixels() {
		
		return art == null ? 1 : art.getWidth();
	}
	
	@Override
	public int getHeightPixels() {
		
		return art == null ? 1 : art.getHeight();
	}
	
	@Override
	public void onBroken( @Nullable Entity brokenEntity ) {
		
		if( world.getGameRules().getBoolean( GameRules.DO_ENTITY_DROPS ) ) {
			playSound( SoundEvents.ENTITY_PAINTING_BREAK, 1.0F, 1.0F );
			if( brokenEntity instanceof PlayerEntity ) {
				if( ( (PlayerEntity)brokenEntity ).isCreative() ) {
					return;
				}
			}
			entityDropItem( getItemStackOfEntity() );
		}
	}
	
	@Override
	public void playPlaceSound() {
		
		playSound( SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F );
	}
	
	@Override
	public void setLocationAndAngles( double x, double y, double z, float yaw, float pitch ) {
		
		setPosition( x, y, z );
	}
	
	@Override
	public void setPositionAndRotationDirect(
		double x,
		double y,
		double z,
		float yaw,
		float pitch,
		int posRotationIncrements,
		boolean teleport ) {
		
		/*BlockPos pos = hangingPosition.add( posX - x, posY - y, posZ - z );
		setPosition( pos.getX(), pos.getY(), pos.getZ() );*/
	}
	
	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		
		SpawnSelectablePaintingMsg.sendToChunkTrackers( world.getChunkAt( getPosition() ), this );
		return new SSpawnObjectPacket( this );
	}
	
	public static EntityType<SelectablePaintingEntity> buildEntityType() {
		
		EntityType<SelectablePaintingEntity> entityType = EntityType.Builder
			.<SelectablePaintingEntity> create( SelectablePaintingEntity::new, EntityClassification.MISC )
			.size( 0.5F, 0.5F )
			.build( SelectablePainting.registry_name );
		entityType.setRegistryName( SelectablePainting.registry_name );
		return entityType;
	}
	
	public PaintingType getArt() {
		
		return art;
	}
	
	public int getSizeIndex() {
		
		return size_index;
	}
	
	public int getPaintingIndex() {
		
		return painting_index;
	}
	
	public boolean isRandom() {
		
		return random;
	}
}