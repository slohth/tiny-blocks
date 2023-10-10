package dev.slohth.tinyblocks.command

import dev.slohth.tinyblocks.region.handler.RegionHandler
import dev.slohth.tinyblocks.utils.command.Command
import dev.slohth.tinyblocks.utils.command.CommandArgs
import dev.slohth.tinyblocks.utils.command.ICommand

class UpdateCommand : ICommand {

    @Command(name = "update")
    fun onUpdateCommand(args: CommandArgs) {
        for (handler in RegionHandler.HANDLERS) {
            handler.updateAll()
        }
    }

}