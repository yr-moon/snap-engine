package org.esa.snap.esasnappy;


import org.esa.snap.core.util.SystemUtils;
import org.esa.snap.core.util.io.TreeDeleter;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

/**
 * @author Olaf Danne
 */
public class PyBridgeTest {

    Path snapPythonPath;
    Path snappyCustomPath;

    @Before
    public void before() throws Exception {
        snappyCustomPath = null;
        final Path snapApplicationPath = SystemUtils.getApplicationDataDir(true).toPath();
        snapPythonPath = snapApplicationPath.resolve(PyBridge.SNAP_PYTHON_DIRNAME);
    }

    @After
    public void after() throws Exception {
        deleteSnappyTestDir(snapPythonPath);
    }

    @Test
    @Ignore
    public void testInstallPythonModule() throws Exception {

        // This test is ignored by default. If needed, remove @Ignore and set correct paths,
        // i.e. the directory of current SNAP installation.
        // Also, make sure that environment variable PYTHONHOME is set and points to your
        // Python installation.
        final String pythonExec = System.getProperty("os.name").startsWith("Windows") ? "python.exe" : "python";
        final Path pythonExecPath = getPythonExecPath(pythonExec);

        final String snappyCustomDirName = System.getProperty("user.home") + File.separator + "snap-python-TEST";
        final Path snappyCustomPath = Paths.get(snappyCustomDirName);
        if (!Files.isDirectory(snappyCustomPath)) {
            final File filePath = new File(snappyCustomDirName);
            filePath.mkdir();
        }
        assertTrue(Files.isDirectory(snappyCustomPath));

        // set this one properly, e.g.
        // final String snapApplDir = "/home/<user>/snap-snapshots/10.0-snapshot/snap";
        final String snapApplDir = "D:\\olaf\\bc\\snap-snapshots\\10\\10.0-snapshot\\snap";
        final Path snapApplPath = Paths.get(snapApplDir);
        PyBridge.installPythonModule(pythonExecPath, snappyCustomPath, snapApplPath, true);

        final Path snappyPropertiesFilePath = Paths.get(snapPythonPath + File.separator + PyBridge.SNAPPY_PROPERTIES_NAME);
        assertTrue(Files.isRegularFile(snappyPropertiesFilePath));
        assertTrue(Files.isDirectory(Paths.get(snappyCustomPath + File.separator + PyBridge.SNAPPY_NAME)));

    }

    private static Path getPythonExecPath(String pythonExec) {
        //
        final String pythonExecPath = System.getenv("PYTHONHOME") + File.separator + pythonExec;
        try {
            Runtime.getRuntime().exec(pythonExecPath + " --version");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No Python executable found, test is ignored. " +
                    "Make sure Python is installed and PYTHONHOME environment variable is set properly.");
            return null;
        }
        return Paths.get(pythonExecPath);
    }

    private static void deleteSnappyTestDir(Path snapPythonPath) throws IOException {
        if (snapPythonPath != null && Files.isDirectory(snapPythonPath)) {
            TreeDeleter.deleteDir(snapPythonPath);
        }
    }

}
