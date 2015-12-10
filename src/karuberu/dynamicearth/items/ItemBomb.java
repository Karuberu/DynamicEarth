package karuberu.dynamicearth.items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import karuberu.core.util.Helper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.client.TextureManager.ItemIcon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemFireworkCharge;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBomb extends ItemDynamicEarth {
	@SuppressWarnings("unused")
	private ItemIcon iconTexture;
	public static CreativeTabs
		creativeTab = CreativeTabs.tabCombat;
	private ItemStack
		basicBomb,
		flashyBomb;

	public ItemBomb(int id, ItemIcon icon) {
		super(id, icon);
		this.iconTexture = icon;
		this.setCreativeTab(creativeTab);
		this.setMaxStackSize(16);
		this.initializeItemStacks();
	}
	
	protected void initializeItemStacks() {
		NBTTagCompound basicBombCompound = new NBTTagCompound();
		this.basicBomb = new ItemStack(this.itemID, 1, 0);
		basicBombCompound.setByte("Explosiveness", (byte)1);
		basicBombCompound.setByte("Fuse Length", (byte)1);
		basicBombCompound.setBoolean("Fire-charged", false);
		this.basicBomb.setTagCompound(basicBombCompound);

		NBTTagCompound flashyBombCompound = new NBTTagCompound();
		NBTTagCompound explosions = new NBTTagCompound();
		NBTTagList explosionList = new NBTTagList();
		this.flashyBomb = new ItemStack(this.itemID, 1, 0);
		flashyBombCompound.setByte("Explosiveness", (byte)4);
		flashyBombCompound.setByte("Fuse Length", (byte)6);
		flashyBombCompound.setBoolean("Fire-charged", true);
		explosions.setByte("Type", (byte)1);
		explosions.setIntArray("Colors", new int[] { ItemDye.dyeColors[1], ItemDye.dyeColors[4], ItemDye.dyeColors[10] });
		explosions.setBoolean("Flicker", true);
		explosionList.appendTag(explosions);
		flashyBombCompound.setTag("Explosions", explosionList);
		this.flashyBomb.setTagCompound(flashyBombCompound);
	}

	@SideOnly(Side.CLIENT)
	@Override
	@SuppressWarnings("rawtypes")
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
		ItemStack litBomb = new ItemStack(DynamicEarth.bombLit);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		if (compound.hasKey("Potion-charged")) {
			List effects = ItemBombLit.getSplashEffects(compound);
			if (effects != null && !effects.isEmpty()) {
				Iterator iterator = effects.iterator();
				while (iterator.hasNext()) {
					StringBuilder builder = new StringBuilder();
					PotionEffect effect = (PotionEffect)iterator.next();
					builder.append(StatCollector.translateToLocal(effect.getEffectName()).trim());
					if (effect.getAmplifier() > 0) {
						builder.append(" ").append(StatCollector.translateToLocal("potion.potency." + effect.getAmplifier()).trim());
					}
					if (effect.getDuration() > 20) {
						builder.append(" (").append(Potion.getDurationString(effect)).append(")");
					}
					if (Potion.potionTypes[effect.getPotionID()].isBadEffect()) {
						builder.insert(0, EnumChatFormatting.RED);
					} else {
						builder.insert(0, EnumChatFormatting.WHITE);
					}
					information.add(builder.toString());
				}
			}
		}
		return information;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSubItems(int id, CreativeTabs creativeTab, List list) {
		list.add(Helper.getFixedNBTItemStack(this.basicBomb));
		list.add(Helper.getFixedNBTItemStack(this.flashyBomb));
	}
}
