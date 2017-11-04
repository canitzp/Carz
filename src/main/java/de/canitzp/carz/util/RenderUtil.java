package de.canitzp.carz.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
public class RenderUtil {

    private static final Map<Pair<String, String>, Integer> cachedLayeredTextures = new HashMap<>();

    public static void bindLayeredTexture(ResourceLocation layer1, ResourceLocation layer2, int colorLayer1, int colorLayer2, boolean recalculate) throws IOException {
        for (Map.Entry<Pair<String, String>, Integer> entry : cachedLayeredTextures.entrySet()) {
            if (entry.getKey().getLeft().equals(layer1.toString()) && entry.getKey().getRight().equals(layer2.toString())) {
                if (recalculate) {
                    TextureUtil.deleteTexture(entry.getValue());
                    break;
                }
                GlStateManager.bindTexture(entry.getValue());
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

        int id = TextureUtil.glGenTextures();
        TextureUtil.uploadTextureImage(id, texture);
        cachedLayeredTextures.put(Pair.of(layer1.toString(), layer2.toString()), id);
    }

    public static void color(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0F;
        float red = (float) (hex >> 16 & 255) / 255.0F;
        float green = (float) (hex >> 8 & 255) / 255.0F;
        float blue = (float) (hex & 255) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);
    }

}
