package uhk.fim.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {

    JButton btnInputAdd;
    JButton btnInputRemove;

    public MainFrame(int width, int height) {
        super("PRO2 - Shopping cart");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initGUI();
    }

    public void initGUI() {
        JPanel panelMain = new JPanel(new BorderLayout());

        JPanel panelInputs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelTable = new JPanel();
        JPanel panelFooter = new JPanel();

        // Název
        JLabel lblInputName = new JLabel("Název: ");
        JTextField txtInputName = new JTextField("", 15);
        // Cena za 1 kus
        JLabel lblInputPricePerPiece = new JLabel("Cena 1 kus: ");
        JTextField txtInputPricePerPiece = new JTextField("", 5);
        // Počet kusů
        JLabel lblInputPieces = new JLabel("Počet kusů: ");
        JTextField txtInputPieces = new JTextField("", 5);

        btnInputAdd = new JButton("Přidat");
        btnInputAdd.addActionListener(this);
        btnInputRemove = new JButton("Smazat");
        btnInputRemove.addActionListener(this);


        panelInputs.add(lblInputName);
        panelInputs.add(txtInputName);

        panelInputs.add(lblInputPricePerPiece);
        panelInputs.add(txtInputPricePerPiece);

        panelInputs.add(lblInputPieces);
        panelInputs.add(txtInputPieces);

        panelInputs.add(btnInputAdd);
        panelInputs.add(btnInputRemove);

        panelMain.add(panelInputs, BorderLayout.NORTH);
        panelMain.add(panelTable, BorderLayout.CENTER);
        panelMain.add(panelFooter, BorderLayout.SOUTH);

        add(panelMain);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == btnInputAdd) {
            System.out.println("Přidán produkt: [NÁZEV], [CENA/KUS] Kč [POČET]x");
        } else if(actionEvent.getSource() == btnInputRemove) {
            System.out.println("Smazáno!");
        }
    }
}