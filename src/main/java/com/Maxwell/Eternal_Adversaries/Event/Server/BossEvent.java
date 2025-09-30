package com.Maxwell.Eternal_Adversaries.Event.Server;

import com.Maxwell.Eternal_Adversaries.Entity.Boss.C.The_VoidSteel_Vanguard;
import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Eternal_Adversaries.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BossEvent {
    @SubscribeEvent
    public static void onBossHurt(LivingHurtEvent event) {
        // ダメージを受けたのが、私たちのボスかどうか？
        if (event.getEntity() instanceof The_VoidSteel_Vanguard boss) {

            // ボスがガード状態でないなら、何もしない
            if (!boss.isGuarding()) {
                return;
            }

            // ダメージを与えた攻撃者を取得
            Entity attacker = event.getSource().getEntity();
            if (attacker != null) {
                // --- 前方からの攻撃かどうかを判定 ---

                // 1. ボスの向いている方向のベクトルを取得
                Vec3 lookVector = boss.getLookAngle();

                // 2. ボスから攻撃者への方向のベクトルを取得
                Vec3 toAttackerVector = attacker.position().subtract(boss.position()).normalize();

                // 3. 2つのベクトルの「内積」を計算
                //    内積がプラスなら、攻撃者はボスの前方180度にいる
                double dotProduct = lookVector.dot(toAttackerVector);

                // 4. 内積が一定値以上（例: 0.5以上なら前方約60度）なら、攻撃は正面からのものとみなす
                if (dotProduct > 0.5D) {

                    // ★★★ ダメージをキャンセル！ ★★★
                    event.setCanceled(true);
                    boss.level().playSound(
                            null, // [修正点1] 特定のプレイヤーではなく、周囲の全員に聞こえるようにするためnullを渡す
                            boss.blockPosition(), // [修正点2] 音が発生する場所。boss.blockPosition()が最もシンプルで確実
                            SoundEvents.SHIELD_BLOCK, // [提案] ANVIL_LANDよりも、盾ガード音の方が状況に適しているかもしれません
                            SoundSource.HOSTILE, // [修正点2] サウンドカテゴリを「敵対的Mob」に設定
                            1.0F, // 音量 (1.0Fが標準)
                            0.8F + boss.getRandom().nextFloat() * 0.4F // ピッチ (少しランダムにすると、毎回同じ音にならず自然に聞こえる)
                    );
                    // (任意) ガード成功時の効果音やパーティクルを再生
                    // boss.level().playSound(...)
                    // ((ServerLevel)boss.level()).sendParticles(...)
                }
            }
        }
    }
}
