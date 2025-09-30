package com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.capabilities;

import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Eternal_Adversaries.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCapabilities {

    /**
     * ★★★ これが、私たちのカスタムデータのCapability本体です ★★★
     * これを通じて、ワールドからデータを取得します。
     */
    public static final Capability<IFieldGeneratorData> FIELD_GENERATOR_DATA =
            CapabilityManager.get(new CapabilityToken<>() {});

    /**
     * MODイベントバスで呼ばれる登録イベント。
     * Forgeに、IFieldGeneratorDataインターフェースをCapabilityとして認識させる。
     */
    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        System.out.println("[DEBUG-1] Capability registration event fired!"); // 検問所1
        event.register(IFieldGeneratorData.class);
    }
}