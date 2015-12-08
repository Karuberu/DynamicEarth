package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.entity.EntityMudball;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMudBlob extends ItemMudMod {

	public ItemMudBlob(int i) {
		super(i);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
    
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            --par1ItemStack.stackSize;
        }
        par2World.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote) {
            par2World.spawnEntityInWorld(new EntityMudball(par2World, player));
        }
        return par1ItemStack;
    }
}
