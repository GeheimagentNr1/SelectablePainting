package de.geheimagentnr1.selectable_painting.network;

import de.geheimagentnr1.minecraft_forge_api.network.AbstractNetwork;
import de.geheimagentnr1.selectable_painting.SelectablePaintingMod;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


@NoArgsConstructor( access = AccessLevel.PRIVATE )
public class Network extends AbstractNetwork {
	
	
	@NotNull
	private static final Network INSTANCE = new Network();
	
	@NotNull
	public static Network getInstance() {
		
		return INSTANCE;
	}
	
	@NotNull
	@Override
	protected String getModId() {
		
		return SelectablePaintingMod.MODID;
	}
	
	@NotNull
	@Override
	protected String getNetworkName() {
		
		return "main";
	}
	
	@Override
	public void registerPackets() {
		
		getChannel().registerMessage(
			0,
			UpdateSelectablePaintingItemStackMsg.class,
			UpdateSelectablePaintingItemStackMsg::encode,
			UpdateSelectablePaintingItemStackMsg::decode,
			UpdateSelectablePaintingItemStackMsg::handle
		);
	}
}
