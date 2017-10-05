package de.canitzp.voxeler;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author canitzp
 */
public class VoxelBase extends ModelBase {

    public VoxelBase(int textureWidth, int textureHeight){
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        for(ModelRenderer modelRenderer : this.boxList){
            modelRenderer.render(scale);
        }
    }
}
