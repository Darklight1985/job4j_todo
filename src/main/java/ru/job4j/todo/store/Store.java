package ru.job4j.todo.store;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.List;

public interface Store {
    Item add(Item item, String[] ids);

    public User addUser(User user);

    boolean replace(int id, Item item);

    boolean delete(int id);

    public User findUser(String name);

    List<Item> findAll();

    List<Item> findNotFinal();

    public List<Category> allCategory();

    public List<Item> findByDescr(String descr);
}