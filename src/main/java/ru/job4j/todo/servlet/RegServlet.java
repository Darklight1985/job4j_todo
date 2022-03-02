package ru.job4j.todo.servlet;

import ru.job4j.todo.model.User;
import ru.job4j.todo.store.Store;
import ru.job4j.todo.store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.nonNull;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        if (nonNull(HbmStore.instOf().findUser(name).size() != 0)) {
            req.setAttribute("error", "Пользователь с данным паролем уже зарегистрирован");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        }
            User user = User.of(name, password);
            Store store = HbmStore.instOf();
            store.addUser(user);
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
}