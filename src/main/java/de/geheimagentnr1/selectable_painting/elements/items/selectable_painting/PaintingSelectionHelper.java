package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import net.minecraft.entity.item.PaintingType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.*;


public class PaintingSelectionHelper {
	
	
	private static long painting_count = 0;
	
	private static String[] painting_sizes;
	
	private static ArrayList<ArrayList<String>> painting_names;
	
	private static ArrayList<ArrayList<PaintingType>> painting_types;
	
	//package-private
	static void init() {
		
		long current_painting_count = Registry.MOTIVE.stream().count();
		if( painting_count != current_painting_count ) {
			painting_count = current_painting_count;
			Iterator<PaintingType> iterator = Registry.MOTIVE.iterator();
			TreeSet<String> sizes = new TreeSet<>();
			TreeMap<String, TreeSet<String>> paintingNames = new TreeMap<>();
			TreeMap<String, TreeSet<PaintingType>> paintingTypes = new TreeMap<>();
			
			while( iterator.hasNext() ) {
				PaintingType paintingType = iterator.next();
				int widthSize = paintingType.getWidth() / 16;
				int heightSize = paintingType.getHeight() / 16;
				@SuppressWarnings( "StringConcatenationMissingWhitespace" )
				String paintingSize = widthSize + "x" + heightSize;
				if( sizes.add( paintingSize ) ) {
					paintingNames.put( paintingSize, new TreeSet<>() );
					paintingTypes.put(
						paintingSize,
						new TreeSet<>( Comparator.comparing(
							paintingType2 -> Objects.requireNonNull( paintingType2.getRegistryName() ).getPath()
						) )
					);
				}
				paintingNames.get( paintingSize ).add( Objects.requireNonNull( paintingType.getRegistryName() )
					.getPath() );
				paintingTypes.get( paintingSize ).add( paintingType );
			}
			painting_sizes = sizes.toArray( new String[0] );
			painting_names = new ArrayList<>();
			for( TreeSet<String> names : paintingNames.values() ) {
				painting_names.add( new ArrayList<>( Arrays.asList( names.toArray( new String[0] ) ) ) );
			}
			painting_types = new ArrayList<>();
			for( TreeSet<PaintingType> types : paintingTypes.values() ) {
				painting_types.add( new ArrayList<>( Arrays.asList( types.toArray( new PaintingType[0] ) ) ) );
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
		
		return painting_sizes[SelectablePaintingItemStackHelper.getSizeIndex( stack )];
	}
	
	public static TranslationTextComponent getPaintingName( ItemStack stack ) {
		
		return getPaintingName(
			SelectablePaintingItemStackHelper.getSizeIndex( stack ),
			SelectablePaintingItemStackHelper.getPaintingIndex( stack )
		);
	}
	
	private static TranslationTextComponent getPaintingName( int size_index, int painting_index ) {
		
		return new TranslationTextComponent( Util.makeTranslationKey(
			"painting",
			painting_types.get( size_index ).get( painting_index ).getRegistryName()
		) );
	}
	
	public static PaintingType getCurrentPaintingType( ItemStack stack ) {
		
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack );
		int painting_index = SelectablePaintingItemStackHelper.getPaintingIndex( stack );
		return painting_types.get( size_index ).get( painting_index );
	}
	
	//package-private
	static PaintingType getPaintingType( ItemStack stack, World world ) {
		
		int size_index = SelectablePaintingItemStackHelper.getSizeIndex( stack );
		int painting_index;
		if( SelectablePaintingItemStackHelper.getRandom( stack ) ) {
			painting_index = world.getRandom().nextInt( painting_types.get( size_index ).size() );
		} else {
			painting_index = SelectablePaintingItemStackHelper.getPaintingIndex( stack );
		}
		return painting_types.get( size_index ).get( painting_index );
	}
}
