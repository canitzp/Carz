package de.canitzp.carz.network;

import de.canitzp.carz.Carz;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author canitzp
 */
public class NetworkHandler {

    public static SimpleNetworkWrapper net = NetworkRegistry.INSTANCE.newSimpleChannel(Carz.MODID);

    public static final DataSerializer<int[]> VARINT_ARRAY = new DataSerializer<int[]>() {
        @ParametersAreNonnullByDefault
        public void write(PacketBuffer buf, int[] value) {
            buf.writeVarIntArray(value);
        }

        @ParametersAreNonnullByDefault
        public int[] read(PacketBuffer buf) throws IOException {
            return buf.readVarIntArray();
        }

        public DataParameter<int[]> createKey(int id) {
            return new DataParameter<>(id, this);
        }

        @Override
        @MethodsReturnNonnullByDefault
        @ParametersAreNonnullByDefault
        public int[] copyValue(int[] value) {
            int[] ret = new int[value.length];
            System.arraycopy(value, 0, ret, 0, value.length);
            return ret;
        }
    };

    static {
        DataSerializers.registerSerializer(VARINT_ARRAY);
    }

    public static void preInit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Carz.carz, new GuiHandler());
        int id = 0;
        net.registerMessage(MessageCarSpeed.class, MessageCarSpeed.class, id++, Side.SERVER);
        net.registerMessage(MessageSendPixelMeshesToClient.class, MessageSendPixelMeshesToClient.class, id++, Side.CLIENT);
        net.registerMessage(MessageSendPixelMeshesToServer.class, MessageSendPixelMeshesToServer.class, id++, Side.SERVER);
        net.registerMessage(MessageUpdatePainter.class, MessageUpdatePainter.class, id++, Side.SERVER);
        net.registerMessage(MessageCarPartInteract.class, MessageCarPartInteract.class, id++, Side.SERVER);
    }

}
