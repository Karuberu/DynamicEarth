package karuberu.dynamicearth.entity.ai;

import karuberu.core.util.Helper;
import karuberu.dynamicearth.api.grass.IGrassyBlock;
import karuberu.dynamicearth.api.grass.IGrassyBlock.GrassType;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIEatGrassyBlock extends EntityAIBase {
	
    protected EntityLiving theEntity;
    protected World theWorld;
    protected int eatGrassTick = 0;

    public EntityAIEatGrassyBlock(EntityLiving entityLiving) {
        this.theEntity = entityLiving;
        this.theWorld = entityLiving.worldObj;
        this.setMutexBits(7);
	}

    @Override
    public boolean shouldExecute() {
        if (this.theEntity.getRNG().nextInt(this.theEntity.isChild() ? 50 : 1000) != 0) {
            return false;
        } else {
            int x = MathHelper.floor_double(this.theEntity.posX);
            int y = MathHelper.floor_double(this.theEntity.posY);
            int z = MathHelper.floor_double(this.theEntity.posZ);
            for (int yi = y; yi >= y - 1; yi--) {
	            Block block = Block.blocksList[this.theWorld.getBlockId(x, yi, z)];
	            if (block instanceof IGrassyBlock
	            && ((IGrassyBlock)block).getType(this.theWorld, x, yi, z) == GrassType.GRASS) {
	            	return true;
	            }
            }
          	return false;
        }
    }

    @Override
	public void startExecuting() {
        this.eatGrassTick = 40;
        this.theWorld.setEntityState(this.theEntity, (byte)10);
        this.theEntity.getNavigator().clearPathEntity();
    }

    @Override
	public void resetTask() {
        this.eatGrassTick = 0;
    }

    @Override
	public boolean continueExecuting() {
        return this.eatGrassTick > 0;
    }

    public int getEatGrassTick() {
        return this.eatGrassTick;
    }
    
    @Override
	public void updateTask() {
        this.eatGrassTick = Math.max(0, this.eatGrassTick - 1);
        if (this.eatGrassTick == 4) {
            int x = MathHelper.floor_double(this.theEntity.posX);
            int y = MathHelper.floor_double(this.theEntity.posY);
            int z = MathHelper.floor_double(this.theEntity.posZ);
            Block block;
            for (int yi = y; yi >= y - 1; yi--) {
 	            block = Block.blocksList[this.theWorld.getBlockId(x, yi, z)];
	            if (block instanceof IGrassyBlock
	            && ((IGrassyBlock)block).getType(this.theWorld, x, yi, z) == GrassType.GRASS) {
                    ItemStack itemStack = ((IGrassyBlock)block).getBlockForType(this.theWorld, x, yi, z, GrassType.DIRT);
                    this.theWorld.playAuxSFX(2001, x, yi, z, Block.grass.blockID);
                    this.theWorld.setBlock(x, yi, z, itemStack.itemID, itemStack.getItemDamage(), Helper.NOTIFY_AND_UPDATE_REMOTE);
                    this.theEntity.eatGrassBonus();
	            }
            }
        }
    }
}

