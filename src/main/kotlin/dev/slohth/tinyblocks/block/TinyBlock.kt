package dev.slohth.tinyblocks.block

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.BlockDisplay

class TinyBlock(val location: Location, val block: Block, val scale: Double) {

    var entity: BlockDisplay? = null

    init {
        spawn()
    }

    fun spawn() {
        remove()

        entity = location.world!!.spawn(location, BlockDisplay::class.java)
        entity!!.block = block.blockData

        val trans = entity!!.transformation
        trans.scale.set(scale)
        entity!!.transformation = trans
    }

    fun remove() {
        entity?.remove()
    }

}