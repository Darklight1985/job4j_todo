package ru.job4j.todo.store;

import  org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
    public Item add(Item item, String[] ids) {
     return this.tx(session -> {
     for (String id: ids) {
         item.addCategory(session.find(Category.class, Integer.parseInt(id)));
     }
     session.save(item);
     return item;
     });
    }

    @Override
    public User addUser(User user) {
        return (User) this.tx(session -> session.save(user));
    }

    @Override
    public boolean replace(int id, Item item) {
       return this.tx(session -> {
   session.update(item);
   return true;
       });
    }

    @Override
    public boolean delete(int id) {
        Item item = new Item();
        item.setId(id);
        this.tx(session -> {
                    session.delete(item);
                    return true;
                }
        );
        return true;
    }

    @Override
    public User findUser(String nameUser) {
        return (User) this.tx(session -> {
            final Query query = session.createQuery("from User where name = :value");
            query.setParameter("value", nameUser);
            return query.uniqueResult();
        });
    }

    @Override
    public List<Item> findAll() {
        return this.tx(session -> {
            final Query query = session.createQuery("from " + Item.class.getName(), Item.class);
            return query.getResultList();
        });
    }

    @Override
    public List<Item> findNotFinal() {
        return this.tx(session -> {
            final Query query = session.createQuery("from Item where done = :value");
            query.setParameter("value", false);
            List<Item> result = (List<Item>) query.getResultList();
            return result;
        });
    }

    @Override
    public List<Item> findByDescr(String descr) {
        return this.tx(session -> {
            final Query query = session.createQuery("from Item where description = :value");
            query.setParameter("value", descr);
            List<Item> result = (List<Item>) query.getResultList();
            return result;
    });
    }

    public List<Category> allCategory() {
        List<Category> result = new ArrayList<>();
        return this.tx(session -> {
            return session.createQuery("select c from Category c", Category.class).list();
        });
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
