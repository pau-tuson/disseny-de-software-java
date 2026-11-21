package ui_solution;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Colleague: only knows the mediator (and its own button, to say which table)
public class TableListener implements ActionListener {

  private final Mediator mediator;
  private final JButton tableButton;

  public TableListener(Mediator mediator, JButton tableButton) {
    this.mediator = mediator;
    this.tableButton = tableButton;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    mediator.tableSelected(tableButton);
  }
}
