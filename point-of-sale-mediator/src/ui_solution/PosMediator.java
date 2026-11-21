package ui_solution;

import pos_creditcard.PointOfSale;

import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Color;
import java.util.HashMap;

/**
 * Mediator pattern: ConcreteMediator.
 *
 * It is the only class of the user interface that knows the PointOfSale
 * (the model), the StatusPanel and the amount text field. It also keeps
 * the state that before was scattered among the listeners:
 *  - which is the current (last clicked) table
 *  - which table has an ongoing sale and its id
 *  - how many products have been ordered at each table
 */
public class PosMediator implements Mediator {

  private static final String EURO = "€";

  private final PointOfSale pointOfSale;
  private final StatusPanel statusPanel;
  private final JTextField textAmount;

  // the last clicked table button, null if no table selected yet
  private JButton currentTableButton = null;

  // table name ("T11", "T12"...) -> id of its ongoing (not yet paid) sale
  private final HashMap<String, Integer> saleOfTable = new HashMap<>();

  // table name -> number of products ordered in its ongoing sale
  private final HashMap<String, Integer> numProductsOfTable = new HashMap<>();

  public PosMediator(PointOfSale pointOfSale, StatusPanel statusPanel,
                     JTextField textAmount) {
    this.pointOfSale = pointOfSale;
    this.statusPanel = statusPanel;
    this.textAmount = textAmount;
  }

  //
  // Methods called by the listeners (colleagues)
  //

  @Override
  public void tableSelected(JButton tableButton) {
    if (currentTableButton != null) {
      // previous table: pink if it still has a sale to pay, default otherwise
      currentTableButton.setBackground(colorOfTable(currentTableButton));
    }
    currentTableButton = tableButton;
    currentTableButton.setBackground(Color.GREEN);
    updateStatus("Table " + currentTable() + " selected");
  }

  @Override
  public void newSale() {
    if (!checkTableSelected()) {
      return;
    }
    String table = currentTable();
    if (saleOfTable.containsKey(table)) {
      updateStatus("Table " + table + " already has an unpaid sale,"
          + "\npay it before making a new one");
      return;
    }
    int idSale = pointOfSale.makeNewSale();
    saleOfTable.put(table, idSale);
    numProductsOfTable.put(table, 0);
    updateStatus("New sale " + idSale + " for table " + table);
  }

  @Override
  public void productSelected(String productName) {
    if (!checkTableHasSale()) {
      return;
    }
    String table = currentTable();
    int idSale = saleOfTable.get(table);
    pointOfSale.addLineItemToSale(idSale, productName, 1);
    numProductsOfTable.put(table, numProductsOfTable.get(table) + 1);
    updateStatus("Added 1 " + productName);
  }

  @Override
  public void printReceipt() {
    if (!checkTableHasSale()) {
      return;
    }
    int idSale = saleOfTable.get(currentTable());
    if (!pointOfSale.saleHasProducts(idSale)) {
      updateStatus("No products ordered yet");
      return;
    }
    System.out.println();
    pointOfSale.printReceiptOfSale(idSale);
    updateStatus("Receipt printed (see console)");
  }

  @Override
  public void pay() {
    if (!checkTableHasSale()) {
      return;
    }
    String table = currentTable();
    int idSale = saleOfTable.get(table);

    if (!pointOfSale.saleHasProducts(idSale)) {
      updateStatus("No products ordered yet,\nnothing to pay");
      return;
    }
    double amount = readAmount();
    if (Double.isNaN(amount)) {
      updateStatus("Invalid amount, write a number\nand press Enter");
      return;
    }
    if (amount <= 0) {
      updateStatus("Amount paid is zero");
      return;
    }
    if (!pointOfSale.isAmountHandedEnough(idSale, amount)) {
      updateStatus("Amount paid " + amount + " " + EURO + " is not enough");
      return;
    }

    pointOfSale.payOneSaleCash(idSale, amount);
    pointOfSale.printPayment(idSale);
    int numProducts = numProductsOfTable.get(table);

    // the table is free again
    saleOfTable.remove(table);
    numProductsOfTable.remove(table);
    textAmount.setText("0.0 " + EURO);

    // show the paid sale one last time
    statusPanel.setTable(table);
    statusPanel.setSale(String.valueOf(idSale));
    statusPanel.setNumProducts(numProducts);
    statusPanel.setState("PAID");
    show("Sale " + idSale + " paid, change in console");
  }

  @Override
  public void amountEntered() {
    double amount = readAmount();
    if (Double.isNaN(amount)) {
      updateStatus("Invalid amount: " + textAmount.getText());
      return;
    }
    textAmount.setText(amount + " " + EURO);
    updateStatus("Amount entered: " + amount + " " + EURO);
  }

  //
  // Private helpers
  //

  private String currentTable() {
    return currentTableButton.getText();
  }

  private boolean checkTableSelected() {
    if (currentTableButton == null) {
      updateStatus("Select a table first");
      return false;
    }
    return true;
  }

  private boolean checkTableHasSale() {
    if (!checkTableSelected()) {
      return false;
    }
    if (!saleOfTable.containsKey(currentTable())) {
      updateStatus("Table " + currentTable() + " has no sale yet,"
          + "\npress New sale first");
      return false;
    }
    return true;
  }

  // pink = table with a sale not paid yet, null = default button color
  private Color colorOfTable(JButton tableButton) {
    return saleOfTable.containsKey(tableButton.getText()) ? Color.PINK : null;
  }

  // reads the amount field: "12.5 €", "12,5" -> 12.5 ; NaN if not a number
  private double readAmount() {
    String text = textAmount.getText().replace(EURO, "").replace(",", ".").trim();
    try {
      return Double.parseDouble(text);
    } catch (NumberFormatException e) {
      return Double.NaN;
    }
  }

  // copies the state of the current table into the status panel
  private void updateStatus(String message) {
    if (currentTableButton == null) {
      statusPanel.setTable("-");
      statusPanel.setSale("-");
      statusPanel.setNumProducts(0);
      statusPanel.setState("NO SALE");
    } else {
      String table = currentTable();
      statusPanel.setTable(table);
      if (saleOfTable.containsKey(table)) {
        statusPanel.setSale(String.valueOf(saleOfTable.get(table)));
        statusPanel.setNumProducts(numProductsOfTable.get(table));
        statusPanel.setState("UNPAID");
      } else {
        statusPanel.setSale("-");
        statusPanel.setNumProducts(0);
        statusPanel.setState("NO SALE");
      }
    }
    show(message);
  }

  private void show(String message) {
    statusPanel.refresh(message);
    System.out.println("[UI] " + message.replace("\n", " "));
  }
}
