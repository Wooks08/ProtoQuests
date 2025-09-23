package net.wookscode.protoquests.task;

import java.util.HashMap;

public class Task {
    private String name;
    private String type;
    private double progress;
    private int actions_completed;
    private int actions_to_complete;
    private boolean finished;

    public Task(String name, String type, int actions_to_complete, int actions_completed){
        this.name = name;
        this.type = type;
        this.progress = (double) actions_completed /actions_to_complete * 100;
        this.actions_completed = actions_completed;
        this.actions_to_complete = actions_to_complete;
    }

    public Task(String name, String type){
        this(name, type, 0, 0);
    }


    public String getName(){
        return this.name;
    };
    public HashMap<String, Object> getProps(){
        HashMap<String, Object> return_hashmap = new HashMap<>();
        return_hashmap.put("name", this.name);
        return_hashmap.put("type", this.type);
        return_hashmap.put("progress", this.progress);
        return_hashmap.put("finished", this.finished);
        return_hashmap.put("actions_completed", this.actions_completed);
        return_hashmap.put("actions_to_complete", this.actions_to_complete);

        return return_hashmap;
    }

    public void setProgress(double new_progress){
        if(this.progress < 100) {
            this.progress = new_progress;
        }
        if(this.progress >= 100){
            this.finished = true;
        }
    }

    public String asString(){
        return this.toString();
    };
}
