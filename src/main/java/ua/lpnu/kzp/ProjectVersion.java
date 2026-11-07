package ua.lpnu.kzp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Надає версію програми, визначену в pom.xml.
 */
public final class ProjectVersion {

    private static final String RESOURCE = "/version.properties";

    private ProjectVersion() {
        // Utility class.
    }

    /**
     * Повертає версію проєкту.
     *
     * @return версія з pom.xml
     */
    public static String get() {
        Properties properties = new Properties();

        try (InputStream input =
                     ProjectVersion.class.getResourceAsStream(RESOURCE)) {

            if (input == null) {
                throw new IllegalStateException(
                        "Не знайдено version.properties");
            }

            properties.load(input);

            String version = properties.getProperty("app.version");

            if (version == null || version.isBlank()) {
                throw new IllegalStateException(
                        "Не вдалося визначити версію програми");
            }

            return version;

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Не вдалося прочитати версію програми",
                    exception);
        }
    }
}