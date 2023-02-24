package com.npc.test.entity.ai.brain.task;



import com.npc.test.api.task.IFarmTask;
import com.npc.test.passive.NpcEntity;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.IItemHandler;

public class MaidFarmMoveTask extends MaidMoveToBlockTask {
    private final NonNullList<ItemStack> seeds = NonNullList.create();
    private final IFarmTask task;

    public MaidFarmMoveTask(IFarmTask task, float movementSpeed, int searchLength) {
        super(movementSpeed, searchLength);
        this.task = task;
    }

//    @Override
//    protected void start(ServerWorld worldIn, NpcEntity entityIn, long gameTimeIn) {
//        seeds.clear();
//        IItemHandler inv = entityIn.getAvailableInv(true);
//        for (int i = 0; i < inv.getSlots(); i++) {
//            ItemStack stack = inv.getStackInSlot(i);
//            if (task.isSeed(stack)) {
//                seeds.add(stack);
//            }
//        }
//        this.searchForDestination(worldIn, entityIn);
//    }

    @Override
    protected void start(ServerWorld worldIn, NpcEntity npc, long gameTimeIn) {

        searchForDestination(worldIn, npc);

    }
    @Override
    protected boolean shouldMoveTo(ServerWorld worldIn, NpcEntity maid, BlockPos basePos) {
//        if (task.checkCropPosAbove()) {
//            BlockPos above2Pos = basePos.above(2);
//            BlockState stateUp2 = worldIn.getBlockState(above2Pos);
//            if (!stateUp2.getCollisionShape(worldIn, above2Pos).isEmpty()) {
//                return false;
//            }
//        }

        BlockPos cropPos = basePos.above();
        BlockState cropState = worldIn.getBlockState(cropPos);
        return true;

//        BlockState cropState = worldIn.getBlockState(cropPos);
//        if (task.canHarvest(maid, cropPos, cropState)) {
//            return true;
//        }
//
//        BlockState baseState = worldIn.getBlockState(basePos);
//        return seeds.stream().anyMatch(seed -> task.canPlant(maid, basePos, baseState, seed));
    }
}
