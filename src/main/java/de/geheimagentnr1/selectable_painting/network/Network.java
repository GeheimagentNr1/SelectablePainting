package de.geheimagentnr1.selectable_painting.network;

import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;


@NoArgsConstructor( access = AccessLevel.PRIVATE )
public class Network {
	
	
	@NotNull
	private static final Network INSTANCE = new Network();
	
	@NotNull
	public static Network getInstance() {
		
		return INSTANCE;
	}
	
	@NotNull
	public static ResourceLocation createId( @NotNull String name ) {
		
		return ResourceLocation.fromNamespaceAndPath( SelectablePaintingMod.MODID, name );
	}
	
	@SubscribeEvent
	public void registerPayloadHandlers( @NotNull RegisterPayloadHandlersEvent event ) {
		
		PayloadRegistrar registrar = event.registrar( SelectablePaintingMod.MODID );
		registrar.playToServer(
			UpdateSelectablePaintingItemStackMsg.TYPE,
			UpdateSelectablePaintingItemStackMsg.STREAM_CODEC,
			UpdateSelectablePaintingItemStackMsg::handle
		);
	}
}
