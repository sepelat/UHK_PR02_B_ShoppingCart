package uhk.fim.helpers;

import uhk.fim.model.ShoppingCart;

import java.io.File;
import java.io.FileNotFoundException;

public interface IFileManipulator {

    ShoppingCart readFromFile(File file) throws FileNotFoundException;

    String writeToFile(ShoppingCart shoppingCart);

}
