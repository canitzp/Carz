package de.canitzp.amcm.converter;

import de.canitzp.amcm.AMCMBox;
import de.canitzp.amcm.AMCMModel;
import de.canitzp.amcm.IAMCMShapes;
import net.minecraft.util.EnumFacing;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class AMCMWriter {

    public static void writeToFile(File file, AMCMModel model) throws IOException {
        List<String> outLines = new ArrayList<>();
        outLines.add("AdvancedMinecraftModel");
        if(model.getTexture() != null && model.getTexture().getTextureLocation() != null){
            outLines.add(":res> " + model.getTexture().getTextureLocation().toString() + ";" + model.getTexture().getTextureWidth() + ";" + model.getTexture().getTextureHeight());
        }
        for(Object shape : model.getShapes()){
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
        FileUtils.writeLines(file, outLines);
    }

}
