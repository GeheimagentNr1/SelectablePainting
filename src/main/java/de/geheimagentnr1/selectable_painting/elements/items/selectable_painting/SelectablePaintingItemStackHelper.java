package de.geheimagentnr1.selectable_painting.elements.items.selectable_painting;

import de.geheimagentnr1.selectable_painting.elements.items.ModItemsRegisterFactory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class SelectablePaintingItemStackHelper {
	
	
	public static int getSizeIndex( @NotNull ItemStack stack ) {
		
		return stack.getOrDefault( ModItemsRegisterFactory.SIZE_INDEX, 0 );
	}
	
	//package-private
	static void setSizeIndex( @NotNull ItemStack stack, int size_index ) {
		
		stack.set( ModItemsRegisterFactory.SIZE_INDEX, size_index );
	}
	
	public static int getPaintingIndex( @NotNull ItemStack stack ) {
		
		return stack.getOrDefault( ModItemsRegisterFactory.PAINTING_INDEX, 0 );
	}
	
	//package-private
	static void setPaintingIndex( @NotNull ItemStack stack, int painting_index ) {
		
		stack.set( ModItemsRegisterFactory.PAINTING_INDEX, painting_index );
	}
	
	public static boolean getRandom( @NotNull ItemStack stack ) {
		
		return stack.getOrDefault( ModItemsRegisterFactory.RANDOM, false );
	}
	
	public static void toogleRandom( @NotNull ItemStack stack ) {
		
		stack.set( ModItemsRegisterFactory.RANDOM, !getRandom( stack ) );
	}
	
	@NotNull
	public static ItemStack writeDataToStack(
		@NotNull ItemStack stack,
		int size_index,
		int painting_index,
		boolean random ) {
		
		setSizeIndex( stack, size_index );
		setPaintingIndex( stack, painting_index );
		stack.set( ModItemsRegisterFactory.RANDOM, random );
		return stack;
	}
}
