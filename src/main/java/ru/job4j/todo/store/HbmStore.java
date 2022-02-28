package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Item;

import java.util.ArrayList;
import java.util.List;

public class HbmStore implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private HbmStore() {
    }

    private static final class Lazy {
        private static final Store INST = new HbmStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Override
    public Item add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item item = new Item(null);
        item.setId(id);
        session.delete(item);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public List<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Item> result = session.createQuery("from ru.job4j.todo.model.Item").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public List<Item> findNotFinal() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Item> result = new ArrayList<>();
        List<Item> list = session.createQuery("from ru.job4j.todo.model.Item").list();
        for (Item item: list) {
            if (!item.isDone()) {
                result.add(item);
            }
        }
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public List<Item> findByDescr(String descr) {
        Session session = sf.openSession();
        List result = new ArrayList();
        session.beginTransaction();
        List<Item> list = session.createQuery("from ru.job4j.todo.model.Item")
                .list();
        for (Item item: list) {
            if (item.getDescription().equals(descr)) {
                result.add(item);
            }
        }
        session.getTransaction().commit();
        session.close();
        return result;
    }
}
