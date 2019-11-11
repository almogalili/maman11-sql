import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

public class ResultSetTableModel extends AbstractTableModel {

	private final Connection connection;
	private final Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;

	private boolean connectedToDatabase = false;

	public ResultSetTableModel(String url, String username, String password, String query) throws SQLException {

		this.connection = DriverManager.getConnection(url, username, password);
		this.statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		this.connectedToDatabase = true;
		setQuery(query);

	}

	public Class getColumnClass(int column) {
		
		if (!this.connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		// determine java class of column
		try {

			String className = this.metaData.getColumnClassName(column + 1);

			return Class.forName(className);
		}

		catch (Exception e) {

			e.printStackTrace();
		}

		// if exception occurs return Object
		return Object.class;

	}

	@Override
	public int getColumnCount() throws IllegalStateException {

		if (!this.connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		try {

			// return how many columns in the model ResultSet
			return this.metaData.getColumnCount();

		} catch (SQLException e) {

			e.printStackTrace();

		}

		// default number of columns, if exception occurs
		return 0;
	}

	public String getColumnName(int column) throws IllegalStateException {
		
		if (!this.connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		try {

			// ResultSet columns start from 1, Not like JTable and Arrays
			// return the name of the column
			return this.metaData.getColumnName(column + 1);

		} catch (SQLException e) {

			e.printStackTrace();

		}

		// default column name, if exception occurs
		return "";

	}

	@Override
	public int getRowCount() throws IllegalStateException {

		if (!this.connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		return this.numberOfRows;

	}

	@Override
	public Object getValueAt(int row, int column) throws IllegalStateException {

		if (!this.connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		try {

			this.resultSet.absolute(row + 1);

			return this.resultSet.getObject(column + 1);

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return "";

	}

	public void setQuery(String query) throws SQLException, IllegalStateException {

		if (!this.connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		this.resultSet = this.statement.executeQuery(query);

		this.metaData = this.resultSet.getMetaData();

		this.resultSet.last();

		this.numberOfRows = this.resultSet.getRow();

		fireTableStructureChanged();

	}

	public void disconnectFromDatabase() {

		if (this.connectedToDatabase) {
			
			try {
				
				this.resultSet.close();
				this.statement.close();
				this.connection.close();
				
			} 
			catch (SQLException e) {
				
				e.printStackTrace();
				
			} 
			finally {

				this.connectedToDatabase = false;
				
			}
		}

	}

}
