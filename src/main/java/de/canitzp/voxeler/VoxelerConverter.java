package de.canitzp.voxeler;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class VoxelerConverter {

    public static final char SPLIT_CHAR = ';';

    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        List<Out> finished = new ArrayList<>();
        Out out = null;
        for(String line : FileUtils.readLines(file, "UTF-8")){
            if(line.contains("this.") && !line.contains("render(")){
                if(line.contains("ModelRenderer(this, ")){
                    String[] s1 = line.split("=");
                    String[] split = s1[1].replace(" ", "").replace("newModelRenderer(this,", "").replace(");", "").split(",");
                    if(out != null){
                        finished.add(out);
                    }
                    out = new Out();
                    out.name = s1[0].substring(s1[0].indexOf(".") + 1).replace(" ", "");
                    if(Voxeler.areNumbers(split[0], split[1])){
                        out.textureX = Integer.parseInt(split[0]);
                        out.textureY = Integer.parseInt(split[1]);
                    }
                } else if(line.contains(".setRotationPoint(")){
                    String[] split = line.substring(line.indexOf("(") + 1, line.indexOf(")")).replace(" ", "").replace("F", "").split(",");
                    if(Voxeler.areNumbers(split[0], split[1], split[2])){
                        out.rotationXPoint = Float.parseFloat(split[0]);
                        out.rotationYPoint = Float.parseFloat(split[1]);
                        out.rotationZPoint = Float.parseFloat(split[2]);
                    }
                } else if(line.contains(".addBox(")){
                    String[] split = line.substring(line.indexOf("(") + 1, line.indexOf(")")).replace(" ", "").replace("F", "").split(",");
                    if(Voxeler.areNumbers(split[0], split[1], split[2], split[3], split[4], split[5], split[6])){
                        OutBox box = new OutBox();
                        box.offsetX = Float.parseFloat(verifyFloat(split[0]));
                        box.offsetY = Float.parseFloat(verifyFloat(split[1]));
                        box.offsetZ = Float.parseFloat(verifyFloat(split[2]));
                        box.width = Integer.parseInt(split[3]);
                        box.height = Integer.parseInt(split[4]);
                        box.depth = Integer.parseInt(split[5]);
                        box.scaleFactor = Float.parseFloat(verifyFloat(split[6]));
                        out.boxes.add(box);
                    }
                } else if(line.contains("this.setRotateAngle(")){
                    String[] split = line.substring(line.indexOf("(") + 1, line.indexOf(")")).replace(" ", "").replace("F", "").split(",");
                    if(Voxeler.areNumbers(split[1], split[2], split[3])){
                        out.rotationAngleX = Float.parseFloat(split[1]);
                        out.rotationAngleY = Float.parseFloat(split[2]);
                        out.rotationAngleZ = Float.parseFloat(split[3]);
                    }
                }
            }
        }
        if(out != null){
            finished.add(out);
        }
        writeVoxFile(new File(file.getParent(), file.getName().replace(".java", ".vox")), 128, 128, finished);
    }

    private static void writeVoxFile(File file, int textWidth, int textHeight, List<Out> outs) throws IOException {
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();
        List<String> lines = new ArrayList<>();
        lines.add("Voxeler File Format" + SPLIT_CHAR + textWidth + SPLIT_CHAR + textHeight);
        for(Out out : outs){
            lines.add(out.compile());
            for(OutBox box : out.boxes){
                lines.add(box.compile());
            }
        }
        FileUtils.writeLines(file, "UTF-8", lines);
    }

    private static class Out{
        String name;
        List<OutBox> boxes = new ArrayList<>();
        List<Out> childs = new ArrayList<>();
        int textureX, textureY;
        float rotationXPoint, rotationYPoint, rotationZPoint, rotationAngleX = 0.0F, rotationAngleY = 0.0F, rotationAngleZ = 0.0F;
        String compile(){
            return String.format(": %s~~%d~~%d~~%s~~%s~~%s~~%s~~%s~~%s", name, textureX, textureY, floatToString(rotationXPoint),
                    floatToString(rotationYPoint), floatToString(rotationZPoint), floatToString(rotationAngleX), floatToString(rotationAngleY), floatToString(rotationAngleZ)).replace("~~", String.valueOf(SPLIT_CHAR));
        }
    }

    private static class OutBox{
        int width, height, depth;
        float offsetX, offsetY, offsetZ, scaleFactor;
        String compile(){
            return String.format(":: %s~~%s~~%s~~%d~~%d~~%d~~%s",
                    floatToString(offsetX), floatToString(offsetY), floatToString(offsetZ), width, height, depth, floatToString(scaleFactor)).replace("~~", String.valueOf(SPLIT_CHAR));
        }
    }

    private static String floatToString(float f){
        return new DecimalFormat("#.###").format(f);
    }

    private static String verifyFloat(String f){
        if(f.endsWith("E-16")){
            return "0";
        }
        return f;
    }

}
