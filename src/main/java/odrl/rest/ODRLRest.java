package odrl.rest;

import odrl.rest.controller.PrivacyPolicyController;
import odrl.rest.exception.Exceptions;
import odrl.rest.exception.InternalServiceException;
import odrl.rest.exception.InvalidRequestException;
import odrl.rest.exception.RenderTemplateException;
import odrl.rest.exception.RepositoryException;
import odrl.rest.view.VelocityRenderer;

import com.github.jsonldjava.shaded.com.google.common.collect.Maps;

import static spark.Spark.exception;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.delete;
import static spark.Spark.put;
import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class ODRLRest {

	// -- Attributes
	private static VelocityRenderer render = new VelocityRenderer();
	// -- Main

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		 // Configuration
		configure();
		staticFileLocation("/");
		get("/", (req, res) -> {
			return render.render(Maps.newHashMap(), "index.html");
		});
		get("", (req, res) -> {
			return render.render(Maps.newHashMap(), "index.html");
		});

        path("/api", () -> {
			get("/", PrivacyPolicyController.list);
			get("", PrivacyPolicyController.list);
	        get("/:id", PrivacyPolicyController.get);
			post("/policy/:id", PrivacyPolicyController.apply);
			post("/:id", PrivacyPolicyController.create);
	        delete("/:id", PrivacyPolicyController.remove);
	    });
        
        // Exceptions
        exception(InternalServiceException.class, InternalServiceException.handle);
		exception(RenderTemplateException.class, RenderTemplateException.handle);
        exception(InvalidRequestException.class, InvalidRequestException.handle);
        exception(RepositoryException.class, RepositoryException.handle);
        exception(Exception.class, Exceptions.handleException);
	}

	private static void configure() {

	}
	
}
