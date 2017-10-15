package de.canitzp.amcm;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import scala.Int;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author canitzp
 */
public class AdvancedMinecraftModel {

    public static AMCMModel loadModel(ResourceLocation res) throws IOException{
        if(!res.toString().endsWith(".amcm")){
           res = new ResourceLocation(res.getResourceDomain(), res.getResourcePath() + ".amcm");
        }
        return loadModel(Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream());
    }

    private static AMCMModel loadModel(InputStream stream) throws IOException {
        if(stream != null){
            List<IAMCMShapes> shapes = new ArrayList<>();
            AMCMTexture modelTexture = AMCMTexture.MISSING;
            for(String line : IOUtils.readLines(stream, "UTF-8")){
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
            if(split.length == 25){
                Map<EnumFacing, int[]> textOffsets = new HashMap<>();
                textOffsets.put(EnumFacing.NORTH, new int[]{Integer.parseInt(split[13]), Integer.parseInt(split[14])});
                textOffsets.put(EnumFacing.SOUTH, new int[]{Integer.parseInt(split[15]), Integer.parseInt(split[16])});
                textOffsets.put(EnumFacing.WEST, new int[]{Integer.parseInt(split[17]), Integer.parseInt(split[18])});
                textOffsets.put(EnumFacing.EAST, new int[]{Integer.parseInt(split[19]), Integer.parseInt(split[20])});
                textOffsets.put(EnumFacing.UP, new int[]{Integer.parseInt(split[21]), Integer.parseInt(split[22])});
                textOffsets.put(EnumFacing.DOWN, new int[]{Integer.parseInt(split[23]), Integer.parseInt(split[24])});
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
