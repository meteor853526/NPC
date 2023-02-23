package com.npc.test.entity.ai.brain.task;


import com.google.common.collect.ImmutableMap;
import com.npc.test.init.InitEntities;
import com.npc.test.passive.NpcEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.world.server.ServerWorld;

public abstract class MaidMoveToBlockTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 120;
    private final float movementSpeed;
    private final int searchRange;
    private final int verticalSearchRange;
    protected int verticalSearchStart;

    public MaidMoveToBlockTask(float movementSpeed, int searchRange) {
        this(movementSpeed, searchRange, 1);
    }

    public MaidMoveToBlockTask(float movementSpeed, int searchRange, int verticalSearchRange) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_ABSENT));
        this.movementSpeed = movementSpeed;
        this.searchRange = searchRange;
        this.verticalSearchRange = verticalSearchRange;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    protected final void searchForDestination(ServerWorld worldIn, NpcEntity npc) {
        BlockPos blockpos = npc.blockPosition();
        BlockPos.Mutable blockPos = new BlockPos.Mutable();
        for (int y = this.verticalSearchStart; y <= this.verticalSearchRange; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < this.searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        blockPos.setWithOffset(blockpos, x, y - 1, z);
                        if (npc.isWithinRestriction(blockPos) && shouldMoveTo(worldIn, npc, blockPos) && checkPathReach(npc, blockPos)) {
                            BrainUtil.setWalkAndLookTargetMemories(npc, blockPos, this.movementSpeed, 0);
                            npc.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosWrapper(blockPos));
                            this.setNextCheckTickCount(5);

                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * 判定条件
     *
     * @param worldIn  当前实体所处的 world
     * @param entityIn 当前需要移动的实体
     * @param pos      当前检索的 pos
     * @return 是否符合判定条件
     */
    protected abstract boolean shouldMoveTo(ServerWorld worldIn, NpcEntity entityIn, BlockPos pos);

    protected boolean checkPathReach(NpcEntity npc, BlockPos pos) {
        return npc.canPathReach(pos);
    }
}
