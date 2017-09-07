package de.canitzp.carz.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
//TODO maybe I have to try to add this as normal via the return value of the parse method, instead of an own List
public class FactoryPlantFermenter implements IRecipeFactory {

    public static List<RecipePlantFermenter> PLANT_FERMENTER_RECIPES = new ArrayList<>();

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        JsonObject itemsJson = JsonUtils.getJsonObject(json, "items", new JsonObject());
        String input = JsonUtils.getString(itemsJson, "input", "");
        String output = JsonUtils.getString(itemsJson, "output", "");
        JsonObject fluidJson = JsonUtils.getJsonObject(json, "fluid", new JsonObject());
        String fluidName = JsonUtils.getString(fluidJson, "name", "");
        int fluidAmount = JsonUtils.getInt(fluidJson, "amount", 0);

        if(!StringUtils.isEmpty(input) && !StringUtils.isEmpty(output)){
            Item itemIn = ForgeRegistries.ITEMS.getValue(new ResourceLocation(input));
            Item itemOut = ForgeRegistries.ITEMS.getValue(new ResourceLocation(output));
            if(itemIn != null && itemOut != null){
                FluidStack fluid = null;
                if(!StringUtils.isEmpty(fluidName) && fluidAmount > 0){
                    fluid = FluidRegistry.getFluidStack(fluidName, fluidAmount);
                }
                PLANT_FERMENTER_RECIPES.add(new RecipePlantFermenter(new ItemStack(itemIn), new ItemStack(itemOut), fluid));
            }
        }

        return new RecipeFake();
    }

    @Nonnull
    public static RecipePlantFermenter getRecipeOrDefault(@Nonnull ItemStack input){
        for(RecipePlantFermenter recipe : PLANT_FERMENTER_RECIPES){
            if(ItemStack.areItemStacksEqual(recipe.getInput(), input)){
                return recipe;
            }
        }
        return RecipePlantFermenter.DEFAULT;
    }

}
