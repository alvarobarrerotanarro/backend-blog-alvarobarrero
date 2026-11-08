package com.alvarobarrero;

import io.javalin.Javalin;

public class HelloWorld {
	public static void main(String[] args) {
		Javalin server = Javalin.create(config -> {
			config.routes.get("/", ctx -> {
				ctx.redirect("/articles");
			});
			
			config.routes.get("/articles", ArticleController::getArticles);
		}).start(3000);
	}

}
