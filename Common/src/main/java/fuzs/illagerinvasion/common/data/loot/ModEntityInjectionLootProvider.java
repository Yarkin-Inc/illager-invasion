package fuzs.illagerinvasion.common.data.loot;

import fuzs.illagerinvasion.common.init.ModItems;
import fuzs.illagerinvasion.common.init.ModLootTables;
import fuzs.puzzleslib.common.api.data.v3.loot.AbstractLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;

public class ModEntityInjectionLootProvider extends AbstractLootSubProvider {

    public ModEntityInjectionLootProvider(LootTableSubProvider.Context context) {
        super(context);
    }

    @Override
    public void generate() {
        this.output.accept(ModLootTables.ILLUSIONER_INJECTION,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(Items.EMERALD)
                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 1)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.output.lookup(Registries.ENCHANTMENT),
                                                ContextFloatProviders.between(0, 1)))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())))
                        .withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(Items.ARROW)
                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 2)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.output.lookup(Registries.ENCHANTMENT),
                                                ContextFloatProviders.between(0, 1)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(ModItems.ILLUSIONARY_DUST_ITEM.value()))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.output.lookup(Registries.ENCHANTMENT),
                                        0.4F,
                                        0.2F))
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())));
        this.output.accept(ModLootTables.PILLAGER_INJECTION,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(ModItems.PLATINUM_CHUNK_ITEM.value())
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(
                                                this.output.lookup(Registries.ENCHANTMENT),
                                                0.5F,
                                                0.0625F))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())))
                        .withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(Items.EMERALD)
                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 1)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.output.lookup(Registries.ENCHANTMENT),
                                                ContextFloatProviders.between(0, 1)))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer()))));
        this.output.accept(ModLootTables.RAVAGER_INJECTION,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(ModItems.PLATINUM_CHUNK_ITEM.value())
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(
                                                this.output.lookup(Registries.ENCHANTMENT),
                                                0.5F,
                                                0.0625F))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer()))));
    }
}
