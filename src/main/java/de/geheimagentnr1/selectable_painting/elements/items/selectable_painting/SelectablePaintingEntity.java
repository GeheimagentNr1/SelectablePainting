package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fmllegacy.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class SelectablePaintingEntity extends HangingEntity implements IEntityAdditionalSpawnData {
	
	
	private Motive motive;
	
	private int size_index;
	
	private int motive_index;
	
	private boolean randomMotive;
	
	@SuppressWarnings( "unused" )
	public SelectablePaintingEntity( Level _level ) {
		
		this( ModItems.SELECTABLE_PAINTING_ENTITY, _level );
	}
	
	private SelectablePaintingEntity( EntityType<SelectablePaintingEntity> entityType, Level _level ) {
		
		super( entityType, _level );
		size_index = 0;
		motive_index = 0;
	}
	
	public SelectablePaintingEntity(
		Level _level,
		BlockPos _pos,
		Direction _direction,
		Motive paintingType,
		int _size_index,
		int _painting_index,
		boolean _random ) {
		
		super( ModItems.SELECTABLE_PAINTING_ENTITY, _level, _pos );
		motive = paintingType;
		init( _direction );
		size_index = _size_index;
		motive_index = _painting_index;
		randomMotive = _random;
	}
	
	private void init( Direction _direction ) {
		
		setDirection( _direction );
	}
	
	@Override
	public ItemStack getPickedResult( HitResult target ) {
		
		return getItemStackOfEntity();
	}
	
	private ItemStack getItemStackOfEntity() {
		
		return SelectablePaintingItemStackHelper.writeDataToStack(
			new ItemStack( ModItems.SELECTABLE_PAINTING ),
			size_index,
			motive_index,
			randomMotive
		);
	}
	
	@Override
	public void addAdditionalSaveData( CompoundTag compound ) {
		
		compound.putString( "Motive", Registry.MOTIVE.getKey( motive ).toString() );
		compound.putByte( "Facing", (byte)direction.get2DDataValue() );
		compound.putInt( "size_index", size_index );
		compound.putInt( "painting_index", motive_index );
		compound.putBoolean( "random", randomMotive );
		super.addAdditionalSaveData( compound );
	}
	
	@Override
	public void writeSpawnData( FriendlyByteBuf buffer ) {
		
		buffer.writeVarInt( Registry.MOTIVE.getId( motive ) );
		buffer.writeVarInt( size_index );
		buffer.writeVarInt( motive_index );
		buffer.writeBoolean( randomMotive );
		buffer.writeBlockPos( pos );
		buffer.writeByte( direction.get2DDataValue() );
	}
	
	@Override
	public void readAdditionalSaveData( CompoundTag compound ) {
		
		motive = Registry.MOTIVE.get( ResourceLocation.tryParse( compound.getString( "Motive" ) ) );
		direction = Direction.from2DDataValue( compound.getByte( "Facing" ) );
		size_index = compound.getInt( "size_index" );
		motive_index = compound.getInt( "painting_index" );
		randomMotive = compound.getBoolean( "random" );
		super.readAdditionalSaveData( compound );
	}
	
	@Override
	public void readSpawnData( FriendlyByteBuf additionalData ) {
		
		motive = Registry.MOTIVE.byId( additionalData.readVarInt() );
		size_index = additionalData.readVarInt();
		motive_index = additionalData.readVarInt();
		randomMotive = additionalData.readBoolean();
		BlockPos position = additionalData.readBlockPos();
		setPos( position.getX(), position.getY(), position.getZ() );
		setDirection( Direction.from2DDataValue( additionalData.readByte() ) );
	}
	
	@Override
	public int getWidth() {
		
		return motive == null ? 1 : motive.getWidth();
	}
	
	@Override
	public int getHeight() {
		
		return motive == null ? 1 : motive.getHeight();
	}
	
	@Override
	public void dropItem( @Nullable Entity brakingEntity ) {
		
		if( level.getGameRules().getBoolean( GameRules.RULE_DOENTITYDROPS ) ) {
			playSound( SoundEvents.PAINTING_BREAK, 1.0F, 1.0F );
			if( brakingEntity instanceof Player ) {
				if( ( (Player)brakingEntity ).isCreative() ) {
					return;
				}
			}
			spawnAtLocation( getItemStackOfEntity() );
		}
	}
	
	@Override
	public void playPlacementSound() {
		
		playSound( SoundEvents.PAINTING_PLACE, 1.0F, 1.0F );
	}
	
	@Override
	public void moveTo( double x, double y, double z, float yaw, float pitch ) {
		
		setPos( x, y, z );
	}
	
	@Override
	public void lerpTo(
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
	public Packet<?> getAddEntityPacket() {
		
		//SpawnSelectablePaintingMsg.sendToChunkTrackers( level.getChunkAt( getOnPos() ), this );
		return NetworkHooks.getEntitySpawningPacket( this );
	}
	
	public static EntityType<SelectablePaintingEntity> buildEntityType() {
		
		EntityType<SelectablePaintingEntity> entityType = EntityType.Builder
			.<SelectablePaintingEntity> of( SelectablePaintingEntity::new, MobCategory.MISC )
			.sized( 0.5F, 0.5F )
			.build( SelectablePainting.registry_name );
		entityType.setRegistryName( SelectablePainting.registry_name );
		return entityType;
	}
	
	public Motive getMotive() {
		
		return motive;
	}
	
	public int getSizeIndex() {
		
		return size_index;
	}
	
	public int getPaintingIndex() {
		
		return motive_index;
	}
	
	public boolean isRandomMotive() {
		
		return randomMotive;
	}
}