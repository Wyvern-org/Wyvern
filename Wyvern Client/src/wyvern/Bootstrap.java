package wyvern;

import wyvern.util.FileUtils;
import wyvern.util.OS;
import wyvern.util.Util;
import wyvern.util.ZipUtil;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

/**
 * this class will bootstrap the new javafx UI
 */
public class Bootstrap
{
    public static void main(String[] args)
    {
        System.out.println("Wyvern bootstrap started");

        try
        {

            new File(Util.getInstallDirectory()).mkdirs();
            File tmp = new File(Util.getInstallDirectory(), "temp/");
            File runtimeFile = new File(tmp, "fx-runtime.zip");
            File runtimeDir = new File(Util.getInstallDirectory(), "java/fx-runtime/");
            File currentJar = new File(Bootstrap.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (!currentJar.getName().endsWith(".jar")) // tf am i supposed to do without a jar?
                throw new RuntimeException("Runtime code-source URI location does not point to a JAR file.");
            System.out.println("Current JAR: " + currentJar.getAbsolutePath());
            tmp.mkdirs();
            runtimeDir.mkdirs();
            if (runtimeDir.exists() && runtimeDir.isDirectory() && Objects.requireNonNull(runtimeDir.listFiles()).length == 0)
            {
                System.out.println("Downloading JavaFX runtime files...");
                String os = OS.getOS().name().toLowerCase();
                //TODO: probably should be replaced with wyvern URL or smth
                FileUtils.copyURLToFile("https://os-mc.net/launcher/dl/fx-runtime-" + os + ".zip", runtimeFile);
                ZipUtil.extractAllTo(runtimeFile.getAbsolutePath(), runtimeDir.getAbsolutePath());
                System.out.println("Finished downloading JavaFX runtime files.");
            }
            String[] cmd = new String[]
                    {
                            "java", //TODO: if possible, we should attempt to locate the correct java version via disk. the OSM launcher has the same issue, so people with more than one version of java sometimes can't run it due to a messed up environment variable
                            "--module-path",
                            new File(runtimeDir, "lib/").getAbsolutePath(),
                            "--add-modules",
                            "javafx.controls,javafx.fxml,javafx.graphics",
                            "-cp",
                            currentJar.getAbsolutePath(),
                            "wyvern.Client"
                    };
            System.out.println("Executing: " + Arrays.toString(cmd));
            Runtime.getRuntime().exec(cmd);
            FileUtils.deleteDirectory(tmp);
            System.out.println("Wyvern bootstrap finished, exiting...");
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            notify(ex.getMessage(), "Bootstrapping failed");
        }
    }

    private static void notify(String msg, String title)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.WARNING_MESSAGE);
    }
}
