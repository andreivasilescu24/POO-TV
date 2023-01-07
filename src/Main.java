import platform.Platform;
import input.data.Input;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;


public class Main {
    /**
     * @param args contains the paths to the input and output files
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        // get input and output file paths
        String inputFilePath = args[0];
        String outputFilePath = args[1];

        ObjectMapper objectMapper = new ObjectMapper();
        Input input = objectMapper.readValue(new File(inputFilePath), Input.class);
        ArrayNode output = objectMapper.createArrayNode();

        Platform platform = Platform.getPlatformInstance();
        platform.initPermissions();
        platform.setOutput(output);
        platform.setInputData(input);

        platform.initPlatform();

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(outputFilePath), output);
    }
}

