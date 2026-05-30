package net.kaupenjoe.academymod.data;

import com.mojang.serialization.Codec;
import net.kaupenjoe.academymod.AcademyMod;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, AcademyMod.MODID);

    public static final Supplier<AttachmentType<Boolean>> IS_HOVERING = ATTACHMENT_TYPES.register("is_hovering",
            () -> AttachmentType.<Boolean>builder(() -> false).serialize(Codec.BOOL.fieldOf("is_hovering"))
                    .sync(ByteBufCodecs.BOOL).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
