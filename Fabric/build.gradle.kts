plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-fabric")
}

dependencies {
    modApi(sharedLibs.fabricapi.fabric)
    modApi(sharedLibs.puzzleslib.fabric)
    modApi(sharedLibs.multiloaderdataextensions.fabric)
    include(sharedLibs.multiloaderdataextensions.fabric)
}

tasks.processResources {
    // Fabric registers these distances directly in VillagerGoalHandler.
    exclude("data/neoforge/data_maps/entity_type/acceptable_villager_distances.json")
}

multiloader {
    mixins {
        mixin("Raid\u0024RaiderTypeFabricMixin")
    }
}

fabricApi.configureTests {
    createSourceSet = true
    modId = "illagerinvasion_test"
    enableGameTests = false
    enableClientGameTests = true
    eula = true
}
