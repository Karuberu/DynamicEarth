package karuberu.mods.mudmod.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import karuberu.core.KaruberuLogger;
import karuberu.core.MCHelper;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import karuberu.mods.mudmod.entity.EntityBomb;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBombLit extends ItemMudMod {

	public static int
		fuseBurnTime = 20,
		maxFuseLength = 6;
	private byte
		fuseLength = 1,
		explosiveness = 1;
	private boolean
		fireCharged = false;

	public ItemBombLit(int id, Texture icon) {
		super(id, icon);
		this.setMaxStackSize(1);
		this.setMaxDamage(getMaxFuseBurnTime());
		this.setNoRepair();
    }
	
	public int getFuseLength() {
		return fuseLength;
	}
	
	public int getExplosiveness() {
		return explosiveness;
	}
	
	public boolean getFireCharged() {
		return fireCharged;
	}
	
    private int getDamagePerTick() {
    	return getMaxFuseBurnTime() / (fuseBurnTime * fuseLength);
    }
    
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List information, boolean bool) {
		NBTTagCompound compound = itemStack.getTagCompound();
		information = ItemBomb.getInformationFromTagCompound(information, compound);
	}
	
	@Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		NBTTagCompound compound;
		if (itemStack.hasTagCompound()) {
			compound = itemStack.getTagCompound();
		} else {
			compound = new NBTTagCompound();
		}
		if (compound.hasKey("Explosiveness")) {
			this.explosiveness = compound.getByte("Explosiveness");
		} else {
			this.explosiveness = 1;
			compound.setByte("Explosiveness", this.explosiveness);
		}
		if (compound.hasKey("Fuse Length")) {
			this.fuseLength = compound.getByte("Fuse Length");
		} else {
			this.fuseLength = 1;
			compound.setByte("Fuse Length", this.fuseLength);
		}
		if (compound.hasKey("Fire-charged")) {
			this.fireCharged = compound.getBoolean("Fire-charged");
		} else {
			this.fireCharged = false;
			compound.setBoolean("Fire-charged", this.fireCharged);
		}
		itemStack.setTagCompound(compound);
	}
	
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
    	if (entity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer)entity;
	    	int damage = itemStack.getItemDamage();
	    	if (damage + this.getDamagePerTick() > itemStack.getMaxDamage()) {
	    		int slot = MCHelper.getItemIndexInInventory(player.inventory, itemStack);
	    		if (slot > -1) {
	    			ItemStack bombStack = getUnlitBombStack(itemStack);
	    			if (bombStack.stackSize <= 0) {
	    				bombStack = null;
	    			}
	    			player.inventory.setInventorySlotContents(slot, bombStack);
	    			if (!world.isRemote) {
	    				itemStack.setItemDamage(itemStack.getMaxDamage());
	    				world.spawnEntityInWorld(new EntityBomb(world, player, itemStack));
	    			}
	    		}
	    	} else {
				itemStack.damageItem(this.getDamagePerTick(), player);
			}
	    }
	}
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		ItemBombLit.spawnThrownBomb(world, player, itemStack);
		return getUnlitBombStack(itemStack);
    }
    
    private static ItemStack getUnlitBombStack(ItemStack itemStack) {
		if (!itemStack.hasTagCompound()
		|| !itemStack.getTagCompound().hasKey("Stack Size")) {
			return new ItemStack(MudMod.bomb, 0);
		}
		byte stackSize = itemStack.getTagCompound().getByte("Stack Size");
		if (stackSize <= 0) {
			return new ItemStack(MudMod.bomb, 0);
		}
    	ItemStack returnStack = new ItemStack(MudMod.bomb, stackSize);
		returnStack.setTagCompound(itemStack.getTagCompound());
		return returnStack;
    }
    
    public static int getMaxFuseBurnTime() {
    	return fuseBurnTime * maxFuseLength * 10;
    }
        
    public static int getTrueFuseLength(ItemStack bombStack) {
    	return (bombStack.getMaxDamage() - bombStack.getItemDamage()) / ((ItemBombLit)bombStack.getItem()).getDamagePerTick();
    }
    
	public static void spawnThrownBomb(World world, EntityPlayer player, ItemStack itemStack) {
        if (!world.isRemote && itemStack != null && itemStack.getItem() instanceof ItemBombLit) {
        	EntityBomb bomb = new EntityBomb(world, player, itemStack);
            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            world.spawnEntityInWorld(bomb);
        }
	}
}
