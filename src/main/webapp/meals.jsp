<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
    <table border="1" align="center">
        <th>Time</th>
        <th>Description</th>
        <th>Calories</th>

        <c:forEach var="meal" items="${listMeals}">
                <tr style="color: ${meal.exceed == 'true' ? "red" : "green"}">
                    <td>${meal.dateTime.format(formatter)}</td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td>
                        <form action="meals" method="post">
                            <input type="hidden" name="removeId" value="${meal.id}"/>
                            <input type="submit" value="delete"/>
                        </form>
                    </td>
                </tr>
        </c:forEach>
    </table>
    <table align="center">
        <form action="meals" method="post">
            <tr>
                <h2 style="text-align: center">Add meal</h2>
            </tr>
            <tr>
                <td>Time:</td>
                <td><input type="datetime-local" name="time"/></td>
            </tr>
            <tr>
                <td>Description:</td>
                <td><input type="text" name="description"/></td>
            </tr>
            <tr>
                <td>Calories:</td>
                <td><input type="number" name="calories"/></td>
            </tr>
            <tr>
                <td><input type="submit" value="Add"/></td>
            </tr>
        </form>
    </table>
</body>
</html>