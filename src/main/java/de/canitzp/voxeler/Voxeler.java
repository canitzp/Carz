package de.canitzp.voxeler;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author canitzp
 */
public class Voxeler {

    public static VoxelBase loadModelFromFile(ResourceLocation location){
        String path = "/assets/" + location.getResourceDomain() + "/" + location.getResourcePath() + ".vox";
        return loadModelFromFile(new File(Voxeler.class.getResource(path).getFile()));
    }

    public static VoxelBase loadModelFromFile(File file) {
        try {
            if (file != null && file.getName().endsWith(".vox")) {
                VoxelBase voxelBase = null;
                ModelRenderer currentRenderer = null;
                for (String line : FileUtils.readLines(file, "UTF-8")) {
                    if(!line.startsWith("#")){
                        if(line.startsWith("Voxeler File Format")){
                            voxelBase = parseVoxelBase(line);
                        } else if(voxelBase != null){
                            if(line.startsWith(": ")){
                                if(currentRenderer != null){
                                    voxelBase.boxList.add(currentRenderer);
                                }
                                currentRenderer = parseModelRenderer(voxelBase, line);
                            } else if(currentRenderer != null){
                                if(line.startsWith(":: ")){
                                    addBox(currentRenderer, line);
                                } else if(line.startsWith("::: ")){

                                }
                            }
                        }
                    }
                }
                if(voxelBase != null && currentRenderer != null){
                    voxelBase.boxList.add(currentRenderer);
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

    private static ModelRenderer parseModelRenderer(VoxelBase voxelBase, String line){
        String[] split = line.replace(": ", "").split(String.valueOf(VoxelerConverter.SPLIT_CHAR));
        if(split.length == 9){
            ModelRenderer modelRenderer = new ModelRenderer(voxelBase, split[0]);
            voxelBase.boxList.remove(modelRenderer);
            if(areNumbers(split[1], split[2])){
                modelRenderer.setTextureOffset(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            }
            if(areNumbers(split[3], split[4], split[5])){
                modelRenderer.setRotationPoint(parseFloat(split[3]), parseFloat(split[4]), parseFloat(split[5]));
            }
            if(areNumbers(split[6], split[7], split[8])){
                modelRenderer.rotateAngleX = parseFloat(split[6]);
                modelRenderer.rotateAngleY = parseFloat(split[7]);
                modelRenderer.rotateAngleZ = parseFloat(split[8]);
            }
            return modelRenderer;
        }
        return null;
    }

    private static void addBox(ModelRenderer renderer, String line){
        String[] split = line.replace(":: ", "").split(String.valueOf(VoxelerConverter.SPLIT_CHAR));
        if(split.length == 7){
            if(areNumbers(split[0], split[1], split[2], split[3], split[4], split[5], split[6])){
                renderer.addBox(parseFloat(split[0]), parseFloat(split[1]), parseFloat(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]), parseFloat(split[6]));
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
