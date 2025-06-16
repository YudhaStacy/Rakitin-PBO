/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package rakitin;

import koneksi.Koneksi;

/**
 *
 * @author Yudaaa
 */
public class Rakitin {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Koneksi db = new Koneksi();
        db.connect(); // akan menampilkan log di console
    }
    
}
