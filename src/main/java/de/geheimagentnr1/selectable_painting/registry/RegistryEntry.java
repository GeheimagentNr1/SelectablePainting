package de.geheimagentnr1.selectable_painting.registry;

import org.jetbrains.annotations.NotNull;


public record RegistryEntry<T>(@NotNull String name, @NotNull T value) {
	
	
	@NotNull
	public static <T> RegistryEntry<T> create( @NotNull String name, @NotNull T value ) {
		
		return new RegistryEntry<>( name, value );
	}
}
