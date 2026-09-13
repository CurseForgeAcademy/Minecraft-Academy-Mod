package net.kaupenjoe.academymod.entity.goal;

import net.kaupenjoe.academymod.entity.custom.BabymanEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BabymanSlamAttackGoal extends Goal {
    // TICK RELATED FIELDS
    private static final int SLAM_TICK = 13; // Tick he hits the ground
    private static final int GROUNDED_END_TICK = 49; // Tick he is no longer grounded
    private static final int ANIMATION_TICKS = 60;

    private static final double ATTACK_RANGE = 5.0;

    private static final int PARTICLE_COUNT = 48;

    // How many ticks you have to parry
    private static final int PARRY_WINDOW = 6;
    private static final double PARRY_FACING = 0.5;
    private static final float LATE_BLOCK_DISABLE_SECONDS = 1.5f;

    private final BabymanEntity babyman;
    private LivingEntity target;
    private int attackTick;
    private int cooldown;
    private boolean hasSlammed;
    private boolean frozen;

    public BabymanSlamAttackGoal(BabymanEntity babyman) {
        this.babyman = babyman;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }

        LivingEntity currentTarget = this.babyman.getTarget();
        if(currentTarget == null || !currentTarget.isAlive() || !this.babyman.onGround()) {
            return false;
        }

        if(this.babyman.distanceToSqr(currentTarget) > ATTACK_RANGE * ATTACK_RANGE) {
            return false;
        }

        this.target = currentTarget;
        return this.babyman.hasLineOfSight(currentTarget);
    }

    @Override
    public boolean canContinueToUse() {
        return this.attackTick < ANIMATION_TICKS;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private double attackDamage() {
        return this.babyman.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    @Override
    public void start() {
        this.attackTick = 0;
        this.hasSlammed = false;
        this.frozen = false;

        this.babyman.getNavigation().stop();
        this.babyman.setSlamming(true);
        this.babyman.setSlamTick(-1);
        this.babyman.triggerAnim(BabymanEntity.ATTACK_CONTROLLER, BabymanEntity.SLAM_TRIGGER);

        if(this.target != null) {
            this.babyman.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        }

        Vec3 lunge = Vec3.ZERO;
        if(this.target != null) {
            lunge = new Vec3(this.target.getX() - this.babyman.getX(), 0,
                    this.target.getZ() - this.babyman.getZ()).normalize().scale(0.18f);
        }

        this.babyman.setDeltaMovement(lunge.x, 0.55, lunge.z);
        this.babyman.playSound(SoundEvents.RAVAGER_ROAR, 1f, 1.4f);
    }

    @Override
    public void tick() {
        this.attackTick++;
        this.babyman.getNavigation().stop();

        if(this.target != null && this.attackTick <= SLAM_TICK) {
            this.babyman.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        }

        if(this.attackTick == SLAM_TICK - 5) {
            Vec3 deltaMovement = this.babyman.getDeltaMovement().scale(0.4);
            this.babyman.setDeltaMovement(deltaMovement.x, -1.4, deltaMovement.z);
        }

        if(!this.hasSlammed && this.attackTick >= SLAM_TICK &&
                (this.babyman.onGround() || this.attackTick >= SLAM_TICK + 4)) {
            this.hasSlammed = true;
            this.babyman.setDeltaMovement(0, this.babyman.getDeltaMovement().y, 0);
            this.babyman.resetFallDistance();

            if(this.babyman.level() instanceof ServerLevel serverLevel) {
                this.doSlam(serverLevel);
            }
        }

        if(this.hasSlammed && !this.frozen && this.babyman.onGround()) {
            this.frozen = true;
            this.babyman.setNoGravity(true);
        }

        if(this.frozen) {
            this.babyman.setDeltaMovement(Vec3.ZERO);
            this.babyman.setSlamTick(this.attackTick);
        } else {
            this.babyman.setSlamTick(-1);
        }

        // Tick 13 to 49 the entity can take damage
        this.babyman.setSlamVulnerable(this.attackTick >= SLAM_TICK & this.attackTick <= GROUNDED_END_TICK);
    }

    @Override
    public void stop() {
        this.babyman.setSlamming(false);
        this.babyman.setSlamVulnerable(false);
        this.babyman.setSlamTick(-1);
        this.babyman.setNoGravity(false);

        this.frozen = false;
        this.target = null;
        this.attackTick = 0;
        this.hasSlammed = false;
        this.cooldown = 60;
    }

    private void doSlam(ServerLevel level) {
        level.playSound(null, this.babyman.getX(), this.babyman.getY(), this.babyman.getZ(),
                SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 1.2f, 0.75f);
        level.playSound(null, this.babyman.getX(), this.babyman.getY(), this.babyman.getZ(),
                SoundEvents.RAVAGER_STUNNED, SoundSource.HOSTILE, 1.0f, 0.8f);

        spawnRing(level, this.babyman.getX(), this.babyman.getY(), this.babyman.getZ());
        damageAround(level);
    }

    private void spawnRing(ServerLevel level, double x, double y, double z) {
        BlockPos below = this.babyman.blockPosition().below();
        BlockState belowState = level.getBlockState(below);
        boolean belowIsAir = belowState.isAir();

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double angle = (Math.PI * 2.0 / PARTICLE_COUNT) * i;
            double dirX = Math.cos(angle);
            double dirZ = Math.sin(angle);

            double posX = x + dirX * 0.6;
            double posZ = z + dirZ * 0.6;

            level.sendParticles(ParticleTypes.CLOUD, posX, y + 0.1, posZ,0, dirX, 0.08, dirZ, 0.55);

            if (!belowIsAir && i % 2 == 0) {
                level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, belowState),
                        posX, y + 0.1, posZ,0, dirX, 0.35, dirZ, 0.6);
            }
        }

        level.sendParticles(ParticleTypes.EXPLOSION, x, y + 0.2, z, 1, 0.0, 0.0, 0.0, 0.0);
    }

    private void damageAround(ServerLevel level) {
        AABB area = this.babyman.getBoundingBox().inflate(ATTACK_RANGE, 1.5, ATTACK_RANGE);

        for(LivingEntity entityInRange : level.getEntitiesOfClass(LivingEntity.class, area,
                entity -> entity.isAlive() && !(entity instanceof BabymanEntity))) {
            double distSqr = entityInRange.distanceToSqr(this.babyman);
            if(distSqr > ATTACK_RANGE * ATTACK_RANGE) {
                continue;
            }

            if(!entityInRange.onGround() && entityInRange.getY() > this.babyman.getY() + 1) {
                continue;
            }

            if(isParrying(entityInRange)) {
                entityInRange.hurtServer(level, level.damageSources().mobAttack(this.babyman), (float)this.attackDamage());

                level.playSound(null, entityInRange.getX(), entityInRange.getY(), entityInRange.getZ(),
                        SoundEvents.SHIELD_BLOCK.value(), SoundSource.PLAYERS, 1f, 1.6f);
                level.sendParticles(ParticleTypes.CRIT, entityInRange.getX(), entityInRange.getEyeY() - 0.2, entityInRange.getZ(),
                        12, 0.25, 0.25, 0.25, 0.15);
                continue;
            }

            this.babyman.doHurtTarget(level, entityInRange);

            if(entityInRange.isBlocking()) {
                disableBlockingItem(level, entityInRange);
            }

            Vec3 push = entityInRange.position().subtract(this.babyman.position());
            push = push.normalize().scale(0.75);

            entityInRange.push(push.x, 0.5, push.z);
            entityInRange.hurtMarked = true;
        }
    }

    private boolean isParrying(LivingEntity entityInRange) {
        if(!entityInRange.isBlocking()) {
            return false;
        }

        Vec3 look = entityInRange.getLookAngle();
        Vec3 toBabyman = this.babyman.position().subtract(entityInRange.position());
        double horizontalLength = Math.sqrt(toBabyman.x * toBabyman.x + toBabyman.z * toBabyman.z);
        double lookLength = Math.sqrt(look.x * look.x + look.z * look.z);

        double facing = (look.x * toBabyman.x + look.z * toBabyman.z) / (horizontalLength * lookLength);
        if(facing < PARRY_FACING) {
            return false;
        }

        int activeTicks = entityInRange.getTicksUsingItem() - blockDelayTicks(entityInRange);
        return activeTicks >= 0 && activeTicks <= PARRY_WINDOW;
    }

    private static int blockDelayTicks(LivingEntity entity) {
        ItemStack stack = entity.getUseItem();
        BlocksAttacks blocksAttacks = stack.get(DataComponents.BLOCKS_ATTACKS);

        return blocksAttacks == null ? 0 : blocksAttacks.blockDelayTicks();
    }

    private static void disableBlockingItem(ServerLevel level, LivingEntity entityInRange) {
        ItemStack stack = entityInRange.getUseItem();
        BlocksAttacks blocksAttacks = stack.get(DataComponents.BLOCKS_ATTACKS);

        if(blocksAttacks != null) {
            blocksAttacks.disable(level, entityInRange, LATE_BLOCK_DISABLE_SECONDS, stack);
        }
    }
}
