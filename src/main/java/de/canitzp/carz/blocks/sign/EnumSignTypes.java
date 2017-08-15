package de.canitzp.carz.blocks.sign;

import de.canitzp.carz.blocks.BlockRoadSign;
import de.canitzp.carz.client.models.signs.ModelRoadSign;
import de.canitzp.carz.client.models.signs.ModelRoadSignTriangle;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Collections;
import java.util.List;

/**
 * @author canitzp
 */
public enum EnumSignTypes {

    TRIANGLE(new ModelRoadSignTriangle(), MapColor.RED, BlockRoadSign.SIGN_DEFAULT_BOTTOM, BlockRoadSign.SIGN_DEFAULT_TOP);

    private ModelRoadSign model;
    private MapColor color;
    private AxisAlignedBB bottomBB, topBB;

    EnumSignTypes(ModelRoadSign model, MapColor color, AxisAlignedBB bottomBB, AxisAlignedBB topBB){
        this.model = model;
        this.color = color;
        this.bottomBB = bottomBB;
        this.topBB = topBB;
    }

    public ModelRoadSign getModel() {
        return model;
    }

    public MapColor getColor() {
        return color;
    }

    public AxisAlignedBB getBottomBoundingBox() {
        return bottomBB;
    }

    public AxisAlignedBB getTopBoundingBox() {
        return topBB;
    }

    public List<AxisAlignedBB> getBottomHitBoxes() {
        return Collections.singletonList(this.getBottomBoundingBox());
    }

    public List<AxisAlignedBB> getTopHitBoxes() {
        return Collections.singletonList(this.getTopBoundingBox());
    }
}
