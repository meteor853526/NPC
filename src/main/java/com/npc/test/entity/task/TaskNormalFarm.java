package com.npc.test.entity.task;


import com.mojang.datafixers.util.Pair;
import com.npc.test.NpcTestMod;
import com.npc.test.api.task.IFarmTask;
import com.npc.test.passive.NpcEntity;
import com.npc.test.util.EmptyBlockReader;
import net.minecraft.block.*;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class TaskNormalFarm implements IFarmTask {
    private static final ResourceLocation NAME = new ResourceLocation(NpcTestMod.MOD_ID, "farm");

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return Items.IRON_HOE.getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(NpcEntity maid) {
        return null;
    }

    @Override
    public List<Pair<Integer, Task<? super NpcEntity>>> createBrainTasks(NpcEntity maid) {
        return null;
    }

//    @Override
//    public boolean isSeed(ItemStack stack) {
//        Item item = stack.getItem();
//        if (item == Items.NETHER_WART) {
//            return true;
//        }
//        if (item instanceof BlockNamedItem) {
//            BlockNamedItem blockNamedItem = (BlockNamedItem) item;
//            Block block = blockNamedItem.getBlock();
//            if (block instanceof IPlantable) {
//                IPlantable plantable = (IPlantable) block;
//                return plantable.getPlantType(EmptyBlockReader.INSTANCE, BlockPos.ZERO) == PlantType.CROP
//                        && plantable.getPlant(EmptyBlockReader.INSTANCE, BlockPos.ZERO).getBlock() != Blocks.AIR;
//            }
//        }
//        return false;
//    }

//    @Override
//    public boolean canHarvest(NpcEntity maid, BlockPos cropPos, BlockState cropState) {
//        Block block = cropState.getBlock();
//        if (block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(cropState)) {
//            return true;
//        }
//        return block == Blocks.NETHER_WART && cropState.getValue(NetherWartBlock.AGE) >= 3;
//    }
//
//    @Override
//    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        if (maid.getMainHandItem().getItem() instanceof HoeItem) {
//            maid.destroyBlock(cropPos);
//        } else {
//            Block cropBlock = cropState.getBlock();
//            if (cropBlock instanceof CropsBlock) {
//                CropsBlock crop = (CropsBlock) cropBlock;
//                TileEntity tileentity = cropState.hasTileEntity() ? maid.level.getBlockEntity(cropPos) : null;
//                Block.dropResources(cropState, maid.level, cropPos, tileentity, maid, maid.getMainHandItem());
//                maid.level.setBlock(cropPos, crop.defaultBlockState(), Constants.BlockFlags.DEFAULT);
//                return;
//            }
//
//            if (cropBlock == Blocks.NETHER_WART) {
//                maid.level.setBlock(cropPos, Blocks.NETHER_WART.defaultBlockState(), Constants.BlockFlags.DEFAULT);
//                Block.popResource(maid.level, cropPos, new ItemStack(Items.NETHER_WART));
//            }
//        }
//    }
//
//    @Override
//    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
//        BlockState aboveState = maid.level.getBlockState(basePos.above());
//        if (!aboveState.getMaterial().isReplaceable() || aboveState.getMaterial().isLiquid()) {
//            return false;
//        }
//        if (seed.getItem() instanceof BlockNamedItem) {
//            BlockNamedItem blockNamedItem = (BlockNamedItem) seed.getItem();
//            Block block = blockNamedItem.getBlock();
//            if (block instanceof IPlantable) {
//                IPlantable plantable = (IPlantable) block;
//                return baseState.canSustainPlant(maid.level, basePos, Direction.UP, plantable);
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
//        if (seed.getItem() instanceof BlockNamedItem) {
//            BlockNamedItem blockNamedItem = (BlockNamedItem) seed.getItem();
//            Block block = blockNamedItem.getBlock();
//            if (block instanceof IPlantable) {
//                maid.placeItemBlock(basePos.above(), seed);
//            }
//        }
//        return seed;
//    }
}
