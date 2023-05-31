package vn.hcmute.seekyourshoes.DAO;

import vn.hcmute.seekyourshoes.connection.DBConnection;
import vn.hcmute.seekyourshoes.model.Category;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class CategoryDAO {
    private static CategoryDAO instance;
    private CategoryDAO(){}
    public static CategoryDAO getInstance() throws Exception {
        if (instance == null) {
            instance = new CategoryDAO();
        }
        return instance;
    }

    public Category getCategoryByName (String catName) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT c FROM Category c WHERE c.catName = :catName";
            TypedQuery<Category> query = em.createQuery(jpql, Category.class);
            query.setParameter("catName", catName);
            Category category = query.getSingleResult();
            return category;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
}
