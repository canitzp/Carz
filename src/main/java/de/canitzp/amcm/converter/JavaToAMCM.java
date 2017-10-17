package de.canitzp.amcm.converter;

import de.canitzp.amcm.AMCMBox;
import de.canitzp.amcm.AMCMDefaultShape;
import de.canitzp.amcm.AdvancedMinecraftModel;
import de.canitzp.amcm.IAMCMShapes;
import net.minecraft.util.EnumFacing;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.canitzp.amcm.AdvancedMinecraftModel.*;

/**
 * @author canitzp
 */
public class JavaToAMCM {

    public static void main(String[] args){
        try {
            convertJavaToAMCM(new File(args[0]), new File(args[0] + ".amcm"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void convertJavaToAMCM(File javaFile, File outputFile) throws IOException {
        if(javaFile != null && javaFile.getName().endsWith(".java")){
            List<IAMCMShapes> shapes = new ArrayList<>();
            AMCMDefaultShape currentShape = null;
            int textureX = 0, textureY = 0;
            for(String line : FileUtils.readLines(javaFile, "UTF-8")){
                if(line.contains("this.") && !line.contains("render(")){
                    if(line.contains("ModelRenderer(this, ")){
                        if(currentShape != null){
                            shapes.add(currentShape);
                        }
                        currentShape = new AMCMBox(0, 0, 0);
                        String[] split = line.split("=");
                        currentShape.setName(split[0].substring(split[0].indexOf(".") + 1, split[0].length() -1));
                        split = split[1].replace(" ", "").replace("newModelRenderer(this,", "").replace(");", "").split(",");
                        textureX = Integer.parseInt(split[0]);
                        textureY = Integer.parseInt(split[1]);
                    }
                    if(currentShape != null) {
                        if (line.contains(".setRotationPoint(")) {
                            String[] split = line.substring(line.indexOf("(") + 1, line.indexOf(")")).replace(" ", "").replace("F", "").split(",");
                            currentShape.setRotationPoint(parseFloat(split[0]), parseFloat(split[1]), parseFloat(split[2]));
                        } else if (line.contains(".addBox(")) {
                            String[] split = line.substring(line.indexOf("(") + 1, line.indexOf(")")).replace(" ", "").replace("F", "").split(",");
                            currentShape.setOffset(parseFloat(split[0]), parseFloat(split[1]), parseFloat(split[2]));
                            currentShape.setDimension(Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]));
                            generateTechneTextures(textureX, textureY, currentShape);
                        }else if(line.contains("this.setRotateAngle(")) {
                            String[] split = line.substring(line.indexOf("(") + 1, line.indexOf(")")).replace(" ", "").replace("F", "").split(",");
                            currentShape.setRotateAngle(parseFloat(split[1]), parseFloat(split[2]), parseFloat(split[3]));
                        }
                    }
                }
            }
            if(currentShape != null){
                shapes.add(currentShape);
            }
            List<String> outLines = new ArrayList<>();
            outLines.add("AdvancedMinecraftModel");
            for(IAMCMShapes shape : shapes){
                if(shape instanceof AMCMBox){
                    AMCMBox box = (AMCMBox) shape;
                    outLines.add(String.format(":box> %s;%d;%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%f;%s",
                            box.name, box.width, box.height, box.depth,
                            box.offset.x, box.offset.y, box.offset.z,
                            box.rotationPoint.x, box.rotationPoint.y, box.rotationPoint.z,
                            box.rotationAngle.x, box.rotationAngle.y, box.rotationAngle.z,
                            box.getTextureStringForFile(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.UP, EnumFacing.DOWN)));
                }
            }
            FileUtils.writeLines(outputFile, outLines);
        }
    }

    private static void generateTechneTextures(int textOffsetX, int textOffsetY, AMCMDefaultShape shape){
        shape.setTextureOffset(EnumFacing.NORTH, textOffsetX + shape.depth, textOffsetY + shape.depth);
        shape.setTextureOffset(EnumFacing.SOUTH, textOffsetX + (2*shape.depth) + shape.width, textOffsetY + shape.depth);
        shape.setTextureOffset(EnumFacing.WEST, textOffsetX, textOffsetY);
        shape.setTextureOffset(EnumFacing.EAST, textOffsetX + shape.width + shape.depth, textOffsetY + shape.depth);
        shape.setTextureOffset(EnumFacing.DOWN, textOffsetX + shape.depth, textOffsetY);
        shape.setTextureOffset(EnumFacing.UP, textOffsetX + shape.width + shape.depth, textOffsetY);
    }

}
