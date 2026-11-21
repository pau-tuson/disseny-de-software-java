package ui_solution;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Colleague: only knows the mediator and the name of its product
public class ProductListener implements ActionListener {

  private final Mediator mediator;
  private final String productName;

  public ProductListener(Mediator mediator, String productName) {
    this.mediator = mediator;
    this.productName = productName;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    mediator.productSelected(productName);
  }
}
