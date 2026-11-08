package com.alvarobarrero.api;

import io.javalin.Javalin;
import io.javalin.community.ssl.SslPlugin;

public class Main {
	public static void main(String[] args) {
		try {
			Class.forName("com.alvarobarrero.dao.DatabaseConnection");
		} catch (ClassNotFoundException e) {
			System.out.println("Could not load database connection class: " + e.getMessage());
			System.exit(1);
		}

		int appPort = Integer.parseInt(System.getenv("APP_PORT"));
		Javalin server = Javalin.create(config -> {
			config.registerPlugin(new SslPlugin(ssl -> {
				String certFile = System.getenv("CERT_FILE");
				String privateKeyFile = System.getenv("PRIVATE_KEY_FILE");

				ssl.securePort = appPort;
				ssl.sniHostCheck = false;
				ssl.pemFromPath(certFile, privateKeyFile);
			}));

			config.routes.beforeMatched(Auth::handleAccess);
			config.routes.get("/", ctx -> {
				ctx.redirect("/articles");
			});
			config.routes.get("/articles", ArticleController::getArticles, Roles.ARTICLE_READ);
			config.routes.get("/articles/{urlPath}", ArticleController::getArticle, Roles.ARTICLE_READ);
			config.routes.post("/articles", ArticleController::postArticle, Roles.ARTICLE_WRITE);
			config.routes.patch("/articles", ArticleController::patchArticle, Roles.ARTICLE_WRITE);
			config.routes.delete("/articles/{id}", ArticleController::deleteArticle, Roles.ARTICLE_WRITE);

		}).start(appPort);
	}

}
