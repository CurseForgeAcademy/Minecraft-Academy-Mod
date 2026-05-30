package net.kaupenjoe.academymod.item;

import net.kaupenjoe.academymod.AcademyMod;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.equipment.ArmorType;
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

    public static final DeferredItem<Item> AURIC_SWORD = ITEMS.registerItem("auric_sword",
            properties -> new Item(properties.sword(ModToolTiers.AURIC, 3, -2.4f)));
    public static final DeferredItem<Item> AURIC_PICKAXE = ITEMS.registerItem("auric_pickaxe",
            properties -> new Item(properties.pickaxe(ModToolTiers.AURIC, 1, -2.8f)));
    public static final DeferredItem<Item> AURIC_SHOVEL = ITEMS.registerItem("auric_shovel",
            properties -> new ShovelItem(ModToolTiers.AURIC, 1.5f, -3.0f, properties));
    public static final DeferredItem<Item> AURIC_AXE = ITEMS.registerItem("auric_axe",
            properties -> new AxeItem(ModToolTiers.AURIC, 6, -3.2f, properties));
    public static final DeferredItem<Item> AURIC_HOE = ITEMS.registerItem("auric_hoe",
            properties -> new HoeItem(ModToolTiers.AURIC, 0f, -3.4f, properties));

    public static final DeferredItem<Item> AURIC_HELMET = ITEMS.registerItem("auric_helmet",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.AURIC_ARMOR_MATERIAL, ArmorType.HELMET)));
    public static final DeferredItem<Item> AURIC_CHESTPLATE = ITEMS.registerItem("auric_chestplate",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.AURIC_ARMOR_MATERIAL, ArmorType.CHESTPLATE)));
    public static final DeferredItem<Item> AURIC_LEGGINGS = ITEMS.registerItem("auric_leggings",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.AURIC_ARMOR_MATERIAL, ArmorType.LEGGINGS)));
    public static final DeferredItem<Item> AURIC_BOOTS = ITEMS.registerItem("auric_boots",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.AURIC_ARMOR_MATERIAL, ArmorType.BOOTS)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
