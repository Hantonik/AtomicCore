package hantonik.atomic.core.registration

class DelayedSupplier<T> : () -> T {
    var supplier: (() -> T)? = null

    override fun invoke(): T {
        this.supplier ?: throw IllegalStateException("Attempted to call DelayedSupplier::invoke() before the supplier was set.")

        return this.supplier!!.invoke()
    }
}