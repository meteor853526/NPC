package com.npc.test.container;

import com.npc.test.passive.NpcEntity;
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
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class NpcContainerBlock extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public NpcContainerBlock(AbstractBlock.Properties properties) {
        super(properties);
//        super(Properties.of(Material.STONE) .harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(5f));
    }
//    public NpcContainerBlock(EntityType type, World world) {  // Block change to NPCEntity
//        super(type, world);
//    }


    @Override
    public boolean hasTileEntity(BlockState state) { // BlockState change to EntityState
        return true;
    }

    @Nullable
    public NpcContainerTileEntity createTileEntity(EntityState state, IBlockReader world) {
        return new NpcContainerTileEntity();
    }

    @SuppressWarnings("deprecation")
//    @Override
    public ActionResultType onBlockActivated(EntityState state, World worldIn, BlockState pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
//        BlockPayTraceResult ? how to npc
//        isRemote => isClientSide ?
//        if (!worldIn.isClientSide && handIn == Hand.MAIN_HAND) {
//            NpcContainerTileEntity npcContainerTileEntity = (NpcContainerTileEntity) worldIn.getBlockEntity(pos);
//            NetworkHooks.openGui((ServerPlayerEntity) player, npcContainerTileEntity, (PacketBuffer packerBuffer) -> {
//                packerBuffer.writeBlockPos(npcContainerTileEntity.getPos());
//            });
//        }
//        return ActionResultType.SUCCESS;
        if (worldIn.isClientSide) {
            return ActionResultType.SUCCESS;
        }
    }

}