package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.storage.StorageMeals;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredWithExceeded;
import static ru.javawebinar.topjava.util.MealsUtil.getMealsWithExceeded;

public class MealServlet extends HttpServlet {

    private static final Logger Log = getLogger(MealServlet.class);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    {
        List<Meal> mealList = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        Storage<Meal> mealStorage = StorageMeals.getInstance();
        mealList.forEach(mealStorage::create);
        List<MealWithExceed> meals = getFilteredWithExceeded(mealStorage.getList(), LocalTime.of(0, 0), LocalTime.of(23,0), 2000);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<MealWithExceed> meals = getMealsWithExceeded(StorageMeals.getInstance().getList(), 2000);
        request.setAttribute("listMeals", meals);
        request.setAttribute("formatter", formatter);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("removeId") != null)
            StorageMeals.getInstance().delete(Integer.parseInt(request.getParameter("removeId")));
        else {
            String time = request.getParameter("time");
            String desc = request.getParameter("description");
            String calories = request.getParameter("calories");
            time = time.replace('T', ' ');

            LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
            Meal meal = new Meal(dateTime, desc, Integer.parseInt(calories));

            StorageMeals.getInstance().create(meal);
        }
        doGet(request, response);
    }
}
