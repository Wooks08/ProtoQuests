package net.wookscode.protoquests.task;

import java.util.HashMap;

public class Task {
    private String name;
    private String type;

    public Task(String name, String type){
        this.name = name;
        this.type = type;
    }

    public String getName(){
        return this.name;
    };
    public HashMap<String, String> getProps(){
        HashMap<String, String> return_hashmap = new HashMap<>();
        return_hashmap.put("type", this.type);

        return return_hashmap;
    }
    public void setProgress(double new_amount){
        this.progress = new_amount;
    }

    public String asString(){
        return this.toString();
    };
}
