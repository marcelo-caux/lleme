/*
 *
 */
package rodovias;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import java.io.*;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import static rodovias.Rodovias.AvlD;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author marcelo
 */
public class RodoviaCtrl extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtQtdNos;
	private JTextField txtMin;
	private JTextField txtMax;
	private JTextField txtDelta;
	private JTextField txtCorte;
        private JTextField txtSumE;
        private JTextField txtSumD;
        private JTextField txtFname;
	private JLabel lblQtdNos;
	private JLabel lblMin;
        private JLabel lblMax;
        private JLabel lblDelta;
        private JLabel lblCorte;
        private JLabel lblSumE;
        private JLabel lblSumD;
        private JLabel lblStatus;
	private JButton btnImport;
	private JButton btnReport;
        private JButton btnArvore;
        
        public String lPath;
        public long ini;
        public long fin;
        public String msg;
        public ArvoreAVL AvlR;
//        public FuncoesData fData;
        
	public RodoviaCtrl() {
		montaJanela();
		montaComponentes();
		mostraTela();
	}

	public void mostraTela() {
		setVisible(true);
	}

                
	public void montaJanela() {
		setResizable(false);
		setTitle("Sistema Fluxo por Setor");
		//setDefaultCloseOperacion(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 50, 650, 325);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}

	@SuppressWarnings({ "rawtypes", "uncheck"})
	public void montaComponentes() {
		JPanel panelImportar = new JPanel();
		panelImportar.setBounds(10, 11, 630, 285);
		panelImportar.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Opera\u00e7\u00f5es", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panelImportar);
		panelImportar.setLayout(null);
                
                // botao importar ==============================================
		JButton btnImport = new JButton("Importar arquivo");
		btnImport.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                            try {
                                CarregaArquivo();
                                //btnCalcular_2.setEnabled(true);
                            } catch (IOException ex) {
                                Logger.getLogger(RodoviaCtrl.class.getName()).log(Level.SEVERE, null, ex);
                            }
			
			}
		});
		btnImport.setBounds(10, 23, 197, 23);
                panelImportar.add(btnImport);
                // botao Relatório ==============================================
		JButton btnReport = new JButton("Relat\u00F3rio");
		btnReport.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GeraRelatorio();
			}
		});
		btnReport.setBounds(10, 103, 197, 23);
		panelImportar.add(btnReport);
              // botao Lista Arvore ==============================================
		JButton btnArvore = new JButton("Mostra \u00c1rvore e indexR");
		btnArvore.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnArvore.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        MousePointer("W");
                        ini=System.currentTimeMillis();
                        AvlD.print();
                        fin=System.currentTimeMillis();
                        msg="Lista Arvore: "+(fin-ini)+" ms / lista index:";
                        ini=System.currentTimeMillis();
                        AvlD.printIndex();
                        fin=System.currentTimeMillis();
                        msg=msg+(fin-ini)+ " ms";
                        System.out.println(msg);
                        MousePointer("D");
                    }
		});
		btnArvore.setBounds(10, 143, 197, 23);
		panelImportar.add(btnArvore);
              // botao Sair ==============================================
		JButton btnSair = new JButton("Encerrar");
		btnSair.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnSair.setBounds(10, 183, 197, 23);
		panelImportar.add(btnSair);
                
                // lable Nós importados ========================================
		JLabel lblQtdNos = new JLabel("N\u00f3s");
		lblQtdNos.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblQtdNos.setBounds(213, 10, 35, 14);
		panelImportar.add(lblQtdNos);
                // text Nós importados
                txtQtdNos = new JTextField();
    		txtQtdNos.setBounds(210, 23, 70, 20);
		panelImportar.add(txtQtdNos);
		txtQtdNos.setColumns(10);
		txtQtdNos.setText(String.valueOf(0));
                // lable Min ========================================
		JLabel lblMin = new JLabel("M\u00EDnimo");
		lblMin.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblMin.setBounds(283, 10, 35, 14);
		panelImportar.add(lblMin);
                // text Min
                txtMin = new JTextField();
    		txtMin.setBounds(280, 23, 70, 20);
		panelImportar.add(txtMin);
		txtMin.setColumns(10);
		txtMin.setText(String.valueOf(0));
                // lable Max ========================================
		JLabel lblMax = new JLabel("M\u00E1ximo");
		lblMax.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblMax.setBounds(353, 10, 35, 14);
		panelImportar.add(lblMax);
                // text Maximo
                txtMax = new JTextField();
    		txtMax.setBounds(350, 23, 70, 20);
		panelImportar.add(txtMax);
		txtMax.setColumns(10);
		txtMax.setText(String.valueOf(0));
                // lable Delta ========================================
		JLabel lblDelta = new JLabel("Delta");
		lblDelta.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblDelta.setBounds(423, 10, 35, 14);
		panelImportar.add(lblDelta);
                // text Delta
                txtDelta = new JTextField();
    		txtDelta.setBounds(420, 23, 90, 20);
		panelImportar.add(txtDelta);
		txtDelta.setColumns(10);
		txtDelta.setText(String.valueOf(0));
              // lable Corte ========================================
		JLabel lblCorte = new JLabel("Corte");
		lblCorte.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblCorte.setBounds(513, 10, 35, 14);
		panelImportar.add(lblCorte);
                // text lblCorte
                txtCorte = new JTextField();
    		txtCorte.setBounds(510, 23, 90, 20);
		panelImportar.add(txtCorte);
		txtCorte.setColumns(10);
		txtCorte.setText(String.valueOf(0));
                
                // lable File name ========================================
		JLabel lblFname = new JLabel("Arquivo a importar");
		lblFname.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblFname.setBounds(10, 63, 85, 14);
		panelImportar.add(lblFname);
                // text File name 
                txtFname = new JTextField();
    		txtFname.setBounds(105, 63, 450, 20);
		panelImportar.add(txtFname);
		//txtFname.setColumns(10);
		//txtFname.setText(String.valueOf(0));
                // lable tempo de carga de cada arquivo ========================================
		//JLabel lblSumE = new JLabel("Sum\u00E1rio Esquerda");
                JLabel lblSumE = new JLabel("T carga do arquivo (ms)");
		lblSumE.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblSumE.setBounds(313, 90, 120, 14);
		panelImportar.add(lblSumE);
                // text Nós Esquerdos percorridos ===============================
                txtSumE = new JTextField();
    		txtSumE.setBounds(310, 103, 100, 20);
		panelImportar.add(txtSumE);
		txtSumE.setColumns(10);
		txtSumE.setText(String.valueOf(0));
                
                // lable tempo de geração do relatório ========================================
		JLabel lblSumD = new JLabel("T gerar relatório (ms)");
		lblSumD.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblSumD.setBounds(453, 90, 100, 14);
		panelImportar.add(lblSumD);
                // text Nós direitos percorridos ===============================
                txtSumD = new JTextField();
    		txtSumD.setBounds(450, 103, 100, 20);
		panelImportar.add(txtSumD);
		txtSumD.setColumns(10);
		txtSumD.setText(String.valueOf(0));
                // lable Status ========================================
		lblStatus = new JLabel("");
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblStatus.setBounds(10, 220, 610, 42);
		panelImportar.add(lblStatus);
                lblStatus.setBorder(new TitledBorder(UIManager.getBorder("TitledBBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
                lPath=new File("").getAbsolutePath();
                //lPath= System.getProperty("user.home");
                //lblStatus.setText(lPath);
                lblStatus.setText("Path atual: "+ lPath);
                
                //===================================================
	}
// ======  Código associado aos botoes  ========================================        
        private void CarregaArquivo() throws IOException {
            //String fName="SSD480MDC-MBP"+lPath+txtFname.getText();
            String fName=txtFname.getText();
            Rodovias RLida;
            if (fName==null) {
                JOptionPane.showMessageDialog(null,"Digite um nome de arquivo para importar");
            } else {
                MousePointer("W");
                ini=System.currentTimeMillis();
                FileArrayReader fr = new FileArrayReader();
                String[] data = fr.readLines(fName);
                for (String line : data) {
                  try {
                  //System.out.println("===============================================================");
                  //System.out.println("Processando a linha " + line);
                  String linha[] = line.split(";");
                  RLida = new Rodovias(Integer.parseInt(linha[0]),Integer.parseInt(linha[2]),Integer.parseInt(linha[3]));
                  AvlD.inserir(RLida);
                  }
                  //catch(IOException e)
                  //{}
                  finally 
                  {}
                }
                  fin=System.currentTimeMillis()-ini;
                  txtSumE.setText(Long.toString(fin));
                  ini=System.currentTimeMillis();
                  AvlD.minFluxo=1000000000;
                  AvlD.CalcMin(AvlD.getRaiz());
                  fin=System.currentTimeMillis()-ini;
                  lblStatus.setText("Tempo de calculo de Min: "+ fin+" ms");
                  txtQtdNos.setText(Long.toString(AvlD.getqtNos()));
                  txtMin.setText(Integer.toString(AvlD.getMinFluxo()));
                  txtMax.setText(Integer.toString(AvlD.getMaxFluxo()));
                  txtDelta.setText(Float.toString(AvlD.calcDelta()));
                  txtCorte.setText(Float.toString(AvlD.calcCorte()));
                  MousePointer("D");
                  JOptionPane.showMessageDialog(null,"Carga encerrada");
            }
        } // Final CarregaArquivo()     
        
        private void GeraRelatorio(){
            //Cursor cursor = Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR );
            //contentPane.setCursor( cursor );
            MousePointer("W");
            // lista na ordem
            System.out.println("\n\n"+"Relatório de Fluxo "+hojeHMS()+ " =======================================");
            ini=System.currentTimeMillis();
            AvlD.emOrdem(AvlD.getRaiz());
            fin=System.currentTimeMillis(); //HAND_CURSOR
            MousePointer("H");
            //cursor = Cursor.getPredefinedCursor( Cursor.HAND_CURSOR );  
            //contentPane.setCursor( cursor ); 
            txtSumD.setText(String.valueOf(fin-ini));
            msg="Lista Arvore em ordem: "+(fin-ini)+" ms";
            System.out.println("\n"+msg);
            System.out.println("\n ===================  FIM "+hojeHMS()+ " =====================");
            MousePointer("D");
            //cursor = Cursor.getDefaultCursor();  
            //contentPane.setCursor( cursor ); 
        }
        
        private void MousePointer(String tipo){
            Cursor cursor;
            cursor = Cursor.getDefaultCursor();
            switch (tipo){
            case "W":
              cursor = Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR );
              break;
            case "H":
              cursor = Cursor.getPredefinedCursor( Cursor.HAND_CURSOR );
              break;
            case "D":
              cursor = Cursor.getDefaultCursor();
              break;
            case "C":
              //criaXXXXXX();
              break;                                                      
            default:
              cursor = Cursor.getDefaultCursor();
              break;               
            }
            contentPane.setCursor(cursor);
        }
    public String hojeHMS(){
        Date hoje = new Date();
        SimpleDateFormat df;
        df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return df.format(hoje);
    }

        
}
