package dev.slohth.tinyblocks.region

import dev.slohth.tinyblocks.region.handler.RegionHandler
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.util.BoundingBox

class BigBlocksRegion(val regionHandler: RegionHandler, val bottomRightLocation: Location) {

    val region = BoundingBox(
        bottomRightLocation.x,
        bottomRightLocation.y,
        bottomRightLocation.z,
        bottomRightLocation.x + regionHandler.bigSize,
        bottomRightLocation.y + regionHandler.bigSize,
        bottomRightLocation.z + regionHandler.bigSize,
    )

    fun contains(block: Block): Boolean {
        return region.contains(block.boundingBox)
    }

}