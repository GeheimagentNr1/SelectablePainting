package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class SelectablePaintingItemStackHelper {
	
	
	@NotNull
	private static final String SELECTED_PAINTING_NAME = "selected_painting";
	
	@NotNull
	private static final String SELECTED_SIZE_INDEX_NAME = "size";
	
	@NotNull
	private static final String SELECTED_PAINTING_INDEX_NAME = "painting";
	
	@NotNull
	private static final String SELECTED_RANDOM_NAME = "random";
	
	public static int getSizeIndex( @NotNull ItemStack stack ) {
		
		return stack.getOrCreateTagElement( SELECTED_PAINTING_NAME ).getInt( SELECTED_SIZE_INDEX_NAME );
	}
	
	//package-private
	static void setSizeIndex( @NotNull ItemStack stack, int size_index ) {
		
		stack.getOrCreateTagElement( SELECTED_PAINTING_NAME ).putInt( SELECTED_SIZE_INDEX_NAME, size_index );
	}
	
	public static int getPaintingIndex( @NotNull ItemStack stack ) {
		
		return stack.getOrCreateTagElement( SELECTED_PAINTING_NAME ).getInt( SELECTED_PAINTING_INDEX_NAME );
	}
	
	//package-private
	static void setPaintingIndex( @NotNull ItemStack stack, int painting_index ) {
		
		stack.getOrCreateTagElement( SELECTED_PAINTING_NAME ).putInt( SELECTED_PAINTING_INDEX_NAME, painting_index );
	}
	
	public static void toogleRandom( @NotNull ItemStack stack ) {
		
		stack.getOrCreateTagElement( SELECTED_PAINTING_NAME ).putBoolean( SELECTED_RANDOM_NAME, !getRandom( stack ) );
	}
	
	public static boolean getRandom( @NotNull ItemStack stack ) {
		
		return stack.getOrCreateTagElement( SELECTED_PAINTING_NAME ).getBoolean( SELECTED_RANDOM_NAME );
	}
	
	@NotNull
	public static ItemStack writeDataToStack(
		@NotNull ItemStack stack,
		int size_index,
		int painting_index,
		boolean random ) {
		
		stack.getOrCreateTagElement( SELECTED_PAINTING_NAME ).putInt( SELECTED_SIZE_INDEX_NAME, size_index );
		stack.getOrCreateTagElement( SELECTED_PAINTING_NAME ).putInt( SELECTED_PAINTING_INDEX_NAME, painting_index );
		stack.getOrCreateTagElement( SELECTED_PAINTING_NAME ).putBoolean( SELECTED_RANDOM_NAME, random );
		return stack;
	}
}
