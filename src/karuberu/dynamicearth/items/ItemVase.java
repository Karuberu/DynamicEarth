package karuberu.dynamicearth.items;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import karuberu.core.util.FluidHelper;
import karuberu.core.util.FluidHelper.FluidReference;
import karuberu.core.util.Helper;
import karuberu.core.util.block.BlockSide;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.client.TextureManager.ItemIcon;
import karuberu.dynamicearth.fluids.FluidColorRegistry;
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
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemVase extends ItemDynamicEarth {
	public static int
		capacity = 1000,
		maxTemperature = 440,
		minTemperature = 130;
	public static boolean
		showMeasurement = true;
	public static List<Integer>
		liquids = new ArrayList<Integer>();
	public static List<String>
		blacklist,
		whitelist;
	private Icon
		vaseIcon,
		vaseSealedIcon,
		contentsIcon;
	public static CreativeTabs
		creativeTab = CreativeTabs.tabMisc;
	public static final String
		FLUID = "fluid",
		AMOUNT = "amount",
		CRAFTING_DRAIN = "craftingDrain";

	public ItemVase(String unlocalizedName) {
		super(unlocalizedName, null);
		this.setCreativeTab(creativeTab);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		FluidStack fluidStack = this.getFluidStack(itemStack);
		if (fluidStack == null) {
			return super.getUnlocalizedName(itemStack);
		} else {
			return super.getUnlocalizedName() + "." + fluidStack.getFluid().getName();
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addInformation(ItemStack itemStack, EntityPlayer player, List information, boolean bool) {
		FluidStack fluidStack = this.getFluidStack(itemStack.getItemDamage());
		if (fluidStack != null && fluidStack.getFluid() != null) {
			if (Helper.usingAdvancedTooltips()) {
				information.add("Fluid: " + fluidStack.getFluid().getName());
			}
			if (showMeasurement || Helper.usingAdvancedTooltips()) {
				StringBuilder builder = new StringBuilder();
				if (Helper.usingAdvancedTooltips()) {
					builder.append("Amount: ");
				}
				builder.append(fluidStack.amount);
				if (fluidStack.amount < capacity) {
					builder.append("/").append(capacity);
				}
				builder.append(" mB");
				information.add(builder.toString());
			}
		} else if (itemStack.getItemDamage() > 0) {
			information.add("If you obtained this item legitimately,");
			information.add("please report it to Karuberu on the");
			information.add("Minecraft Forums.");
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
		FluidStack liquid = this.getFluidStack(damage);
		if (liquid == null && damage == 0) {
			return this.vaseIcon;
		} else if (FluidHelper.getFluidColor(liquid) != FluidColorRegistry.UNDEFINED_COLOR) {
			if (renderPass == 0) {
				return this.vaseIcon;
			} else {
				return this.contentsIcon;
			}
		} else {
			return this.vaseSealedIcon;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int renderPass) {
		if (renderPass > 0) {
			FluidStack fluidStack = this.getFluidStack(itemStack.getItemDamage());
			return FluidHelper.getFluidColor(fluidStack);
		}
		return FluidColorRegistry.UNDEFINED_COLOR;
	}

	@Override
	public int getDamage(ItemStack itemStack) {
		int damage = super.getDamage(itemStack);
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound == null
		|| !tagCompound.hasKey(FLUID)
		|| !tagCompound.hasKey(AMOUNT)) {
			if (damage != 0) {
				this.setDamage(itemStack, damage);
			}
		} else {
			String fluidName = tagCompound.getString(FLUID);
			int amount = tagCompound.getInteger(AMOUNT);
			int actualDamage = this.getDamage(FluidRegistry.getFluid(fluidName), amount);
			if (damage != actualDamage) {
				this.setDamage(itemStack, actualDamage);
			}
			return actualDamage;
		}
		return damage;
	}

	@Override
	public void setDamage(ItemStack itemStack, int damage) {
		NBTTagCompound tagCompound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
		FluidStack fluidStack = this.getFluidStack(damage);
		if (fluidStack != null && fluidStack.getFluid() != null) {
			tagCompound.setString(FLUID, fluidStack.getFluid().getName());
			tagCompound.setInteger(AMOUNT, fluidStack.amount);
		} else {
			tagCompound.removeTag(FLUID);
			tagCompound.removeTag(AMOUNT);
		}
		itemStack.setTagCompound(tagCompound);
		super.setDamage(itemStack, damage);
	}

	@Override
	public boolean hasContainerItem() {
		return true;
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
		FluidStack contents = this.getFluidStack(itemStack.getItemDamage());
		if (contents != null) {
			return false;
		}
		return true;
	}

	@Override
	public ItemStack getContainerItemStack(ItemStack itemStack) {
		if (itemStack.hasTagCompound()) {
			NBTTagCompound tagCompound = itemStack.getTagCompound();
			FluidStack contents = this.getFluidStack(itemStack.getItemDamage());
			if (contents != null) {
				if (tagCompound.hasKey(CRAFTING_DRAIN)) {
					contents.amount -= tagCompound.getInteger(CRAFTING_DRAIN);
					tagCompound.removeTag(CRAFTING_DRAIN);
					itemStack.setTagCompound(tagCompound);
					itemStack.setItemDamage(this.getDamage(contents));
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
		FluidStack contents = this.getFluidStack(itemStack.getItemDamage());
		if (contents != null) {
			if (contents.containsFluid(FluidReference.MILK.getBucketVolumeStack())
			|| contents.containsFluid(FluidReference.SOUP.getBowlVolumeStack())) {
				return 32;
			}
		}
		return super.getMaxItemUseDuration(itemStack);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemStack) {
		FluidStack contents = this.getFluidStack(itemStack.getItemDamage());
		if (contents != null) {
			if (contents.containsFluid(FluidReference.MILK.getBucketVolumeStack())
			|| contents.containsFluid(FluidReference.SOUP.getBowlVolumeStack())) {
				return EnumAction.drink;
			}
		}
		return super.getItemUseAction(itemStack);
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (world.getBlockId(x, y, z) == Block.cauldron.blockID) {
			FluidStack contents = this.getFluidStack(itemStack.getItemDamage());
			int cauldronMeta = world.getBlockMetadata(x, y, z);
			if (cauldronMeta < 3
			&& contents != null
			&& contents.containsFluid(FluidReference.WATER.getFluidStack(FluidHelper.BOTTLE_VOLUME))
			&& !player.isSneaking()) {
				if (cauldronMeta < 3) {
					int filledMeta = cauldronMeta + (contents.amount / 250);
					if (filledMeta > 3) {
						filledMeta = 3;
					}
					world.setBlockMetadataWithNotify(x, y, z, filledMeta, Helper.NOTIFY_AND_UPDATE_REMOTE);
					if (!player.capabilities.isCreativeMode) {
						contents.amount -= (filledMeta - cauldronMeta) * 250;
						itemStack.setItemDamage(this.getDamage(contents));
					}
				}
			} else if (cauldronMeta > 0
			&& (contents == null || contents.containsFluid(FluidReference.WATER.getFluidStack(1)))) {
				if (contents == null) {
					contents = FluidReference.WATER.getFluidStack(0);
				}
				int amount = (cauldronMeta * 250) + contents.amount;
				int emptiedMeta = 0;
				if (amount < ItemVase.capacity + 750) {
					if (amount <= ItemVase.capacity) {
						contents.amount = amount;
					} else {
						contents.amount = 1000;
						emptiedMeta = (amount - 1000) / 250;
						if (emptiedMeta > 3) {
							emptiedMeta = 3;
						}
					}
					world.setBlockMetadataWithNotify(x, y, z, emptiedMeta, Helper.NOTIFY_AND_UPDATE_REMOTE);
					itemStack.setItemDamage(this.getDamage(contents));
				}
			}
		}
		return false;
	}
		
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		FluidStack contents = this.getFluidStack(itemStack.getItemDamage());
		if (contents == null
		|| (contents.getFluid().canBePlacedInWorld())) {
			float var4 = 1.0F;
			double playerPosX = player.prevPosX + (player.posX - player.prevPosX) * var4;
			double playerPosY = player.prevPosY + (player.posY - player.prevPosY) * var4 + 1.62D - player.yOffset;
			double playerPosZ = player.prevPosZ + (player.posZ - player.prevPosZ) * var4;
			MovingObjectPosition movingObjectPosition = this.getMovingObjectPositionFromPlayer(world, player, contents == null || contents.amount < FluidHelper.BUCKET_VOLUME);
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
						// Test to see if another mod will fill a bucket with the liquid.
						// If so, extract the liquid from the bucket for use in a vase.
						// If not, proceed with vanilla liquid handling.
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
								itemStack.setItemDamage(this.getDamage(fluid));
								return itemStack;
							}
							return itemStack;
						} else {
							Material material = world.getBlockMaterial(movingObjectPosX, movingObjectPosY, movingObjectPosZ);
							int metadata = world.getBlockMetadata(movingObjectPosX, movingObjectPosY, movingObjectPosZ);
							if (material == Material.lava
							&& metadata == FluidReference.LAVA.blockMeta) {
								player.renderBrokenItemStack(itemStack);
								player.destroyCurrentEquippedItem();
								player.setFire(15);
								return itemStack;
							} else if (material == Material.water
							&& metadata == FluidReference.WATER.blockMeta) {
								world.setBlockToAir(movingObjectPosX, movingObjectPosY, movingObjectPosZ);
								if (player.capabilities.isCreativeMode) {
									return itemStack;
								}
								if (itemStack.stackSize == 1) {
									itemStack.setItemDamage(this.getDamage(FluidReference.WATER.getBucketVolumeStack()));
									return itemStack;
								} else {
									itemStack.stackSize--;
									ItemStack filledVase = this.getFilledVase(FluidReference.WATER.getBucketVolumeStack());
									boolean added = player.inventory.addItemStackToInventory(filledVase);
									if (!added) {
										player.dropPlayerItem(filledVase);
									}
								}
								return itemStack;
							}
						}
					} else {
						BlockSide sideHit = BlockSide.get(movingObjectPosition.sideHit);
						if (player.isSneaking()
						&& sideHit != BlockSide.BOTTOM
						&& world.getBlockId(movingObjectPosX, movingObjectPosY, movingObjectPosZ) == Block.cauldron.blockID) {
							return itemStack;
						}
						if (sideHit == BlockSide.BOTTOM) {
							--movingObjectPosY;
						}
						if (sideHit == BlockSide.TOP) {
							++movingObjectPosY;
						}
						if (sideHit == BlockSide.EAST) {
							--movingObjectPosZ;
						}
						if (sideHit == BlockSide.WEST) {
							++movingObjectPosZ;
						}
						if (sideHit == BlockSide.NORTH) {
							--movingObjectPosX;
						}
						if (sideHit == BlockSide.SOUTH) {
							++movingObjectPosX;
						}
						if (!player.canPlayerEdit(movingObjectPosX, movingObjectPosY, movingObjectPosZ, movingObjectPosition.sideHit, itemStack)) {
							return itemStack;
						}
						if (this.tryPlaceContainedLiquid(itemStack, world, playerPosX, playerPosY, playerPosZ, movingObjectPosX, movingObjectPosY, movingObjectPosZ)
						&& !player.capabilities.isCreativeMode) {
							contents.amount -= FluidHelper.BUCKET_VOLUME;
							itemStack.setItemDamage(this.getDamage(contents));
							return itemStack;
						}
					}
				}
				return itemStack;
			}
		} else if (contents != null) {
			if (contents.containsFluid(FluidReference.MILK.getBucketVolumeStack())) {
				player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
			} else if (contents.containsFluid(FluidReference.SOUP.getBowlVolumeStack())
			&& player.canEat(false)) {
				player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
			}
		}
		return itemStack;
	}

	@Override
	public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
		FluidStack contents = this.getFluidStack(itemStack.getItemDamage());
		if (contents != null) {
			if (contents.containsFluid(FluidReference.MILK.getBucketVolumeStack())) {
				if (!player.capabilities.isCreativeMode) {
					contents.amount -= 1000;
				}
				if (!world.isRemote) {
					player.curePotionEffects(new ItemStack(Item.bucketMilk));
				}
			} else if (contents.containsFluid(FluidReference.SOUP.getBowlVolumeStack())) {
				world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
				player.getFoodStats().addStats((ItemFood)Item.bowlSoup);
				if (!player.capabilities.isCreativeMode) {
					contents.amount -= 250;
				}
			}
			itemStack.setItemDamage(this.getDamage(contents));
		}
		return itemStack;
	}

	public boolean tryPlaceContainedLiquid(ItemStack itemStack, World world, double playerPosX, double playerPosY, double playerPosZ, int movingObjectPosX, int movingObjectPosY, int movingObjectPosZ) {
		FluidStack contents = this.getFluidStack(itemStack.getItemDamage());
		if (contents == null) {
			return false;
		} else if (!world.isAirBlock(movingObjectPosX, movingObjectPosY, movingObjectPosZ)
		&& world.getBlockMaterial(movingObjectPosX, movingObjectPosY, movingObjectPosZ).isSolid()) {
			return false;
		} else {
			if (world.provider.isHellWorld
			&& contents.containsFluid(FluidReference.WATER.getBucketVolumeStack())) {
				world.playSoundEffect(playerPosX + 0.5D, playerPosY + 0.5D, playerPosZ + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
				for (int var11 = 0; var11 < 8; ++var11) {
					world.spawnParticle("largesmoke", movingObjectPosX + Math.random(), movingObjectPosY + Math.random(), movingObjectPosZ + Math.random(), 0.0D, 0.0D, 0.0D);
				}
				return true;
			} else if (contents.getFluid().canBePlacedInWorld()
			&& contents.amount >= FluidHelper.BUCKET_VOLUME) {
				int metadata = FluidHelper.getFluidBlockMetadata(contents.getFluid());
				if (metadata >= 0) {
					world.setBlock(movingObjectPosX, movingObjectPosY, movingObjectPosZ, FluidHelper.getFluidBlockID(contents.getFluid()), metadata, Helper.NOTIFY_AND_UPDATE_REMOTE);
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public boolean getHasSubtypes() {
		return true;
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void getSubItems(int id, CreativeTabs creativeTabs, List list) {
		list.add(new ItemStack(id, 1, 0));
		for (Integer damage : liquids) {
			list.add(new ItemStack(id, 1, damage));
		}
	}

	public int getDamage(int id, int amount) {
		if (amount <= 0) {
			return 0;
		} else {
			ByteBuffer bb = ByteBuffer.wrap(new byte[4]).putShort((short)id).putShort((short)amount);
			bb.rewind();
			return bb.getInt();
		}
	}
	public int getDamage(FluidStack fluid) {
		return fluid == null ? 1 : this.getDamage(fluid.fluidID, fluid.amount);
	}
	public int getDamage(Fluid fluid, int amount) {
		try {
			return fluid == null ? 1 : this.getDamage(fluid.getID(), amount);
		} catch (NullPointerException e) {
			return 1;
		}
	}
	
	public ItemStack getFilledVase(FluidStack fluid) {
		return new ItemStack(DynamicEarth.vase.itemID, 1, this.getDamage(fluid));
	}

	public ItemStack getFilledVase(Fluid fluid, int volume) {
		return this.getFilledVase(new FluidStack(fluid, volume));
	}
	
	public ItemStack getFilledVase(Fluid fluid) {
		return this.getFilledVase(fluid, FluidHelper.BUCKET_VOLUME);
	}
	
	public FluidStack getFluidStack(ItemStack itemStack) {
		if (itemStack == null) {
			return null;
		}
		if (itemStack.itemID != this.itemID) {
			throw new IllegalArgumentException("ItemStack contains an invalid item ID. ID must be " + this.itemID + " and the ItemStack's ID was " + itemStack.itemID);
		}
		return this.getFluidStack(itemStack.getItemDamage());
	}
	
	public FluidStack getFluidStack(int damage) {
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

	public boolean canPickupLiquid(int id) {
		return id < Block.blocksList.length && Block.blocksList[id].blockMaterial.isLiquid();
	}
}