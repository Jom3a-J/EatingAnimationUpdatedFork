package ru.tpsd.eatinganimationmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

public class EatingAnimationClientMod implements ClientModInitializer {

    public static final String MOD_ID = "eatinganimationid";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final ArrayList<Item> FOOD_ITEMS = new ArrayList<>(Registries.ITEM.stream().filter(
            p -> p.getDefaultStack().getComponents().contains(DataComponentTypes.FOOD)).toList());
   static {
       FOOD_ITEMS.add(Items.MILK_BUCKET);
   }

    @Override
    public void onInitializeClient() {
        for (Item item : FOOD_ITEMS) {
            ModelPredicateProviderRegistry.register(item, Identifier.of("eat"), (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity == null) {
                    return 0.0F;
                }
                if(livingEntity instanceof OtherClientPlayerEntity) {
                    if(itemStack.getMaxUseTime(livingEntity) > 16) {
                        return livingEntity.getActiveItem() != itemStack ? 0.0F : ((float)livingEntity.getItemUseTime() / (float)itemStack.getMaxUseTime(livingEntity)) % 1;
                    }
                    else {
                        return livingEntity.getActiveItem() != itemStack ? 0.0F : ((float)livingEntity.getItemUseTime() / 32.0f) % 0.5F;
                    }
                }
                return livingEntity.getActiveItem() != itemStack ? 0.0F : (itemStack.getMaxUseTime(livingEntity) - livingEntity.getItemUseTimeLeft()) / 30.0F;
            });

            ModelPredicateProviderRegistry.register(item, Identifier.of("eating"), (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity == null) {
                    return 0.0F;
                }
                return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
            });

        }
        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(EatingAnimationClientMod::registerSupportPacks);
    }

    /**
     * Registers resourcepacks/&lt;mod id&gt; as a built-in pack for each supported mod that is installed,
     * so item models never reference textures of mods that are missing.
     */
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
