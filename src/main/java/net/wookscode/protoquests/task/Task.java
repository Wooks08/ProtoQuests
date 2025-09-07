package net.wookscode.protoquests.task;

import java.util.HashMap;

public class Task {
    private String name;
    private String type;
    private double progress;

    public Task(String name, String type, double progress){
        this.name = name;
        this.type = type;
        this.progress = progress;
    }

    public Task(String name, String type){
        this(name, type, 0.0);
    }


    public String getName(){
        return this.name;
    };
    public HashMap<String, Object> getProps(){
        HashMap<String, Object> return_hashmap = new HashMap<>();
        return_hashmap.put("name", this.name);
        return_hashmap.put("type", this.type);
        return_hashmap.put("progress", this.progress);

        return return_hashmap;
    }

    public void setProgress(double new_progress){
        this.progress = new_progress;
    }

    public String asString(){
        return this.toString();
    };
}
