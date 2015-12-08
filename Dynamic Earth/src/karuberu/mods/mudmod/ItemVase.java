package karuberu.mods.mudmod;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class ItemVase extends Item {
    /** field for checking if the bucket has been filled. */
    private int isFull;

    public ItemVase(int i, int contents) {
        super(i);
        this.maxStackSize = 1;
        this.isFull = contents;
        this.setTabToDisplayOn(CreativeTabs.tabMisc);
    }

    @Override
    public String getTextureFile() {
    	return MudMod.itemsFile;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        float var4 = 1.0F;
        double playerPosX = player.prevPosX + (player.posX - player.prevPosX) * (double)var4;
        double playerPosY = player.prevPosY + (player.posY - player.prevPosY) * (double)var4 + 1.62D - (double)player.yOffset;
        double playerPosZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)var4;
        boolean isEmpty = this.isFull == 0;
        MovingObjectPosition movingObjectPosition = this.getMovingObjectPositionFromPlayer(world, player, isEmpty);

        if (movingObjectPosition == null) {
            return itemStack;
        } else {
            if (movingObjectPosition.typeOfHit == EnumMovingObjectType.TILE) {
                int movingObjectPosX = movingObjectPosition.blockX;
                int movingObjectPosY = movingObjectPosition.blockY;
                int movingObjectPosZ = movingObjectPosition.blockZ;

                if (!world.canMineBlock(player, movingObjectPosX, movingObjectPosY, movingObjectPosZ)) {
                    return itemStack;
                }

                if (this.isFull == 0) {
                    if (!player.canPlayerEdit(movingObjectPosX, movingObjectPosY, movingObjectPosZ)) {
                        return itemStack;
                    }

                    if (world.getBlockMaterial(movingObjectPosX, movingObjectPosY, movingObjectPosZ) == Material.water && world.getBlockMetadata(movingObjectPosX, movingObjectPosY, movingObjectPosZ) == 0) {
                        world.setBlockWithNotify(movingObjectPosX, movingObjectPosY, movingObjectPosZ, 0);

                        if (player.capabilities.isCreativeMode) {
                            return itemStack;
                        }

                        if (--itemStack.stackSize <= 0) {
                            return new ItemStack(MudMod.vaseWater);
                        }

                        if (!player.inventory.addItemStackToInventory(new ItemStack(MudMod.vaseWater))) {
                            player.dropPlayerItem(new ItemStack(MudMod.vaseWater.shiftedIndex, 1, 0));
                        }

                        return itemStack;
                    }

                    if (world.getBlockMaterial(movingObjectPosX, movingObjectPosY, movingObjectPosZ) == Material.lava && world.getBlockMetadata(movingObjectPosX, movingObjectPosY, movingObjectPosZ) == 0) {
                    	--itemStack.stackSize;
                    	player.setFire(15);
                        return itemStack;
                    }
                } else {
                    if (this.isFull < 0) {
                        return new ItemStack(Item.bucketEmpty);
                    }

                    if (movingObjectPosition.sideHit == 0) {
                        --movingObjectPosY;
                    }

                    if (movingObjectPosition.sideHit == 1) {
                        ++movingObjectPosY;
                    }

                    if (movingObjectPosition.sideHit == 2) {
                        --movingObjectPosZ;
                    }

                    if (movingObjectPosition.sideHit == 3) {
                        ++movingObjectPosZ;
                    }

                    if (movingObjectPosition.sideHit == 4) {
                        --movingObjectPosX;
                    }

                    if (movingObjectPosition.sideHit == 5) {
                        ++movingObjectPosX;
                    }

                    if (!player.canPlayerEdit(movingObjectPosX, movingObjectPosY, movingObjectPosZ)) {
                        return itemStack;
                    }

                    if (this.func_77875_a(world, playerPosX, playerPosY, playerPosZ, movingObjectPosX, movingObjectPosY, movingObjectPosZ) && !player.capabilities.isCreativeMode) {
                        return new ItemStack(MudMod.vase);
                    }
                }
            } else if (this.isFull == 0 && movingObjectPosition.entityHit instanceof EntityCow) {
                return new ItemStack(Item.bucketMilk);
            }

            return itemStack;
        }
    }

    public boolean func_77875_a(World par1World, double par2, double par4, double par6, int par8, int par9, int par10)
    {
        if (this.isFull <= 0)
        {
            return false;
        }
        else if (!par1World.isAirBlock(par8, par9, par10) && par1World.getBlockMaterial(par8, par9, par10).isSolid())
        {
            return false;
        }
        else
        {
            if (par1World.provider.isHellWorld && this.isFull == Block.waterMoving.blockID)
            {
                par1World.playSoundEffect(par2 + 0.5D, par4 + 0.5D, par6 + 0.5D, "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);

                for (int var11 = 0; var11 < 8; ++var11)
                {
                    par1World.spawnParticle("largesmoke", (double)par8 + Math.random(), (double)par9 + Math.random(), (double)par10 + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            }
            else
            {
                par1World.setBlockAndMetadataWithNotify(par8, par9, par10, this.isFull, 0);
            }

            return true;
        }
    }

}
