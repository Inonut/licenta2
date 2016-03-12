package licenta.util

import javafx.scene.control.Alert
import javafx.scene.image.Image
import licenta.domain.logic.FileImage
import licenta.util.image.impl.ImageFormatter
import org.apache.commons.io.FilenameUtils

import java.nio.file.Files
import java.nio.file.Path

import static javafx.scene.control.Alert.AlertType.CONFIRMATION
import static javafx.scene.control.Alert.AlertType.ERROR
import static licenta.util.BussinesConstants.*

/**
 * Created by Dragos on 08.02.2016.
 */
public class Util {

    public synchronized static Alert confirmMessage(String headerText) {

        Alert alert;
        new Concurrency().runNow = {
            alert = new Alert(CONFIRMATION);
            alert.with {
                it.headerText = headerText;
                it.buttonTypes.clear();
                it.buttonTypes.addAll BUTTON_YES, BUTTON_NO;
            };
        };

        return alert;
    }

    public synchronized static Alert errorMessage(String headerText) {

        Alert alert;
        new Concurrency().runNow = {
            alert = new Alert(ERROR);
            alert.with {
                it.headerText = headerText;
                it.buttonTypes.clear();
                it.buttonTypes.addAll BUTTON_CANCEL_CLOSE;
            };
        };

        return alert;
    }

    public synchronized static double[] generateArray(Random r, int n) {
        def v = new double[n];
        for (int i = 0; i < v.length; i++) {
            v[i] = r.nextDouble() / 10;
        }
        return v;
    }

    public synchronized static List<FileImage> getImagesFrom(Path _path, List<String> _category, int _nrOfImages) {
        return getImagesFrom(_path, _category, _nrOfImages, 0)
    }

    public synchronized
    static List<FileImage> getImagesFrom(Path _path, List<String> _category, int _nrOfImages, int ungle) {
        return Files.list(_path)
                .filter { path -> ImageFormatter.isImage(path.toFile()) }
                .map { path -> [file: path.toFile(), tk: new StringTokenizer(FilenameUtils.getBaseName(path.toFile().getAbsolutePath()), "_").collect()] }
                .map { elem -> [file: elem["file"], type: elem["tk"][0], name: elem["tk"][1], id: elem["tk"][2]] }
                .map { map ->
            if (!map.id || !map.name || !map.type) {
                map.name = map.type ?: "" + map.name ?: "" + map.id ?: ""
                map.id = map.type = null
            }
            return map
        }
        .filter { map ->
            _category == null || map.type == null || _category.stream().map {
                it.toUpperCase()
            }.collect().contains(map.type.toString().toUpperCase())
        }
        .collect()
                .groupBy { it["name"] }
                .collect { _nrOfImages < 0 ? it.value : it.value.take(_nrOfImages) }
                .flatten()
                .stream()
                .map { elem ->
            def fileImage = prepareFileImage(elem["file"] as File, ungle)

            fileImage.with {
                it.name = elem["name"]
                it.id = elem["id"] ? elem["id"] as int : -1
                it.typeImage = elem["type"]
                it.location = elem["file"] as File

            }
            println fileImage
            return fileImage;
        }
        .collect()

    }

    public synchronized static FileImage prepareFileImage(File file) {
        return prepareFileImage(ImageFormatter.convertFileToImage(file), 0)
    }

    public synchronized static FileImage prepareFileImage(Image image) {
        return prepareFileImage(image, 0)
    }

    public synchronized static FileImage prepareFileImage(File file, int ungle) {
        return prepareFileImage(ImageFormatter.convertFileToImage(file), ungle)
    }

    public synchronized static FileImage prepareFileImage(Image image, int ungle) {
        def imageAction = new ImageFormatter()
        def fileImage = new FileImage()
        fileImage.with {
            it.originalImage = imageAction.rotate(image, ungle);
            it.grayImage = imageAction.convertToGray(it.originalImage);

            it.semnificativeImage = imageAction.getSemnificativeImage(it.grayImage);
            it.semnificativeScaledImage = imageAction.scalingImage(it.semnificativeImage, WIDTH, HEIGHT);
            it.semnificativeScaledImageTransformated = imageAction.convertImageToArray(it.semnificativeScaledImage);
        }
        return fileImage
    }

    public synchronized static double[] toDouble(String s) {
        if (s.length() > 0) {
            s.substring(1, s.length() - 1).split(",").collect() as double[]
        } else {
            null
        }
    }

    public synchronized static void showTrainData(List list, int m) {
        list.eachWithIndex { def entry, int i ->
            if (i % m == 0) {
                println()
            }

            print "${entry}".padLeft(4)
        }
    }

}