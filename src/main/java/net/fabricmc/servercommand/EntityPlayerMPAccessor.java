package net.fabricmc.servercommand;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EntityPlayerMPAccessor {
    public default void serverCommandAddon$setTpaRequestName(String name)
    {
    }
    public default Optional<String> serverCommandAddon$getTpaRequestName()
    {
        return Optional.empty();
    }

    void serverCommandAddon$setHomePosition(String name, double posX, double posY, double posZ);
    double[] serverCommandAddon$getHomePosition(String name);

    void serverCommandAddon$deleteHomePosition(String name);
    List<String> serverCommandAddon$listHomePosition();
}
