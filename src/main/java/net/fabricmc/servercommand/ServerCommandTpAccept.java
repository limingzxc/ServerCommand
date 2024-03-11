package net.fabricmc.servercommand;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ServerCommandTpAccept extends CommandBase {
    @Override
    public String getCommandName() {
        return "tpaccept";
    }

    @Override
    public List getCommandAliases()
    {
        return Collections.singletonList("tpyes");
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
        if (arguments.length > 2) {

            throw new WrongUsageException("Try /tpaccept <playername>", new Object[0]);
        }

        // gets the person accepting the request (the person sending the message) as a EntityPlayerMP object
        EntityPlayerMP acceptingPlayer = getCommandSenderAsPlayer(sender);

        if (acceptingPlayer == null)
        {
            throw new PlayerNotFoundException();
        }

        EntityPlayerMP teleportingPlayer;

        // Optional String must be converted to a pure String, otherwise the game won't read the name properly
        Optional<String> optionalName = ((EntityPlayerMPAccessor)acceptingPlayer).serverCommandAddon$getTpaRequestName();
        String tpaRequestName = optionalName.orElse("");

        if (arguments.length < 1) {

            if (tpaRequestName.isEmpty())
            {
                acceptingPlayer.addChatMessage("No active teleport requests found from anyone.");
                return;
            }

            teleportingPlayer = func_82359_c(sender, tpaRequestName);

        } else {

            teleportingPlayer = func_82359_c(sender, arguments[arguments.length - 1]);
            if (teleportingPlayer == null)
            {
                throw new PlayerNotFoundException();
            }
        }

        if (teleportingPlayer.worldObj != acceptingPlayer.worldObj)
        {
            notifyAdmins(sender, "commands.tp.notSameDimension", new Object[0]);
            return;
        }

        String teleportingPlayerName = teleportingPlayer.getEntityName();
        String acceptingPlayerName = acceptingPlayer.getEntityName();


        if (tpaRequestName.equals(teleportingPlayerName))
        {
            acceptingPlayer.addChatMessage("Teleported "+teleportingPlayerName+" to you.");
            teleportingPlayer.addChatMessage("Teleported you to "+acceptingPlayerName+".");

            teleportingPlayer.mountEntity((Entity)null);
            teleportingPlayer.playerNetServerHandler.setPlayerLocation(acceptingPlayer.posX, acceptingPlayer.posY, acceptingPlayer.posZ, acceptingPlayer.rotationYaw, acceptingPlayer.rotationPitch);
            ((EntityPlayerMPAccessor)acceptingPlayer).serverCommandAddon$setTpaRequestName(""); // prevents the accepter from spam-teleporting

        } else {
            acceptingPlayer.addChatMessage("No active teleport requests found from "+teleportingPlayerName+".");
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
