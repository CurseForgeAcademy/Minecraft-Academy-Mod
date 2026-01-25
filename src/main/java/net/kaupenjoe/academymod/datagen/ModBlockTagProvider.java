package net.kaupenjoe.academymod.datagen;

import net.kaupenjoe.academymod.AcademyMod;
import net.kaupenjoe.academymod.block.ModBlocks;
import net.kaupenjoe.academymod.tag.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, AcademyMod.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.AURIC_ORE.get(),
                        ModBlocks.AURIC_BLOCK.get(),
                        ModBlocks.AURIC_DEEPSLATE_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.AURIC_ORE.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(ModBlocks.AURIC_DEEPSLATE_ORE.get());

        tag(ModTags.Blocks.NEEDS_AURIC_TOOL)
                .addTag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.AURIC_BLOCK.get());

        tag(ModTags.Blocks.INCORRECT_FOR_AURIC_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .remove(ModTags.Blocks.NEEDS_AURIC_TOOL);
    }
}
