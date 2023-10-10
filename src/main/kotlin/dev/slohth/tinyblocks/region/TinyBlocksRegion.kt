package dev.slohth.tinyblocks.region

import dev.slohth.tinyblocks.block.TinyBlock
import dev.slohth.tinyblocks.region.handler.RegionHandler
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.BlockFace

class TinyBlocksRegion(val regionHandler: RegionHandler, val bottomRightLocation: Location) {

    fun handlePlacement(bigBlock: Block) {
        val vectorToCorner = bigBlock.location.add(0.5, 0.5, 0.5).toVector().subtract(regionHandler.bigBottomRightCorner.clone().add(0.5, 0.5, 0.5).toVector())
        vectorToCorner.multiply(regionHandler.scale)
        val location = bottomRightLocation.clone().add(vectorToCorner)

        regionHandler.blockEntityRelation[bigBlock] = TinyBlock(location, bigBlock, regionHandler.scale)
    }

    fun handleRemove(bigBlock: Block) {
        regionHandler.blockEntityRelation[bigBlock]?.let {
            it.remove()
            regionHandler.blockEntityRelation.remove(bigBlock)
        }
    }

    fun handleUpdate(bigBlock: Block) {
        if (bigBlock.isEmpty) {
            if (regionHandler.blockEntityRelation.containsKey(bigBlock)) handleRemove(bigBlock)
            return
        }

        if (regionHandler.blockEntityRelation.containsKey(bigBlock)) {
            val block = regionHandler.blockEntityRelation[bigBlock]
            block!!.remove()
            if (shouldBeVisible(bigBlock)) block.spawn()
        } else {
            if (shouldBeVisible(bigBlock) && regionHandler.bigRegion.contains(bigBlock)) handlePlacement(bigBlock)
        }
    }

    fun shouldBeVisible(bigBlock: Block): Boolean {
        var visible = false;
        for (face in listOf(BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST)) {
            if (bigBlock.getRelative(face).isPassable) visible = true
        }
        return visible
    }

}