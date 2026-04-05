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

    // ── Gap Reminder ─────────────────────────────────────────────────────────
    public boolean gapEnabled     = false;
    /** HP value (0–20 scale). Reminder fires when health is strictly below this. */
    public int     gapThreshold   = 8;
    public GapName       gapName       = GapName.GAPPLE;
    public ReminderStyle reminderStyle = ReminderStyle.BLINK;

    public enum GapName {
        GOLDEN_APPLE, GAPPLE, GAP
    }

    public enum ReminderStyle {
        BLINK, TITLE, OVERLAY_TEXT, VIGNETTE
    }
}
