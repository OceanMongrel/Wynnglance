package om.wynnglance.mixin;

import om.wynnglance.WynnglanceClient;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    private static final RarityMatcher[] rarityMatchers = new RarityMatcher[8];

    static {
        rarityMatchers[0] = new RarityMatcher(0.0F, 0.66666F, 0.66666F, "§3Crafted");
        rarityMatchers[1] = new RarityMatcher(0.33333F, 1.0F, 0.33333F, "§aSet");
        rarityMatchers[2] = new RarityMatcher(0.66666F, 0.0F, 0.66666F, "§5Mythic");
        rarityMatchers[3] = new RarityMatcher(1.0F, 0.33333F, 0.33333F, "§cFabled");
        rarityMatchers[4] = new RarityMatcher(0.33333F, 1.0F, 1.0F, "§bLegendary", "§b✫✫✫");
        rarityMatchers[5] = new RarityMatcher(1.0F, 0.33333F, 1.0F, "§dRare", "§d✫✫§8✫");
        rarityMatchers[6] = new RarityMatcher(1.0F, 1.0F, 0.33333F, "§eUnique", "§e✫§8✫✫");
        rarityMatchers[7] = new RarityMatcher(1.0F, 1.0F, 1.0F, "§fNormal", "§8✫✫✫");
    }

    @Inject(method = "drawSlot", at = @At(value = "HEAD"))
    private void drawSlotInject(MatrixStack matrices, Slot slot, CallbackInfo ci) {
        ItemStack stack = slot.getStack();

        if (stack.isEmpty()) return;

        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !nbt.contains("display")) return;

        String display = nbt.get("display").asString();

        if (display.contains("§6Ingredient") || display.contains("§7Crafting")) return;

        for (RarityMatcher rarityMatcher : rarityMatchers) {
            if (rarityMatcher.canApply(display)) {
                rarityMatcher.apply(display);
                return;
            }
        }
    }

    public static class RarityMatcher {
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

        public void apply(String display) {
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
}
