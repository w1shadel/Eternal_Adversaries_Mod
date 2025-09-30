package com.Maxwell.Eternal_Adversaries.Entity.Boss.C;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class The_VoidSteel_Vanguard extends Monster {
    // --- アニメーションステートの定義 ---
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState sheiledAnimationState = new AnimationState(); // 2種類目の攻撃アニメ用
    public final AnimationState dashAnimationState = new AnimationState();
    public final AnimationState deadAnimationState = new AnimationState();
    public int swordCooldown = 40;
    public int shieldCooldown = 80;
    public int dashCooldown = 0;
    public int deadtime = 0;

    private static final EntityDataAccessor<Boolean> IS_GUARDING =
            SynchedEntityData.defineId(The_VoidSteel_Vanguard.class, EntityDataSerializers.BOOLEAN);
    public The_VoidSteel_Vanguard(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public boolean isAttacking() {
        return this.attackAnimationState.isStarted() ||
                this.sheiledAnimationState.isStarted() ||
                this.dashAnimationState.isStarted() ||
                this.deadAnimationState.isStarted();

    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_GUARDING, false);
    }
    public boolean isGuarding() {
        return this.entityData.get(IS_GUARDING);
    }

    public void setGuarding(boolean guarding) {
        this.entityData.set(IS_GUARDING, guarding);
    }
    // --- AI Goalの登録 ---
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new JetDashGoal(this));
        this.goalSelector.addGoal(3, new ShieldBashGoal(this));
        this.goalSelector.addGoal(4, new SwordAttackGoal(this));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // --- ステータス設定 ---
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 250.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.2)
                .add(Attributes.ARMOR, 20.0);
    }

    // --- アニメーションイベントの送受信 ---
    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 5) {
            this.sheiledAnimationState.start(this.tickCount);
        }
        if (pId == 8) { // ダッシュ攻撃の合図
            this.dashAnimationState.start(this.tickCount);
        }
        if (pId == 99)
        {
            this.deadAnimationState.start(this.tickCount);
        }
        if (pId == 4) {
            // 攻撃アニメーションをランダムに切り替える
            if (this.random.nextBoolean()) {
                this.attackAnimationState.start(this.tickCount);
            }
        }
        else {
            super.handleEntityEvent(pId);
        }
    }
    @Override
    public void aiStep() {
        super.aiStep();
        if (!isAttacking() ) {
            this.swordCooldown--;
            this.shieldCooldown--;
            this.dashCooldown--;
        }
        if (isAttacking()) {
            this.getNavigation().stop();
        }
    }
    @Override
    public void die(@NotNull DamageSource pCause) {
        super.die(pCause); // die()自体は呼び出してOK
    }
    public boolean isdead = false;
    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        }
        if (this.getHealth() - pAmount <= 0) {
            this.setHealth(1);
            this.tick();
            isdead = true;
            return true;
        }
        return super.hurt(pSource, pAmount);
    }
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(true, this.tickCount);
        }
        if(isdead)
        {
            deadtime++;
            this.setHealth(1);
            this.navigation.stop();
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            if (this.deadtime == 1 && !this.level().isClientSide && !this.isDeadOrDying()) {
                this.level().broadcastEntityEvent(this, (byte)99);
            }
            if (this.deadtime > 120 && !this.level().isClientSide && !this.isDeadOrDying()) {
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    // --- AI Goal (MeleeAttackGoal) ---

    static class SwordAttackGoal extends Goal {
        private final The_VoidSteel_Vanguard boss;
        private LivingEntity target;
        private int attackTime; // 攻撃モーションの経過時間
        private int checkCooldown; // 攻撃チャンスをうかがう間隔
        private int power = 0;

        public SwordAttackGoal(The_VoidSteel_Vanguard boss) {
            this.boss = boss;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.boss.isAttacking() || this.boss.swordCooldown > 0) {
                return false;
            }
            LivingEntity target = this.boss.getTarget();
            return target != null && target.isAlive() || !boss.isdead;
        }

        @Override
        public boolean canContinueToUse() {
            // このGoalは攻撃モーション中のみ継続する
            return this.attackTime > 0 || !boss.isdead;
        }

        @Override
        public void start() {
            // このメソッドは canUse() が true になった瞬間に一度だけ呼ばれる
            // しかし、実際の攻撃開始は tick() の中で判断する
            this.target = this.boss.getTarget();
            this.checkCooldown = 0;
        }

        @Override
        public void stop() {
            this.boss.swordCooldown = 40;
            this.boss.attackAnimationState.stop();
            this.attackTime = 0;
        }

        @Override
        public void tick() {
            this.target = this.boss.getTarget();
            if (this.target == null) return;

            this.boss.getLookControl().setLookAt(this.target);

            // --- 攻撃モーション中かどうかで処理を分岐 ---
            if (this.attackTime > 0) {
                this.boss.getNavigation().stop();
                this.boss.setDeltaMovement(0, this.boss.getDeltaMovement().y, 0);
                this.attackTime++;
                double distance = this.boss.distanceToSqr(this.target);
                if (this.attackTime == 15) {
                    performAttack(); // ダメージ処理
                    if (distance <= 8)
                    {
                        target.hurt(this.boss.damageSources().mobAttack(this.boss), (float) (8 - distance));
                    }
                }
                if (this.attackTime >= 20) {
                    this.attackTime = 0; // canContinueToUse() を false にしてGoalを終了させる
                }

            } else {
                // --- 追跡・索敵フェーズ ---
                this.boss.getNavigation().moveTo(this.target, 0.9D);

                // 攻撃チャンスをうかがう（毎フレーム判定すると重いので、少し間隔をあける）
                this.checkCooldown = Math.max(0, this.checkCooldown - 1);
                if (this.checkCooldown <= 0) {
                    this.checkCooldown = 4; // 4tickごと（0.2秒ごと）にチェック

                    double distanceSqr = this.boss.distanceToSqr(this.target);
                    // 攻撃範囲の2乗 (getBbWidth()なども考慮すると良い)
                    double attackRangeSqr = 3.5 * 3.5;
                    if (distanceSqr <= attackRangeSqr) {
                        this.attackTime = 1; // 攻撃タイマーを開始
                        this.boss.getNavigation().stop();
                        this.boss.level().broadcastEntityEvent(this.boss, (byte) 4);
                    }
                }
            }
        }

        private void performAttack() {
            if (this.boss.distanceToSqr(this.target) < 4.0 * 4.0) {

                this.boss.level().broadcastEntityEvent(this.boss, (byte) 4);

            }
        }
    }

    static class ShieldBashGoal extends Goal {
        private final The_VoidSteel_Vanguard boss;
        private LivingEntity target;
        private int attackTime;
        private boolean isGuarding = false;



        public ShieldBashGoal(The_VoidSteel_Vanguard boss) {
            this.boss = boss;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        /**
         * 行動を開始する条件
         */
        @Override
        public boolean canUse() {
            // 他の攻撃中でなく、クールダウンが終わっているか？
            if (this.boss.isAttacking() || this.boss.shieldCooldown > 0 || !boss.isdead) {
                return false;
            }

            this.target = this.boss.getTarget();
            if (this.target == null || !this.target.isAlive() || !boss.isdead) {
                return false;
            }

            // ★★★ シールドバッシュ特有の条件 ★★★
            // ターゲットが非常に近い（3ブロック以内）場合のみ、発動のチャンスがある
            return this.boss.distanceToSqr(this.target) < 9.0 || !boss.isdead; // 3*3 = 9
        }

        /**
         * 行動を継続する条件
         */
        @Override
        public boolean canContinueToUse() {
            // shiled.animation.jsonの長さは3.0秒なので、60tick
            return this.attackTime < 60 && this.target.isAlive() || !boss.isdead;
        }

        @Override
        public void start() {
            this.attackTime = 0;
            this.boss.getNavigation().stop(); // その場で攻撃するため、移動を停止
            // ★★★ アニメーション再生の合図(ID:5)を送る ★★★
            this.boss.level().broadcastEntityEvent(this.boss, (byte) 5);
            this.isGuarding = false; // 開始時はまだガードしていない
        }

        @Override
        public void stop() {
            this.boss.shieldCooldown = 120; // 5秒のクールダウン
            this.boss.sheiledAnimationState.stop(); // 終了時にアニメーションを停止
            if (this.isGuarding) {
                this.boss.setGuarding(false);
            }
        }

        /**
         * 毎tick実行されるメインロジック
         */
        @Override
        public void tick() {
            this.attackTime++;
            if (this.target != null) {
                this.boss.getLookControl().setLookAt(this.target);
            }
            if (this.attackTime > 0 && this.attackTime <= 20)
            {
                this.boss.setGuarding(true);
            }
            if (this.attackTime == 20) {
                performShieldBash();
                this.isGuarding = false;
                this.boss.setGuarding(false); // ガード状態を解除
            }
        }

        private void performShieldBash() {
            // 攻撃範囲（目の前2.5ブロック程度）を定義
            AABB attackRange = this.boss.getBoundingBox().inflate(1.0D, 0.5D, 1.0D).move(this.boss.getLookAngle().scale(1.5));

            for (LivingEntity entity : this.boss.level().getEntitiesOfClass(LivingEntity.class, attackRange)) {
                if (entity.equals(this.target)) {
                    // 1. ダメージを与える (剣攻撃よりは少し控えめ)
                    entity.hurt(this.boss.damageSources().mobAttack(this.boss), 15.0F);
                    double knockbackStrength = 2.5; // ノックバックの強さ
                    double verticalKnockback = 0.6; // 少しだけ上に打ち上げる

                    // ボスからターゲットへの方向ベクトル
                    Vec3 direction = this.boss.position().vectorTo(entity.position()).normalize();

                    entity.setDeltaMovement(
                            direction.x * knockbackStrength,
                            verticalKnockback,
                            direction.z * knockbackStrength
                    );
                    entity.hurtMarked = true; // ノックバックが正しく適用されるようにする

                    // 効果音などを再生
                    this.boss.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F);
                    break; // ターゲットに当たったらループを抜ける
                }
            }
        }
    }

    // --- JetDashGoal (安全確認機能付き) ---
    static class JetDashGoal extends Goal {
        private final The_VoidSteel_Vanguard boss;
        private LivingEntity target;
        private int dashTime;
        private enum DashType { NONE, EVASIVE, AGGRESSIVE }
        private DashType currentDashType = DashType.NONE;

        public JetDashGoal(The_VoidSteel_Vanguard boss) {
            this.boss = boss;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.boss.isAttacking() || this.boss.dashCooldown > 0 || !boss.isdead) return false;
            this.target = this.boss.getTarget();
            if (this.target == null || !this.target.isAlive() || !boss.isdead) return false;

            if (this.boss.getHealth() / this.boss.getMaxHealth() <= 0.3f && this.boss.random.nextInt(30) == 0) {
                this.currentDashType = DashType.EVASIVE;
                return true;
            }
            double distanceSqr = this.boss.distanceToSqr(this.target);
            if (distanceSqr > 25.0 && distanceSqr < 225.0 && this.boss.random.nextInt(40) == 0) {
                this.currentDashType = DashType.AGGRESSIVE;
                return true;
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() { return this.dashTime < 20 && this.target != null && this.target.isAlive() || !boss.isdead; }

        @Override
        public void start() {
            this.dashTime = 0;
            this.boss.getNavigation().stop();
            this.boss.level().broadcastEntityEvent(this.boss, (byte)8);
        }

        @Override
        public void stop() {
            this.boss.dashCooldown = 80;
            this.boss.dashAnimationState.stop();
            this.currentDashType = DashType.NONE;
        }

        @Override
        public void tick() {
            this.dashTime++;
            if (this.target != null) this.boss.getLookControl().setLookAt(this.target);
            if (this.dashTime == 5) {
                performSafePushDash();
            }
        }
        private void performSafePushDash() {
            if (this.target == null) return;

            // --- 1. ダッシュ方向を決定 ---
            Vec3 dashVector;
            if (this.currentDashType == DashType.EVASIVE) {
                dashVector = this.boss.position().subtract(this.target.position()).normalize();
            } else { // AGGRESSIVE
                Vec3 toTarget = this.target.position().subtract(this.boss.position());
                Vec3 sideDir = toTarget.cross(new Vec3(0, 1, 0)).normalize();
                if (this.boss.random.nextBoolean()) sideDir = sideDir.reverse();
                dashVector = sideDir;
            }

            // --- 2. ダッシュ後の予測着地点を計算 ---
            double dashStrength = 2.5; // ダッシュの強さ
            BlockPos landingPos = BlockPos.containing(
                    this.boss.getX() + dashVector.x * dashStrength,
                    this.boss.getY(),
                    this.boss.getZ() + dashVector.z * dashStrength
            );

            // --- 3. 着地点が安全かどうかをチェック ---
            if (!isSafeLanding(landingPos, 5)) { // 5ブロック下までチェック
                // 安全でないなら、ダッシュをキャンセル
                return;
            }

            // --- 4. 安全が確認できたので、ダッシュを実行 ---
            this.boss.push(dashVector.x * dashStrength, 0.2, dashVector.z * dashStrength);

            // エフェクト
            this.boss.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0F, 1.2F);
            if (this.boss.level() instanceof ServerLevel serverLevel) {
                // ... (パーティクル処理)
            }
        }

        /**
         * 着地点の安全性をチェックするメソッド (物理ダッシュ用)
         */
        private boolean isSafeLanding(BlockPos pos, int searchDepth) {
            Level level = this.boss.level();

            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());

            for (int i = 0; i < searchDepth; i++) {
                BlockState blockState = level.getBlockState(mutablePos);

                // 液体や、通り抜けられるブロックは危険
                if (blockState.liquid() || !blockState.blocksMotion()) {
                    // 何もしない、下に降りてチェックを続ける
                } else {
                    // 固いブロックが見つかったので安全
                    return true;
                }

                mutablePos.move(Direction.DOWN);
            }
            return false;
        }
    }
}