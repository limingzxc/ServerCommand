package net.fabricmc.servercommand;

import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.WrongUsageException;

import java.util.Collections;
import java.util.List;

public class ServerCommandTpDeny extends CommandBase {

    @Override
    public String getCommandName() {
        return "tpdeny";
    }

    @Override
    public List getCommandAliases()
    {
        return Collections.singletonList("tpno");
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }

    public void processCommand(ICommandSender sender, String[] arguments)
    {

        if (arguments.length > 1)
        {
            throw new WrongUsageException("Try /tpdeny", new Object[0]);
        }
        else
        {
            EntityPlayerMP cancelingPlayer;

            // gets the person canceling the request (the person sending the message) as a EntityPlayerMP object
            cancelingPlayer = getCommandSenderAsPlayer(sender);

            cancelingPlayer.addChatMessage("to deny and delete the teleport request");

            ((EntityPlayerMPAccessor)cancelingPlayer).serverCommandAddon$setTpaRequestName("");
        }
    }
}
