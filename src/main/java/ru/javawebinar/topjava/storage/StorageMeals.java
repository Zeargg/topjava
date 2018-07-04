package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageMeals implements Storage<Meal>{

    private Map<Integer, Meal> meals = Collections.synchronizedMap(new HashMap<>());
    private static StorageMeals storageMeals;
    public static AtomicInteger nextID = new AtomicInteger(0);

    private StorageMeals(){}

    public static StorageMeals getInstance(){
        if (storageMeals == null)
            storageMeals = new StorageMeals();
        return storageMeals;
    }

    @Override
    public void create(Meal meal){
        meals.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id){
        meals.remove(id);
    }

    @Override
    public void update(int id, Meal meal){
        meals.put(id ,meal);
    }

    @Override
    public Meal read(int id){
        return meals.get(id);
    }

    @Override
    public List<Meal> getList(){
        List<Meal> list = new ArrayList<>();
        for (Map.Entry pair : meals.entrySet())
            list.add((Meal)pair.getValue());
        return list;
    }
}
