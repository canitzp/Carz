package de.canitzp.carz.client;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author canitzp
 */
public class PixelMeshParser {

    private File[] files = new File[0];

    public PixelMeshParser(File... files) {
        this.files = files;
    }

    public List<PixelMesh> parseFiles() throws IOException {
        List<PixelMesh> meshes = new ArrayList<>();
        for (File file : this.files) {
            PixelMesh mesh = null;
            for (String line : FileUtils.readLines(file, "UTF-8")) {
                if (line.startsWith("Pixel Mesh")) {
                    String[] split = line.split("~");
                    if (split.length == 4) {
                        mesh = new PixelMesh(split[1], Integer.parseInt(split[2]), UUID.fromString(split[3]));
                    } else if (split.length == 3) {
                        mesh = new PixelMesh(split[1], Integer.parseInt(split[2]));
                    }
                }
            }
            if (mesh != null) {
                meshes.add(mesh);
            }
        }
        return meshes;
    }

}
