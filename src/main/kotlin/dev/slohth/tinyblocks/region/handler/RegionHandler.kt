package dev.slohth.tinyblocks.region.handler

import dev.slohth.tinyblocks.block.TinyBlock
import dev.slohth.tinyblocks.TinyBlocks
import dev.slohth.tinyblocks.region.BigBlocksRegion
import dev.slohth.tinyblocks.region.TinyBlocksRegion
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

class RegionHandler(
    val bigSize: Int,
    val bigBottomRightCorner: Location,
    val smallSize: Int,
    val smallBottomRightCorner: Location
) : Listener {

    val blockEntityRelation: MutableMap<Block, TinyBlock> = HashMap()

    val allowedSizes = listOf(1, 2, 4, 8, 16, 32, 64, 128)
    val scale: Double = (smallSize.toDouble() / bigSize.toDouble())

    val bigRegion: BigBlocksRegion
    val tinyRegion: TinyBlocksRegion

    init {
        if (!allowedSizes.contains(bigSize) || !allowedSizes.contains(smallSize))
            throw NumberFormatException("Size cannot be larger than 64x64 blocks and must be a power of 2")

        HANDLERS.add(this)
        Bukkit.getPluginManager().registerEvents(this, TinyBlocks.INSTANCE)

        bigRegion = BigBlocksRegion(this, bigBottomRightCorner)
        tinyRegion = TinyBlocksRegion(this, smallBottomRightCorner)
    }

    fun updateAll() {
        for (x in 0..(bigSize)) {
            for (y in 0..(bigSize)) {
                for (z in 0..(bigSize)) {
                    val block = bigBottomRightCorner.block.getRelative(x, y, z)
                    tinyRegion.handleUpdate(block)
                }
            }
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (bigRegion.contains(event.block)) {
            tinyRegion.handlePlacement(event.block)

            Bukkit.getScheduler().runTaskLater(TinyBlocks.INSTANCE, Runnable {
                for (face in BlockFace.values()) {
                    val adjBlock = event.block.getRelative(face)
                    if (bigRegion.contains(adjBlock)) tinyRegion.handleUpdate(adjBlock)
                }
            }, 1)

        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (bigRegion.contains(event.block)) {
            tinyRegion.handleRemove(event.block)

            Bukkit.getScheduler().runTaskLater(TinyBlocks.INSTANCE, Runnable {
                for (face in BlockFace.values()) {
                    val adjBlock = event.block.getRelative(face)
                    if (bigRegion.contains(adjBlock)) tinyRegion.handleUpdate(adjBlock)
                }
            }, 1)

        }
    }

    companion object {
        val HANDLERS: MutableList<RegionHandler> = ArrayList()
    }

}