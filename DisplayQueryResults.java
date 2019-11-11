import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class DisplayQueryResults {

	private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";
	private static final String USERNAME = "postgres";
	private static final String PASSWORD = "12i12144";

	// execute default query, if we have syntax error
	private static final String DEFAULT_QUERY = "SELECT * FROM Employee";

	private static ResultSetTableModel tableModel;

	public static void main(String[] args) {

		try {

			tableModel = new ResultSetTableModel(DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY);

			final JTextArea queryArea = new JTextArea(DEFAULT_QUERY, 3, 100);
			queryArea.setWrapStyleWord(true);
			queryArea.setLineWrap(true);

			JScrollPane scrollPane = new JScrollPane(queryArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

			JButton submitQueryButton = new JButton("Submit Query");

			Box boxNorth = Box.createHorizontalBox();
			boxNorth.add(scrollPane);
			boxNorth.add(submitQueryButton);

			JTable resultTable = new JTable(tableModel);

			JFrame window = new JFrame("Displaying Query Results");
			window.add(boxNorth, BorderLayout.NORTH);
			window.add(new JScrollPane(resultTable), BorderLayout.CENTER);

			submitQueryButton.addActionListener (

					new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent event) {

							try {

								tableModel.setQuery(queryArea.getText());

							} catch (SQLException e1) {

								JOptionPane.showMessageDialog(null, e1.getMessage(), "Database Error",
										JOptionPane.ERROR_MESSAGE);

								// if error occurs when the user execute query, we execute the default query.

								try {

									tableModel.setQuery(DEFAULT_QUERY);
									queryArea.setText(DEFAULT_QUERY);

								} catch (SQLException e2) {

									JOptionPane.showMessageDialog(null, e2.getMessage(), "Database Error",
											JOptionPane.ERROR_MESSAGE);

									// ensure database connection is closed.
									tableModel.disconnectFromDatabase();

									// terminate application
									System.exit(1);

								}

							}

						}

					}

			);


			window.setSize(500, 250);
			window.setVisible(true);

			window.addWindowListener(

					new WindowAdapter() {

						public void windowClosed(WindowEvent event) {
							tableModel.disconnectFromDatabase();
							System.exit(0);
						}

					}

			);

		} catch (SQLException e) {

			JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);

			tableModel.disconnectFromDatabase();
			System.exit(1);

		}

	}

}
