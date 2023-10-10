package dev.slohth.tinyblocks.utils.item

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import dev.slohth.tinyblocks.utils.CC.color
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.ArrayList

/**
 * A builder class to easily create and modify ItemStacks
 * @author Brandon @slohth
 */
class ItemBuilder {

    private var item: ItemStack
    private var meta: ItemMeta

    constructor(item: ItemStack) {
        this.item = item
        this.meta = item.itemMeta!!
    }

    constructor(material: Material) {
        this.item = ItemStack(material)
        this.meta = item.itemMeta!!
    }

    fun name(name: String): ItemBuilder { meta.setDisplayName(name.color()); return this }

    fun name() = meta.displayName

    fun name(action: (String) -> String): ItemBuilder {
        meta.setDisplayName(action(meta.displayName))
        return this
    }

    fun lore(lore: String): ItemBuilder { meta.lore = listOf(lore.color()); return this }

    fun lore(vararg lore: String): ItemBuilder { meta.lore = lore.color(); return this }

    fun lore(lore: List<String>): ItemBuilder { meta.lore = lore.color(); return this }

    fun lore(): MutableList<String> = if (meta.lore.isNullOrEmpty()) ArrayList() else ArrayList(meta.lore)

    fun appendLore(lore: String): ItemBuilder = lore(lore().plus(lore))

    fun appendLore(vararg lore: String): ItemBuilder = lore(lore().plus(lore))

    fun appendLore(lore: List<String>): ItemBuilder = lore(lore().plus(lore))

    fun clearLore(): ItemBuilder { meta.lore = null; return this }

    fun lore(action: (String) -> String): ItemBuilder {
        val list: MutableList<String> = ArrayList()
        for (line in meta.lore!!) list.add(action(line))
        return lore(list)
    }

    fun amount(amount: Int): ItemBuilder { item.amount = amount; return this }

    fun durability(durability: Int): ItemBuilder { (meta as Damageable).damage = durability; return this }

    fun enchant(enchant: Enchantment): ItemBuilder = enchant(enchant, 1)

    fun clearEnchants(): ItemBuilder { for (enchant in item.enchantments.keys) item.removeEnchantment(enchant); return this }

    fun enchant(enchant: Enchantment, level: Int): ItemBuilder {
        item.itemMeta = meta
        item.addUnsafeEnchantment(enchant, level)
        meta = item.itemMeta!!
        return this
    }

    fun unbreakable(unbreakable: Boolean): ItemBuilder { meta.isUnbreakable = unbreakable; return this }

    fun type() = item.type

    fun type(material: Material): ItemBuilder {
        item.itemMeta = meta
        item.type = material
        meta = item.itemMeta!!
        return this
    }

    fun armorColor(color: Color): ItemBuilder { (meta as LeatherArmorMeta).setColor(color); return this }

    fun localisedName(name: String): ItemBuilder { meta.setLocalizedName(name); return this }

    fun localisedName() = meta.localizedName

    fun localisedName(action: (String) -> String): ItemBuilder {
        meta.setLocalizedName(action(meta.localizedName))
        return this
    }

    fun flag(vararg flag: ItemFlag): ItemBuilder { meta.addItemFlags(*flag); return this }

    fun skull(name: String): ItemBuilder { (meta as SkullMeta).owningPlayer = Bukkit.getOfflinePlayer(name); return this }

    fun texture(texture: String): ItemBuilder {
        val profile = GameProfile(UUID.randomUUID(), null)
        profile.properties.put("textures", Property("textures", texture))
        val field: Field
        try {
            field = meta.javaClass.getDeclaredField("profile")
            field.isAccessible = true
            field.set(meta, profile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    fun potion(type: PotionType, extended: Boolean, upgraded: Boolean): ItemBuilder {
        (meta as PotionMeta).basePotionData = PotionData(type, extended, upgraded)
        return this
    }

    fun build(): ItemStack {
        item.itemMeta = meta
        return item
    }
}