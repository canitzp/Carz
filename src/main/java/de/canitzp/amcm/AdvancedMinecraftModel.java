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

    public static AMCMModel loadModel(InputStream stream) throws IOException {
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
        if(line.startsWith(":box> ")){ // name,width,height,depth,offX,offY,offZ,rotPointX,rotPointY,rotPointZ,rotAngleX,rotAngleY,rotAngleZ,allTextureOffsets[...],flags
            String[] split = line.replace(":box> ", "").split(";");
            if(split.length >= 25){
                AMCMBox box = new AMCMBox(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]))
                        .setOffset(parseFloat(split[4]), parseFloat(split[5]), parseFloat(split[6]))
                        .setRotationPoint(parseFloat(split[7]), parseFloat(split[8]), parseFloat(split[9]))
                        .setRotateAngle(parseFloat(split[10]), parseFloat(split[11]), parseFloat(split[12]))
                        .setName(split[0]).setTexture(texture)
                        .setTextureOffset(EnumFacing.NORTH, Integer.parseInt(split[13]), Integer.parseInt(split[14]))
                        .setTextureOffset(EnumFacing.SOUTH, Integer.parseInt(split[15]), Integer.parseInt(split[16]))
                        .setTextureOffset(EnumFacing.WEST, Integer.parseInt(split[17]), Integer.parseInt(split[18]))
                        .setTextureOffset(EnumFacing.EAST, Integer.parseInt(split[19]), Integer.parseInt(split[20]))
                        .setTextureOffset(EnumFacing.UP, Integer.parseInt(split[21]), Integer.parseInt(split[22]))
                        .setTextureOffset(EnumFacing.DOWN, Integer.parseInt(split[23]), Integer.parseInt(split[24]));
                if(split.length == 26){
                    box.setFlags(split[25]);
                }
                shapes.add(box);
            }
        } else if(line.startsWith(":tri> ")){ // name,width,height,depth,offX,offY,offZ,rotPointX,rotPointY,rotPointZ,rotAngleX,rotAngleY,rotAngleZ,allTextureOffsets[...],flags
            String[] split = line.replace(":tri> ", "").split(";");
            if(split.length >= 26){
                AMCMTriangle triangle = new AMCMTriangle(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]),
                        new AMCMShapeVar<>(Integer.parseInt(split[23]), Integer.parseInt(split[24]), Integer.parseInt(split[25])))
                        .setOffset(parseFloat(split[4]), parseFloat(split[5]), parseFloat(split[6]))
                        .setRotationPoint(parseFloat(split[7]), parseFloat(split[8]), parseFloat(split[9]))
                        .setRotateAngle(parseFloat(split[10]), parseFloat(split[11]), parseFloat(split[12]))
                        .setName(split[0]).setTexture(texture)
                        .setTextureOffset(EnumFacing.NORTH, Integer.parseInt(split[13]), Integer.parseInt(split[14]))
                        .setTextureOffset(EnumFacing.SOUTH, Integer.parseInt(split[15]), Integer.parseInt(split[16]))
                        .setTextureOffset(EnumFacing.WEST, Integer.parseInt(split[17]), Integer.parseInt(split[18]))
                        .setTextureOffset(EnumFacing.EAST, Integer.parseInt(split[19]), Integer.parseInt(split[20]))
                        .setTextureOffset(EnumFacing.DOWN, Integer.parseInt(split[21]), Integer.parseInt(split[22]));
                if(split.length == 27){
                    triangle.setFlags(split[26]);
                }
                shapes.add(triangle);
            }
        }
    }

    public static float parseFloat(String s){
        return Float.parseFloat(s.replace(",", "."));
    }

}
