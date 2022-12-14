package odrl.rest.controller;

import java.awt.desktop.SystemSleepEvent;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import odrl.lib.OdrlLib;
import odrl.lib.Policies;
import odrl.lib.operands.Time;
import odrl.rest.exception.InvalidRequestException;
import odrl.rest.model.PolicyDocument;
import odrl.rest.persistence.Repository;
import org.apache.jena.atlas.json.io.parser.JSONParser;
import org.hibernate.internal.build.AllowSysOut;

import spark.Request;
import spark.Response;
import spark.Route;

public class PrivacyPolicyController {

	private static Repository<PolicyDocument> repository = new Repository<>(PolicyDocument.class);

	public static final Route list = (Request request, Response response) -> {
		response.status(200);
		response.type("application/json");
		JsonArray array = new JsonArray();
		repository.retrieve().parallelStream().map(doc -> toIdJson(doc.getId())).forEach(elem -> array.add(elem));
		return array;
	};

	private static JsonObject toIdJson(String id) {
		JsonObject object = new JsonObject();
		object.addProperty("id", id);
		object.addProperty("url", "/api/" + id);
		return object;
	}

	public static final Route get = (Request request, Response response) -> {
		String id = fetchId(request);
		Optional<PolicyDocument> documentOpt = repository.retrieve(id);
		if (documentOpt.isPresent()) {
			response.status(200);
			response.type("text/turtle");
			return documentOpt.get().policyDocument;
		} else {
			response.status(404);
			return "";
		}
	};

	public static final Route create = (Request request, Response response) -> {
		String id = fetchId(request);
		String body = request.body();
		if (body.isBlank())
			throw new InvalidRequestException("An ODRL policy must be provided in the body");
		PolicyDocument doc = new PolicyDocument(id, body);
		response.status(201);
		if (repository.exists(id)) {
			response.status(204);
			repository.delete(id);
		}
		try {
			repository.persist(doc);
			return doc;
		} catch (Exception e) {
			throw new InvalidRequestException(e.toString());
		}
	};

	public static final Route remove = (Request request, Response response) -> {
		String id = fetchId(request);
		if (repository.exists(id)) {
			repository.delete(id);
			response.status(200);
		} else {
			response.status(404);
		}
		return "";
	};

	public static final Route apply = (Request request, Response response) -> {
		String id = fetchId(request);
		JsonObject body = new Gson().fromJson(request.body(), JsonObject.class);		
		if (body == null || !body.isJsonObject()) {
			throw new InvalidRequestException("Provide a valid format argument: JSON");
		}
		// Retrieve
		Optional<PolicyDocument> documentOpt = repository.retrieve(id);
		if (documentOpt.isPresent()) {
			OdrlLib odrl = new OdrlLib();
			JsonObject policy = Policies.fromJsonld11String(repository.retrieve(id).get().getPolicyDocument());
			odrl.registerPrefix("ops","http://upm.es/operands#");
			odrl.register("ops",new Time());		
			try {
				Map<String, List<String>> result = odrl.solve(policy);
				return result.get(body.get("target").getAsString()).get(0).equals(body.get("action").getAsString());
			}catch(Exception e) {
				return false;
			}
		} else {
			throw new InvalidRequestException("The id belongs to no privacy document");
		}
	};

	protected static final String fetchId(Request request) {
		String id = request.params("id");
		if (id == null || id.isEmpty())
			throw new InvalidRequestException("Missing valid Component id");
		return id;
	}
}
