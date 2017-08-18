package de.canitzp.carz;

import de.canitzp.carz.api.EntityRenderedBase;
import de.canitzp.carz.blocks.BlockFuelStation;
import de.canitzp.carz.blocks.BlockRoad;
import de.canitzp.carz.blocks.BlockSign;
import de.canitzp.carz.blocks.EnumSigns;
import de.canitzp.carz.client.models.ModelBus;
import de.canitzp.carz.client.models.ModelSportscar;
import de.canitzp.carz.client.renderer.RenderCar;
import de.canitzp.carz.client.renderer.RenderInvisibleCarPart;
import de.canitzp.carz.entity.EntityBus;
import de.canitzp.carz.entity.EntityInvisibleCarPart;
import de.canitzp.carz.entity.EntitySportscar;
import de.canitzp.carz.items.ItemBlockSign;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.Constants;
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

    /**
     * Internal Stuff:
     */
    private static int entityId = 0;
    @SideOnly(Side.CLIENT)
    private static IRenderFactory<EntityRenderedBase> renderFactory;

    /**
     * Random:
     */
    public static final CreativeTabs TAB = new CreativeTabs(Carz.MODID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.ARMOR_STAND);
        }
    };

    /**
     * Blocks:
     */
    public static BlockFuelStation blockFuelStation;
    public static BlockRoad blockRoad;
    public static BlockSign blockSign;

    /**
     * Items:
     */

    /**
     * Models:
     */
    public static final ModelSportscar MODEL_SPORTSCAR = new ModelSportscar();
    public static final ModelBus MODEL_BUS = new ModelBus();

    /**
     * Statistics:
     */
    public static StatBase ENTITY_HIT_COUNT = new StatBasic("stat.entity_hit.count", new TextComponentTranslation(Carz.MODID + ".stat.entity_hit.count")).registerStat();
    public static StatBase ENTITY_HIT_DAMAGE = new StatBasic("stat.entity_hit.dmg", new TextComponentTranslation(Carz.MODID + ".stat.entity_hit.dmg")).registerStat();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        Carz.LOG.info("Registering Blocks");
        IForgeRegistry<Block> reg = event.getRegistry();
        reg.register(blockFuelStation = new BlockFuelStation());
        reg.register(blockRoad = new BlockRoad("road"));
        reg.register(blockSign = new BlockSign());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        Carz.LOG.info("Registering Items");
        IForgeRegistry<Item> reg = event.getRegistry();
        reg.register(new ItemBlock(blockFuelStation).setRegistryName(blockFuelStation.getRegistryName()).setUnlocalizedName(blockFuelStation.getUnlocalizedName()));
        reg.register(new ItemBlock(blockRoad).setRegistryName(blockRoad.getRegistryName()).setUnlocalizedName(blockRoad.getUnlocalizedName()));
        reg.register(new ItemBlockSign(blockSign));
    }

    @SubscribeEvent
    public static void registerModel(ModelRegistryEvent event) {
        //for(EnumSigns sign : EnumSigns.values()){
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockSign), 0, new ModelResourceLocation(new ResourceLocation(blockSign.getRegistryName().toString()), "inventory"));
        //}
    }

    public static void preInit(FMLPreInitializationEvent event) {
        registerEntity("sportscar", EntitySportscar.class, event.getSide());
        registerEntity("bus", EntityBus.class, event.getSide());
        EntityRegistry.registerModEntity(new ResourceLocation(Carz.MODID, "invispart"), EntityInvisibleCarPart.class, "invispart", entityId++, Carz.carz, 64, 5, true);
        if (event.getSide().isClient()) {
            RenderingRegistry.registerEntityRenderingHandler(EntityInvisibleCarPart.class, new RenderInvisibleCarPart.RenderInvisibleCarPartFactory());
        }
    }

    private static <T extends EntityRenderedBase> void registerEntity(String name, Class<T> entity, Side side) {
        Carz.LOG.info(String.format("Registering '%s'", name));
        EntityRegistry.registerModEntity(new ResourceLocation(Carz.MODID, name), entity, name, entityId++, Carz.carz, 64, 5, true);
        if (side.isClient()) {
            initClientEntity(entity);
        }
    }

    @SideOnly(Side.CLIENT)
    private static <T extends EntityRenderedBase> void initClientEntity(Class<T> entityClass) {
        if (renderFactory == null) {
            renderFactory = manager -> {
                RenderCar<EntityRenderedBase> renderCar = new RenderCar<>(manager);
                ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(renderCar);
                return renderCar;
            };
        }
        RenderingRegistry.registerEntityRenderingHandler(entityClass, renderFactory);
    }

}
