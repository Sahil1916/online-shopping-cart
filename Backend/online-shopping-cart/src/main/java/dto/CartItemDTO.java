package dto;

public class CartItemDTO {
	private Long cartId;
	private Long productId;
	private String productName;
	private double price;
	private String imageUrl;
	private int quantity;
	
	
	public CartItemDTO() {
		
	}


	public CartItemDTO(Long cartId, Long productId, String productName, double price, String imageUrl, int quantity) {
		this.cartId = cartId;
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.imageUrl = imageUrl;
		this.quantity = quantity;
	}


	public Long getCartId() {
		return cartId;
	}


	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}


	public Long getProductId() {
		return productId;
	}


	public void setProductId(Long productId) {
		this.productId = productId;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	@Override
	public String toString() {
		return "CartItemDTO [cartId=" + cartId + ", productId=" + productId + ", productName=" + productName
				+ ", price=" + price + ", imageUrl=" + imageUrl + ", quantity=" + quantity + "]";
	}
	
	

	
}
