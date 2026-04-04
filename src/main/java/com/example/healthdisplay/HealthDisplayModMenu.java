package com.example.healthdisplay;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * Registers the mod with Mod Menu and builds the Cloth Config settings screen.
 * Entrypoint key: "modmenu" in fabric.mod.json.
 */
public class HealthDisplayModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::buildScreen;
    }

    private Screen buildScreen(Screen parent) {
        HealthDisplayConfig cfg = HealthDisplayMod.getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("config.healthdisplay.title"))
                // Persist to disk when the user clicks Done
                .setSavingRunnable(() ->
                        AutoConfig.getConfigHolder(HealthDisplayConfig.class).save());

        ConfigCategory general = builder.getOrCreateCategory(
                Text.translatable("config.healthdisplay.category.general"));
        ConfigEntryBuilder eb = builder.entryBuilder();

        // ── Enable / disable ────────────────────────────────────────────────
        general.addEntry(eb
                .startBooleanToggle(
                        Text.translatable("config.healthdisplay.enabled"), cfg.enabled)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.healthdisplay.enabled.tooltip"))
                .setSaveConsumer(v -> cfg.enabled = v)
                .build());

        // ── Position ────────────────────────────────────────────────────────
        general.addEntry(eb
                .startEnumSelector(
                        Text.translatable("config.healthdisplay.position"),
                        HealthDisplayConfig.Position.class,
                        cfg.position)
                .setDefaultValue(HealthDisplayConfig.Position.ABOVE_HEALTH_BAR)
                .setEnumNameProvider(e ->
                        Text.translatable("config.healthdisplay.position."
                                + e.name().toLowerCase()))
                .setSaveConsumer(v -> cfg.position = v)
                .build());

        // ── Display format ───────────────────────────────────────────────────
        general.addEntry(eb
                .startEnumSelector(
                        Text.translatable("config.healthdisplay.format"),
                        HealthDisplayConfig.Format.class,
                        cfg.format)
                .setDefaultValue(HealthDisplayConfig.Format.NUMERIC)
                .setEnumNameProvider(e ->
                        Text.translatable("config.healthdisplay.format."
                                + e.name().toLowerCase()))
                .setSaveConsumer(v -> cfg.format = v)
                .build());

        // ── Colour mode ──────────────────────────────────────────────────────
        general.addEntry(eb
                .startEnumSelector(
                        Text.translatable("config.healthdisplay.color_mode"),
                        HealthDisplayConfig.ColorMode.class,
                        cfg.colorMode)
                .setDefaultValue(HealthDisplayConfig.ColorMode.DYNAMIC)
                .setEnumNameProvider(e ->
                        Text.translatable("config.healthdisplay.color_mode."
                                + e.name().toLowerCase()))
                .setSaveConsumer(v -> cfg.colorMode = v)
                .build());

        // ── Static colour picker ─────────────────────────────────────────────
        general.addEntry(eb
                .startColorField(
                        Text.translatable("config.healthdisplay.static_color"),
                        cfg.staticColor)
                .setDefaultValue(0xFFFFFF)
                .setTooltip(Text.translatable("config.healthdisplay.static_color.tooltip"))
                .setSaveConsumer(v -> cfg.staticColor = v)
                .build());

        // ── Font size ────────────────────────────────────────────────────────
        general.addEntry(eb
                .startEnumSelector(
                        Text.translatable("config.healthdisplay.font_size"),
                        HealthDisplayConfig.FontSize.class,
                        cfg.fontSize)
                .setDefaultValue(HealthDisplayConfig.FontSize.MEDIUM)
                .setEnumNameProvider(e ->
                        Text.translatable("config.healthdisplay.font_size."
                                + e.name().toLowerCase()))
                .setSaveConsumer(v -> cfg.fontSize = v)
                .build());

        // ── Background pill ──────────────────────────────────────────────────
        general.addEntry(eb
                .startBooleanToggle(
                        Text.translatable("config.healthdisplay.show_background"),
                        cfg.showBackground)
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.healthdisplay.show_background.tooltip"))
                .setSaveConsumer(v -> cfg.showBackground = v)
                .build());

        // ── Decimal display ──────────────────────────────────────────────────
        general.addEntry(eb
                .startBooleanToggle(
                        Text.translatable("config.healthdisplay.show_decimals"),
                        cfg.showDecimals)
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.healthdisplay.show_decimals.tooltip"))
                .setSaveConsumer(v -> cfg.showDecimals = v)
                .build());

        return builder.build();
    }
}
