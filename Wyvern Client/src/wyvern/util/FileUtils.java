package wyvern.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * these are actually functions from commons IO, but i asked chatgpt to write me alternatives so I didn't have to import the library
 */
public class FileUtils
{
    public static void copyURLToFile(String urlStr, File file) throws IOException
    {
        URL url = new URL(urlStr);
        BufferedInputStream in = null;
        FileOutputStream out = null;

        try
        {
            in = new BufferedInputStream(url.openStream());
            out = new FileOutputStream(file);

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1)
                out.write(dataBuffer, 0, bytesRead);
        } finally {
            if (in != null)
            {
                try
                {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        Path directoryPath = directory.toPath();

        try {
            Files.walk(directoryPath)
                    .sorted((path1, path2) -> -path1.compareTo(path2))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
