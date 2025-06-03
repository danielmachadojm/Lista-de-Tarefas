# ✅ Lista de Tarefas em Java

🎯 Uma aplicação de linha de comando desenvolvida em **Java**, que permite gerenciar suas tarefas com organização, praticidade e persistência de dados! Ideal para estudos, organização pessoal ou como base para projetos maiores.

---

## 📋 Funcionalidades

- 📝 Adicionar novas tarefas
- 📋 Listar todas as tarefas
- 🗃️ Filtrar tarefas por status (pendente/concluída)
- 🔄 Alterar status das tarefas
- 🗑️ Excluir tarefas com confirmação
- 📊 Ordenar tarefas por:
  - Prioridade
  - Data de criação
  - Título (ordem alfabética)
  - Status
- 📤 Exportar tarefas para:
  - `.txt`
  - `.csv`
  - `.pdf` (via biblioteca iText)
- 💾 Persistência local de dados entre sessões
- 🧠 Código modular com uso do padrão de projeto Singleton

---

## 📂 Estrutura do Projeto

src/
data/
PersistenciaTarefa.java
main/
Main.java
modelo/
Prioridade.java
Status.java
Tarefa.java
servico/
GerenciadorTarefas.java


---

## 🚀 Como Executar

1. **Pré-requisitos**:
   - Java 8+ instalado
   - IDE (IntelliJ, Eclipse, VSCode) ou compilador de terminal

2. **Clone o projeto**:

`git clone https://github.com/seu-usuario/lista-tarefas-java.git`
`cd lista-tarefas-java`

Compile o projeto:

Se estiver usando o terminal:

`bash`
`javac -d bin src/**/*.java`
Execute o programa:

`bash`
`java -cp bin main.Main`

Como Usar
O sistema funciona via terminal com um menu intuitivo:

===== MENU =====
1. Adicionar tarefa
2. Listar tarefas
3. Listar tarefas pendentes
4. Listar tarefas concluídas
5. Alterar status de tarefa
6. Ordenar tarefas
7. Excluir tarefa
8. Exportar tarefas
0. Sair

Tecnologias Utilizadas:
- Java (Orientado a Objetos)

- Enum para prioridade e status

- Padrão de Projeto Singleton

- API de arquivos (BufferedWriter)

- Biblioteca iText (lowagie) para exportação em PDF

🤝 Autores
Desenvolvido por alunos da Universidade Católica do Salvador (UCSAL):

- Breno Souza

- Caio Bispo

- Cauã Garrido

- Daniel Machado

- Gustavo Martins

Orientador: Prof. Mário Pereira

📚 Referências
Gamma, E. et al. Design Patterns: Elements of Reusable Object-Oriented Software

Freeman, E. et al. Head First Design Patterns
