package de.canitzp.carz.client.models.signs;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public abstract class ModelRoadSign extends ModelBase {

    private List<ModelRenderer> parts = new ArrayList<>();

    @SideOnly(Side.CLIENT)
    public void render(float scale) {
        if (this.parts.isEmpty()) {
            scanParts();
        } else {
            for (ModelRenderer part : this.parts) {
                part.render(scale);
            }
        }
    }

    public abstract ResourceLocation getTexture();

    private void scanParts() {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.getType().getName().equals("net.minecraft.client.model.ModelRenderer")) {
                try {
                    this.parts.add((ModelRenderer) field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
