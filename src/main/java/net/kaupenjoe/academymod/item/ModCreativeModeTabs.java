package net.kaupenjoe.academymod.item;

import net.kaupenjoe.academymod.AcademyMod;
import net.kaupenjoe.academymod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AcademyMod.MODID);

    public static final Supplier<CreativeModeTab> AURIC_ACADEMY_TAB = CREATIVE_MODE_TABS.register("academy_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.AURIC_SHARD.get()))
                    .title(Component.translatable("creativetab.academymod.academy_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.RAW_AURIC);
                        output.accept(ModItems.AURIC_SHARD);

                        output.accept(ModItems.AURIC_SWORD);
                        output.accept(ModItems.AURIC_PICKAXE);
                        output.accept(ModItems.AURIC_SHOVEL);
                        output.accept(ModItems.AURIC_AXE);
                        output.accept(ModItems.AURIC_HOE);

                        output.accept(ModBlocks.AURIC_BLOCK);
                        output.accept(ModBlocks.AURIC_ORE);
                        output.accept(ModBlocks.AURIC_DEEPSLATE_ORE);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
