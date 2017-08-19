package de.canitzp.carz.client;

import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author canitzp
 */
public class PixelMeshParser {

    public static Map<UUID, PixelMesh> readMeshFile(@Nullable File... files) throws IOException {
        Map<UUID, PixelMesh> meshes = new ConcurrentHashMap<>();
        if(files != null){
            for (File file : files) {
                PixelMesh mesh = null;
                int addingRow = 0;
                for (String line : FileUtils.readLines(file, "UTF-8")) {
                    if (line.startsWith("Pixel Mesh")) {
                        String[] split = line.split("~");
                        if (split.length == 7) {
                            mesh = new PixelMesh(split[1], Integer.parseInt(split[2]), UUID.fromString(split[5]), UUID.fromString(split[6]));
                            mesh.setOffset(Integer.parseInt(split[3]), Integer.parseInt(split[4]));
                        }
                    } else if(mesh != null){
                        if(line.contains(",") && line.contains(";")){
                            String[] splitPixel = line.split(";");
                            if(splitPixel.length == mesh.getPixels().length){
                                List<Pixel> pixels = new ArrayList<>();
                                for(String pxString : splitPixel){
                                    pixels.add(new Pixel(pxString));
                                }
                                mesh.setLine(addingRow++, pixels.toArray(new Pixel[0]));
                            }
                        }
                    }
                }
                if (mesh != null) {
                    meshes.put(mesh.getId(), mesh);
                }
            }
        }
        return meshes;
    }

    public static void writeMeshFile(File meshDir, Collection<PixelMesh> meshes) throws IOException {
        for (PixelMesh mesh : meshes) {
            File file = new File(meshDir, mesh.getId().toString() + ".mesh");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            List<String> lines = new ArrayList<>();
            lines.add(String.format("Pixel Mesh~%s~%d~%d~%d~%s~%s", mesh.getName(), mesh.getPixels().length, mesh.getOffsetX(), mesh.getOffsetY(), mesh.getId().toString(), mesh.getOwner().toString()));
            for(Pixel[] pixels : mesh.getPixels()){
                StringBuilder builder = new StringBuilder();
                for(Pixel pixel : pixels){
                    builder.append(pixel.writeString());
                }
                lines.add(builder.toString());
            }
            FileUtils.writeLines(file, "UTF-8", lines);
        }
    }

}
