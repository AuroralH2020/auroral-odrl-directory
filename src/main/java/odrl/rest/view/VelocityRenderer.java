package odrl.rest.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import odrl.rest.exception.RenderTemplateException;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class VelocityRenderer {

	private VelocityTemplateEngine engine;
	private ObjectMapper mapper;

	public VelocityRenderer() {
		engine = new VelocityTemplateEngine();
		mapper = new ObjectMapper();
	}

	public String render(Map<String, Object> model, String template) throws RenderTemplateException {
		try {
			return engine.render(new ModelAndView(model, template));
		} catch (Exception e) {
			throw new RenderTemplateException(e.toString());
		}
	}
	
	public Map<String, Object> toModel(Object object) {
		@SuppressWarnings("unchecked")
		Map<String, Object> model = mapper.convertValue(object, Map.class);
		return model;
	}
	
	public String render(Object object, String template) throws RenderTemplateException {
		return render(toModel(object), template);
	}
	
	public Map<String, Object> toModel(List<?> object, String variable) {
		Map<String, Object> model = new HashMap<>();
		List<Object> values = object.parallelStream().map(elem -> toModel(elem)).collect(Collectors.toList());
		model.put(variable, values);
		return model;
	}
	
}
