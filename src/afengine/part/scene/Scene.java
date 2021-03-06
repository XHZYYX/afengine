/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package afengine.part.scene;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class Scene{
    public final static Map<String,Actor> staticActorNodeMap=new HashMap<>();
    
    private static class AdapterLoader extends AbSceneLoader{
        @Override
        public void load() {
            System.out.println("Load Scene:"+this.getThisScene().name);
        }
        @Override
        public void shutdown() {
            System.out.println("Shutdown Scene:"+this.getThisScene().name);
        }
        @Override
        public void pause() {
            System.out.println("Pause Scene:"+this.getThisScene().name);
        }
        @Override
        public void resume() {
            System.out.println("Resume Scene:"+this.getThisScene().name);            
        }        
    };
    
    
    public long id;
    private String name;
    public final Map<String,Actor> nodeActorMap=new HashMap<>();
    private AbSceneLoader loader;
    private boolean shouldoutput;
    
    public Scene(long id,String name,AbSceneLoader loader){
        this.id=id;
        this.name=name;
        this.loader=loader;
        this.loader.setThisScene(this);
        shouldoutput=false;
    }
    public Scene(long id,String name){
        this(id,name,new AdapterLoader());
    }
    
    public static Map<String, Actor> getStaticActorNodeMap() {
        return staticActorNodeMap;
    }
    public static Actor getStaticActorNode(String nodeName){
        return staticActorNodeMap.get(nodeName);
    }
    public static boolean hasStaticActorNode(String nodeName){
        return staticActorNodeMap.containsKey(nodeName);
    }
    public static void removeStaticActorNode(String nodeName){
        staticActorNodeMap.remove(nodeName);
    }
    public AbSceneLoader getLoader() {
        return loader;
    }
    public void setLoader(AbSceneLoader loader){
        this.loader=loader;
    }
    public final Map<String, Actor> getNodeActorMap() {
        return nodeActorMap;
    }
    public final Actor findActorByID(long id){
        
        Iterator<Actor> actoriter = Scene.staticActorNodeMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            Actor dest = findActor(id,actor);
            if(dest!=null)
                return dest;
        }        
        
        actoriter = nodeActorMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            Actor dest = findActor(id,actor);
            if(dest!=null)
                return dest;
        }                
        
        return null;
    }

    public boolean isShouldoutput() {
        return shouldoutput;
    }

    public void setShouldoutput(boolean shouldoutput) {
        this.shouldoutput = shouldoutput;
    }

    
    private Actor findActor(long id,Actor actor){
        if(actor.id==id)
            return actor;
        
        Deque<Actor> actordeque = new ArrayDeque<>();
        actordeque.addFirst(actor);
        Iterator<Actor> childiter;
        while(!actordeque.isEmpty()){
            Actor act = actordeque.pollLast();
            if(act.id==id)
                return act;

            childiter= act.getChildren().iterator();
            while(childiter.hasNext()){
                Actor acto = childiter.next();
                actordeque.addFirst(acto);
            }
        }
        
        return null;
    }
    
    public final void addNodeActor(String nodeName,Actor actor){
        nodeActorMap.put(nodeName, actor);
    }
    public final boolean hasNodeActor(String nodeName){
        return nodeActorMap.containsKey(nodeName);
    }
    public final void removeNodeActor(String nodeName){
        nodeActorMap.remove(nodeName);
    }        
    public String getName() {
        return name;
    }        
    public void setName(String name) {
        this.name = name;
    }       
    public void updateScene(long time){
        Iterator<Actor> staticActoriter = Scene.staticActorNodeMap.values().iterator();
        while(staticActoriter.hasNext()){
            Actor actor = staticActoriter.next();
            actor.updateActor(time);
        }
        
        Iterator<Actor> actoriter = nodeActorMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            actor.updateActor(time);
        }
    }
}
