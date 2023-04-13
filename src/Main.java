import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {

        // Создать три экземпляра класса GameProgress.
        GameProgress[] gameProgresses = new GameProgress[]{
                new GameProgress(1, 1, 1, 0.0),
                new GameProgress(700, 8, 20, 2000.0),
                new GameProgress(10_000, 100, 80, 8888.88)
        };

        File saveDirectory = new File("D:\\Games\\savegames\\"); // Текущая рабочая директория с файлами сохранения

        // Сохранить сериализованные объекты GameProgress в папку savegames из предыдущей задачи.
        for (GameProgress player : gameProgresses) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(saveDirectory + "\\" + player.hashCode() + ".player");
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                objectOutputStream.writeObject(player);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        File[] directoryFiles = saveDirectory.listFiles();

        // Созданные файлы сохранений из папки savegames запаковать в архив zip.
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try (FileOutputStream fos = new FileOutputStream(saveDirectory + "\\save.zip")) {
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for (File file : directoryFiles) {
                if (file.getAbsolutePath().endsWith(".player")) {
                    fis = new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(file.getName());
                    System.out.println("Zipping the save file: " + file.getName());
                    zipOut.putNextEntry(ze);
                    byte[] tmp = new byte[8 * 1024];
                    int size = 0;
                    while ((size = fis.read(tmp)) != -1) {
                        zipOut.write(tmp, 0, size);
                    }
                    zipOut.flush();
                    fis.close();
                }
            }
            zipOut.close();
            System.out.println("Done... Zipped the all saves");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Удалить файлы сохранений, лежащие вне архива.
        if (directoryFiles != null) {
            for (File file : directoryFiles) {
                if (file.getAbsolutePath().endsWith(".player")) {
                    file.delete();
                }
            }
        }
    }
}
