/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2015
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.World.Dimension.Structure;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import Reika.ChromatiCraft.Base.DimensionStructureGenerator;
import Reika.ChromatiCraft.Base.StructureData;
import Reika.ChromatiCraft.Block.BlockChromaDoor.TileEntityChromaDoor;
import Reika.ChromatiCraft.Block.Dimension.Structure.BlockGOLController.GOLController;
import Reika.ChromatiCraft.Block.Dimension.Structure.BlockGOLTile.GOLTile;
import Reika.ChromatiCraft.Block.Worldgen.BlockStructureShield.BlockType;
import Reika.ChromatiCraft.Registry.ChromaBlocks;
import Reika.ChromatiCraft.Registry.ChromaOptions;
import Reika.ChromatiCraft.Registry.ChromaSounds;
import Reika.ChromatiCraft.World.Dimension.Structure.GOL.GOLDoors;
import Reika.ChromatiCraft.World.Dimension.Structure.GOL.GOLEntrance;
import Reika.ChromatiCraft.World.Dimension.Structure.GOL.GOLLoot;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Instantiable.Worldgen.ChunkSplicedGenerationCache.TileCallback;

public class GOLGenerator extends DimensionStructureGenerator {

	public static final int MAX_ALLOWED = getSize()*4;
	public static final int ROOM_HEIGHT = 12;

	public static final int SIZE = getSize();

	protected int floorY;

	private ArrayList<Coordinate> initialActive = new ArrayList();

	private Coordinate door;

	private static int getSize() {
		switch(ChromaOptions.getStructureDifficulty()) {
			case 1:
				return 12;
			case 2:
				return 16;
			case 3:
			default:
				return 24;
		}
	}

	@Override
	protected void calculate(int x, int z, Random rand) {
		posY = 30+rand.nextInt(40);
		floorY = posY+1;

		for (int i = -SIZE; i < SIZE; i++) {
			for (int k = -SIZE; k < SIZE; k++) {
				int dx = x+i;
				int dz = z+k;
				this.placeTile(dx, dz, false);
				for (int h = 1; h < ROOM_HEIGHT; h++) {
					world.setBlock(dx, floorY+h, dz, Blocks.air);
				}
				world.setBlock(dx, floorY-1, dz, ChromaBlocks.STRUCTSHIELD.getBlockInstance(), BlockType.CLOAK.metadata);
				world.setBlock(dx, floorY+ROOM_HEIGHT+1, dz, ChromaBlocks.STRUCTSHIELD.getBlockInstance(), BlockType.CLOAK.metadata);
			}
		}

		for (int k = -1; k <= ROOM_HEIGHT+1; k++) {
			for (int i = -SIZE-1; i <= SIZE; i++) {
				int m = (Math.abs(k%8) == Math.abs(i%8)) ? BlockType.LIGHT.metadata : BlockType.CLOAK.metadata;
				world.setBlock(x+i, floorY+k, z-SIZE-1, ChromaBlocks.STRUCTSHIELD.getBlockInstance(), m);
				world.setBlock(x+i, floorY+k, z+SIZE, ChromaBlocks.STRUCTSHIELD.getBlockInstance(), m);
				world.setBlock(x-SIZE-1, floorY+k, z+i, ChromaBlocks.STRUCTSHIELD.getBlockInstance(), m);
				world.setBlock(x+SIZE, floorY+k, z+i, ChromaBlocks.STRUCTSHIELD.getBlockInstance(), m);
			}
		}

		world.setTileEntity(posX-SIZE-3, floorY+1, posZ, ChromaBlocks.GOLCONTROL.getBlockInstance(), 0, new GOLTileCallback(this, false));

		new GOLDoors(this).generate(world, x, floorY+1, z-8);
		new GOLLoot(this).generate(world, x+SIZE+1, floorY-1, z-3);

		this.addDynamicStructure(new GOLEntrance(this), x-SIZE-1-8, z-8);

		door = new Coordinate(x+SIZE, floorY+1, z);
	}

	@Override
	protected int getCenterXOffset() {
		return 0;
	}

	@Override
	protected int getCenterZOffset() {
		return 0;
	}

	protected final void placeTile(int x, int z, boolean startOn) {
		world.setTileEntity(x, floorY, z, ChromaBlocks.GOL.getBlockInstance(), 0, new GOLTileCallback(this, startOn));
		world.setBlock(x, floorY+ROOM_HEIGHT, z, ChromaBlocks.GOL.getBlockInstance(), 2);
	}

	private static class GOLTileCallback implements TileCallback {

		private final boolean initOn;
		private final GOLGenerator generator;

		private GOLTileCallback(GOLGenerator gen, boolean on) {
			initOn = on;
			generator = gen;
		}

		@Override
		public void onTilePlaced(World world, int x, int y, int z, TileEntity te) {
			if (te instanceof GOLTile) {
				((GOLTile)te).initialize(initOn);
				((GOLTile)te).uid = generator.id;
			}
			if (te instanceof GOLController) {
				((GOLController)te).initialize(generator.posX-SIZE, generator.posX+SIZE, generator.posZ-SIZE, generator.posZ+SIZE, generator.floorY);
				((GOLController)te).uid = generator.id;
			}
		}

	}

	@Override
	public StructureData createDataStorage() {
		return null;
	}

	public void deactivateTile(World world, int x, int y, int z) {
		Coordinate c = new Coordinate(x, y, z);
		initialActive.remove(c);
	}

	public boolean activateTile(World world, int x, int y, int z) {
		if (initialActive.size() >= MAX_ALLOWED) {
			return false;
		}
		Coordinate c = new Coordinate(x, y, z);
		initialActive.add(c);
		return true;
	}

	@Override
	protected void clearCaches() {
		initialActive.clear();
		door = null;
	}

	public void clearTiles() {
		initialActive.clear();
	}

	public void checkConditions(World world) {
		int num = 0;
		for (int i = -SIZE; i < SIZE; i++) {
			for (int k = -SIZE; k < SIZE; k++) {
				int dx = posX+i;
				int dz = posZ+k;
				Coordinate c = new Coordinate(posX, floorY+ROOM_HEIGHT, posZ);
				if (c.getBlock(world) == ChromaBlocks.GOL.getBlockInstance()) {
					if (c.getBlockMetadata(world) == 3) {
						num++;
					}
				}
			}
		}
		int min = (int)((SIZE*2+1)*(SIZE*2+1)*1F/*0.9F*/);
		if (num >= min) {
			TileEntity te = door.getTileEntity(world);
			if (te instanceof TileEntityChromaDoor) {
				((TileEntityChromaDoor)te).open(0);
			}
			ChromaSounds.CAST.playSoundAtBlock(world, door.xCoord-SIZE*2-3, door.yCoord, door.zCoord, 2, 1);
		}
		else {
			ChromaSounds.ERROR.playSoundAtBlock(world, door.xCoord-SIZE*2-3, door.yCoord, door.zCoord, 2, 1);
		}
	}

}
