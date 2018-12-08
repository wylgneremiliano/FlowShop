/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flowshop;

import java.util.Scanner;

/**
 *
 * @author Gleidson
 */
public class FlowShop {
    // Variavel global para definir o numero de MAQUINAS   
    public static int NUMERO_MAQUINAS = 0;
    //Variavel global para definir o tamanho de TAREFAS
    public static int NUMERO_TAREFAS = 0;

    public static void main(String[] args) {
        //Cria o scanner para leitura dos valores
        Scanner ler = new Scanner(System.in);

        // Informa a quantidade de Maquinas
        System.out.println("Informe o numero de Maquinas: ");
        NUMERO_MAQUINAS = ler.nextInt();

        // Informa a quantidade de Tarefas
        System.out.println("Informe o numero de Tarefas: ");
        NUMERO_TAREFAS = ler.nextInt();

        // vetores que receberão os nomes das maquinas e tarefas   
        String[] maquinas = new String[NUMERO_MAQUINAS];
        String[] tarefas = new String[NUMERO_TAREFAS];

        //Vetor para atribuir a nomeação para fins de teste e não ter q digitar toda hora
        String[] nomeTarefas = {"Meia", "Cueca", "Lingerie", "Pijama"};
        String[] nomeMaquinas = {"Corte", "Costura", "Passadoria"};

        int[] tempoProcessamentoPorMaquina = new int[NUMERO_MAQUINAS];
        int[] tempoProcessamentoPorTarefa = new int[NUMERO_TAREFAS];

        // Popula o vetor de maquinas com os Nomes Especificados
        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            maquinas[i] = nomeMaquinas[i];
        }

        // Popula o vetor de TAREFAS com os Nomes Especificados
        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            tarefas[i] = nomeTarefas[i];
        }

        //Inicialização e Preenchimento VetorTempoTarefa
        int vetorTempoTarefa[][] = new int[NUMERO_MAQUINAS][NUMERO_TAREFAS];
        int vetorTempoTarefa2[][] = new int[NUMERO_MAQUINAS][NUMERO_TAREFAS];
        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            for (int j = 0; j < NUMERO_TAREFAS; j++) {
                System.out.println("Informe o Tempo de Processamento da maquina de: " + (maquinas[i]) + " com a atividade de: " + (tarefas[j]));
                vetorTempoTarefa[i][j] = ler.nextInt();
            }
        }

        // Chamo a Função para teste de Impressão
        calcularTempoProcessamento(vetorTempoTarefa2, tempoProcessamentoPorMaquina, vetorTempoTarefa);
        imprimir(vetorTempoTarefa, vetorTempoTarefa2, maquinas, tarefas, tempoProcessamentoPorMaquina);
        ordenarLPT(vetorTempoTarefa);
    }

    
    // Fun��o para calcular o makespan
    private static void calcularTempoProcessamento(int[][] permutacao, int[] resultados, int[][] vetorTempoTarefa) {
      // calculando o resultado
        int resTmp = 0;
        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            for (int j = 0; j < NUMERO_TAREFAS; j++) {

                permutacao[i][j] = vetorTempoTarefa[i][j] + resTmp;
                resTmp = permutacao[i][j];

                // Se já não estiver na primeira Linha, somo com as Informações com Linha Anterior          
                if (i != 0) {
                    permutacao[i][j] = permutacao[i - 1][j] + vetorTempoTarefa[i][j];
                    resTmp = permutacao[i][j];

                    // Se já não estiver na primeira coluna, somo com a Coluna Anterior  
                    if (j != 0) {
                        /*Verificação para analisar se a tarefa a ser somada com a coluna anterior não é inferior a maquina da linha anterior, ou seja
                        Verificação para vê se a atividade ainda não estava em execução' na maquina anterior*/
                        
                        //Codigo de Verificação se a tarefa não esta em execução na maquina anterior; 
                        if ((permutacao[i][j - 1] + vetorTempoTarefa[i][j]) > (permutacao[i - 1][j] + vetorTempoTarefa[i][j])) {
                            permutacao[i][j] = permutacao[i][j - 1] + vetorTempoTarefa[i][j];
                            resTmp = permutacao[i][j];
                        } else {
                            permutacao[i][j] = permutacao[i - 1][j] + vetorTempoTarefa[i][j];
                            resTmp = permutacao[i][j];
                        }
                    }
                }
            }

            resultados[i] = resTmp;
        }
    }

 //Função para calcular o tempo de Processamentos das Tarefas
     private static void ordenarLPT(int[][] vetorTempoTarefa) {
        int k=0;
        int resTmp=0;
        int [] vetorTempoTarefaOrdenado = new int[NUMERO_TAREFAS];
        int [][] mediaTemposProcessamentoEPosicao = new int[2][NUMERO_TAREFAS];
        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            for (int j = 0; j < NUMERO_MAQUINAS; j++) {
              resTmp = resTmp + vetorTempoTarefa[j][i];
            }
        mediaTemposProcessamentoEPosicao[0][k] = (resTmp/NUMERO_MAQUINAS);
        mediaTemposProcessamentoEPosicao[1][k] = i;
        resTmp=0;
        k++;
        }
        
        
        System.out.println("Medias e Posição");
        for (int i=0; i<NUMERO_TAREFAS;i++){
            System.out.println(" "+mediaTemposProcessamentoEPosicao[0][i]);
                    }
        
         for (int i=0; i<NUMERO_TAREFAS;i++){
            System.out.println(" "+mediaTemposProcessamentoEPosicao[1][i]);
        }
     }
    
     
     
     
     
     
     
     
     
     
     
    
    // Função de Impressão
    private static void imprimir(int[][] vetorTempoTarefa, int[][] vetorPermutacao, String[] maquinas, String[] tarefas, int[] resultados) {
        int i;
        for (i = 0; i < NUMERO_MAQUINAS; i++) {
            System.out.print("\nMaquina: " + maquinas[i] + "\n");
            for (int j = 0; j < NUMERO_TAREFAS; j++) {
                System.out.print("            " + tarefas[j] + vetorTempoTarefa[i][j] + "   " + vetorPermutacao[i][j]);
            }
            System.out.print(" Tempo Processamento da Maquina" + resultados[i]);

        }
    }

}
