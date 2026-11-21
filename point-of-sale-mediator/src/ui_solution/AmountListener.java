package ui_solution;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Colleague of the amount JTextField: called when the user presses Enter
public class AmountListener implements ActionListener {

  private final Mediator mediator;

  public AmountListener(Mediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    mediator.amountEntered();
  }
}
