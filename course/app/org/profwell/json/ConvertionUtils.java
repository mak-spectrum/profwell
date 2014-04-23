package org.profwell.json;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.profwell.generic.validation.ValidationContext;
import org.profwell.generic.validation.ValidationMessage;

public final class ConvertionUtils {

    private ConvertionUtils() {
        // Nothing here
    }

    public static JsonNode convertToJson(ValidationContext context) {
        ObjectNode jsonContext = new ObjectNode(JsonNodeFactory.instance);

        for (ValidationMessage message : context.getAllMessages()) {
            ArrayNode array = (ArrayNode) jsonContext
                    .get(message.getSource());

            if (array == null) {
                array = new ArrayNode(JsonNodeFactory.instance);
                jsonContext.put(message.getSource(), array);
            }

            array.add(convertToJson(message));
        }

        return jsonContext;
    }

    private static JsonNode convertToJson(ValidationMessage message) {
        ObjectNode object = new ObjectNode(JsonNodeFactory.instance);

        object.put("source", message.getSource());
        object.put("severity", message.getSeverity().name().toLowerCase());
        object.put("message", message.getMessage());

        return object;
    }

}
