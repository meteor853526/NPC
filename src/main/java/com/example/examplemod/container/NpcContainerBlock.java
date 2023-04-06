package com.example.examplemod.container;


import com.example.examplemod.gui.OpenGuI;
import com.sun.org.apache.xerces.internal.impl.validation.EntityState;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class NpcContainerBlock extends Block {
//    public static final DirectionProperty FACING = HorizontalBlock.LOGGER;
    public NpcContainerBlock() { super(Properties.create(Material.ROCK).hardnessAndResistance(5));}
//    public NpcContainerBlock(EntityType type, World world) {  // Block change to NPCEntity
//        super(type, world);
//    }


    @Override
    public boolean hasTileEntity(BlockState state) { // BlockState change to EntityState
        return true;
    }

    @Nullable
    public NpcContainerTileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new NpcContainerTileEntity();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
//        BlockPayTraceResult ? how to npc
//        isRemote => isClientSide ?
        if (!worldIn.isRemote && handIn == Hand.MAIN_HAND) {
            NpcContainerTileEntity npcContainerTileEntity = (NpcContainerTileEntity) worldIn.getTileEntity(pos);
            NetworkHooks.openGui((ServerPlayerEntity) player, npcContainerTileEntity, (PacketBuffer packerBuffer) -> {
                packerBuffer.writeBlockPos(npcContainerTileEntity.getPos());
            });
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

}
