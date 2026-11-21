package ui_solution;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Colleague: only knows the mediator
public class NewSaleListener implements ActionListener {

  private final Mediator mediator;

  public NewSaleListener(Mediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    mediator.newSale();
  }
}
