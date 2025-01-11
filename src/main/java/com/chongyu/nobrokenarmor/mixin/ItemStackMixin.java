package com.chongyu.nobrokenarmor.mixin;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract int getMaxDamage();
    @Shadow public abstract int getDamage();
    @Shadow public abstract Item getItem();

    @Inject(at = @At("HEAD"), method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", cancellable = true)
    public void damage(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if(this.getItem() instanceof ArmorItem){
            //装备耐久为1或者伤害数量-耐久>1
            if(this.getMaxDamage() - this.getDamage() <= 1){
                cir.setReturnValue(false);
            }
            else if( amount - (this.getMaxDamage() - this.getDamage()) >= 1){
                int i = this.getMaxDamage() - this.getDamage()- 1 ;
                ((ItemStack)(Object)this).setDamage(this.getMaxDamage()-1);
                cir.setReturnValue(false);
            }
        }
    }
}
