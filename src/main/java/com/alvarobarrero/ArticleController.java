package com.alvarobarrero;

import java.sql.SQLException;
import java.util.List;

import com.alvarobarrero.dao.Article;
import com.alvarobarrero.dao.ArticleDAO;

import io.javalin.http.Context;

public class ArticleController {
	private static final ArticleDAO dao;

	static {
		dao = new ArticleDAO(DatabaseConnection.get());
		try {
			dao.createTable();
		} catch (SQLException e) {
			System.err.println("Database schema error: " + e.getMessage());
			System.exit(1);
		}
	}

	public static void getArticles(Context ctx) {
		try {
			List<Article> articles = dao.find();
			ctx.json(articles);
		} catch (SQLException e) {
			ctx.json("error: " + e.getMessage());
		}
	}
}
