package com.Maxwell.Eternal_Adversaries.Misc;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;

public class BossInfoServer extends ServerBossEvent {
    private int renderType;

    public BossInfoServer(Component component, BossBarColor bossBarColor, boolean dark, int renderType) {
        super(component, bossBarColor, BossBarOverlay.PROGRESS);
        this.setDarkenScreen(dark);
        this.renderType = renderType;
    }

    public void setRenderType(int renderType) {
        if (renderType != this.renderType) {
            this.renderType = renderType;
        }

    }

    public int getRenderType() {
        return this.renderType;
    }

    public void addPlayer(ServerPlayer serverPlayer) {
        super.addPlayer(serverPlayer);
    }

    public void removePlayer(ServerPlayer serverPlayer) {
        super.removePlayer(serverPlayer);
    }
}
