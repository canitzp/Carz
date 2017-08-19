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
        NetworkRegistry.INSTANCE.registerGuiHandler(Carz.carz, new GuiHandler());
        int id = 0;
        net.registerMessage(MessageCarSpeed.class, MessageCarSpeed.class, id++, Side.SERVER);
        net.registerMessage(MessageSendPixelMeshesToClient.class, MessageSendPixelMeshesToClient.class, id++, Side.CLIENT);
        net.registerMessage(MessageSendPixelMeshesToServer.class, MessageSendPixelMeshesToServer.class, id++, Side.SERVER);
        net.registerMessage(MessageUpdatePainter.class, MessageUpdatePainter.class, id++, Side.SERVER);
        net.registerMessage(MessageCarPartInteract.class, MessageCarPartInteract.class, id++, Side.SERVER);
        net.registerMessage(MessageCarMultiSeatChange.class, MessageCarMultiSeatChange.class, id++, Side.CLIENT);
    }

}
