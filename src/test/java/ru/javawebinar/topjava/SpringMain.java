package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.repository.mock.InMemoryUserRepositoryImpl;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            InMemoryUserRepositoryImpl userRepository = appCtx.getBean(InMemoryUserRepositoryImpl.class);
            userRepository.save(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));
            System.out.println();

            InMemoryMealRepositoryImpl mealRepository = appCtx.getBean(InMemoryMealRepositoryImpl.class);
            List<Meal> filteredMealsWithExceeded =
                    mealRepository.getBetween(
                            LocalDateTime.of(2015, Month.MAY, 30,7, 0),
                            LocalDateTime.of(2015, Month.MAY, 31,11, 0), 100000);
            filteredMealsWithExceeded.forEach(System.out::println);
        }
    }
}
