
import java.io.*;
import java.util.*;

public class Descompressor {
    private Map<String, Character> letra = new HashMap<>();
    private Map<Character, String> codigo = new HashMap<>();
    private String palavras, map, translation;

    public Descompressor(String palavras, String map, String translation) throws IOException {
        this.palavras = palavras;
        this.map = map;
        this.translation = translation;
    }


    public Map<Character, String> getCodigo() {
        return codigo;
    }


    public void pegaTabela() throws IOException { // Responsável por receber a tabela como entrada e fazer o tratamento de dados

        FileInputStream dicionario = new FileInputStream(map);
        BufferedInputStream leitor = new BufferedInputStream(dicionario);

        byte line[] = leitor.readAllBytes();

        String arquivo = new String(line, "UTF8");

        String[] codigo = arquivo.split(String.valueOf((char) 351));
        int i = 0;
        while (i < codigo.length) {              // Tratamento da codificação obtida pela compressão
            letra.put(codigo[i + 1], codigo[i].charAt(0));
            this.codigo.put(codigo[i].charAt(0), codigo[i + 1]);
            i += 2;
        }
        dicionario.close();
    }


    public void decodificaTexto() throws IOException { // Responsável pela decodificação da mensagem

        FileInputStream carry = new FileInputStream(palavras);

        byte[] bytes = carry.readAllBytes();
        String str = "";
        String aux;
        for (int i = 0; i < bytes.length; i++) {
            aux = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(" ", "0"); // Convertendo bytes em binário

            for (int j = (aux.length() -1) ; j >=  0 ;j--){
                str+=aux.charAt(j);
            }

        }
        String comparator = "";
        FileWriter cpy = new FileWriter(translation); //

        for (int j = 0; j < str.length(); j++) {
            comparator += str.charAt(j);
            if (codigo.containsValue(comparator)) {
                    cpy.write(letra.get(comparator));
                    comparator = "";
            }
        }
        cpy.close();
    }
}






