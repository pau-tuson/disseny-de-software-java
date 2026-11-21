package ui_solution;

import javax.swing.JButton;

/**
 * Mediator pattern: abstract Mediator.
 *
 * All the listeners (colleagues) of the user interface only know this
 * interface. When the user clicks a button, its listener just tells the
 * mediator "this happened" and the mediator decides what to do:
 * talk to the PointOfSale, update the StatusPanel, the amount field, etc.
 *
 * This way listeners do not know each other nor the PointOfSale,
 * so the many-to-many dependencies become one-to-many (all -> mediator).
 */
public interface Mediator {

  void tableSelected(JButton tableButton);

  void newSale();

  void productSelected(String productName);

  void printReceipt();

  void pay();

  void amountEntered();
}
