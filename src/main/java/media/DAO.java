package media;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.List;

public class DAO{
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("HomeTasksServer");

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static boolean persist(Object obj) {
        boolean ret = true;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            entityManager.persist(obj);
            transaction.commit();
        }catch (Exception e){
            System.err.println(e.toString());
            ret = false;
        }
        return ret;
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

    private static Object getField(Field f, Object o){
        try {
            return f.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Object findFieldEntities(Object entity, String targetEntity){
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        Class<?> targetClass = DAO.findTargetClass(targetEntity);
        if (targetClass == null) return null;
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
        return null;
    }

    public static Object findEntity(String targetClassName, String primaryKey){
        Class<?> targetClass = DAO.findTargetClass(targetClassName);
        if (targetClass==null) return null;
        Field[] declaredFields = targetClass.getDeclaredFields();
        for(Field f :declaredFields){
            if(f.getAnnotation(Id.class) != null){
                Class<?> type = f.getType();
                /*Primary keys are just Integer or String*/
                if (type.equals(Integer.class)){
                    return DAO.find(targetClass,new Integer(primaryKey));
                }
                else {
                    return DAO.find(targetClass,primaryKey);
                }
            }
        }
        return null;
    }

    private static Class<?> findTargetClass(String targetClassName){
        try {
            return Class.forName(DAO.class.getPackage().getName() + "." + targetClassName);
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
