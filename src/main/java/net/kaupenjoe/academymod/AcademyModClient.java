package net.kaupenjoe.academymod;

import net.kaupenjoe.academymod.data.ModDataAttachments;
import net.kaupenjoe.academymod.entity.ModEntities;
import net.kaupenjoe.academymod.entity.client.BabymanRenderer;
import net.kaupenjoe.academymod.event.ModEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = AcademyMod.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = AcademyMod.MODID, value = Dist.CLIENT)
public class AcademyModClient {
    public AcademyModClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.BABYMAN.get(), BabymanRenderer::new);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();

        if(minecraft.level == null || minecraft.player == null) {
            return;
        }

        for(AbstractClientPlayer otherPlayer : minecraft.level.players()) {
            if(otherPlayer.getData(ModDataAttachments.IS_HOVERING) && otherPlayer != minecraft.player
            && minecraft.player.distanceToSqr(otherPlayer) < 1024) {
                ModEvents.spawnHoverCubeParticles(otherPlayer);
            }
        }
    }
}
