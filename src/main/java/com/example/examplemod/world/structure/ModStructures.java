package com.example.examplemod.world.structure;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.world.structure.structures.HorseFarmStructure;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.concurrent.Immutable;
import java.util.HashMap;
import java.util.Map;

public class ModStructures {
    public static final DeferredRegister<Structure<?>> STRUCTURES =
            DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, ExampleMod.MOD_ID);

    public static final RegistryObject<Structure<NoFeatureConfig>> HORSEFARM =
            STRUCTURES.register("horsefarm", HorseFarmStructure::new);

    public static void setupStructures() {
        setupMapSpacingAndLand(HORSEFARM.get(),
                new StructureSeparationSettings(100,50,1234567890), true);
    }

    public static <F extends  Structure<?>> void setupMapSpacingAndLand(F structure, StructureSeparationSettings structureSeparationSettings,
                                                                        boolean transformSurroundingLand) {

        Structure.NAME_STRUCTURE_BIMAP.put(structure.getRegistryName().toString(), structure);

        if (transformSurroundingLand) {
            Structure.field_236384_t_ = ImmutableList.<Structure<?>>builder()
                    .addAll(Structure.field_236384_t_)
                    .add(structure)
                    .build();
//            ImmutableList<Structure<?>> newStructures = ImmutableList.<Structure<?>>builder()
//                    .addAll(Structure.field_236384_t_)
//                    .add(structure)
//                    .build();

        }


        DimensionStructuresSettings.field_236191_b_ =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.field_236191_b_)
                        .put(structure, structureSeparationSettings)
                        .build();
//        ImmutableMap<Structure<?>, StructureSeparationSettings> newMap =
//                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
//                        .putAll(DimensionStructuresSettings.field_236191_b_)
//                        .put(structure, structureSeparationSettings)
//                        .build();


//        DimensionStructuresSettings.field_236191_b_ =
//                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
//                        .putAll(DimensionStructuresSettings.field_236191_b_)
//                        .put(structure, structureSeparationSettings)
//                        .build();

        WorldGenRegistries.NOISE_SETTINGS.getEntries().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap =
                    settings.getValue().getStructures().func_236195_a_();
            /*
             * Pre-caution in case a mod makes the structure map immutable like datapacks do.
             * I take no chances myself. You never know what another mods does...
             *
             * structureConfig requires AccessTransformer  (See resources/META-INF/accesstransformer.cfg)
             */
            if (structureMap instanceof ImmutableMap) {
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                settings.getValue().getStructures().func_236195_a_();

            } else {
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }

    public static void register(IEventBus eventBus) {
        STRUCTURES.register(eventBus);
    }

}
