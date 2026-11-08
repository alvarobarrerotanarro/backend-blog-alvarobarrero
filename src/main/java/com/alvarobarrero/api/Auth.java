package com.alvarobarrero.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.javalin.http.Context;
import io.javalin.http.Header;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.security.BasicAuthCredentials;
import io.javalin.security.RouteRole;

class Credentials {
	public String name;
	public String password;

	public Credentials(String name, String password) {
		this.name = name;
		this.password = password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Credentials other = (Credentials) obj;
		return Objects.equals(name, other.name) && Objects.equals(password, other.password);
	}

}

public class Auth {
	private static Map<Credentials, Set<RouteRole>> rolesMap;

	static {
		String user = System.getenv("ADMIN_USR");
		String pwd = System.getenv("ADMIN_PWD");
		rolesMap = new HashMap<>();
		rolesMap.put(new Credentials(user, pwd), Set.of(Roles.ARTICLE_WRITE, Roles.ARTICLE_READ));

		rolesMap.put(new Credentials("guest", ""), Set.of(Roles.ARTICLE_READ));
	}

	private static Credentials userCredentials(Context ctx) {
		BasicAuthCredentials httpCredentials = ctx.basicAuthCredentials();

		if (httpCredentials == null) {
			return new Credentials("guest", "");
		}
		Credentials credentials = new Credentials(httpCredentials.getUsername(), httpCredentials.getPassword());
		return credentials;
	}

	public static void handleAccess(Context ctx) {
		Credentials credentials = userCredentials(ctx);
		Set<RouteRole> userRoles = rolesMap.getOrDefault(credentials, Set.of(Roles.ARTICLE_READ));
		Set<RouteRole> acceptedRoles = ctx.routeRoles();

		boolean matchAny = false;
		for (RouteRole role : userRoles) {
			if (acceptedRoles.contains(role)) {
				matchAny = true;
			}
		}

		if (matchAny) {
			return;
		}

		ctx.header(Header.WWW_AUTHENTICATE, "Basic");
		throw new UnauthorizedResponse();
	}
}
