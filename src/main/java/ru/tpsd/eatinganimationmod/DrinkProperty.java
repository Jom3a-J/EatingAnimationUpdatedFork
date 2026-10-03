package ru.tpsd.eatinganimationmod;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Item model property {@code eatinganimationid:drink}: drinking progress used to pick the sip frame.
 */
public record DrinkProperty() implements RangeSelectItemModelProperty {

    public static final MapCodec<DrinkProperty> CODEC = MapCodec.unit(new DrinkProperty());

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        LivingEntity livingEntity = owner == null ? null : owner.asLivingEntity();
        if (livingEntity == null || livingEntity.getUseItem() != itemStack) {
            return 0.0F;
        }
        return (itemStack.getUseDuration(livingEntity) - livingEntity.getUseItemRemainingTicks()) / 30.0F;
    }

    @Override
    public MapCodec<DrinkProperty> type() {
        return CODEC;
    }
}
