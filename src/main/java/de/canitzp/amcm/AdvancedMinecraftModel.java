package de.canitzp.amcm;

import com.google.common.collect.Lists;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import scala.Int;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author canitzp
 */
public class AdvancedMinecraftModel {

    public static AMCMModel loadModel(File file) throws IOException {
        if(file != null && file.getName().endsWith("amcm")){
            List<IAMCMShapes> shapes = new ArrayList<>();
            AMCMTexture modelTexture = AMCMTexture.MISSING;
            for(String line : FileUtils.readLines(file, "UTF-8")){
                if(line.startsWith(":")){
                    if(line.startsWith(":res> ")){
                        String[] split = line.replace(":res> ", "").split(";");
                        if(split.length == 3){
                            modelTexture = new AMCMTexture(new ResourceLocation(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                        }
                    } else {
                        parseLine(line, shapes, modelTexture);
                    }
                }
            }
            return new AMCMPredefinedModel(shapes).setTexture(modelTexture);
        }
        return null;
    }

    private static void parseLine(String line, List<IAMCMShapes> shapes, AMCMTexture texture){
        if(line.startsWith(":box> ")){ // name,width,height,depth,offX,offY,offZ,rotPointX,rotPointY,rotPointZ,rotAngleX,rotAngleY,rotAngleZ,offTXDown,offTYDown
            String[] split = line.replace(":box> ", "").split(";");
            if(split.length == 17){
                Map<EnumFacing, int[]> textOffsets = new HashMap<>();
                textOffsets.put(EnumFacing.NORTH, new int[]{Integer.parseInt(split[13]), Integer.parseInt(split[14])});
                textOffsets.put(EnumFacing.SOUTH, new int[]{Integer.parseInt(split[15]), Integer.parseInt(split[16])});
                shapes.add(new AMCMBox(parseFloat(split[1]), parseFloat(split[2]), parseFloat(split[3]))
                        .setOffset(parseFloat(split[4]), parseFloat(split[5]), parseFloat(split[6]))
                        .setRotationPoint(parseFloat(split[7]), parseFloat(split[8]), parseFloat(split[9]))
                        .setRotateAngle(parseFloat(split[10]), parseFloat(split[11]), parseFloat(split[12]))
                        .setTextureOffset(textOffsets)
                        .setName(split[0]).setTexture(texture));
            }
        }
    }

    public static float parseFloat(String s){
        return Float.parseFloat(s.replace(",", "."));
    }

}
