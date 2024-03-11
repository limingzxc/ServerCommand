package btw.community.servercommand;

import btw.AddonHandler;
import btw.BTWAddon;
import net.fabricmc.servercommand.*;

public class ServerCommandAddon extends BTWAddon {
    private static ServerCommandAddon instance;

    private ServerCommandAddon() {
        super("Server Command", "1.0.0", "ServerCommand");
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        registerAddonCommand(new ServerCommandTpRequest());
        registerAddonCommand(new ServerCommandTpAccept());
        registerAddonCommand(new ServerCommandTpDeny());
        registerAddonCommand(new ServerCommandSetHome());
        registerAddonCommand(new ServerCommandGoHome());
        registerAddonCommand(new ServerCommandListHome());
        registerAddonCommand(new ServerCommandDeleteHome());
    }

    public static ServerCommandAddon getInstance() {
        if (instance == null)
            instance = new ServerCommandAddon();
        return instance;
    }
}
