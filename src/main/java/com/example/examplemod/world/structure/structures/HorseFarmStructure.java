package com.example.examplemod.world.structure.structures;

import com.example.examplemod.ExampleMod;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class HorseFarmStructure extends Structure<NoFeatureConfig> {

    public HorseFarmStructure(){
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    protected boolean func_230363_a_(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunnkPos, NoFeatureConfig featureConfig) {

        BlockPos centerOfChunk = new BlockPos((chunkX<<4)+7,0,(chunkZ<<4)+7);
        int landHeight = chunkGenerator.getHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);

        IBlockReader columnOfBlocks = chunkGenerator.func_230348_a_(centerOfChunk.getX(),centerOfChunk.getZ());
        BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.up(landHeight));

        return topBlock.getFluidState().isEmpty();
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {

        return HorseFarmStructure.Start::new;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }



        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistriesManager, ChunkGenerator chunkGenerator,
                                   TemplateManager templateManagerIn, int chunkX, int chunkZ,
                                   Biome biomeIn, NoFeatureConfig config) {

            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;

            BlockPos blockpos = new BlockPos(x, 0, z);

            //addpieces()
            JigsawManager.func_242837_a(dynamicRegistriesManager,
                    new VillageConfig(() -> dynamicRegistriesManager.getRegistry(Registry.JIGSAW_POOL_KEY).getOrDefault(new ResourceLocation(ExampleMod.MOD_ID, "horsefarm/start_pool")),10), AbstractVillagePiece::new, chunkGenerator, templateManagerIn, blockpos, this.components, this.rand, false, true);
            this.components.forEach(place -> place.offset(0,1,0));
            this.components.forEach(place -> place.getBoundingBox().minY -= 1);

            this.recalculateStructureSize();

            LogManager.getLogger().log(Level.DEBUG, "Horse Farm at" +
                    this.components.get(0).getBoundingBox().minX + " " +
                    this.components.get(0).getBoundingBox().minY + " " +
                    this.components.get(0).getBoundingBox().minZ);
        }
    }
}