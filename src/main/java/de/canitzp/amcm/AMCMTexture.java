package de.canitzp.amcm;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class AMCMTexture {

    public static final AMCMTexture MISSING = new AMCMTexture(null, 0, 0).setForceTexture(true);

    private ResourceLocation textureLocation;
    private int textureWidth, textureHeight;
    private boolean forceTexture = false;

    public AMCMTexture(ResourceLocation textureLocation, int textureWidth, int textureHeight) {
        this.textureLocation = textureLocation;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public ResourceLocation getTextureLocation() {
        return textureLocation;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public AMCMTexture setForceTexture(boolean force){
        this.forceTexture = force;
        return this;
    }

    public AMCMTexture bind(){
        if(this.textureLocation != null){
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.textureLocation);
        } else if (forceTexture){
            GlStateManager.bindTexture(TextureUtil.MISSING_TEXTURE.getGlTextureId());
        }
        return this;
    }
}
