package karuberu.mods.mudmod.items;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import karuberu.core.MCHelper;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import karuberu.mods.mudmod.liquids.LiquidHandler;
import karuberu.mods.mudmod.liquids.LiquidHelper;

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
		builder.append(StringTranslate.getInstance().translateNamedKey(this.getLocalizedName(itemStack)));
		LiquidStack liquid = ItemVase.getLiquidStack(itemStack.getItemDamage());
		if (liquid != null) {
			builder.append(" of ").append(liquid.asItemStack().getDisplayName());
		}
        return builder.toString();
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List information, boolean bool) {
		if (showMeasurement) {
			LiquidStack liquid = ItemVase.getLiquidStack(itemStack.getItemDamage());
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
		this.itemIcon = this.vaseIcon = TextureManager.instance().getItemTexture(Texture.VASE);
		this.vaseSealedIcon = TextureManager.instance().getItemTexture(Texture.VASESEALED);
		this.contentsIcon = TextureManager.instance().getItemTexture(Texture.VASECONTENTS);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	@Override
	public Icon getIconFromDamageForRenderPass(int damage, int renderPass) {
		LiquidStack liquid = ItemVase.getLiquidStack(damage);
		if (liquid == null) {
			return this.vaseIcon;
		} else {
			if (LiquidHelper.getLiquidColor(liquid) < 0) {
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
			LiquidStack liquid = ItemVase.getLiquidStack(itemStack.getItemDamage());
			int color = LiquidHelper.getLiquidColor(liquid);
			if (liquid != null && color >= 0) {
				return color;
			}
		}
		return 0xFFFFFF;
	}
	
    @Override
    public int getItemDamageFromStack(ItemStack itemStack) {
    	int damage = super.getItemDamageFromStack(itemStack);
    	NBTTagCompound tagCompound = itemStack.getTagCompound();
    	if (tagCompound == null || !tagCompound.hasKey("damage")) {
    		this.setItemDamageForStack(itemStack, damage);
    	} else {
    		int actualDamage = tagCompound.getInteger("damage");
    		if (damage != actualDamage) {
    			this.setItemDamageForStack(itemStack, actualDamage);
    		}
			return actualDamage;
    	}
    	return damage;
    }
    
    @Override
    public void setItemDamageForStack(ItemStack itemStack, int damage) {
    	NBTTagCompound tagCompound;
    	if (itemStack.hasTagCompound()) {
    		tagCompound = itemStack.getTagCompound();
    	} else {
    		tagCompound = new NBTTagCompound();
    	}
    	tagCompound.setInteger("damage", damage);
    	itemStack.setTagCompound(tagCompound);
    	super.setItemDamageForStack(itemStack, damage);
    }
		
	@Override
	public boolean hasContainerItem() {
		return true;
	}
	    	
    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
		LiquidStack contents = ItemVase.getLiquidStack(itemStack.getItemDamage());
    	if (contents != null) {
    		return false;
    	}
        return true;
    }
    
    @Override
    public ItemStack getContainerItemStack(ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
        	NBTTagCompound tagCompound = itemStack.getTagCompound();
    		LiquidStack contents = ItemVase.getLiquidStack(itemStack.getItemDamage());
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
   		LiquidStack contents = ItemVase.getLiquidStack(itemStack.getItemDamage());
    	if (contents != null) {
    		if (contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHandler.MILK, 1000))
    	    || contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHandler.SOUP, 250))) {
    			return 32;
    		}
    	}
        return super.getMaxItemUseDuration(itemStack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
   		LiquidStack contents = ItemVase.getLiquidStack(itemStack.getItemDamage());
    	if (contents != null) {
    		if (contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHandler.MILK, 1000))
    		|| contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHandler.SOUP, 250))) {
    			return EnumAction.drink;
    		}
    	}
        return super.getItemUseAction(itemStack);
    }

    @Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.getBlockId(x, y, z) == Block.cauldron.blockID) {
        	LiquidStack contents = ItemVase.getLiquidStack(itemStack.getItemDamage());
        	if (contents != null && contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHandler.WATER, 1000))) {
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
    	LiquidStack contents = ItemVase.getLiquidStack(itemStack.getItemDamage());
        if (contents == null
    	|| (contents.itemID < Block.blocksList.length && Block.blocksList[contents.itemID].blockMaterial.isLiquid())) {
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
	                	Material material = world.getBlockMaterial(movingObjectPosX, movingObjectPosY, movingObjectPosZ);
	                	int blockID = world.getBlockId(movingObjectPosX, movingObjectPosY, movingObjectPosZ);
	                    int metadata = world.getBlockMetadata(movingObjectPosX, movingObjectPosY, movingObjectPosZ);
	                    if (material == Material.lava && metadata == 0) {
	                    	player.renderBrokenItemStack(itemStack);
	                      	player.destroyCurrentEquippedItem();
	                    	player.setFire(15);
	                        return itemStack;
	                    }
	                    LiquidStack liquid = new LiquidStack(Block.blocksList[blockID], metadata);
	                	if (LiquidDictionary.getCanonicalLiquid(liquid) != null) {
	                        world.setBlock(movingObjectPosX, movingObjectPosY, movingObjectPosZ, 0, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
	                        if (!player.canPlayerEdit(movingObjectPosX, movingObjectPosY, movingObjectPosZ, movingObjectPosition.sideHit, itemStack)) {
	                            return itemStack;
	                        }
	                        if (player.capabilities.isCreativeMode) {
	                            return itemStack;
	                        }
	                        liquid.amount = 1000;
                        	itemStack.setItemDamage(ItemVase.getDamage(liquid));
                            return itemStack;
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
        	if (contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHandler.MILK, 1000))) {
        		player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        	} else if (contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHandler.SOUP, 250))
        	&& player.canEat(false)) {
        		player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        	}
        }
        return itemStack;
    }
    
    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
    	LiquidStack contents = ItemVase.getLiquidStack(itemStack.getItemDamage());
    	if (contents != null) {
    		if (contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHandler.MILK, 1000))) {
            	if (!player.capabilities.isCreativeMode) {
                    contents.amount -= 1000;
                }
                if (!world.isRemote) {
                    player.curePotionEffects(new ItemStack(Item.bucketMilk));
                }
        	} else if (contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHandler.SOUP, 250))) {
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
    	LiquidStack contents = ItemVase.getLiquidStack(itemStack.getItemDamage());
        if (contents == null) {
            return false;
        } else if (!world.isAirBlock(movingObjectPosX, movingObjectPosY, movingObjectPosZ) && world.getBlockMaterial(movingObjectPosX, movingObjectPosY, movingObjectPosZ).isSolid()) {
            return false;
        } else {
            if (world.provider.isHellWorld
            && contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHandler.WATER, 1000))) {
                world.playSoundEffect(playerPosX + 0.5D, playerPosY + 0.5D, playerPosZ + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                for (int var11 = 0; var11 < 8; ++var11) {
                    world.spawnParticle("largesmoke", movingObjectPosX + Math.random(), movingObjectPosY + Math.random(), movingObjectPosZ + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            } else {
            	if (contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHandler.WATER, 1000))) {
	                world.setBlock(movingObjectPosX, movingObjectPosY, movingObjectPosZ, contents.itemID - 1, contents.itemMeta, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
            	} else if (contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHelper.REDSTONE, 1000))
                || contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHelper.GLOWSTONE, 1000))) {
            		world.setBlock(movingObjectPosX, movingObjectPosY, movingObjectPosZ, contents.itemID, 7, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
            	} else if (contents.containsLiquid(LiquidDictionary.getLiquid(LiquidHelper.ENDER, 1000))) {
            		world.setBlock(movingObjectPosX, movingObjectPosY, movingObjectPosZ, contents.itemID, 3, MCHelper.NOTIFY_AND_UPDATE_REMOTE);           		
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
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
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
    public static int getDamage(LiquidStack liquid) {
    	return liquid == null ? 0 : ItemVase.getDamage(liquid.itemID, liquid.amount);
    }
    
    public static LiquidStack getLiquidStack(int damage) {
    	ByteBuffer bb = ByteBuffer.wrap(new byte[4]).putInt(damage);
    	bb.rewind();
    	int id = bb.getShort();
    	int amount = bb.getShort();
    	if (id > 0 && amount > 0) {
    		return new LiquidStack(id, amount);
    	} else {
    		return null;
    	}
    }
    
    public static boolean canPickupLiquid(int id) {
    	return id < Block.blocksList.length && Block.blocksList[id].blockMaterial.isLiquid();
    }
}