package de.canitzp.carz.events;

import de.canitzp.carz.Carz;
import de.canitzp.carz.Registry;
import de.canitzp.carz.api.EntitySteerableBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Controls
 *
 * @author MisterErwin
 */
@SuppressWarnings("WeakerAccess")
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Carz.MODID)
public class KeyListener {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        EntityPlayer p = Minecraft.getMinecraft().player;
        Entity riding = p.getRidingEntity();
        if (!(riding instanceof EntitySteerableBase)) {
            return;
        }
        EntitySteerableBase steerable = (EntitySteerableBase) riding;
        steerable.updateInputs(Registry.keyLeft.isKeyDown(), Registry.keyRight.isKeyDown(), Registry.keyForward.isKeyDown(), Registry.keyBackward.isKeyDown());
    }
}
