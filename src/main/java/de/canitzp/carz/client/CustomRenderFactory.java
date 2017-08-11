package de.canitzp.carz.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * @author canitzp
 */
public class CustomRenderFactory<T extends Entity> implements IRenderFactory<T> {

    private Class<? extends Render> renderClass;

    public CustomRenderFactory(Class<? extends Render> renderClass){
        this.renderClass = renderClass;
    }

    @Override
    public Render<? super T> createRenderFor(RenderManager manager) {
        Render<T> render = null;
        try {
            render = this.renderClass.getDeclaredConstructor(RenderManager.class).newInstance(manager);
            if(render instanceof IResourceManagerReloadListener){
                ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener((IResourceManagerReloadListener) render);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return render;
    }

}
