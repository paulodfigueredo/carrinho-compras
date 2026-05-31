package br.com.fabiola

data class Produto(val id: Int, val nome: String, val preco: Double, val estoqueMaximo: Int)

fun Double.formatarMoeda(): String = "R$%.2f".format(this)

class SistemaVendas {
    private val catalogo = listOf(
        Produto(1, "Arroz", 4.00, 10),
        Produto(2, "Feijão", 7.00, 14),
        Produto(3, "Macarrão", 3.00, 15),
        Produto(4, "Açúcar", 5.00, 12)
    )

    private val carrinho = mutableMapOf<Produto, Int>()

    fun iniciar() {
        var executando = true
        while (executando) {
            println("\n1-Visualizar | 2-Adicionar | 3-Remover | 4-Ver Carrinho | 5-Finalizar")
            print("Escolha uma opção: ")

            val opcao = readlnOrNull()?.toIntOrNull()

            when (opcao) {
                1 -> visualizarCatalogo()
                2 -> adicionarItem()
                3 -> removerItem()
                4 -> verCarrinho()
                5 -> if (finalizarCompra()) executando = false
                else -> println("Opção inválida. Tente novamente.")
            }
        }
    }

    private fun visualizarCatalogo() {
        catalogo.forEach {
            println("ID: ${it.id} - Item: ${it.nome}, Valor: ${it.preco.formatarMoeda()}, Qtd disponível: ${it.estoqueMaximo}")
        }
    }

    private fun lerIdSeguro(): Produto? {
        print("Digite o ID do item: ")
        val id = readlnOrNull()?.toIntOrNull() ?: return null
        return catalogo.find { it.id == id } // Uso de funções de alta ordem (find)
    }

    private fun lerQuantidadeSegura(): Int {
        print("Digite a quantidade: ")
        return readlnOrNull()?.toIntOrNull() ?: 0
    }

    private fun adicionarItem() {
        val produto = lerIdSeguro()
        if (produto == null) {
            println("Produto não encontrado.")
            return
        }

        val qtdDesejada = lerQuantidadeSegura()
        if (qtdDesejada <= 0) return

        val qtdNoCarrinho = carrinho.getOrDefault(produto, 0)
        val novaQtdTotal = qtdNoCarrinho + qtdDesejada

        if (novaQtdTotal <= produto.estoqueMaximo) {
            carrinho[produto] = novaQtdTotal
            println("$qtdDesejada item(s) de ${produto.nome} adicionado(s) ao carrinho.")
        } else {
            println("Quantidade indisponível. Máximo permitido: ${produto.estoqueMaximo}")
        }
    }

    private fun removerItem() {
        val produto = lerIdSeguro()
        if (produto == null || !carrinho.containsKey(produto)) {
            println("Produto não encontrado no carrinho.")
            return
        }

        val qtdRemover = lerQuantidadeSegura()
        if (qtdRemover <= 0) return

        val qtdAtual = carrinho.getValue(produto)

        if (qtdRemover >= qtdAtual) {
            carrinho.remove(produto)
            println("Todos os itens de ${produto.nome} foram removidos.")
        } else {
            carrinho[produto] = qtdAtual - qtdRemover
            println("$qtdRemover item(s) de ${produto.nome} removido(s) do carrinho.")
        }
    }

    private fun verCarrinho() {
        if (carrinho.isEmpty()) {
            println("Seu carrinho está vazio.")
            return
        }

        println("-".repeat(70))
        System.out.printf("%-20s %-15s %-20s %s\n", "Item", "Quantidade", "Preço-unitário", "Preço-total")

        carrinho.forEach { (produto, quantidade) ->
            val valorTotalItem = quantidade * produto.preco
            System.out.printf(
                "%-20s %-15d %-20s %s\n",
                produto.nome,
                quantidade,
                produto.preco.formatarMoeda(),
                valorTotalItem.formatarMoeda()
            )
        }
        println("-".repeat(70))
    }

    private fun finalizarCompra(): Boolean {
        if (carrinho.isEmpty()) {
            println("Carrinho vazio. Compra cancelada.")
            return true
        }

        print("Digite um dos cupons disponíveis (5, 10, 15): ")
        val desconto = readlnOrNull()?.toDoubleOrNull() ?: 0.0

        if (desconto !in listOf(5.0, 10.0, 15.0)) {
            println("Cupom inválido.")
            return false
        }

        verCarrinho()

        val somaTotal = carrinho.entries.sumOf { it.key.preco * it.value }
        val valorComDesconto = somaTotal - (somaTotal * (desconto / 100))

        System.out.printf("%-36s %s\n", "TOTAL:", somaTotal.formatarMoeda())
        println("-".repeat(70))
        System.out.printf("%-36s %s\n", "TOTAL COM DESCONTO ($desconto%):", valorComDesconto.formatarMoeda())
        println("-".repeat(70))

        return true
    }
}

fun main() {
    val sistema = SistemaVendas()
    sistema.iniciar()
}