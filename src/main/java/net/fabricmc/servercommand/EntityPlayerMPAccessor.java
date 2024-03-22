package net.fabricmc.servercommand;

import net.minecraft.src.NBTTagCompound;

import java.util.List;
import java.util.Optional;

public interface EntityPlayerMPAccessor {
    public default void serverCommandAddon$setTpaRequestName(String name)
    {
    }
    public default Optional<String> serverCommandAddon$getTpaRequestName()
    {
        return Optional.empty();
    }

    void serverCommandAddon$setHomePosition(String name, double posX, double posY, double posZ, int dimension);

    double[] serverCommandAddon$getHomePosition(String name);

    int serverCommandAddon$getHomeDimension(String name);

    void serverCommandAddon$deleteHomePosition(String name);

    List<String> serverCommandAddon$listHomePosition();

    void serverCommandAddon$readPlayerHomeFromNBT(NBTTagCompound par1NBTTagCompound);
}
