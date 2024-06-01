package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import de.geheimagentnr1.selectable_painting.elements.items.ModItemsRegisterFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
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
	
	private int size_index;
	
	private int motive_index;
	
	private boolean randomVariant;
	
	@SuppressWarnings( "unused" )
	public SelectablePaintingEntity( @NotNull Level pLevel ) {
		
		this( ModItemsRegisterFactory.SELECTABLE_PAINTING_ENTITY, pLevel );
	}
	
	private SelectablePaintingEntity(
		@NotNull EntityType<SelectablePaintingEntity> entityType,
		@NotNull Level pLevel ) {
		
		super( entityType, pLevel );
		size_index = 0;
		motive_index = 0;
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
		setMotiveHolder( paintingType );
		init( _direction );
		size_index = _size_index;
		motive_index = _painting_index;
		randomVariant = _random;
	}
	
	private void init( @NotNull Direction _direction ) {
		
		setDirection( _direction );
	}
	
	@Override
	protected void defineSynchedData( SynchedEntityData.Builder pBuilder ) {
		
		pBuilder.define( DATA_VARIANT_ID, getDefaultMotive() );
	}
	
	@Override
	public void onSyncedDataUpdated( @NotNull EntityDataAccessor<?> pKey ) {
		
		if( pKey.equals( DATA_VARIANT_ID ) ) {
			recalculateBoundingBox();
		}
	}
	
	@NotNull
	private static Holder<PaintingVariant> getDefaultMotive() {
		
		return BuiltInRegistries.PAINTING_VARIANT.getHolderOrThrow( DEFAULT_VARIANT );
	}
	
	private void setMotiveHolder( @NotNull Holder<PaintingVariant> holder ) {
		
		entityData.set( DATA_VARIANT_ID, holder );
	}
	
	@NotNull
	private Holder<PaintingVariant> getVariantHolder() {
		
		return entityData.get( DATA_VARIANT_ID );
	}
	
	@NotNull
	@Override
	public ItemStack getPickedResult( @NotNull HitResult target ) {
		
		return getItemStackOfEntity();
	}
	
	@NotNull
	private ItemStack getItemStackOfEntity() {
		
		return SelectablePaintingItemStackHelper.writeDataToStack(
			new ItemStack( ModItemsRegisterFactory.SELECTABLE_PAINTING ),
			size_index,
			motive_index,
			randomVariant
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
		pCompound.putInt( "size_index", size_index );
		pCompound.putInt( "painting_index", motive_index );
		pCompound.putBoolean( "random", randomVariant );
		super.addAdditionalSaveData( pCompound );
	}
	
	@Override
	public void readAdditionalSaveData( @NotNull CompoundTag pCompound ) {
		
		setMotiveHolder(
			Painting.VARIANT_CODEC.parse( NbtOps.INSTANCE, pCompound )
				.result()
				.orElseGet( SelectablePaintingEntity::getDefaultMotive )
		);
		size_index = pCompound.getInt( "size_index" );
		motive_index = pCompound.getInt( "painting_index" );
		randomVariant = pCompound.getBoolean( "random" );
		super.readAdditionalSaveData( pCompound );
		setDirection( Direction.from2DDataValue( pCompound.getByte( "Facing" ) ) );
	}
	
	@Override
	public int getWidth() {
		
		return getVariantHolder().value().getWidth();
	}
	
	@Override
	public int getHeight() {
		
		return getVariantHolder().value().getHeight();
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
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		
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
			.build( SelectablePainting.registry_name );
	}
	
	@NotNull
	public PaintingVariant getVariant() {
		
		return getVariantHolder().value();
	}
}