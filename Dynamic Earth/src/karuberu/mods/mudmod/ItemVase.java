package karuberu.mods.mudmod;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPig;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class ItemVase extends Item {
    /** field for checking if the bucket has been filled. */
    private int contents;

    public ItemVase(int i, int contents) {
        super(i);
        this.maxStackSize = 1;
        this.contents = contents;
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public String getTextureFile() {
    	return MudMod.itemsFile;
    }

    // Very hacky way to milk a cow without editing the cow class.
    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityLiving entityLiving) {
        if (entityLiving instanceof EntityCow) {
        	EntityCow cow = (EntityCow)entityLiving;
        	if (cow.getGrowingAge() >= 0) {
            	itemStack.itemID = MudMod.vaseMilk.shiftedIndex;
                return true;
        	}
        }
		return false;
    }
    
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        float var4 = 1.0F;
        double playerPosX = player.prevPosX + (player.posX - player.prevPosX) * var4;
        double playerPosY = player.prevPosY + (player.posY - player.prevPosY) * var4 + 1.62D - player.yOffset;
        double playerPosZ = player.prevPosZ + (player.posZ - player.prevPosZ) * var4;
        MovingObjectPosition movingObjectPosition = this.getMovingObjectPositionFromPlayer(world, player, this.contents == 0);

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

                if (this.contents == 0) {
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
                    if (this.contents < 0) {
                        return new ItemStack(MudMod.vase);
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

                    if (this.doPlaceContents(world, playerPosX, playerPosY, playerPosZ, movingObjectPosX, movingObjectPosY, movingObjectPosZ) && !player.capabilities.isCreativeMode) {
                        return new ItemStack(MudMod.vase);
                    }
                }
            } else if (this.contents == 0 && movingObjectPosition.entityHit instanceof EntityCow) {
        		return new ItemStack(MudMod.vaseMilk);
        	}

            return itemStack;
        }
    }

    public boolean doPlaceContents(World world, double playerPosX, double playerPosY, double playerPosZ, int movingObjectPosX, int movingObjectPosY, int movingObjectPosZ) {
        if (this.contents <= 0) {
            return false;
        } else if (!world.isAirBlock(movingObjectPosX, movingObjectPosY, movingObjectPosZ) && world.getBlockMaterial(movingObjectPosX, movingObjectPosY, movingObjectPosZ).isSolid()) {
            return false;
        } else {
            if (world.provider.isHellWorld && this.contents == Block.waterMoving.blockID) {
                world.playSoundEffect(playerPosX + 0.5D, playerPosY + 0.5D, playerPosZ + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                for (int var11 = 0; var11 < 8; ++var11) {
                    world.spawnParticle("largesmoke", movingObjectPosX + Math.random(), movingObjectPosY + Math.random(), movingObjectPosZ + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            } else {
                world.setBlockAndMetadataWithNotify(movingObjectPosX, movingObjectPosY, movingObjectPosZ, this.contents, 0);
            }
            return true;
        }
    }

}
