package karuberu.mods.mudmod.items;

import org.omg.CORBA.SystemException;

import karuberu.core.MCHelper;
import karuberu.mods.mudmod.MudMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class ItemVase extends ItemMudMod {
    /** field for checking if the bucket has been filled. */
    private int contents;

    public ItemVase(int i, int contents, int icon) {
        super(i, icon);
        this.maxStackSize = 1;
        this.contents = contents;
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setContainerItem(MudMod.vase);
    }
        
    @Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (this.contents > 0) {
            if (world.getBlockId(x, y, z) == Block.cauldron.blockID) {
            	int cauldronMeta = world.getBlockMetadata(x, y, z);
            	if (cauldronMeta < 3) {
            		world.setBlockMetadataWithNotify(x, y, z, 3);
            		if (!player.capabilities.isCreativeMode) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(this.getContainerItem()));
            		}
            	}
            }
        }
        return false;
    }
    
    @Override
	public ItemStack onItemRightClick(ItemStack itemStack,  World world, EntityPlayer player) {
        float var4 = 1.0F;
        double playerPosX = player.prevPosX + (player.posX - player.prevPosX) * var4;
        double playerPosY = player.prevPosY + (player.posY - player.prevPosY) * var4 + 1.62D - player.yOffset;
        double playerPosZ = player.prevPosZ + (player.posZ - player.prevPosZ) * var4;
        MovingObjectPosition movingObjectPosition = this.getMovingObjectPositionFromPlayer(world, player, this.contents == 0);

        if (movingObjectPosition == null) {
            return itemStack;
        } else {
            FillBucketEvent event = new FillBucketEvent(player, itemStack, world, movingObjectPosition);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return itemStack;
            }
            if (event.getResult() == Event.Result.ALLOW) {
                if (player.capabilities.isCreativeMode) {
                    return itemStack;
                }            	
                if (--itemStack.stackSize <= 0) {
                    return event.result;
                }
                if (!player.inventory.addItemStackToInventory(new ItemStack(MudMod.vaseWater))) {
                    player.dropPlayerItem(event.result);
                }
                return itemStack;
            }
            
        	if (movingObjectPosition.typeOfHit == EnumMovingObjectType.TILE) {
                int movingObjectPosX = movingObjectPosition.blockX;
                int movingObjectPosY = movingObjectPosition.blockY;
                int movingObjectPosZ = movingObjectPosition.blockZ;

                if (!world.canMineBlock(player, movingObjectPosX, movingObjectPosY, movingObjectPosZ)) {
                    return itemStack;
                }

                if (this.contents == 0) {
                    if (world.getBlockMaterial(movingObjectPosX, movingObjectPosY, movingObjectPosZ) == Material.water && world.getBlockMetadata(movingObjectPosX, movingObjectPosY, movingObjectPosZ) == 0) {
                        world.setBlockAndMetadataWithNotify(movingObjectPosX, movingObjectPosY, movingObjectPosZ, 0, 0);

                        if (!player.canPlayerEdit(movingObjectPosX, movingObjectPosY, movingObjectPosZ, movingObjectPosition.sideHit, itemStack)) {
                            return itemStack;
                        }
                        
                        if (player.capabilities.isCreativeMode) {
                            return itemStack;
                        }

                        if (--itemStack.stackSize <= 0) {
                            return new ItemStack(MudMod.vaseWater);
                        }

                        if (!player.inventory.addItemStackToInventory(new ItemStack(MudMod.vaseWater))) {
                            player.dropPlayerItem(new ItemStack(MudMod.vaseWater.itemID, 1, 0));
                        }

                        return itemStack;
                    }

                    if (world.getBlockMaterial(movingObjectPosX, movingObjectPosY, movingObjectPosZ) == Material.lava && world.getBlockMetadata(movingObjectPosX, movingObjectPosY, movingObjectPosZ) == 0) {
                    	player.renderBrokenItemStack(itemStack);
                      	player.destroyCurrentEquippedItem();
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

                    if (!player.canPlayerEdit(movingObjectPosX, movingObjectPosY, movingObjectPosZ, movingObjectPosition.sideHit, itemStack)) {
                        return itemStack;
                    }

                    if (this.tryPlaceContainedLiquid(world, playerPosX, playerPosY, playerPosZ, movingObjectPosX, movingObjectPosY, movingObjectPosZ) && !player.capabilities.isCreativeMode) {
                        return new ItemStack(MudMod.vase);
                    }
                }
            }
        	
            return itemStack;
        }
    }

    public boolean tryPlaceContainedLiquid(World world, double playerPosX, double playerPosY, double playerPosZ, int movingObjectPosX, int movingObjectPosY, int movingObjectPosZ) {
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
