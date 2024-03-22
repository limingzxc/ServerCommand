package net.fabricmc.servercommand;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.util.List;
import java.util.Objects;

public class ServerCommandTpRequest extends CommandBase {
    @Override
    public String getCommandName() {
        return "tpa";
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

    @Override
    public void processCommand(ICommandSender sender, String[] arguments) {
        if (arguments.length != 1)
        {
            throw new WrongUsageException("Try /tpa [playername]", new Object[0]);

        } else {

            EntityPlayerMP teleportingPlayer;
            //gets the request sender (the person sending the message) as a EntityPlayerMP object
            teleportingPlayer = getCommandSenderAsPlayer(sender);

            if (teleportingPlayer == null)
            {
                throw new PlayerNotFoundException();
            }

            //gets the target of the tp request
            EntityPlayerMP targetPlayer = func_82359_c(sender, arguments[arguments.length - 1]);

            if (targetPlayer == null)
            {
                throw new PlayerNotFoundException();
            }

//            if (targetPlayer.worldObj != teleportingPlayer.worldObj)
//            {
//                notifyAdmins(sender, "commands.tp.notSameDimension", new Object[0]);
//                return;
//            }

            if(Objects.equals(targetPlayer.getEntityName(), teleportingPlayer.getEntityName()))
            {
                teleportingPlayer.addChatMessage("You can't teleport to yourself.");
                return;
            }

            String targetPlayerName = targetPlayer.getEntityName();

            ((EntityPlayerMPAccessor)targetPlayer).serverCommandAddon$setTpaRequestName(teleportingPlayer.getEntityName());
            teleportingPlayer.addChatMessage("You sent a teleport request to "+targetPlayerName+".");
            targetPlayer.addChatMessage(teleportingPlayer.getEntityName()+" sent you a teleport request.");
        }
    }

    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length >= 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return par2 == 0;
    }
}
