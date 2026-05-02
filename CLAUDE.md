# CLAUDE.md - Selectable Painting

## Projekt-Übersicht

**Selectable Painting** ist ein NeoForge Minecraft Mod für Minecraft 1.21.1.
- **Mod ID**: `selectable_painting`
- **Package**: `de.geheimagentnr1.selectable_painting`
- **Java Version**: 21
- **NeoForge Version**: 21.1.x

Fügt das "Selectable Painting" hinzu, mit dem das zu platzierende Gemälde ausgewählt werden kann.

## Abhängigkeiten

Keine Mod-Abhängigkeiten - eigenständiger Mod.

## Projektstruktur

```
src/main/java/de/geheimagentnr1/selectable_painting/
├── SelectablePaintingMod.java             # Haupt-Mod-Klasse
├── elements/
│   ├── creative_mod_tabs/                 # Creative-Tab Registration
│   │   ├── CreativeModeTabFactory.java
│   │   ├── ModCreativeModeTabsRegisterFactory.java
│   │   └── SelectablePaintingCreativeModeTabFactory.java
│   └── items/
│       └── ModItemsRegisterFactory.java   # Item-Registry
├── network/
│   ├── Network.java                       # Netzwerk-Handler
│   └── UpdateSelectablePaintingItemStackMsg.java  # Netzwerk-Paket
└── registry/
    ├── RegistryEntry.java                 # Registry-Utility
    └── RegistryKeys.java                  # Registry-Keys
```

## Architektur

Dieser Mod nutzt **nicht** `AbstractMod` aus ManyIdeas Core:
```java
@Mod( SelectablePaintingMod.MODID )
public class SelectablePaintingMod {
    public SelectablePaintingMod( @NotNull IEventBus modEventBus ) {
        ModItemsRegisterFactory modItemsRegisterFactory = new ModItemsRegisterFactory();
        modEventBus.register( modItemsRegisterFactory );
        modEventBus.register( new ModCreativeModeTabsRegisterFactory( modItemsRegisterFactory ) );
        modEventBus.register( Network.getInstance() );
    }
}
```

## Besonderheiten

- **Netzwerk-Kommunikation**: Client-Server Sync für Gemälde-Auswahl
- **Eigenes Registry-System**: Eigene `RegistryEntry` und `RegistryKeys` Klassen

## Code-Stil

- **Annotations**: `@NotNull` aus `org.jetbrains.annotations`
- **Lombok**: Projekt nutzt Lombok
- **Formatierung**: Leerzeichen nach `(` und vor `)` bei Methodenaufrufen

## Build & Test

```bash
./gradlew build
./gradlew runClient
./gradlew runServer
```

## Deployment

- **CurseForge**: `./gradlew curseforge`
- **Modrinth**: `./gradlew modrinth`

## Wichtige Hinweise

1. **Eigenständig**: Nutzt nicht das ManyIdeas Core Framework
2. **Netzwerk-Pakete**: `UpdateSelectablePaintingItemStackMsg` für Client-Server Kommunikation

## Testing

### Java-Versionen

Verschiedene Java-Versionen sind unter `C:\Program Files\Eclipse Adoptium` installiert. Für einen Gradle-Build muss die passende Java-Version gewählt werden:

```powershell
# Java 21 für MC 1.20.5+ (NeoForge)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot"
./gradlew build
```

### Unit Tests (JUnit 5)

Für reine Logik-Tests ohne Minecraft-Abhängigkeiten:

```bash
./gradlew test
```

Tests liegen unter `src/test/java/`. Ergebnisse: `build/reports/tests/test/index.html`

### NeoForge GameTest Framework

Für Integration Tests in einer echten Minecraft-Umgebung:

```bash
./gradlew runGameTestServer
```

GameTest-Klassen werden mit `@GameTestHolder` annotiert und liegen unter `src/main/java/.../elements/gametests/`.

### CI/CD (GitHub Actions)

Der Workflow `.github/workflows/build-and-test.yml` führt automatisch aus:
1. **Build**: Kompiliert den Mod
2. **Unit Tests**: Führt JUnit Tests aus
3. **GameTests**: Startet GameTestServer (optional)

### Was kann automatisiert getestet werden?

| Aspekt | Automatisiert? | Methode |
|--------|----------------|---------|
| Utility-Klassen | ✅ | JUnit |
| Config-Parsing | ✅ | JUnit |
| Commands | ✅ | GameTest |
| Block/Item-Verhalten | ✅ | GameTest |
| Multi-MC-Version | ⚠️ Pro Branch | CI Matrix |

## Referenzen

- [NeoForge Migration Primer](https://docs.neoforged.net/primer/docs/) — Dokumentiert API-Aenderungen zwischen Minecraft/NeoForge-Versionen; nuetzlich fuer die Pruefung von Breaking Changes beim Upgrade auf neue Versionen
