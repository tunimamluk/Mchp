package com.example.healthdisplay;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

/**
 * Persistent config for Health Display.
 * Serialised to {@code config/healthdisplay.json} by AutoConfig / GsonConfigSerializer.
 */
@Config(name = "healthdisplay")
public class HealthDisplayConfig implements ConfigData {

    // ── General ──────────────────────────────────────────────────────────────
    public boolean enabled = true;

    // ── Position ─────────────────────────────────────────────────────────────
    public Position position = Position.ABOVE_HEALTH_BAR;

    // ── Format ───────────────────────────────────────────────────────────────
    public Format format = Format.NUMERIC;

    // ── Colour ───────────────────────────────────────────────────────────────
    public ColorMode colorMode = ColorMode.DYNAMIC;
    /** RGB int (no alpha); used only when colorMode == STATIC. */
    public int staticColor = 0xFFFFFF;

    // ── Appearance ───────────────────────────────────────────────────────────
    public FontSize fontSize = FontSize.MEDIUM;
    public boolean showBackground = true;
    public boolean showDecimals = false;

    // ── Enums ─────────────────────────────────────────────────────────────────

    public enum Position {
        TOP_LEFT, TOP_RIGHT,
        BOTTOM_LEFT, BOTTOM_RIGHT,
        ABOVE_HEALTH_BAR
    }

    public enum Format {
        /** "14 HP" */
        NUMERIC,
        /** "14 / 20" */
        FRACTION,
        /** "70%" */
        PERCENTAGE,
        /** "❤ 14" */
        WITH_ICON
    }

    public enum ColorMode {
        STATIC, DYNAMIC
    }

    public enum FontSize {
        SMALL, MEDIUM, LARGE
    }
}
