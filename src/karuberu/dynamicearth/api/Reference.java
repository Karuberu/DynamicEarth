package karuberu.dynamicearth.api;

import java.util.logging.Logger;
import karuberu.dynamicearth.api.fallingblock.EntityFallingBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class Reference {
	public static ITillable mycelium;
	public static Logger logger;
	public static Class<? extends EntityFallingBlock> fallingBlockEntityClass;
}
