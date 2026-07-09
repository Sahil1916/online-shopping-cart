package service;

import java.util.List;

import dao.CartDAO;
import dto.CartItemDTO;
import model.Cart;

public class CartService {

	final private CartDAO cartdao = new CartDAO();

	public boolean addCart(Cart cart) {

		Cart find = cartdao.findCartItem(cart.getUserId(), cart.getProductId());

		if (find != null) {

			int newQuantity = find.getQuantity() + cart.getQuantity();

			return cartdao.updateQuantity(find.getId(), newQuantity);

		}

		return cartdao.addCart(cart);

 	}
	
	public List<CartItemDTO> getCartByUserId(Long UserId){
		
		return cartdao.getcartByUserId(UserId);
	}
	
	public boolean deleteCart(Long userId) {
		
		return cartdao.delete(userId);
	}
	
	public boolean upadateQuantity(Long cartId,int quantity) {
		
		return cartdao.updateQuantity(cartId, quantity);
	}

	
	public boolean updateQuantity(Long cartId, Long userId, int quantity) {

	    Cart cart = cartdao.findByCartIdAndUserId(cartId, userId);

	    if (cart == null) {
	        return false;
	    }

	    return cartdao.updateQuantity(cartId, quantity);
	}
}
