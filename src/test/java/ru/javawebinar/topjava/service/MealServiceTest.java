package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;



@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/addMealsDb.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static{
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void create(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy HH:mm");
        Meal newMeal = new Meal(LocalDateTime.parse("12.12.2006 15:00", formatter), "dinner", 100);
        Meal created = mealService.create(newMeal, 100000);
        newMeal.setId(created.getId());
        assertMatch(mealService.getAll(100000), m1, m2, m3, newMeal);
    }

    @Test
    public void delete() {
        mealService.delete(1, 100000);
        assertMatch(mealService.getAll(100000), m2, m3);
    }

    @Test(expected = NotFoundException.class)
    public void deleteWrongId() {
        mealService.delete(1, 100003);
    }

    @Test
    public void get(){
        Meal meal = mealService.get(1, 100000);
        assertMatch(meal, m1);
    }

    @Test(expected = NotFoundException.class)
    public void getWrongId(){
        mealService.get(1, 100002);
    }

    @Test
    public void update(){
        Meal updated = new Meal(m1);
        updated.setDescription("updated");
        updated.setCalories(0);
        mealService.update(updated, 100000);
        assertMatch(mealService.get(updated.getId(), 100000), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateWrongId(){
        Meal updated = new Meal(m1);
        updated.setDescription("updated");
        updated.setCalories(0);
        mealService.update(updated, 100003);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> all = mealService.getAll(100000);
        assertMatch(all, m1, m2, m3);
    }
}