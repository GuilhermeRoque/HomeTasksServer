package media;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.List;

public class JPAUtils {
    public static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("HomeTasksDB");

    public static boolean persist(Object obj) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(obj);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            System.err.println(e.toString());
            return false;
        }finally {
            entityManager.close();
        }
        return true;
    }

    public static boolean update(Object obj) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(obj);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            System.err.println(e.toString());
            return false;
        }finally {
            entityManager.close();
        }
        return true;
    }


    public static <T> T find(Class<T> entityClass, Object primaryKey) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        T t = null;
        try {
            t = entityManager.find(entityClass, primaryKey);
        }catch (Exception e){
            System.err.println(e.toString());
        }
        return t;
    }

    public static Object find(String stringClass, String primaryKey){
        Class<?> targetClass = JPAUtils.findTargetClass(stringClass);
        if (targetClass != null){
            Field[] declaredFields = targetClass.getDeclaredFields();
            for(Field f :declaredFields){
                if(f.getAnnotation(Id.class) != null){
                    Class<?> type = f.getType();
                    /*Primary keys are just Integer or String*/
                    if (type.equals(Integer.class)){
                        return JPAUtils.find(targetClass,new Integer(primaryKey));
                    }
                    else {
                        return JPAUtils.find(targetClass,primaryKey);
                    }
                }
            }
        }
        return null;
    }

    private static Object getField(Field f, Object o){
        try {
            return f.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Object findFieldEntities(Object entity, String stringClass){
        Class<?> targetClass = JPAUtils.findTargetClass(stringClass);
        if (targetClass !=null){
            Field[] declaredFields = entity.getClass().getDeclaredFields();
            for(Field f :declaredFields){
                OneToMany oneToMany = f.getAnnotation(OneToMany.class);
                ManyToMany manyToMany = f.getAnnotation(ManyToMany.class);
                if(oneToMany != null){
                    Class<?> fieldClass = oneToMany.targetEntity();
                    if (targetClass.equals(fieldClass)){
                        return getField(f,entity);
                    }
                }
                else if(manyToMany != null){
                    Class<?> fieldClass = manyToMany.targetEntity();
                    if (targetClass.equals(fieldClass)){
                        return getField(f,entity);
                    }
                }
            }
        }
        return null;
    }


    public static Object getEntityFieldValue(Object entity, String fieldName){
        if (entity != null) {
            try {
                return entity.getClass().getDeclaredField(fieldName).get(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean setEntityFieldValue(Object entity, String fieldName, Object value){
        if (entity != null) {
            try {
                entity.getClass().getDeclaredField(fieldName).set(entity,value);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Class<?> findTargetClass(String targetClassName){
        try {
            return Class.forName(JPAUtils.class.getPackage().getName() + "." + targetClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> List<T> findAll(Class<T> entityClass) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> from = query.from(entityClass);
        CriteriaQuery<T> select = query.select(from);
        TypedQuery<T> typedQuery = entityManager.createQuery(select);
        return typedQuery.getResultList();
    }



}
