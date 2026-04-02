package net.russianphonks.adminmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.text.Text;
import net.minecraft.server.network.ServerPlayerEntity;
import net.russianphonks.adminmod.util.ColorUtil;

public class InvSeeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("invsee")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("PlayerEntity", EntityArgumentType.player())
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "PlayerEntity");

                        if (source.getEntity() instanceof ServerPlayerEntity admin) {
                            PlayerInventory targetInventory = target.getInventory();
                            SimpleInventory viewInventory = new SimpleInventory(45);
                            copyInventory(targetInventory, viewInventory);
                            viewInventory.addListener(changedInventory -> copyInventory(changedInventory, targetInventory));

                            Text title = ColorUtil.colorize("&8Viewing &b" + target.getName().getString() + "&8's inventory");
                            admin.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                                    (syncId, playerInventory, playerEntity) -> new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X5, syncId, playerInventory, viewInventory, 5),
                                    title
                            ));

                            source.sendFeedback(() -> ColorUtil.colorize("&aViewing &b" + target.getName().getString() + "&a's inventory"), false);
                        } else {
                            source.sendError(Text.literal("§cOnly players can use this command!"));
                        }
                        return 1;
                    })
                )
        );
    }

    private static void copyInventory(Inventory from, Inventory to) {
        int slotCount = Math.min(from.size(), to.size());
        for (int slot = 0; slot < slotCount; slot++) {
            to.setStack(slot, from.getStack(slot).copy());
        }
        to.markDirty();
    }
}
