package net.kaupenjoe.academymod.entity.custom;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.util.GeckoLibUtil;
import net.kaupenjoe.academymod.entity.goal.BabymanSlamAttackGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BabymanEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static final String ATTACK_CONTROLLER = "attack_controller";
    public static final String SLAM_TRIGGER = "slam";

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation SLAM = RawAnimation.begin().thenPlay("attack");

    private static final EntityDataAccessor<Boolean> SLAMMING =
            SynchedEntityData.defineId(BabymanEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SLAM_TICK =
            SynchedEntityData.defineId(BabymanEntity.class, EntityDataSerializers.INT);

    private record HitboxKeyframe(int frame, float forward, float up) { }
    private static final HitboxKeyframe[] SLAM_HITBOX_KEYS = {
            new HitboxKeyframe(0,  0.0F,  0.0F),
            new HitboxKeyframe(10, 0.5F,  0.2F),
            new HitboxKeyframe(13, 1.1F, -0.5F),
            new HitboxKeyframe(49, 1.1F, -0.5F),
            new HitboxKeyframe(60, 0.0F,  0.0F)
    };

    private boolean slamVulnerable;
    private int slamHitboxTick = -1;

    public BabymanEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(SLAMMING, false);
        entityData.define(SLAM_TICK, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 80)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75)
                .add(Attributes.ATTACK_DAMAGE, 12)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.STEP_HEIGHT, 1);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BabymanSlamAttackGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1, true));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.2));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 12));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<BabymanEntity>("controller", 5, state -> {
            if(this.isSlamming()) {
                return PlayState.STOP;
            }

            if(state.isMoving()) {
                return state.setAndContinue(WALK);
            }

            return state.setAndContinue(IDLE);
        }));

        controllers.add(new AnimationController<BabymanEntity>(ATTACK_CONTROLLER, 0, state -> PlayState.STOP)
                .triggerableAnim(SLAM_TRIGGER, SLAM));
    }

    /* SLAMMING */
    public boolean isSlamming() {
        return this.entityData.get(SLAMMING);
    }

    public void setSlamming(boolean slam) {
        this.entityData.set(SLAMMING, slam);
    }

    public void setSlamVulnerable(boolean vulnerable) {
        this.slamVulnerable = vulnerable;
    }

    public void setSlamTick(int tick) {
        this.entityData.set(SLAM_TICK, tick);
        this.applySlamHitboxTick(tick);
    }

    @Override
    public void tick() {
        super.tick();

        if(!this.level().isClientSide() && this.isNoGravity() && this.slamHitboxTick < 0) {
            this.setNoGravity(false);
        }
    }

    /* HITBOX */
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if(SLAM_TICK.equals(accessor)) {
            this.applySlamHitboxTick(this.entityData.get(SLAM_TICK));
        }
    }

    private void applySlamHitboxTick(int tick) {
        if(this.slamHitboxTick == tick) {
            return;
        }

        this.slamHitboxTick = tick;
        this.setPos(this.getX(), this.getY(), this.getZ());
    }

    @Override
    protected AABB makeBoundingBox(Vec3 position) {
        return super.makeBoundingBox(position).move(this.getSlamHitboxOffset());
    }

    protected Vec3 getSlamHitboxOffset() {
        if(this.slamHitboxTick < 0) {
            return Vec3.ZERO;
        }

        float forwardOffset = 0f;
        float upOffset = 0f;

        for(int i = 0; i < SLAM_HITBOX_KEYS.length - 1; i++) {
            HitboxKeyframe fromTick = SLAM_HITBOX_KEYS[i];
            HitboxKeyframe toTick = SLAM_HITBOX_KEYS[i + 1];

            if(this.slamHitboxTick < fromTick.frame || this.slamHitboxTick > toTick.frame) {
                continue;
            }

            float progress = (float) (this.slamHitboxTick - fromTick.frame) / (toTick.frame - fromTick.frame);
            forwardOffset = Mth.lerp(progress, SLAM_HITBOX_KEYS[i].forward, SLAM_HITBOX_KEYS[i + 1].forward);
            upOffset = Mth.lerp(progress, SLAM_HITBOX_KEYS[i].up, SLAM_HITBOX_KEYS[i + 1].up);
            break;
        }

        upOffset = Math.max(upOffset, -0.9f);

        if(forwardOffset == 0f && upOffset == 0f) {
            return Vec3.ZERO;
        }

        Vec3 forward = Vec3.directionFromRotation(0f, this.yBodyRot);
        return new Vec3(forward.x * forwardOffset, upOffset, forward.z * forwardOffset);
    }

    @Override
    public boolean isInWall() {
        return this.slamHitboxTick < 0 && super.isInWall();
    }

    @Override
    public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
        if(super.isInvulnerableTo(level, source)) {
            return true;
        }

        if(source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }

        return !this.slamVulnerable;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance newEffect) {
        return this.slamVulnerable || newEffect.getEffect().value().isBeneficial();
    }
}
