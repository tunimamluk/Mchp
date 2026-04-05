package com.example.healthdisplay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;

public final class HealthDisplayHud {

    private HealthDisplayHud() {}

    // ─────────────────────────────────────────────────────────────────────────
    // Entry point (registered via HudRenderCallback)
    // ─────────────────────────────────────────────────────────────────────────

    public static void render(DrawContext drawContext, RenderTickCounter tickCounter) {
        HealthDisplayConfig cfg = HealthDisplayMod.getConfig();
        if (!cfg.enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;

        PlayerEntity player = client.player;
        float health    = player.getHealth();
        float maxHealth = player.getMaxHealth();

        String text  = buildText(cfg, health, maxHealth);
        int    color = computeColor(cfg, health, maxHealth);
        float  scale = scaleOf(cfg.fontSize);

        TextRenderer tr = client.textRenderer;
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();

        // Text dimensions in screen-space (scaled)
        int textW = Math.round(tr.getWidth(text) * scale);
        int textH = Math.round(tr.fontHeight    * scale);

        int[] pos     = computePosition(cfg, sw, sh, textW, textH);
        int screenX   = pos[0];
        int screenY   = pos[1];

        // Push a scaled matrix so the text and background are enlarged/shrunk
        // together.  All draw calls inside use pre-scaled coords (= screen / scale).
        // In 1.21.11+ DrawContext.getMatrices() returns Matrix3x2fStack (2-D only).
        var matrices = drawContext.getMatrices();
        matrices.pushMatrix();
        matrices.scale(scale, scale);

        float inv  = 1.0f / scale;
        int   dx   = Math.round(screenX * inv);
        int   dy   = Math.round(screenY * inv);
        int   rawW = tr.getWidth(text);
        int   rawH = tr.fontHeight;

        if (cfg.showBackground) {
            int pad = 3;
            // 0x80000000 = semi-transparent black (alpha = 128)
            drawContext.fill(dx - pad, dy - pad, dx + rawW + pad, dy + rawH + pad, 0x80000000);
        }

        drawContext.drawText(tr, text, dx, dy, color, true);
        matrices.popMatrix();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Text formatting
    // ─────────────────────────────────────────────────────────────────────────

    private static String buildText(HealthDisplayConfig cfg, float health, float maxHealth) {
        // When showDecimals is on, keep the .5 from half-hearts; otherwise floor.
        boolean hasHalf = cfg.showDecimals && (health % 1.0f != 0.0f);
        String  hp      = hasHalf ? String.format("%.1f", health) : String.valueOf((int) health);

        return switch (cfg.format) {
            case NUMERIC    -> hp + " HP";
            case FRACTION   -> hp + " / " + (int) maxHealth;
            case PERCENTAGE -> (int) ((health / maxHealth) * 100) + "%";
            case WITH_ICON  -> "\u2764 " + hp;   // ❤
        };
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Colour
    // ─────────────────────────────────────────────────────────────────────────

    private static int computeColor(HealthDisplayConfig cfg, float health, float maxHealth) {
        if (cfg.colorMode == HealthDisplayConfig.ColorMode.STATIC) {
            return 0xFF000000 | (cfg.staticColor & 0xFFFFFF);
        }

        // Dynamic gradient: green (0x55FF55) → yellow (0xFFFF55) → red (0xFF5555)
        float ratio = maxHealth > 0f ? health / maxHealth : 0f;
        int r, g;
        if (ratio >= 0.5f) {
            // upper half: r ramps 0x55 → 0xFF as ratio drops from 1.0 to 0.5
            float t = (ratio - 0.5f) * 2.0f;         // 1 at full health, 0 at 50%
            r = 0x55 + Math.round((1.0f - t) * (0xFF - 0x55));
            g = 0xFF;
        } else {
            // lower half: g ramps 0xFF → 0x55 as ratio drops from 0.5 to 0
            float t = ratio * 2.0f;                   // 1 at 50%, 0 at death
            r = 0xFF;
            g = 0x55 + Math.round(t * (0xFF - 0x55));
        }
        return 0xFF000000 | (r << 16) | (g << 8) | 0x55;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Font size → scale factor
    // ─────────────────────────────────────────────────────────────────────────

    private static float scaleOf(HealthDisplayConfig.FontSize size) {
        return switch (size) {
            case SMALL  -> 0.75f;
            case MEDIUM -> 1.0f;
            case LARGE  -> 1.5f;
        };
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Position  (all values are in screen-space pixels)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns [x, y] in screen-space pixels for the top-left corner of the text.
     *
     * <p>BOTTOM_* positions sit just above the hotbar (22 px at the very bottom).
     * ABOVE_HEALTH_BAR centres the text above the vanilla hearts row (~55 px up).
     */
    private static int[] computePosition(HealthDisplayConfig cfg,
                                          int sw, int sh, int tw, int th) {
        final int MARGIN  = 5;
        final int HOTBAR  = 22;  // height of the vanilla hotbar strip
        final int HEARTS_OFFSET = 55; // empirical offset to clear the hearts row

        return switch (cfg.position) {
            case TOP_LEFT    -> new int[]{ MARGIN,               MARGIN };
            case TOP_RIGHT   -> new int[]{ sw - tw - MARGIN,     MARGIN };
            case BOTTOM_LEFT -> new int[]{ MARGIN,               sh - th - MARGIN - HOTBAR };
            case BOTTOM_RIGHT-> new int[]{ sw - tw - MARGIN,     sh - th - MARGIN - HOTBAR };
            case ABOVE_HEALTH_BAR -> new int[]{ sw / 2 - tw / 2, sh - th - HEARTS_OFFSET };
        };
    }
}
