package hantonik.atomic.core.util.helper

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.nbt.TagParser
import net.minecraft.util.GsonHelper
import net.minecraft.world.item.ItemStack

object ACNBTHelper {
    private val GSON = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

    fun parseTag(json: JsonElement): CompoundTag {
        return try {
            if (json.isJsonObject)
                TagParser.parseTag(GSON.toJson(json))
            else
                TagParser.parseTag(GsonHelper.convertToString(json, "nbt"))
        } catch (e: CommandSyntaxException) {
            throw JsonSyntaxException("Invalid NBT entry: $e")
        }
    }

    fun setTag(stack: ItemStack, key: String, value: Tag) {
        this.getTagCompound(stack).put(key, value)
    }

    fun setByte(stack: ItemStack, key: String, value: Byte) {
        this.getTagCompound(stack).putByte(key, value)
    }

    fun setShort(stack: ItemStack, key: String, value: Short) {
        this.getTagCompound(stack).putShort(key, value)
    }

    fun setInt(stack: ItemStack, key: String, value: Int) {
        this.getTagCompound(stack).putInt(key, value)
    }

    fun setLong(stack: ItemStack, key: String, value: Long) {
        this.getTagCompound(stack).putLong(key, value)
    }

    fun setFloat(stack: ItemStack, key: String, value: Float) {
        this.getTagCompound(stack).putFloat(key, value)
    }

    fun setDouble(stack: ItemStack, key: String, value: Double) {
        this.getTagCompound(stack).putDouble(key, value)
    }

    fun setString(stack: ItemStack, key: String, value: String) {
        this.getTagCompound(stack).putString(key, value)
    }

    fun setByteArray(stack: ItemStack, key: String, value: ByteArray) {
        this.getTagCompound(stack).putByteArray(key, value)
    }

    fun setIntArray(stack: ItemStack, key: String, value: IntArray) {
        this.getTagCompound(stack).putIntArray(key, value)
    }

    fun setBoolean(stack: ItemStack, key: String, value: Boolean) {
        this.getTagCompound(stack).putBoolean(key, value)
    }

    fun getTag(stack: ItemStack, key: String): Tag? {
        return if (stack.hasTag()) this.getTagCompound(stack)[key] else null
    }

    fun getByte(stack: ItemStack, key: String): Byte {
        return if (stack.hasTag()) this.getTagCompound(stack).getByte(key) else 0
    }

    fun getShort(stack: ItemStack, key: String): Short {
        return if (stack.hasTag()) this.getTagCompound(stack).getShort(key) else 0
    }

    fun getInt(stack: ItemStack, key: String): Int {
        return if (stack.hasTag()) this.getTagCompound(stack).getInt(key) else 0
    }

    fun getLong(stack: ItemStack, key: String): Long {
        return if (stack.hasTag()) this.getTagCompound(stack).getLong(key) else 0L
    }

    fun getFloat(stack: ItemStack, key: String): Float {
        return if (stack.hasTag()) this.getTagCompound(stack).getFloat(key) else 0.0f
    }

    fun getDouble(stack: ItemStack, key: String): Double {
        return if (stack.hasTag()) this.getTagCompound(stack).getDouble(key) else 0.0
    }

    fun getString(stack: ItemStack, key: String): String {
        return if (stack.hasTag()) this.getTagCompound(stack).getString(key) else ""
    }

    fun getByteArray(stack: ItemStack, key: String): ByteArray {
        return if (stack.hasTag()) this.getTagCompound(stack).getByteArray(key) else ByteArray(0)
    }

    fun getIntArray(stack: ItemStack, key: String): IntArray {
        return if (stack.hasTag()) this.getTagCompound(stack).getIntArray(key) else IntArray(0)
    }

    fun getBoolean(stack: ItemStack, key: String): Boolean {
        return stack.hasTag() && this.getTagCompound(stack).getBoolean(key)
    }

    fun hasKey(stack: ItemStack, key: String): Boolean {
        return stack.hasTag() && this.getTagCompound(stack).contains(key)
    }

    fun removeTag(stack: ItemStack, key: String) {
        if (hasKey(stack, key))
            this.getTagCompound(stack).remove(key)
    }

    fun validateCompound(stack: ItemStack) {
        if (!stack.hasTag()) {
            val tag = CompoundTag()

            stack.setTag(tag)
        }
    }

    fun getTagCompound(stack: ItemStack): CompoundTag {
        this.validateCompound(stack)

        return stack.tag!!
    }
}