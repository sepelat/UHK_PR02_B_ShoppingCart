package uhk.fim.helpers;


import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVFileManipulator implements IFileManipulator {
    @Override
    public ShoppingCart readFromFile(File file) throws FileNotFoundException {
        ShoppingCart shoppingCart = new ShoppingCart();

        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            List<String> record = this.lineToRecord(scanner.nextLine());

            ShoppingCartItem shoppingCartItem = new ShoppingCartItem(
                    record.get(0),
                    Double.parseDouble(record.get(1)),
                    Integer.parseInt(record.get(2)),
                    Boolean.parseBoolean(record.get(3))
            );

            shoppingCart.addItem(shoppingCartItem);
        }

        scanner.close();

        return shoppingCart;
    }

    @Override
    public String writeToFile(ShoppingCart shoppingCart) {
        StringBuilder stringBuilder = new StringBuilder();

        shoppingCart.getItems().forEach(shoppingCartItem -> {
            stringBuilder.append(
                String.format(
                    "%s,%s,%s,%b\n",
                    shoppingCartItem.getName(),
                    shoppingCartItem.getPricePerPiece(),
                    shoppingCartItem.getPieces(),
                    shoppingCartItem.isBought()
                )
            );
        });

        return stringBuilder.toString();
    }

    private List<String> lineToRecord(String line) {
        List<String> values = new ArrayList<>();

        try (Scanner scanner = new Scanner(line)) {
            scanner.useDelimiter(",");

            while (scanner.hasNext()) {
                values.add(scanner.next());
            }
        }

        return values;
    }
}
