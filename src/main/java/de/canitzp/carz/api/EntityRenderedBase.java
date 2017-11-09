package de.canitzp.carz.api;

import de.canitzp.carz.Carz;
import de.canitzp.carz.inventory.ContainerCar;
import de.canitzp.carz.network.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class does provide the main function for a renderable object.
 * Extend this class if you want to make a car object
 * which doesn't move, isn't steerable and you can't sit in it.
 *
 * @author MisterErwin
 * @author canitzp
 * @see EntityMoveableBase
 * @see EntityRideableBase
 * @see EntitySteerableBase
 */
@SuppressWarnings("WeakerAccess")
public abstract class EntityRenderedBase extends Entity {

    public EntityRenderedBase(World worldIn) {
        super(worldIn);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return distance < getRenderDistance();
    }

    /**
     * @return The Model for the Car
     */
    @SideOnly(Side.CLIENT)
    public abstract ModelBase getCarModel();

    /**
     * Here you can define the texture for the car. You can just make a new {@link ResourceLocation},
     * since this variable gets cached by the Renderer, unless you restart the game or reload
     * the resources with F3+T
     *
     * @return The Location to the texture of the Car.
     */
    @SideOnly(Side.CLIENT)
    @Nullable
    public abstract ResourceLocation getCarTexture();

    /**
     * Here you have to setup all the OpenGL Stuff for the car like translation, rotation and scaling.
     *
     * @param x            The x coordinate where the model have to render
     * @param y            The y coordinate where the model have to render
     * @param z            The y coordinate where the model have to render
     * @param entityYaw    The current rotationYaw of the car, use this instead of this.rotationYaw!
     * @param partialTicks The partial ticks for rendering
     * @see net.minecraft.client.renderer.GlStateManager
     */
    @SideOnly(Side.CLIENT)
    public abstract void setupGL(double x, double y, double z, float entityYaw, float partialTicks);

    /**
     * Provides a rotation around the 3rd axis
     * @return the rotation
     */
    public float getRotationRoll(){
        //TODO @canitzp
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public double getRenderDistance(){
        return 16 * // one block
                16 * // one chunk
                (Minecraft.getMinecraft().gameSettings.fancyGraphics ? 16 : 8) // we render it for 16 chunks if fancy grafics are active
                 * 16; // cause the distance double is in micro blocks
    }

    /**
     * This is a special method where a car is able to manipulate the light level of the world.
     * The background behind this is done via ASM {@link de.canitzp.carz.asm.CarzEntityLightTransformer}.
     * The value the car does return here is recognized server and client side and should be zero,
     * if no light is send out by the car at the moment.
     *
     * @param world The world of the car
     * @param pos The position of the light that gets manipulated
     * @return The new light level value at the given position. Range: 0-15 Default: 0
     */
    public int getLightAt(World world, BlockPos pos){
        return 0;
    }
}
