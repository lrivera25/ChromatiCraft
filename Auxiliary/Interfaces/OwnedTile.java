/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2015
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Auxiliary.Interfaces;

import net.minecraft.entity.player.EntityPlayer;


public interface OwnedTile {

	boolean onlyAllowOwnersToMine();
	boolean onlyAllowOwnersToUse();

	boolean isOwnedByPlayer(EntityPlayer ep);

}
