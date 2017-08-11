package de.canitzp.carz.listener;

import de.canitzp.carz.api.EntitySteerableBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

/**
 * Controls
 *
 * @author MisterErwin
 */
@Mod.EventBusSubscriber
public class KeyListener {

    private KeyBinding keyForward = Minecraft.getMinecraft().gameSettings.keyBindForward;
    private KeyBinding keyBackward = Minecraft.getMinecraft().gameSettings.keyBindBack;
    private KeyBinding keyLeft = Minecraft.getMinecraft().gameSettings.keyBindLeft;
    private KeyBinding keyRight = Minecraft.getMinecraft().gameSettings.keyBindRight;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        EntityPlayer p = Minecraft.getMinecraft().player;
        Entity riding = p.getRidingEntity();
        if (!(riding instanceof EntitySteerableBase)){
            return;
        }
        EntitySteerableBase steerable = (EntitySteerableBase) riding;
        steerable.updateInputs(this.keyLeft.isKeyDown(), this.keyRight.isKeyDown(), this.keyForward.isKeyDown(), this.keyBackward.isKeyDown());
    }
}
