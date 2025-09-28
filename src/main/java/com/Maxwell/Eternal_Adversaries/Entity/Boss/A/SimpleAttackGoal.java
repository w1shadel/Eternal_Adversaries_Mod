package com.Maxwell.Eternal_Adversaries.Entity.Boss.A;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class SimpleAttackGoal extends Goal {
    private final Volcanic_TyrantMain mob;
    private LivingEntity target;
    private int attacktick; // このGoalが、自身の攻撃時間を管理する

    // --- このGoalのパラメータ ---
    private final int animationLength;
    private final int damageApplyTick;
    private final int cooldown;
    private final double attackRangeSqr;
    private final String Animation_name;

    public SimpleAttackGoal(Volcanic_TyrantMain mob, int animationLength, int damageApplyTick, int cooldown, double attackRange, String Animation_name) {
        this.mob = mob;
        this.animationLength = animationLength;
        this.damageApplyTick = damageApplyTick;
        this.cooldown = cooldown;
        this.attackRangeSqr = attackRange * attackRange;
        this.Animation_name = Animation_name;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK)); // 実行中は他の動きを止める
    }

    @Override
    public boolean canUse() {
        // ★★★ 修正点 ★★★
        // 攻撃中でないかどうかのチェックを追加
        if (mob.currentState == Volcanic_TyrantMain.ActionState.ATTACKING) return false;
        if (mob.getAttackCooldown() > 0) return false;

        LivingEntity potentialTarget = mob.getTarget();
        if (potentialTarget == null || !potentialTarget.isAlive()) return false;

        return mob.distanceToSqr(potentialTarget) <= attackRangeSqr;
    }
    @Override
    public boolean canContinueToUse() {
        return this.attacktick < this.animationLength && this.target.isAlive();
    }

    @Override
    public void start() {
        this.target = mob.getTarget();
        this.mob.setActionState(Volcanic_TyrantMain.ActionState.ATTACKING);
        this.attacktick = 0; // 攻撃タイマーをリセット
        mob.getNavigation().stop(); // 移動を停止

    }

    @Override
    public void tick() {
        this.attacktick++; // 自身のタイマーを進める
        if (this.attacktick == 1)
        {
            this.mob.triggerAnim("controller","sippo");
        }
        if (this.attacktick == this.damageApplyTick) {
            mob.performAttack(10.0f, 6.0);
        }
    }
}