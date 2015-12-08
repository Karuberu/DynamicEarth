package karuberu.dynamicearth.plugins;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockAdobeSlab;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PluginThermalExpansion implements IDynamicEarthPlugin {

	@Override
	public String getName() {
		return "Thermal Expansion Plugin";
	}

	@Override
	public String getErrorReportRequestMessage() {
		return PluginHandler.pleaseNotify;
	}

	@Override
	public boolean requiredModsAreLoaded() {
		return Loader.isModLoaded("ThermalExpansion");
	}

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {}

	@Override
	public void initialize() throws NoClassDefFoundError, NoSuchMethodError, Exception {
		this.addPulverizerRecipes();
	}
	
	private void addPulverizerRecipes() {
		if (DynamicEarth.includePeat) {
			this.addPulverizerRecipe(
				350,
				new ItemStack(Block.cobblestoneMossy),
				new ItemStack(Block.sand),
				new ItemStack(DynamicEarth.peatMossSpecimen),
				5
			);
		}
		if (DynamicEarth.includeAdobe) {
			this.addPulverizerRecipe(
				320,
				new ItemStack(DynamicEarth.adobe),
				new ItemStack(DynamicEarth.adobeDust, 4)
			);
			this.addPulverizerRecipe(
				160,
				new ItemStack(DynamicEarth.adobeSingleSlab, 1, BlockAdobeSlab.ADOBE),
				new ItemStack(DynamicEarth.adobeDust, 2)
			);
			this.addPulverizerRecipe(
				320,
				new ItemStack(DynamicEarth.adobeStairs),
				new ItemStack(DynamicEarth.adobeDust, 4)
			);
			this.addPulverizerRecipe(
				80,
				new ItemStack(DynamicEarth.earthbowl),
				new ItemStack(DynamicEarth.adobeDust, 3)
			);
			this.addPulverizerRecipe(
				160,
				new ItemStack(DynamicEarth.vase),
				new ItemStack(DynamicEarth.adobeDust, 5)
			);
		}
	}
	
	private void addPulverizerRecipe(int energy, ItemStack inputStack, ItemStack outputStack) {
		this.addPulverizerRecipe(energy, inputStack, outputStack, null, 0);		
	}

	private void addPulverizerRecipe(int energy, ItemStack input, ItemStack output, ItemStack bonus, int chance) {
		NBTTagCompound comm = new NBTTagCompound();
		comm.setCompoundTag("input", input.writeToNBT(new NBTTagCompound()));
		comm.setCompoundTag("primaryOutput", output.writeToNBT(new NBTTagCompound()));
		comm.setInteger("energy", energy);
		if (chance > 0) {
			comm.setInteger("secondaryChance", chance);
		}
		if (bonus != null) {
			comm.setCompoundTag("secondaryOutput", bonus.writeToNBT(new NBTTagCompound()));			
		}
		FMLInterModComms.sendMessage("ThermalExpansion", "PulverizerRecipe", comm);
	}
}
