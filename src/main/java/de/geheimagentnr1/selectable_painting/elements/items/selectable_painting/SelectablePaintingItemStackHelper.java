package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import net.minecraft.item.ItemStack;


//package-private
class SelectablePaintingItemStackHelper {
	
	
	private static final String SELECTED_PAINTING_NAME = "selected_painting";
	
	private static final String SELECTED_SIZE_INDEX_NAME = "size";
	
	private static final String SELECTED_PAINTING_INDEX_NAME = "painting";
	
	//package-private
	static int getSizeIndex( ItemStack stack ) {
		
		return stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).getInt( SELECTED_SIZE_INDEX_NAME );
	}
	
	//package-private
	static void setSizeIndex( ItemStack stack, int size_index ) {
		
		stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).putInt( SELECTED_SIZE_INDEX_NAME, size_index );
	}
	
	//package-private
	static int getPaintingIndex( ItemStack stack ) {
		
		return stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).getInt( SELECTED_PAINTING_INDEX_NAME );
	}
	
	//package-private
	static void setPaintingIndex( ItemStack stack, int painting_index ) {
		
		stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).putInt( SELECTED_PAINTING_INDEX_NAME, painting_index );
	}
	
	//package-private
	static ItemStack writeIndexesToStack( ItemStack stack, int size_index, int painting_index ) {
		
		stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).putInt( SELECTED_SIZE_INDEX_NAME, size_index );
		stack.getOrCreateChildTag( SELECTED_PAINTING_NAME ).putInt( SELECTED_PAINTING_INDEX_NAME, painting_index );
		return stack;
	}
}
