package net.wookscode.protoquests.task;

public class Task {
    private String name;

    public Task(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    };

    public String asString(){
        return this.toString();
    };
}
