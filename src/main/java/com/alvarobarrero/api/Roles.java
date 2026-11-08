package com.alvarobarrero.api;

import io.javalin.security.RouteRole;

public enum Roles implements RouteRole {
	ARTICLE_READ, ARTICLE_WRITE
}
