package de.canitzp.voxeler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class VoxelBase<T extends VoxelBase> extends ModelBase {

    public List<VoxelPart> boxList = new ArrayList<>();

    private ResourceLocation texture;

    public VoxelBase(int textureWidth, int textureHeight){
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    /**
     * This method is for excluding specific parts with their name as identifier.
     * @param parts The names of the parts
     * @return The current object.
     */
    public VoxelBase<T> exclude(String... parts){
        List<VoxelPart> removeables = new ArrayList<>();
        for(VoxelPart voxelPart : this.boxList){
            for(String part : parts){
                if(part.equals(voxelPart.boxName)){
                    removeables.add(voxelPart);
                }
            }
        }
        this.boxList.removeAll(removeables);
        return this;
    }

    public VoxelBase<T> copy(){
        VoxelBase<T> voxelBase = new VoxelBase<>(this.textureWidth, this.textureHeight);
        voxelBase.boxList.addAll(this.boxList);
        voxelBase.swingProgress = this.swingProgress;
        voxelBase.isRiding = this.isRiding;
        voxelBase.isChild = this.isChild;
        return voxelBase;
    }

    public VoxelBase<T> setTexture(ResourceLocation texture){
        this.texture = texture;
        return this;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        boolean customTextureFlag = false;
        for(VoxelPart part : this.boxList){
            if(!part.hasCustomTexture() && customTextureFlag){
                Minecraft.getMinecraft().getTextureManager().bindTexture(this.getTexture());
            }
            part.render(scale);
            customTextureFlag = part.hasCustomTexture();
        }
    }
}
