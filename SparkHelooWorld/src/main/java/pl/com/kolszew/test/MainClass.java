package pl.com.kolszew.test;


import spark.Spark;

public class MainClass {

	public static void main(String[] args) {
		Spark.before("/app/*", (req, res) -> {
			String user = req.session().attribute("user");
			if (user == null) {
				req.session().attribute("before-login", req.pathInfo());
				res.redirect("/login", 302);
			}
		});
		
		Spark.get("/", (req, res) -> {
			req.session(true);
			req.session().invalidate();
			return "<html><h1>Weclome</h1><div><a href=\"/app/hello\">go to hello</a></div><div><a href=\"/app/hello/Filip\">go to hello Filip</a></div></html>";
		});

		Spark.get("/login", (req, res) -> {
			return "<html><form action=\"login-user\" method=\"post\">"
				   + "Login <input name=\"login\" /><br/>" 		
				   + "Hasło <input name=\"password\" /><br/>"
				   + "<input type=\"submit\" value=\"Logowanie\" /> "				   
					+ "</html></html>";
		}); 

		
		Spark.post("/login-user", (req, res) -> {
			String login = req.queryParams("login");
			String password = req.queryParams("password");
			if ("admin".equals(login) && "admin".equals(password)) {
				req.session().attribute("user", login);
				String beforeLogin = req.session().attribute("before-login");
				if (beforeLogin != null) {
					res.redirect(beforeLogin);
				}
			} else {
				res.redirect("/bad-login");
			}
			return "";
		});

		Spark.get("/bad-login", (req, res) -> {
			return "<html><h3>Niepoprawny login lub hasło</h3><div><a href=\"/login\" >Logowanie</a></div></html>";
		}); 
		
		Spark.get("/app/hello/:name", (req, res) -> "<html><h1>Hello world</h1><div>" + req.params(":name") + "</div><div>"
				+ System.nanoTime() + "</div></html>");
		
		Spark.get("/app/hello", (req, res) -> "<html><h1>Hello world</h1><div>" + System.nanoTime() + "</div><div>"
				+ System.nanoTime() + "</div></html>");
	}

}
