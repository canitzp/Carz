package de.canitzp.carz.client.models.signs;

import de.canitzp.carz.Carz;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class ModelRoadSignTriangle extends ModelRoadSign {

    public ModelRenderer pole;
    public ModelRenderer lower_sign;
    public ModelRenderer upper_sign1;
    public ModelRenderer upper_sign2;
    public ModelRenderer upper_sign4;
    public ModelRenderer upper_sign6;
    public ModelRenderer upper_sign7;
    public ModelRenderer upper_sign8;
    public ModelRenderer upper_sign9;
    public ModelRenderer upper_sign10;
    public ModelRenderer upper_sign11;
    public ModelRenderer upper_sign12;
    public ModelRenderer upper_sign13;
    public ModelRenderer upper_sign14;
    public ModelRenderer upper_sign15;
    public ModelRenderer upper_sign16;
    public ModelRenderer upper_sign3;
    public ModelRenderer upper_sign5;

    public ModelRoadSignTriangle() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.upper_sign1 = new ModelRenderer(this, 62, 0);
        this.upper_sign1.setRotationPoint(0.0F, -39.0F, -1.0F);
        this.upper_sign1.addBox(-1.0F, -1.0F, -1.0F, 2, 1, 1, 0.0F);
        this.upper_sign4 = new ModelRenderer(this, 10, 5);
        this.upper_sign4.setRotationPoint(0.0F, -35.0F, -1.0F);
        this.upper_sign4.addBox(-4.0F, 0.0F, -1.0F, 8, 1, 1, 0.0F);
        this.upper_sign14 = new ModelRenderer(this, 10, 44);
        this.upper_sign14.setRotationPoint(0.0F, -19.0F, -1.0F);
        this.upper_sign14.addBox(-14.0F, 0.0F, -1.0F, 28, 2, 1, 0.0F);
        this.upper_sign16 = new ModelRenderer(this, 10, 52);
        this.upper_sign16.setRotationPoint(0.0F, -15.0F, -1.0F);
        this.upper_sign16.addBox(-14.0F, 0.0F, -1.0F, 28, 1, 1, 0.0F);
        this.upper_sign3 = new ModelRenderer(this, 26, 0);
        this.upper_sign3.setRotationPoint(0.0F, -37.0F, -1.0F);
        this.upper_sign3.addBox(-3.0F, 0.0F, -1.0F, 6, 2, 1, 0.0F);
        this.upper_sign5 = new ModelRenderer(this, 10, 7);
        this.upper_sign5.setRotationPoint(0.0F, -34.0F, -1.0F);
        this.upper_sign5.addBox(-5.0F, 0.0F, -1.0F, 10, 2, 1, 0.0F);
        this.lower_sign = new ModelRenderer(this, 0, 65);
        this.lower_sign.setRotationPoint(0.0F, -4.0F, -2.0F);
        this.lower_sign.addBox(-13.0F, -4.5F, 0.0F, 26, 12, 1, 0.0F);
        this.upper_sign6 = new ModelRenderer(this, 10, 12);
        this.upper_sign6.setRotationPoint(0.0F, -32.0F, -1.0F);
        this.upper_sign6.addBox(-6.0F, 0.0F, -1.0F, 12, 2, 1, 0.0F);
        this.pole = new ModelRenderer(this, 0, 0);
        this.pole.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.pole.addBox(-1.0F, -56.0F, -1.0F, 2, 56, 2, 0.0F);
        this.upper_sign7 = new ModelRenderer(this, 10, 16);
        this.upper_sign7.setRotationPoint(0.0F, -30.0F, -1.0F);
        this.upper_sign7.addBox(-7.0F, 0.0F, -1.0F, 14, 1, 1, 0.0F);
        this.upper_sign9 = new ModelRenderer(this, 10, 24);
        this.upper_sign9.setRotationPoint(0.0F, -27.0F, -1.0F);
        this.upper_sign9.addBox(-9.0F, 0.0F, -1.0F, 18, 2, 1, 0.0F);
        this.upper_sign10 = new ModelRenderer(this, 10, 28);
        this.upper_sign10.setRotationPoint(0.0F, -25.0F, -1.0F);
        this.upper_sign10.addBox(-10.0F, 0.0F, -1.0F, 20, 1, 1, 0.0F);
        this.upper_sign2 = new ModelRenderer(this, 68, 0);
        this.upper_sign2.setRotationPoint(0.0F, -39.0F, -1.0F);
        this.upper_sign2.addBox(-2.0F, 0.0F, -1.0F, 4, 2, 1, 0.0F);
        this.upper_sign12 = new ModelRenderer(this, 10, 36);
        this.upper_sign12.setRotationPoint(0.0F, -22.0F, -1.0F);
        this.upper_sign12.addBox(-12.0F, 0.0F, -1.0F, 24, 2, 1, 0.0F);
        this.upper_sign13 = new ModelRenderer(this, 10, 40);
        this.upper_sign13.setRotationPoint(0.0F, -20.0F, -1.0F);
        this.upper_sign13.addBox(-13.0F, 0.0F, -1.0F, 26, 1, 1, 0.0F);
        this.upper_sign8 = new ModelRenderer(this, 10, 20);
        this.upper_sign8.setRotationPoint(0.0F, -29.0F, -1.0F);
        this.upper_sign8.addBox(-8.0F, 0.0F, -1.0F, 16, 2, 1, 0.0F);
        this.upper_sign11 = new ModelRenderer(this, 10, 32);
        this.upper_sign11.setRotationPoint(0.0F, -24.0F, -1.0F);
        this.upper_sign11.addBox(-11.0F, 0.0F, -1.0F, 22, 2, 1, 0.0F);
        this.upper_sign15 = new ModelRenderer(this, 10, 48);
        this.upper_sign15.setRotationPoint(0.0F, -17.0F, -1.0F);
        this.upper_sign15.addBox(-15.0F, 0.0F, -1.0F, 30, 2, 1, 0.0F);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Carz.MODID, "textures/blocks/signs/triangle.png");
    }
}
