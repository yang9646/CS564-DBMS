package gui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * UW-Madison, CS 564, Summer 2020, Team 8 Final Project: Game Ranking Database
 * GUI/Front End by @author Nate Sackett
 * Requires JavaFX and mysql-connector; FXML GUI design implemented using SceneBuilder
 * 
 * Game represents a game in the Game Rank Database; used in Controller Class
 */
public class Game {

	public LongProperty gameRank = new SimpleLongProperty();
	public LongProperty gid = new SimpleLongProperty();
	public StringProperty gameName = new SimpleStringProperty();
	public StringProperty platform = new SimpleStringProperty();
	public StringProperty year = new SimpleStringProperty();
	public StringProperty genre = new SimpleStringProperty();
	public StringProperty publisher = new SimpleStringProperty();
	public DoubleProperty naSales = new SimpleDoubleProperty();
	public DoubleProperty euSales = new SimpleDoubleProperty();
	public DoubleProperty jpSales = new SimpleDoubleProperty();
	public DoubleProperty otherSales = new SimpleDoubleProperty();
	public DoubleProperty globalSales = new SimpleDoubleProperty();

	public Game(long gameRank, long gid, String gameName, String platform, String year, String genre, String publisher, double naSales,
			double euSales, double jpSales, double otherSales, double globalSales) {
		this.gameRank.set(gameRank);
		this.gid.set(gid);
		this.gameName.set(gameName);
		this.platform.set(platform);
		this.year.set(year);
		this.genre.set(genre);
		this.publisher.set(publisher);
		this.naSales.set(naSales);
		this.euSales.set(euSales);
		this.jpSales.set(jpSales);
		this.otherSales.set(otherSales);
		this.globalSales.set(globalSales);
	}
	
	public Game() {}
	
	public LongProperty gameRankProperty() {
		return gameRank;
	}
	
	public LongProperty gidProperty() {
		return gid;
	}
	
	public StringProperty gameNameProperty() {
		return gameName;
	}
	
	public StringProperty platformProperty() {
		return platform;
	}
	
	public StringProperty yearProperty() {
		return year;
	}
	
	public StringProperty genreProperty() {
		return genre;
	}
	
	public StringProperty publisherProperty() {
		return publisher;
	}
	
	public DoubleProperty naSalesProperty() {
		return naSales;
	}
	
	public DoubleProperty euSalesProperty() {
		return euSales;
	}
	
	public DoubleProperty jpSalesProperty() {
		return jpSales;
	}
	
	public DoubleProperty otherSalesProperty() {
		return otherSales;
	}
	
	public DoubleProperty globalSalesProperty() {
		return globalSales;
	}
	
}
