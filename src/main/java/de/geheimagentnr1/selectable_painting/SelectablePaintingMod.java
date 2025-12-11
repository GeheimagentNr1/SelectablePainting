package de.geheimagentnr1.selectable_painting;


import de.geheimagentnr1.selectable_painting.elements.creative_mod_tabs.ModCreativeModeTabsRegisterFactory;
import de.geheimagentnr1.selectable_painting.elements.items.ModItemsRegisterFactory;
import de.geheimagentnr1.selectable_painting.network.Network;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;


@Mod( SelectablePaintingMod.MODID )
public class SelectablePaintingMod {
	
	
	@NotNull
	public static final String MODID = "selectable_painting";
	
	public SelectablePaintingMod( @NotNull IEventBus modEventBus ) {
		
		ModItemsRegisterFactory modItemsRegisterFactory = new ModItemsRegisterFactory();
		modEventBus.register( modItemsRegisterFactory );
		modEventBus.register( new ModCreativeModeTabsRegisterFactory( modItemsRegisterFactory ) );
		modEventBus.register( Network.getInstance() );
	}
}
