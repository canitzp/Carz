package de.canitzp.carz.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author canitzp
 */
@SuppressWarnings("WeakerAccess")
public class ModelSportscar extends ModelBase {

    public ModelRenderer bottom_plate;
    public ModelRenderer sidepanel_left;
    public ModelRenderer frontpanel;
    public ModelRenderer frontpanel_small1;
    public ModelRenderer engine_cowling;
    public ModelRenderer backpanel;
    public ModelRenderer backpanel_small1;
    public ModelRenderer backpanel_small2;
    public ModelRenderer sidepanel_right;
    public ModelRenderer frontpanel_small2;
    public ModelRenderer air_intake;
    public ModelRenderer windscreen1;
    public ModelRenderer windscreen2;
    public ModelRenderer windscreen4;
    public ModelRenderer rooftop;
    public ModelRenderer backwindscreen1;
    public ModelRenderer backwindscreen2;
    public ModelRenderer backwindscreen3;
    public ModelRenderer windscreen3;
    public ModelRenderer axle_front;
    public ModelRenderer axle_back;
    public ModelRenderer tyre1_front_left;
    public ModelRenderer tyre2_front_left;
    public ModelRenderer tyre1_back_left;
    public ModelRenderer tyre2_back_left;
    public ModelRenderer tyre1_back_right;
    public ModelRenderer tyre2_back_right;
    public ModelRenderer tyre1_front_right;
    public ModelRenderer tyre2_front_right;

    public ModelSportscar() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.tyre1_front_right = new ModelRenderer(this, 0, 96);
        this.tyre1_front_right.setRotationPoint(-13.0F, 21.5F, -9.5F);
        this.tyre1_front_right.addBox(-0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.tyre2_back_right = new ModelRenderer(this, 0, 107);
        this.tyre2_back_right.setRotationPoint(-13.0F, 21.5F, 9.5F);
        this.tyre2_back_right.addBox(-0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.setRotateAngle(tyre2_back_right, 1.5707963267948966F, 0.0F, 0.0F);
        this.engine_cowling = new ModelRenderer(this, 68, 13);
        this.engine_cowling.setRotationPoint(-11.0F, 8.0F, -13.0F);
        this.engine_cowling.addBox(-7.494005416219807E-16F, 0.0F, 0.0F, 22, 1, 8, 0.0F);
        this.windscreen2 = new ModelRenderer(this, 0, 54);
        this.windscreen2.setRotationPoint(-12.0F, 1.0F, -5.0F);
        this.windscreen2.addBox(-0.0F, 0.0F, 0.0F, 24, 4, 1, 0.0F);
        this.air_intake = new ModelRenderer(this, 48, 18);
        this.air_intake.setRotationPoint(-2.0F, 7.0F, -11.5F);
        this.air_intake.addBox(-0.0F, 0.0F, 0.0F, 4, 1, 4, 0.0F);
        this.tyre2_front_right = new ModelRenderer(this, 0, 107);
        this.tyre2_front_right.setRotationPoint(-13.0F, 21.5F, -9.5F);
        this.tyre2_front_right.addBox(-0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.setRotateAngle(tyre2_front_right, 1.5707963267948966F, 0.0F, 0.0F);
        this.axle_front = new ModelRenderer(this, 0, 84);
        this.axle_front.setRotationPoint(0.0F, 21.0F, -10.0F);
        this.axle_front.addBox(-12.0F, 0.0F, 0.0F, 24, 1, 1, 0.0F);
        this.windscreen1 = new ModelRenderer(this, 0, 49);
        this.windscreen1.setRotationPoint(-12.0F, 5.0F, -6.0F);
        this.windscreen1.addBox(-0.0F, 0.0F, 0.0F, 24, 3, 1, 0.0F);
        this.frontpanel = new ModelRenderer(this, 83, 7);
        this.frontpanel.setRotationPoint(-11.0F, 8.0F, -14.0F);
        this.frontpanel.addBox(-7.494005416219807E-16F, 0.0F, 0.0F, 22, 12, 1, 0.0F);
        this.windscreen4 = new ModelRenderer(this, 0, 60);
        this.windscreen4.setRotationPoint(0.0F, -5.0F, -3.0F);
        this.windscreen4.addBox(-12.0F, 0.0F, 0.0F, 24, 3, 1, 0.0F);
        this.frontpanel_small2 = new ModelRenderer(this, 48, 14);
        this.frontpanel_small2.setRotationPoint(-9.0F, 15.0F, -16.0F);
        this.frontpanel_small2.addBox(1.0F, -3.0F, 0.0F, 16, 4, 1, 0.0F);
        this.tyre1_front_left = new ModelRenderer(this, 0, 96);
        this.tyre1_front_left.setRotationPoint(12.0F, 21.5F, -9.5F);
        this.tyre1_front_left.addBox(-0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.tyre1_back_left = new ModelRenderer(this, 0, 96);
        this.tyre1_back_left.setRotationPoint(12.0F, 21.5F, 9.5F);
        this.tyre1_back_left.addBox(-0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.tyre2_front_left = new ModelRenderer(this, 0, 107);
        this.tyre2_front_left.setRotationPoint(12.0F, 21.5F, -9.5F);
        this.tyre2_front_left.addBox(-0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.setRotateAngle(tyre2_front_left, 1.5707963267948966F, 0.0F, 0.0F);
        this.frontpanel_small1 = new ModelRenderer(this, 84, 8);
        this.frontpanel_small1.setRotationPoint(-10.0F, 10.0F, -15.0F);
        this.frontpanel_small1.addBox(-7.494005416219807E-16F, 0.0F, 0.0F, 20, 8, 1, 0.0F);
        this.backwindscreen3 = new ModelRenderer(this, 0, 74);
        this.backwindscreen3.setRotationPoint(0.0F, 4.0F, 12.0F);
        this.backwindscreen3.addBox(-12.0F, 0.0F, 0.0F, 24, 4, 1, 0.0F);
        this.rooftop = new ModelRenderer(this, 48, 38);
        this.rooftop.setRotationPoint(-12.0F, -5.0F, -2.0F);
        this.rooftop.addBox(-0.0F, 0.0F, 0.0F, 24, 1, 12, 0.0F);
        this.windscreen3 = new ModelRenderer(this, 0, 54);
        this.windscreen3.setRotationPoint(-12.0F, -2.0F, -4.0F);
        this.windscreen3.addBox(-0.0F, 0.0F, 0.0F, 24, 3, 1, 0.0F);
        this.backpanel_small2 = new ModelRenderer(this, 0, 29);
        this.backpanel_small2.setRotationPoint(-8.0F, 12.0F, 15.0F);
        this.backpanel_small2.addBox(-0.0F, 0.0F, 0.0F, 16, 5, 1, 0.0F);
        this.tyre2_back_left = new ModelRenderer(this, 0, 107);
        this.tyre2_back_left.setRotationPoint(12.0F, 21.5F, 9.5F);
        this.tyre2_back_left.addBox(-0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.setRotateAngle(tyre2_back_left, 1.5707963267948966F, 0.0F, 0.0F);
        this.axle_back = new ModelRenderer(this, 0, 86);
        this.axle_back.setRotationPoint(0.0F, 21.0F, 9.0F);
        this.axle_back.addBox(-12.0F, 0.0F, 0.0F, 24, 1, 1, 0.0F);
        this.sidepanel_left = new ModelRenderer(this, 49, 0);
        this.sidepanel_left.setRotationPoint(-11.0F, 15.0F, -14.0F);
        this.sidepanel_left.addBox(-7.494005416219807E-16F, -7.0F, 0.0F, 28, 12, 1, 0.0F);
        this.setRotateAngle(sidepanel_left, 0.0F, -1.5707963267948966F, 0.0F);
        this.backpanel_small1 = new ModelRenderer(this, 81, 0);
        this.backpanel_small1.setRotationPoint(-10.0F, 10.0F, 14.0F);
        this.backpanel_small1.addBox(-0.0F, 0.0F, 0.0F, 20, 9, 1, 0.0F);
        this.backwindscreen1 = new ModelRenderer(this, 0, 68);
        this.backwindscreen1.setRotationPoint(0.0F, -4.0F, 10.0F);
        this.backwindscreen1.addBox(-12.0F, 0.0F, 0.0F, 24, 4, 1, 0.0F);
        this.sidepanel_right = new ModelRenderer(this, 0, 36);
        this.sidepanel_right.setRotationPoint(12.0F, 8.0F, -14.0F);
        this.sidepanel_right.addBox(-0.0F, 0.0F, 0.0F, 28, 12, 1, 0.0F);
        this.setRotateAngle(sidepanel_right, 0.0F, -1.5707963267948966F, 0.0F);
        this.tyre1_back_right = new ModelRenderer(this, 0, 96);
        this.tyre1_back_right.setRotationPoint(-13.0F, 21.5F, 9.5F);
        this.tyre1_back_right.addBox(-0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.bottom_plate = new ModelRenderer(this, -28, 0);
        this.bottom_plate.setRotationPoint(-12.0F, 20.0F, -14.0F);
        this.bottom_plate.addBox(-7.494005416219807E-16F, 0.0F, 0.0F, 24, 1, 28, 0.0F);
        this.backpanel = new ModelRenderer(this, 78, 20);
        this.backpanel.setRotationPoint(-11.0F, 8.0F, 13.0F);
        this.backpanel.addBox(-0.0F, 0.0F, 0.0F, 22, 12, 1, 0.0F);
        this.backwindscreen2 = new ModelRenderer(this, 0, 54);
        this.backwindscreen2.setRotationPoint(0.0F, 0.0F, 11.0F);
        this.backwindscreen2.addBox(-12.0F, 0.0F, 0.0F, 24, 4, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.tyre1_front_right.render(f5);
        this.tyre2_back_right.render(f5);
        this.engine_cowling.render(f5);
        this.windscreen2.render(f5);
        this.air_intake.render(f5);
        this.tyre2_front_right.render(f5);
        this.axle_front.render(f5);
        this.windscreen1.render(f5);
        this.frontpanel.render(f5);
        this.windscreen4.render(f5);
        this.frontpanel_small2.render(f5);
        this.tyre1_front_left.render(f5);
        this.tyre1_back_left.render(f5);
        this.tyre2_front_left.render(f5);
        this.frontpanel_small1.render(f5);
        this.backwindscreen3.render(f5);
        this.rooftop.render(f5);
        this.windscreen3.render(f5);
        this.backpanel_small2.render(f5);
        this.tyre2_back_left.render(f5);
        this.axle_back.render(f5);
        this.sidepanel_left.render(f5);
        this.backpanel_small1.render(f5);
        this.backwindscreen1.render(f5);
        this.sidepanel_right.render(f5);
        this.tyre1_back_right.render(f5);
        this.bottom_plate.render(f5);
        this.backpanel.render(f5);
        this.backwindscreen2.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

}