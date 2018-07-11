package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {

    private final MealService service;

    @Autowired
    public MealRestController(MealService service){
        this.service = service;
    }
    public List<Meal> getAll() {
        return service.getAll();
    }

    public Meal get(int id) {
        return service.get(id);
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        return service.create(meal);
    }

    public void delete(int id) {
        service.delete(id);
    }

    public void update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        service.update(meal);
    }

    public List<MealWithExceed> getAllMealWithExceed(){
        List<Meal> list = getAll();
        Map<LocalDate, Integer> caloriesSumByDate = list.stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));

        return list.stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), LocalTime.MIN, LocalTime.MAX))
                .map(meal -> new MealWithExceed(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesSumByDate.get(meal.getDate()) > MealsUtil.DEFAULT_CALORIES_PER_DAY))
                .collect(toList());
    }

    public List<MealWithExceed> getFilterMealWithExceed(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate){
        List<MealWithExceed> list = getAllMealWithExceed();
        list = list.stream()
                .filter(m -> DateTimeUtil.isBetweenDate(m.getDateTime().toLocalDate(), startDate, endDate))
                .collect(Collectors.toList());

        list = list.stream()
                .filter(m -> DateTimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());
        return list;
    }

}