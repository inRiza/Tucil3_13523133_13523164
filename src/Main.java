import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan Nama File (Namafile): ");
        String filename = scanner.nextLine();
        String filePath = "../test/" + filename + ".txt";

        try {
            File file = new File(filePath);
            Scanner fileScanner = new Scanner(file);

            String[] dimensions = fileScanner.nextLine().split(" ");
            int rows = Integer.parseInt(dimensions[0]);
            int cols = Integer.parseInt(dimensions[1]);
            int jumlahPiece = Integer.parseInt(fileScanner.nextLine());

            char[][] board = new char[rows][cols];
            for (int i = 0; i < rows; i++) {
                String line = fileScanner.nextLine();
                for (int j = 0; j < cols; j++) {
                    board[i][j] = line.charAt(j);
                }
            }

           System.out.println("Ukuran board: " + rows + "x" + cols);
           System.out.println("Jumlah Piece: " + jumlahPiece);
           System.out.println("Isi board:");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    System.out.print(board[i][j]);
                }
                System.out.println();
            }
            fileScanner.close();
            System.out.print("\nApakah anda ingin menyimpan solusi? (y/n): ");
           String savefile = scanner.nextLine();
            
            if (savefile.equalsIgnoreCase("y")) {
                System.out.print("Masukkan nama file (Hasil.txt): ");
                String FileName = scanner.nextLine();
                saveSolution(FileName, rows, cols, jumlahPiece, board);
                System.out.println("Solusi berhasil disimpan ke " + FileName);
            }
           
        } catch (FileNotFoundException e) {
            System.out.println("File tidak ditemukan: " + filePath);
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
       
       scanner.close();
   }

   private static void saveSolution(String fileName, int rows, int cols, int jumlahPiece, char[][] board) {
    try {
        FileWriter writer = new FileWriter("../test/hasil/" + fileName + ".txt" );
    //    writer.write("Waktu pencarian: " + TotalTime + " ms\n");
        writer.write(rows + " " + cols + "\n");
        writer.write(jumlahPiece + "\n");

        // Tulis isi board
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                writer.write(board[i][j]);
            }
            writer.write("\n");
        }
        writer.close();
    } catch (IOException e) {
        System.out.println("Gagal menyimpan solusi: " + e.getMessage());
    }
}
}
   