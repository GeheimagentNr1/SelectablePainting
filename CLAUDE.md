# CLAUDE.md - Selectable Painting

## Projekt-Übersicht

**Selectable Painting** ist ein NeoForge Minecraft Mod.
- **Mod ID**: `selectable_painting`
- **Package**: `de.geheimagentnr1.selectable_painting`
- **Java Version**: 21
- **NeoForge Version**: je Branch, siehe Tabelle

Fügt das "Selectable Painting" hinzu, mit dem das zu platzierende Gemälde ausgewählt werden kann.

| Branch | MC | Range | NeoForge (kompiliert gegen) | Hinweis |
|---|---|---|---|---|
| `develop_1.21.1` | 1.21.1 | `[1.21.1,1.21.2)` | 21.1.x | Release `1.21.1-4.0.1` |
| `develop_1.21.2` | 1.21.2 - 1.21.4 | `[1.21.2,1.21.5)` | `21.2.1-beta` | Item-ID, Rezeptformat, `EntityRenderState`, `items/`-Definition für 1.21.4 |
| `develop_1.21.5` | 1.21.5 | `[1.21.5,1.21.6)` | `21.5.98` | `VariantUtils`, Migration alter Gemälde (`TileX/Y/Z` → `block_pos`, Inline-Motiv → ID über `asset_id`) |
| `develop_1.21.6` | 1.21.6 - 1.21.8 | `[1.21.6,1.21.9)` | `21.6.20-beta` | `ValueInput`/`ValueOutput`, `super.defineSynchedData` (Richtung wird synchronisiert), Textfarben mit Alpha, Paket über `ServerboundCustomPayloadPacket` |
| `develop_1.21.9` | 1.21.9 - 1.21.10 | `[1.21.9,1.21.11)` | `21.9.16-beta` | Renderer nach Vanilla `PaintingRenderer` (Submit, Painting-Atlas), `createDefaultStackConfig` |
| `develop_1.21.11` | 1.21.11 | `[1.21.11,1.21.12)` | `21.11.45` | `Identifier`, `decoration.painting`, `RenderTypes`, `renderContents`, `ENTITY_DROPS` |

Alle 1.21.2+-Jars: Version `4.0.1`, released 2026-10-02. Lokaler Branch `wip_1.21.2_first_attempt_base` sichert den ersten, unfertigen 1.21.2-Versuch; `develop_1.21.3` ist ein alter Forge-Stand. Details: [`../Docs/migrations/1.21.1-to-1.21.2.md`](../Docs/migrations/1.21.1-to-1.21.2.md) 4f.

**Offen:** Übersetzungen für die nach 1.21.1 hinzugekommenen Gemälde fehlen (siehe `MOD_KOMPATIBILITAET.md`).

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
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.8-hotspot"
./gradlew build
```

### Unit Tests (JUnit 5)

Für reine Logik-Tests ohne Minecraft-Abhängigkeiten:

```bash
./gradlew test
```

Tests liegen unter `src/test/java/`. Ergebnisse: `build/reports/tests/test/index.html`

### NeoForge GameTest Framework

Ab den 1.21.2+-Branches keine GameTests mehr (trivialer Smoke-Test samt CI-Job entfernt).

### Ingame-Test

Rezepte beidseitig, GUI (Titel, Größe, Bildname, Vorschau, Pfeile, Zufall), Tooltip, Gemälde setzen (Größe, zu groß, zufällig), Abbauen (Survival-Drop mit Daten, Creative ohne), Neustart. Bei Speicherformat-Änderungen eine Welt der Vorversion in den Testpack kopieren (Upgrade-Test).

### CI/CD (GitHub Actions)

Der Workflow `.github/workflows/build-and-test.yml` führt automatisch aus:
1. **Build**: Kompiliert den Mod
2. **Unit Tests**: Führt JUnit Tests aus

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

---

## Wissensdatenbank

Versionsübergreifende Migrations- und Entwicklungs-Erkenntnisse (Breaking Changes, Fixes, Testumgebungs-Patterns) werden zentral in [`../Docs/`](../Docs/) gepflegt. Bei neuen relevanten Erkenntnissen dort ergänzen, nicht nur hier.
