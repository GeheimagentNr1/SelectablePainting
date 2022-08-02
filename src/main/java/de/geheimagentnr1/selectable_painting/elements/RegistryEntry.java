package de.geheimagentnr1.selectable_painting.elements;


public class RegistryEntry<T> {
	
	
	private final String registryName;
	
	private final T value;
	
	private RegistryEntry( String _registryName, T _value ) {
		
		registryName = _registryName;
		value = _value;
	}
	
	public static <T> RegistryEntry<T> create( String registryName, T value ) {
		
		return new RegistryEntry<>( registryName, value );
	}
	
	public String getRegistryName() {
		
		return registryName;
	}
	
	public T getValue() {
		
		return value;
	}
}
