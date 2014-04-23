package org.profwell.generic.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ValidationContext {

    private Map<String, List<ValidationMessage>> messages =
            new HashMap<String, List<ValidationMessage>>();

    public List<ValidationMessage> getAllMessages() {
        List<ValidationMessage> result = new ArrayList<>();

        for (Entry<String, List<ValidationMessage>> e : this.messages.entrySet()) {
            result.addAll(e.getValue());
        }

        return Collections.unmodifiableList(result);
    }

    public boolean existForSource(String source) {
        return this.messages.get(source) != null;
    }

    public List<ValidationMessage> getForSource(String source) {
        List<ValidationMessage> result = this.messages.get(source);
        if (result == null) {
            result = Collections.emptyList();
        }
        return Collections.unmodifiableList(result);
    }

    public void add(ValidationMessage message) {
        List<ValidationMessage> scope = this.messages.get(message.getSource());

        if (scope == null) {
            scope = new ArrayList<>();
            this.messages.put(message.getSource(), scope);
        }

        scope.add(message);
    }

    public void add(String source, String messageText) {

        ValidationMessage message =
                new ValidationMessageBuilder().source(source)
                        .message(messageText).build();

        List<ValidationMessage> scope = this.messages.get(message.getSource());

        if (scope == null) {
            scope = new ArrayList<>();
            this.messages.put(message.getSource(), scope);
        }

        scope.add(message);
    }

    public boolean isEmpty() {
        return this.messages.isEmpty();
    }

}
