package Reika.ChromatiCraft.Items.Tools.Powered;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import Reika.ChromatiCraft.Base.ItemPoweredChromaTool;
import Reika.ChromatiCraft.Registry.CrystalElement;


public class ItemBodyLight extends ItemPoweredChromaTool {

	public ItemBodyLight(int index) {
		super(index);
	}

	@Override
	protected CrystalElement getColor() {
		return CrystalElement.BLUE;
	}

	@Override
	protected int getMaxCharge() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getChargeStates() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean isActivated(EntityPlayer e, boolean held) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int getChargeConsumptionRate(EntityPlayer e, World world, ItemStack is) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean doTick(ItemStack is, World world, EntityPlayer e, boolean held) {
		// TODO Auto-generated method stub
		return false;
	}

}