import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;

public class Navegacao {
    private final int numPortos = 9; // o número de portos que o mapa tem
    Grafo grafo;
    public Navegacao() {
    }
    
    public void start(String arquivo){
        prep(arquivo);
        responde();
    }

    private void prep(String arquivo){

        Path path = Paths.get(arquivo);
        try (BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset())) {
            String line = reader.readLine();
            int altura = Integer.parseInt(line.substring(0,line.indexOf(" ")));
            int largura = Integer.parseInt(line.substring(line.indexOf(" ")+1));
            char[][] matriz = new char[altura][largura];
            for (int i = 0; i<altura; i++){
                line = reader.readLine();
                matriz[i] = line.toCharArray();
            }
            geraGrafo(matriz,altura,largura);
            // debugPrintMap(matriz); // imprime o Mapa lido
            // debugPrintGraph(); // imprime o Grafo gerado
            // debugPrintPositions(altura,largura); // imprime as posiçoes de cada elemento do mapa
        }
        catch (IOException e) {
            System.err.format("Erro");
        }
    }

    private void geraGrafo(char[][] matriz, int altura, int largura){
        grafo = new Grafo(altura, largura, numPortos); // inicializa o grafo
        int v1;
        int v2;

        for(int i=0;i<altura;i++){
            for(int j=0;j<largura;j++){
                if (matriz[i][j] != '*'){
                    v1 = ((i * largura) + j); // posição absoluta do vérice atual no mapa

                    if ((i>0) && (matriz[i-1][j] != '*')){ // verifica a posição de cima do vértice atual
                        v2 = (((i - 1) * largura)+ j);
                        grafo.addAdj(v1,v2);
                    }
                    if ((i<altura-1) && (matriz[i+1][j] != '*')){ // verifica a posição de baixo do vértice atual
                        v2 = (((i + 1) * largura) + j);
                        grafo.addAdj(v1,v2);
                    }
                    if ((j>0) && (matriz[i][j-1] != '*')){ // verifica a posição à esquerda do vértice atual
                        v2 = ((i * largura) + j - 1);
                        grafo.addAdj(v1,v2);
                    }
                    if ((j<largura-1) && (matriz[i][j+1] != '*')){ // verifica a posição à direita do vértice atual
                        v2 = ((i * largura) + j + 1);
                        grafo.addAdj(v1,v2);
                    }


                    // depois das asjacências do vértice terem sido adicionadas
                    if (matriz[i][j] != '.'){ // verifica se o vértice em sí é um porto
                        // guara o valor númerico, pois 'matriz' armazena um char
                        int porto = Character.getNumericValue(matriz[i][j]);
                        grafo.portos[porto-1] = v1;
                    }
                }
            }
        }
    }

    private void responde(){
        int p2; // porto 2
        System.out.println("--------------------------RESPOSTA--------------------------");
        for (int i=1;i<=numPortos;i++){
            int[] distancias = grafo.bfsDisPortos(grafo.portos[i-1]);
            System.out.print("A viagem entre ");
            p2 = calcProx(distancias,i);
            System.out.print("Porto " + i + " e Porto " + p2);
            System.out.println( " custará " + distancias[p2] + " Unidades de combustivel");
            if (p2 == 1){
                break;
            }
        }
        System.out.println("--------------------------RESPOSTA--------------------------");
    }

    /**
     * retorna o número do próximo porto
     * @param distancias é o array que guarda as distâncias entre o porto atual e os outros
     * @param start é o número do porto atual
     * @return o número do próximo porto alcançável
     */
    private int calcProx(int[] distancias, int start){
        for (int i=1;i<numPortos;i++){
            if (distancias[((start+i)%numPortos)] != -1){
                return (((start+i)%numPortos));
            }
        }
        return -1;
    }


    private void debugPrintMap(char[][] matriz){
        System.out.println("--------------------------DEBUG - PRINT - MAPA--------------------------");
        for (char[] i: matriz){
            for (char j : i){
                System.out.print(j);
            }
            System.out.print("\n");
        }
        System.out.println("--------------------------DEBUG - PRINT - MAPA--------------------------");
    }
    private void debugPrintGraph(){
        System.out.println("--------------------------DEBUG - PRINT - GRAFO-------------------------");
        System.out.println(grafo.toString());
        System.out.println("--------------------------DEBUG - PRINT - GRAFO-------------------------");
    }
    private void debugPrintPositions(int altura, int largura){
        System.out.println("--------------------------DEBUG - PRINT - POSIÇÕES----------------------");
        for (int i=0;i<altura;i++){
            for (int j=0;j<largura;j++){
                System.out.print((i*largura+j) + " ");
            }
            System.out.print("\n");
        }
        System.out.println("--------------------------DEBUG - PRINT - POSIÇÕES----------------------");
    }
}
