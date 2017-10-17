package de.canitzp.amcm;

/**
 * @author canitzp
 */
public class AMCMShapeVar<T extends Number> {

    public T x, y, z;

    public AMCMShapeVar(T x, T y, T z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AMCMShapeVar<Float> toFloat(float factor){
        return new AMCMShapeVar<>(this.x.floatValue() * factor, this.y.floatValue() * factor, this.z.floatValue() * factor);
    }

    public AMCMShapeVar<Double> toDouble(double factor){
        return new AMCMShapeVar<>(this.x.doubleValue() * factor, this.y.doubleValue() * factor, this.z.doubleValue() * factor);
    }

    public AMCMShapeVar<Integer> toInt(int factor){
        return new AMCMShapeVar<>(this.x.intValue() * factor, this.y.intValue() * factor, this.z.intValue() * factor);
    }

    public boolean allZero(){
        AMCMShapeVar<Double> var = this.toDouble(1.0D);
        return var.x == 0.0D && var.y == 0.0D && var.z == 0.0D;
    }

    @Override
    public String toString() {
        return String.format("%s<%s>{x=%s, y=%s, z=%s}", this.getClass().getSimpleName(), this.x.getClass().getSimpleName(), this.x.toString(), this.y.toString(), this.z.toString());
    }
}
