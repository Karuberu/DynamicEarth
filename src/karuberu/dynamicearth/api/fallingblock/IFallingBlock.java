package karuberu.dynamicearth.api.fallingblock;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface IFallingBlock {

	boolean canSpawnFromBlock(World world, int x, int y, int z);
	
	boolean canFallBelow(World world, int x, int y, int z);

    boolean tryToFall(World world, int x, int y, int z);

	void onStartFalling(Entity entity, World world, int x, int y, int z, int metadata);

	void onFallTick(Entity entity, World world, int x, int y, int z, int metadata, int fallTime);
	
    void onFinishFalling(Entity entity, World world, int x, int y, int z, int metadata);
    
    int getMetaForFall(int fallTime, int metadata, Random random);
   
    float getFallDamage();

    int getMaxFallDamage();
    
    DamageSource getDamageSource();
    
    ArrayList<ItemStack> getItemsDropped(World world, int x, int y, int z, int fallTime, int metadata, Random random);

}
