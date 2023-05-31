package vn.hcmute.seekyourshoes.DAO;

import vn.hcmute.seekyourshoes.connection.DBConnection;
import vn.hcmute.seekyourshoes.model.Brand;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class BrandDAO {
    private static BrandDAO instance;
    private BrandDAO(){}
    public static BrandDAO getInstance() throws Exception {
        if (instance == null) {
            instance = new BrandDAO();
        }
        return instance;
    }

    public Brand getBrandByName (String brandName) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT b FROM Brand b WHERE b.brandName = :brandName";
            TypedQuery<Brand> query = em.createQuery(jpql, Brand.class);
            query.setParameter("brandName", brandName);
            Brand brand = query.getSingleResult();
            return brand;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            em.close();
        }
    }
}
