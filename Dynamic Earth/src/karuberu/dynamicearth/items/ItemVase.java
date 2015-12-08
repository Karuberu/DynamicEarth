package karuberu.dynamicearth.items;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import joptsimple.internal.Strings;

import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import karuberu.core.MCHelper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.DELogger;
import karuberu.dynamicearth.client.TextureManager;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import karuberu.dynamicearth.client.TextureManager.ItemIcon;
import karuberu.dynamicearth.fluids.FluidHandler;
import karuberu.dynamicearth.fluids.FluidHelper;

public class ItemVase extends Item {
	public static int capacity = 1000;
	public static boolean showMeasurement = true;
	public static List<Integer> liquids = new ArrayList<Integer>();
	
	private Icon
		vaseIcon,
		vaseSealedIcon,
		contentsIcon;

	public ItemVase(int id) {
		super(id);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}
	
	@Override
    public String getItemDisplayName(ItemStack itemStack) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.getItemDisplayName(itemStack));
		FluidStack liquid = ItemVase.getFluidStack(itemStack.getItemDamage());
		if (liquid != null) {
			String name = liquid.getFluid().getName();
			if (name != null) {
				builder.append(" of ");
				String[] words = name.split(" ");
				for (String word : words) {
					builder.append(Character.toUpperCase(word.charAt(0)));
					builder.append(word.substring(1));
					builder.append(" ");
				}
				if (words.length > 1) {
					builder.deleteCharAt(builder.length() - 1);
				}
			}
		}
        return builder.toString();
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List information, boolean bool) {
		if (showMeasurement) {
			FluidStack liquid = ItemVase.getFluidStack(itemStack.getItemDamage());
			if (liquid != null) {
				StringBuilder builder = new StringBuilder();
				builder.append(liquid.amount);
				if (liquid.amount < capacity) {
					builder.append("/").append(capacity);
				}
				builder.append(" mB");
				information.add(builder.toString());
			}
		}
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.vaseIcon = iconRegister.registerIcon(ItemIcon.VASE.getIconPath());
		this.vaseSealedIcon = iconRegister.registerIcon(ItemIcon.VASESEALED.getIconPath());
		this.contentsIcon = iconRegister.registerIcon(ItemIcon.VASECONTENTS.getIconPath());
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	@Override
	public Icon getIconFromDamageForRenderPass(int damage, int renderPass) {
		FluidStack liquid = ItemVase.getFluidStack(damage);
		if (liquid == null) {
			return this.vaseIcon;
		} else {
			if (liquid.getFluid().getColor() < 0) {
				return this.vaseSealedIcon;
			} else { 
				if (renderPass == 0) {
					return this.vaseIcon;
				} else {
					return this.contentsIcon;
				}
			}
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int renderPass) {
		if (renderPass > 0) {
			FluidStack liquid = ItemVase.getFluidStack(itemStack.getItemDamage());
			int color = FluidHelper.getFluidColor(liquid);
			if (liquid != null && color >= 0) {
				return color;
			}
		}
		return 0xFFFFFF;
	}
	
    @Override
    public int getDamage(ItemStack itemStack) {
    	int damage = super.getDamage(itemStack);
    	NBTTagCompound tagCompound = itemStack.getTagCompound();
    	if (tagCompound == null || !tagCompound.hasKey("damage")) {
    		this.setDamage(itemStack, damage);
    	} else {
    		int actualDamage = tagCompound.getInteger("damage");
    		if (damage != actualDamage) {
    			this.setDamage(itemStack, actualDamage);
    		}
			return actualDamage;
    	}
    	return damage;
    }
    
    @Override
    public void setDamage(ItemStack itemStack, int damage) {
    	NBTTagCompound tagCompound;
    	if (itemStack.hasTagCompound()) {
    		tagCompound = itemStack.getTagCompound();
    	} else {
    		tagCompound = new NBTTagCompound();
    	}
    	tagCompound.setInteger("damage", damage);
    	itemStack.setTagCompound(tagCompound);
    	super.setDamage(itemStack, damage);
    }
		
	@Override
	public boolean hasContainerItem() {
		return true;
	}
	    	
    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
		FluidStack contents = ItemVase.getFluidStack(itemStack.getItemDamage());
    	if (contents != null) {
    		return false;
    	}
        return true;
    }
    
    @Override
    public ItemStack getContainerItemStack(ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
        	NBTTagCompound tagCompound = itemStack.getTagCompound();
    		FluidStack contents = ItemVase.getFluidStack(itemStack.getItemDamage());
        	if (contents != null) {
        		if (tagCompound.hasKey("craftingDrain")) {
	        		contents.amount -= tagCompound.getInteger("craftingDrain");
	            	itemStack.setItemDamage(ItemVase.getDamage(contents));
        		} else {
        			itemStack.setItemDamage(0);
        		}
        	}
        } else {
            itemStack.setItemDamage(0);        	
        }
        return itemStack;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
   		FluidStack contents = ItemVase.getFluidStack(itemStack.getItemDamage());
    	if (contents != null) {
    		if (contents.containsFluid(FluidRegistry.getFluidStack(FluidHandler.MILK, 1000))
    	    || contents.containsFluid(FluidRegistry.getFluidStack(FluidHandler.SOUP, 250))) {
    			return 32;
    		}
    	}
        return super.getMaxItemUseDuration(itemStack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
   		FluidStack contents = ItemVase.getFluidStack(itemStack.getItemDamage());
    	if (contents != null) {
    		if (contents.containsFluid(FluidRegistry.getFluidStack(FluidHandler.MILK, 1000))
    		|| contents.containsFluid(FluidRegistry.getFluidStack(FluidHandler.SOUP, 250))) {
    			return EnumAction.drink;
    		}
    	}
        return super.getItemUseAction(itemStack);
    }

    @Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.getBlockId(x, y, z) == Block.cauldron.blockID) {
        	FluidStack contents = ItemVase.getFluidStack(itemStack.getItemDamage());
        	if (contents != null && contents.containsFluid(FluidRegistry.getFluidStack(FluidHandler.WATER, 1000))) {
            	int cauldronMeta = world.getBlockMetadata(x, y, z);
            	if (cauldronMeta < 3) {
            		world.setBlockMetadataWithNotify(x, y, z, 3, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
            		if (!player.capabilities.isCreativeMode) {
            			itemStack.setItemDamage(0);
            		}
            	}
        	}
        }
        return false;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    	FluidStack contents = ItemVase.getFluidStack(itemStack.getItemDamage());
        if (contents == null
    	|| (contents.getFluid().getBlockID() < Block.blocksList.length && Block.blocksList[contents.getFluid().getBlockID()].blockMaterial.isLiquid())) {
			float var4 = 1.0F;
	        double playerPosX = player.prevPosX + (player.posX - player.prevPosX) * var4;
	        double playerPosY = player.prevPosY + (player.posY - player.prevPosY) * var4 + 1.62D - player.yOffset;
	        double playerPosZ = player.prevPosZ + (player.posZ - player.prevPosZ) * var4;
	        MovingObjectPosition movingObjectPosition = this.getMovingObjectPositionFromPlayer(world, player, contents == null || contents.amount < 1000);
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
	                if (contents == null) {
	                    FillBucketEvent event = new FillBucketEvent(player, new ItemStack(Item.bucketEmpty), world, movingObjectPosition);
	                    if (MinecraftForge.EVENT_BUS.post(event)) {
	                        return itemStack;
	                    }
	                    if (event.getResult() == Event.Result.ALLOW) {
	                        FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(event.result);
	                        if (fluid != null) {
		                        world.setBlockToAir(movingObjectPosX, movingObjectPosY, movingObjectPosZ);
		                        if (!player.canPlayerEdit(movingObjectPosX, movingObjectPosY, movingObjectPosZ, movingObjectPosition.sideHit, itemStack)) {
		                            return itemStack;
		                        }
		                        if (player.capabilities.isCreativeMode) {
		                            return itemStack;
		                        }
	                        	itemStack.setItemDamage(ItemVase.getDamage(fluid));
	                            return itemStack;
	                        }
	                        return itemStack;
	                    } else {
		                	Material material = world.getBlockMaterial(movingObjectPosX, movingObjectPosY, movingObjectPosZ);
		                	int blockID = world.getBlockId(movingObjectPosX, movingObjectPosY, movingObjectPosZ);
		                    int metadata = world.getBlockMetadata(movingObjectPosX, movingObjectPosY, movingObjectPosZ);
		                    if (material == Material.lava && metadata == 0) {
		                    	player.renderBrokenItemStack(itemStack);
		                      	player.destroyCurrentEquippedItem();
		                    	player.setFire(15);
		                        return itemStack;
		                    } else if (material == Material.water && metadata == 0) {
		                    	world.setBlockToAir(movingObjectPosX, movingObjectPosY, movingObjectPosZ);
		                        if (player.capabilities.isCreativeMode) {
		                            return itemStack;
		                        }
		                        if (itemStack.stackSize == 1) {
		                        	itemStack.setItemDamage(ItemVase.getDamage(new FluidStack(FluidRegistry.WATER, 1000)));
		                            return itemStack;
		                        } else {
		                        	itemStack.stackSize--;
		                        	ItemStack filledVase = new ItemStack(DynamicEarth.vase.itemID, 1, ItemVase.getDamage(new FluidStack(FluidRegistry.WATER, 1000)));
		                        	boolean added = player.inventory.addItemStackToInventory(filledVase);
		                        	if (!added) {
		                        		player.dropPlayerItem(filledVase);
		                        	}
		                        }
		                        return itemStack;
		                    }
	                    }
	                } else {
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
	                    if (this.tryPlaceContainedLiquid(itemStack, world, playerPosX, playerPosY, playerPosZ, movingObjectPosX, movingObjectPosY, movingObjectPosZ)
	                    && !player.capabilities.isCreativeMode) {
	                    	contents.amount -= 1000;
	                    	itemStack.setItemDamage(ItemVase.getDamage(contents));
	                    	return itemStack;
	                    }
	                }
	            }
	            return itemStack;
		    }
        } else if (contents != null) {
        	if (contents.containsFluid(FluidRegistry.getFluidStack(FluidHandler.MILK, 1000))) {
        		player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        	} else if (contents.containsFluid(FluidRegistry.getFluidStack(FluidHandler.SOUP, 250))
        	&& player.canEat(false)) {
        		player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        	}
        }
        return itemStack;
    }
    
    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
    	FluidStack contents = ItemVase.getFluidStack(itemStack.getItemDamage());
    	if (contents != null) {
    		if (contents.containsFluid(FluidRegistry.getFluidStack(FluidHandler.MILK, 1000))) {
            	if (!player.capabilities.isCreativeMode) {
                    contents.amount -= 1000;
                }
                if (!world.isRemote) {
                    player.curePotionEffects(new ItemStack(Item.bucketMilk));
                }
        	} else if (contents.containsFluid(FluidRegistry.getFluidStack(FluidHandler.SOUP, 250))) {
                world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
                player.getFoodStats().addStats((ItemFood)Item.bowlSoup);
                if (!player.capabilities.isCreativeMode) {
                    contents.amount -= 250;
                }
        	}
        	itemStack.setItemDamage(ItemVase.getDamage(contents));
    	}
        return itemStack;
    }
    
    public boolean tryPlaceContainedLiquid(ItemStack itemStack, World world, double playerPosX, double playerPosY, double playerPosZ, int movingObjectPosX, int movingObjectPosY, int movingObjectPosZ) {
    	FluidStack contents = ItemVase.getFluidStack(itemStack.getItemDamage());
        if (contents == null) {
            return false;
        } else if (!world.isAirBlock(movingObjectPosX, movingObjectPosY, movingObjectPosZ) && world.getBlockMaterial(movingObjectPosX, movingObjectPosY, movingObjectPosZ).isSolid()) {
            return false;
        } else {
            if (world.provider.isHellWorld
            && contents.containsFluid(FluidRegistry.getFluidStack(FluidHandler.WATER, 1000))) {
                world.playSoundEffect(playerPosX + 0.5D, playerPosY + 0.5D, playerPosZ + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                for (int var11 = 0; var11 < 8; ++var11) {
                    world.spawnParticle("largesmoke", movingObjectPosX + Math.random(), movingObjectPosY + Math.random(), movingObjectPosZ + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            } else if (contents.getFluid().canBePlacedInWorld()){
            	if (contents.containsFluid(FluidRegistry.getFluidStack(FluidHandler.WATER, 1000))) {
           			world.setBlock(movingObjectPosX, movingObjectPosY, movingObjectPosZ, contents.getFluid().getBlockID() - 1, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
            	} else if (contents.containsFluid(FluidRegistry.getFluidStack(FluidHelper.REDSTONE, 1000))
                || contents.containsFluid(FluidRegistry.getFluidStack(FluidHelper.GLOWSTONE, 1000))) {
            		world.setBlock(movingObjectPosX, movingObjectPosY, movingObjectPosZ, contents.fluidID, 7, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
            	} else if (contents.containsFluid(FluidRegistry.getFluidStack(FluidHelper.ENDER, 1000))) {
            		world.setBlock(movingObjectPosX, movingObjectPosY, movingObjectPosZ, contents.fluidID, 3, MCHelper.NOTIFY_AND_UPDATE_REMOTE);           		
            	} else {
            		return false;
            	}
            }
            return true;
        }
    }
    
    @Override
    public boolean getHasSubtypes() {
    	return true;
    }
    
    @Override
    public void getSubItems(int id, CreativeTabs creativeTabs, List list) {
    	list.add(new ItemStack(id, 1, 0));
    	for (Integer damage : liquids) {
    		list.add(new ItemStack(id, 1, damage));
    	}
    }
    
    public static int getDamage(int id, int amount) {
    	if (amount <= 0) {
    		return 0;
    	} else {
    		ByteBuffer bb = ByteBuffer.wrap(new byte[4]).putShort((short)id).putShort((short)amount);
    		bb.rewind();
    		return bb.getInt();
    	}    	
    }
    public static int getDamage(FluidStack liquid) {
    	return liquid == null ? 0 : ItemVase.getDamage(liquid.fluidID, liquid.amount);
    }
    
    public static FluidStack getFluidStack(int damage) {
    	ByteBuffer bb = ByteBuffer.wrap(new byte[4]).putInt(damage);
    	bb.rewind();
    	int id = bb.getShort();
    	int amount = bb.getShort();
    	if (id > 0 && amount > 0) {
    		return new FluidStack(id, amount);
    	} else {
    		return null;
    	}
    }
    
    public static boolean canPickupLiquid(int id) {
    	return id < Block.blocksList.length && Block.blocksList[id].blockMaterial.isLiquid();
    }
}