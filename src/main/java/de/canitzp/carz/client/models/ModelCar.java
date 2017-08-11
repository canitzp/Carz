package de.canitzp.carz.client.models;

import de.canitzp.carz.entity.EntityCar;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class ModelCar extends ModelBase {

    public ModelRenderer Sportwagen_body;
    public ModelRenderer Achse_front;
    public ModelRenderer Achse_back;
    public ModelRenderer Rad_front_left;
    public ModelRenderer Rad_back_left;
    public ModelRenderer Rad_back_right;
    public ModelRenderer Rad_front_right;

    public ModelCar() {
        this.textureWidth = 64;
        this.textureHeight = 32;

        this.Sportwagen_body = new ModelRenderer(this, 0, 0);
        this.Sportwagen_body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Sportwagen_body.addBox(0.0F, 8.0F, 0.0F, 32, 8, 16);
        this.Achse_front = new ModelRenderer(this, 0, 0);
        this.Achse_front.setRotationPoint(5.0F, 16.0F, 0.0F);
        this.Achse_front.addBox(0.0F, 0.0F, 0.0F, 1, 1, 16);
        this.Achse_back = new ModelRenderer(this, 0, 0);
        this.Achse_back.setRotationPoint(25.0F, 16.0F, 0.0F);
        this.Achse_back.addBox(0.0F, 0.0F, 0.0F, 1, 1, 16);
        this.Rad_front_left = new ModelRenderer(this, 0, 0);
        this.Rad_front_left.setRotationPoint(4.0F, 15.0F, 16.0F);
        this.Rad_front_left.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1);
        this.Rad_back_left = new ModelRenderer(this, 0, 0);
        this.Rad_back_left.setRotationPoint(24.0F, 15.0F, 16.0F);
        this.Rad_back_left.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1);
        this.Rad_back_right = new ModelRenderer(this, 0, 0);
        this.Rad_back_right.setRotationPoint(24.0F, 15.0F, -1.0F);
        this.Rad_back_right.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1);
        this.Rad_front_right = new ModelRenderer(this, 0, 0);
        this.Rad_front_right.setRotationPoint(4.0F, 15.0F, -1.0F);
        this.Rad_front_right.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1);
    }

    @Override
    public void render(Entity car, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        //GlStateManager.rotate(90.0F, 0, 1, 0);
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, car);
        this.Sportwagen_body.render(scale);
        this.Achse_front.render(scale);
        this.Achse_back.render(scale);
        this.Rad_front_left.render(scale);
        this.Rad_back_left.render(scale);
        this.Rad_back_right.render(scale);
        this.Rad_front_right.render(scale);
        GlStateManager.disableBlend();
    }

    public void setRotationAngles(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}