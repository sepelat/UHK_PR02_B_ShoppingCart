package uhk.fim.gui;

import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import javax.swing.table.AbstractTableModel;

public class ShoppingCartTableModel extends AbstractTableModel {
    // V modelu potřebujeme referenci na data
    private ShoppingCart shoppingCart;

    @Override
    public int getRowCount() {
        return shoppingCart.getItems().size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    // Tato metoda se volá, když se tabulka dotazuje hodnotu v buňce. Tedy pro kažkou buňku.
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Řádek tabulky (rowIndex) odpovídá pozici položky v seznamu položek
        ShoppingCartItem item = shoppingCart.getItems().get(rowIndex);
        // Podle indexu sloupce vracíme atribut položky
        switch (columnIndex) {
            case 0:
                return item.getName();
            case 1:
                return item.getPricePerPiece();
            case 2:
                return item.getPieces();
            case 3:
                return item.getTotalPrice();
            case 4:
                return item.isBought();
            default:
                return null;
        }
    }

    // Tato metoda se volá, když se tabulka dotazuje na nýzvy sloupců
    @Override
    public String getColumnName(int columnIndex) {
        // Podle indexu sloupce vracíme název
        switch (columnIndex) {
            case 0:
                return "Název";
            case 1:
                return "Cena/kus";
            case 2:
                return "Počet kusů";
            case 3:
                return "Cena celkem";
            case 4:
                return "Zakoupeno";
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
            case 3:
                return Double.class;
            case 2:
                return Integer.class;
            case 4:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 4;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 4) {
            shoppingCart.getItems().get(rowIndex).setBought((boolean) aValue);
        }
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
}
