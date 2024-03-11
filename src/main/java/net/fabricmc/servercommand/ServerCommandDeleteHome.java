package net.fabricmc.servercommand;

import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.WrongUsageException;

import java.util.Collections;
import java.util.List;

public class ServerCommandDeleteHome extends CommandBase {
    @Override
    public String getCommandName() {
        return "delhome";
    }

    @Override
    public List getCommandAliases()
    {
        return Collections.singletonList("deletehome");
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

        if (arguments.length != 1) {

            throw new WrongUsageException("Try /delhome [homename]", new Object[0]);
        }

        // gets the person accepting the request (the person sending the message) as a EntityPlayerMP object
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);

        ((EntityPlayerMPAccessor)player).serverCommandAddon$deleteHomePosition(arguments[0]);
        player.addChatMessage("You have successfully deleted the home with the name " + arguments[0] + " .");
    }

    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        // gets the person accepting the request (the person sending the message) as a EntityPlayerMP object
        EntityPlayerMP player = getCommandSenderAsPlayer(par1ICommandSender);

        return par2ArrayOfStr.length >= 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, String.join(",",
                ((EntityPlayerMPAccessor) player).serverCommandAddon$listHomePosition())): null;
    }
}
