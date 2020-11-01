package uhk.fim.gui;

import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame implements ActionListener {
    JPanel panelMain;

    // Tlačítka deklarujeme zde, abychom k nim měli přístup v metodě actionPerformed
    JButton btnInputAdd;
    JTextField txtInputName, txtInputPricePerPiece;
    JSpinner spInputPieces;

    // Labels
    JLabel lblTotalPrice;

    ShoppingCart shoppingCart;
    ShoppingCartTableModel shoppingCartTableModel;


    public MainFrame(int width, int height) {
        super("PRO2 - Shopping cart");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Vytvoříme košík (data)
        shoppingCart = new ShoppingCart();
        // Vytvoříme model
        shoppingCartTableModel = new ShoppingCartTableModel();
        // Propojíme model s košíkem (data)
        shoppingCartTableModel.setShoppingCart(shoppingCart);

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
        JTable table = new JTable();
        // Tabulku propojíme s naším modelem
        table.setModel(shoppingCartTableModel);
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
                System.out.println("Nový soubor");
            }
        });
        fileMenu.add(new AbstractAction("Otevřít") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Otevřít");
            }
        });
        fileMenu.add(new AbstractAction("Uložit") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Uložit");
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
        if(!txtInputName.getText().isBlank()) {
            try {
                double price = Double.parseDouble(txtInputPricePerPiece.getText().replace(",", "."));
                if(price > 0) {
                    // Vytvořit novou položku
                    ShoppingCartItem item = new ShoppingCartItem(txtInputName.getText(), price, (int) spInputPieces.getValue());
                    // Přidat položku do košíku
                    shoppingCart.addItem(item);
                    // Refreshnout tabulku
                    shoppingCartTableModel.fireTableDataChanged();
                    // Upravit patičku
                    updateFooter();
                } else {
                    JOptionPane.showMessageDialog(this, "Cena musí být větší než 0", "Chyba", JOptionPane.ERROR_MESSAGE);
                }
            } catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Zadejte správný formát ceny a počtu kusů!", "Chyba", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Došlo k chybě v programu!", "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vyplňte název produktu!", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFooter() {
        lblTotalPrice.setText("Celková cena: " + String.format("%.2f", shoppingCart.getTotalPrice()));
    }
}