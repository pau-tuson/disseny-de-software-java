package ui_solution;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Colleague: only knows the mediator (no AmountListener, no TableListener)
public class PayListener implements ActionListener {

  private final Mediator mediator;

  public PayListener(Mediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    mediator.pay();
  }
}
