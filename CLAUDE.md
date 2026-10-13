# CLAUDE.md - Selectable Painting

Selectable Painting fügt das Item „Selectable Painting“ hinzu: Rechtsklick in die Luft öffnet eine GUI, in der Größe und Motiv (oder „zufällig“) gewählt werden; an der Wand platziert es eine eigene Gemälde-Entity mit genau diesem Motiv. Mod-ID `selectable_painting`, Package `de.geheimagentnr1.selectable_painting`. **Client+Server** (Item, Entity, Menü und Netzwerkpaket müssen auf beiden Seiten vorhanden sein), keine Dist-Beschränkung (`@Mod( MODID )`).

> Diese Datei ist auf allen `develop_*`-Branches identisch; Quelle: `../Docs/mod-claude/SelectablePainting.md` (Änderungen dort, dann `Docs/tools/sync_mod_claude_md.sh`). Versionsstand: [`../Docs/VERSIONEN.md`](../Docs/VERSIONEN.md).

## Versionen

- Branches, Ranges und Tags: Zeilen „SelectablePainting“ in [`../Docs/VERSIONEN.md`](../Docs/VERSIONEN.md).
- NeoForge: acht Jars - `develop_1.21.1`, `develop_1.21.2` (1.21.2 - 1.21.4), `develop_1.21.5`, `develop_1.21.6` (1.21.6 - 1.21.8), `develop_1.21.9` (1.21.9 - 1.21.10), `develop_1.21.11`, `develop_26.1` (26.1 - 26.1.2), `develop_26.2` (26.2 - 26.3). Forge: `develop_<mc>` bis 1.21.3 und `develop_1.21.1_forge`.
- Gründe für die vielen Jars: eigene Hanging-Entity (Speicherformat 1.21.5/1.21.6), Renderer (1.21.9, Licht-API 26.2), Paketumzug (1.21.11), GUI (26.1). Details: [`../Docs/migrations/1.21.1-to-1.21.2.md`](../Docs/migrations/1.21.1-to-1.21.2.md) 4f, [`../Docs/migrations/1.21.11-to-26.1.md`](../Docs/migrations/1.21.11-to-26.1.md).

## Abhängigkeiten

- Keine Mod-Abhängigkeiten, keine gebündelten Bibliotheken. Forge ab 1.20: MinecraftForgeAPI (JarInJar, siehe [`../Docs/forge/README.md`](../Docs/forge/README.md)).

## Architektur

- `SelectablePaintingMod` - Hauptklasse ohne `AbstractMod`: registriert `ModItemsRegisterFactory`, `ModCreativeModeTabsRegisterFactory` und `Network` auf dem Mod-Bus.
- `elements/items/ModItemsRegisterFactory` - `RegisterEvent` für Item, `MenuType`, `EntityType` und drei Data-Components (`size`, `painting`, `random`); Client: Entity-Renderer und Menü-Screen.
- `elements/items/selectable_painting/` - `SelectablePainting` (Item: `use` öffnet das Menü, `useOn` platziert die Entity, Tooltip), `SelectablePaintingEntity` (Kopie von Vanilla-`Painting`, erbt von `HangingEntity`), `SelectablePaintingRenderer`, `PaintingSelectionHelper` (Motive aus der `PAINTING_VARIANT`-Registry nach Größe gruppiert, Namen), `SelectablePaintingItemStackHelper` (Auswahl am Stack lesen/schreiben), `screen/` (Menü, Screen, Pfeil-Buttons).
- `network/UpdateSelectablePaintingItemStackMsg` - C2S-Paket mit dem geänderten Stack, der Server übernimmt es nur bei offenem `SelectablePaintingMenu`.
- `registry/` - eigene `RegistryEntry`/`RegistryKeys`; `elements/creative_mod_tabs/` - eigener Creative-Tab.
- Item-Daten: bis Forge 1.20.4 NBT `selected_painting` (`size`, `painting`, `random`), ab 1.20.6 Data-Components. Forge bis 1.19.4: statische `handlers/ModEventHandler`, ältere Branches mit Item-Groups und eigenem Spawn-Paket.

## Funktionen, Befehle, Konfiguration

- Keine Befehle, keine Config.
- Rezepte (formlos): Gemälde → Selectable Painting und zurück.
- GUI: Größe und Motiv per Pfeilen, Zufall-Checkbox, Vorschau; Gemäldenamen aus den Vanilla-Titeln, Fallback ein lesbarer Name aus der ID; lange Namen laufen als Lauftext (beides seit 4.0.2).
- Abbauen: im Survival droppt das Item mit der gewählten Auswahl, im Creative nichts. Die Entity speichert Motiv (Registry-ID) und Position.

## Fallstricke

- **Speicherformat der Entity**: ab 1.21.5 liest `BlockAttachedEntity` nur `block_pos` - alte `TileX/Y/Z` werden in `readAdditionalSaveData` migriert (sonst fallen Gemälde ab); vor 1.21.5 lag das Motiv inline statt als ID im Save, Migration über `asset_id`. Bei jeder Änderung Upgrade-Test mit alter Welt.
- **Synched Data**: ab 1.21.6 synchronisiert `HangingEntity` die Richtung - in `defineSynchedData`/`onSyncedDataUpdated` immer `super` aufrufen (sonst „has not defined synched data value 8“).
- **GUI-Farben**: ab 1.21.6 wertet `drawString` Alpha aus - Farben immer mit Alpha (`0xFFFFFFFF`, `0xFF404040`).
- **Scissor/Lauftext**: `enableScissor` rechnet die Pose je Version anders ein (1.21.1 - 1.21.3 gar nicht, 1.21.4/1.21.5 `pose.last().pose()`, ab 1.21.6 `transformAxisAligned`). Scissor und Text deshalb in Bildschirmkoordinaten nach `super.render(..)` zeichnen (26.x: in `extractContents` nach `super`), nicht in `renderLabels`.
- **Netzwerk**: `PacketDistributor.sendToServer` fehlt in NeoForge 21.6 - Paket über `connection.send( new ServerboundCustomPayloadPacket( payload ) )`.
- **Renderer**: ab 1.21.9 nach Vanilla-`PaintingRenderer` (Submit, Painting-Atlas), 26.2 Licht über `LightCoordsUtil`, 26.3 `last().rotate(..)` statt `mulPose`. 1.21.11: Painting-Klassen nach `decoration.painting`, `renderContents` statt `renderWidget`.
- **Gemäldenamen**: Titel der `PaintingVariant` (ab 1.21.2), sonst `translatableWithFallback( painting.<ns>.<name>.title, lesbarer Name )` - keine eigenen Übersetzungen pro Gemälde pflegen.

## Testen

- Allgemeines Vorgehen: [`../Docs/testing/README.md`](../Docs/testing/README.md); Jar in Server-Testpack **und** Client-Instanz, Jars nie bei laufendem Client tauschen.
- Ingame: Rezepte in beide Richtungen, GUI (Titel, Größe, Name inkl. Lauftext, Vorschau, Pfeile, Zufall), Tooltip, Platzieren (Größe passt / zu groß / zufällig), Abbauen (Survival-Drop mit Daten, Creative ohne), Neustart.
- Speicherformat-Änderungen: Welt der Vorversion in den Testpack kopieren (Upgrade-Kette, siehe 4f). Ab 1.21.9 lädt der Server keine Spawn-Chunks - Entities per RCON erst abfragbar, wenn ein Spieler in der Nähe ist.
- Ab den 1.21.2+-Branches keine GameTests; CI: Build + JUnit.

## Links

- Wiki: https://github.com/GeheimagentNr1/SelectablePainting/wiki
- CurseForge: https://www.curseforge.com/minecraft/mc-mods/selectable-painting (Projekt-ID 388638)
- Modrinth: https://modrinth.com/mod/NBJXn70b
- [`../Docs/migrations/BREAKING_CHANGES.md`](../Docs/migrations/BREAKING_CHANGES.md), [`../Docs/releasing/release-process.md`](../Docs/releasing/release-process.md)
