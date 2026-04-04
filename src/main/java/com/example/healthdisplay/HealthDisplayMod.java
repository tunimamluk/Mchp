package com.example.healthdisplay;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class HealthDisplayMod implements ClientModInitializer {

    public static final String MOD_ID = "healthdisplay";

    @Override
    public void onInitializeClient() {
        // Register config (loads from / creates config/healthdisplay.json)
        AutoConfig.register(HealthDisplayConfig.class, GsonConfigSerializer::new);

        // Register HUD callback – fires every frame after the vanilla HUD is drawn
        HudRenderCallback.EVENT.register(HealthDisplayHud::render);
    }

    /** Convenience accessor used by the HUD renderer and config screen. */
    public static HealthDisplayConfig getConfig() {
        return AutoConfig.getConfigHolder(HealthDisplayConfig.class).getConfig();
    }
}
