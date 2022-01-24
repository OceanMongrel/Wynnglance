package om.wynnglance;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class RarityMatcher {
    private static final Identifier TEXTURE = new Identifier("wynnglance", "textures/aura.png");

    private final String[] matches;
    private final float r;
    private final float g;
    private final float b;

    public RarityMatcher(float r, float g, float b, String... matches) {
        this.matches = matches;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void apply(MatrixStack matrices, Slot slot) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(r, g, b, 0.5F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        DrawableHelper.drawTexture(matrices, slot.x - 1, slot.y - 1, 0, 0, 18, 18, 18, 18);
        RenderSystem.disableBlend();
    }

    public boolean canApply(String display) {
        for (String i : matches) {
            if (display.contains(i)) {
                return true;
            }
        }
        return false;
    }
}
