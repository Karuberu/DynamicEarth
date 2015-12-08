package karuberu.mods.mudmod.blocks;

import java.util.Random;

import karuberu.mods.mudmod.entity.EntityFallingBlock;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface IFallingBlock {

    boolean tryToFall(World world, int x, int y, int z);

    void onStartFalling(EntityFallingBlock entityFallingBlock);

    void onFinishFalling(World world, int x, int y, int z, int metadata);
    
    boolean isDamagedByFall();
    
    int getMetaForFall(int fallTime, int metadata, Random random);
   
    int getFallDamage();

    int getMaxFallDamage();
    
    DamageSource getDamageSource();
}
