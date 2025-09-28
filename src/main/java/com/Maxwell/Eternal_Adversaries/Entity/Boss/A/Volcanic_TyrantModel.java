package com.Maxwell.Eternal_Adversaries.Entity.Boss.A;

import com.Maxwell.Eternal_Adversaries.Eternal_Adversaries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;
@OnlyIn(Dist.CLIENT)
public class Volcanic_TyrantModel extends GeoModel<Volcanic_TyrantMain> {

    // --- 推奨される修正 ---
    // 各リソースをstatic finalな定数として一度だけ定義します。
    // これにより、メソッドが呼ばれるたびに新しいオブジェクトが生成されるのを防ぎ、効率が良くなります。
    // コンストラクタの代わりに ResourceLocation.fromNamespaceAndPath() を使用します。
    private static final ResourceLocation MODEL_RESOURCE = ResourceLocation.fromNamespaceAndPath(Eternal_Adversaries.MODID, "geo/entity/volcanic_tyrant.geo.json");
    private static final ResourceLocation TEXTURE_RESOURCE = ResourceLocation.fromNamespaceAndPath(Eternal_Adversaries.MODID, "textures/entity/volvanic_tyrant.png");
    private static final ResourceLocation ANIMATION_RESOURCE = ResourceLocation.fromNamespaceAndPath(Eternal_Adversaries.MODID, "animations/entity/volcanic.animation.json");

    @Override
    public ResourceLocation getModelResource(Volcanic_TyrantMain animatable) {
        // 定数を返すだけにします
        return MODEL_RESOURCE;
    }

    @Override
    public ResourceLocation getTextureResource(Volcanic_TyrantMain animatable) {
        // 定数を返すだけにします
        return TEXTURE_RESOURCE;
    }

    @Override
    public ResourceLocation getAnimationResource(Volcanic_TyrantMain animatable) {
        // 定数を返すだけにします
        return ANIMATION_RESOURCE;
    }
}