package fuzs.illagerinvasion.fabric.test;

import fuzs.illagerinvasion.common.IllagerInvasion;
import fuzs.illagerinvasion.common.client.gui.screens.inventory.ImbuingScreen;
import fuzs.illagerinvasion.common.init.ModEntityTypes;
import fuzs.illagerinvasion.common.init.ModRegistry;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;

import java.util.List;

/** Exercises the client renderers, menu synchronization, and the data-driven brewing recipe. */
public final class PortClientGameTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        try (var world = context.worldBuilder().create()) {
            var server = world.getServer();
            server.runCommand("gamemode creative @a");
            server.runCommand("time set noon");
            server.runCommand("tp @a 8 -60 0 0 0");
            var entityIds = server.computeOnServer(instance -> {
                var level = instance.overworld();
                var types = List.of(ModEntityTypes.ALCHEMIST_ENTITY_TYPE, ModEntityTypes.ARCHIVIST_ENTITY_TYPE,
                        ModEntityTypes.BASHER_ENTITY_TYPE, ModEntityTypes.FIRECALLER_ENTITY_TYPE,
                        ModEntityTypes.INQUISITOR_ENTITY_TYPE, ModEntityTypes.INVOKER_ENTITY_TYPE,
                        ModEntityTypes.MARAUDER_ENTITY_TYPE, ModEntityTypes.NECROMANCER_ENTITY_TYPE,
                        ModEntityTypes.PROVOKER_ENTITY_TYPE, ModEntityTypes.SORCERER_ENTITY_TYPE,
                        ModEntityTypes.SURRENDERED_ENTITY_TYPE);
                var ids = new java.util.ArrayList<Integer>();
                for (int i = 0; i < types.size(); i++) {
                    Mob mob = types.get(i).value().spawn(level, new BlockPos(i * 2 - 2, -60, 10), EntitySpawnReason.COMMAND);
                    if (mob == null) throw new AssertionError("Failed to spawn " + types.get(i).key());
                    mob.setNoAi(true);
                    mob.setPersistenceRequired();
                    ids.add(mob.getId());
                }
                level.setBlockAndUpdate(new BlockPos(8, -60, 2), ModRegistry.IMBUING_TABLE_BLOCK.value().defaultBlockState());
                level.setBlockAndUpdate(new BlockPos(8, -60, 3), Blocks.BREWING_STAND.defaultBlockState());
                var stand = (BrewingStandBlockEntity) level.getBlockEntity(new BlockPos(8, -60, 3));
                ItemStack potion = new ItemStack(Items.POTION);
                potion.set(DataComponents.POTION_CONTENTS, new PotionContents(net.minecraft.world.item.alchemy.Potions.AWKWARD));
                stand.setItem(0, potion);
                stand.setItem(3, new ItemStack(Items.GOAT_HORN));
                stand.setItem(4, new ItemStack(Items.BLAZE_POWDER));
                return ids;
            });
            world.getConnection().waitForChunksDownload();
            context.waitFor(client -> entityIds.stream().allMatch(id -> client.level.getEntity(id) != null));
            world.getConnection().waitForChunksRender();
            context.waitTicks(20);
            context.takeScreenshot("illager-invasion-26.3-mobs");

            server.runOnServer(instance -> {
                var level = instance.overworld();
                var pos = new BlockPos(8, -60, 2);
                instance.getPlayerList().getPlayers().getFirst().openMenu(level.getBlockState(pos).getMenuProvider(level, pos));
            });
            context.waitFor(client -> client.gui.screen() instanceof ImbuingScreen);
            context.waitTicks(5);
            context.takeScreenshot("illager-invasion-26.3-imbuing");
            context.runOnClient(client -> client.player.closeContainer());
            server.waitFor(instance -> {
                var stand = (BrewingStandBlockEntity) instance.overworld().getBlockEntity(new BlockPos(8, -60, 3));
                return stand.getItem(0).getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                        .is(ModRegistry.BERSERKING_POTION);
            }, 600);

            var reload = context.computeOnClient(client -> client.reloadResourcePacks());
            context.waitFor(client -> reload.isDone(), 1200);
            if (reload.isCompletedExceptionally()) throw new AssertionError("Resource reload failed");
            world.getConnection().waitForChunksRender();
        }
        IllagerInvasion.LOGGER.info("PASS: all illager renderers, imbuing screen, berserking brewing, resource reload");
    }
}
