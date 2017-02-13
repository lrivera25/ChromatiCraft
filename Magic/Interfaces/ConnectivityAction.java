/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2016
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Magic.Interfaces;

import Reika.ChromatiCraft.Magic.Network.CrystalPath;


public interface ConnectivityAction extends CrystalRepeater {

	public void notifySendingTo(CrystalPath p, CrystalReceiver r);
	public void notifyReceivingFrom(CrystalPath p, CrystalTransmitter t);

}
