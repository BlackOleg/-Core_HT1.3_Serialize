import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        String saveDir = "C:\\Games\\savegames\\";
        List<GameProgress> games = new ArrayList<>();
        List<String> listFiles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            games.add(new GameProgress(new Random().nextInt(100),
                    new Random().nextInt(10),
                    new Random().nextInt(20),
                    new Random().nextInt(1000)));
            //Запомним все имена
            listFiles.add(saveDir + "game" + i + ".dat");
            //Запишем игру...
            saveGame(saveDir + "game" + i + ".dat", games.get(i));
        }
        // в ZIP с удалением
        zipFiles(saveDir + "games_packed.zip", listFiles, true);
        System.out.println("Файлы записаны и упакованы!");
    }

    static void saveGame(String file, GameProgress game) {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            // запишем экземпляр класса в файл
            oos.writeObject(game);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static void zipFiles(String outFile, List<String> files, boolean delMark) {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outFile))) {
            for (String filePath : files) {
                File fileToZip = new File(filePath);
                zipOut.putNextEntry(new ZipEntry(fileToZip.getName()));
                Files.copy(fileToZip.toPath(), zipOut);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (delMark) {
            boolean isFileDeleted;
            for (String filePath : files) {
                try {
                    isFileDeleted = new File(filePath).delete();
                    if (!isFileDeleted) System.out.println("Sorry, unable to delete the file!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
