package net.kaupenjoe.academymod.datagen;

import net.kaupenjoe.academymod.AcademyMod;
import net.kaupenjoe.academymod.block.ModBlocks;
import net.kaupenjoe.academymod.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new ModRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "Academy Recipes";
        }
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.MISC, ModBlocks.AURIC_BLOCK.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.AURIC_SHARD.get())
                .unlockedBy(getHasName(ModItems.AURIC_SHARD.get()), has(ModItems.AURIC_SHARD.get())).save(this.output);

        shapeless(RecipeCategory.MISC, ModItems.AURIC_SHARD.get(), 9)
                .requires(ModBlocks.AURIC_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.AURIC_BLOCK.get()), has(ModBlocks.AURIC_BLOCK.get())).save(this.output);

        List<ItemLike> AURIC_SMELTABLES = List.of(ModItems.RAW_AURIC, ModBlocks.AURIC_ORE, ModBlocks.AURIC_DEEPSLATE_ORE);

        oreSmelting(AURIC_SMELTABLES, RecipeCategory.MISC, ModItems.AURIC_SHARD.get(), 0.25f, 200, "auric");
        oreBlasting(AURIC_SMELTABLES, RecipeCategory.MISC, ModItems.AURIC_SHARD.get(), 0.25f, 100, "auric");
    }

    @Override
    protected <T extends AbstractCookingRecipe> void oreCooking(RecipeSerializer<T> serializer, AbstractCookingRecipe.Factory<T> recipeFactory,
                                                                List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group, String suffix) {
        for(ItemLike itemlike : ingredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), category, result, experience, cookingTime, serializer, recipeFactory)
                    .group(group).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(this.output, AcademyMod.MODID + ":" + getItemName(result) + suffix + "_" + getItemName(itemlike));
        }
    }
}
