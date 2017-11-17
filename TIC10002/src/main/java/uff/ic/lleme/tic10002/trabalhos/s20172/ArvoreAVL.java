
package rodovias;

/**
 *
 * @author Luis / adaptação marcelo
 */
public class ArvoreAVL {
    private No raiz = null;
    private boolean balanceada = false;
    private int maxSaldo = 0;
    private long qtNos=0;

    public ArvoreAVL(boolean balanceada) {
        this.balanceada = balanceada;
    }
    
    // Variáveis de controle e corte de fluxo e seus métodos
    public int minFluxo=1000000000;//inicializa com fluxo absurdo
    public int maxFluxo=0;//inicializa com menor fluxo possível
    public float delta=0;
    public float corte=0;

    private final No[] indexR=new No[10000000]; //limitado a 99 setores
    //private final No[] indexR=new No[5200000]; // limitado a 50 setores
    private int maxIndexR=0;
    private int minIndexR=10000001;
 /* 
        esta variável (seta) é usada no método emOrdem para fazer a quebra de linha por setor
        no relatório final, lini é o contador de linhas impressas
 */
    public int seta=-1;
    public int lini=0;
    
    private class No {

        public Rodovias conteudo = null;
        public No esquerda = null;
        public No direita = null;
        public int saldoAltura = 0;

        public No(Rodovias conteudo) {
            this.conteudo = conteudo;
        }
           
// Inicio dos métodos
    
        public void printTree() {
            if (esquerda != null)
                esquerda.printTree(false, "");
            printNodeValue();
            if (direita != null)
                direita.printTree(true, "");
        }

        private void printNodeValue() {
            System.out.print("" + conteudo.chave + "/" + saldo() + "/" +conteudo.fluxo);
            System.out.print('\n');
        }

        private void printTree(boolean isRight, String indent) {
            if (esquerda != null)
                esquerda.printTree(false, indent + (isRight ? " |      " : "        "));
            System.out.print(indent);
            if (isRight)
                System.out.print(" \\");
            else
                System.out.print(" /");
            System.out.print("----- ");
            printNodeValue();
            if (direita != null)
                direita.printTree(true, indent + (isRight ? "        " : " |      "));
        }

        private int altura() {
            return Math.max(altura(this.direita), altura(this.esquerda)) + 1;
        }

        private int altura(No no) {
            if (no != null)
                return Math.max(altura(no.direita), altura(no.esquerda)) + 1;
            return 0;
        }

        private int saldo() {
            if (this.direita != null && this.esquerda != null)
                return Math.abs(this.direita.altura() - this.esquerda.altura());
            else if (this.direita != null && this.esquerda == null)
                return Math.abs(this.direita.altura() - 0);
            else if (this.direita == null && this.esquerda != null)
                return Math.abs(0 - this.esquerda.altura());
            return 0;
        }
/*
      
*/
/*
    Fim dos métodos adicionados
*/        
    }
    public void CalcMin(No no) {
        if(no != null){
            if (no.conteudo.fluxo<minFluxo) minFluxo=no.conteudo.fluxo;
            CalcMin(no.esquerda);
            CalcMin(no.direita);
        }
    }
    
    public void print() {
        raiz.printTree();
    }

    public int altura() {
        return altura(raiz);
    }

    private int altura(No no) {
        if (no != null)
            return Math.max(altura(no.direita), altura(no.esquerda)) + 1;
        return 0;
    }

    public void printIndex(){
        int lim=maxIndexR;
        if (qtNos>50){
            lim=minIndexR+50;
        } 
        for(int i=minIndexR;i<=lim;i++){
            if (indexR[i] != null)
                //System.out.println("index["+i+"]="+indexR[i].conteudo.chave+"/"+indexR[i].conteudo.setor+"/"+indexR[i].conteudo.data+"/"+indexR[i].conteudo.fluxo+"/"+indexR[i].conteudo);
                System.out.println("index["+i+"]="+indexR[i]);
    }
    }
    public int maxSaldo() {
        maxSaldo = 0;
        altura2(raiz);
        return maxSaldo;
    }

    private int altura2(No no) {
        if (no != null) {
            int alturaDireita = altura2(no.direita);
            int alturaEsquerda = altura2(no.esquerda);
            int saldoSubArvores = Math.abs(alturaDireita - alturaEsquerda);
            if (saldoSubArvores > maxSaldo)
                maxSaldo = saldoSubArvores;
            return Math.max(alturaDireita, alturaEsquerda) + 1;
        }
        return 0;
    }

    public boolean inserir(Rodovias conteudo) {
        if (raiz == null){
            raiz = new No(conteudo);
            indexR[conteudo.chave]=raiz; //Adiciona o ponteiro para a raiz no índice
            calcLimitIndexR(conteudo.chave);
            qtNos++;
            return (raiz != null);
        }
        else if (indexR[conteudo.chave] == null){ //indica ser um novo nó da arvore
            return inserir(raiz, conteudo);
            } else { // o nó já existe, consequentemente preciso somar o novo fluxo, uso o ponteiro do índice
            indexR[conteudo.chave].conteudo.fluxo=indexR[conteudo.chave].conteudo.fluxo+conteudo.fluxo;
            calcMinMax(indexR[conteudo.chave].conteudo.fluxo); //recalcula o máximo
            return true;
        }
    }

    private boolean inserir(No no, Rodovias conteudo) {
        if (no.conteudo.chave == conteudo.chave){
            // Caso encontre a chave, significa que precisa acumular o fluxo
            no.conteudo.fluxo=no.conteudo.fluxo+conteudo.fluxo;
            // Aproveita e atualiza min e max de fluxo acumulado
            calcMinMax(no.conteudo.fluxo);
            return false;
        }
        else if (no.conteudo.chave > conteudo.chave)
            if (no.direita == null) {
                no.direita = new No(conteudo);
                qtNos++;
                //Inicializa index para atualização incluindo o ponteiro do novo nó
                indexR[conteudo.chave]=no.direita; 
                calcLimitIndexR(conteudo.chave);
                // sempre que crio um novo nó atualiza min e max de fluxo acumulado
                calcMinMax(no.direita.conteudo.fluxo);
                //inserido à direita do nó: casos a) e e) pág. 109
                //<editor-fold defaultstate="collapsed" desc="balanceamento">
                no.saldoAltura++;
                //</editor-fold>

                return true;
            } else {

                //<editor-fold defaultstate="collapsed" desc="balanceamento">
                int saldoAnteriorU = no.direita.saldoAltura;
                int saldoAnteriorV = 0;
                if (no.direita.esquerda != null)
                    saldoAnteriorV = no.direita.esquerda.saldoAltura;
                //</editor-fold>

                boolean inserido = inserir(no.direita, conteudo);

                //<editor-fold defaultstate="collapsed" desc="balanceamento">
                int saldoPosteriorU = no.direita.saldoAltura;
                int saldoPosteriorV = 0;
                if (no.direita.esquerda != null)
                    saldoPosteriorV = no.direita.esquerda.saldoAltura;

                int deltaU = saldoPosteriorU - saldoAnteriorU;
                int deltaV = saldoPosteriorV - saldoAnteriorV;

                if (Math.abs(deltaU) > 0 && saldoPosteriorU != 0) {
                    no.saldoAltura++;
                    if (Math.abs(no.saldoAltura) > 1 && balanceada)
                        balancearADireita(no, deltaU, deltaV);
                }
                //</editor-fold>

                return inserido;
            }
        else if (no.conteudo.chave < conteudo.chave)
            if (no.esquerda == null) {
                no.esquerda = new No(conteudo);
                qtNos++;
                //Inicializa index para atualização incluindo o ponteiro do novo nó
                indexR[conteudo.chave]=no.esquerda;
                calcLimitIndexR(conteudo.chave);
                // sempre que crio um novo nó atualiza max de fluxo acumulado
                calcMinMax(no.esquerda.conteudo.fluxo);
                //inserido à esquerda do nó: casos c) e g) pág. 109
                //<editor-fold defaultstate="collapsed" desc="balanceamento">
                no.saldoAltura--;
                //</editor-fold>

                return true;
            } else {

                //<editor-fold defaultstate="collapsed" desc="balanceamento">
                int saldoAnteriorZ = no.esquerda.saldoAltura;
                int saldoAnteriorY = 0;
                if (no.esquerda.direita != null)
                    saldoAnteriorY = no.esquerda.direita.saldoAltura;
                //</editor-fold>

                boolean inserido = inserir(no.esquerda, conteudo);

                //<editor-fold defaultstate="collapsed" desc="balanceamento">
                int saldoPosteriorZ = no.esquerda.saldoAltura;
                int saldoPosteriorY = 0;
                if (no.esquerda.direita != null)
                    saldoPosteriorY = no.esquerda.direita.saldoAltura;

                int deltaZ = saldoPosteriorZ - saldoAnteriorZ;
                int deltaY = saldoPosteriorY - saldoAnteriorY;

                if (Math.abs(deltaZ) > 0 && saldoPosteriorZ != 0) {
                    no.saldoAltura--;
                    if (Math.abs(no.saldoAltura) > 1 && balanceada)
                        balancearAEsquerda(no, deltaZ, deltaY);
                }
                //</editor-fold>

                return inserido;
            }
        else
            return false;
    }

    private void balancearADireita(No n1, int deltaU, int deltaV) {
        if (deltaU > 0) {
            No n2 = n1.direita;
            Rodovias p = n1.conteudo;
            Rodovias u = n2.conteudo;
            No t1 = n2.direita;
            No t2 = n2.esquerda;
            No t3 = n1.esquerda;

            n1.conteudo = u;
            n1.direita = t1;
            n1.esquerda = n2;
            n2.conteudo = p;
            n2.direita = t2;
            n2.esquerda = t3;
            //reajustando o indexR
            if (t1 != null) indexR[t1.conteudo.chave]=t1;
            if (t2 != null) indexR[t2.conteudo.chave]=t2;
            if (t3 != null) indexR[t3.conteudo.chave]=t3;
            if (n1 != null) indexR[n1.conteudo.chave]=n1;
            if (n2 != null) indexR[n2.conteudo.chave]=n2;
           
            n1.saldoAltura = 0;
            n2.saldoAltura = 0;
        } else if (deltaU < 0) {
            No n2 = n1.direita;
            No n3 = n2.esquerda;
            Rodovias p = n1.conteudo;
            Rodovias u = n2.conteudo;
            Rodovias v = n3.conteudo;
            No t1 = n2.direita;
            No t2 = n3.direita;
            No t3 = n3.esquerda;
            No t4 = n1.esquerda;

            n1.conteudo = v;
            n1.direita = n2;
            n1.esquerda = n3;
            n2.conteudo = u;
            n2.direita = t1;
            n2.esquerda = t2;
            n3.conteudo = p;
            n3.direita = t3;
            n3.esquerda = t4;
            //reajustando o indexR
            if (t1 != null) indexR[t1.conteudo.chave]=t1;
            if (t2 != null) indexR[t2.conteudo.chave]=t2;
            if (t3 != null) indexR[t3.conteudo.chave]=t3;
            if (t4 != null) indexR[t4.conteudo.chave]=t4;
            if (n1 != null) indexR[n1.conteudo.chave]=n1;
            if (n2 != null) indexR[n2.conteudo.chave]=n2;
            if (n3 != null) indexR[n3.conteudo.chave]=n3;

            n1.saldoAltura = 0;
            if (t2 == null && t3 == null) {
                n2.saldoAltura = 0;
                n3.saldoAltura = 0;
            } else if (deltaV > 0) {
                n2.saldoAltura = 0;
                n3.saldoAltura = -1;
            } else {
                n2.saldoAltura = 1;
                n3.saldoAltura = 0;
            }
        }
    }

    private void balancearAEsquerda(No n1, int deltaZ, int deltaY) {
        if (deltaZ < 0) {
            No n2 = n1.esquerda;
            Rodovias p = n1.conteudo;
            Rodovias z = n2.conteudo;
            No t1 = n1.direita;
            No t2 = n2.direita;
            No t3 = n2.esquerda;

            n1.conteudo = z;
            n1.direita = n2;
            n1.esquerda = t3;
            n2.conteudo = p;
            n2.direita = t1;
            n2.esquerda = t2;
            //reajustando o indexR
            if (t1 != null) indexR[t1.conteudo.chave]=t1;
            if (t2 != null) indexR[t2.conteudo.chave]=t2;
            if (t3 != null) indexR[t3.conteudo.chave]=t3;
            if (n1 != null) indexR[n1.conteudo.chave]=n1;
            if (n2 != null) indexR[n2.conteudo.chave]=n2;

            n1.saldoAltura = 0;
            n2.saldoAltura = 0;
        } else if (deltaZ > 0) {
            No n2 = n1.esquerda;
            No n3 = n2.direita;
            Rodovias p = n1.conteudo;
            Rodovias z = n2.conteudo;
            Rodovias y = n3.conteudo;
            No t1 = n1.direita;
            No t2 = n3.direita;
            No t3 = n3.esquerda;
            No t4 = n2.esquerda;

            n1.conteudo = y;
            n1.direita = n2;
            n1.esquerda = n3;
            n2.conteudo = p;
            n2.direita = t1;
            n2.esquerda = t2;
            n3.conteudo = z;
            n3.direita = t3;
            n3.esquerda = t4;
            //reajustando o indexR
            if (t1 != null) indexR[t1.conteudo.chave]=t1;
            if (t2 != null) indexR[t2.conteudo.chave]=t2;
            if (t3 != null) indexR[t3.conteudo.chave]=t3;
            if (t4 != null) indexR[t4.conteudo.chave]=t4;
            if (n1 != null) indexR[n1.conteudo.chave]=n1;
            if (n2 != null) indexR[n2.conteudo.chave]=n2;
            if (n3 != null) indexR[n3.conteudo.chave]=n3;

            n1.saldoAltura = 0;
            if (t2 == null && t3 == null) {
                n2.saldoAltura = 0;
                n3.saldoAltura = 0;
            } else if (deltaY < 0) {
                n2.saldoAltura = 1;
                n3.saldoAltura = 0;
            } else {
                n2.saldoAltura = 0;
                n3.saldoAltura = -1;
            }
        }
    }

    public boolean existe(){
        if (this.raiz != null)
            return true;
        else
            return false;        
    }
    
    public Rodovias buscar(int chave) {
        if (raiz != null)
            return buscar(raiz, chave);
        else
            return null;
    }

    private Rodovias buscar(No no, int chave) {
        if (no.conteudo.chave == chave)
            return no.conteudo;
        else if (no.conteudo.chave > chave && no.direita != null)
            return buscar(no.direita, chave);
        else if (no.conteudo.chave < chave && no.direita != null)
            return buscar(no.esquerda, chave);
        else
            return null;
    }
    public void calcLimitIndexR(int index){
        if (index>maxIndexR) maxIndexR=index;
        if (index<minIndexR) minIndexR=index;
    }
    public void calcMinMax(int fluxo){
        /*if (minFluxo>fluxo){<<< Cálculo do mínimo só pode ser realizado ao final do processamento
            minFluxo=fluxo;
        } */
        if (maxFluxo<fluxo){
            maxFluxo=fluxo;
        }
    }

    public float calcDelta(){
        delta=(maxFluxo-minFluxo)*Float.parseFloat("0.8");
        //delta=((maxFluxo-minFluxo)*8)/10;
        return delta;
    }
    public No getRaiz(){
        return raiz;
    }
    public float calcCorte(){
        corte=minFluxo+delta;
        return corte;
    }
    public float getDelta(){
        return delta;
    }
    public int getMinFluxo(){
        return minFluxo;
    }
    public int getMaxFluxo(){
        return maxFluxo;
    }
    public float getCorte(){
        return corte;
    }            
    public long getqtNos(){
        return qtNos;
    }
// metodos de varredura da arvore ===============
    // Usando o método emOrdem com alterações para gerar o relatório
    public void emOrdem(No NoA) {
        if(NoA != null){
            emOrdem(NoA.direita);
            if (NoA.conteudo.fluxo>=getCorte()){
                if (seta != NoA.conteudo.setor) {
                   seta=NoA.conteudo.setor;
                   lini++;
                   System.out.print(String.format("%05d",lini)+" Setor: "+NoA.conteudo.setor + "(Corte = "+getCorte()+")"+ "------------------------------\n");
                }
                lini++;
                System.out.print(String.format("%05d",lini)+" |  Data: "+NoA.conteudo.data+" Fluxo acc: "+NoA.conteudo.fluxo+"\n");
            }
            emOrdem(NoA.esquerda);
        }
    }
    public void preOrdem(No NoA) {
        if(NoA != null){
           if (NoA.conteudo.fluxo>=getCorte()){
               lini++;
               System.out.print(String.format("%05d",lini)+" Setor: "+NoA.conteudo.setor + " Data: "+NoA.conteudo.data+" Fluxo acc: "+NoA.conteudo.fluxo+"\n");
            }
            preOrdem(NoA.direita);
            preOrdem(NoA.esquerda);
        }
    }
    public void posOrdem(No NoA) {
        if(NoA != null){
            posOrdem(NoA.direita);
            posOrdem(NoA.esquerda);
           if (NoA.conteudo.fluxo>=getCorte()){
                System.out.print("Setor: "+NoA.conteudo.setor + " Data: "+NoA.conteudo.data+" Fluxo acc: "+NoA.conteudo.fluxo+"\n");
            }
         }
    }

    
    //=====================================    
   
}
