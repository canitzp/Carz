package de.canitzp.carz.listener;

import de.canitzp.carz.entity.car.EntitySteerableBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

/**
 * Controls
 * @author MisterErwin
 */
public class KeyListener {
    private KeyBinding keyForward;
    private KeyBinding keyBackward;
    private KeyBinding keyLeft;
    private KeyBinding keyRight;

    public KeyListener() {
        ClientRegistry.registerKeyBinding(this.keyForward =
                new KeyBinding("key.drive_forward", 17, "category.carz"));
        ClientRegistry.registerKeyBinding(this.keyBackward =
                new KeyBinding("key.drive_backward", 31, "category.carz"));
        ClientRegistry.registerKeyBinding(this.keyLeft =
                new KeyBinding("key.drive_left", 30, "category.carz"));
        ClientRegistry.registerKeyBinding(this.keyRight =
                new KeyBinding("key.drive_right", 32, "category.carz"));

    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        EntityPlayer p = Minecraft.getMinecraft().player;
        Entity riding = p.getRidingEntity();
        if (!(riding instanceof EntitySteerableBase))
            return;
        EntitySteerableBase steerable = (EntitySteerableBase) riding;
        steerable.updateInputs(this.keyLeft.isKeyDown(), this.keyRight.isKeyDown(),
                this.keyForward.isKeyDown(), this.keyBackward.isKeyDown());

    }
}
