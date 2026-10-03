package ru.tpsd.eatinganimationmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class EatingAnimationClientMod implements ClientModInitializer {

    public static final String MOD_ID = "eatinganimationid";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        // Frames are picked in assets/<namespace>/items/<item>.json via these properties.
        RangeSelectItemModelProperties.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MOD_ID, "eat"), EatProperty.CODEC);
        RangeSelectItemModelProperties.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MOD_ID, "drink"), DrinkProperty.CODEC);

        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(EatingAnimationClientMod::registerSupportPacks);
    }

    /**
     * Registers resourcepacks/&lt;mod id&gt; as a built-in pack for each supported mod that is installed,
     * so item definitions never reference textures of mods that are missing.
     */
    private static void registerSupportPacks(ModContainer container) {
        container.findPath("resourcepacks").ifPresent(root -> {
            try (Stream<Path> packs = Files.list(root)) {
                packs.map(pack -> pack.getFileName().toString().replace("/", ""))
                        .forEach(modId -> FabricLoader.getInstance().getModContainer(modId).ifPresent(mod ->
                                ResourceLoader.registerBuiltinPack(Identifier.fromNamespaceAndPath(MOD_ID, modId), container,
                                        Component.literal("Eating Animation: " + mod.getMetadata().getName()), PackActivationType.DEFAULT_ENABLED)));
            } catch (IOException e) {
                LOGGER.error("Failed to list Eating Animation support packs", e);
            }
        });
    }
}
