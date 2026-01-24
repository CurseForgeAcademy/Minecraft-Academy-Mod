package net.kaupenjoe.academymod.item;

import net.kaupenjoe.academymod.AcademyMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(AcademyMod.MODID);

    public static final DeferredItem<Item> RAW_AURIC =
            ITEMS.registerItem("raw_auric", Item::new, Item.Properties::new);
    public static final DeferredItem<Item> AURIC_SHARD =
            ITEMS.registerItem("auric_shard", Item::new, Item.Properties::new);


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
