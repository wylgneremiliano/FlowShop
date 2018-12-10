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
        String[] nomeMaquinas = new String[NUMERO_MAQUINAS];
        String[] nomeTarefas = new String[NUMERO_TAREFAS];

        //Vetor para atribuir a nomeação para fins de teste e não ter q digitar toda hora
        int[] tempoFinalPorMaquina = new int[NUMERO_MAQUINAS];
        int[] tempoProcessamentoPorTarefa = new int[NUMERO_TAREFAS];

        // Popula o vetor de maquinas com os Nomes Especificados
        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            nomeMaquinas[i] = "M" + i;
        }

        // Popula o vetor de TAREFAS com os Nomes Especificados
        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            nomeTarefas[i] = "Tarefa" + i;
        }

        //Inicialização e Preenchimento VetorTempoTarefa
        int vetorTempoIndividualTarefa[][] = new int[NUMERO_MAQUINAS][NUMERO_TAREFAS];
        int vetorTempoTotalTarefasMaquinas[][] = new int[NUMERO_MAQUINAS][NUMERO_TAREFAS];
        int vetorTempoTarefaDuasMaquinas[][] = new int[NUMERO_MAQUINAS][2];

        int[] posDoisMelhores = new int[2];

        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            for (int j = 0; j < NUMERO_TAREFAS; j++) {
                System.out.println("Informe o Tempo de Processamento da maquina de: " + (nomeMaquinas[i]) + " com a atividade de: " + (nomeTarefas[j]));
                vetorTempoIndividualTarefa[i][j] = ler.nextInt();
            }
        }

        //Função de Calculo
        vetorTempoTotalTarefasMaquinas = calcularTempoProcessamento(tempoFinalPorMaquina, vetorTempoIndividualTarefa);
        imprimir(vetorTempoIndividualTarefa, vetorTempoTotalTarefasMaquinas, nomeMaquinas, nomeTarefas, tempoFinalPorMaquina);
        posDoisMelhores = ordenarLPT(vetorTempoIndividualTarefa);

        for (int i = 0; i <= 1; i++) {
            System.out.println("A Posição dos dois Melhores São: " + posDoisMelhores[i]);
        }

        int melhorOpcao = 0;
        melhorOpcao = calcularTempoDuasMelhores(posDoisMelhores, tempoFinalPorMaquina, vetorTempoIndividualTarefa);
        System.out.println(melhorOpcao);
        /*
        imprimir2(vetorTempoIndividualTarefa, vetorTempoTotalTarefasMaquinas, nomeMaquinas, nomeTarefas, tempoFinalPorMaquina);
         */
    }

    // Fun��o para calcular o makespan
    private static int[][] calcularTempoProcessamento(int[] tempoFinalPorMaquina, int[][] vetorTempoIndividualTarefa) {
        // vetorTempoTotal receberá os Valores Acumulados dos Tempos Individuais
        // vetorFinalPorMaquina receberá os valores que cada maquina gastou para rodar todas as Atividades
        // vetorTempoIndividualTarefa trás o tempo que cada atividade gasta para executar em cada maquina
        int vetorTempoTotalTarefasMaquinas[][] = new int[NUMERO_MAQUINAS][NUMERO_TAREFAS];
        int resTmp = 0;
        
        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            for (int j = 0; j < NUMERO_TAREFAS; j++) {

                vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoIndividualTarefa[i][j] + resTmp;
                resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                // Se já não estiver na primeira Linha, somo com as Informações com Linha Anterior          
                if (i != 0) {
                    vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][j];
                    resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                    // Se já não estiver na primeira coluna, somo com a Coluna Anterior  
                    if (j != 0) {
                        /*Verificação para analisar se a tarefa a ser somada com a coluna anterior não é inferior a maquina da linha anterior, ou seja
                        Verificação para vê se a atividade ainda não estava em execução' na maquina anterior*/

                        //Codigo de Verificação se a tarefa não esta em execução na maquina anterior; 
                        if ((vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][j]) > (vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][j])) {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][j];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        } else {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][j];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        }
                    }
                }
            }
            tempoFinalPorMaquina[i] = resTmp;
        }
        return vetorTempoTotalTarefasMaquinas;
    }

    // Fun��o para calcular o makespan
    private static int calcularTempoDuasMelhores(int[] posDoisMelhores, int[] tempoFinalPorMaquina, int[][] vetorTempoIndividualTarefa) {
        // vetorTempoTotal receberá os Valores Acumulados dos Tempos Individuais
        // vetorFinalPorMaquina receberá os valores que cada maquina gastou para rodar todas as Atividades
        // vetorTempoIndividualTarefa trás o tempo que cada atividade gasta para executar em cada maquina
        int vetorTempoTotalTarefasMaquinas[][] = new int[NUMERO_MAQUINAS][NUMERO_TAREFAS];
        int resTmp = 0;
        int tempo1 = 0, tempo2 =0;
        int[] posInvDoisMelhores = new int[2];
        int x1 = 0, x2 = 0;
        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            for (int j = 0; j <= 1; j++) {
                vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoIndividualTarefa[i][posDoisMelhores[j]] + resTmp;
                resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                // Se já não estiver na primeira Linha, somo com as Informações com Linha Anterior          
                if (i != 0) {
                    vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][posDoisMelhores[j]];
                    resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                    // Se já não estiver na primeira coluna, somo com a Coluna Anterior  
                    if (j != 0) {
                        /*Verificação para analisar se a tarefa a ser somada com a coluna anterior não é inferior a maquina da linha anterior, ou seja
                        Verificação para vê se a atividade ainda não estava em execução' na maquina anterior*/

                        //Codigo de Verificação se a tarefa não esta em execução na maquina anterior; 
                        if ((vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][posDoisMelhores[j]]) > (vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][posDoisMelhores[j]])) {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][j];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        } else {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][posDoisMelhores[j]];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        }
                    }
                }
            }
            tempoFinalPorMaquina[i] = resTmp;
            tempo1 = resTmp;
            x1 = posDoisMelhores[0];
        }
        
        
        posInvDoisMelhores[0] = posDoisMelhores[1];
        posInvDoisMelhores[1] = posDoisMelhores[0];
        
        for(int i=0; i<NUMERO_MAQUINAS;i++){
            vetorTempoTotalTarefasMaquinas[i][0]=0;
            vetorTempoTotalTarefasMaquinas[i][1]=0;
        }
        resTmp =0;
        
        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            for (int j = 0; j <= 1; j++) {
                vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoIndividualTarefa[i][posInvDoisMelhores[j]] + resTmp;
                resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                // Se já não estiver na primeira Linha, somo com as Informações com Linha Anterior          
                if (i != 0) {
                    vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][posInvDoisMelhores[j]];
                    resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                    // Se já não estiver na primeira coluna, somo com a Coluna Anterior  
                    if (j != 0) {
                        /*Verificação para analisar se a tarefa a ser somada com a coluna anterior não é inferior a maquina da linha anterior, ou seja
                        Verificação para vê se a atividade ainda não estava em execução' na maquina anterior*/
            
                        

                        //Codigo de Verificação se a tarefa não esta em execução na maquina anterior; 
                        if ((vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][posInvDoisMelhores[j]]) > (vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][posInvDoisMelhores[j]])) {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][posInvDoisMelhores[j]];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        } else {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][posInvDoisMelhores[j]];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        }
                    }
                }
            }
            tempoFinalPorMaquina[i] = resTmp;
            tempo2 = resTmp;
            x2 = posDoisMelhores[1];
        }
            System.out.printf("Makespan de %d para %d: %d\n", posDoisMelhores[0], posDoisMelhores[1], tempo1);
            System.out.printf("Makespan de %d para %d: %d\n", posDoisMelhores[1], posDoisMelhores[0], tempo2);
            if (x1 > x2) {
                return x1;
            } else {
                return x2;
            }
    
    }

    private static int[] ordenarLPT(int[][] vetorTempoTarefa) {
        int k = 0;
        int[] retorno = new int[2];
        int resTmp = 0;
        int[][] mediaTemposProcessamentoEPosicao = new int[2][NUMERO_TAREFAS];

        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            for (int j = 0; j < NUMERO_MAQUINAS; j++) {
                resTmp = resTmp + vetorTempoTarefa[j][i];
            }

            mediaTemposProcessamentoEPosicao[0][k] = (resTmp / NUMERO_MAQUINAS);
            mediaTemposProcessamentoEPosicao[1][k] = i;
            resTmp = 0;
            k++;
        }

        System.out.println("\n\nMedias e Posição\n");
        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            System.out.print("  " + mediaTemposProcessamentoEPosicao[0][i]);
        }
        System.out.println("\n");
        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            System.out.print("  " + mediaTemposProcessamentoEPosicao[1][i]);
        }

        for (int i = 0; i == 0; i++) {
            for (int j = 0; j < mediaTemposProcessamentoEPosicao.length; j++) {
                if (mediaTemposProcessamentoEPosicao[0][j] > mediaTemposProcessamentoEPosicao[0][j + 1]) {
                    int aux = mediaTemposProcessamentoEPosicao[0][j];
                    int aux2 = mediaTemposProcessamentoEPosicao[1][j];
                    mediaTemposProcessamentoEPosicao[0][j] = mediaTemposProcessamentoEPosicao[0][j + 1];
                    mediaTemposProcessamentoEPosicao[1][j] = mediaTemposProcessamentoEPosicao[1][j + 1];
                    mediaTemposProcessamentoEPosicao[0][j + 1] = aux;
                    mediaTemposProcessamentoEPosicao[1][j + 1] = aux2;
                    j = 0; //+++
                    //  alterarVetorTarefa(tarefas, j, j+1);
                }
            }
        }

        boolean controle;
        int aux = 0;
        int aux2 = 0;
        for (int i = 0; i < mediaTemposProcessamentoEPosicao.length; ++i) {
            controle = true;
            for (int j = 0; j < (mediaTemposProcessamentoEPosicao.length - 1); ++j) {
                if (mediaTemposProcessamentoEPosicao[0][j] > mediaTemposProcessamentoEPosicao[0][j + 1]) {
                    aux = mediaTemposProcessamentoEPosicao[0][j];
                    mediaTemposProcessamentoEPosicao[0][j] = mediaTemposProcessamentoEPosicao[0][j + 1];
                    mediaTemposProcessamentoEPosicao[0][j + 1] = aux;

                    aux2 = mediaTemposProcessamentoEPosicao[1][j];
                    mediaTemposProcessamentoEPosicao[1][j] = mediaTemposProcessamentoEPosicao[1][j + 1];
                    mediaTemposProcessamentoEPosicao[1][j + 1] = aux2;

                    //alterarVetorTarefa(tarefas, j, j+1);
                    controle = false;
                }
            }
            if (controle) {
                break;
            }

        }
//        for(int i=0; i< tarefas.length; i++){
//            System.out.println(tarefas[i]);
//        }

        System.out.println("\n\nMedias e Posição222\n");
        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            System.out.print("  " + mediaTemposProcessamentoEPosicao[0][i]);
        }
        System.out.println("\n");
        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            System.out.print("  " + mediaTemposProcessamentoEPosicao[1][i]);
        }

        retorno[0] = mediaTemposProcessamentoEPosicao[1][NUMERO_TAREFAS - 1];
        retorno[1] = mediaTemposProcessamentoEPosicao[1][NUMERO_TAREFAS - 2];
        return retorno;

    }

    // Função para Alterar Vetor Tarefa
    private static void alterarVetorTarefa(String[] nomeTarefas, int pos1, int pos2) {
        String aux;
        aux = nomeTarefas[pos1];
        nomeTarefas[pos1] = nomeTarefas[pos2];
        nomeTarefas[pos2] = aux;
    }

    // Função de Impressão
    private static void imprimir(int[][] vetorTempoIndividualTarefa, int[][] vetorTempoTotalTarefasMaquinas, String[] nomeMaquinas, String[] nomeTarefas, int[] tempoFinalPorMaquina) {
        int i;
        for (i = 0; i < NUMERO_MAQUINAS; i++) {
            System.out.print("\nMaquina: " + nomeMaquinas[i] + "\n");
            for (int j = 0; j < NUMERO_TAREFAS; j++) {
                System.out.print("            " + nomeTarefas[j] + "Tempo Nesta Tarefa: " + vetorTempoIndividualTarefa[i][j] + "   " + vetorTempoTotalTarefasMaquinas[i][j]);
            }
            System.out.print(" Tempo Processamento da Maquina" + tempoFinalPorMaquina[i]);

        }
    }

    // Função de Impressão
}
