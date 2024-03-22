package net.fabricmc.servercommand.mixin;

import net.fabricmc.servercommand.EntityPlayerMPAccessor;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mixin(EntityPlayerMP.class)
public abstract class EntityPlayerMPMixin extends EntityPlayer implements EntityPlayerMPAccessor {

	//AARON ADDED these variables in order to keep track of teleportation requests
	@Unique
	public String tpaRequestPlayerName;
	@Unique
	public NBTTagCompound playerHomePosition = new NBTTagCompound();
	@Unique
	public NBTTagList playerDimension = new NBTTagList();
	@Unique
	public NBTTagList playerHomeList = new NBTTagList();
	@Unique
	private NBTTagCompound playerNBT = new NBTTagCompound();

	@Shadow public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {}

	public EntityPlayerMPMixin(World par1World) {
		super(par1World);
	}

	@Inject(method = "writeEntityToNBT", at = @At("RETURN"))
	private void writeEntityToNBTMixin(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
		par1NBTTagCompound.setCompoundTag("playerHomePosition", playerHomePosition);
		par1NBTTagCompound.setTag("playerHomeList", playerHomeList);
		par1NBTTagCompound.setTag("playerDimension", playerDimension);
	}

	@Inject(method = "readEntityFromNBT", at = @At("RETURN"))
	private void readEntityFromNBTMixin(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
		this.playerHomePosition = par1NBTTagCompound.getCompoundTag("playerHomePosition");
		this.playerHomeList = par1NBTTagCompound.getTagList("playerHomeList");
		this.playerDimension = par1NBTTagCompound.getTagList("playerDimension");
	}

	@Inject(method = "travelToDimension", at = @At("HEAD"))
	private void travelToDimensionAndSaveHome(int par1, CallbackInfo ci) {
		this.writeEntityToNBT(playerNBT);
	}

	@Inject(method = "travelToDimension", at = @At("RETURN"))
	private void travelToDimensionAndLoadHome(int par1, CallbackInfo ci) {
		this.serverCommandAddon$readPlayerHomeFromNBT(playerNBT);
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
	public void serverCommandAddon$setHomePosition(String name, double posX, double posY, double posZ, int dimension) {
		NBTTagList xyz = newDoubleNBTList(posX, posY, posZ);
		playerHomePosition.setTag(name, xyz);
		playerDimension.appendTag(new NBTTagInt("dimension", dimension));
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
	public int serverCommandAddon$getHomeDimension(String name) {
		for(int i = 0; i < playerHomeList.tagCount(); i++) {
			if(Objects.equals(((NBTTagString) (playerHomeList.tagAt(i))).data, name)) {
				return ((NBTTagInt)playerDimension.tagAt(i)).data;
			}
		}
        return 0;
    }
	@Override
	public void serverCommandAddon$deleteHomePosition(String name) {
		playerHomePosition.removeTag(name);
		for(int i = 0; i < playerHomeList.tagCount(); i++) {
			if(Objects.equals(((NBTTagString) (playerHomeList.tagAt(i))).data, name)) {
				playerHomeList.removeTag(i);
				playerDimension.removeTag(i);
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

	public void serverCommandAddon$readPlayerHomeFromNBT(NBTTagCompound par1NBTTagCompound) {
		playerHomePosition = par1NBTTagCompound.getCompoundTag("playerHomePosition");
		playerHomeList = par1NBTTagCompound.getTagList("playerHomeList");
		playerDimension = par1NBTTagCompound.getTagList("playerDimension");
	}
}
