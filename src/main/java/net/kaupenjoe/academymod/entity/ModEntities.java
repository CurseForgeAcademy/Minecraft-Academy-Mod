package net.kaupenjoe.academymod.entity;

import net.kaupenjoe.academymod.AcademyMod;
import net.kaupenjoe.academymod.entity.custom.BabymanEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.createEntities(AcademyMod.MODID);

    public static final ResourceKey<EntityType<?>> BABYMAN_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(AcademyMod.MODID, "babyman"));

    public static final Supplier<EntityType<BabymanEntity>> BABYMAN = ENTITY_TYPES.register("babyman",
            () -> EntityType.Builder.of(BabymanEntity::new, MobCategory.MONSTER)
                    .sized(1.5f, 1.5f).build(BABYMAN_KEY));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
