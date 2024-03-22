package net.fabricmc.servercommand;

import net.minecraft.src.*;

import java.util.List;

public class ServerCommandGoHome extends CommandBase {
    @Override
    public String getCommandName() {
        return "home";
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
        if (arguments.length > 1) {

            throw new WrongUsageException("Try /home [homename]", new Object[0]);
        }

        if (arguments.length < 1)
        {
            arguments = new String[]{"home"};

        }

        // gets the person accepting the request (the person sending the message) as a EntityPlayerMP object
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);

        double[] pos;
        int dimension;
        try {
            pos = ((EntityPlayerMPAccessor) player).serverCommandAddon$getHomePosition(arguments[0]);
            dimension = ((EntityPlayerMPAccessor) player).serverCommandAddon$getHomeDimension(arguments[0]);
        }
        catch (IndexOutOfBoundsException var) {
            player.addChatMessage("You didn't set up a home with that name.");
            return;
        }
        if(player.dimension == 1 && dimension != 1) {
            player.addChatMessage("You can't travel to dimension in The End.");
            return;
        }
        player.mountEntity((Entity)null);
        if(player.dimension != dimension) {
            player.travelToDimension(dimension);
        }
        player.playerNetServerHandler.setPlayerLocation(pos[0], pos[1], pos[2], 0, 0);
        player.addChatMessage("You have successfully go home");
    }

    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        // gets the person accepting the request (the person sending the message) as a EntityPlayerMP object
        EntityPlayerMP player = getCommandSenderAsPlayer(par1ICommandSender);

        return par2ArrayOfStr.length >= 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, String.join(",",
                ((EntityPlayerMPAccessor) player).serverCommandAddon$listHomePosition())): null;
    }
}
