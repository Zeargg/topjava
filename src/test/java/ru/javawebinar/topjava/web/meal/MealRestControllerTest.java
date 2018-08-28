package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.contentJson;
import static ru.javawebinar.topjava.util.MealsUtil.getWithExceeded;
import static ru.javawebinar.topjava.web.json.JacksonObjectMapper.getMapper;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Test
    void testGet() throws Exception{
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getMapper().writeValueAsString(MEAL1)));
    }

    @Test
    void testDelete() throws Exception{
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(USER_ID), MEAL2, MEAL3, MEAL4, MEAL5, MEAL6);
    }

    @Test
    void testUpdate() throws Exception{
        Meal updated = getUpdated();
        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getMapper().writeValueAsBytes(updated)))
                .andExpect(status().isOk());
        assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getMapper().writeValueAsString(Arrays.asList(MEAL1, MEAL2, MEAL3, MEAL4, MEAL5, MEAL6))));
    }


    @Test
    void testGetBetween() throws Exception {
        mockMvc.perform(post(REST_URL + "timeFilter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(LocalDate.MIN))
                .content(JsonUtil.writeValue(LocalTime.MIN))
                .content(JsonUtil.writeValue(LocalDate.MAX))
                .content(JsonUtil.writeValue(LocalTime.MIN)))
                .andExpect(status().isCreated());
        List<Meal> meals = mealService.getBetweenDateTimes(LocalDateTime.MIN, LocalDateTime.MAX, USER_ID);
        assertMatch(meals, MEAL1, MEAL2, MEAL3, MEAL4, MEAL5, MEAL6);
    }
}