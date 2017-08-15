package de.canitzp.carz.network;

import de.canitzp.carz.Carz;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author canitzp
 */
public class NetworkHandler {

    public static SimpleNetworkWrapper net = NetworkRegistry.INSTANCE.newSimpleChannel(Carz.MODID);

    public static void preInit(FMLPreInitializationEvent event) {
        int id = 0;
        net.registerMessage(MessageCarSpeed.class, MessageCarSpeed.class, id++, Side.SERVER);
        net.registerMessage(MessageSendPixelMeshes.class, MessageSendPixelMeshes.class, id++, Side.CLIENT);
    }

}
