package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;


public class PaintingSelectionHelper {
	
	
	private static long painting_count = 0;
	
	private static String[] painting_sizes;
	
	private static ArrayList<ArrayList<String>> painting_names;
	
	private static ArrayList<ArrayList<Motive>> motives;
	
	//package-private
	static void init() {
		
		long current_painting_count = Registry.MOTIVE.stream().count();
		if( painting_count != current_painting_count ) {
			painting_count = current_painting_count;
			Iterator<Motive> iterator = Registry.MOTIVE.iterator();
			TreeSet<String> sizes = new TreeSet<>();
			TreeMap<String, TreeSet<String>> paintingNames = new TreeMap<>();
			TreeMap<String, TreeSet<Motive>> motivesMap = new TreeMap<>();
			
			while( iterator.hasNext() ) {
				Motive motive = iterator.next();
				int widthSize = motive.getWidth() / 16;
				int heightSize = motive.getHeight() / 16;
				@SuppressWarnings( "StringConcatenationMissingWhitespace" )
				String paintingSize = widthSize + "x" + heightSize;
				if( sizes.add( paintingSize ) ) {
					paintingNames.put( paintingSize, new TreeSet<>() );
					motivesMap.put(
						paintingSize,
						new TreeSet<>( Comparator.comparing(
							motive2 -> Objects.requireNonNull( motive2.getRegistryName() ).getPath()
						) )
					);
				}
				paintingNames.get( paintingSize ).add( Objects.requireNonNull( motive.getRegistryName() ).getPath() );
				motivesMap.get( paintingSize ).add( motive );
			}
			painting_sizes = sizes.toArray( new String[0] );
			painting_names = new ArrayList<>();
			for( TreeSet<String> names : paintingNames.values() ) {
				painting_names.add( new ArrayList<>( Arrays.asList( names.toArray( new String[0] ) ) ) );
			}
			motives = new ArrayList<>();
			for( TreeSet<Motive> types : motivesMap.values() ) {
				motives.add( new ArrayList<>( Arrays.asList( types.toArray( new Motive[0] ) ) ) );
			}
		}
	}
	
	public static void previousSize( ItemStack stack ) {
		
		init();
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack ) - 1;
		size_index = size_index < 0 ? painting_sizes.length - 1 : size_index;
		SelectablePaintingItemStackHelper.setSizeIndex( stack, size_index );
		SelectablePaintingItemStackHelper.setPaintingIndex( stack, 0 );
	}
	
	public static void nextSize( ItemStack stack ) {
		
		init();
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack ) + 1;
		size_index = size_index >= painting_sizes.length ? 0 : size_index;
		SelectablePaintingItemStackHelper.setSizeIndex( stack, size_index );
		SelectablePaintingItemStackHelper.setPaintingIndex( stack, 0 );
	}
	
	public static void previousPainting( ItemStack stack ) {
		
		init();
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack );
		int painting_index = SelectablePaintingItemStackHelper.getPaintingIndex( stack ) - 1;
		painting_index = painting_index < 0 ? painting_names.get( size_index ).size() - 1 : painting_index;
		SelectablePaintingItemStackHelper.setPaintingIndex( stack, painting_index );
	}
	
	public static void nextPainting( ItemStack stack ) {
		
		init();
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack );
		int painting_index = SelectablePaintingItemStackHelper.getPaintingIndex( stack ) + 1;
		painting_index = painting_index >= painting_names.get( size_index ).size() ? 0 : painting_index;
		SelectablePaintingItemStackHelper.setPaintingIndex( stack, painting_index );
	}
	
	public static String getSizeName( ItemStack stack ) {
		
		init();
		return painting_sizes[SelectablePaintingItemStackHelper.getSizeIndex( stack )];
	}
	
	public static TranslatableComponent getPaintingName( ItemStack stack ) {
		
		init();
		return getPaintingName(
			SelectablePaintingItemStackHelper.getSizeIndex( stack ),
			SelectablePaintingItemStackHelper.getPaintingIndex( stack )
		);
	}
	
	private static TranslatableComponent getPaintingName( int size_index, int painting_index ) {
		
		init();
		return new TranslatableComponent( Util.makeDescriptionId(
			"painting",
			motives.get( size_index ).get( painting_index ).getRegistryName()
		) );
	}
	
	public static Motive getCurrentMotive( ItemStack stack ) {
		
		init();
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack );
		int painting_index = SelectablePaintingItemStackHelper.getPaintingIndex( stack );
		return motives.get( size_index ).get( painting_index );
	}
	
	//package-private
	static Motive getMotive( ItemStack stack, Level level ) {
		
		init();
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack );
		int painting_index;
		if( SelectablePaintingItemStackHelper.getRandom( stack ) ) {
			painting_index = level.getRandom().nextInt( motives.get( size_index ).size() );
		} else {
			painting_index = SelectablePaintingItemStackHelper.getPaintingIndex( stack );
		}
		return motives.get( size_index ).get( painting_index );
	}
}
