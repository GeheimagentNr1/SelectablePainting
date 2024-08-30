package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import de.geheimagentnr1.selectable_painting.elements.items.ModItemsRegisterFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerEntity;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;


public class SelectablePaintingEntity extends HangingEntity {
	
	
	@NotNull
	private static final EntityDataAccessor<Holder<PaintingVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(
		SelectablePaintingEntity.class,
		EntityDataSerializers.PAINTING_VARIANT
	);
	
	@NotNull
	private static final ResourceKey<PaintingVariant> DEFAULT_VARIANT = PaintingVariants.KEBAB;
	
	
	@NotNull
	private static final EntityDataAccessor<Integer> DATA_SIZE_INDEX = SynchedEntityData.defineId(
		SelectablePaintingEntity.class,
		EntityDataSerializers.INT
	);
	
	
	@NotNull
	private static final EntityDataAccessor<Integer> DATA_MOTIVE_INDEX = SynchedEntityData.defineId(
		SelectablePaintingEntity.class,
		EntityDataSerializers.INT
	);
	
	
	@NotNull
	private static final EntityDataAccessor<Boolean> DATA_RANDOM_VARIANT = SynchedEntityData.defineId(
		SelectablePaintingEntity.class,
		EntityDataSerializers.BOOLEAN
	);
	
	@SuppressWarnings( "unused" )
	public SelectablePaintingEntity( @NotNull Level pLevel ) {
		
		this( ModItemsRegisterFactory.SELECTABLE_PAINTING_ENTITY, pLevel );
	}
	
	private SelectablePaintingEntity(
		@NotNull EntityType<SelectablePaintingEntity> entityType,
		@NotNull Level pLevel ) {
		
		super( entityType, pLevel );
	}
	
	public SelectablePaintingEntity(
		@NotNull Level pLevel,
		@NotNull BlockPos pPos,
		@NotNull Direction _direction,
		@NotNull Holder<PaintingVariant> paintingType,
		int _size_index,
		int _painting_index,
		boolean _random ) {
		
		super( ModItemsRegisterFactory.SELECTABLE_PAINTING_ENTITY, pLevel, pPos );
		init( _direction );
		setMotiveHolder( paintingType );
		setSizeIndex( _size_index );
		setMotiveIndex( _painting_index );
		setRandomVariant( _random );
	}
	
	private void init( @NotNull Direction _direction ) {
		
		setDirection( _direction );
	}
	
	@Override
	protected void defineSynchedData( SynchedEntityData.Builder pBuilder ) {
		
		pBuilder.define( DATA_VARIANT_ID, getDefaultMotive() );
		pBuilder.define( DATA_SIZE_INDEX, 0 );
		pBuilder.define( DATA_MOTIVE_INDEX, 0 );
		pBuilder.define( DATA_RANDOM_VARIANT, false );
	}
	
	@Override
	public void onSyncedDataUpdated( @NotNull EntityDataAccessor<?> pKey ) {
		
		if( pKey.equals( DATA_VARIANT_ID ) ) {
			recalculateBoundingBox();
		}
	}
	
	@NotNull
	private Holder<PaintingVariant> getDefaultMotive() {
		
		return this.registryAccess().registryOrThrow( Registries.PAINTING_VARIANT ).getAny().orElseThrow();
	}
	
	private void setMotiveHolder( @NotNull Holder<PaintingVariant> value ) {
		
		entityData.set( DATA_VARIANT_ID, value );
	}
	
	@NotNull
	private Holder<PaintingVariant> getVariantHolder() {
		
		return entityData.get( DATA_VARIANT_ID );
	}
	
	private void setSizeIndex( @NotNull Integer value ) {
		
		entityData.set( DATA_SIZE_INDEX, value );
	}
	
	@NotNull
	private Integer getSizeIndex() {
		
		return entityData.get( DATA_SIZE_INDEX );
	}
	
	private void setMotiveIndex( @NotNull Integer value ) {
		
		entityData.set( DATA_MOTIVE_INDEX, value );
	}
	
	@NotNull
	private Integer getMotiveIndex() {
		
		return entityData.get( DATA_MOTIVE_INDEX );
	}
	
	private void setRandomVariant( @NotNull Boolean value ) {
		
		entityData.set( DATA_RANDOM_VARIANT, value );
	}
	
	@NotNull
	private Boolean getRandomVariant() {
		
		return entityData.get( DATA_RANDOM_VARIANT);
	}
	
	@NotNull
	@Override
	public ItemStack getPickResult() {
		
		return getItemStackOfEntity();
	}
	
	@NotNull
	private ItemStack getItemStackOfEntity() {
		
		return SelectablePaintingItemStackHelper.writeDataToStack(
			new ItemStack( ModItemsRegisterFactory.SELECTABLE_PAINTING ),
			getSizeIndex(),
			getMotiveIndex(),
			getRandomVariant()
		);
	}
	
	@Override
	public void addAdditionalSaveData( @NotNull CompoundTag pCompound ) {
		
		Painting.VARIANT_CODEC.encodeStart( NbtOps.INSTANCE, getVariantHolder() ).ifSuccess( tag -> {
			pCompound.merge( (CompoundTag)tag );
		} );
		pCompound.putString(
			"Motive",
			getVariantHolder().unwrapKey().orElse( DEFAULT_VARIANT ).location().toString()
		);
		pCompound.putByte( "Facing", (byte)direction.get2DDataValue() );
		pCompound.putInt( "size_index", getSizeIndex() );
		pCompound.putInt( "painting_index", getMotiveIndex() );
		pCompound.putBoolean( "random", getRandomVariant() );
		super.addAdditionalSaveData( pCompound );
	}
	
	@Override
	public void readAdditionalSaveData( @NotNull CompoundTag pCompound ) {
		
		setMotiveHolder(
			Painting.VARIANT_CODEC.parse( NbtOps.INSTANCE, pCompound )
				.result()
				.orElseGet( this::getDefaultMotive )
		);
		setSizeIndex(pCompound.getInt( "size_index" ));
		setMotiveIndex( pCompound.getInt( "painting_index" ) );
		setRandomVariant( pCompound.getBoolean( "random" ) );
		super.readAdditionalSaveData( pCompound );
		setDirection( Direction.from2DDataValue( pCompound.getByte( "Facing" ) ) );
	}
	
	@Override
	protected AABB calculateBoundingBox( BlockPos blockPos, Direction pDirection ) {
		
		Vec3 vec3 = Vec3.atCenterOf( blockPos ).relative( pDirection, -0.46875 );
		PaintingVariant paintingvariant = this.getVariant();
		double d0 = this.offsetForPaintingSize( paintingvariant.width() );
		double d1 = this.offsetForPaintingSize( paintingvariant.height() );
		Direction direction = pDirection.getCounterClockWise();
		Vec3 vec31 = vec3.relative( direction, d0 ).relative( Direction.UP, d1 );
		Direction.Axis direction$axis = pDirection.getAxis();
		double d2 = direction$axis == Direction.Axis.X ? 0.0625 : paintingvariant.width();
		double d3 = paintingvariant.height();
		double d4 = direction$axis == Direction.Axis.Z ? 0.0625 : paintingvariant.width();
		return AABB.ofSize( vec31, d2, d3, d4 );
	}
	
	private double offsetForPaintingSize( int size ) {
		
		return size % 2 == 0 ? 0.5 : 0.0;
	}
	
	@Override
	public void dropItem( @Nullable Entity pBrokenEntity ) {
		
		if( level().getGameRules().getBoolean( GameRules.RULE_DOENTITYDROPS ) ) {
			playSound( SoundEvents.PAINTING_BREAK, 1.0F, 1.0F );
			if( pBrokenEntity instanceof Player ) {
				if( ( (Player)pBrokenEntity ).isCreative() ) {
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
		double pX,
		double pY,
		double pZ,
		float pYRot,
		float pXRot,
		int pSteps ) {
		
		setPos( pX, pY, pZ );
	}
	
	@NotNull
	@Override
	public Vec3 trackingPosition() {
		
		return Vec3.atLowerCornerOf( pos );
	}
	
	@NotNull
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket( ServerEntity serverEntity ) {
		
		return new ClientboundAddEntityPacket( this, this.direction.get3DDataValue(), this.getPos() );
	}
	
	public void recreateFromPacket( @NotNull ClientboundAddEntityPacket pPacket ) {
		
		super.recreateFromPacket( pPacket );
		setDirection( Direction.from3DDataValue( pPacket.getData() ) );
	}
	
	@NotNull
	public static EntityType<SelectablePaintingEntity> buildEntityType() {
		
		return EntityType.Builder
			.<SelectablePaintingEntity> of( SelectablePaintingEntity::new, MobCategory.MISC )
			.sized( 0.5F, 0.5F )
			.clientTrackingRange( 10 )
			.updateInterval( Integer.MAX_VALUE )
			.build( SelectablePainting.registry_name );
	}
	
	@NotNull
	public PaintingVariant getVariant() {
		
		return getVariantHolder().value();
	}
}