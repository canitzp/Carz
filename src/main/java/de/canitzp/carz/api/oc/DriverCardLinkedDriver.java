package de.canitzp.carz.api.oc;

import de.canitzp.carz.Registry;
import de.canitzp.carz.api.EntityAIDriveableBase;
import de.canitzp.carz.items.ItemCardLinkedDriver;
import li.cil.oc.api.Network;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.DriverItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * @author MisterErwin
 */
public class DriverCardLinkedDriver extends DriverItem {
    public DriverCardLinkedDriver() {
        super(new ItemStack(Registry.itemCardLinkedDriver));
    }

    @Override
    public String slot(ItemStack itemStack) {
        return Slot.Card;
    }

    @Override
    public ManagedEnvironment createEnvironment(ItemStack itemStack, EnvironmentHost environmentHost) {
        return new Environment(itemStack, environmentHost);
    }

    public class Environment extends li.cil.oc.api.prefab.AbstractManagedEnvironment {
        private final EnvironmentHost environmentHost;
        private final UUID car;

        private WeakReference<EntityAIDriveableBase> carEntity;

        public Environment(ItemStack itemStack, EnvironmentHost environmentHost) {
            this.environmentHost = environmentHost;
            if (itemStack.getItem() instanceof ItemCardLinkedDriver)
                car = ((ItemCardLinkedDriver) itemStack.getItem()).getEntityID(itemStack);
            else
                car = null;
            this.setNode(Network.newNode(this, Visibility.Neighbors).withComponent("driver").create());
        }

        //        @Optional.Method(modid = "OpenComputers")
        @Callback(doc = "function(steering:number):boolean -- Sets the steering of the vehicle ranging from -1 to 1")
        public Object[] setSteering(Context context, Arguments arguments) {
            double steering = Math.min(1, Math.max(-1, arguments.checkDouble(0)));
            EntityAIDriveableBase car = getCar();
            if (car == null)
                return new Object[]{null, "car_not_found"};
            car.setSteering((float) steering);
            return new Object[]{true};
        }

        @Callback(doc = "function(forward:number):boolean -- Sets the forward motion of the vehicle ranging from -1 to 1")
        public Object[] setForward(Context context, Arguments arguments) {
            double fwd = Math.min(1, Math.max(-1, arguments.checkDouble(0)));
            EntityAIDriveableBase car = getCar();
            if (car == null)
                return new Object[]{null, "car_not_found"};
            car.setForward((float) fwd);
            return new Object[]{true};
        }

        @Callback(doc = "function():boolean -- Starts the engine")
        public Object[] start(Context context, Arguments arguments) {
            EntityAIDriveableBase car = getCar();
            if (car == null)
                return new Object[]{false, "car_not_found"};
            car.startCar();
            return new Object[]{true};
        }

        @Callback(doc = "function():boolean -- Stops the engine")
        public Object[] stop(Context context, Arguments arguments) {
            EntityAIDriveableBase car = getCar();
            if (car == null)
                return new Object[]{null, "car_not_found"};
            car.stopCar();
            return new Object[]{true};
        }

        @Callback(doc = "function():boolean or nil, boolean -- Returns true if the engine is running")
        public Object[] isRunning(Context context, Arguments arguments) {
            EntityAIDriveableBase car = getCar();
            if (car == null)
                return new Object[]{null, "car_not_found"};
            return new Object[]{car.isRunning()};
        }

        //        @Optional.Method(modid = "OpenComputers")
        @Callback(doc = "function():number -- Gets the current speed of the vehicle")
        public Object[] getSpeed(Context context, Arguments arguments) {
            EntityAIDriveableBase car = getCar();
            if (car == null)
                return new Object[]{null, "car_not_found"};
            return new Object[]{car.getSpeed()};
        }

        @Callback(doc = "function():number, number, number or nil, string -- Gets the current position relative to the machine ")
        public Object[] getPosition(Context context, Arguments arguments) {
            EntityAIDriveableBase car = getCar();
            if (car == null)
                return new Object[]{null, "car_not_found"};
            return new Object[]{car.posX - environmentHost.xPosition(),
                    car.posY - environmentHost.yPosition(),
                    car.posZ - environmentHost.zPosition()};
        }

        @Callback(doc = "function():number or nil, string -- Gets the current rotation ")
        public Object[] getRotation(Context context, Arguments arguments) {
            EntityAIDriveableBase car = getCar();
            if (car == null)
                return new Object[]{null, "car_not_found"};
            return new Object[]{car.rotationYaw};
        }

        @Callback(doc = "function():boolean or nil, string -- Returns true if someone is currently driving ")
        public Object[] hasDriver(Context context, Arguments arguments) {
            EntityAIDriveableBase car = getCar();
            if (car == null)
                return new Object[]{null, "car_not_found"};
            return new Object[]{car.canPassengerSteer()};
        }



        @Override
        public boolean canUpdate() {
            return true;
        }


        @Override
        public void update() {
            EntityAIDriveableBase car = getCar();
            if (car == null) return;
            car.setAISteered();
        }

        private EntityAIDriveableBase getCar() {
            EntityAIDriveableBase car = carEntity != null ? carEntity.get() : null;
            if (car == null) {
                setCarReference();
                if (carEntity == null) return null;
                car = carEntity.get();
            }
            if (car != null && car.isDead) {
                if (carEntity != null)
                    carEntity.enqueue();
                return null;
            }
            return car;
        }

        private void setCarReference() {
            for (Entity e : environmentHost.world().getLoadedEntityList()) {
                if (e.getPersistentID().equals(car)) {
                    if (e instanceof EntityAIDriveableBase) {
                        carEntity = new WeakReference<>((EntityAIDriveableBase) e);
                    }
                    return;
                }
            }
            carEntity = null;
        }


    }
}
