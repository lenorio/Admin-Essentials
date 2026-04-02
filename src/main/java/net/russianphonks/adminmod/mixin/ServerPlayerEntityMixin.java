package net.russianphonks.adminmod.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.russianphonks.adminmod.util.VanishState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements VanishState {
    @Unique
    private boolean adminmod$vanished;

    @Override
    public boolean adminmod$isVanished() {
        return adminmod$vanished;
    }

    @Override
    public void adminmod$setVanished(boolean vanished) {
        this.adminmod$vanished = vanished;
    }

    @Inject(method = "allowsServerListing", at = @At("HEAD"), cancellable = true)
    private void adminmod$allowsServerListing(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!adminmod$vanished);
    }
}