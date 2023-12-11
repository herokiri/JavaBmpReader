import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BMP {
    private static final int HEADER_SIZE = 54;
    private static final int INFO_HEADER_SIZE = 40;
    private static final int PIXEL_SIZE = 3;

    private String fileName;

    public BMP(String fileName) {
        this.fileName = fileName;
    }

    public void readHeader() {
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            // Считываем общий заголовок BMP (14 байт)
            byte[] header = new byte[HEADER_SIZE];
            fileInputStream.read(header);

            // Считываем заголовок информации (40 байт)
            byte[] infoHeader = new byte[INFO_HEADER_SIZE];
            fileInputStream.read(infoHeader);

            // Извлекаем необходимую информацию из заголовка
            int fileSize = byteArrayToInt(header, 2);
            int width = byteArrayToInt(header, 18);
            int height = byteArrayToInt(header, 22);
            short bitsPerPixel = byteArrayToShort(header, 28);

            // Выводим информацию
            System.out.println("Размер файла: " + fileSize + " байт");
            System.out.println("Ширина изображения: " + width + " пикселей");
            System.out.println("Высота изображения: " + height + " пикселей");
            System.out.println("Глубина цвета: " + bitsPerPixel + " бит");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void removeColorComponent(char componentToRemove) {
        String outputFileName = fileName.replace(".bmp", "_without_" + componentToRemove + ".bmp");

        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             FileOutputStream fileOutputStream = new FileOutputStream(outputFileName)) {

            // Считываем заголовок BMP (54 байта)
            byte[] header = new byte[HEADER_SIZE];
            fileInputStream.read(header);
            fileOutputStream.write(header);

            // Считываем и обрабатываем каждый пиксель
            int width = byteArrayToInt(header, 18);
            int height = byteArrayToInt(header, 22);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    byte[] pixel = new byte[PIXEL_SIZE];
                    fileInputStream.read(pixel);

                    // Удаляем соответствующий цветовой компонент
                    if (componentToRemove == 'R') {
                        pixel[2] = 0;  // Зануляем красный
                    } else if (componentToRemove == 'G') {
                        pixel[1] = 0;  // Зануляем зеленый
                    } else if(componentToRemove == 'B') {
                        pixel[0] = 0;
                    }

                    // Записываем обработанный пиксель в выходной файл
                    fileOutputStream.write(pixel);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File without " + componentToRemove + " saved as: " + outputFileName);
    }

    private static int byteArrayToInt(byte[] bytes, int offset) {
        return ByteBuffer.wrap(bytes, offset, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    private static short byteArrayToShort(byte[] bytes, int offset) {
        return ByteBuffer.wrap(bytes, offset, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

}
