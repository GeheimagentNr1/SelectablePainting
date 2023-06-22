package de.geheimagentnr1.selectable_painting;


import de.geheimagentnr1.minecraft_forge_api.AbstractMod;
import de.geheimagentnr1.selectable_painting.elements.creative_mod_tabs.ModCreativeModeTabsRegisterFactory;
import de.geheimagentnr1.selectable_painting.elements.items.ModItemsRegisterFactory;
import de.geheimagentnr1.selectable_painting.network.Network;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;


@Mod( SelectablePaintingMod.MODID )
public class SelectablePaintingMod extends AbstractMod {
	
	
	@NotNull
	public static final String MODID = "selectable_painting";
	
	@NotNull
	@Override
	public String getModId() {
		
		return MODID;
	}
	
	@Override
	protected void initMod() {
		
		ModItemsRegisterFactory modItemsRegisterFactory = registerEventHandler( new ModItemsRegisterFactory() );
		registerEventHandler( new ModCreativeModeTabsRegisterFactory( modItemsRegisterFactory ) );
		registerEventHandler( Network.getInstance() );
	}
}