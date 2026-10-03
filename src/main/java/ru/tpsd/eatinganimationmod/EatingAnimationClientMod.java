package ru.tpsd.eatinganimationmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.item.property.numeric.NumericProperties;
import net.minecraft.util.Identifier;

public class EatingAnimationClientMod implements ClientModInitializer {

    public static final String MOD_ID = "eatinganimationid";

    @Override
    public void onInitializeClient() {
        // Frames are picked in assets/<namespace>/items/<item>.json via these properties.
        NumericProperties.ID_MAPPER.put(Identifier.of(MOD_ID, "eat"), EatProperty.CODEC);
        NumericProperties.ID_MAPPER.put(Identifier.of(MOD_ID, "drink"), DrinkProperty.CODEC);

        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(eatinganimation ->
                ResourceManagerHelper.registerBuiltinResourcePack(EatingAnimationClientMod.locate("supporteatinganimation"), eatinganimation, ResourcePackActivationType.DEFAULT_ENABLED));
    }

    public static Identifier locate(String path) {
        return Identifier.ofVanilla(path);
    }
}
