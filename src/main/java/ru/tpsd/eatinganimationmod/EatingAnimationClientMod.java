package ru.tpsd.eatinganimationmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.render.item.property.numeric.NumericProperties;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
        NumericProperties.ID_MAPPER.put(Identifier.of(MOD_ID, "eat"), EatProperty.CODEC);
        NumericProperties.ID_MAPPER.put(Identifier.of(MOD_ID, "drink"), DrinkProperty.CODEC);

        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(EatingAnimationClientMod::registerSupportPacks);
    }

    /**
     * Registers resourcepacks/&lt;mod id&gt; as a built-in pack for each supported mod that is installed,
     * so item definitions never reference textures of mods that are missing.
     */
    @SuppressWarnings("deprecation") // ResourceManagerHelper is the only built-in pack API on every Fabric API for 1.21.9-1.21.11
    private static void registerSupportPacks(ModContainer container) {
        container.findPath("resourcepacks").ifPresent(root -> {
            try (Stream<Path> packs = Files.list(root)) {
                packs.map(pack -> pack.getFileName().toString().replace("/", ""))
                        .forEach(modId -> FabricLoader.getInstance().getModContainer(modId).ifPresent(mod ->
                                ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(MOD_ID, modId), container,
                                        Text.literal("Eating Animation: " + mod.getMetadata().getName()), ResourcePackActivationType.DEFAULT_ENABLED)));
            } catch (IOException e) {
                LOGGER.error("Failed to list Eating Animation support packs", e);
            }
        });
    }
}
