public class Main {
    public static void main(String[] args) {
        BMP bmp = new BMP("src/files/RGB.bmp");
        bmp.readHeader();
        bmp.removeColorComponent('R');
        bmp.removeColorComponent('G');
        bmp.removeColorComponent('B');
    }
}