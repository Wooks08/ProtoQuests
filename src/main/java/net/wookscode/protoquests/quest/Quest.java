package net.wookscode.protoquests.quest;

import net.minecraft.nbt.NbtCompound;
import net.wookscode.protoquests.result.GiveResult;
import net.wookscode.protoquests.result.Result;
import net.wookscode.protoquests.task.BreakTask;
import net.wookscode.protoquests.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Quest {
    private String name;
    private List<Task> tasks;
    private List<Result> results;

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

    public List<Result> getResults(){
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

        List<String> results_as_list_of_strings = new ArrayList<>();
        for(Result result: results){
            results_as_list_of_strings.add(result.asString());
        }
        nbt.putString("results", results_as_list_of_strings.toString());

        return nbt;
    }

    public static Quest fromNbt(NbtCompound nbt) {
        Quest quest = new Quest(nbt.getString("name"));

        for(String task: elementsInStringToList(nbt.getString("tasks"))) {
            List<String> list_with_task = listInStringToList(task);
            String type = list_with_task.get(list_with_task.size() - 1).strip();
            switch (type) {
                case "break":
                    quest.tasks.add(BreakTask.fromString(task));
            }
        }

        for(String result: elementsInStringToList(nbt.getString("results"))){
            List<String> list_with_result = listInStringToList(result);
            String type = list_with_result.get(list_with_result.size() - 1).strip();

//            TODO: Finish recovering data from nbt
//            switch (type){
//                case "give":
//                    quest.results.add(GiveResult.fromString(result));
//                case "xp":
//                    ;
//                case "command":
//                    ;
//            }
        }

        return quest;
    }

    private static List<String> elementsInStringToList(String string_of_elements){
        /*
        * Structure of the input string should look as follows:
        *
        * "[[element1, element2], [element3, element4], ...]"
         */

        StringBuilder sb1 = new StringBuilder(string_of_elements);

        sb1.deleteCharAt(string_of_elements.length() - 1);

        string_of_elements = sb1.toString();

        List<String> list_of_tasks = new ArrayList<>(List.of(string_of_elements.split("]")));

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
