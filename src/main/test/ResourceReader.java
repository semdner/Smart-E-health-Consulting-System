import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceReader {
    /**
     * Reads a requested resource and returns its content as string
     * @param resourceName the name of the resource to be read
     * @return text content of the resource as string
     */
    public static String getResourceString(String resourceName) {
        InputStream inputStream = ResourceReader.class.getResourceAsStream(resourceName);
        try {
            return CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return "";
        }
    }
}
