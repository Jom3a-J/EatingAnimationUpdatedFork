package ru.tpsd.eatinganimationmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.resources.Identifier;

public class EatingAnimationClientMod implements ClientModInitializer {

    public static final String MOD_ID = "eatinganimationid";

    @Override
    public void onInitializeClient() {
        // Frames are picked in assets/<namespace>/items/<item>.json via these properties.
        RangeSelectItemModelProperties.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MOD_ID, "eat"), EatProperty.CODEC);
        RangeSelectItemModelProperties.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MOD_ID, "drink"), DrinkProperty.CODEC);

        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(eatinganimation ->
                ResourceLoader.registerBuiltinPack(EatingAnimationClientMod.locate("supporteatinganimation"), eatinganimation, PackActivationType.DEFAULT_ENABLED));
    }

    public static Identifier locate(String path) {
        return Identifier.withDefaultNamespace(path);
    }
}
