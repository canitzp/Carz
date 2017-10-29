package de.canitzp.amcm;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public abstract class AMCMModel<T extends AMCMModel> {

    private List<IAMCMShapes> shapes = new ArrayList<>();
    private AMCMTexture texture = AMCMTexture.MISSING.setForceTexture(true);

    public T setTexture(AMCMTexture texture){
        if(texture != null){
            this.texture = texture;
        }
        return (T) this;
    }

    public abstract void createModel(List<IAMCMShapes> shapes);

    public void render(float scale){
        this.texture.bind();
        if(this.shapes.isEmpty()){
            this.createModel(this.shapes);
        }
        for(IAMCMShapes shape : this.shapes){
            shape.render(scale);
        }
    }

    public AMCMTexture getTexture() {
        return texture;
    }

    public List<IAMCMShapes> getShapes() {
        return shapes;
    }
}
