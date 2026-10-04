package ru.tpsd.eatinganimationmod;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Item model property {@code eatinganimationid:eat}: eating progress used to pick the bite frame.
 */
public record EatProperty() implements NumericProperty {

    public static final MapCodec<EatProperty> CODEC = MapCodec.unit(new EatProperty());

    @Override
    public float getValue(ItemStack itemStack, @Nullable ClientWorld world, @Nullable LivingEntity livingEntity, int seed) {
        if (livingEntity == null || livingEntity.getActiveItem() != itemStack) {
            return 0.0F;
        }
        if (livingEntity instanceof OtherClientPlayerEntity) {
            if (itemStack.getMaxUseTime(livingEntity) > 16) {
                return ((float) livingEntity.getItemUseTime() / (float) itemStack.getMaxUseTime(livingEntity)) % 1;
            }
            else {
                return ((float) livingEntity.getItemUseTime() / 32.0f) % 0.5F;
            }
        }
        return (itemStack.getMaxUseTime(livingEntity) - livingEntity.getItemUseTimeLeft()) / 30.0F;
    }

    @Override
    public MapCodec<EatProperty> getCodec() {
        return CODEC;
    }
}
