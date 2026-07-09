package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import config.DBconnection;
import model.OrderItem;

public class OrderItemDAO {

	public boolean saveOrderItem(OrderItem item) {

		String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?,?,?,?)";

		try (Connection con = DBconnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setLong(1, item.getOrderId());
			ps.setLong(2, item.getProductId());
			ps.setInt(3, item.getQuantity());
			ps.setBigDecimal(4, item.getPrice());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public List<OrderItem> getOrderItemsByOrderId(Long orderId) {

		List<OrderItem> itemlist = new ArrayList<OrderItem>();

		String sql = "SELECT * FROM order_items WHERE order_id = ?";

		try (Connection con = DBconnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setLong(1, orderId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				OrderItem item = new OrderItem();

				item.setId(rs.getLong("id"));
				item.setOrderId(rs.getLong("order_id"));
				item.setProductId(rs.getLong("product_id"));
				item.setQuantity(rs.getInt("quantity"));
				item.setPrice(rs.getBigDecimal("price"));

				itemlist.add(item);

			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return itemlist;

	}
}