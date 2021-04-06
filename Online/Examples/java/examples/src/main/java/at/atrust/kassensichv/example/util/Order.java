package at.atrust.kassensichv.example.util;

import java.util.ArrayList;
import java.util.List;

public class Order {
	
	private class OrderItem {

		private int amount;
		private String description;
		private int price;
		
		private static final String ORDER_ITEM_SEPARATOR = ";";
		
		public OrderItem(int amount, String description, int price) {
			this.amount = amount;
			this.description = description;
			this.price = price;
		}
		
		public String getFormatedOrderItem() {
			return amount + ORDER_ITEM_SEPARATOR + getFormatedDescription() + ORDER_ITEM_SEPARATOR + CashRegisterFormater.toEuroString(price);
		}
		
		private String getFormatedDescription() {
			return description.replace("\"", "\"\"");
		}
		
	}
	
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	private static final String ORDER_ITEM_SEPARATOR = "\n";
	
	public void addItem(int amount, String description, int price) {
		OrderItem oi = new OrderItem(amount, description, price);
		orderItems.add(oi);
	}
	
	public String getFormatedOrder() {
		String toReturn = "";
		for(OrderItem oi : orderItems) {
			toReturn = toReturn + oi.getFormatedOrderItem() + ORDER_ITEM_SEPARATOR;
		}
		if(orderItems.size() != 0) {
			toReturn = toReturn.substring(0, toReturn.length() - 1);
		}
		return toReturn;
	}

}
