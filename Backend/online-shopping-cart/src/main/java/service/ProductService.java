package service;

import java.util.List;

import dao.ProductDAO;
import model.Product;

public class ProductService {

    private ProductDAO productDAO = new ProductDAO();

    public boolean addProduct(Product product){
        return productDAO.save(product);
    }

    public List<Product> getAllProducts(){
        return productDAO.findAll();
    }

    public Product getProductById(Long id){
        return productDAO.findById(id);
    }

    public boolean updateProduct(Product product){
        return productDAO.update(product);
    }

    public boolean deleteProduct(Long id){
        return productDAO.delete(id);
    }
}
