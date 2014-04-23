package org.profwell.conf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.AbstractResource;

import play.libs.Json;

public class EnvironmentResource extends AbstractResource {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(EnvironmentResource.class);

    private static InputStream environmentResourceContent;

    static {
        try {
            String mysqlCredentionals = System.getenv("VCAP_SERVICES");

            JsonNode rootNode = Json.parse(mysqlCredentionals);

            JsonNode mySql = rootNode.get(rootNode.getFieldNames().next());
            JsonNode mySqlCred = mySql.get(0).get("credentials");

            String driverClassName = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://"
                    + mySqlCred.get("hostname").asText()
                    + ":"
                    + mySqlCred.get("port").asText()
                    + "/"
                    + mySqlCred.get("name").asText();

            String user = mySqlCred.get("user").asText();
            String password = mySqlCred.get("password").asText();

            Properties props = new Properties();
            props.setProperty("jdbc.driverClassName", driverClassName);
            props.setProperty("jdbc.url", url);
            props.setProperty("jdbc.username", user);
            props.setProperty("jdbc.password", password);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            props.store(baos, "");

            environmentResourceContent =
                    new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException ioe) {
            LOGGER.error("Writing properties error.", ioe);
        }

    }

    @Override
    public String getDescription() {
        return "Single environment resource.";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return environmentResourceContent;
    }

}
