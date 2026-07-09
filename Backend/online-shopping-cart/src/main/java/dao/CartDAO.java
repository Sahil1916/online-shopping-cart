package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.DBconnection;
import dto.CartItemDTO;
import model.Cart;

public class CartDAO {

	public boolean addCart(Cart cart) {

		String sql = "INSERT INTO cart (user_id,product_id,quantity)VALUES(?,?,?) ";

		try (Connection con = DBconnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, cart.getUserId());
			ps.setLong(2, cart.getProductId());
			ps.setInt(3, cart.getQuantity());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public Cart findCartItem(Long userId, Long productId) {

		String sql = "SELECT * FROM cart WHERE user_id = ? AND product_id = ?";

		try (Connection con = DBconnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);

		) {

			ps.setLong(1, userId);
			ps.setLong(2, productId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				Cart cart = new Cart();
				cart.setId(rs.getLong("id"));
				cart.setUserId(rs.getLong("user_id"));
				cart.setProductId(rs.getLong("product_id"));
				cart.setQuantity(rs.getInt("quantity"));

				return cart;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public boolean updateQuantity(Long cart_id, int quantity) {

		String sql = "UPDATE cart SET quantity = ? WHERE id = ?";
		try (Connection con = DBconnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setInt(1, quantity);
			ps.setLong(2, cart_id);

			int rows = ps.executeUpdate();

			return rows > 0;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	public List<CartItemDTO> getcartByUserId(long userId) {

		String sql =
			    "SELECT c.id AS cart_id, c.quantity, p.id AS product_id, " +
			    "p.name, p.price, p.image_url " +
			    "FROM cart c " +
			    "JOIN products p ON c.product_id = p.id " +
			    "WHERE c.user_id = ?";
		List<CartItemDTO> cartitem = new ArrayList<>();

		try (Connection con = DBconnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setLong(1, userId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				CartItemDTO item = new CartItemDTO();

				item.setCartId(rs.getLong("cart_id"));
				item.setProductId(rs.getLong("product_id"));
				item.setProductName(rs.getString("name"));
				item.setPrice(rs.getDouble("price"));
				item.setImageUrl(rs.getString("image_url"));
				item.setQuantity(rs.getInt("quantity"));

				cartitem.add(item);

			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return cartitem;

	}
	
	 public boolean delete(Long cartId) {

	        String sql = "DELETE FROM cart WHERE id=?";

	        try (
	                Connection con = DBconnection.getConnection();
	                PreparedStatement ps = con.prepareStatement(sql)
	        ) {

	            ps.setLong(1, cartId);

	            return ps.executeUpdate() > 0;

	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }
	    }
	 
	 
	 public Cart findByCartIdAndUserId(Long cartId, Long userId) {

		    String sql = "SELECT * FROM cart WHERE id = ? AND user_id = ?";

		    try (
		            Connection con = DBconnection.getConnection();
		            PreparedStatement ps = con.prepareStatement(sql);
		    ) {

		        ps.setLong(1, cartId);
		        ps.setLong(2, userId);

		        ResultSet rs = ps.executeQuery();

		        if (rs.next()) {

		            Cart cart = new Cart();

		            cart.setId(rs.getLong("id"));
		            cart.setUserId(rs.getLong("user_id"));
		            cart.setProductId(rs.getLong("product_id"));
		            cart.setQuantity(rs.getInt("quantity"));

		            return cart;
		        }

		    } catch (Exception e) {
		        throw new RuntimeException(e);
		    }

		    return null;
		}
	
	 public List<Cart> getCartItemsByUserId(Long userId) {

		    List<Cart> cartList = new ArrayList<>();

		    String sql = "SELECT * FROM cart WHERE user_id = ?";

		    try (
		            Connection con = DBconnection.getConnection();
		            PreparedStatement ps = con.prepareStatement(sql);
		    ) {

		        ps.setLong(1, userId);

		        ResultSet rs = ps.executeQuery();

		        while (rs.next()) {

		            Cart cart = new Cart();

		            cart.setId(rs.getLong("id"));
		            cart.setUserId(rs.getLong("user_id"));
		            cart.setProductId(rs.getLong("product_id"));
		            cart.setQuantity(rs.getInt("quantity"));

		            cartList.add(cart);
		        }

		    } catch (Exception e) {
		        throw new RuntimeException(e);
		    }

		    return cartList;
		}
	 
	 public boolean clearCart(Long userId) {

		    String sql = "DELETE FROM cart WHERE user_id = ?";

		    try (
		            Connection con = DBconnection.getConnection();
		            PreparedStatement ps = con.prepareStatement(sql);
		    ) {

		        ps.setLong(1, userId);

		        return ps.executeUpdate() > 0;

		    } catch (Exception e) {
		        throw new RuntimeException(e);
		    }
		}
	 
}
