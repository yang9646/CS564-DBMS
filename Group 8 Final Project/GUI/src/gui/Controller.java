package gui;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import sqlConnect.SQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * UW-Madison, CS 564, Summer 2020, Team 8 Final Project: Game Ranking Database
 * GUI/Front End by @author Nate Sackett
 * Requires JavaFX and mysql-connector; FXML GUI design implemented using SceneBuilder
 * 
 * Controller used to initialize elements from Main.fxml and handle button methods
 */
public class Controller {

	/* Sorted variable declarations for GUI elements */

	 @FXML
	    private TableColumn<?, ?> tJP_Sales;
	    @FXML
	    private TextField uJP_Sales;
	    @FXML
	    private TableView data;
	    @FXML
	    private TextField iPublisher;
	    @FXML
	    private TableColumn<?, ?> tEU_Sales;
	    @FXML
	    private TextField sGameName;
	    @FXML
	    private TextField iGameName;
	    @FXML
	    private TextField iPlatform;
	    @FXML
	    private TextField iEU_Sales;
	    @FXML
	    private TextField uPlatform;
	    @FXML
	    private TableColumn<?, ?> tGameName;
	    @FXML
	    private TableColumn<?, ?> tPublisher;
	    @FXML
	    private TextField uEU_Sales;
	    @FXML
	    private TextField uOther_Sales;
	    @FXML
	    private TextField uGameName;
	    @FXML
	    private TableColumn<?, ?> tGID;
	    @FXML
	    private TableColumn<?, ?> tGlobal_Sales;
	    @FXML
	    private TableColumn<?, ?> tPlatform;
	    @FXML
	    private TableColumn<?, ?> tYear;
	    @FXML
	    private TextField iGenre;
	    @FXML
	    private TextField genericQuery;
	    @FXML
	    private TextField dPlatform;
	    @FXML
	    private TableColumn<?, ?> tGenre;
	    @FXML
	    private TextField uNA_Sales;
	    @FXML
	    private TextField iOther_Sales;
	    @FXML
	    private TextField iJP_Sales;
	    @FXML
	    private TextField iNA_Sales;
	    @FXML
	    private TableColumn<?, ?> tOther_Sales;
	    @FXML
	    private TextField iYear;
	    @FXML
	    private TableColumn<?, ?> tGameRank;
	    @FXML
	    private TableColumn<?, ?> tNA_Sales;
	    @FXML
	    private TextField dGameName;

	// Reused arguments for Button Methods
	private SQLConnection sqlConnection = new SQLConnection();
    private String databasePrefix ="gameProject";
    private String hostName ="localhost";
	private String databaseURL2 ="jdbc:mysql://"+hostName+"/"+databasePrefix+"?autoReconnect=true&useSSL=false";
	private Connection c = sqlConnection.getConnection(databaseURL2);
	@SuppressWarnings("rawtypes")
	private ObservableList<ObservableList> table;
	private Statement statement;
	private ResultSet rs;

	/* Button Methods */

	/*
	 * Built referencing:
	 * https://blog.ngopal.com.np/2011/10/19/dyanmic-tableview-data-from-database/comment-page-1/
	 * and @Yerbol at
	 * https://stackoverflow.com/questions/18941093/how-to-fill-up-a-tableview-with-database-data
	 */
	@FXML
	void refreshDB(ActionEvent event) {
		ResultSet rs = null;
		String sql = "select GameRank, g.gid, GameName, Platform, Year, Genre, PublisherName, NA_Sales, EU_Sales, JP_Sales, Other_Sales, s.Global_Sales from game g, publisher p, ranking r, sales s where r.gid = g.gid and g.gid = p.gid and p.gid = s.gid order by GameRank asc limit 17000;";

		try {
			// Result set
			rs = c.createStatement().executeQuery(sql);
			handle(rs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

			
	}

	@FXML
	void searchGame(ActionEvent event) {
		String gameName = sGameName.getText().toString();
		String sql = "{call searchGame(?)}";
		
		try {
			CallableStatement cs = c.prepareCall(sql);
			cs.setString(1, gameName);
			ResultSet rs = cs.executeQuery();
			handle(rs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void insertGame(ActionEvent event) {
		String sql = "{call insertGame(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		
		try {
			CallableStatement cs = c.prepareCall(sql);
			
			cs.setString(1, iGameName.getText() != null ? iGameName.getText().toString() : "N/A");
			cs.setString(2, iPlatform.getText() != null ? iPlatform.getText().toString() : "N/A");
			cs.setString(3, iYear.getText() != null ? iYear.getText().toString() : "N/A");
			cs.setString(4, iGenre.getText() != null ? iGenre.getText().toString() : "N/A");
			cs.setString(5, iPublisher.getText() != null ? iPublisher.getText().toString() : "N/A");
			cs.setDouble(6, iNA_Sales.getText() != null ? Double.valueOf(iNA_Sales.getText()) : 0.0);
			cs.setDouble(7, iEU_Sales.getText() != null ? Double.valueOf(iEU_Sales.getText()) : 0.0);
			cs.setDouble(8, iJP_Sales.getText() != null ? Double.valueOf(iJP_Sales.getText()) : 0.0);
			cs.setDouble(9, iOther_Sales.getText() != null ? Double.valueOf(iOther_Sales.getText()) : 0.0);
			
			cs.execute();
			cs.close();			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Refresh table view
		refreshDB(event);
	}

	@FXML
	void deleteGame(ActionEvent event) {
		String sql = "{call deleteGame(?, ?)}";
		
		try {
			CallableStatement cs = c.prepareCall(sql);
			
			cs.setString(1, dGameName.getText() != null ? dGameName.getText().toString() : "N/A");
			cs.setString(2, dPlatform.getText() != null ? dPlatform.getText().toString() : "N/A");
						
			cs.execute();
			cs.close();			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Refresh table view
		refreshDB(event);
	}

	@FXML
	void updateSales(ActionEvent event) {
		String sql = "{call updateSales(?, ?, ?, ?, ?, ?)}";
		
		try {
			CallableStatement cs = c.prepareCall(sql);
			
			cs.setString(1, uGameName.getText() != null ? uGameName.getText().toString() : "N/A");
			cs.setString(2, uPlatform.getText() != null ? uPlatform.getText().toString() : "N/A");
			cs.setDouble(3, uNA_Sales.getText() != null ? Double.valueOf(uNA_Sales.getText()) : 0.0);
			cs.setDouble(4, uEU_Sales.getText() != null ? Double.valueOf(uEU_Sales.getText()) : 0.0);
			cs.setDouble(5, uJP_Sales.getText() != null ? Double.valueOf(uJP_Sales.getText()) : 0.0);
			cs.setDouble(6, uOther_Sales.getText() != null ? Double.valueOf(uOther_Sales.getText()) : 0.0);
			
			cs.execute();
			cs.close();			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Refresh table view
		refreshDB(event);
	}

	@FXML
	void updateGlobalSales(ActionEvent event) {
		String sql = "{call updateGlobalSales()}";
		
		try {
			CallableStatement cs = c.prepareCall(sql);
			
			cs.execute();
			cs.close();			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Refresh table view
		refreshDB(event);
	}

	@FXML 
	void updateRank(ActionEvent event) {
		String sql = "{call updateRank()}";
		
		try {
			CallableStatement cs = c.prepareCall(sql);
			
			cs.execute();
			cs.close();			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Refresh table view
		refreshDB(event);
	}
	
	@FXML
    void topGamePlatform(ActionEvent event) {
    	ResultSet rs = null;
    	String sql = "select r.gamerank, g.gid, g.GameName, g.platform, g.year, g.genre, \r\n" + 
    			"	p.publishername, s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, \r\n" + 
    			"	s.global_sales\r\n" + 
    			"from Ranking r, Game g, Publisher p, Sales s\r\n" + 
    			"where g.GID = r.GID\r\n" + 
    			"	and g.GID = p.GID\r\n" + 
    			"    and g.GID = s.GID\r\n" + 
    			"group by g.platform\r\n" + 
    			"having r.gamerank <= all (select r2.gamerank\r\n" + 
    			"						from Game g2, Ranking r2\r\n" + 
    			"                        where g.platform = g2.platform\r\n" + 
    			"                        and g2.GID = r2.GID\r\n" + 
    			"                        and g.GID <> g2.GID\r\n" + 
    			"                        )\r\n" + 
    			"order by g.platform;";
    	
		try {			
			// Result set
			rs = c.createStatement().executeQuery(sql);
			handle(rs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void topGameGenre(ActionEvent event) {
    	ResultSet rs = null;
    	String sql = "select r.gamerank, g.gid, g.GameName, g.platform, g.year, g.genre, p.publishername, \r\n" + 
    			"	s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, s.global_sales\r\n" + 
    			"from Ranking r, Game g, Publisher p, Sales s\r\n" + 
    			"where g.GID = r.GID\r\n" + 
    			"	and g.GID = p.GID\r\n" + 
    			"    and g.GID = s.GID\r\n" + 
    			"group by g.genre\r\n" + 
    			"having r.gamerank <= all (select r2.gamerank\r\n" + 
    			"						from Game g2, Ranking r2\r\n" + 
    			"                        where g.genre = g2.genre\r\n" + 
    			"                        and g2.GID = r2.GID\r\n" + 
    			"                        and g.GID <> g2.GID\r\n" + 
    			"                        )\r\n" + 
    			"order by g.genre;";
    	
		try {			
			// Result set
			rs = c.createStatement().executeQuery(sql);
			handle(rs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void topGameYear(ActionEvent event) {
    	ResultSet rs = null;
    	String sql = "select r.gamerank, g.gid, g.GameName, g.platform, g.year, g.genre, p.publishername, \r\n" + 
    			"	s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, s.global_sales\r\n" + 
    			"from Ranking r, Game g, Publisher p, Sales s\r\n" + 
    			"where g.GID = r.GID\r\n" + 
    			"	and g.GID = p.GID\r\n" + 
    			"    and g.GID = s.GID\r\n" + 
    			"group by g.year\r\n" + 
    			"having r.gamerank < all (select r2.gamerank\r\n" + 
    			"						from Game g2, Ranking r2\r\n" + 
    			"                        where g.year = g2.year\r\n" + 
    			"                        and g2.GID = r2.GID\r\n" + 
    			"                        and g.GID <> g2.GID\r\n" + 
    			"                        )\r\n" + 
    			"order by g.year desc;";
    	
		try {			
			// Result set
			rs = c.createStatement().executeQuery(sql);
			handle(rs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void topNAYear(ActionEvent event) {
    	ResultSet rs = null;
    	String sql = "select r.gamerank, g.gid, g.GameName, g.platform, g.year, g.genre, p.publishername, \r\n" + 
    			"	s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, s.global_sales\r\n" + 
    			"from Ranking r, Game g, Publisher p, Sales s\r\n" + 
    			"where g.GID = r.GID\r\n" + 
    			"	and g.GID = p.GID\r\n" + 
    			"    and g.GID = s.GID\r\n" + 
    			"    and s.NA_Sales > all (select s2.NA_Sales\r\n" + 
    			"						from Game g2, Sales s2\r\n" + 
    			"                        where g.year = g2.year\r\n" + 
    			"							and g2.GID = s2.GID\r\n" + 
    			"							and g.GID <> g2.GID\r\n" + 
    			"                        )\r\n" + 
    			"group by g.year\r\n" + 
    			"order by g.year desc;";
    	
		try {			
			// Result set
			rs = c.createStatement().executeQuery(sql);
			handle(rs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void topEUYear(ActionEvent event) {
    	ResultSet rs = null;
    	String sql = "select r.gamerank, g.gid, g.GameName, g.platform, g.year, g.genre, p.publishername, \r\n" + 
    			"	s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, s.global_sales\r\n" + 
    			"from Ranking r, Game g, Publisher p, Sales s\r\n" + 
    			"where g.GID = r.GID\r\n" + 
    			"	and g.GID = p.GID\r\n" + 
    			"    and g.GID = s.GID\r\n" + 
    			"    and s.EU_Sales >= all (select s2.EU_Sales\r\n" + 
    			"						from Game g2, Sales s2\r\n" + 
    			"                        where g2.GID = s2.GID\r\n" + 
    			"							and g.year = g2.year\r\n" + 
    			"							and g.GID <> g2.GID\r\n" + 
    			"                        )\r\n" + 
    			"group by g.year\r\n" + 
    			"order by g.year desc;";
    	
		try {			
			// Result set
			rs = c.createStatement().executeQuery(sql);
			handle(rs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void topJPYear(ActionEvent event) {
    	ResultSet rs = null;
    	String sql = "select r.gamerank, g.gid, g.GameName, g.platform, g.year, g.genre, p.publishername,\r\n" + 
    			"	s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, s.global_sales\r\n" + 
    			"from Ranking r, Game g, Publisher p, Sales s\r\n" + 
    			"where g.GID = r.GID\r\n" + 
    			"	and g.GID = p.GID\r\n" + 
    			"    and g.GID = s.GID\r\n" + 
    			"    and s.JP_Sales >= all (select s2.JP_Sales\r\n" + 
    			"						from Game g2, Sales s2\r\n" + 
    			"                        where g2.GID = s2.GID\r\n" + 
    			"							and g.year = g2.year\r\n" + 
    			"							and g.GID <> g2.GID\r\n" + 
    			"                        )\r\n" + 
    			"group by g.year\r\n" + 
    			"order by g.year desc;";
    	
		try {			
			// Result set
			rs = c.createStatement().executeQuery(sql);
			handle(rs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @SuppressWarnings("unchecked")
	private void handle(ResultSet rs) {
		ArrayList<Game> games = new ArrayList<>();
		Game game = null;
    	try {
			// Handle Result Set - Copied from refrshDB
			while (rs.next()) {
				game = new Game();
				game.gameRank.set(rs.getLong("gameRank"));
				game.gid.set(rs.getLong("GID"));
				game.gameName.set(rs.getString("GameName"));
				game.platform.set(rs.getString("Platform"));
				game.year.set(rs.getString("Year"));
				game.genre.set(rs.getString("Genre"));
				game.publisher.set(rs.getString("PublisherName"));
				game.naSales.set(rs.getDouble("NA_Sales"));
				game.euSales.set(rs.getDouble("EU_Sales"));
				game.jpSales.set(rs.getDouble("JP_Sales"));
				game.otherSales.set(rs.getDouble("Other_Sales"));
				game.globalSales.set(rs.getDouble("Global_Sales"));
				
				games.add(game);
			}
			rs.close();
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		@SuppressWarnings("rawtypes")
		ObservableList gamesData = FXCollections.observableArrayList(games);
		
		// Make columns readable
		tGameRank.setCellValueFactory(new PropertyValueFactory<>("gameRank"));
		tGID.setCellValueFactory(new PropertyValueFactory<>("gid"));
		tGameName.setCellValueFactory(new PropertyValueFactory<>("gameName"));
		tPlatform.setCellValueFactory(new PropertyValueFactory<>("platform"));
		tYear.setCellValueFactory(new PropertyValueFactory<>("year"));
		tGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
		tPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		tNA_Sales.setCellValueFactory(new PropertyValueFactory<>("naSales"));
		tEU_Sales.setCellValueFactory(new PropertyValueFactory<>("jpSales"));
		tJP_Sales.setCellValueFactory(new PropertyValueFactory<>("euSales"));
		tOther_Sales.setCellValueFactory(new PropertyValueFactory<>("otherSales"));
		tGlobal_Sales.setCellValueFactory(new PropertyValueFactory<>("globalSales"));
		
		data.setItems(gamesData);
    }
}
