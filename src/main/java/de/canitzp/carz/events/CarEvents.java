package de.canitzp.carz.events;

import de.canitzp.carz.api.EntityPartedBase;
import de.canitzp.carz.entity.EntityInvisibleCarPart;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;

/**
 * @author canitzp
 */
@Mod.EventBusSubscriber
public class CarEvents {

    @SubscribeEvent
    public static void entityCollisionEvent(GetCollisionBoxesEvent event) {
        if (event.getEntity() instanceof EntityPartedBase) {
            //TODO: Is this really necessary?
            Iterator<AxisAlignedBB> iterator = event.getCollisionBoxesList().iterator();
            while(iterator.hasNext()){
                AxisAlignedBB bb = iterator.next();
                for (EntityInvisibleCarPart p : ((EntityPartedBase)event.getEntity()).getPartArray()) {
                    if (bb.equals(p.getCollisionBoundingBox())) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }


}
