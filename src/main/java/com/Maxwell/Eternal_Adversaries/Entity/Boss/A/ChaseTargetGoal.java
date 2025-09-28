package com.Maxwell.Eternal_Adversaries.Entity.Boss.A;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ChaseTargetGoal extends Goal {
    private final Volcanic_TyrantMain mob;
    private LivingEntity target;
    private final double speedModifier; // 追尾速度
    private final float chaseRange;    // 追尾範囲

    public ChaseTargetGoal(Volcanic_TyrantMain mob, double speedModifier, float MinchaseRange) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.chaseRange = MinchaseRange;
    }

    @Override
    public boolean canUse() {
        LivingEntity currentTarget = this.mob.getTarget();
        if (currentTarget != null && currentTarget.isAlive() && this.mob.distanceTo(currentTarget) <= chaseRange || this.mob.currentState == Volcanic_TyrantMain.ActionState.ATTACKING) {
            this.target = currentTarget;
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        // 追尾を開始
        if (this.target != null) {
            this.mob.getNavigation().moveTo(this.target, speedModifier);
        }
    }

    @Override
    public void stop() {
        // 追尾を停止
        this.target = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {

        if (this.target != null && this.mob.distanceTo(target) <= chaseRange && target.isAlive()) {
            Vec3 direction2 = target.position().subtract(mob.position()).normalize();
            this.mob.setDeltaMovement(direction2.x * speedModifier, mob.getDeltaMovement().y, direction2.z * speedModifier);
            float yaw = (float)(Mth.atan2(direction2.z, direction2.x) * (180F / Math.PI)) - 90F;
            mob.setYRot(yaw);
            mob.setYBodyRot(yaw);
            mob.setYHeadRot(yaw);

        } else {
              stop();
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() && this.mob.distanceTo(this.target) <= chaseRange || this.mob.currentState == Volcanic_TyrantMain.ActionState.ATTACKING;
    }
}