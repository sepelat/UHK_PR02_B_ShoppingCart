package uhk.fim.gui;

import com.google.gson.Gson;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import uhk.fim.helpers.CSVFileManipulator;
import uhk.fim.helpers.XMLFileManipulator;
import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.xml.parsers.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;

public class MainFrame extends JFrame implements ActionListener {
    JPanel panelMain;

    // Tlačítka deklarujeme zde, abychom k nim měli přístup v metodě actionPerformed
    JButton btnInputAdd;
    JTextField txtInputName, txtInputPricePerPiece;
    JSpinner spInputPieces;

    // Labels
    JLabel lblTotalPrice = new JLabel();

    ShoppingCart shoppingCart;
    ShoppingCartTableModel shoppingCartTableModel = new ShoppingCartTableModel();


    public MainFrame(int width, int height) {
        super("PRO2 - Shopping cart");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        readFromFileCSV(new File("src/storage.csv"));

        initGUI();

        updateFooter();
    }

    public void initGUI() {
        // Vytvoříme hlavní panel, do kterého budeme přidávat další (pod)panely.
        // Naším cílem při tvorbě GUI, je snaha jednotlivé komponenty zanořovat.
        panelMain = new JPanel(new BorderLayout());

        // Menu
        createMenuBar();

        // Vytvoříme další 3 panely. Panel pro prvky formuláře pro přidání položky.
        // Panel pro tabulku a panel pro patičku.
        JPanel panelInputs = new JPanel(new FlowLayout(FlowLayout.LEFT)); // FlowLayout LEFT - komponenty chceme zarovnat zleva doprava.
        JPanel panelTable = new JPanel(new BorderLayout());
        JPanel panelFooter = new JPanel(new BorderLayout());

        // *** Formulář pro přidání položky ***
        // Název
        JLabel lblInputName = new JLabel("Název: ");
        txtInputName = new JTextField("", 15);
        // Cena za 1 kus
        JLabel lblInputPricePerPiece = new JLabel("Cena 1 kus: ");
        txtInputPricePerPiece = new JTextField("", 5);
        // Počet kusů
        JLabel lblInputPieces = new JLabel("Počet kusů: ");
        spInputPieces = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        // Tlačítka
        btnInputAdd = new JButton("Přidat");
        btnInputAdd.addActionListener(this); // Nastavení ActionListeneru - kdo obslouží kliknutí na tlačítko.

        // Přidání komponent do horního panelu pro formulář na přidání položky
        panelInputs.add(lblInputName);
        panelInputs.add(txtInputName);
        panelInputs.add(lblInputPricePerPiece);
        panelInputs.add(txtInputPricePerPiece);
        panelInputs.add(lblInputPieces);
        panelInputs.add(spInputPieces);
        panelInputs.add(btnInputAdd);

        // *** Patička ***
        lblTotalPrice = new JLabel("");
        panelFooter.add(lblTotalPrice, BorderLayout.WEST);

        // *** Tabulka ***
        JTable table = new JTable(shoppingCartTableModel);
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // TODO: Update shoppingCart model so Footer could update
                updateFooter();
            }
        });

        // Tabulku přidáme do panelu a obalíme ji komponentou JScrollPane
        panelTable.add(new JScrollPane(table), BorderLayout.CENTER);

        // Přidání (pod)panelů do panelu hlavního
        panelMain.add(panelInputs, BorderLayout.NORTH);
        panelMain.add(panelTable, BorderLayout.CENTER);
        panelMain.add(panelFooter, BorderLayout.SOUTH);

        // Přidání hlavního panelu do MainFrame (JFrame)
        add(panelMain);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Soubor");
        fileMenu.add(new AbstractAction("Nový nákupní seznam") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clearTable();
            }
        });
        fileMenu.add(new AbstractAction("Otevřít") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                readFromFileCSV(new File("src/storage.csv"));
            }
        });
        fileMenu.add(new AbstractAction("Otevřít jako") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                openAs();
            }
        });
        fileMenu.add(new AbstractAction("Uložit") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveToCSV(new File("src/storage.csv"));
            }
        });
        fileMenu.add(new AbstractAction("Uložit jako") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveAs();
            }
        });
        fileMenu.add(new AbstractAction("Načti json") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loadJson();
            }
        });
        menuBar.add(fileMenu);

        JMenu aboutMenu = new JMenu("O programu");
        menuBar.add(aboutMenu);

        setJMenuBar(menuBar);
    }

    // Při kliknutí na jakékoliv tlačítko se zavolá tato metoda.
    // Toho jsme docílili implementování rozhraní ActionListener a nastavením tlačítek např. btnInputAdd.addActionListener(this);
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Metoda se volá pro každé tlačítko, musíme tedy rozhodnout, co se má skutečně stát pro konkrétní tlačítka
        if (actionEvent.getSource() == btnInputAdd) {
            addProductToCart();
        }
    }

    private void addProductToCart() {
        if (!txtInputName.getText().isBlank()) {
            try {
                double price = Double.parseDouble(txtInputPricePerPiece.getText().replace(",", "."));
                if (price > 0) {
                    // Vytvořit novou položku
                    ShoppingCartItem item = new ShoppingCartItem(txtInputName.getText(), price, (int) spInputPieces.getValue(), false);
                    // Přidat položku do košíku
                    shoppingCart.addItem(item);
                    // Refreshnout tabulku
                    shoppingCartTableModel.fireTableDataChanged();
                    // Upravit patičku
                    updateFooter();
                } else {
                    JOptionPane.showMessageDialog(this, "Cena musí být větší než 0", "Chyba", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Zadejte správný formát ceny a počtu kusů!", "Chyba", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Došlo k chybě v programu!", "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vyplňte název produktu!", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFooter() {
        lblTotalPrice.setText(String.format(
            "Celková cena: %.2f | Cena zakoupených: %.2f | Rozdíl: %.2f",
            shoppingCart.getTotalPrice(),
            shoppingCart.getBoughtPrice(),
            shoppingCart.getTotalPrice() - shoppingCart.getBoughtPrice()
        ));
    }

    // Metoda, která se postará o uložení košíku do formátu CSV
    private void saveFileCsv() {
        // Init chooser - okno, které Vám dá možnost zvolit umístění souboru a jeho název
        JFileChooser fc = new JFileChooser();
        // Pokud jsme klikli na  tlačítko uložit
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Načeteme si cestu k souboru
            String fileName = fc.getSelectedFile().getAbsolutePath();
            // Try-with-resources https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
            // Nemusíme uzavírat spojení - uzavře se samo
            // Používáme buffer, který potřebuje nějaký primitivnější stream
            try (BufferedWriter bfw = new BufferedWriter(new FileWriter(fileName, true))) {
                // Projdeme položky
                for (ShoppingCartItem item : shoppingCart.getItems()) {
                    // Pro každou položku vytvoříme záznam v CSV
                    bfw.write(item.getName() + ";" + item.getPricePerPiece() + ";" + item.getPieces());
                    // Nový řádek
                    bfw.newLine();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Při ukládání došlo k chybě",
                        "Chyba",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void loadJson() {
        Gson gson = new Gson();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Simulace dlouhé odpovědi ze serveru
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    ShoppingCart cart = gson.fromJson(new InputStreamReader(
                            new URL("https://lide.uhk.cz/fim/student/benesja4/shoppingCart.json").openStream()
                    ), ShoppingCart.class);
                    System.out.println("Done");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    private void readFromFileCSV(File file) {
        CSVFileManipulator csvFileManipulator = new CSVFileManipulator();

        try {
            this.updateShoppingCart(csvFileManipulator.readFromFile(file));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readFromFileXML(File file) {
        XMLFileManipulator xmlFileManipulator = new XMLFileManipulator();

        try {
            this.updateShoppingCart(xmlFileManipulator.readFromFile(file));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveToXML(File file) {
        XMLFileManipulator xmlFileManipulator = new XMLFileManipulator();

        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(file))) {
            bfw.write(xmlFileManipulator.writeToFile(shoppingCart));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToCSV(File file) {
        CSVFileManipulator csvFileManipulator = new CSVFileManipulator();

        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(file))) {
            bfw.write(csvFileManipulator.writeToFile(shoppingCart));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearTable() {
        this.updateShoppingCart(new ShoppingCart());
    }

    private void updateShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        this.shoppingCartTableModel.setShoppingCart(shoppingCart);
        shoppingCartTableModel.fireTableDataChanged();
        updateFooter();
    }

    private void saveAs() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
        jFileChooser.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));

        int returnValue = jFileChooser.showSaveDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());

            switch (extension) {
                case ".xml": {
                    saveToXML(selectedFile);
                    break;
                }
                case ".csv": {
                    saveToCSV(selectedFile);
                    break;
                }
            }
        }
    }

    private void openAs() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
        jFileChooser.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));

        int returnValue = jFileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());

            switch (extension) {
                case ".xml": readFromFileXML(selectedFile);
                case ".csv": readFromFileCSV(selectedFile);
            }
        }
    }
}