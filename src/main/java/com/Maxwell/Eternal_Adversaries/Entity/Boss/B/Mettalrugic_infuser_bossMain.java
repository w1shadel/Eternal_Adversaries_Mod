package com.Maxwell.Eternal_Adversaries.Entity.Boss.B;

import com.Maxwell.Eternal_Adversaries.Entity.Boss.A.Volcanic_TyrantMain;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Mettalrugic_infuser_bossMain extends Monster implements Enemy, GeoEntity {
    public Mettalrugic_infuser_bossMain(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.MAX_HEALTH, 80)
                .add(Attributes.ARMOR, 1000)
                .add(Attributes.KNOCKBACK_RESISTANCE, 9999);
    }
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("Default");
    protected static final RawAnimation MOVE = RawAnimation.begin().thenLoop("move");
    protected static final RawAnimation FAR_ATK = RawAnimation.begin().thenPlay("far_atk");
    protected static final RawAnimation NEAR_ATK = RawAnimation.begin().thenPlay("Near_Atk");
    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Movecontroler", 5, this::movecontroler));
        controllers.add(new AnimationController<>(this, "Attacktrriger", 20, this::AttackController)
                .triggerableAnim("far_atk", FAR_ATK)
                .triggerableAnim("Near_Atk", NEAR_ATK));
    }
    protected <E extends Mettalrugic_infuser_bossMain> PlayState movecontroler(final AnimationState<E> event) {
        if (event.isMoving()) {
            return event.setAndContinue(MOVE);
        } else {

            return event.setAndContinue(IDLE);
        }
    }
    public int attackType;
    protected <E extends Mettalrugic_infuser_bossMain> PlayState AttackController(final AnimationState<E> event) {
        return switch (attackType) {
            case 1 -> event.setAndContinue(FAR_ATK); // Swing Attack
            case 2 -> event.setAndContinue(NEAR_ATK); // Throw Attack
            default -> PlayState.STOP; // アニメーションを停止
        };
    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
