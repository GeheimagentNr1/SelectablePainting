package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;


public class PaintingSelectionHelper {
	
	
	private static String[] painting_sizes;
	
	private static List<List<Holder<PaintingVariant>>> motives;
	
	//package-private
	static void init() {
		
		long current_painting_count = BuiltInRegistries.PAINTING_VARIANT.size();
		if( motivesCount() != current_painting_count ) {
			TreeSet<String> sizes = new TreeSet<>();
			TreeMap<String, TreeSet<Holder<PaintingVariant>>> motivesMap = new TreeMap<>();
			
			BuiltInRegistries.PAINTING_VARIANT.registryKeySet().forEach( paintingVariantResourceKey ->
				BuiltInRegistries.PAINTING_VARIANT.getHolder( paintingVariantResourceKey ).ifPresent( motiveHolder -> {
					PaintingVariant motive = motiveHolder.value();
					int widthSize = motive.getWidth() / 16;
					int heightSize = motive.getHeight() / 16;
					@SuppressWarnings( "StringConcatenationMissingWhitespace" )
					String paintingSize = widthSize + "x" + heightSize;
					if( sizes.add( paintingSize ) ) {
						motivesMap.put(
							paintingSize,
							new TreeSet<>( Comparator.comparing(
								motiveComparing -> motiveComparing.unwrapKey().orElseThrow().location().toString()
							) )
						);
					}
					motivesMap.get( paintingSize ).add( motiveHolder );
				} )
			);
			painting_sizes = sizes.toArray( new String[0] );
			motives = motivesMap.values().stream()
				.map( holders -> holders.stream().toList() )
				.toList();
			if( painting_sizes.length != motives.size() ||
				motivesCount() != current_painting_count ) {
				throw new IllegalStateException(
					"Invalid build of motives list, please create a bug report on GitHub"
				);
			}
		}
	}
	
	private static int motivesCount() {
		
		return motives == null ? 0 : motives.stream()
			.mapToInt( List::size )
			.sum();
	}
	
	public static void previousSize( @NotNull ItemStack stack ) {
		
		init();
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack ) - 1;
		size_index = size_index < 0 ? painting_sizes.length - 1 : size_index;
		SelectablePaintingItemStackHelper.setSizeIndex( stack, size_index );
		SelectablePaintingItemStackHelper.setPaintingIndex( stack, 0 );
	}
	
	public static void nextSize( @NotNull ItemStack stack ) {
		
		init();
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack ) + 1;
		size_index = size_index >= painting_sizes.length ? 0 : size_index;
		SelectablePaintingItemStackHelper.setSizeIndex( stack, size_index );
		SelectablePaintingItemStackHelper.setPaintingIndex( stack, 0 );
	}
	
	public static void previousPainting( @NotNull ItemStack stack ) {
		
		init();
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack );
		int painting_index = SelectablePaintingItemStackHelper.getPaintingIndex( stack ) - 1;
		painting_index = painting_index < 0 ? motives.get( size_index ).size() - 1 : painting_index;
		SelectablePaintingItemStackHelper.setPaintingIndex( stack, painting_index );
	}
	
	public static void nextPainting( @NotNull ItemStack stack ) {
		
		init();
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack );
		int painting_index = SelectablePaintingItemStackHelper.getPaintingIndex( stack ) + 1;
		painting_index = painting_index >= motives.get( size_index ).size() ? 0 : painting_index;
		SelectablePaintingItemStackHelper.setPaintingIndex( stack, painting_index );
	}
	
	public static String getSizeName( @NotNull ItemStack stack ) {
		
		init();
		return painting_sizes[SelectablePaintingItemStackHelper.getSizeIndex( stack )];
	}
	
	public static Component getPaintingName( @NotNull ItemStack stack ) {
		
		init();
		return getPaintingName(
			SelectablePaintingItemStackHelper.getSizeIndex( stack ),
			SelectablePaintingItemStackHelper.getPaintingIndex( stack )
		);
	}
	
	@NotNull
	private static Component getPaintingName( int size_index, int painting_index ) {
		
		init();
		return Component.translatable( Util.makeDescriptionId(
			"painting",
			BuiltInRegistries.PAINTING_VARIANT.getKey(
				motives.get( size_index >= motives.size() ? 0 : size_index )
					.get( painting_index >= motives.get( size_index ).size() ? 0 : painting_index )
					.value()
			)
		) );
	}
	
	@NotNull
	public static PaintingVariant getCurrentMotive( @NotNull ItemStack stack ) {
		
		init();
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack );
		int painting_index = SelectablePaintingItemStackHelper.getPaintingIndex( stack );
		return motives.get( size_index >= motives.size() ? 0 : size_index )
			.get( painting_index >= motives.get( size_index ).size() ? 0 : painting_index )
			.value();
	}
	
	//package-private
	@NotNull
	static Holder<PaintingVariant> getMotive( @NotNull ItemStack stack, @NotNull Level level ) {
		
		init();
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack );
		int painting_index;
		if( SelectablePaintingItemStackHelper.getRandom( stack ) ) {
			painting_index = level.getRandom().nextInt( motives.get( size_index ).size() );
		} else {
			painting_index = SelectablePaintingItemStackHelper.getPaintingIndex( stack );
		}
		return motives.get( size_index >= motives.size() ? 0 : size_index )
			.get( painting_index >= motives.get( size_index ).size() ? 0 : painting_index );
	}
}
