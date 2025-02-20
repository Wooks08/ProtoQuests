package net.wookscode.protoquests.util;

import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Quest {
    private String name;
    private List<String> tasks;
    private List<String> results;

    public Quest(String name){
        this.name = name;
        this.tasks = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public HashMap<String, List<String>> getProps(){
        HashMap<String, List<String>> props = new HashMap<>();
        props.put("name", nameToList());
        props.put("tasks", tasks);
        props.put("results", results);

        return props;
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        nbt.putString("name", name);
        nbt.putString("tasks", listToString(tasks));
        nbt.putString("results", listToString(results));

        return nbt;
    }

    public static Quest fromNbt(NbtCompound nbt) {
        Quest quest = new Quest(nbt.getString("name"));

        quest.tasks = stringToList(nbt.getString("tasks"));
        quest.results = stringToList(nbt.getString("results"));

        return quest;
    }

    private String listToString(List<String> list){
        return list.toString();
    }

    private static List<String> stringToList(String string) {

        StringBuilder sb = new StringBuilder(string);

        sb.deleteCharAt(string.length() - 1);
        sb.deleteCharAt(0);

        string = sb.toString();

        return List.of(string.split(","));
    }

    private List<String> nameToList(){
        List<String> nameList = new ArrayList<>();
        nameList.add(this.name);
        return nameList;
    }
}
