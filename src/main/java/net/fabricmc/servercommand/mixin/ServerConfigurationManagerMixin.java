package net.fabricmc.servercommand.mixin;

import net.fabricmc.servercommand.EntityPlayerMPAccessor;
import net.minecraft.src.*;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ServerConfigurationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerConfigurationManager.class)
public class ServerConfigurationManagerMixin {
    @Unique
    private NBTTagCompound playerNBT = new NBTTagCompound();

    @Inject(method = "respawnPlayer", at =@At("HEAD"))
    private void savePlayerHome(EntityPlayerMP oldPlayer, int iDefaultDimension, boolean bPlayerLeavingTheEnd,
                                CallbackInfoReturnable<EntityPlayerMP> ci)
    {
        oldPlayer.writeEntityToNBT(this.playerNBT);
    }

    @Inject(method = "respawnPlayer", at =@At("RETURN"))
    private void loadPlayerHome(EntityPlayerMP oldPlayer, int iDefaultDimension, boolean bPlayerLeavingTheEnd,
                                CallbackInfoReturnable<EntityPlayerMP> ci)
    {
        ((EntityPlayerMPAccessor) ci.getReturnValue()).serverCommandAddon$readPlayerHomeFromNBT(this.playerNBT);
    }
}

