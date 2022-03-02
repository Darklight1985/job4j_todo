package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.List;

public interface Store {
    Item add(Item item);

    public User addUser(User user);

    boolean replace(int id, Item item);

    boolean delete(int id);

    public List<User> findUser(String name);

    List<Item> findAll();

    List<Item> findNotFinal();

    public List<Item> findByDescr(String descr);
}