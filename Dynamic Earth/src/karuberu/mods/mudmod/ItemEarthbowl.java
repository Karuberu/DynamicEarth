package karuberu.mods.mudmod;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMooshroom;
import net.minecraft.src.ItemStack;

public class ItemEarthbowl extends ItemMudMod {

	protected ItemEarthbowl(int i) {
		super(i);
	}

    // Very hacky way to milk a cow without editing the cow class.
    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityLiving entityLiving) {
        if (entityLiving instanceof EntityMooshroom) {
        	EntityMooshroom mooshroom = (EntityMooshroom)entityLiving;
        	if (mooshroom.getGrowingAge() >= 0) {
            	itemStack.itemID = MudMod.earthbowlSoup.shiftedIndex;
                return true;
        	}
        }
		return false;
    }
}
