package de.canitzp.carz.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * A bus exposing its inner parts - perfect for boundingbox-debugging
 * @author canitzp
 */
public class ModelNakedBus extends ModelBase {
    public ModelRenderer bottom_plate;
    public ModelRenderer left_side;
    public ModelRenderer front_plate;
    public ModelRenderer right_front_panel;
    public ModelRenderer rooftop_plate;
    public ModelRenderer back_plate;
    public ModelRenderer right_back_panel;
    public ModelRenderer right_middle_panel;
    public ModelRenderer vent;
    public ModelRenderer driverseat_base;
    public ModelRenderer driverseat_back;
    public ModelRenderer seat1_base;
    public ModelRenderer seat1_back;
    public ModelRenderer seat2_base;
    public ModelRenderer seat2_back;
    public ModelRenderer seat3_base;
    public ModelRenderer seat3_back;
    public ModelRenderer seat4_base;
    public ModelRenderer seat4_back;
    public ModelRenderer seat5_base;
    public ModelRenderer seat5_back;
    public ModelRenderer seat6_base;
    public ModelRenderer seat6_back;
    public ModelRenderer axle_front;
    public ModelRenderer axle_back;
    public ModelRenderer tyre1_left_front;
    public ModelRenderer tyre2_left_front;
    public ModelRenderer tyre1_left_back;
    public ModelRenderer tyre2_left_back;
    public ModelRenderer tyre1_right_back;
    public ModelRenderer tyre2_right_back;
    public ModelRenderer tyre1_right_front;
    public ModelRenderer tyre2_right_front;

    public ModelNakedBus() {
        this.textureWidth = 512;
        this.textureHeight = 512;
        this.seat1_base = new ModelRenderer(this, 0, 220);
        this.seat1_base.setRotationPoint(-15.0F, 18.0F, -23.0F);
        this.seat1_base.addBox(-8.0F, -2.0F, -8.0F, 16, 4, 16, 0.0F);
        this.seat3_base = new ModelRenderer(this, 0, 220);
        this.seat3_base.setRotationPoint(-15.0F, 18.0F, -2.0F);
        this.seat3_base.addBox(-8.0F, -2.0F, -8.0F, 16, 4, 16, 0.0F);
        this.seat6_base = new ModelRenderer(this, 0, 220);
        this.seat6_base.setRotationPoint(15.0F, 18.0F, 47.0F);
        this.seat6_base.addBox(-8.0F, -2.0F, -8.0F, 16, 4, 16, 0.0F);
        this.rooftop_plate = new ModelRenderer(this, 0, 144);
        this.rooftop_plate.setRotationPoint(0.0F, -13.0F, 0.0F);
        this.rooftop_plate.addBox(-24.0F, 0.0F, -56.0F, 48, 1, 112, 0.0F);
        this.vent = new ModelRenderer(this, 0, 176);
        this.vent.setRotationPoint(0.0F, -15.0F, 0.0F);
        this.vent.addBox(-12.0F, -2.0F, -16.0F, 24, 4, 32, 0.0F);
        this.right_back_panel = new ModelRenderer(this, 0, 37);
        this.right_back_panel.setRotationPoint(-24.0F, 4.0F, 40.0F);
        this.right_back_panel.addBox(0.0F, -16.0F, -16.0F, 1, 32, 32, 0.0F);
        this.seat4_base = new ModelRenderer(this, 0, 220);
        this.seat4_base.setRotationPoint(15.0F, 18.0F, -2.0F);
        this.seat4_base.addBox(-8.0F, -2.0F, -8.0F, 16, 4, 16, 0.0F);
        this.axle_front = new ModelRenderer(this, 0, 260);
        this.axle_front.setRotationPoint(0.0F, 21.0F, -28.0F);
        this.axle_front.addBox(-24.0F, 0.0F, 0.0F, 48, 1, 1, 0.0F);
        this.seat5_base = new ModelRenderer(this, 0, 220);
        this.seat5_base.setRotationPoint(-15.0F, 18.0F, 47.0F);
        this.seat5_base.addBox(-8.0F, -2.0F, -8.0F, 16, 4, 16, 0.0F);
        this.left_side = new ModelRenderer(this, 0, 0);
        this.left_side.setRotationPoint(23.0F, 4.0F, 0.0F);
        this.left_side.addBox(0.0F, -16.0F, -56.0F, 1, 32, 112, 0.0F);
        this.seat6_back = new ModelRenderer(this, 0, 220);
        this.seat6_back.setRotationPoint(15.0F, 10.0F, 54.0F);
        this.seat6_back.addBox(-8.0F, -6.0F, 0.0F, 16, 12, 1, 0.0F);
        this.seat2_base = new ModelRenderer(this, 0, 220);
        this.seat2_base.setRotationPoint(15.0F, 18.0F, -23.0F);
        this.seat2_base.addBox(-8.0F, -2.0F, -8.0F, 16, 4, 16, 0.0F);
        this.tyre1_right_front = new ModelRenderer(this, 0, 275);
        this.tyre1_right_front.setRotationPoint(-25.0F, 21.5F, -27.5F);
        this.tyre1_right_front.addBox(0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.tyre2_left_back = new ModelRenderer(this, 0, 280);
        this.tyre2_left_back.setRotationPoint(24.0F, 21.5F, 30.5F);
        this.tyre2_left_back.addBox(0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.setRotateAngle(tyre2_left_back, 1.5707963267948966F, 0.0F, 0.0F);
        this.right_front_panel = new ModelRenderer(this, 96, 0);
        this.right_front_panel.setRotationPoint(-24.0F, 4.0F, -52.0F);
        this.right_front_panel.addBox(0.0F, -16.0F, -4.0F, 1, 32, 8, 0.0F);
        this.tyre1_right_back = new ModelRenderer(this, 0, 275);
        this.tyre1_right_back.setRotationPoint(-25.0F, 21.5F, 30.5F);
        this.tyre1_right_back.addBox(0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.tyre2_left_front = new ModelRenderer(this, 0, 280);
        this.tyre2_left_front.setRotationPoint(24.0F, 21.5F, -27.5F);
        this.tyre2_left_front.addBox(0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.setRotateAngle(tyre2_left_front, 1.5707963267948966F, 0.0F, 0.0F);
        this.axle_back = new ModelRenderer(this, 0, 260);
        this.axle_back.setRotationPoint(0.0F, 21.0F, 30.0F);
        this.axle_back.addBox(-24.0F, 0.0F, 0.0F, 48, 1, 1, 0.0F);
        this.driverseat_base = new ModelRenderer(this, 0, 220);
        this.driverseat_base.setRotationPoint(15.0F, 18.0F, -47.0F);
        this.driverseat_base.addBox(-8.0F, -2.0F, -8.0F, 16, 4, 16, 0.0F);
        this.seat2_back = new ModelRenderer(this, 0, 220);
        this.seat2_back.setRotationPoint(15.0F, 10.0F, -16.0F);
        this.seat2_back.addBox(-8.0F, -6.0F, 0.0F, 16, 12, 1, 0.0F);
        this.seat3_back = new ModelRenderer(this, 0, 220);
        this.seat3_back.setRotationPoint(-15.0F, 10.0F, 5.0F);
        this.seat3_back.addBox(-8.0F, -6.0F, 0.0F, 16, 12, 1, 0.0F);
        this.bottom_plate = new ModelRenderer(this, 0, 0);
        this.bottom_plate.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.bottom_plate.addBox(-24.0F, 0.0F, -56.0F, 48, 1, 112, 0.0F);
        this.seat5_back = new ModelRenderer(this, 0, 220);
        this.seat5_back.setRotationPoint(-15.0F, 10.0F, 54.0F);
        this.seat5_back.addBox(-8.0F, -6.0F, 0.0F, 16, 12, 1, 0.0F);
        this.tyre1_left_back = new ModelRenderer(this, 0, 275);
        this.tyre1_left_back.setRotationPoint(24.0F, 21.5F, 30.5F);
        this.tyre1_left_back.addBox(0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.driverseat_back = new ModelRenderer(this, 0, 220);
        this.driverseat_back.setRotationPoint(15.0F, 10.0F, -40.0F);
        this.driverseat_back.addBox(-8.0F, -6.0F, 0.0F, 16, 12, 1, 0.0F);
        this.tyre2_right_front = new ModelRenderer(this, 0, 280);
        this.tyre2_right_front.setRotationPoint(-25.0F, 21.5F, -27.5F);
        this.tyre2_right_front.addBox(0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.setRotateAngle(tyre2_right_front, 1.5707963267948966F, 0.0F, 0.0F);
        this.tyre2_right_back = new ModelRenderer(this, 0, 280);
        this.tyre2_right_back.setRotationPoint(-25.0F, 21.5F, 30.5F);
        this.tyre2_right_back.addBox(0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.setRotateAngle(tyre2_right_back, 1.5707963267948966F, 0.0F, 0.0F);
        this.tyre1_left_front = new ModelRenderer(this, 0, 275);
        this.tyre1_left_front.setRotationPoint(24.0F, 21.5F, -27.5F);
        this.tyre1_left_front.addBox(0.0F, -2.5F, -1.5F, 1, 5, 3, 0.0F);
        this.back_plate = new ModelRenderer(this, 0, 32);
        this.back_plate.setRotationPoint(0.0F, 4.0F, 55.0F);
        this.back_plate.addBox(-23.0F, -16.0F, 0.0F, 46, 32, 1, 0.0F);
        this.seat4_back = new ModelRenderer(this, 0, 220);
        this.seat4_back.setRotationPoint(15.0F, 10.0F, 5.0F);
        this.seat4_back.addBox(-8.0F, -6.0F, 0.0F, 16, 12, 1, 0.0F);
        this.right_middle_panel = new ModelRenderer(this, 0, 106);
        this.right_middle_panel.setRotationPoint(-24.0F, 4.0F, -12.0F);
        this.right_middle_panel.addBox(0.0F, -16.0F, -19.0F, 1, 32, 38, 0.0F);
        this.front_plate = new ModelRenderer(this, 0, 0);
        this.front_plate.setRotationPoint(0.0F, 4.0F, -56.0F);
        this.front_plate.addBox(-23.0F, -16.0F, 0.0F, 46, 32, 1, 0.0F);
        this.seat1_back = new ModelRenderer(this, 0, 220);
        this.seat1_back.setRotationPoint(-15.0F, 10.0F, -16.0F);
        this.seat1_back.addBox(-8.0F, -6.0F, 0.0F, 16, 12, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.seat1_base.render(f5);
        this.seat3_base.render(f5);
        this.seat6_base.render(f5);
//        this.rooftop_plate.render(f5);
//        this.vent.render(f5);
//        this.right_back_panel.render(f5);
        this.seat4_base.render(f5);
        this.axle_front.render(f5);
        this.seat5_base.render(f5);
        this.left_side.render(f5);
        this.seat6_back.render(f5);
        this.seat2_base.render(f5);
        this.tyre1_right_front.render(f5);
        this.tyre2_left_back.render(f5);
//        this.right_front_panel.render(f5);
        this.tyre1_right_back.render(f5);
        this.tyre2_left_front.render(f5);
        this.axle_back.render(f5);
        this.driverseat_base.render(f5);
        this.seat2_back.render(f5);
        this.seat3_back.render(f5);
//        this.bottom_plate.render(f5);
        this.seat5_back.render(f5);
        this.tyre1_left_back.render(f5);
        this.driverseat_back.render(f5);
        this.tyre2_right_front.render(f5);
        this.tyre2_right_back.render(f5);
        this.tyre1_left_front.render(f5);
//        this.back_plate.render(f5);
        this.seat4_back.render(f5);
//        this.right_middle_panel.render(f5);
//        this.front_plate.render(f5);
        this.seat1_back.render(f5);
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