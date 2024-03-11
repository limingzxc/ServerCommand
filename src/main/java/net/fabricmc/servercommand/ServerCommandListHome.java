package net.fabricmc.servercommand;

import net.minecraft.src.*;

import java.util.List;
import java.util.stream.Collectors;

public class ServerCommandListHome extends CommandBase {
    @Override
    public String getCommandName() {
        return "listhome";
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
        if (arguments.length > 0) {

            throw new WrongUsageException("Try /listhome", new Object[0]);
        }

        // gets the person accepting the request (the person sending the message) as a EntityPlayerMP object
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);

        List<String> home = ((EntityPlayerMPAccessor)player).serverCommandAddon$listHomePosition();
        player.addChatMessage(home.stream().map(String::valueOf).collect(Collectors.joining("\n")));
    }
}
