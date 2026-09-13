package net.kaupenjoe.academymod.entity.client;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.kaupenjoe.academymod.AcademyMod;
import net.kaupenjoe.academymod.entity.custom.BabymanEntity;
import net.minecraft.resources.Identifier;

public class BabymanModel extends GeoModel<BabymanEntity> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(AcademyMod.MODID, "babyman");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(AcademyMod.MODID, "textures/entity/babyman/babyman.png");
    }

    @Override
    public Identifier getAnimationResource(BabymanEntity animatable) {
        return Identifier.fromNamespaceAndPath(AcademyMod.MODID, "babyman");
    }
}
