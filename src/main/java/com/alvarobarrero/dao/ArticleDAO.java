package com.alvarobarrero.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {
	private Connection conn;

	public ArticleDAO(Connection conn) {
		this.conn = conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public boolean createTable() throws SQLException {
		DatabaseMetaData metadata = conn.getMetaData(); 
		ResultSet result = metadata.getTables(null, null, "articles", null);
		if (!result.next()) {
			String sql = "CREATE TABLE IF NOT EXISTS articles (" + "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "    title TEXT NOT NULL," + "    summary TEXT," + "    body TEXT NOT NULL," + "    tags TEXT,"
					+ "    category TEXT," + "    reading_minutes INTEGER DEFAULT 0," + "    meta_description TEXT,"
					+ "    seo_keywords TEXT," + "    url_path TEXT UNIQUE NOT NULL,"
					+ "    status TEXT DEFAULT 'DRAFT'," + "    views_count INTEGER DEFAULT 0,"
					+ "    likes_count INTEGER DEFAULT 0," + "    shares_count INTEGER DEFAULT 0,"
					+ "    time_created DATETIME DEFAULT CURRENT_TIMESTAMP,"
					+ "    last_modification DATETIME DEFAULT CURRENT_TIMESTAMP" + ");";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();
			
			return true;
		}
		return false;
	}

	public boolean insert(Article a) throws SQLException {
		String sql = "INSERT INTO articles (" + "    title, summary, body, tags, category, reading_minutes, "
				+ "    meta_description, seo_keywords, url_path, status, "
				+ "    views_count, likes_count, shares_count, time_created, last_modification"
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement stmt = conn.prepareStatement(sql);

		stmt.setString(1, a.getTitle());
		stmt.setString(2, a.getSummary());
		stmt.setString(3, a.getBody());

		String tagsJoined = (a.getTags() != null) ? String.join(",", a.getTags()) : "";
		stmt.setString(4, tagsJoined);

		stmt.setString(5, a.getCategory());
		stmt.setInt(6, a.getReadingMinutes());
		stmt.setString(7, a.getMetaDescription());
		stmt.setString(8, a.getSeoKeywords());
		stmt.setString(9, a.getUrlPath());

		stmt.setString(10, a.getStatus() != null ? a.getStatus().name() : "DRAFT");

		stmt.setInt(11, a.getViewsCount());
		stmt.setInt(12, a.getLikesCount());
		stmt.setInt(13, a.getSharesCount());

		stmt.setTimestamp(14, a.getTimeCreated() != null ? new Timestamp(a.getTimeCreated().getTime()) : null);
		stmt.setTimestamp(15,
				a.getLastModification() != null ? new Timestamp(a.getLastModification().getTime()) : null);

		int affectedRows = stmt.executeUpdate();
		stmt.close();

		return affectedRows > 0;
	}

	private Article makeFromResultSet(ResultSet result) throws SQLException {
		Article article = new Article();

		article.setId(result.getInt("id"));
		article.setTitle(result.getString("title"));
		article.setSummary(result.getString("summary"));
		article.setBody(result.getString("body"));

		String stringTags = result.getString("tags");
		if (stringTags.trim().length() > 0) {
			article.setTags(new ArrayList<>(List.of(stringTags.split("; "))));
		}

		article.setCategory(result.getString("category"));
		article.setReadingMinutes(result.getInt("reading_minutes"));
		article.setMetaDescription(result.getString("meta_description"));
		article.setSeoKeywords(result.getString("seo_keywords"));
		article.setUrlPath(result.getString("url_path"));

		article.setStatus(ArticleStatus.valueOf(result.getString("status")));

		article.setViewsCount(result.getInt("views_count"));
		article.setLikesCount(result.getInt("likes_count"));
		article.setSharesCount(result.getInt("shares_count"));

		Timestamp timeCreatedTs = result.getTimestamp("time_created");
		article.setTimeCreated(new java.util.Date(timeCreatedTs.getTime()));

		Timestamp lastModificationTs = result.getTimestamp("time_created");
		article.setLastModification(new java.util.Date(lastModificationTs.getTime()));

		return article;
	}

	public ArrayList<Article> find() throws SQLException{
		String sql = "SELECT id, title, summary, body, tags, category, reading_minutes, "
				+ "meta_description, seo_keywords, url_path, status, views_count, "
				+ "likes_count, shares_count, time_created, last_modification;";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		ArrayList<Article> articles = new ArrayList<>();
		ResultSet result = stmt.executeQuery();
		while (result.next()) {
			Article a = makeFromResultSet(result);
			articles.add(a);
		}
		
		stmt.close();
		return articles;
	}
	
	public Article findById(int id) throws SQLException {
		String sql = "SELECT id, title, summary, body, tags, category, reading_minutes, "
				+ "meta_description, seo_keywords, url_path, status, views_count, "
				+ "likes_count, shares_count, time_created, last_modification WHERE id = ?;";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);

		ResultSet result = stmt.executeQuery();
		if (!result.next()) {
			stmt.close();
			return null;
		}

		Article a = makeFromResultSet(result);
		stmt.close();
		return a;
	}

	public List<Article> findByTitle(String title) throws SQLException {
		String sql = "SELECT id, title, summary, body, tags, category, reading_minutes, "
				+ "meta_description, seo_keywords, url_path, status, views_count, "
				+ "likes_count, shares_count, time_created, last_modification WHERE title = ?;";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, title);

		ArrayList<Article> articles = new ArrayList<>();
		ResultSet result = stmt.executeQuery();
		while (result.next()) {
			Article a = makeFromResultSet(result);
			articles.add(a);
		}

		stmt.close();
		return articles;
	}

	public boolean update(Article a) throws SQLException {
		String sql = "UPDATE articles SET " + "    title = ?, " + "    summary = ?, " + "    body = ?, "
				+ "    tags = ?, " + "    category = ?, " + "    reading_minutes = ?, " + "    meta_description = ?, "
				+ "    seo_keywords = ?, " + "    url_path = ?, " + "    status = ?, " + "    views_count = ?, "
				+ "    likes_count = ?, " + "    shares_count = ?, " + "    time_created = ?, "
				+ "    last_modification = ? " + "WHERE id = ?;";

		PreparedStatement stmt = conn.prepareStatement(sql);

		stmt.setString(1, a.getTitle());
		stmt.setString(2, a.getSummary());
		stmt.setString(3, a.getBody());

		String tagsJoined = (a.getTags() != null) ? String.join(",", a.getTags()) : "";
		stmt.setString(4, tagsJoined);

		stmt.setString(5, a.getCategory());
		stmt.setInt(6, a.getReadingMinutes());
		stmt.setString(7, a.getMetaDescription());
		stmt.setString(8, a.getSeoKeywords());
		stmt.setString(9, a.getUrlPath());

		stmt.setString(10, a.getStatus() != null ? a.getStatus().name() : "DRAFT");

		stmt.setInt(11, a.getViewsCount());
		stmt.setInt(12, a.getLikesCount());
		stmt.setInt(13, a.getSharesCount());

		stmt.setTimestamp(14, a.getTimeCreated() != null ? new Timestamp(a.getTimeCreated().getTime()) : null);
		stmt.setTimestamp(15,
				a.getLastModification() != null ? new Timestamp(a.getLastModification().getTime()) : null);

		stmt.setInt(16, a.getId());

		int affectedRows = stmt.executeUpdate();
		stmt.close();

		return affectedRows > 0;
	}

	public boolean deleteById(int id) throws SQLException {
		String sql = "DELETE FROM article WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		int affectedRows = stmt.executeUpdate();

		stmt.close();
		return affectedRows > 0;
	}
}
