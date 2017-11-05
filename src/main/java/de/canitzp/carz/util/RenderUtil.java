package de.canitzp.carz.util;

import de.canitzp.carz.client.Pixel;
import de.canitzp.carz.client.PixelMesh;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author canitzp
 */
public class RenderUtil {

    private static final Map<UUID, Integer> cachedLayeredTextures = new HashMap<>();

    public static void bindLayeredTexture(UUID carId, ResourceLocation layer1, ResourceLocation layer2, int colorLayer1, int colorLayer2, @Nullable PixelMesh mesh, List<Pair<Integer, Integer>> meshCoords, boolean recalculate) throws IOException {
        if(cachedLayeredTextures.containsKey(carId)){
            if (recalculate) {
                TextureUtil.deleteTexture(cachedLayeredTextures.get(carId));
                // We don't need to remove the map entry, since it gets overridden at the end of the method in any case
            } else {
                GlStateManager.bindTexture(cachedLayeredTextures.get(carId));
                return;
            }
        }

        IResource layer1Resource = Minecraft.getMinecraft().getResourceManager().getResource(layer1);
        IResource layer2Resource = Minecraft.getMinecraft().getResourceManager().getResource(layer2);

        BufferedImage layer1Texture = TextureUtil.readBufferedImage(layer1Resource.getInputStream());
        for (int width = 0; width < layer1Texture.getWidth(); width++) {
            for (int height = 0; height < layer1Texture.getHeight(); height++) {
                int rgb = layer1Texture.getRGB(width, height);
                if ((colorLayer1 & 0x00FFFFFF) != 0xFFFFFF) {
                    layer1Texture.setRGB(width, height, MathHelper.multiplyColor(rgb, colorLayer1));
                }
            }
        }
        BufferedImage layer2Texture = TextureUtil.readBufferedImage(layer2Resource.getInputStream());
        for (int width = 0; width < layer2Texture.getWidth(); width++) {
            for (int height = 0; height < layer2Texture.getHeight(); height++) {
                int rgb = layer2Texture.getRGB(width, height);
                if ((colorLayer2 & 0x00FFFFFF) != 0xFFFFFF) {
                    layer2Texture.setRGB(width, height, MathHelper.multiplyColor(rgb, colorLayer2));
                }
            }
        }

        BufferedImage texture = new BufferedImage(layer1Texture.getWidth(), layer1Texture.getHeight(), BufferedImage.TYPE_INT_ARGB);
        texture.getGraphics().drawImage(layer1Texture, 0, 0, null);
        texture.getGraphics().drawImage(layer2Texture, 0, 0, null);

        if(mesh != null){
            BufferedImage meshImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Pixel[][] pixels1 = mesh.getPixels();
            for (int row = 0; row < pixels1.length; row++) {
                Pixel[] pixels = pixels1[row];
                for (int column = 0; column < pixels.length; column++) {
                    Pixel pixel = pixels[column];
                    if(pixel.isValid()){
                        meshImage.setRGB(column, row, pixel.toHex());
                    }
                }
            }
            for(Pair<Integer, Integer> coords : meshCoords){
                texture.getGraphics().drawImage(meshImage, coords.getLeft(), coords.getRight(), null);
            }
        }

        int id = TextureUtil.glGenTextures();
        TextureUtil.uploadTextureImage(id, texture);
        cachedLayeredTextures.put(carId, id);
    }

    public static void color(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0F;
        float red = (float) (hex >> 16 & 255) / 255.0F;
        float green = (float) (hex >> 8 & 255) / 255.0F;
        float blue = (float) (hex & 255) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void render2DBoundTexture(int x, int y, int textureX, int textureY, int width, int height, float scale) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0).tex(textureX * scale, (textureY + height) * scale).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0).tex((textureX + width) * scale, (textureY + height) * scale).endVertex();
        bufferbuilder.pos(x + width, y,0.0).tex((textureX + width) * scale, textureY * scale).endVertex();
        bufferbuilder.pos(x, y, 0.0).tex(textureX * scale, textureY * scale).endVertex();
        tessellator.draw();
    }

}
