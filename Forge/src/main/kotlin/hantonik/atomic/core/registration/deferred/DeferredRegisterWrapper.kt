package hantonik.atomic.core.registration.deferred

import net.minecraft.resources.ResourceLocation
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.RegistryObject

abstract class DeferredRegisterWrapper<T>(registry: IForgeRegistry<T>, protected val modId: String) {
    protected val register: DeferredRegister<T>

    val entries: Collection<RegistryObject<T>> get() = this.register.entries
    
    init {
        this.register = DeferredRegister.create(registry, this.modId)
    }
    
    open fun register(bus: IEventBus) {
        this.register.register(bus)
    }
    
    protected fun resource(name: String) = ResourceLocation(this.modId, name)
}