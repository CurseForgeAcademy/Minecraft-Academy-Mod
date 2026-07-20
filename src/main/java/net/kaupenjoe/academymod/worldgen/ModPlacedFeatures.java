package net.kaupenjoe.academymod.worldgen;

import net.kaupenjoe.academymod.AcademyMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> AURIC_ORE_PLACED_KEY = registerKey("auric_ore_placed");
    public static final ResourceKey<PlacedFeature> NETHER_AURIC_ORE_PLACED_KEY = registerKey("nether_auric_ore_placed");
    public static final ResourceKey<PlacedFeature> END_AURIC_ORE_PLACED_KEY = registerKey("end_auric_ore_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, AURIC_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_AURIC_ORE_KEY),
                OrePlacements.commonOrePlacement(12,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-64),
                                VerticalAnchor.absolute(80))));
        register(context, NETHER_AURIC_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.NETHER_AURIC_ORE_KEY),
                OrePlacements.commonOrePlacement(9,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-64),
                                VerticalAnchor.absolute(80))));
        register(context, END_AURIC_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.END_AURIC_ORE_KEY),
                OrePlacements.commonOrePlacement(12,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-64),
                                VerticalAnchor.absolute(80))));
    }


    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(AcademyMod.MODID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
