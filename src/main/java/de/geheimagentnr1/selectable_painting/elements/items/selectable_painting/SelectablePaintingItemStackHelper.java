package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import net.minecraft.item.ItemStack;


public class SelectablePaintingItemStackHelper {
	
	
	private static final String SELECTED_PAINTING_NAME = "selected_painting";
	
	private static final String SELECTED_SIZE_INDEX_NAME = "size";
	
	private static final String SELECTED_PAINTING_INDEX_NAME = "painting";
	
	private static final String SELECTED_RANDOM_NAME = "random";
	
	public static int getSizeIndex( ItemStack stack ) {
		
		return stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).getInt( SELECTED_SIZE_INDEX_NAME );
	}
	
	//package-private
	static void setSizeIndex( ItemStack stack, int size_index ) {
		
		stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).putInt( SELECTED_SIZE_INDEX_NAME, size_index );
	}
	
	public static int getPaintingIndex( ItemStack stack ) {
		
		return stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).getInt( SELECTED_PAINTING_INDEX_NAME );
	}
	
	//package-private
	static void setPaintingIndex( ItemStack stack, int painting_index ) {
		
		stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).putInt( SELECTED_PAINTING_INDEX_NAME, painting_index );
	}
	
	public static void toogleRandom( ItemStack stack ) {
		
		stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).putBoolean( SELECTED_RANDOM_NAME, !getRandom( stack ) );
	}
	
	public static boolean getRandom( ItemStack stack ) {
		
		return stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).getBoolean( SELECTED_RANDOM_NAME );
	}
	
	public static ItemStack writeDataToStack( ItemStack stack, int size_index, int painting_index, boolean random ) {
		
		stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).putInt( SELECTED_SIZE_INDEX_NAME, size_index );
		stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).putInt( SELECTED_PAINTING_INDEX_NAME, painting_index );
		stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).putBoolean( SELECTED_RANDOM_NAME, random );
		return stack;
	}
}
