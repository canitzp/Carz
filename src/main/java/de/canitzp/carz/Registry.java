package de.canitzp.carz;

import de.canitzp.carz.api.EntityRenderdBase;
import de.canitzp.carz.blocks.BlockFuelStation;
import de.canitzp.carz.client.renderer.RenderCar;
import de.canitzp.carz.entity.EntitySportscar;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author canitzp
 */
@Mod.EventBusSubscriber
public class Registry {

    private static int entityId = 0;
    @SideOnly(Side.CLIENT)
    private static IRenderFactory<EntityRenderdBase> renderFactory;

    public static final CreativeTabs TAB = new CreativeTabs(Carz.MODID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.ARMOR_STAND);
        }
    };

    public static BlockFuelStation blockFuelStation;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        Carz.LOG.info("Registering Blocks");
        IForgeRegistry<Block> reg = event.getRegistry();
        reg.register(blockFuelStation = new BlockFuelStation());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        Carz.LOG.info("Registering Items");
        IForgeRegistry<Item> reg = event.getRegistry();
        reg.register(new ItemBlock(blockFuelStation).setRegistryName(blockFuelStation.getRegistryName()).setUnlocalizedName(blockFuelStation.getUnlocalizedName()));
    }

    public static void preInit(FMLPreInitializationEvent event){
        registerEntity("sportscar", EntitySportscar.class, event.getSide());
    }

    private static <T extends EntityRenderdBase> void  registerEntity(String name, Class<T> entity, Side side){
        Carz.LOG.info(String.format("Registering '%s'", name));
        EntityRegistry.registerModEntity(new ResourceLocation(Carz.MODID, name), entity, name, entityId++, Carz.carz, 64, 5, true);
        if(side.isClient()){
            if(renderFactory == null){
                renderFactory = new IRenderFactory<EntityRenderdBase>() {
                    @SideOnly(Side.CLIENT)
                    @Override
                    public Render<? super EntityRenderdBase> createRenderFor(RenderManager manager) {
                        RenderCar<EntityRenderdBase> renderCar = new RenderCar<>(manager);
                        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(renderCar);
                        return renderCar;
                    }
                };
            }
            RenderingRegistry.registerEntityRenderingHandler(entity, renderFactory);
        }
    }

}
