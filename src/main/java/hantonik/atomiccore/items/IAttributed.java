package hantonik.atomiccore.items;

import hantonik.atomiccore.registration.object.FluidObject;
import net.minecraft.world.level.material.Fluid;

public interface IAttributed {
    FluidObject<? extends Fluid> getMelted();
    int getBurningTime();
    int getBurningTemperature();
    int getMeltingTemperature();
    boolean isBurnable();
    boolean isFusible();
}
