package om.wynnglance;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class WynnglanceClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {}

    public static float[][] rarityColours = {
            {1.0F, 1.0F, 1.0F},
            {1.0F, 1.0F, 0.33333F},
            {1.0F, 0.33333F, 1.0F},
            {0.33333F, 1.0F, 1.0F},
            {1.0F, 0.33333F, 0.33333F},
            {0.66666F, 0.0F, 0.66666F},
            {0.33333F, 1.0F, 0.33333F},
            {0.0F, 0.66666F, 0.66666F}
    };
}
