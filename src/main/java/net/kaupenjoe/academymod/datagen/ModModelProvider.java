package net.kaupenjoe.academymod.datagen;

import net.kaupenjoe.academymod.AcademyMod;
import net.kaupenjoe.academymod.block.ModBlocks;
import net.kaupenjoe.academymod.item.ModArmorMaterials;
import net.kaupenjoe.academymod.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, AcademyMod.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ModItems.AURIC_SHARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.RAW_AURIC.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.AURIC_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.AURIC_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.AURIC_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.AURIC_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.AURIC_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        itemModels.generateTrimmableItem(ModItems.AURIC_HELMET.get(), ModArmorMaterials.AURIC_KEY, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(ModItems.AURIC_CHESTPLATE.get(), ModArmorMaterials.AURIC_KEY, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(ModItems.AURIC_LEGGINGS.get(), ModArmorMaterials.AURIC_KEY, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(ModItems.AURIC_BOOTS.get(), ModArmorMaterials.AURIC_KEY, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);


        blockModels.createTrivialCube(ModBlocks.AURIC_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.AURIC_ORE.get());
        blockModels.createTrivialCube(ModBlocks.AURIC_DEEPSLATE_ORE.get());
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return ModItems.ITEMS.getEntries().stream();
    }
}
