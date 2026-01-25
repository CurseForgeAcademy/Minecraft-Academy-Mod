package net.kaupenjoe.academymod.datagen;

import net.kaupenjoe.academymod.AcademyMod;
import net.kaupenjoe.academymod.item.ModItems;
import net.kaupenjoe.academymod.tag.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, AcademyMod.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.AURIC_REPAIRABLES)
                .add(ModItems.AURIC_SHARD.get());

        tag(ItemTags.SWORDS)
                .add(ModItems.AURIC_SWORD.get());
        tag(ItemTags.PICKAXES)
                .add(ModItems.AURIC_PICKAXE.get());
        tag(ItemTags.SHOVELS)
                .add(ModItems.AURIC_SHOVEL.get());
        tag(ItemTags.AXES)
                .add(ModItems.AURIC_AXE.get());
        tag(ItemTags.HOES)
                .add(ModItems.AURIC_HOE.get());
    }
}
