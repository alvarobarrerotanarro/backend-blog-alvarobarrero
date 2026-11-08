package com.alvarobarrero.api;

import java.sql.SQLException;
import java.util.List;

import com.alvarobarrero.dao.Article;
import com.alvarobarrero.dao.ArticleDAO;
import com.alvarobarrero.dao.DatabaseConnection;

import io.javalin.http.Context;

public class ArticleController {
	private static final ArticleDAO dao;

	static {
		System.out.println("Loading class ArticleController.");

		dao = new ArticleDAO(DatabaseConnection.get());
		try {
			System.out.println("Creating table articles.");
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
			ctx.status(500);
			ctx.json("error: " + e.getMessage());
		}
	}

	public static void getArticle(Context ctx) {
		String urlPath = ctx.pathParam("urlPath");
		try {
			Article article = dao.findByUrlPath(urlPath);

			if (article == null) {
				ctx.status(404);
				ctx.json("error: not found");
			} else {
				ctx.json(article);
			}

		} catch (SQLException e) {
			ctx.status(500);
			ctx.json("error: " + e.getMessage());
		}

	}

	public static void postArticle(Context ctx) {
		Article a = ctx.bodyAsClass(Article.class);
		try {
			Article search = dao.findByUrlPath(a.getUrlPath());
			if (search != null) {
				ctx.status(400);
				ctx.json("error: duplicated url path");
			} else {
				dao.insert(a);
				int id = dao.findByUrlPath(a.getUrlPath()).getId();
				ctx.json(id);
			}
		} catch (SQLException e) {
			ctx.status(500);
			ctx.json("error: " + e.getMessage());
		}
	}

	public static void patchArticle(Context ctx) {
		Article a = ctx.bodyAsClass(Article.class);
		try {
			dao.update(a);
			ctx.json(a);
		} catch (SQLException e) {
			ctx.status(500);
			ctx.json("error: " + e.getMessage());
		}
	}

	public static void deleteArticle(Context ctx) {
		int id = ctx.pathParamAsClass("id", Integer.class).get();
		try {
			dao.deleteById(id);
			ctx.json(id);
		} catch (SQLException e) {
			ctx.status(500);
			ctx.json("error: " + e.getMessage());
		}
	}
}
