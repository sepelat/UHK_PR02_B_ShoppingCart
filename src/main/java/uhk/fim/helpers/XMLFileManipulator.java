package uhk.fim.helpers;


import com.thoughtworks.xstream.XStream;
import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import java.io.File;
import java.io.FileNotFoundException;

public class XMLFileManipulator implements IFileManipulator {
    @Override
    public ShoppingCart readFromFile(File file) throws FileNotFoundException {
        XStream xstream = new XStream();
        xstream.processAnnotations(ShoppingCart.class);
        xstream.processAnnotations(ShoppingCartItem.class);

        return (ShoppingCart) xstream.fromXML(file);
    }

    @Override
    public String writeToFile(ShoppingCart shoppingCart) {
        XStream xstream = new XStream();
        xstream.processAnnotations(ShoppingCart.class);
        xstream.processAnnotations(ShoppingCartItem.class);

        return xstream.toXML(shoppingCart);
    }
}
