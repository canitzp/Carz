package de.canitzp.carz.client.models;

import net.minecraft.entity.Entity;

/**
 * A bus exposing its inner parts - perfect for debugging
 * @author canitzp
 */
public class ModelNakedBus extends ModelBus {

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
//        this.left_side.render(f5);
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

}