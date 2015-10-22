package ru.gurps.generator.desktop.lib.export.pojo;

import javafx.collections.ObservableList;
import ru.gurps.generator.desktop.models.characters.CharactersAddon;
import ru.gurps.generator.desktop.models.characters.CharactersFeature;

public class JsonCharacterFeature {
    public CharactersFeature charactersFeature;
    public ObservableList<CharactersAddon> charactersAddons;

    public JsonCharacterFeature() {
    }
}
