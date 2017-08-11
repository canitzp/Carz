package de.canitzp.carz.proxy;

import de.canitzp.carz.Carz;
import de.canitzp.carz.entity.EntityCar;
import de.canitzp.carz.entity.car.EntityTestCar;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * @author MisterErwin
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        Carz.LOG.info("Registering Cars");
        EntityRegistry.registerModEntity(new ResourceLocation(Carz.MODID, "car"), EntityCar.class, "car", 0, Carz.carz, 64, 5, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Carz.MODID, "testcar"), EntityTestCar.class, "testcar", 1, Carz.carz, 64, 5, true);

    }

    public void init(FMLInitializationEvent e) {

    }

    public void postInit(FMLPostInitializationEvent e) {

    }
}
