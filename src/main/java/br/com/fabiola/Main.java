package br.com.fabiola;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Object[][] itens = new Object[][]{
                {1, "Arroz", 4.00, 10},
                {2, "Feijão", 7.00, 14},
                {3, "Macarrão", 3.00, 15},
                {4, "Açúcar", 5.00, 12},
        };

        //Receber as entradas do usuario
        Scanner input = new Scanner(System.in);
        //TODO(Precisamos verificar a quantidade de itens disponiveis no carrinhos ou varieade de items)
        int[][] carrinho = new int[19][];
        int valorSelecionado = 0;
        //Laço de repetiçao do menu
        boolean finalizaPrograma = false;
        while (!finalizaPrograma) {
            System.out.println("\n1-Visualizar, 2-Adicionar, 3-Remover, 4-Ver Carrinho, 5-Finalizar");
            valorSelecionado = input.nextInt();
            if (valorSelecionado == 1) {
                visualizacao(itens);
            } else if (valorSelecionado == 2) {
                carrinho = adicionarItem(input, itens, carrinho);
            } else if (valorSelecionado == 3) {
                carrinho = removerItem(input, itens, carrinho);
            } else if (valorSelecionado == 4) {
                verCarrinho(carrinho, itens);
            } else if (valorSelecionado == 5) {
                finalizaPrograma = exibirRecibo(input, carrinho, itens);
            } else {
                System.out.println("Opcão inválida.");
            }
        }
    }

    public static Object[] getItemSelecionado(Object[][] itens, int id) {
        Object[] itemSelecionado = null;

        for (int i = 0; i < itens.length; i++) {
            if (itens[i][0].equals(id)) {
                itemSelecionado = itens[i];
                break;
            }
        }
        return itemSelecionado;
    }

    public static int[] getCarrinhoSelecionado(int[][] carrinho, int id) {
        int[] itemSelecionado = null;

        for (int i = 0; i < carrinho.length; i++) {
            if (carrinho[i] != null && carrinho[i][0] == id) {
                itemSelecionado = carrinho[i];
                break;
            }
        }
        return itemSelecionado;
    }

    public static void visualizacao(Object[][] itens) {
        //Printando os itens diponiveis 1 por 1
        for (int i = 0; i < itens.length; i++) {
            System.out.println(
                    "ID: " + itens[i][0] + " - Item: " + itens[i][1] + ", Valor: R$" + itens[i][2] + ", Qtd disponível: " + itens[i][3]
            );
        }
    }

    public static int[][] adicionarItem(Scanner input, Object[][] itens, int[][] carrinho) {
        System.out.println("Digite o ID do item: ");
        int id = input.nextInt();

        Object[] itemSelecionado = getItemSelecionado(itens, id);
        int[] carrinhoSelecionado = getCarrinhoSelecionado(carrinho, id);

        if (itemSelecionado == null) {
            System.out.println("Produto de ID " + id + " não encontrado.");
            return carrinho;
        }

        System.out.println("Digite a quantidade: ");
        int qtd = input.nextInt();
        if (qtd <= 0) {
            System.out.println("A quantidade deve ser superior a 0(ZERO).");
            return carrinho;
        }
        int totalItens = 0;
        for (int i = 0; i < carrinho.length; i++) {
            if (carrinho[i] != null) {
                totalItens = totalItens + carrinho[i][1];
            }
        }
        if (totalItens + qtd > 20) {
            System.out.println("Limite de itens ultrapassado em " + (totalItens + qtd - 20) + ". Max. de 20 itens.");
            System.out.println("Item não adicionado.");
            return carrinho;
        }

        if (carrinhoSelecionado != null) {
            qtd = qtd + carrinhoSelecionado[1];
        }

        if (qtd <= (int) itemSelecionado[3]) {
            for (int i = 0; i < carrinho.length; i++) {
                if (carrinhoSelecionado == null) {
                    if (carrinho[i] == null) {
                        carrinho[i] = new int[]{id, qtd};
                        break;
                    }
                } else {
                    if (carrinho[i] != null && carrinho[i][0] == carrinhoSelecionado[0]) {
                        carrinho[i] = new int[]{id, qtd};
                        break;
                    }
                }
            }
            System.out.println(qtd + " item(s) " + itemSelecionado[1] + " adicionado ao carrinho.");
            return carrinho;
        } else {
            System.out.println("Quantidade " + qtd + " indiponivel. Max: " + itemSelecionado[3]);
            return carrinho;
        }
    }

    public static int[][] removerItem(Scanner input, Object[][] itens, int[][] carrinho) {
        System.out.println("Digite o ID do item: ");
        int id = input.nextInt();

        Object[] itemSelecionado = getItemSelecionado(itens, id);
        int[] carrinhoSelecionado = getCarrinhoSelecionado(carrinho, id);

        if (carrinhoSelecionado == null || itemSelecionado == null) {
            System.out.println("Produto de ID " + id + " não encontrado.");
            return carrinho;
        }

        System.out.println("Digite a quantidade: ");
        int qtd = input.nextInt();
        if (qtd <= 0) {
            System.out.println("A quantidade deve ser superior a 0(ZERO).");
            return carrinho;
        }
        if (qtd <= carrinhoSelecionado[1]) {
            for (int i = 0; i < carrinho.length; i++) {
                if (carrinho[i] != null && carrinho[i][0] == carrinhoSelecionado[0]) {
                    if (carrinhoSelecionado[1] == qtd) {
                        carrinho[i] = null;
                    } else {
                        carrinho[i] = new int[]{id, carrinhoSelecionado[1] - qtd};
                    }

                    break;
                }
            }
            System.out.println(qtd + " item(s) " + itemSelecionado[1] + " removidos do carrinho.");
            return carrinho;
        } else {
            System.out.println("Quantidade " + qtd + " indiponivel no carrinho. Quatindade no carrinho: " + carrinhoSelecionado[1]);
            return carrinho;
        }
    }

    public static boolean exibirRecibo(Scanner input, int[][] carrinho, Object[][] itens) {
        //Printando os itens diponiveis 1 por 1
        System.out.println("Você possui um cupom de desconto ?[SIM, NÃO]");
        String resposta = input.next();
        double desconto = 0;
        if (resposta.equals("SIM")) {
            System.out.println("Digite um dentre o cupons disponiveis: 5, 10, 15");
            desconto = input.nextDouble();
            if (desconto != 5.00 && desconto != 10.00 && desconto != 15.00) {
                System.out.println("Cupom inválido");
                return false;
            }
        } else if (resposta.equals("NÃO")) {
            desconto = 0;
        } else {
            System.out.println("Opcão digitada inválida");
        }

        System.out.println("---------------------------------------------------");
        System.out.printf("%-20s %-15s %s\n", "Item", "Quantidade", "Preço");
        double somaTotal = 0.0;
        for (int i = 0; i < carrinho.length; i++) {
            if (carrinho[i] != null) {
                Object[] itemSelecionado = getItemSelecionado(itens, carrinho[i][0]);
                double valorTotal = carrinho[i][1] * (double) itemSelecionado[2];
                somaTotal += valorTotal;
                System.out.printf("%-20s %-15d R$%.2f\n", itemSelecionado[1], carrinho[i][1], valorTotal);
            }
        }
        System.out.println("---------------------------------------------------");

        double valorComDesconto = somaTotal - (somaTotal * (desconto / 100));
        System.out.printf("%-36s R$%.2f\n", "TOTAL:", somaTotal);
        System.out.println("---------------------------------------------------");
        System.out.printf("%-36s R$%.2f\n", "TOTAL COM DESCONTO(" + desconto + "%):", valorComDesconto);
        System.out.println("---------------------------------------------------");
        return true;
    }

    public static void verCarrinho(int[][] carrinho, Object[][] itens) {
        System.out.println("-----------------------------------------------------------------------");
        System.out.printf("%-20s | %-12s | %-16s | %s\n", "Item", "Quantidade", "Preço Unitário", "Preço Total");
        System.out.println("-----------------------------------------------------------------------");

        for (int i = 0; i < carrinho.length; i++) {
            if (carrinho[i] != null) {
                Object[] itemSelecionado = getItemSelecionado(itens, carrinho[i][0]);
                double valorTotal = carrinho[i][1] * (double) itemSelecionado[2];

                System.out.printf("%-20s | %-12d | R$ %-13.2f | R$ %.2f\n", itemSelecionado[1], carrinho[i][1], (double) itemSelecionado[2], valorTotal);
            }
        }
    }
}