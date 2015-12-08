package karuberu.mods.mudmod.items;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import karuberu.core.MCHelper;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.entity.EntityBomb;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFireworkCharge;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class ItemBomb extends ItemMudMod {
	
	
	public ItemBomb(int id, int icon) {
		super(id, icon);
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.setMaxStackSize(16);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List information, boolean bool) {
		NBTTagCompound compound = itemStack.getTagCompound();
		information = ItemBomb.getInformationFromTagCompound(information, compound);
	}
	
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    	NBTTagCompound compound;
    	if (itemStack.hasTagCompound()) {
    		compound = (NBTTagCompound)itemStack.getTagCompound().copy();
    	} else {
    		compound = new NBTTagCompound();
    	}
    	compound.setByte("Stack Size", (byte)(itemStack.stackSize - 1));
    	ItemStack litBomb = new ItemStack(MudMod.bombLit);
    	litBomb.setTagCompound(compound);
    	litBomb.onCrafting(world, player, 0);
    	litBomb.damageItem(1, player);
    	if (player.capabilities.isCreativeMode) {
    		ItemBombLit.spawnThrownBomb(world, player, litBomb);
    	} else {
        	world.playSoundAtEntity(player, "random.fuse", 1, 1);
        	return litBomb;
    	}
    	return itemStack;
    }
	
	public static List getInformationFromTagCompound(List information, NBTTagCompound compound) {
		if (compound == null) {
			return information;
		}
		byte explosiveness = compound.getByte("Explosiveness");
		if (explosiveness > 0) {
			StringBuilder string = new StringBuilder();
			string.append("Explosive");
			if (explosiveness > 1) {
				string.append(" (" + explosiveness + ")");
			}
			information.add(string.toString());
		}
		byte fuseLength = compound.getByte("Fuse Length");
		if (fuseLength > 1) {
			StringBuilder string = new StringBuilder();
			string.append("Long Fuse");
			if (fuseLength > 2) {
				string.append(" (" + (fuseLength - 1) + ")");
			}
			information.add(string.toString());
		}
		if (compound.getBoolean("Fire-charged")) {
			information.add("Fire-charged");
		}
		if (compound.hasKey("Explosions")) {
			information.add("Firework-charged");
            NBTTagList explosions = compound.getTagList("Explosions");
            if (explosions != null && explosions.tagCount() > 0) {
                for (int i = 0; i < explosions.tagCount(); ++i) {
                    NBTTagCompound explosion = (NBTTagCompound)explosions.tagAt(i);
                    ArrayList<String> arraylist = new ArrayList<String>();
                    ItemFireworkCharge.func_92107_a(explosion, arraylist);
                    if (arraylist.size() > 0) {
                        for (int j = 0; j < arraylist.size(); ++j) {
                        	StringBuilder string = new StringBuilder();
                        	string.append(" ");
                        	if (j > 0) {
                        		string.append("  ");
                        	}
                        	string.append(arraylist.get(j));
                            arraylist.set(j, string.toString());
                        }
                        information.addAll(arraylist);
                    }
                }
            }
		}
		return information;
	}
	
	@Override
    public void getSubItems(int id, CreativeTabs creativeTab, List list) {
		ItemStack itemStack = new ItemStack(id, 1, 0);
		NBTTagCompound compound = new NBTTagCompound();
    	compound.setByte("Explosiveness", (byte)1);
    	compound.setByte("Fuse Length", (byte)1);
    	compound.setBoolean("Fire-charged", false);
    	itemStack.setTagCompound((NBTTagCompound)compound.copy());
        list.add(MCHelper.getFixedNBTItemStack(itemStack));
    }
}
