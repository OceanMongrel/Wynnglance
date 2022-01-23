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

    private Identifier TEXTURE = new Identifier("wynnglance", "textures/aura.png");
    private int rarity = 0;

    private int detectRarity(String display, String ... match) {
        for (String i : match) {
            if (display.contains(i)
                && !display.contains("§6Ingredient")
                && !display.contains("§7Crafting")) {
                return 1;
            }
        }
        return 0;
    }

    private float getChannel(int channel) {
        return WynnglanceClient.rarityColours[rarity - 1][channel];
    }

    @Inject(method = "drawSlot", at = @At(value = "HEAD"))
    private void drawSlotInject(MatrixStack matrices, Slot slot, CallbackInfo ci) {
        ItemStack stack = slot.getStack();
        if (!stack.isEmpty()) {
            NbtCompound nbt = stack.getNbt();
            if (nbt != null) {
                if (nbt.contains("display")) {
                    String nbtDisplay = nbt.get("display").asString();
                    rarity =
                        Math.max(detectRarity(nbtDisplay, "§fNormal", "§8✫✫✫") * 1,
                        Math.max(detectRarity(nbtDisplay, "§eUnique", "§e✫§8✫✫") * 2,
                        Math.max(detectRarity(nbtDisplay, "§dRare", "§d✫✫§8✫") * 3,
                        Math.max(detectRarity(nbtDisplay, "§bLegendary", "§b✫✫✫") * 4,
                        Math.max(detectRarity(nbtDisplay, "§cFabled") * 5,
                        Math.max(detectRarity(nbtDisplay, "§5Mythic") * 6,
                        Math.max(detectRarity(nbtDisplay, "§aSet") * 7,
                        detectRarity(nbtDisplay, "§3Crafted") * 8)))))));
                }
            }
            if (rarity > 0) {
                RenderSystem.enableBlend();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(getChannel(0), getChannel(1), getChannel(2), 0.5F);
                RenderSystem.setShaderTexture(0, TEXTURE);
                DrawableHelper.drawTexture(matrices, slot.x - 1, slot.y - 1, 0, 0, 18, 18, 18, 18);
                RenderSystem.disableBlend();
            }
        }
    }
}
