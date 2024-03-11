package net.fabricmc.servercommand.mixin;

import btw.AddonHandler;
import btw.community.servercommand.ServerCommandAddon;
import net.fabricmc.servercommand.EntityPlayerMPAccessor;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.logging.Level;

@Mixin(EntityPlayerMP.class)
public abstract class EntityPlayerMPMixin extends EntityPlayer implements EntityPlayerMPAccessor {

	//AARON ADDED these variables in order to keep track of teleportation requests
	@Unique
	public String tpaRequestPlayerName;
	@Unique
	public NBTTagCompound playerHomePosition = new NBTTagCompound();

	@Unique
	public NBTTagList playerHomeList = new NBTTagList();

	public EntityPlayerMPMixin(World par1World) {
		super(par1World);
	}

	@Inject(method = "writeEntityToNBT", at = @At("RETURN"))
	private void writeEntityToNBTMixin(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
		par1NBTTagCompound.setCompoundTag("playerHomePosition", playerHomePosition);
		par1NBTTagCompound.setTag("playerHomeList", playerHomeList);
	}

	@Inject(method = "readEntityFromNBT", at = @At("RETURN"))
	private void readEntityFromNBTMixin(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
		playerHomePosition = par1NBTTagCompound.getCompoundTag("playerHomePosition");
		playerHomeList = par1NBTTagCompound.getTagList("playerHomeList");
	}

	//AARON added this method to keep track of teleport requests
	@Override
	public void serverCommandAddon$setTpaRequestName(String name)
	{
		tpaRequestPlayerName = name;
	}

	@Override
	public Optional<String> serverCommandAddon$getTpaRequestName()
	{
		return Optional.ofNullable(tpaRequestPlayerName);
	}

	@Override
	public void serverCommandAddon$setHomePosition(String name, double posX, double posY, double posZ) {
		NBTTagList xyz = newDoubleNBTList(posX, posY, posZ);
		playerHomePosition.setTag(name, xyz);
		playerHomeList.appendTag(new NBTTagString("home", name));
	}

	@Override
	public double[] serverCommandAddon$getHomePosition(String name) {
		double[] pos = new double[3];
		NBTTagList xyz = playerHomePosition.getTagList(name);
		pos[0] = ((NBTTagDouble)xyz.tagAt( 0)).data;
		pos[1] = ((NBTTagDouble)xyz.tagAt( 1)).data;
		pos[2] = ((NBTTagDouble)xyz.tagAt( 2)).data;
		return pos;
	}

	@Override
	public void serverCommandAddon$deleteHomePosition(String name) {
		playerHomePosition.removeTag(name);
		for(int i = 0; i < playerHomeList.tagCount(); i++) {
			if(Objects.equals(((NBTTagString) (playerHomeList.tagAt(i))).data, name)) {
				playerHomeList.removeTag(i);
				return;
			}
		}

	}

	@Override
	public List<String> serverCommandAddon$listHomePosition() {
		List<String> homeName = new ArrayList<>();
		for(int i = 0; i < playerHomeList.tagCount(); i++) {
			homeName.add(((NBTTagString)playerHomeList.tagAt(i)).data);
		}
		return homeName;
	}
}
