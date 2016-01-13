package ru.gurps.generator.desktop.singletons;

import ru.gurps.generator.desktop.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SpellTypeSingleton {
    private static SpellTypeSingleton ourInstance = new SpellTypeSingleton();

    private Set<Map.Entry<Integer, String>> list = setList();

    public static SpellTypeSingleton getInstance() {
        return ourInstance;
    }

    private SpellTypeSingleton() {
    }

    private Set<Map.Entry<Integer, String>> setList() {
        Map<Integer, String> hash = new HashMap<>();
        hash.put(0, Main.locale.getString("usual"));
        hash.put(1, Main.locale.getString("area"));
        hash.put(2, Main.locale.getString("throw"));
        hash.put(3, Main.locale.getString("contact"));
        hash.put(4, Main.locale.getString("block_spell"));
        hash.put(5, Main.locale.getString("resistance"));
        hash.put(6, Main.locale.getString("information"));
        hash.put(7, Main.locale.getString("charm"));
        hash.put(8, Main.locale.getString("special"));
        return hash.entrySet();
    }

    public Set<Map.Entry<Integer, String>> getList() {
        return list;
    }
}
