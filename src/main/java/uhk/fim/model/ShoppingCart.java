package uhk.fim.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("shoppingCart")
public class ShoppingCart {
    private List<ShoppingCartItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<ShoppingCartItem>();
    }

    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public void addItem(ShoppingCartItem newItem) {
        boolean isInList = false;
        for (ShoppingCartItem item : items) {
            if(item.getName().equals(newItem.getName()) && item.getPricePerPiece() == newItem.getPricePerPiece()) {
                isInList = true;
                item.setPieces(item.getPieces() + newItem.getPieces());
                break;
            }
        }

        if(!isInList)
            this.items.add(newItem);
    }

    public double getTotalPrice() {
        double sum = 0;
        for (ShoppingCartItem item : items) {
            sum += item.getTotalPrice();
        }
        return sum;
    }

    public double getBoughtPrice() {
        return items
            .stream()
            .filter(ShoppingCartItem::isBought)
            .map(ShoppingCartItem::getTotalPrice)
            .reduce(0.0, Double::sum);
    }
}
