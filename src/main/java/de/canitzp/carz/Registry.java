package de.canitzp.carz;

import de.canitzp.carz.api.EntityRenderedBase;
import de.canitzp.carz.blocks.*;
import de.canitzp.carz.client.CustomModelLoader;
import de.canitzp.carz.client.models.ModelBus;
import de.canitzp.carz.client.models.ModelNakedBus;
import de.canitzp.carz.client.renderer.RenderCar;
import de.canitzp.carz.client.renderer.RenderInvisibleCarPart;
import de.canitzp.carz.entity.EntityBus;
import de.canitzp.carz.entity.EntityInvisibleCarPart;
import de.canitzp.carz.entity.EntitySportscar;
import de.canitzp.carz.fluid.FluidBase;
import de.canitzp.carz.items.*;
import de.canitzp.voxeler.VoxelBase;
import de.canitzp.voxeler.Voxeler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
@SuppressWarnings("WeakerAccess")
@Mod.EventBusSubscriber
public class Registry {
    /**
     * Internal Stuff:
     */
    public static final List<BlockBase> BLOCKS_FOR_REGISTERING = new ArrayList<>();
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final List<ItemBase> ITEMS_FOR_REGISTERING = new ArrayList<>();
    private static int entityId = 0;
    @SideOnly(Side.CLIENT)
    private static IRenderFactory<EntityRenderedBase> renderFactory;

    /**
     * Random:
     */
    public static final CreativeTabs TAB_GENERAL = new CreativeTabs(Carz.MODID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(itemKey);
        }
    };
    public static final CreativeTabs TAB_FIRE = new CreativeTabs(Carz.MODID + "_fire") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Blocks.FIRE);
        }
    };

    /**
     * Blocks:
     */
    public static BlockFuelStation blockFuelStation = new BlockFuelStation().register();
    public static BlockRoad blockRoad = new BlockRoad<>("road").register();
    public static BlockRoadSlope blockRoadSlope = new BlockRoadSlope().register();
    public static BlockRoadSign blockRoadSign = new BlockRoadSign().register();
    public static BlockPlantFermenter blockPlantFermenter = new BlockPlantFermenter().register();
    public static BlockBasic blockBasic = new BlockBasic().register();
    public static BlockRubberLog blockLog = new BlockRubberLog().register();
    public static BlockRubberLeaves blockRubberLeaves = new BlockRubberLeaves().register();

    /**
     * Items:
     */
    public static ItemCarPart itemCarPart = new ItemCarPart().register();
    public static ItemPainter itemPainter = new ItemPainter().register();
    public static ItemOilProbe itemOilProbe = new ItemOilProbe().register();
    public static ItemBaseDefault itemPressedPlant = new ItemBaseDefault<>("pressed_plant").register();
    public static ItemKey itemKey = new ItemKey().register();
    public static ItemBaseDefault itemRawRubber = new ItemBaseDefault<>("raw_rubber").register();
    public static ItemBaseDefault itemTreeTap = new ItemBaseDefault<>("tree_tap").register();
    public static ItemLicense itemLicense = new ItemLicense().register();

    /**
     * Fluids:
     */
    public static Fluid fluidBioFuel = new FluidBase("bio_fuel", 0xFFFFFFFF).setViscosity(6000);

    /**
     * Models:
     */
    public static VoxelBase MODEL_SPORTSCAR;
    public static final ModelBus MODEL_BUS = getInstanceWithDebug(ModelBus.class, ModelNakedBus.class);

    /**
     * Statistics:
     */
    public static StatBase ENTITY_HIT_COUNT = new StatBasic(Carz.MODID + ":stat.entity_hit.count", new TextComponentTranslation(Carz.MODID + ":stat.entity_hit.count")).registerStat();
    public static StatBase ENTITY_HIT_DAMAGE = new StatBasic(Carz.MODID + ":stat.entity_hit.dmg", new TextComponentTranslation(Carz.MODID + ":stat.entity_hit.dmg")).registerStat();

    /**
     * Keys:
     * They can't be initialized here, cause then the server would crash on startup, although we use {@link SideOnly}
     */
    @SideOnly(Side.CLIENT)
    public static KeyBinding keyForward;
    @SideOnly(Side.CLIENT)
    public static KeyBinding keyBackward;
    @SideOnly(Side.CLIENT)
    public static KeyBinding keyLeft;
    @SideOnly(Side.CLIENT)
    public static KeyBinding keyRight;
    @SideOnly(Side.CLIENT)
    public static KeyBinding enableDebug;
    @SideOnly(Side.CLIENT)
    public static KeyBinding keyStartEngine;


    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> reg = event.getRegistry();
        for (BlockBase block : BLOCKS_FOR_REGISTERING) {
            Carz.LOG.info("Registering Block: " + block.getRegistryName());
            reg.register(block);
        }
        for (Block block : BLOCKS) {
            Carz.LOG.info("Registering Block: " + block.getRegistryName());
            reg.register(block);
        }
        registerFluids(reg);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> reg = event.getRegistry();
        for (BlockBase block : BLOCKS_FOR_REGISTERING) {
            Carz.LOG.info("Registering ItemBlock: " + block.getRegistryName());
            reg.register(block.getItemBlock());
        }
        for (Block block : BLOCKS) {
            Carz.LOG.info("Registering ItemBlock: " + block.getRegistryName());
            reg.register(new CustomItemBlock(block).setRegistryName(block.getRegistryName()));
        }
        for (ItemBase item : ITEMS_FOR_REGISTERING) {
            Carz.LOG.info("Registering Item: " + item.getRegistryName());
            reg.register(item);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModel(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new CustomModelLoader());
        for (BlockBase block : BLOCKS_FOR_REGISTERING) {
            block.registerClient();
        }
        for (Block block : BLOCKS) {
            for(int i = 0; i < 16; i++){
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, new ModelResourceLocation(block.getRegistryName(), "inventory"));
            }
        }
        for (ItemBase item : ITEMS_FOR_REGISTERING) {
            item.registerClient();
        }
        initModels();
    }

    private static void registerFluids(IForgeRegistry<Block> registry) {
        FluidRegistry.registerFluid(fluidBioFuel);
        FluidRegistry.addBucketForFluid(fluidBioFuel);
        registry.register(new BlockFluid(fluidBioFuel));
    }

    public static void preInit(FMLPreInitializationEvent event) {
        registerEntity("sportscar", EntitySportscar.class, event.getSide());
        registerEntity("bus", EntityBus.class, event.getSide());
        EntityRegistry.registerModEntity(new ResourceLocation(Carz.MODID, "invispart"), EntityInvisibleCarPart.class, "invispart", entityId++, Carz.carz, 64, 5, true);
        if (event.getSide().isClient()) {
            /*((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new IResourceManagerReloadListener() {
                @SideOnly(Side.CLIENT)
                @Override
                public void onResourceManagerReload(IResourceManager resourceManager) {
                    initModels();
                }
            });*/
            RenderingRegistry.registerEntityRenderingHandler(EntityInvisibleCarPart.class, new RenderInvisibleCarPart.RenderInvisibleCarPartFactory());
            keyForward = Minecraft.getMinecraft().gameSettings.keyBindForward;
            keyBackward = Minecraft.getMinecraft().gameSettings.keyBindBack;
            keyLeft = Minecraft.getMinecraft().gameSettings.keyBindLeft;
            keyRight = Minecraft.getMinecraft().gameSettings.keyBindRight;
            ClientRegistry.registerKeyBinding(enableDebug = new KeyBinding(I18n.format("carz:key.render_debug.desc"), Keyboard.KEY_O, Carz.MODNAME));
            ClientRegistry.registerKeyBinding(keyStartEngine = new KeyBinding(I18n.format("carz:key.start_engine.desc"), Keyboard.KEY_R, Carz.MODNAME));
        }
    }

    @SideOnly(Side.CLIENT)
    private static void initModels(){
        registerFluidRenderer(fluidBioFuel);
        MODEL_SPORTSCAR = Voxeler.loadModelFromFile(new ResourceLocation(Carz.MODID, "models/voxeler/sportscar"));
    }

    public static void init(FMLInitializationEvent event){
        if(event.getSide().isClient()){
            for(Block block : BLOCKS){
                if(block instanceof IColoredBlock){
                    Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(((IColoredBlock) block)::getBlockColor, block);
                    Minecraft.getMinecraft().getItemColors().registerItemColorHandler(((IColoredBlock) block)::getItemColor, block);
                }
            }
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

    @SideOnly(Side.CLIENT)
    private static void registerFluidRenderer(Fluid fluid) {
        Block block = fluid.getBlock();
        if(block != null){
            Item item = Item.getItemFromBlock(block);
            final ModelResourceLocation loc = new ModelResourceLocation(new ResourceLocation(Carz.MODID, "fluids"), fluid.getName());
            ItemMeshDefinition mesh = stack -> loc;
            StateMapperBase mapper = new StateMapperBase() {
                @Nonnull
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                    return loc;
                }
            };
            ModelLoader.registerItemVariants(item);
            ModelLoader.setCustomMeshDefinition(item, mesh);
            ModelLoader.setCustomStateMapper(block, mapper);
        }
    }

    private static <T> T getInstanceWithDebug(Class<T> normal, Class<? extends T> debug) {
        try {
            if ("true".equals(System.getProperty("renderDebug")))
                return debug.newInstance();
            return normal.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
