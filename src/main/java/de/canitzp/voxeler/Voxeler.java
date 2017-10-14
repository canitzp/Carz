package de.canitzp.voxeler;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class Voxeler {

    private static Map<String, VoxelPart> cachedChilds = new HashMap<>();

    public static VoxelBase loadModelFromFile(ResourceLocation location){
        String path = "/assets/" + location.getResourceDomain() + "/" + location.getResourcePath() + ".vox";
        return loadModelFromFile(new File(Voxeler.class.getResource(path).getFile()));
    }

    public static VoxelBase loadModelFromFile(File file) {
        try {
            if (file != null && file.getName().endsWith(".vox")) {
                VoxelBase voxelBase = null;
                VoxelPart currentPart = null;
                for (String line : FileUtils.readLines(file, "UTF-8")) {
                    if(!line.startsWith("#")){
                        if(line.startsWith("Voxeler File Format")){
                            voxelBase = parseVoxelBase(line);
                        } else if(voxelBase != null){
                            if(line.startsWith(": ")){
                                if(currentPart != null){
                                    voxelBase.boxList.add(currentPart);
                                }
                                currentPart = parseModelRenderer(voxelBase, line);
                            } else if(line.startsWith(":r> ")){
                                voxelBase.setTexture(new ResourceLocation(line.replace(":r> ", "").replace(" ", "")));
                            } else if(line.startsWith(":# ")){
                                String[] split = line.replace(":# ", "").split(";");
                                if(split.length == 4){
                                    String childFileName = split[0];
                                    if(areNumbers(split[1], split[2], split[3])){
                                        if(cachedChilds.containsKey(childFileName)){
                                            voxelBase.boxList.add(cachedChilds.get(childFileName).setOffset(parseFloat(split[1]), parseFloat(split[2]), parseFloat(split[3])));
                                        } else {
                                            VoxelPart child = loadChildFromFile(childFileName);
                                            if(child != null){
                                                cachedChilds.put(childFileName, child);
                                                voxelBase.boxList.add(child.setOffset(parseFloat(split[1]), parseFloat(split[2]), parseFloat(split[3])));
                                            }
                                        }
                                    }
                                }
                            } else {
                                if(currentPart != null){
                                    if(line.startsWith(":: ")){
                                        addBox(currentPart, line);
                                    } else if(line.startsWith("::: ")){
                                        // TODO implement child model parsing
                                    }
                                }
                            }
                        }
                    }
                }
                if(voxelBase != null && currentPart != null){
                    voxelBase.boxList.add(currentPart);
                }
                return voxelBase;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static VoxelBase parseVoxelBase(String line){
        String[] split = line.split(String.valueOf(VoxelerConverter.SPLIT_CHAR));
        if(split.length == 3){
            if(areNumbers(split[1], split[2])){
                return new VoxelBase(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            }
        }
        return null;
    }

    private static VoxelPart parseModelRenderer(VoxelBase voxelBase, String line){
        String[] split = line.replace(": ", "").split(String.valueOf(VoxelerConverter.SPLIT_CHAR));
        if(split.length == 9){
            int textOffX = areNumbers(split[1]) ? Integer.parseInt(split[1]) : 0;
            int textOffY = areNumbers(split[2]) ? Integer.parseInt(split[2]) : 0;
            VoxelPart part = new VoxelPart(split[0], textOffX, textOffY, voxelBase.textureWidth, voxelBase.textureHeight);
            if(areNumbers(split[3], split[4], split[5])){
                part.rotationPointX = parseFloat(split[3]);
                part.rotationPointY = parseFloat(split[4]);
                part.rotationPointZ = parseFloat(split[5]);
            }
            if(areNumbers(split[6], split[7], split[8])){
                part.rotateAngleX = parseFloat(split[6]);
                part.rotateAngleY = parseFloat(split[7]);
                part.rotateAngleZ = parseFloat(split[8]);
            }
            return part;
        }
        return null;
    }

    private static VoxelPart loadChildFromFile(String initialLine) throws Exception {
        File file = new File(Voxeler.class.getResource("/assets/" + initialLine + ".voxpart").getFile());
        ResourceLocation texture = null;
        VoxelPart renderer = null;
        for(String line : FileUtils.readLines(file, "UTF-8")){
            if(line.startsWith("Voxel Part")){
                String[] split = line.split(";");
                if(split.length == 5){
                    if(areNumbers(split[1], split[2])){
                        //renderer = new VoxelPart(split[4], )
                    }
                }
            }
        }
        return renderer;
    }

    private static void addBox(VoxelPart renderer, String line){
        String[] split = line.replace(":: ", "").split(String.valueOf(VoxelerConverter.SPLIT_CHAR));
        if(split.length == 7){
            if(areNumbers(split[0], split[1], split[2], split[3], split[4], split[5], split[6])){
                renderer.addBox("box", parseFloat(split[0]), parseFloat(split[1]), parseFloat(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]), parseFloat(split[6]));
            }
        }
    }

    public static boolean areNumbers(String... strings){
        for(String s : strings){
            if(!NumberUtils.isCreatable(s.replace(",", "."))){
                return false;
            }
        }
        return true;
    }

    public static float parseFloat(String s){
        return Float.parseFloat(s.replace(",", "."));
    }

}
