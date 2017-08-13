package de.canitzp.carz.events;

import de.canitzp.carz.api.EntityRenderedBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
@Mod.EventBusSubscriber
public class CarEvents {

    @SubscribeEvent
    public static void entityCollisionEvent(GetCollisionBoxesEvent event) {
        if (event.getEntity() instanceof EntityRenderedBase) {
            List<AxisAlignedBB> hitBoxes = new ArrayList<>(event.getCollisionBoxesList());
            hitBoxes = ((EntityRenderedBase) event.getEntity()).getHitBoxes(event.getAabb(), hitBoxes);
            event.getCollisionBoxesList().clear();
            event.getCollisionBoxesList().addAll(hitBoxes);
        }
    }

}
