package dev.slohth.tinyblocks

import dev.slohth.tinyblocks.command.UpdateCommand
import dev.slohth.tinyblocks.region.handler.RegionHandler
import dev.slohth.tinyblocks.utils.command.Framework
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class TinyBlocks : JavaPlugin(), Listener {

    companion object {
        lateinit var INSTANCE: TinyBlocks
    }

    lateinit var framework: Framework

    override fun onEnable() {
        INSTANCE = this
        framework = Framework(this)

        for (cmd in listOf(UpdateCommand())) {
            framework.registerCommands(cmd)
        }

//        RegionHandler(
//            16, Location(Bukkit.getWorld("world"), -92.0, 91.0, 49.0),
//            1, Location(Bukkit.getWorld("world"), -95.0, 91.0, 46.0)
//        )
//
//        RegionHandler(
//            32, Location(Bukkit.getWorld("world"), -51.0, 117.0, 33.0),
//            1, Location(Bukkit.getWorld("world"), -54.0, 118.0, 30.0)
//        )
//
//        RegionHandler(
//            64, Location(Bukkit.getWorld("world"), -14.0, 117.0, 33.0),
//            1, Location(Bukkit.getWorld("world"), -17.0, 118.0, 30.0)
//        )

        RegionHandler(
            128, Location(Bukkit.getWorld("world"), 55.0, 117.0, 33.0),
            1, Location(Bukkit.getWorld("world"), 52.0, 118.0, 30.0)
        )
    }

}