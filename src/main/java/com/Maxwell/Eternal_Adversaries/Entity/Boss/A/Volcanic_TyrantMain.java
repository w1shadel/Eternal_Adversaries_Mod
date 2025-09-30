package com.Maxwell.Eternal_Adversaries.Entity.Boss.A;
import com.Maxwell.Eternal_Adversaries.Entity.AI.HurtByNearestTargetGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class Volcanic_TyrantMain extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // --- 状態管理：enumを使って、コードを読みやすくします ---
    public enum ActionState {
        IDLE,       // 待機
        WALKING,    // 移動
        ATTACKING   // 攻撃中
    }
    public ActionState currentState = ActionState.IDLE;
    private int attackCooldown = 0;

    // --- アニメーションの定義 ---
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("default");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    // 攻撃アニメーション名は、あなたのBlockbenchでの設定に合わせてください
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("sippo");

    public Volcanic_TyrantMain(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setMaxUpStep(1.25F);

    }

    // --- 状態を外部（AI）から変更するためのメソッド ---
    public void setActionState(ActionState state) { this.currentState = state; }
    public void setAttackCooldown(int ticks) { this.attackCooldown = ticks; }
    public int getAttackCooldown() { return this.attackCooldown; }

    // --- Mobのステータス設定 ---
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 400.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    // --- AI Goalの登録 ---
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SimpleAttackGoal(
                this,
                30,     // アニメーションの長さ (tick)
                18,     // ダメージの発生タイミング (tick)
                30,     // クールダウンの時間 (tick)
                6.0,// 攻撃射程 (ブロック)
                "sippo" // アニメーションの名前、triggerbleanimationじゃないとダメよ
        ));
        this.goalSelector.addGoal(2, new ChaseTargetGoal(this,0.2f,40));
        // 2. 平時の行動
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        // 3. ターゲットの選定
        this.targetSelector.addGoal(1, new HurtByNearestTargetGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // --- 毎tick実行される処理 ---
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }
    }
    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 0, this::predicate)
                .triggerableAnim("sippo",ATTACK_ANIM));
    }

    private <E extends GeoAnimatable> PlayState predicate(final AnimationState<E> event) {
        // 攻撃中は、他のアニメーションに絶対に割り込ませない
        if (this.currentState == ActionState.ATTACKING) {
            return event.setAndContinue(ATTACK_ANIM);
        }
        if (event.isMoving()) {
            return event.setAndContinue(WALK_ANIM);
        } else {
            return event.setAndContinue(IDLE_ANIM);
        }
    }

    // --- ダメージ処理 ---
    public void performAttack(float damage, double range) {
        AABB attackBounds = this.getBoundingBox().inflate(range);
        List<Player> players = this.level().getEntitiesOfClass(Player.class, attackBounds);
        for (Player player : players) {
            if (player.isAlive()) {
                player.hurt(this.damageSources().mobAttack(this), damage);
            }
        }
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}