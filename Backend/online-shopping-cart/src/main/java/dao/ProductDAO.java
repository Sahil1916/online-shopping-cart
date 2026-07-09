package dao;


import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import config.DBconnection;

public class ProductDAO {

    public boolean save(Product product) {

        String sql = "INSERT INTO products(name,description,category,price,mrp,quantity,image_url) VALUES(?,?,?,?,?,?,?)";

        try (
                Connection con = DBconnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getCategory());
            ps.setBigDecimal(4, product.getPrice());
            ps.setBigDecimal(5, product.getMrp());
            ps.setInt(6, product.getQuantity());
            ps.setString(7, product.getImageUrl());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> findAll() {

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products";

        try (
                Connection con = DBconnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    public Product findById(Long id) {

        String sql = "SELECT * FROM products WHERE id = ?";

        try (
                Connection con = DBconnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean update(Product product) {

        String sql = "UPDATE products SET name=?,description=?, category=?, price=?,mrp=?,quantity=?,image_url=?   WHERE id=?";

        try (
                Connection con = DBconnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getCategory());
            ps.setBigDecimal(4, product.getPrice());
            ps.setBigDecimal(5, product.getMrp());
            ps.setInt(6, product.getQuantity());
            ps.setString(7, product.getImageUrl());
            ps.setLong(8, product.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Long id) {

        String sql = "DELETE FROM products WHERE id=?";

        try (
                Connection con = DBconnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Product mapRow(ResultSet rs) 
            throws SQLException {

        Product product = new Product();

        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setCategory(rs.getString("category"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setMrp(rs.getBigDecimal("mrp"));
        product.setQuantity(rs.getInt("quantity"));
        product.setImageUrl(rs.getString("image_url"));

        return product;
    }
    
    public Product getProductById(Long productId) {

        String sql = "SELECT * FROM products WHERE id = ?";

        try (
                Connection con = DBconnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {

            ps.setLong(1, productId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Product product = new Product();

                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setMrp(rs.getBigDecimal("mrp"));
                product.setCategory(rs.getString("category"));
                product.setImageUrl(rs.getString("image_url"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                return product;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}