package de.geheimagentnr1.selectable_painting;


import de.geheimagentnr1.selectable_painting.setup.ClientProxy;
import de.geheimagentnr1.selectable_painting.setup.IProxy;
import de.geheimagentnr1.selectable_painting.setup.ModSetup;
import de.geheimagentnr1.selectable_painting.setup.ServerProxy;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;


@Mod( SelectablePaintingMod.MODID )
public class SelectablePaintingMod {
	
	
	public static final String MODID = "selectable_painting";
	
	public static final IProxy proxy = DistExecutor.safeRunForDist( () -> ClientProxy::new, () -> ServerProxy::new );
	
	public static final ModSetup setup = new ModSetup();
}
