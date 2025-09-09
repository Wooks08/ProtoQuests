package net.wookscode.protoquests.result;

import java.util.HashMap;

public class Result {
    private String name;
    private String type;
    private boolean received;

    public Result(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName(){
        return this.name;
    }

    public HashMap<String, Object> getProps(){
        HashMap<String, Object> return_hashmap = new HashMap<>();
        return_hashmap.put("name", this.name);
        return_hashmap.put("type", this.type);
        return_hashmap.put("received", this.received);

        return return_hashmap;
    }

    public void setToReceived(){
        if(!this.received){
            this.received = false;
        }
    }

    public String asString(){
        return this.toString();
    };
}
