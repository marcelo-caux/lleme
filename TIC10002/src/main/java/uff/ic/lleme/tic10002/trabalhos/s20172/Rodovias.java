/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rodovias;

import java.awt.EventQueue;
import java.util.Calendar;

/**
 *
 * @author marcelo
 */
public class Rodovias {
    
        public int chave;
        public int setor;
        public int data;
        public int fluxo;
        public Calendar c = Calendar.getInstance();
        
        public Rodovias(int setor,int data, int fluxo) {
            this.chave=calcChave(setor, data);
            this.setor=setor;
            this.data=data;
            this.fluxo=fluxo;
        }
    
        // Esta função cria a chave a partir de uma regra
        public int calcChave(int setor,int data){
            String dtkey;
            String dt;
            dt=Integer.toString(data);
            /*
            transforma a data tipo int em uma data verdadeira para poder extrair o dia do ano no formato ddd
            detalhe: Janeiro é mes 0 e Dezembro 11,por isso subtrai 1 na posição do mes
            A formação da cheve é Setor+ano(YY)+dia(ddd)
            com esta transformação a chave Setor SS + data YYYYMMDD que usava 10 bytes
            ficou reduzida a SSYYDDD que usa apenas 7 bytes, a única restrição é que só
            posso ter 99 setores e armazenar dados por 100 anos, o que eu considero ACEITÁVEL!
            */
            c.set(Integer.parseInt(dt.substring(0, 4)), Integer.parseInt(dt.substring(4, 6))-1, Integer.parseInt(dt.substring(6, 8)));
            dtkey=Integer.toString(setor)+dt.substring(2, 4)+String.format("%03d",c.get(Calendar.DAY_OF_YEAR));
            return Integer.parseInt(dtkey);
        }
    
// Declara a árvode que armazenará os fluxos e variáveis publicas
    public static ArvoreAVL AvlD;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                                        AvlD=new ArvoreAVL(true); // Inicializa a árvore
           				new RodoviaCtrl();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

    }
    
    
}
