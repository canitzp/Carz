package de.canitzp.amcm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author canitzp
 */
public class AMCMPredefinedModel extends AMCMModel<AMCMPredefinedModel>{

    private List<IAMCMShapes> shapes = new ArrayList<>();

    public AMCMPredefinedModel(Collection<IAMCMShapes> shapes){
        this.shapes.addAll(shapes);
    }

    @Override
    public void createModel(List<IAMCMShapes> shapes) {
        shapes.addAll(this.shapes);
    }

    @Override
    public List<IAMCMShapes> getShapes() {
        return this.shapes;
    }
}
