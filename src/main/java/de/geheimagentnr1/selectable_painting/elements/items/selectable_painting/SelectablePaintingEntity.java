package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import de.geheimagentnr1.selectable_painting.elements.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.decoration.PaintingVariants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;


public class SelectablePaintingEntity extends HangingEntity {
	
	
	private static final EntityDataAccessor<Holder<PaintingVariant>> DATA_MOTIVE_ID = SynchedEntityData.defineId(
		Painting.class,
		EntityDataSerializers.PAINTING_VARIANT
	);
	
	private static final ResourceKey<PaintingVariant> DEFAULT_VARIANT = PaintingVariants.KEBAB;
	
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
		Holder<PaintingVariant> paintingType,
		int _size_index,
		int _painting_index,
		boolean _random ) {
		
		super( ModItems.SELECTABLE_PAINTING_ENTITY, _level, _pos );
		setMotiveHolder( paintingType );
		init( _direction );
		size_index = _size_index;
		motive_index = _painting_index;
		randomMotive = _random;
	}
	
	private void init( Direction _direction ) {
		
		setDirection( _direction );
	}
	
	@Override
	protected void defineSynchedData() {
		
		entityData.define( DATA_MOTIVE_ID, getDefaultMotive() );
	}
	
	@Override
	public void onSyncedDataUpdated( EntityDataAccessor<?> data ) {
		
		if( data.equals( DATA_MOTIVE_ID ) ) {
			recalculateBoundingBox();
		}
	}
	
	private static Holder<PaintingVariant> getDefaultMotive() {
		
		return Registry.PAINTING_VARIANT.getHolderOrThrow( DEFAULT_VARIANT );
	}
	
	private void setMotiveHolder( Holder<PaintingVariant> holder ) {
		
		entityData.set( DATA_MOTIVE_ID, holder );
	}
	
	public Holder<PaintingVariant> getMotiveHolder() {
		
		return entityData.get( DATA_MOTIVE_ID );
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
		
		compound.putString( "Motive", getMotiveHolder().unwrapKey().orElse( DEFAULT_VARIANT ).location().toString() );
		compound.putByte( "Facing", (byte)direction.get2DDataValue() );
		compound.putInt( "size_index", size_index );
		compound.putInt( "painting_index", motive_index );
		compound.putBoolean( "random", randomMotive );
		super.addAdditionalSaveData( compound );
	}
	
	@Override
	public void readAdditionalSaveData( CompoundTag compound ) {
		
		setMotiveHolder( Registry.PAINTING_VARIANT.getHolder( ResourceKey.create(
			Registry.PAINTING_VARIANT_REGISTRY,
			Optional.ofNullable( ResourceLocation.tryParse( compound.getString( "Motive" ) ) )
				.orElse( DEFAULT_VARIANT.location() )
		) ).orElseGet( SelectablePaintingEntity::getDefaultMotive ) );
		size_index = compound.getInt( "size_index" );
		motive_index = compound.getInt( "painting_index" );
		randomMotive = compound.getBoolean( "random" );
		super.readAdditionalSaveData( compound );
		setDirection( Direction.from2DDataValue( compound.getByte( "Facing" ) ) );
	}
	
	@Override
	public int getWidth() {
		
		return getMotiveHolder().value().getWidth();
	}
	
	@Override
	public int getHeight() {
		
		return getMotiveHolder().value().getHeight();
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
		
		setPos( x, y, z );
	}
	
	@Nonnull
	@Override
	public Packet<?> getAddEntityPacket() {
		
		return new ClientboundAddEntityPacket( this, this.direction.get3DDataValue(), this.getPos() );
	}
	
	public void recreateFromPacket( @NotNull ClientboundAddEntityPacket packet ) {
		
		super.recreateFromPacket( packet );
		setDirection( Direction.from3DDataValue( packet.getData() ) );
	}
	
	public static EntityType<SelectablePaintingEntity> buildEntityType() {
		
		EntityType<SelectablePaintingEntity> entityType = EntityType.Builder
			.<SelectablePaintingEntity> of( SelectablePaintingEntity::new, MobCategory.MISC )
			.sized( 0.5F, 0.5F )
			.build( SelectablePainting.registry_name );
		return entityType;
	}
	
	public PaintingVariant getMotive() {
		
		return getMotiveHolder().value();
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