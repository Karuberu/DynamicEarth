package karuberu.dynamicearth.blocks;

import java.util.ArrayList;
import java.util.Random;

import karuberu.dynamicearth.entity.EntityFallingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface IFallingBlock {

    boolean tryToFall(World world, int x, int y, int z);

    void onStartFalling(EntityFallingBlock entityFallingBlock);

    void onFinishFalling(World world, int x, int y, int z, int metadata);
            
    int getMetaForFall(int fallTime, int metadata, Random random);
   
    int getFallDamage();

    int getMaxFallDamage();
    
    DamageSource getDamageSource();
    
    ArrayList<ItemStack> getItemsDropped(World world, int x, int y, int z, int fallTime, int metadata, Random random);
}
