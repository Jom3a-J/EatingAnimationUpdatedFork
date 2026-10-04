package ru.tpsd.eatinganimationmod;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Item model property {@code eatinganimationid:drink}: drinking progress used to pick the sip frame.
 */
public record DrinkProperty() implements NumericProperty {

    public static final MapCodec<DrinkProperty> CODEC = MapCodec.unit(new DrinkProperty());

    @Override
    public float getValue(ItemStack itemStack, @Nullable ClientWorld world, @Nullable LivingEntity livingEntity, int seed) {
        if (livingEntity == null || livingEntity.getActiveItem() != itemStack) {
            return 0.0F;
        }
        return (itemStack.getMaxUseTime(livingEntity) - livingEntity.getItemUseTimeLeft()) / 30.0F;
    }

    @Override
    public MapCodec<DrinkProperty> getCodec() {
        return CODEC;
    }
}
