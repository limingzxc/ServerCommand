package net.fabricmc.servercommand;

import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.WrongUsageException;

import java.util.List;
import java.util.Objects;

public class ServerCommandSetHome extends CommandBase {
    @Override
    public String getCommandName() {
        return "sethome";
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

            throw new WrongUsageException("Try /sethome [homename]", new Object[0]);
        }

        if(arguments.length < 1)
        {
            arguments = new String[]{"home"};
        }

        // gets the person accepting the request (the person sending the message) as a EntityPlayerMP object
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        if(((EntityPlayerMPAccessor)player).serverCommandAddon$listHomePosition().size() > 5) {
            player.addChatMessage("You've set up five homes.");
            return;
        }
        List<String> listHome = ((EntityPlayerMPAccessor)player).serverCommandAddon$listHomePosition();
        for(String name: listHome) {
            if(Objects.equals(name, arguments[0])) {
                player.addChatMessage("This name is already occupied, please delete it before setting up your home.");
                return;
            }
        }

        ((EntityPlayerMPAccessor)player).serverCommandAddon$setHomePosition(arguments[0], player.posX, player.posY,
                player.posZ, player.dimension);
        player.addChatMessage("You have successfully set up home");
    }
}
