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
                        if (split.length == 4) {
                            mesh = new PixelMesh(split[1], Integer.parseInt(split[2]), UUID.fromString(split[3]));
                            mesh.setFileNameLoadedFrom(file.getName());
                        } else if (split.length == 3) {
                            mesh = new PixelMesh(split[1], Integer.parseInt(split[2]));
                            mesh.setFileNameLoadedFrom(file.getName());
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
            if(mesh.getFileNameLoadedFrom() != null){
                File oldFile = new File(meshDir, mesh.getFileNameLoadedFrom());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }
            File file = new File(meshDir, mesh.getId().toString() + ".txt");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            List<String> lines = new ArrayList<>();
            lines.add(String.format("Pixel Mesh~%s~%d~%s", mesh.getName(), mesh.getPixels().length, mesh.getId().toString()));
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
