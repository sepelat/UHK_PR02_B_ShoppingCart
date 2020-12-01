package uhk.fim.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("shoppingCartItem")
public class ShoppingCartItem {
    private String name;
    private double pricePerPiece;
    private int pieces;
    private boolean bought;

    public ShoppingCartItem(String name, double pricePerPiece, int pieces, boolean bought) {
        this.name = name;
        this.pricePerPiece = pricePerPiece;
        this.pieces = pieces;
        this.bought = bought;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerPiece() {
        return pricePerPiece;
    }

    public void setPricePerPiece(double pricePerPiece) {
        this.pricePerPiece = pricePerPiece;
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public double getTotalPrice() {
        return pricePerPiece * pieces;
    }

    public boolean isBought() { return bought; }

    public void setBought(boolean bought) { this.bought = bought; }
}
