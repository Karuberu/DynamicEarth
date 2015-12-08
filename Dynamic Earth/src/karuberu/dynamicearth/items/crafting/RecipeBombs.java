package karuberu.dynamicearth.items.crafting;

import karuberu.core.MCHelper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.items.ItemBombLit;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class RecipeBombs implements IRecipe {
	public static final RecipeBombs
		instance = new RecipeBombs();
	public static int
		maxGunpowder = 4;
    private ItemStack result;

	@Override
	public boolean matches(InventoryCrafting inventorycrafting, World world) {
		ItemStack bomb = null;
		ItemStack potion = null;
		int bowls = 0;
		int gunpowder = 0;
		int string = 0;
		int fireCharges = 0;
		boolean hasFireworks = false;
		NBTTagList list = new NBTTagList();
		
		for (int i = 0; i < inventorycrafting.getSizeInventory(); i++) {
			ItemStack item = inventorycrafting.getStackInSlot(i);
			if (item != null) {
				if (item.itemID == DynamicEarth.bomb.itemID) {
					if (bomb != null) {
						return false;
					}
					bomb = item;
				} else if (item.itemID == DynamicEarth.earthbowl.itemID) {
					bowls++;
				} else if (item.itemID == Item.gunpowder.itemID) {
					gunpowder++;
				} else if (item.itemID == Item.silk.itemID) {
					string++;
				} else if (item.itemID == Item.fireballCharge.itemID) {
					fireCharges++;
				} else if (item.itemID == Item.fireworkCharge.itemID) {
					hasFireworks = true;
					if (item.hasTagCompound() && item.getTagCompound().hasKey("Explosion")) {
						list.appendTag(item.getTagCompound().getCompoundTag("Explosion"));
					}
				} else if (item.itemID == Item.potion.itemID) {
					if (potion != null) {
						return false;
					}
					potion = item;
				} else {
					return false;
				}
			}
		}
		if (bomb == null && bowls == 2 && string >= 1 && fireCharges <= 1
		&& (gunpowder >= 1 || fireCharges == 1 || hasFireworks)
		&& gunpowder <= maxGunpowder) {
			NBTTagCompound compound = new NBTTagCompound();
			result = new ItemStack(DynamicEarth.bomb);
			compound.setByte("Explosiveness", (byte)gunpowder);
			compound.setByte("Fuse Length", (byte)string);
			compound.setBoolean("Fire-charged", fireCharges == 1);
			if (hasFireworks) {
				compound.setTag("Explosions", list);
			}
			if (potion != null) {
				compound.setInteger("Potion-charged", potion.getItemDamage());
				if (potion.hasTagCompound() && potion.getTagCompound().hasKey("CustomPotionEffects")) {
					compound.setTag("CustomPotionEffects", potion.getTagCompound().getTagList("CustomPotionEffects"));
				}
			}
			result.setTagCompound(compound);
			return true;
		} else if (bomb != null && string >= 1
		&& bowls == 0 && gunpowder == 0 && fireCharges == 0 && !hasFireworks) {
			result = new ItemStack(DynamicEarth.bomb);
			NBTTagCompound compound;
			if (bomb.hasTagCompound()) {
				compound = (NBTTagCompound)bomb.getTagCompound().copy();
			} else {
				compound = new NBTTagCompound();
			}
			if (compound != null && compound.hasKey("Fuse Length")) {
				int fuseLength = string + compound.getByte("Fuse Length");
				if (fuseLength > ItemBombLit.maxFuseLength) {
					return false;
				}
				compound.setByte("Fuse Length", (byte)fuseLength);
			} else {
				if (compound == null) {
					compound = new NBTTagCompound();
				}
				compound.setByte("Fuse Length", (byte)(1 + string));
			}
			result.setTagCompound(compound);
			return true;
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		return MCHelper.getFixedNBTItemStack(result);
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}
}
