package ru.job4j.todo.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

public class PutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Collection<Item> list;
        if (req.getParameter("finalized").equals("true")) {
            list = HbmStore.instOf().findAll();
        } else {
            list = HbmStore.instOf().findNotFinal();
        }
        PrintWriter out = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(list);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String descr = req.getParameter("descrip");
        System.out.println(descr);
        List<Item> list = HbmStore.instOf().findByDescr(descr);
       for (Item item: list) {
           item.setDone(true);
           HbmStore.instOf().replace(item.getId(), item);
       }
    }
}
