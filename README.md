# Illager Invasion

A Minecraft mod. Downloads can be found on [CurseForge](https://www.curseforge.com/members/fuzs_/projects) and [Modrinth](https://modrinth.com/user/Fuzs).

![](banner.png)

## Minecraft 26.3 fork

Branch `26.3` ports the `26.2.x` codebase to Minecraft Java Edition 26.3 for Fabric and NeoForge.
Build with JDK 25:

```sh
./gradlew build
```

The mod jars are written to `Fabric/build/libs/` and `NeoForge/build/libs/`.
Install the jar for your loader together with Puzzles Lib 26.3.12 or newer.
Fabric also requires Fabric Loader 0.19.5 or newer, Fabric API 0.163.0 or newer for 26.3, and Forge Config API Port for 26.3.
MultiLoader Data Extensions is embedded in both jars.

Regenerate resources with `./gradlew :NeoForge:runData`.
Run the client integration test with `./gradlew :Fabric:runClientGameTest`; it checks mob rendering,
the imbuing screen, berserking brewing, and resource reload. The client test requires a working graphics environment.
On Linux with Mesa, it can also run without a display:

```sh
SDL_VIDEODRIVER=offscreen SDL_AUDIODRIVER=dummy LIBGL_ALWAYS_SOFTWARE=1 ./gradlew :Fabric:runClientGameTest
```
