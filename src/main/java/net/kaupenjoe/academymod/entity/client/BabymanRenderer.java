package net.kaupenjoe.academymod.entity.client;

import com.geckolib.renderer.GeoEntityRenderer;
import net.kaupenjoe.academymod.entity.custom.BabymanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class BabymanRenderer extends GeoEntityRenderer<BabymanEntity, LivingEntityRenderState> {
    public BabymanRenderer(EntityRendererProvider.Context context) {
        super(context, new BabymanModel());
    }
}
