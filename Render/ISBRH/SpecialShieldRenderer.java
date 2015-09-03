/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2015
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Render.ISBRH;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import Reika.ChromatiCraft.ChromatiCraft;
import Reika.ChromatiCraft.Block.Dimension.Structure.BlockSpecialShield;
import Reika.ChromatiCraft.Registry.ChromaBlocks;
import Reika.DragonAPI.Instantiable.Rendering.EdgeDetectionRenderer;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;


public class SpecialShieldRenderer implements ISimpleBlockRenderingHandler {

	private final EdgeDetectionRenderer edge = new EdgeDetectionRenderer(ChromaBlocks.SPECIALSHIELD.getBlockInstance()).setIcons(BlockSpecialShield.edgeIcons);

	@Override
	public void renderInventoryBlock(Block b, int metadata, int modelId, RenderBlocks rb) {
		Tessellator tessellator = Tessellator.instance;

		rb.renderMaxX = 1;
		rb.renderMinY = 0;
		rb.renderMaxZ = 1;
		rb.renderMinX = 0;
		rb.renderMinZ = 0;
		rb.renderMaxY = 1;

		IIcon ico = b.getIcon(0, metadata);

		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		rb.renderFaceYNeg(b, 0.0D, 0.0D, 0.0D, ico);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		rb.renderFaceYPos(b, 0.0D, 0.0D, 0.0D, ico);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		rb.renderFaceZNeg(b, 0.0D, 0.0D, 0.0D, ico);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		rb.renderFaceZPos(b, 0.0D, 0.0D, 0.0D, ico);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		rb.renderFaceXNeg(b, 0.0D, 0.0D, 0.0D, ico);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		rb.renderFaceXPos(b, 0.0D, 0.0D, 0.0D, ico);
		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block b, int modelId, RenderBlocks rb) {

		Tessellator v5 = Tessellator.instance;
		int meta = world.getBlockMetadata(x, y, z);
		BlockSpecialShield bs = (BlockSpecialShield)b;

		if (bs.useNoLighting(world, x, y, z))
			v5.setBrightness(240);
		else
			v5.setBrightness(b.getMixedBrightnessForBlock(world, x, y, z));

		v5.setColorOpaque_F(255, 255, 255);
		IIcon ico = b.getIcon(world, x, y, z, 0);
		rb.renderFaceYNeg(b, x, y, z, ico);

		ico = b.getIcon(world, x, y, z, 1);
		rb.renderFaceYPos(b, x, y, z, ico);

		ico = b.getIcon(world, x, y, z, 2);
		rb.renderFaceZNeg(b, x, y, z, ico);

		ico = b.getIcon(world, x, y, z, 3);
		rb.renderFaceZPos(b, x, y, z, ico);

		ico = b.getIcon(world, x, y, z, 4);
		rb.renderFaceXNeg(b, x, y, z, ico);

		ico = b.getIcon(world, x, y, z, 5);
		rb.renderFaceXPos(b, x, y, z, ico);

		if (meta%8 <= 1) {
			v5.addTranslation(x, y, z);
			v5.setColorOpaque_I(0xffffff);
			v5.setBrightness(240);
			edge.renderBlock(world, x, y, z, rb);
			v5.addTranslation(-x, -y, -z);
		}

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return ChromatiCraft.proxy.specialShieldRender;
	}

}