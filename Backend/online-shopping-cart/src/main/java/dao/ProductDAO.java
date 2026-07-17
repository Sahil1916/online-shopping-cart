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

    // Atomically decrements stock only if enough quantity is available.
    // Returns false if there isn't enough stock (prevents overselling under concurrent orders).
    public boolean decrementStock(Long productId, int qty) {

        String sql = "UPDATE products SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";

        try (
                Connection con = DBconnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, qty);
            ps.setLong(2, productId);
            ps.setInt(3, qty);

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

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            product.setCreatedAt(createdAt.toLocalDateTime());
        }

        return product;
    }
}