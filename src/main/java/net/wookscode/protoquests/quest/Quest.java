package net.wookscode.protoquests.quest;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.wookscode.protoquests.task.BreakTask;
import net.wookscode.protoquests.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Quest {
    private String name;
    private List<Task> tasks;
    private List<String> results;

    public Quest(String name){
        this.name = name;
        this.tasks = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public HashMap<String, List> getProps(){
        HashMap<String, List> props = new HashMap<>();
        props.put("name", nameToList());
        props.put("tasks", tasks);
        props.put("results", results);

        return props;
    }

    public String getName(){
        return name;
    }

    public List<Task> getTasks(){
        return tasks;
    }

    public List<String> getResults(){
        return results;
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        nbt.putString("name", name);

        List<String> tasks_as_list_of_strings = new ArrayList<>();
        for(Task task : tasks){
            tasks_as_list_of_strings.add(task.asString());
        }
        nbt.putString("tasks",tasks_as_list_of_strings.toString());

        nbt.putString("results", results.toString());

        return nbt;
    }

    public static Quest fromNbt(NbtCompound nbt) {
        Quest quest = new Quest(nbt.getString("name"));

        for(String task: tasksInStringToList(nbt.getString("tasks"))) {
            List<String> type_list = listInStringToList(task);
            String type = type_list.get(type_list.size() - 1).strip();
            switch (type) {
                case "break":
                    quest.tasks.add(BreakTask.fromString(task));
            }
        }
        quest.results = listInStringToList(nbt.getString("results"));

        return quest;
    }

    private static List<String> tasksInStringToList(String string){
        StringBuilder sb1 = new StringBuilder(string);

        sb1.deleteCharAt(string.length() - 1);

        string = sb1.toString();

        List<String> list_of_tasks = new ArrayList<>(List.of(string.split("]")));

        for (String task : list_of_tasks){
            StringBuilder sb2 = new StringBuilder(task);

            sb2.deleteCharAt(0);
            sb2.append(']');

             list_of_tasks.set(list_of_tasks.indexOf(task), sb2.toString());
        }

        return list_of_tasks;
    }

    private static List<String> listInStringToList(String string) {

        StringBuilder sb = new StringBuilder(string);

        sb.deleteCharAt(string.length() - 1);
        sb.deleteCharAt(0);

        string = sb.toString();

        return List.of(string.split(","));
    }

    private List<String> nameToList(){
        List<String> name_list = new ArrayList<>();
        name_list.add(this.name);
        return name_list;
    }
}
